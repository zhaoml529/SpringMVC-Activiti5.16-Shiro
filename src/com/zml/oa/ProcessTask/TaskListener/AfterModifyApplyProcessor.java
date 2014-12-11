package com.zml.oa.ProcessTask.TaskListener;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.zml.oa.entity.SalaryAdjust;
import com.zml.oa.service.ISalaryAdjustService;


/**
 * 演示任务监听
 * @author ZML
 *
 */
@Component
@Transactional
public class AfterModifyApplyProcessor implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5975676580192909075L;
	private static final Logger logger = Logger.getLogger(AfterModifyApplyProcessor.class);
	
	@Autowired
	private ISalaryAdjustService salaryService;
	
	@Autowired
    RuntimeService runtimeService;
	
	@Override
	public void notify(DelegateTask delegateTask){
		String processInstanceId = delegateTask.getProcessInstanceId();
        ProcessInstance processInstance = this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        String businessKey = processInstance.getBusinessKey();
		try {
			SalaryAdjust salary = this.salaryService.findById(new Integer(businessKey));
//			delegateTask.getVariable("entity");
			SalaryAdjust salaryAdjust = (SalaryAdjust) this.runtimeService.getVariable(processInstanceId, "entity");
			salary.setAdjustMoney(salaryAdjust.getAdjustMoney());
			salary.setUserId(salaryAdjust.getUserId());
			salary.setDscp(salaryAdjust.getDscp());
			this.salaryService.doUpdate(salary);
			logger.info("薪资修改完成！");
		} catch (Exception e) {
			logger.error("修改薪资调整失败：", e);
		}
	}

}
