<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />    
<title>流程定义</title>

<script type="text/javascript">
$(function() {
	$('#deploy').click(function() {
		$('#deployFieldset').toggle('normal');
	});
	$('#redeploy').click(function() {
		window.location.href = '${ctx }/processAction/process/redeploy/all';
	});
});

function doSearch(currentPage)
{
	var pageNum = document.getElementById("pageNum").value;
	if(isNaN(pageNum))
	{
		alert("请输入正确的行数!");
	}
	else
	{
		document.getElementById('currentPage').value = currentPage;
		document.forms[1].submit();
	}
}
</script>

</head>

<body>

	<div id="main">
	  <c:if test="${not empty message}">
		<div class="ui-widget">
			<div class="ui-state-highlight ui-corner-all" style="margin-top: 20px; padding: 0 .7em;"> 
				<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
				<strong>提示：</strong>${message}</p>
			</div>
		</div>
	  </c:if>
      <div class="sort_switch">
          <ul id="TabsNav">
          	  <li class="selected"><a href="#">流程定义</a></li>
          </ul>
      </div>
      <div style="text-align: right;padding: 2px 1em 2px">
		<div id="message" class="info" style="display:inline;"><b>提示：</b>点击xml或者png链接可以查看具体内容！</div>
		<input type="button" id="deploy" value="部署流程" class="input_button4"/>
		<input type="button" id="redeploy" value="重新部署流程" class="input_button4"/>
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
	  
      <div class="sort_content">
      	<form action=" ${ctx }/processAction/process/listProcess_page" method="post">
          <table class="tableHue1" width="100%" border="1" bordercolor="#a4d5e3" cellspacing="0" cellpadding="0">
              <thead>
                <tr>
                  	<th>ProcessDefinitionId</th>
					<th>DeploymentId</th>
					<th>名称</th>
					<th>KEY</th>
					<th>版本号</th>
					<th>XML</th>
					<th>图片</th>
					<th>部署时间</th>
					<th>是否挂起</th>
					<th>操作</th>
                </tr>
              </thead>
              <tbody id="tbody">
	              <c:forEach items="${obj }" var="object">
					<c:set var="process" value="${object[0] }" />
					<c:set var="deployment" value="${object[1] }" />
					<tr>
						<td>${process.id }</td>
						<td>${process.deploymentId }</td>
						<td>${process.name }</td>
						<td>${process.key }</td>
						<td>${process.version }</td>
						<td><a target="_blank" href='${ctx }/processAction/process/resource/process-definition?processDefinitionId=${process.id}&resourceType=xml'>${process.resourceName }</a></td>
						<td><a target="_blank" href='${ctx }/processAction/process/resource/process-definition?processDefinitionId=${process.id}&resourceType=image'>${process.diagramResourceName }</a></td>
						<td><fmt:formatDate value="${deployment.deploymentTime }"  type="both"/></td>
						<td>${process.suspended} |
							<c:if test="${process.suspended }">
								<a href="${ctx }/processAction/process/updateProcessStatusByProDefinitionId/active/${process.id}">激活</a>
							</c:if>
							<c:if test="${!process.suspended }">
								<a href="${ctx }/processAction/process/updateProcessStatusByProDefinitionId/suspend/${process.id}">挂起</a>
							</c:if>
						</td>
						<td>
	                        <a href='${ctx }/processAction/process/delete?deploymentId=${process.deploymentId}'>删除</a>
	                        <a href='${ctx }/processAction/process/convert-to-model?processDefinitionId=${process.id}'>转换为Model</a>
	                    </td>
					</tr>
				  </c:forEach>
              	<tr>
              		<td class="fun_area" colspan="10" align="center">${page }</td>
              	</tr>
              </tbody>
          </table>
         </form>
      </div>
      


</div>
    
</body>

</html>
