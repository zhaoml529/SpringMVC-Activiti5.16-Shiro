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
			<span class="badge" iconCls="icon-save" plain="true" >提示</span>
			<p>
				在此你可以对<span class="label-info"><strong>流程定义</strong></span>进行部署!  &nbsp;<span class="label-info"><strong>提示</strong></span>可以单个文件加载，
				也可以多个文件同时加载！点击xml或者png链接可以查看具体内容！
			</p>
		</div>
	</div>
	<div data-options="region:'center',border:true">
		 <div id="tb">
			<div style="margin:5px 5px 5px 5px;">
				<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="showResource();">加载</a>
				<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="convert_to_model();">转换为Model</a>
				<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delRows();">删除</a>&nbsp;|&nbsp;
				<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="deploy();">部署流程</a>
				<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="deployAll();">重新部署全部流程</a>
			</div>
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
<%-- 			<thead>
				<tr>
					<th data-options="field:'id'">ProcessDefinitionId</th>
					<th data-options="field:'deploymentId'">DeploymentId</th>
					<th data-options="field:'name'">名称</th>
					<th data-options="field:'key'">KEY</th>
					<th data-options="field:'version'">版本号</th>
					<th data-options="field:'resourceName'">XML</th>
					<th data-options="field:'diagramResourceName'">图片</th>
					<th data-options="field:'deploymentTime'">部署时间</th>
					<th data-options="field:'suspended'">是否挂起</th>
					<th data-options="field:'caozuo'">操作</th>
				</tr>
		    </thead> 
			<tbody id="tbody">
	              <c:forEach items="${rows }" var="object">
					<c:set var="process" value="${object[0] }" />
					<c:set var="deployment" value="${object[1] }" />
					<tr>
						<td>${process.id }</td>
						<td>${process.deploymentId }</td>
						<td>${process.name }</td>
						<td>${process.key }</td>
						<td>${process.version }</td>
						<td><a target="_blank" href='${ctx }/processAction/process/process-definition?processDefinitionId=${process.id}&resourceType=xml'>${process.resourceName }</a></td>
						<td><a target="_blank" href='${ctx }/processAction/process/process-definition?processDefinitionId=${process.id}&resourceType=image'>${process.diagramResourceName }</a></td>
						<td><fmt:formatDate value="${deployment.deploymentTime }"  type="both"/></td>
						<td>${process.suspended} |
							<c:if test="${process.suspended }">
								<a href="${ctx }/processAction/process/updateProcessStatusByProDefinitionId?status=active&processDefinitionId=${process.id}">激活</a>
							</c:if>
							<c:if test="${!process.suspended }">
								<a href="${ctx }/processAction/process/updateProcessStatusByProDefinitionId?status=suspend&processDefinitionId=${process.id}">挂起</a>
							</c:if>
						</td>
						<td>
	                        <a href='${ctx }/processAction/process/delete?deploymentId=${process.deploymentId}'>删除</a>|
	                        <a href='${ctx }/processAction/process/redeploy/single?resourceName=${process.resourceName }&deploymentId=${process.deploymentId}'>加载</a>|
	                        <a href='${ctx }/processAction/process/redeploy/single?resourceName=${process.resourceName }&diagramResourceName=${process.diagramResourceName }&deploymentId=${process.deploymentId}'>加载</a>|
	                        <a href='${ctx }/processAction/process/convert_to_model?processDefinitionId=${process.id}'>转换为Model</a>
	                    </td>
					</tr>
				  </c:forEach>
              </tbody> --%>
		</table>
	</div>
	</div>
  </body>
</html>
