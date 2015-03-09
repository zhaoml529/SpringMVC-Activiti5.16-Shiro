<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />    
<title>代办任务列表</title>
<link href="${ctx }/css/common/jquery.qtip.min.css" type="text/css" rel="stylesheet" />
<script src="${ctx }/js/workflow.js" type="text/javascript"></script>
<script src="${ctx }/js/common/jquery.outerhtml.js" type="text/javascript"></script>
<script src="${ctx }/js/common/jquery.qtip.min.js" type="text/javascript"></script>
<script type="text/javascript">
	var ctx = "${ctx}";
	$(function() {
		var taskType = "${taskType}";
		$("#" + taskType).attr("class", "selected");
		if(taskType == "candidate"){
			$("#taskForm").attr("action","${ctx}/processAction/todoTaskList_page");
		}else if(taskType == "finished"){
			$("#taskForm").attr("action","${ctx}/processAction/finishedTask_page");
		}
	    // 跟踪
	    $('.trace').click(graphTrace);
	});
	$(function() {
		var message = "${message}";
		if(message != ""){
			$( "#dialog-message" ).dialog({
			      modal: true,
			      buttons: {
			        Ok: function() {
			          $( this ).dialog( "close" );
			        }
			      }
		    });
		}
	});
	
</script>
</head>

<body>
	<div id="main">
		<div id="dialog-message" title="complete">
		  <p>
		    <span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 5px 0;"></span>
		    ${message}
		  </p>
		</div>
      <div class="sort_switch">
          <ul id="TabsNav">
          	  <li class="" id="candidate"><a href="${ctx}/processAction/todoTaskList_page">待办的任务</a></li>
          	  <li class="" id="finished"><a href="${ctx}/processAction/finishedTask_page">已完成的任务</a></li>
          </ul>
      </div>
      
      <div class="sort_content">
      	<form id="taskForm" method="post">
          <table class="tableHue1" width="100%" border="1" bordercolor="#a4d5e3" cellspacing="0" cellpadding="0">
              <thead>
	  			<tr>
					<th>单据类型</th>
					<th>申请人</th>
					<th>标题</th>
					<th>当前节点</th>
					<th>任务创建时间</th>
					<th>流程状态</th>
					<th>操作</th>
				</tr>
              </thead>
              <tbody id="">
              	<c:forEach items="${tasklist }" var="base">
              	<c:set var="task" value="${base.task }" />
				<c:set var="pi" value="${base.processInstance }" />
				<c:set var="pd" value="${base.processDefinition }" />
                <tr>
                  <td>
                  	<c:choose>
                  		<c:when test="${base.businessType == 'vacation'}">请假申请</c:when>
                  		<c:when test="${base.businessType == 'salary'}">薪资调整</c:when>
                  		<c:when test="${base.businessType == 'expense'}">报销申请</c:when>
                  	</c:choose>
                  </td>
                  <td>${base.user_name}</td>
                  <td>${base.title}</td>
                  <td>
                  	  <%-- <a class="trace" id="diagram" href="${ctx }/processAction/process/process-instance?pid=${pi.id }&type=image" pid="${pi.id }" pdid="${pi.processDefinitionId}" title="点击查看流程图">${task.name }</a> --%>
                  	<a class="trace" id="diagram" href="#" pid="${pi.id }" pdid="${pi.processDefinitionId}" title="点击查看流程图">${task.name }</a>
                  </td>
                  <td><fmt:formatDate value="${task.createTime }" type="date" /></td>
				  <td>${pi.suspended ? "已挂起" : "正常" }；<b title='流程版本号'>V: ${pd.version }</b></td>
                  <td> 
                  
                  	  <c:if test="${empty task.assignee }">
                  	  		<a class="claim" href="${ctx }/processAction/claim/${task.id}">签收</a>
					  </c:if>
					  <c:if test="${not empty task.assignee }">
					  	<c:choose>
					  		<c:when test="${'vacation' eq base.businessType }">
					  			<a class="handle" href="${ctx }/vacationAction/toApproval/${task.id}">办理</a>
					  		</c:when>
					  		<c:when test="${'expense' eq base.businessType }">
					  			<a class="handle" href="${ctx }/expenseAction/toApproval/${task.id}">办理</a>
					  		</c:when>
					  		<c:when test="${'salary' eq base.businessType }">
					  			<a class="handle" href="${ctx }/salaryAction/toApproval/${task.id}">办理</a>
					  		</c:when>
					  	</c:choose>
					  </c:if>
                  </td>
                </tr>
                </c:forEach>
                <tr>
              		<td class="fun_area" colspan="7" align="center">${page }</td>
              	</tr>
              </tbody>
          </table>
          </form>
      </div>
	</div>
</body>

</html>
