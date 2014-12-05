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
      	<form action="${ctx }/vacationAction/toList_page" method="post">
          <table class="tableHue1" width="100%" border="1" bordercolor="#a4d5e3" cellspacing="0" cellpadding="0">
			<thead>
				<tr>
					<th>假种</th>
					<th>申请时间</th>
					<th>请假开始时间</th>
					<th>请假结束时间</th>
					<th>天数</th>
					<th>审批状态</th>
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
                  <td><fmt:formatDate value="${vacation.applyDate }" type="date" /></td>
                  <td><fmt:formatDate value="${vacation.beginDate }" type="date" /></td>
                  <td><fmt:formatDate value="${vacation.endDate }" type="date" /></td>
				  <td>${vacation.days }</td>
				  <td>
				  	  <c:choose>
							<c:when test="${'WAITING_FOR_APPROVAL' eq vacation.status }">待审批</c:when>
							<c:when test="${'PENDING' eq vacation.status }">审批中</c:when>
							<c:when test="${'APPROVAL_SUCCESS' eq vacation.status }"><span style="color:green;">通过</span></c:when>
							<c:when test="${'APPROVAL_FAILED' eq vacation.status }"><span style="color:red;">不通过</span></c:when>
						</c:choose>
				  </td>
				  <td>
				  	  <a href="${ctx }/vacationAction/details/${vacation.id }">详细</a>
				  </td>
                </tr>
              </c:forEach>
              </tbody>
          </table>
          </form>
      </div>

      
</div>
    
</body>

</html>
