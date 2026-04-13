package com.jsp.AutomationEngine.Model;


import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class NodeExecutionResult {

    private Boolean executionResult;
    private String executionStatus;
    private String remarks;

}
