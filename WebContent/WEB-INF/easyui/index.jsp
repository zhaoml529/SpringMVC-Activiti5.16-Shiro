<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
  <head>
    <title>欢迎</title>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript">
		$(function(){
			if (jqueryUtil.isLessThanIe8()) {
				$.messager.show({
					title : '警告',
					msg : '您使用的浏览器版本太低！<br/>建议您使用谷歌浏览器来获得更快的页面响应效果！',
					timeout : 1000 * 30
				});
			}
		});
	</script>
	<style type="text/css">
	#menuAccordion a.l-btn span span.l-btn-text {
	    display: inline-block;
	    height: 14px;
	    line-height: 14px;
	    margin: 0px 0px 0px 10px;
	    padding: 0px 0px 0px 10px;
	    vertical-align: baseline;
	    width: 128px;
	}
	#menuAccordion 	a.l-btn span span.l-btn-icon-left {
	    background-position: left center;
	    padding: 0px 0px 0px 20px;
	}
	#menuAccordion .panel-body {
		padding:5px;
	}
	#menuAccordion span:focus{
		outline: none;
	}
	</style>
  </head>
 <body class="easyui-layout">
	<div data-options="region:'north',border:false" style="height:40px;background:#EEE;padding:10px;overflow: hidden;"  href="${ctx }/north"></div>
	<div data-options="region:'west',split:true,title:'主要菜单'" style="width:200px;">
			<div id="menuAccordion">
				<div class="well well-small">
				<ul class="easyui-tree">
					<li>
						<shiro:hasRole name="user">
							<span>用户功能</span>
						</shiro:hasRole>
						<shiro:hasRole name="admin">
							<span>管理员功能</span>
						</shiro:hasRole>
						<ul>
							<c:forEach items="${menuList }" var="menu">
								<li class="last"><a href="javascript:void(0);" onclick="addTab('${menu.name }','${ctx}/${menu.url }');">${menu.name }</a></li>
							</c:forEach>
						</ul>
					</li>
				</ul>
				</div>
			</div>
	</div> 
	<div data-options="region:'south',border:false" style="height:25px;background:#EEE;padding:5px;" href="${ctx }/south"></div>
	<div data-options="region:'center',plain:true,title:'欢迎使用OA'" style="overflow: hidden;"  href="${ctx }/center"></div>
<%--	<jsp:include page="user/loginAndReg.jsp"></jsp:include>--%>
</body>
</html>
