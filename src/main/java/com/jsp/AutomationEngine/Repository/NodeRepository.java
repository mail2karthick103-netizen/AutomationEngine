package com.jsp.AutomationEngine.Repository;

import com.jsp.AutomationEngine.Model.NodeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface NodeRepository extends JpaRepository<NodeModel, BigInteger> {

List<NodeModel> findByWorkflowIdAndTenantId(String wfId, String tententId);
}
