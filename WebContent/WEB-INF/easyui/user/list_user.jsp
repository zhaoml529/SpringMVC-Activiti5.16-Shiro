<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>

<!DOCTYPE html>
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
			在此你可以对<span class="label-info"><strong>用户</strong></span>进行管理!<br/><br/>
			其中<span class="label-info"><strong>同步用户</strong></span>功能是将用户表的数据同步到工作流(Activiti)中的用户表中，具体
			工作流中的用户表包括： <span class="label-info"><strong>act_id_group</strong></span>、<span class="label-info"><strong>act_id_membership</strong></span>、
			<span class="label-info"><strong>act_id_user</strong></span>。<br/>
			同步用户功能并不影响系统的正常使用，
		</p>
	</div>
	
	<div id="toolbar" style="padding:2px 0">
		<table cellpadding="0" cellspacing="0">
			<tr>
				<td style="padding-left:2px">
					<shiro:hasRole name="admin">
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showUser();">添加</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit();">编辑</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="del();">删除</a>|
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="sync();">同步用户</a>
					</shiro:hasRole>
			</tr>
		</table>
	</div>
	<table id="user_datagrid" title="用户管理"></table>
  </body>
</html>
