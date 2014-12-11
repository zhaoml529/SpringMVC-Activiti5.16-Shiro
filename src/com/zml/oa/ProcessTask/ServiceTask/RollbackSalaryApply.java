package com.zml.oa.ProcessTask.ServiceTask;

import java.math.BigDecimal;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import com.zml.oa.entity.Salary;
import com.zml.oa.entity.SalaryAdjust;
import com.zml.oa.service.ISalaryService;

/**
 * 回滚薪资调整
 * @author ZML
 *
 */
public class RollbackSalaryApply implements JavaDelegate {

    @Autowired
    protected RuntimeService runtimeService;
    
	@Autowired
	private ISalaryService salaryService;
    
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		SalaryAdjust salaryAdjust = (SalaryAdjust)this.runtimeService.getVariable(execution.getProcessInstanceId(), "entity");
    	BigDecimal baseMoney = (BigDecimal) this.runtimeService.getVariable(execution.getProcessInstanceId(), "baseMoney");
		Salary salary = this.salaryService.findByUserId(salaryAdjust.getUserId().toString());
		salary.setBaseMoney(baseMoney);
		this.salaryService.doUpdate(salary);
	}


}
