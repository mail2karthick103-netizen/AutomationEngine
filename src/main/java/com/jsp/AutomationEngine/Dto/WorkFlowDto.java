package com.jsp.AutomationEngine.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorkFlowDto {

        private String workflowCode;
        private String workflowName;
        private String tenantId;
        private String uniqueField;
        private String sourceData;
        private String entityCode;


}
