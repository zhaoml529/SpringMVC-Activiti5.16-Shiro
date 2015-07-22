<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>权限编辑</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${ctx}/js/app/permission.js?_=${sysInitTime}"></script>
	</head>
  <body>
   <div id="panel" data-options="border:false">
		<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false" title="" style="height: 82px; overflow: hidden; padding: 5px;">
			<div class="well well-small">
				<span class="badge" iconCls="icon-save" plain="true" >提示</span>
				<p>
					新增菜单功能不属于当前角色，请在<span class="label-info"><strong>菜单权限分派</strong></span>中为该角色进行资源分派！请<span class="label-info"><strong>双击角色</strong></span>查看所属资源！
					超级管理员默认拥有<span class="label-info"><strong>所有权限！</strong></span><br/>
					更改相应的组权限后，要点击<span class="label-info"><strong>保存设置</strong></span>来保存更改。
				</p>
			</div>
		</div>
			<div data-options="region:'west',split:true,border:true" style="width:500px;">
				<div id="toolbar" style="padding:2px 0">
					<table cellpadding="0" cellspacing="0">
						<tr>
							<td style="padding-left:4px;padding-bottom:4px;">
								<shiro:hasRole name="admin">
									<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="savePermission();">保存设置</a>
									<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showGroup();">添加</a>
									<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="operation();">编辑</a>
									<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delRows();">删除</a>
								</shiro:hasRole>
							</td>
						</tr>
					</table>
				</div>
				<table id="group" title="用户组"></table>
			</div>
			<div data-options="region:'center',border:true">
				<div id="tb" style="padding:2px 0">
					<table cellpadding="0" cellspacing="0">
						<tr>
							<td style="padding-left:4px;padding-bottom:4px;">
								<shiro:hasRole name="admin">
									<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="expandAll();">展开</a>
									<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="collapseAll();">收缩</a>
									<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="refresh();">刷新</a>
								</shiro:hasRole>
							</td>
						</tr>
					</table>
				</div>
				<%-- 列表右键 --%>
				<div id="resource_datagrid_menu" class="easyui-menu" style="width:120px;display: none;">
				    <div onclick="showResource();" data-options="iconCls:'icon-add'">新增</div>
				    <div onclick="openResource();" data-options="iconCls:'icon-edit'">编辑</div>
				    <div onclick="del();" data-options="iconCls:'icon-remove'">删除</div>
				    <div onclick="lock(false);" data-options="iconCls:'icon-ok'">启用</div>
				    <div onclick="lock(true);" data-options="iconCls:'icon-lock'">停用</div>
				</div>
				
				<table id="resource" title="权限"></table>
			</div>
		</div>
	</div>
  </body>
</html>
