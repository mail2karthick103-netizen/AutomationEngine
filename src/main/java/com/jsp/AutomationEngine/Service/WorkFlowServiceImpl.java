package com.jsp.AutomationEngine.Service;

import com.jsp.AutomationEngine.Dto.AppResponseDto;
import com.jsp.AutomationEngine.Dto.WorkFlowDto;
import com.jsp.AutomationEngine.Model.NodeModel;
import com.jsp.AutomationEngine.Model.WorkFlowModel;
import com.jsp.AutomationEngine.Repository.NodeRepository;
import com.jsp.AutomationEngine.Repository.WorkFlowRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkFlowServiceImpl implements WorkFlowService {

    @Autowired
    private WorkFlowRepository workFlowRepository;
    @Autowired
    private NodeRepository nodeRepository;


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
                            .findAllByWorkflowCodeInAndStatusFlag(workflowcode, "Draft")
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
        model.setStatusFlag("Draft");
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
            WorkFlowModel draft = workFlowRepository.findByWorkflowCodeAndStatusFlag(workflow.getWorkflowCode(), "Draft")
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

            List<NodeModel> nodeList = getNodeList(draft.getSourceData());
            nodeRepository.saveAll(nodeList);



            return success(workFlowRepository.saveAll(list));
        } catch (Exception err) {
            return   error(err);
        }
    }

    public List<NodeModel> getNodeList(String xml){
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            BPMNHandler handler = new BPMNHandler();
            saxParser.parse(new InputSource(new StringReader(xml)), handler);

            return handler.getNodes();

        } catch (Exception e) {
            throw new RuntimeException("Error parsing XML", e);
        }

    }


    private AppResponseDto success(List<WorkFlowModel> data) {
        return new AppResponseDto("200", null, "SUCCESS", data);
    }

    private AppResponseDto error(Exception ex) {
        return new AppResponseDto("500", "ERROR", ex.getMessage(), null);
    }
}


