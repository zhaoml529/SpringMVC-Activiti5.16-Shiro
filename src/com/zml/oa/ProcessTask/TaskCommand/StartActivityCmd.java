package com.zml.oa.ProcessTask.TaskCommand;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.runtime.AtomicOperation;

public class StartActivityCmd implements Command<java.lang.Void> {

	private ActivityImpl _activity;

	private String _executionId;

	public StartActivityCmd(String executionId, ActivityImpl activity)
	{
		this._activity = activity;
		this._executionId = executionId;
	}
	
	@Override
	public Void execute(CommandContext commandContext) {
		ExecutionEntity execution = commandContext.getExecutionEntityManager().findExecutionById(this._executionId);
		execution.setActivity(this._activity);

		execution.performOperation(AtomicOperation.ACTIVITY_START);
		return null;
	}

}
