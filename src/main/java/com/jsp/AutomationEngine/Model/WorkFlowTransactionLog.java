package com.jsp.AutomationEngine.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Entity
@Table(name = "sa_wf_transaction_Log")
public class WorkFlowTransactionLog extends WorkFlowData {

    @Id
    @Column(name = "alt_key")
    private BigInteger altKey = generateAltKey();
    @Column(name = "transaction_id")
    private String transactionId;
    @Column(name = "transaction_unique_value")
    private String transactionUniqueValue;
    @Column(name = "tenant_id")
    private String tenantId;
    @Column(name = "workflow_id")
    private String workflowId;
    @Column(name = "node_id")
    private String nodeId;
    @Column(name = "node_type")
    private String nodeType;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "status_flag")
    private String statusFlag;
    @Column(name = "trigger_date")
    private Date triggerDate;
    @Column(name = "previous_nodeId")
    private String previousNodeId;
    @Column(name = "transaction_start_date")
    private Date transactionStartDate;
    @Column(name = "transaction_end_date")
    private Date transactionEndDate;


    private BigInteger generateAltKey() {
        long value = Math.abs(ThreadLocalRandom.current().nextLong());
        return BigInteger.valueOf(value);
    }

}
