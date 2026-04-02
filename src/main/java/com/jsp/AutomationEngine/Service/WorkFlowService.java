package com.jsp.AutomationEngine.Service;

import com.jsp.AutomationEngine.Dto.AppResponseDto;
import com.jsp.AutomationEngine.Dto.WorkFlowDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkFlowService {
     AppResponseDto processSaveWorkFlow(List<WorkFlowDto> workFlowDtoList);
     AppResponseDto processSaveOrUpdateDraft(List<WorkFlowDto> dtoList);
     AppResponseDto processActivateWorkflow(WorkFlowDto workflow);
}
