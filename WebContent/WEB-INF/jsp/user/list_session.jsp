<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<%@taglib prefix="oafn" uri="http://github.com/zhaoml529/tags/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0"/>    
<title>在线用户列表</title>
</head>

<body>

	<div id="main">

        
        
      <div class="sort_switch">
          <ul id="TabsNav">
          	  <li class="selected"><a href="#">用户</a></li>
          </ul>
      </div>
      
      <div class="sort_content">
      	<form action="${ctx }/userAction/toList_page" method="post">
          <table class="tableHue1" width="100%" border="1" bordercolor="#a4d5e3" cellspacing="0" cellpadding="0">
              <thead>
                <tr>
                    <th style="width: 150px;">会话ID</th>
		            <th>用户名</th>
		            <th>主机地址</th>
		            <th>最后访问时间</th>
		            <th>已强制退出</th>
		            <th>操作</th>
                </tr>
              </thead>
              <tbody id="tbody">
              <c:forEach items="${sessions}" var="session">
		        <tr>
		            <td>${session.id}</td>
		            <td>${oafn:principal(session)}</td>
		            <td>${session.host}</td>
		            <td><fmt:formatDate value="${session.lastAccessTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
		            <td>${oafn:isForceLogout(session) ? '是' : '否'}</td>
		            <td>
		                <c:if test="${not oafn:isForceLogout(session)}">
		                    <a href="${ctx}/userAction/${session.id}/forceLogout">强制退出</a>
		                </c:if>
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
