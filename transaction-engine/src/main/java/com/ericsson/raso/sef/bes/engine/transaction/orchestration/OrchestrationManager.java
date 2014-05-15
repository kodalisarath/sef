package com.ericsson.raso.sef.bes.engine.transaction.orchestration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.ericsson.raso.sef.bes.engine.transaction.Constants;
import com.ericsson.raso.sef.bes.engine.transaction.TransactionException;
import com.ericsson.raso.sef.bes.engine.transaction.commands.AbstractTransaction;
import com.ericsson.raso.sef.bes.engine.transaction.entities.AbstractResponse;
import com.ericsson.raso.sef.bes.prodcat.tasks.TransactionTask;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;

public class OrchestrationManager {

	ExecutorService grinder = SefCoreServiceResolver.getExecutorService(Constants.ORCHESTRATION.name());
	private Map<AbstractTransaction, Orchestration> transactionStore =  SefCoreServiceResolver.getCloudAwareCluster().getMap(Constants.ORCHESTRATION.name());
	private Map<AbstractTransaction, Future<AbstractResponse>> resultStore =  SefCoreServiceResolver.getCloudAwareCluster().getMap(Constants.TRANSACTION_STATUS.name());
	
	private static OrchestrationManager instance = null;
	
	public static synchronized OrchestrationManager getInstance() {
		if (instance == null)
			instance = new OrchestrationManager();
		return instance;
	}
	
	
	public Orchestration createExecutionProfile(String northBoundCorrelator, List<TransactionTask> tasks) throws TransactionException {
		Orchestration result = new Orchestration(northBoundCorrelator, tasks);
		
		
		return result;
	}


	public void submit(AbstractTransaction usecase, Orchestration execution) {
		this.transactionStore.put(usecase, execution);
		Future<AbstractResponse> orchestrationStatus = this.grinder.submit(execution);
		
	}
}
