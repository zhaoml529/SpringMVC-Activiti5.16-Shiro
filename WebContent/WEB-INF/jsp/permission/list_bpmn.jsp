<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />    
<title>设定审批人员</title>

<script type="text/javascript">
	function initialization(){
		window.location.href = '${ctx }/permissionAction/initialization';
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
          	  <li class="selected"><a href="#">审批设定</a></li>
          </ul>
      </div>
      <div style="text-align: right;padding: 2px 1em 2px">
		<input type="button" value="初始化" class="input_button4" onclick="initialization();" />
	  </div>
      <div class="sort_content">
      	<form action=" ${ctx }/processAction/process/listProcess_page" method="post">
          <table class="tableHue1" width="100%" border="1" bordercolor="#a4d5e3" cellspacing="0" cellpadding="0">
              <thead>
                <tr>
					<th>名称</th>
					<th>KEY</th>
					<th>XML</th>
					<th>图片</th>
					<th>操作</th>
                </tr>
              </thead>
              <tbody id="tbody">
	              <c:forEach items="${proDefList }" var="process">
					<tr>
						<td>${process.name }</td>
						<td>${process.key }</td>
						<td><a target="_blank" href='${ctx }/processAction/process/process-definition?processDefinitionId=${process.id}&resourceType=xml'>${process.resourceName }</a></td>
						<td><a target="_blank" href='${ctx }/processAction/process/process-definition?processDefinitionId=${process.id}&resourceType=image'>${process.diagramResourceName }</a></td>
						<td>
	                        <a href='${ctx }/permissionAction/setAuthor?processDefinitionId=${process.id }'>设定人员</a>|
	                        <a href='${ctx }/permissionAction/loadSingleBpmn?processDefinitionId=${process.id}'>加载</a>
	                    </td>
					</tr>
				  </c:forEach>
              	<tr>
              		<td class="fun_area" colspan="5" align="center"><c:out value="${page }" escapeXml="false" /></td>
              	</tr>
              </tbody>
          </table>
         </form>
      </div>
      


</div>
    
</body>

</html>
