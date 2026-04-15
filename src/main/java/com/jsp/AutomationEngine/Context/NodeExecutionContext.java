package com.jsp.AutomationEngine.Context;

import com.jsp.AutomationEngine.Model.NodeConfig;
import com.jsp.AutomationEngine.Model.WorkFlowModel;
import com.jsp.AutomationEngine.Model.WorkFlowTransactionEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class NodeExecutionContext {

    private Date executionStart;
    private Date executionEnd;
    private String executionStatus;
    private NodeConfig currentNodeConfig;
    private NodeConfig previousExecutedConfig;
    private List<NodeConfig> nextExecutionNodeConfig;
    private WorkFlowModel workFlowModel;
    private WorkFlowTransactionEntity workFlowTransactionModel;
    private Map<String, Object> transactionDataMap;
    private NodeExecutionResult nodeExecutionResult;

    public NodeExecutionContext(NodeExecutionContext oldValue) {
        this.executionStart = oldValue.executionStart;
        this.executionEnd = oldValue.executionEnd;
        this.executionStatus = oldValue.executionStatus;
        this.currentNodeConfig = oldValue.currentNodeConfig;
        this.previousExecutedConfig = oldValue.previousExecutedConfig;
        this.nextExecutionNodeConfig = oldValue.nextExecutionNodeConfig;
        this.workFlowModel = oldValue.workFlowModel;
        this.workFlowTransactionModel = oldValue.workFlowTransactionModel;
        this.transactionDataMap = oldValue.transactionDataMap;
        this.nodeExecutionResult = oldValue.nodeExecutionResult;
    }


}
