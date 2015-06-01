<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>

<!DOCTYPE html>
<html>
  <head>
    <title>资源管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${ctx}/js/app/resource.js?_=${sysInitTime}"></script>
  </head>
  <body>
	<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',border:false" title="" style="height: 82px; overflow: hidden; padding: 5px;">
		<div class="well well-small">
			<span class="badge" iconCls="icon-save" plain="true" >提示</span>
			<p>
				在此你可以对<span class="label-info"><strong>菜单功能</strong></span>进行编辑!  &nbsp;<span class="label-info"><strong>注意</strong></span>操作功能是对菜单功能的操作权限！
				请谨慎填写程序编码，权限区分标志，请勿重复!
			</p>
		</div>
	</div>
	<div data-options="region:'center',border:true">
		 <div id="tb">
			<div style="margin:5px 5px 5px 5px;">
				<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showResource();">添加</a>
				<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="openResource();">编辑</a>
				<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delRows();">删除</a>|
				<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="expandAll();">展开</a>
				<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="collapseAll();">收缩</a>
				<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="refresh();">刷新</a>
			</div>
		</div>
		
		<%-- 列表右键 --%>
		<div id="resource_treegrid_menu" class="easyui-menu" style="width:120px;display: none;">
		    <div onclick="showResource();" data-options="iconCls:'icon-add'">新增</div>
		    <div onclick="openResource();" data-options="iconCls:'icon-edit'">编辑</div>
		    <div onclick="del();" data-options="iconCls:'icon-remove'">删除</div>
		    <div onclick="lock(false);" data-options="iconCls:'icon-ok'">启用</div>
		    <div onclick="lock(true);" data-options="iconCls:'icon-lock'">停用</div>
		</div>
		
		<table id="resource" title="权限"></table>
	</div>
	</div>
  </body>
</html>
