<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">    
<title>代办任务列表</title>
<script>
	$(function() {
		var taskType = "${businessType}";
		$("#" + taskType).attr("class", "selected");
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
		<div id="dialog-message" title="Download complete">
		  <p>
		    <span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
		    ${message}
		  </p>
		</div>
      <div class="sort_switch">
          <ul id="TabsNav">
          	  <li class="" id="candidate"><a href="${ctx}/vacationAction/todoTaskList_page">待办的任务</a></li>
          	  <li class="" id="assignee"><a href="${ctx}/vacationAction/doTaskList_page">受理的任务</a></li>
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
              <tbody id="">
              	<c:forEach items="${vacationList}" var="vacation">
              	<c:set var="task" value="${vacation.task }" />
				<c:set var="pi" value="${vacation.processInstance }" />
				<c:set var="pd" value="${vacation.processDefinition }" />
                <tr>
                  <td>
                  	<c:choose>
                  		<c:when test="${vacation.vacationType == 0}">年假</c:when>
                  		<c:when test="${vacation.vacationType == 1}">病假</c:when>
                  		<c:when test="${vacation.vacationType == 2}">事假</c:when>
                  	</c:choose>
                  </td>
                  <td>${vacation.user_name}</td>
                  <td><fmt:formatDate value="${vacation.applyDate}" type="date" /></td>
                  <td><fmt:formatDate value="${vacation.beginDate}" type="date" /></td>
                  <td><fmt:formatDate value="${vacation.endDate}" type="date" /></td>
                  <td>
                  	  <a class="trace" href='#' pid="${pi.id }" pdid="${pi.processDefinitionId}" title="点击查看流程图">${task.name }</a>
                  </td>
                  <td><fmt:formatDate value="${task.createTime }" type="date" /></td>
				  <td>${pi.suspended ? "已挂起" : "正常" }；<b title='流程版本号'>V: ${pd.version }</b></td>
                  <td> 
                  
                  	  <c:if test="${empty task.assignee }">
							<a class="claim" href="${ctx }/vacationAction/claim/${task.id}">签收</a>
					  </c:if>
					  <c:if test="${not empty task.assignee }">
						<%-- 此处用tkey记录当前节点的名称 --%>
						<a class="handle" href="${ctx }/vacationAction/toApproval/${task.id}">办理</a>
					  </c:if>
                  </td>
                </tr>
                </c:forEach>
                <tr>
              		<td class="fun_area" colspan="9" align="center">${page }</td>
              	</tr>
              </tbody>
          </table>
      </div>

      
	</div>
    
</body>

</html>
