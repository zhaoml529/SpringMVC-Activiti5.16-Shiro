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
	<script type="text/javascript" src="${ctx}/js/app/process_running.js?_=${sysInitTime}"></script>
  </head>
  <body>
	 <div class="well well-small" style="margin-left: 5px;margin-top: 5px">
		<span class="badge">提示</span>
		<p>
			在此你可以对<span class="label-info"><strong>用户</strong></span>进行编辑!
		</p>
	 </div>	
	 <div data-options="region:'center',border:true">
	 <div id="tb" style="padding:2px 0">
		<table cellpadding="0" cellspacing="0">
			<tr>
				<td style="padding-left:2px">
					<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showResource();">挂起</a>
			<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="convert_to_model();">激活</a>
				</td>
			</tr>
		</table>
	 </div>
	<table id="process_running" title="流程定义"></table>
	</div>
  </body>
</html>
