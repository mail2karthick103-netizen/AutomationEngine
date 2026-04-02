package com.jsp.AutomationEngine.Service;

import com.jsp.AutomationEngine.Dto.AppResponseDto;
import com.jsp.AutomationEngine.Dto.WorkFlowDto;
import com.jsp.AutomationEngine.Model.WorkFlowModel;
import com.jsp.AutomationEngine.Repository.WorkFlowRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkFlowServiceImpl implements WorkFlowService {

    @Autowired
    private WorkFlowRepository workFlowRepository;

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
        return model;
    }

    @Override
    public AppResponseDto processActivateWorkflow(WorkFlowDto workflow) {
        try {
            WorkFlowModel draft = workFlowRepository.findByWorkflowCodeAndStatusFlag(workflow.getWorkflowCode(), "Draft")
                    .orElseThrow(() -> new RuntimeException("draft not found"));

            List<WorkFlowModel> list = workFlowRepository.findByWorkflowCode(workflow.getWorkflowCode());

            Integer maxVersion = workFlowRepository.maxValue(workflow.getWorkflowCode());

            for (WorkFlowModel wf : list) {
                if ("ACTIVE".equals(wf.getStatusFlag()))
                    wf.setStatusFlag("INACTIVE");
            }

            WorkFlowModel newVersion = new WorkFlowModel();
            newVersion.setWorkflowCode(workflow.getWorkflowCode());
            newVersion.setWorkflowVersion(maxVersion + 1);
            newVersion.setStatusFlag("ACTIVE");
            newVersion.setWorkflowId(workflow.getWorkflowCode() + "_" + (maxVersion + 1));

            newVersion.setSourceData(draft.getSourceData());
            newVersion.setTenantId(draft.getTenantId());
            newVersion.setUniqueField(draft.getUniqueField());
            newVersion.setEntityCode(draft.getEntityCode());
            newVersion.setWorkflowName(draft.getWorkflowName());
            list.add(newVersion);



            return success(workFlowRepository.saveAll(list));
        } catch (Exception err) {
            return   error(err);
        }
    }




    private AppResponseDto success(List<WorkFlowModel> data) {
        return new AppResponseDto("200", null, "SUCCESS", data);
    }

    private AppResponseDto error(Exception ex) {
        return new AppResponseDto("500", "ERROR", ex.getMessage(), null);
    }
}


