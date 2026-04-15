package com.jsp.AutomationEngine.Repository;

import com.jsp.AutomationEngine.Model.NodeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface NodeConfigRepository extends JpaRepository<NodeConfig, BigInteger> {

    NodeConfig findByWorkflowIdAndIsStartNodeTrue(String workflowId);
    List<NodeConfig> findByWorkflowIdAndIsStartNodeFalse(String workflowId);

}
