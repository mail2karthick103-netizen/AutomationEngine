package com.jsp.AutomationEngine.Repository;

import com.jsp.AutomationEngine.Model.WorkFlowTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface TransactionRepository extends JpaRepository<WorkFlowTransactionEntity, BigInteger> {

}
