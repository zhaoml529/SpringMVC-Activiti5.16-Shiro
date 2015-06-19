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
	<script src="${ctx}/js/app/process_running.js?_=${sysInitTime}" type="text/javascript"></script>
	<script src="${ctx}/js/app/process_finished.js?_=${sysInitTime}" type="text/javascript"></script>
	<script src="${ctx }/js/trace.js" type="text/javascript"></script>
	<script src="${ctx }/js/common/jquery.outerhtml.js" type="text/javascript"></script>
	<script src="${ctx }/js/common/jquery.qtip.min.js" type="text/javascript"></script>
  </head>
  <body>
	 <div class="well well-small" style="margin-left: 5px;margin-top: 5px;">
		<span class="badge easyui-tooltip" title="提示">提示</span>
		<p>
			在此你可以对<span class="label-info"><strong>流程实例</strong></span>进行操作!
		</p>
	 </div>	
	 <div id="tabs" class="easyui-tabs">
		<div title="运行中流程" closable="true" style="padding:10px;">
			<table id="process_running" title="流程实例"></table>
		</div>
		<div title="已结束的流程" closable="true" style="padding:10px;">
			<table id="process_finished" title="流程实例"></table>
		</div>
	</div>
  </body>
</html>
