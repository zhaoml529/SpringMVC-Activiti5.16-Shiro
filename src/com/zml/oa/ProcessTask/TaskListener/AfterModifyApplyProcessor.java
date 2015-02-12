package com.zml.oa.ProcessTask.TaskListener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zml.oa.entity.SalaryAdjust;
import com.zml.oa.service.ISalaryAdjustService;


/**
 * 演示任务监听
 * @author ZML
 *
 */
@Component("afterModifyApplyProcessor")
public class AfterModifyApplyProcessor implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5975676580192909075L;
	private static final Logger logger = Logger.getLogger(AfterModifyApplyProcessor.class);
	
	@Autowired
	private ISalaryAdjustService salaryService;
	
	private Expression businessKey;

	public void setBusinessKey(Expression businessKey) {
		this.businessKey = businessKey;
	}



	@Override
	public void notify(DelegateTask delegateTask){
		//如果值是String类型，参数也可以为null
		Integer salaryId = (Integer) businessKey.getValue(delegateTask);
		String processInstanceId = delegateTask.getProcessInstanceId();
		try {
			SalaryAdjust salary = this.salaryService.findById(new Integer(salaryId));
			SalaryAdjust salaryAdjust = (SalaryAdjust) delegateTask.getVariable("entity");
	        salary.setAdjustMoney(salaryAdjust.getAdjustMoney());
			salary.setUserId(salaryAdjust.getUserId());
			salary.setDscp(salaryAdjust.getDscp());
			salary.setProcessInstanceId(processInstanceId);
			this.salaryService.doUpdate(salary);
			logger.info("薪资修改完成！");
		} catch (Exception e) {
			logger.error("薪资修改失败！");
			e.printStackTrace();
		}
	}

}
