<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />    
<title></title>
<link href="${ctx }/css/common/jquery.qtip.min.css" type="text/css" rel="stylesheet" />
<script src="${ctx }/js/workflow.js" type="text/javascript"></script>
<script src="${ctx }/js/common/jquery.outerhtml.js" type="text/javascript"></script>
<script src="${ctx }/js/common/jquery.qtip.min.js" type="text/javascript"></script>
<script type="text/javascript">
	var ctx = "${ctx}";
	$(function() {
		var businessType = "${businessType}";
		$("#" + businessType).attr("class", "selected");
	    // 跟踪
	    $('.trace').click(graphTrace);
	});
</script>
</head>

<body>
	<div id="main">
      <div class="sort_switch">
          <ul id="TabsNav">
          	  <li class="" id="vacation"><a href="${ctx}/processAction/process/runingProcessInstance/vacation/list_page">请假申请</a></li>
          	  <li class="" id="salary"><a href="${ctx}/processAction/process/runingProcessInstance/salary/list_page">薪资调整申请</a></li>
          	  <li class="" id="expense"><a href="${ctx}/processAction/process/runingProcessInstance/expense/list_page">报销申请</a></li>
          </ul>
      </div>
      <div class="sort_content">
      	<form id="runingForm" action="${ctx }/processAction/process/runingProcessInstance/${businessType }/list_page" method="post">
          <table class="tableHue1" width="100%" border="1" bordercolor="#a4d5e3" cellspacing="0" cellpadding="0">
              <thead>
                <tr>
                  	<th>申请人</th>
					<th>标题</th>
					<th>当前节点</th>
					<th>申请时间</th>
					<th>流程状态</th>
					<th>操作</th>
                </tr>
              </thead>
              <tbody id="tbody">
              <c:forEach var="base" items="${baseList}">
              	<c:set var="task" value="${base.task }" />
				<c:set var="pi" value="${base.processInstance }" />
				<c:set var="pd" value="${base.processDefinition }" />
                <tr>
                  <td>${base.user_name}</td>
                  <td>${base.title}</td>
                  <td>
                  	  <%-- <a class="trace" id="diagram" href="${ctx }/processAction/process/process-instance?pid=${pi.id }&type=image" pid="${pi.id }" pdid="${pi.processDefinitionId}" title="点击查看流程图">${task.name }</a> --%>
                  	<a class="trace" id="diagram" href="#" pid="${pi.id }" pdid="${pi.processDefinitionId}" title="点击查看流程图">${task.name }</a>
                  </td>
                  <td><fmt:formatDate value="${task.createTime }" type="date" /></td>
				  <td>${pi.suspended ? "已挂起" : "正常" }；<b title='流程版本号'>V: ${pd.version }</b></td>
                  <td>
                  	<c:choose>
                  		<c:when test="${'vacation' eq base.businessType }">
                  			<a href="${ctx}/vacationAction/details/${base.businessKey}">详情</a>
                  		</c:when>
                  		<c:when test="${'salary' eq base.businessType }">
                  			<a href="${ctx}/salaryAction/details/${base.businessKey}">详情</a>
                  		</c:when>
                  		<c:when test="${'expense' eq base.businessType }">
                  			<a href="${ctx}/expenseAction/details/${base.businessKey}">详情</a>
                  		</c:when>
                  	</c:choose>
                  </td>
                </tr>
              </c:forEach>
              <tr>
              		<td class="fun_area" colspan="6" align="center">${page }</td>
              </tr>
              </tbody>
          </table>
         </form>
      </div>
</div>
</body>
</html>
