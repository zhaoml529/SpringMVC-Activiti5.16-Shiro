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
        
      <div class="sort_switch">
          <ul id="TabsNav">
          	  <li class="selected"><a href="#">用户组 </a></li>
          </ul>
      </div>
      
      <div class="sort_content">
          <table class="tableHue1" width="100%" border="1" bordercolor="#a4d5e3" cellspacing="0" cellpadding="0">
              <thead>
                <tr>
                  <td width="45%"><strong>名称</strong></td>
                  <td width="45%"><strong>类型</strong></td>
                  <td width="10%"><strong>操作</strong></td>
                </tr>
              </thead>
              <tbody id="tbody">
              	<c:forEach var="group" items="${groupList}">
                <tr>
                  <td>${group.name}</td>
                  <td>${group.type}</td>
                  <td align="center"><a href="${ctx }/permissionAction/listPermission_page?groupId=${group.id}">权限设置</a></td>
                </tr>
                </c:forEach>
                <tr>
              		<td class="fun_area" colspan="3" align="center"><c:out value="${page }" escapeXml="false" /></td>
              	</tr>
              </tbody>
          </table>
      </div>
      
	</div>
    
</body>

</html>
