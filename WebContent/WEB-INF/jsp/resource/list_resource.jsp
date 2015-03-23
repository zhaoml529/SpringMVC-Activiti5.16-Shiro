<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />    
<title>资源列表</title>
<script type="text/javascript">
	$(function() {
		var message = "${message}";
		if(message != ""){
			$( "#dialog-complete" ).dialog({
			      modal: true,
			      buttons: {
			        Ok: function() {
			          $( this ).dialog( "close" );
			        }
			      }
		    });
		}
	})
	
	function toAdd(){
		window.location.href="${ctx}/resourceAction/toAdd";
	}
</script>
</head>

<body>

	<div id="main">
      <div id="dialog-complete" title="complete" style="display: none;">
		    <span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 1px 5px 0;"></span>
		  	<span id="message">${message }</span>
	  </div>  
      <div class="sort_switch">
          <ul id="TabsNav">
          	  <li class="selected"><a href="#">资源列表</a></li>
          </ul>
      </div>
      <div style="text-align: right;padding: 2px 1em 2px">
      	<button onclick="toAdd();" class="input_button4">添加</button>
      </div>
      <div class="sort_content">
      	<form action=" ${ctx }/resourceAction/listResource_page" method="post">
          <table class="tableHue1" width="100%" border="1" bordercolor="#a4d5e3" cellspacing="0" cellpadding="0">
              <thead>
                <tr>
                  <td width="10%"><strong>资源名称</strong></td>
                  <td width="5%"><strong>资源类型</strong></td>
                  <td width="35%"><strong>资源路径</strong></td>
                  <td width="20%"><strong>权限字符串</strong></td>
                  <td width="10%"><strong>父编号</strong></td>
                  <td width="10%"><strong>父编号列表</strong></td>
                  <td width="10%"><strong>操作</strong></td>
                </tr>
              </thead>
              <tbody id="tbody">
              <c:forEach items="${list}" var="resource">
                <tr>
                  <td>${resource.name}</td>
                  <td align="center">${resource.type}</td>
                  <td>${resource.url}</td>
                  <td>${resource.permission}</td>
                  <td>${resource.parentId}</td>
                  <td>${resource.parentIds}</td>
                  <td align="center">
                  	  <a href="${ctx }/resourceAction/toUpdate/${resource.id}">修改</a>|
                  	  <a href="${ctx }/resourceAction/doDelete/${resource.id}" onclick="javascript:return confirm('确定删除此资源？')">删除</a>|
                  </td>
                </tr>
              </c:forEach>
              	<tr>
              		<td class="fun_area" colspan="7" align="center"><c:out value="${page }" escapeXml="false"/></td>
              	</tr>
              </tbody>
          </table>
         </form>
      </div>
      


</div>
    
</body>

</html>
