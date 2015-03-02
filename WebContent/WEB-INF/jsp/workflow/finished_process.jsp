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
          	  <li class="" id="running"><a href="${ctx }/processAction/process/runningProcess_page">管理运行中流程</a></li>
          	  <li class="" id="finished"><a href="${ctx}/processAction/process/finishedProcess_page">已结束的流程</a></li>
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
					<th>流程启动时间</th>
					<th>流程结束时间</th>
					<th>流程结束原因</th>
					<th>流程版本</th>
					<th>操作</th>
				</tr>
              </thead>
              <tbody id="">
              	<c:forEach items="${processList }" var="base">
				<c:set var="hpi" value="${base.historicProcessInstance }" />
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
                  <td><fmt:formatDate value="${hpi.startTime }" type="date" /></td>
				  <td><fmt:formatDate value="${hpi.endTime }" type="date" /></td>
				  <td>${hpi.deleteReason }</td>
				  <td><b title='流程版本号'>V: ${pd.version }</b></td>
                  <td> 
                  
                  </td>
                </tr>
                </c:forEach>
                <tr>
              		<td class="fun_area" colspan="8" align="center">${page }</td>
              	</tr>
              </tbody>
          </table>
          </form>
      </div>
	</div>
</body>

</html>
