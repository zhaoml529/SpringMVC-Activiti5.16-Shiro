<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0"/>    
<title>用户列表</title>

<script type="text/javascript">
	function add() {
		window.location.href="<c:url value='/userAction/toAdd'/>";
	}
	function sync() {
		window.location.href="${ctx }/userAction/syncUserToActiviti"
	}
</script>
</head>

<body>

	<div id="main">
	  <c:if test="${not empty msg}">
		<div class="ui-widget">
			<div class="ui-state-highlight ui-corner-all" style="margin-top: 20px; padding: 0 .7em;"> 
				<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
				<strong>提示：</strong>${msg}</p>
			</div>
		</div>
	  </c:if>
      <div class="sort_switch">
          <ul id="TabsNav">
          	  <li class="selected"><a href="#">用户</a></li>
          </ul>
      </div>
      <div style="text-align: right;padding: 2px 1em 2px">
		<input type="button" value="添加" class="input_button4" onClick="add()"/>
		<input type="button" value="同步用户" class="input_button4" onclick="sync()" />
	  </div>
      <div class="sort_content">
      	<form action="${ctx }/userAction/toList_page" method="post">
          <table class="tableHue1" width="100%" border="1" bordercolor="#a4d5e3" cellspacing="0" cellpadding="0">
              <thead>
                <tr>
                  <td width="30%"><strong>姓名</strong></td>
                  <td width="10%"><strong>注册时间</strong></td>
                  <td width="50%"><strong>所属用户组</strong></td>
                  <td width="10%"><strong>操作</strong></td>
                </tr>
              </thead>
              <tbody id="tbody">
              <c:forEach items="${listUser}" var="user">
                <tr>
                  <td>${user.name}</td>
                  <td>${user.registerDate}</td>
                  <td>${user.group.name}</td>
                  <td>
                  	<a href="<c:url value='/userAction/toUpdate/${user.id }' />">修改</a>|
                  	<a href="<c:url value='/userAction/delete/${user.id }' />">删除</a>
                  </td>
                </tr>
              </c:forEach>
              	<tr>
              		<td class="fun_area" colspan="4" align="center">${page }</td>
              	</tr>
              </tbody>
          </table>
         </form>
      </div>
      


</div>
    
</body>
</html>
