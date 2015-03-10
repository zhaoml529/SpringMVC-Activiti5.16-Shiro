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

</head>

<body>

	<div id="main">

        
      <div class="">

                <input type="button" value="添加" class="input_button3" onClick="add()"/>
      </div>
        
      <div class="sort_switch">
          <ul id="TabsNav">
          	  <li class="selected"><a href="#">用户</a></li>
          </ul>
      </div>
      
      <div class="sort_content">
      	<form action=" ${ctx }/userAction/userList_page" method="post">
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
                  <td>删除</td>
                </tr>
              </c:forEach>
              	<tr>
              		<td class="fun_area" colspan="4" align="center"><c:out value="${page }" escapeXml="false"/></td>
              	</tr>
              </tbody>
          </table>
         </form>
      </div>
      


</div>
    
</body>

</html>
