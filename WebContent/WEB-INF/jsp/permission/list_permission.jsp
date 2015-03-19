<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />    
<title>权限列表</title>
<script type="text/javascript">

	function addPermission( resourceId, groupId ){
		$.ajax({
			type : "post",
			url : "${ctx}/permissionAction/addPermission",
			data : {"resourceId":resourceId,"groupId":groupId},
			success : function(data){
				if(data == "success"){
					alert("添加成功！");
				}else{
					alert("添加失败！");
				}
			}
		});
	}
	
	function delPermission( id ){
		$.ajax({
			type : "post",
			url : "${ctx}/permissionAction/delPermission",
			data : {"id":id},
			success : function(data){
				if(data == "success"){
					alert("取消成功！");
				}else{
					alert("取消失败！");
				}
			}
		});
	}
</script>
</head>

<body>

	<div id="main">
      <div class="sort_switch">
          <ul id="TabsNav">
          	  <li class="selected"><a href="#">权限列表</a></li>
          </ul>
      </div>
      <c:set value="${garMap }" var="gar"/>
      <div class="sort_content">
      	<form action=" ${ctx }/permissionAction/listPermission_page" method="post">
      	  <input type="hidden" name="groupId" value="${groupId}"/>
          <table class="tableHue1" width="100%" border="1" bordercolor="#a4d5e3" cellspacing="0" cellpadding="0">
              <thead>
                <tr>
                  <td></td>
                  <td width="30%"><strong>资源名称</strong></td>
                  <td width="10%"><strong>资源类型</strong></td>
                  <td width="50%"><strong>资源路径</strong></td>
                  <td width="50%"><strong>权限字符串</strong></td>
                </tr>
              </thead>
              <tbody id="tbody">
              <c:forEach items="${resList}" var="res">
              	<c:set value="${gar[res.id]}" var="garId"/>
                <tr>
                  <td>
	                  <c:if test="${res.parentId != 0 }">
	                  	  <c:choose>
	                  	  	<c:when test="${garId != null}">
			                  <input type="checkbox" checked="checked" name="resourceId" value="${res.id }" onclick="delPermission('${garId}');" />
	                  	  	</c:when>
	                  	  	<c:otherwise>
	                  	  	  <input type="checkbox" name="resourceId" value="${res.id }" onclick="addPermission(this.value,'${groupId}');" />
	                  	  	</c:otherwise>
	                  	  </c:choose>
	                  </c:if>
	              </td>
                  <td>${res.name}</td>
                  <td>${res.type}</td>
                  <td>${res.url}</td>
                  <td>${res.permission}</td>
                </tr>
              </c:forEach>
              	<tr>
              		<td class="fun_area" colspan="5" align="center"><c:out value="${page }" escapeXml="false"/></td>
              	</tr>
              </tbody>
          </table>
         </form>
      </div>
      


</div>
    
</body>

</html>
