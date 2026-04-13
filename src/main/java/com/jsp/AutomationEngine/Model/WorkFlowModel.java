package com.jsp.AutomationEngine.Model;

import com.jsp.AutomationEngine.Dto.WorkFlowDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Entity
@Table(name = "workflow_master")
@NoArgsConstructor
@AllArgsConstructor
public class WorkFlowModel extends WorkFlowData {


        @Id
        @Column(name = "alt_key")
        private BigInteger altKey = generateAltKey();

        @Column(name = "workflow_version")
        private int workflowVersion = 0;

        @Column(name = "tenant_id")
        private String tenantId;

        @Column(name = "workflow_id")
        private String workflowId;

        @Column(name = "workflow_code")
        private String workflowCode;

        @Column(name = "workflow_name")
        private String workflowName;

        @Column(name = "status_flag")
        private String statusFlag = "DRAFT";

        @Column(name = "unique_field")
        private String uniqueField;

        @Column(name = "source_data", columnDefinition = "LONGTEXT")
        private String sourceData;

        @Column(name = "entity_code")
        private String entityCode;

        @Transient
        private List<NodeModel>  nodeProperties;




public WorkFlowModel (WorkFlowDto workFlowDto)
{
this.workflowCode = workFlowDto.getWorkflowCode();
this.workflowName =workFlowDto.getWorkflowName();
this.tenantId = workFlowDto.getTenantId();
this.uniqueField = workFlowDto.getUniqueField();
this.sourceData = workFlowDto.getSourceData();
this.entityCode = workFlowDto.getEntityCode();
}
        private BigInteger generateAltKey() {
                long value = Math.abs(ThreadLocalRandom.current().nextLong());
                return BigInteger.valueOf(value);
        }
}

