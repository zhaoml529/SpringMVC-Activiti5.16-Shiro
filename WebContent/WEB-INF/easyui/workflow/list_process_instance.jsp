<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>

<!DOCTYPE html>
<html>
  <head>
    <title>流程定义</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${ctx}/js/app/process.js?_=${sysInitTime}"></script>
  </head>
  <body>
	<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north',border:false" title="" style="height: 82px; overflow: hidden; padding: 5px;">
		<div class="well well-small">
			<span class="badge" iconCls="icon-save" plain="true" id="tishi" title="提示">提示</span>
			<p>
				在此你可以对<span class="label-info"><strong>流程定义</strong></span>进行部署!  &nbsp;<span class="label-info"><strong>提示</strong></span>可以单个文件加载，
				也可以多个文件同时加载！点击xml或者png链接可以查看具体内容！
			</p>
		</div>
	</div>
	<div data-options="region:'center',border:true">
		<div id="tb" style="padding:2px 0">
			<table cellpadding="0" cellspacing="0">
				<tr>
					<td style="padding-left:2px">
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="redeploy();">加载</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="convert_to_model();">转换为Model</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delRows();">删除</a>&nbsp;|&nbsp;
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="deploy();">部署流程</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="deployAll();">重新部署全部流程</a>
					</td>
				</tr>
			</table>
		</div>
		<fieldset id="deployFieldset" style="display: none">
			<legend style="margin-left: 10px" align="left">部署新流程</legend>
			<div align="left">
			<b>支持文件格式：</b>zip、bar、bpmn、bpmn20.xml<br /><br />
			<form action="${ctx }/processAction/process/deploy" method="post" enctype="multipart/form-data">
				<input type="file" name="file" />
				<input type="submit" class="input_button4" value="Submit" />
			</form>
			</div>
		</fieldset>
		<table id="process" title="流程定义">
		</table>
	</div>
	</div>
  </body>
</html>
