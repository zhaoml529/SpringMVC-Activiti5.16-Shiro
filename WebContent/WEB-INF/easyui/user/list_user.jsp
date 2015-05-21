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
	
	<%-- 列表右键 --%>
	<div id="user_datagrid_menu" class="easyui-menu" style="width:120px; display: none;">
	    <div onclick="showDialog();" data-options="iconCls:'easyui-icon-add'">新增</div>
	    <div onclick="edit();" data-options="iconCls:'easyui-icon-edit'">编辑</div>
	    <div onclick="del();" data-options="iconCls:'easyui-icon-remove'">删除</div>
	    <div onclick="editPassword();" data-options="iconCls:'eu-icon-lock'">修改密码</div>
	    <div onclick="editUserOrgan();" data-options="iconCls:'eu-icon-group'">设置机构</div>
	    <div onclick="editUserPost();" data-options="iconCls:'eu-icon-group'">设置岗位</div>
	    <div onclick="editUserRole();" data-options="iconCls:'eu-icon-group'">设置角色</div>
	    <div onclick="editUserResource();" data-options="iconCls:'eu-icon-folder'">设置资源</div>
	    <div onclick="move(true);" data-options="iconCls:'eu-icon-up'">上移</div>
	    <div onclick="move();" data-options="iconCls:'eu-icon-down'">下移</div>
	    <div onclick="lock(false);" data-options="iconCls:'eu-icon-user'">启用</div>
	    <div onclick="lock(true);" data-options="iconCls:'eu-icon-lock'">停用</div>
	</div>
	
    <table id="user_datagrid" title="用户管理"></table>
  </body>
</html>
