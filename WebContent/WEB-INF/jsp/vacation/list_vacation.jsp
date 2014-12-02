<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">    
<title></title>

</head>

<body>

	<div id="main">
	 	<c:if test="${not empty message}">
			<div id="message" class="alert alert-success">${message}</div>
		</c:if>
      <div class="sort_switch">
          <ul id="TabsNav">
          	  <li class="selected" id="vacation"><a href="${ctx}/process-listProcessInstance.action?processType=vacation">请假申请</a></li>
          	  <li class="" id="salary"><a href="${ctx}/process-listProcessInstance.action?processType=salary">薪资调整申请</a></li>
          	  <li class="" id="expense"><a href="${ctx}/process-listProcessInstance.action?processType=expense">报销申请</a></li>
          </ul>
      </div>
      
      <div class="sort_content">
          <table class="tableHue1" width="100%" border="1" bordercolor="#a4d5e3" cellspacing="0" cellpadding="0">
			<thead>
				<tr>
					<th>假种</th>
					<th>申请人</th>
					<th>申请时间</th>
					<th>开始时间</th>
					<th>结束时间</th>
					<th>当前节点</th>
					<th>任务创建时间</th>
					<th>流程状态</th>
					<th>操作</th>
				</tr>
			</thead>
              <tbody id="tbody">
              <c:forEach var="vacation" items="${vacationList}">
                <tr>
                  <td>
                  	<c:choose>
                  		<c:when test="${vacationType == 0 }">年假</c:when>
                  		<c:when test="${vacationType == 1 }">病假</c:when>
                  		<c:when test="${vacationType == 2 }">事假</c:when>
                  	</c:choose>
                  </td>
                  <td>${vacation.user_name }</td>
                  <td>${vacation.applyDate }</td>
                  <td>${vacation.beginDate }</td>
                  <td>${vacation.endDate }</td>
                  <td>
						<a class="trace" href='#' pid="${pi.id }" pdid="${pi.processDefinitionId}" title="点击查看流程图">${task.name }</a>
				  </td>
				  <td>${task.createTime }</td>
					<td>${pi.suspended ? "已挂起" : "正常" }；<b title='流程版本号'>V: ${vacation.processDefinition.version }</b></td>
					<td>
						<c:if test="${empty task.assignee }">
							<a class="claim" href="${ctx }/oa/leave/task/claim/${task.id}">签收</a>
						</c:if>
						<c:if test="${not empty task.assignee }">
							<%-- 此处用tkey记录当前节点的名称 --%>
							<a class="handle" tkey='${task.taskDefinitionKey }' tname='${task.name }' href="${ctx }/toApproval/${task.id}">办理</a>
						</c:if>
					</td>
                </tr>
              </c:forEach>
              </tbody>
          </table>
      </div>

      
</div>
    
</body>

</html>
