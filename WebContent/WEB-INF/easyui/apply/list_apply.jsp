<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html>
<html>
  <head>
    <title>流程管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="${ctx }/css/common/jquery.qtip.min.css" type="text/css" rel="stylesheet" />
	<script src="${ctx}/js/app/applys.js?_=${sysInitTime}" type="text/javascript"></script>
	<script src="${ctx }/js/trace.js" type="text/javascript"></script>
	<script src="${ctx }/js/common/jquery.outerhtml.js" type="text/javascript"></script>
	<script src="${ctx }/js/common/jquery.qtip.min.js" type="text/javascript"></script>
  </head>
  <body>
	 <div class="well well-small" style="margin-left: 5px;margin-top: 5px;">
		<span class="badge easyui-tooltip" title="提示">提示</span>
		<p>
			在此你可以查看您所提交的<span class="label-info"><strong>请假申请</strong></span>、<span class="label-info"><strong>薪资调整申请</strong></span>、
			<span class="label-info"><strong>报销申请</strong></span>，并查看其审批状态。
		</p>
	 </div>	
 	 <div id="toolbar" style="padding:2px 0">
		<table cellpadding="0" cellspacing="0">
			<tr>
				<td style="padding-left:2px">
					<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="showDetails();">详细</a>
				</td>
			</tr>
		</table>
	 </div>
	 <div id="tabs" class="easyui-tabs">
		<div title="请假申请" closable="true" data-options="selected:true" style="padding:10px;">
			<table id="vacation_datagrid" title="请假申请"></table>
		</div>
		<div title="薪资调整申请" closable="true" style="padding:10px;">
			<table id="salary_datagrid" title="薪资调整申请"></table>
		</div>
		<div title="报销申请" closable="true" style="padding:10px;">
			<table id="expense_datagrid" title="报销申请"></table>
		</div>
	</div>
  </body>
</html>
