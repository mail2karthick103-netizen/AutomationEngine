package com.jsp.AutomationEngine.Context;

import com.jsp.AutomationEngine.Model.NodeConfig;
import com.jsp.AutomationEngine.Model.WorkFlowModel;
import com.jsp.AutomationEngine.Model.WorkFlowTransactionEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class WorkFlowTransactionContext {


    private WorkFlowModel workFlowModel;
    private WorkFlowTransactionEntity workFlowTransactionModel;
    private NodeConfig nextNodeConfig;
    private Map<String, NodeExecutionContext> currentNodeExecutionContextMap;
    private Date executionStart;
    private Date executionEnd;
    private String remarks;
    private String executionStatus;
    private String tenantId;

    public void setCurrentNodeExecutionContext(String nodeId, NodeExecutionContext nodeContext) {

        if (this.currentNodeExecutionContextMap == null) {
            this.currentNodeExecutionContextMap = new HashMap<>();
        }

        this.currentNodeExecutionContextMap.put(nodeId, nodeContext);
    }

    public WorkFlowTransactionContext cloneContext() {

        WorkFlowTransactionContext copy = new WorkFlowTransactionContext();

        copy.setWorkFlowModel(this.workFlowModel);
        copy.setWorkFlowTransactionModel(this.workFlowTransactionModel);
        copy.setNextNodeConfig(this.nextNodeConfig);
        copy.setExecutionStart(this.executionStart);
        copy.setExecutionEnd(this.executionEnd);
        copy.setExecutionStatus(this.executionStatus);
        copy.setTenantId(this.tenantId);
        copy.setRemarks(this.remarks);

        if (this.currentNodeExecutionContextMap != null) {
           Map<String, NodeExecutionContext> newMap = new HashMap<>();

            for (String key : this.currentNodeExecutionContextMap.keySet()) {

                NodeExecutionContext oldValue =
                        this.currentNodeExecutionContextMap.get(key);

                NodeExecutionContext newValue =
                        (oldValue == null) ? null : new NodeExecutionContext(oldValue);

                newMap.put(key, newValue);
            }

            copy.setCurrentNodeExecutionContextMap(newMap);
        }

        return copy;
    }
}



