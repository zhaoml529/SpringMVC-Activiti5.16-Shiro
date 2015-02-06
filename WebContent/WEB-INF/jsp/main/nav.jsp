<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" href="${ctx}/css/nav.css" type="text/css" />
</head>

<body>
	<div class="admin_memu">
		<ul id="nav">
			<li>
				<shiro:hasRole name="user">
					<a href="#" onclick="clickNav(this);return false;" class="parent"><h5><strong>用户功能</strong></h5></a>
				</shiro:hasRole>
				<shiro:hasRole name="admin">
					<a href="#" onclick="clickNav(this);return false;" class="parent"><h5><strong>管理员功能</strong></h5></a>
				</shiro:hasRole>
				<ul id="opt" class="child_area">
					<c:forEach items="${menuList }" var="menu">
						<li class="last"><a href="${ctx}/${menu.url}" target="mainFrame"><h6>${menu.name }</h6></a></li>
					</c:forEach>
				</ul>
			</li>
        </ul>
	</div>

</body>
<script type="text/javascript">
	function clickNav() {
		var childUl = document.getElementById("opt");
		if (childUl.style.display == "block" || childUl.style.display == "")	{
			childUl.style.display = "none";
		} else {
			childUl.style.display = "block";
		}
	}

</script>
</html>
