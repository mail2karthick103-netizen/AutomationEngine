package com.jsp.AutomationEngine.Model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Entity
@Table(name = "sa_wf_transaction")
@AllArgsConstructor
@NoArgsConstructor
public class WorkFlowTransactionEntity  extends  WorkFlowData{
    @Id
    @Column(name = "alt_key")
    private BigInteger altKey = generateAltKey();
    @Column(name = "transaction_id")
    private String transactionId;
    @Column(name = "transaction_unique_value")
    private String transactionUniqueValue;
    @Column(name = "workflow_id")
    private String workflowId;
    @Column(name = "workflow_code")
    private String workflowCode;
    @Column(name = "tenant_id")
    private String tenantId;
    @Column(name = "status_flag")
    private String statusFlag;
    @Column(name = "transaction_start_date")
    private Date transactionStartDate;
    @Column(name = "transaction_end_date")
    private Date transactionEndDate;
    @Column(name = "remarks")
    private String remarks;


    private BigInteger generateAltKey() {
        long value = Math.abs(ThreadLocalRandom.current().nextLong());
        return BigInteger.valueOf(value);
    }
}
