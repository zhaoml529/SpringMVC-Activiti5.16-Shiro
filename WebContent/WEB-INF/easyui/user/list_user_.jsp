<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>用户管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${ctx}/js/app/user.js?_=${sysInitTime}"></script>
  </head>
  <body>
 		<div class="well well-small" style="margin-left: 5px;margin-top: 5px">
		<span class="badge">提示</span>
		<p>
			在此你可以对<span class="label-info"><strong>用户</strong></span>进行编辑!
		</p>
	</div>
	
	<%-- 列表菜单 --%>
	<div id="toolbar" style="padding:2px 0" class="datagrid-toolbar">
	<table cellpadding="0" cellspacing="0">
		<tr>
			<td style="padding-left:2px">
				<shiro:hasRole name="admin">
					<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addRowsOpenDlg();">添加</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit('','');">编辑</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delRows();">删除</a>
				</shiro:hasRole>
			<!--  	<shiro:hasPermission name="userEndEdit">
					<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="endEdit();">结束编辑</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="userSave">
					<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="saveRows();">保存</a>
				</shiro:hasPermission>-->
			</td>
			<td style="padding-left:2px">
				<input id="searchbox" type="text"/>
			</td>
			<td style="padding-left:2px">
				<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="userSearch();">高级查询</a>
			</td>
		</tr>
	</table>
	</div>
	<div id="mm">
		<div name="myid">用户编码</div>
		<div name="account">用户账户</div>
		<div name="name">用户名</div>
		<div name="email">邮箱</div>
		<div name="tel">电话</div>
		<div name="organizeName">组织</div>
		<div name="description">描述</div>
	</div>
	
    <table id="user_datagrid" title="用户管理"></table>
  </body>
</html>
