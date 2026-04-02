package com.jsp.AutomationEngine.Controller;

import com.jsp.AutomationEngine.Dto.AppResponseDto;
import com.jsp.AutomationEngine.Dto.WorkFlowDto;
import com.jsp.AutomationEngine.Service.WorkFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WorkFlowController {

    @Autowired
    private WorkFlowService workFlowService;

    @PostMapping(value = "/saveWorkFlow")
    public @ResponseBody AppResponseDto saveWorkFlow(@RequestBody List<WorkFlowDto> workFlowDto){
        return   workFlowService.processSaveWorkFlow(workFlowDto);
    }

    @PostMapping(value = "/saveOrUpdateDraft")
    public @ResponseBody AppResponseDto saveOrUpdateDraft(@RequestBody List<WorkFlowDto> workFlowDto){
        return   workFlowService.processSaveOrUpdateDraft(workFlowDto);
    }

    @PostMapping(value = "/activateWorkFlow")
    public  AppResponseDto activateWorkFlow(@RequestBody WorkFlowDto workFlow){
        return workFlowService.processActivateWorkflow(workFlow);
    }

}
