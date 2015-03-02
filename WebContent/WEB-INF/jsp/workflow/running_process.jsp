<%@ page import="com.zml.oa.util.ProcessDefinitionCache,org.activiti.engine.RepositoryService"%>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="java.util.*,org.apache.commons.lang3.StringUtils,org.apache.commons.lang3.ObjectUtils" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />    
<title>流程管理</title>
<link href="${ctx }/css/common/jquery.qtip.min.css" type="text/css" rel="stylesheet" />
<script src="${ctx }/js/workflow.js" type="text/javascript"></script>
<script src="${ctx }/js/common/jquery.outerhtml.js" type="text/javascript"></script>
<script src="${ctx }/js/common/jquery.qtip.min.js" type="text/javascript"></script>
	<script type="text/javascript">
		var ctx = "${ctx}";
		$(function() {
			var taskType = "${taskType}";
			$("#" + taskType).attr("class", "selected");
			if(taskType == "running"){
				$("#taskForm").attr("action","${ctx}/processAction/process/runningProcess_page");
			}else if(taskType == "finished"){
				$("#taskForm").attr("action","${ctx}/processAction/process/finishedProcess_page");
			}
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
			// 跟踪
		    $('.trace').click(graphTrace);
		});
	</script>
</head>

<body>
	<%
	RepositoryService repositoryService = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext()).getBean(org.activiti.engine.RepositoryService.class);
	ProcessDefinitionCache.setRepositoryService(repositoryService);
	%>
	<div id="main">
		<div id="dialog-message" title="complete">
		  <p>
		    <span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 5px 0;"></span>
		    ${message}
		  </p>
		</div>
      <div class="sort_switch">
          <ul id="TabsNav">
          	  <li class="" id="running"><a href="${ctx }/processAction/process/runningProcess_page">管理运行中流程</a></li>
          	  <li class="" id="finished"><a href="${ctx}/processAction/process/finishedProcess_page">已结束的流程</a></li>
          </ul>
      </div>
      
      <div class="sort_content">
      	<form id="taskForm" method="post">
          <table class="tableHue1" width="100%" border="1" bordercolor="#a4d5e3" cellspacing="0" cellpadding="0">
				<tr>
					<th>执行ID</th>
					<th>流程实例ID</th>
					<th>流程定义ID</th>
					<th>当前节点</th>
					<th>是否挂起</th>
					<th>操作</th>
				</tr>
		
				<c:forEach items="${list }" var="p" varStatus="i">
				<c:set var="pdid" value="${p.processDefinitionId }" />
				<c:set var="activityId" value="${p.activityId }" />
				<tr align="center">
					<td>${p.id }</td>
					<td>${p.processInstanceId }</td>
					<td>${pdid }</td>
					<td>
						<a class="trace" id="diagram" href="#" pid="${p.id }" pdid="${p.processDefinitionId}" title="see"><%=ProcessDefinitionCache.getActivityName(pageContext.getAttribute("pdid").toString(), ObjectUtils.toString(pageContext.getAttribute("activityId"))) %> </a>
					</td>
					<td>${p.suspended }</td>
					<td>
						<c:if test="${p.suspended }">
							<a href="${ctx }/processAction/process/updateProcessStatusByProInstanceId/active/${p.processInstanceId}">激活</a>
						</c:if>
						<c:if test="${!p.suspended }">
							<a href="${ctx }/processAction/process/updateProcessStatusByProInstanceId/suspend/${p.processInstanceId}">挂起</a>
						</c:if>
					</td>
				</tr>
				</c:forEach>
				<tr>
              		<td class="fun_area" colspan="6" align="center">${page }</td>
              	</tr>
			</table>
		</form>
		</div>
	</div>

</body>
</html>
