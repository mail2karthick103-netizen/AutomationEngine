package com.jsp.AutomationEngine.Repository;

import com.jsp.AutomationEngine.Model.WorkFlowModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkFlowRepository extends JpaRepository<WorkFlowModel, BigInteger> {

    Optional<WorkFlowModel> findAllByWorkflowCodeInAndStatusFlag(List<String> workflowCode, String statusFlag);

    Optional<WorkFlowModel> findByWorkflowCodeAndStatusFlag(String workflowCode, String statusFlag);

    @Query("SELECT MAX(w.workflowVersion) FROM WorkFlowModel w WHERE w.workflowCode = :ver")
    Integer maxValue(@Param("ver") String ver);

    List<WorkFlowModel> findByWorkflowCode(String workflowCode);
}
