package com.jsp.AutomationEngine.Repository;

import com.jsp.AutomationEngine.Model.WorkFlowTransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface TransactionLogRepository extends JpaRepository<WorkFlowTransactionLog, BigInteger> {

}
