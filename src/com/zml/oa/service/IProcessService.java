package com.zml.oa.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;

import com.zml.oa.entity.BaseVO;
import com.zml.oa.entity.CommentVO;
import com.zml.oa.entity.ExpenseAccount;
import com.zml.oa.entity.SalaryAdjust;
import com.zml.oa.entity.User;
import com.zml.oa.entity.Vacation;
import com.zml.oa.pagination.Page;

public interface IProcessService {
	
	/**
	 * 启动薪资调整
	 * @param user
	 * @param salary
	 * @param variables
	 * @return
	 * @throws Exception
	 */
	public String startSalaryAdjust(SalaryAdjust salary) throws Exception;
	
	/**
	 * 启动请假流程
	 * @param vacation
	 * @return
	 * @throws Exception
	 */
	public String startVacation(Vacation vacation) throws Exception;
	
	/**
	 * 启动报销流程
	 * @param expense
	 * @return
	 * @throws Exception
	 */
	public String startExpense(ExpenseAccount expense) throws Exception;
	
	/**
	 * 查询代办任务
	 * @param user
	 * @param model
	 * @return
	 */
	public List<BaseVO> findTodoTask(User user, Page<BaseVO> page) throws Exception;
	
    /**
     * 签收任务
     * @param user
     * @param taskId
     */
    public void doClaim(User user, String taskId) throws Exception;
    
    /**
     * 委派任务
     * @param userId
     * @throws Exception
     */
    public void doDelegateTask(String userId, String taskId) throws Exception;
    
    /**
     * 转办任务
     * @param userId
     * @param taskId
     * @throws Exception
     */
    public void doTransferTask(String userId, String taskId) throws Exception;
    
    /**
     * 完成任务
     * @param taskId
     * @param content
     * @param userid
     * @param completeFlag
     */
    public void complete(String taskId, String content, String userid, Map<String, Object> variables) throws Exception;
    
    /**
     * 撤销任务
     * @param historyTaskId
     * @throws Exception
     */
    public Integer revoke(String historyTaskId, String processInstanceId) throws Exception;
    
    /**
     * 获取评论
     * @param processInstanceId
     * @return
     * @throws Exception
     */
    public List<CommentVO> getComments(String processInstanceId) throws Exception;
    
	/**
	 * 跳转（包括回退和向前）至指定活动节点
	 */
	public void moveTo(String currentTaskId, String targetTaskDefinitionKey) throws Exception;

	/**
	 * 跳转（包括回退和向前）至指定活动节点
	 * 
	 * @param currentTaskEntity
	 *            当前任务节点
	 * @param targetTaskDefinitionKey
	 *            目标任务节点（在模型定义里面的节点名称）
	 * @throws Exception
	 */
	public void moveTo(TaskEntity currentTaskEntity, String targetTaskDefinitionKey) throws Exception;
 
    
    /**
     * 显示流程图,带流程跟踪
     * @param processInstanceId
     * @return
     */
    public InputStream getDiagram(String processInstanceId) throws Exception;
    
    /**
     * 显示图片-通过流程ID，不带流程跟踪(没有乱码问题)
     * @param resourceType
     * @param processInstanceId
     * @return
     */
    public InputStream getDiagramByProInstanceId_noTrace(String resourceType, String processInstanceId) throws Exception;
    
    /**
     * 显示图片-通过部署ID，不带流程跟踪(没有乱码啊问题)
     * @param resourceType
     * @param processInstanceId
     * @return
     * @throws Exception
     */
    public InputStream getDiagramByProDefinitionId_noTrace(String resourceType, String processDefinitionId) throws Exception;

    /**
     * 读取已结束中的流程-admin查看
     *
     * @return
     */
    public List<BaseVO> findFinishedProcessInstances(Page<BaseVO> page) throws Exception;
    
    /**
     * 各个审批人员查看自己完成的任务
     * @param model
     * @return
     * @throws Exception
     */
    public List<BaseVO> findFinishedTaskInstances(User user, Page<BaseVO> page) throws Exception;
    
    /**
     * 查看正在运行的请假流程
     * @param user
     * @return
     * @throws Exception
     */
    public List<BaseVO> listRuningVacation(User user, Page<Vacation> page) throws Exception;
    
    /**
     * 查看正在运行的报销流程
     * @param user
     * @return
     * @throws Exception
     */
    public List<BaseVO> listRuningExpense(User user, Page<ExpenseAccount> page) throws Exception;
    
    /**
     * 查看正在运行的薪资跳转流程
     * @param user
     * @return
     * @throws Exception
     */
    public List<BaseVO> listRuningSalaryAdjust(User user, Page<SalaryAdjust> page) throws Exception;
    
    /**
     * 管理运行中流程
     * @return
     * @throws Exception
     */
    public List<ProcessInstance> listRuningProcess(Page<ProcessInstance> page) throws Exception;

    /**
     * 激活流程实例
     * @param processInstanceId
     * @throws Exception
     */
    public void activateProcessInstance(String processInstanceId) throws Exception;
    
    /**
     * 挂起流程实例
     * @param processInstanceId
     * @throws Exception
     */
    public void suspendProcessInstance(String processInstanceId) throws Exception;
    
    /**
     * 测试 - 动态创建流程信息
     * @throws Exception
     */
    public void addProcessByDynamic() throws Exception;

}
