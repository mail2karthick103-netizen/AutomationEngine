package com.jsp.AutomationEngine.Service;

import com.jsp.AutomationEngine.Dto.AppResponseDto;
import com.jsp.AutomationEngine.Dto.WorkFlowDto;
import com.jsp.AutomationEngine.Model.NodeConfig;
import com.jsp.AutomationEngine.Model.NodeModel;
import com.jsp.AutomationEngine.Model.WorkFlowModel;
import com.jsp.AutomationEngine.Repository.NodeConfigRepository;
import com.jsp.AutomationEngine.Repository.NodeRepository;
import com.jsp.AutomationEngine.Repository.WorkFlowRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkFlowServiceImpl implements WorkFlowService {

    Logger logger = LoggerFactory.getLogger(WorkFlowServiceImpl.class);
    @Autowired
    private WorkFlowRepository workFlowRepository;
    @Autowired
    private NodeRepository nodeRepository;
    @Autowired
    private NodeConfigRepository nodeConfigRepository;
    @Autowired
    private NodeConfigBuilder nodeConfigBuilder;

    // Save Method
    @Override
    public AppResponseDto processSaveWorkFlow(List<WorkFlowDto> workFlowDtoList) {
        try {
            List<WorkFlowModel> entities = workFlowDtoList.stream()
                    .map(this::createDraft)
                    .toList();

            return success(workFlowRepository.saveAll(entities));
        } catch (Exception ex) {
            return error(ex);
        }
    }



    @Override
    public AppResponseDto processSaveOrUpdateDraft(List<WorkFlowDto> dtoList) {
        try {
            //Collect workflow code
            List<String> workflowcode = dtoList.stream()
                    .map(WorkFlowDto::getWorkflowCode)
                    .toList();
            // Fetch existing drafts
            Map<String, WorkFlowModel> draftMap =
                    workFlowRepository
                            .findAllByWorkflowCodeInAndStatusFlag(workflowcode, "DRAFT")
                            .stream()
                            .collect(Collectors.toMap(WorkFlowModel::getWorkflowCode,
                                    model -> model));

            //check condition
            List<WorkFlowModel> entities = dtoList.stream()
                    .map(dto -> {
                        WorkFlowModel existing = draftMap.get(dto.getWorkflowCode());
                        return (existing != null) ? updateDraft(existing, dto) : createDraft(dto);
                    }).toList();
            return success(workFlowRepository.saveAll(entities));

        } catch (Exception ex) {
            return error(ex);
        }
    }

    //for Create new record
    private WorkFlowModel createDraft(WorkFlowDto dto) {
        WorkFlowModel model = new WorkFlowModel(dto);
        model.setStatusFlag("DRAFT");
        model.setWorkflowVersion(0);
        model.setWorkflowId(dto.getWorkflowCode() + "_0");
        return model;
    }

    //update values
    private WorkFlowModel updateDraft(WorkFlowModel model, WorkFlowDto dto) {
        model.setSourceData(dto.getSourceData());
        model.setTenantId(dto.getTenantId());
        model.setUniqueField(dto.getUniqueField());
        model.setEntityCode(dto.getEntityCode());
        model.setWorkflowName(dto.getWorkflowName());
        return model;
    }

    @Override
    public AppResponseDto processActivateWorkflow(WorkFlowDto workflow) {
        try {
            WorkFlowModel draft = workFlowRepository.findByWorkflowCodeAndStatusFlag(workflow.getWorkflowCode(), "DRAFT")
                    .orElseThrow(() -> new RuntimeException("draft not found"));

            List<WorkFlowModel> list = workFlowRepository.findByWorkflowCode(workflow.getWorkflowCode());

            for (WorkFlowModel wf : list) {
                if ("ACTIVE".equals(wf.getStatusFlag()))
                    wf.setStatusFlag("INACTIVE");
            }

            Integer maxVersion = workFlowRepository.maxValue(workflow.getWorkflowCode());

            draft.setStatusFlag("ACTIVE");
            draft.setWorkflowVersion(maxVersion + 1);
            draft.setWorkflowId(draft.getWorkflowCode() + "_" + (maxVersion + 1));

            List<NodeModel> nodeList =
                    parse(draft.getSourceData(), draft.getWorkflowId(), draft.getTenantId());

            list.add(draft);
            workFlowRepository.saveAll(list);

            nodeRepository.saveAll(nodeList);

            WorkFlowModel flowModel = getWorkflowbyIdandtId(draft.getWorkflowId(), draft.getTenantId());
            List<NodeConfig> nodeConf = nodeConfigBuilder.getNodeConfig(flowModel.getNodeProperties());
            nodeConfigRepository.saveAll(nodeConf);

            return success(list);
        } catch (Exception err) {
            return   error(err);
        }
    }

    public List<NodeModel> parse(String xml, String wfCode, String tenantId) {

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            BPMNHandler handler = new BPMNHandler(wfCode, tenantId);

            InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            parser.parse(is, handler);
            return handler.getNodes();

        } catch (Exception e) {
            throw new RuntimeException("Error parsing XML", e);
        }
    }

    public WorkFlowModel getWorkflowbyIdandtId(String wfId, String tID) {
        WorkFlowModel byWorkflowIDAndTenantID = workFlowRepository.findByWorkflowIdAndTenantId(wfId, tID);
        List<NodeModel> nodeModelList = nodeRepository.findByWorkflowIdAndTenantId(wfId, tID);
        byWorkflowIDAndTenantID.setNodeProperties(nodeModelList);
        return byWorkflowIDAndTenantID;
    }

    private AppResponseDto success(List<WorkFlowModel> data) {
        return new AppResponseDto("200", null, "SUCCESS", data);
    }

    private AppResponseDto error(Exception ex) {
        return new AppResponseDto("500", "ERROR", ex.getMessage(), null);
    }
}


