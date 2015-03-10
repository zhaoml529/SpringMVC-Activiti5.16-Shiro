<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />    
<title>选取审批人员</title>
<script type="text/javascript" src="${ctx}/js/jquery.multiselect.min.js"></script>
<script type="text/javascript">
	//var api = frameElement.api;
	//var W = api.opener;
	
	function selectUser( userId, userName ){
		var key = "${key }";
		alert(userId+"---"+userName+"---"+key);
		$("#"+key+"_id", window.top.document).val(userId);
		$("#modelTable", window.top.document).find("").val(userName);
		//W.document.getElementById(key+"_id").value = userId ;
		//W.document.getElementById(key+"_name").value = userName;
		//api.close();
	}
	
	function getUserByGroup( groupId, flag ){
		window.location.href = '${ctx }/userAction/chooseUser?groupId='+groupId+'&flag='+flag;
	}
	
	$(function() {
	    $(':checkbox').change(function () {
			var t = ''; 
			var d = {};
			$(':checked').each(function () { 
				if (d[this.value]) return; 
				d[this.value] = 1;
				t += (t ? ',' : '') + this.value 
			});
			//$('#p').val(t); 
			alert(t);
		});
	});
	
</script>
</head>
<body>
      <div style="text-align: left;padding: 2px 1em 2px">
	      用户组：<select id="group"  name="subject" onchange="getUserByGroup(this.value,'${flag }')">
			<option value="-1"> -- 所有人员 -- </option> 
			<c:choose>
				<c:when test="${empty groupList }">
					<option value="-1">-- 所有人员 --</option>
				</c:when>
				<c:otherwise>
					<c:forEach items="${groupList}" var="group">
						<option ${groupId == group.id?'selected':'' } value="${group.id}">${group.name}</option>
					</c:forEach>
				</c:otherwise>
			</c:choose>  
		  </select>
	  </div>
      <div class="sort_content">
      	<form action="${ctx }/userAction/toList_page" method="post">
          <table class="tableHue2" width="100%" border="1" bordercolor="#a4d5e3" cellspacing="0" cellpadding="0">
              <thead>
                <tr>
                  <td></td>
                  <td width="30%"><strong>姓名</strong></td>
                  <td width="10%"><strong>注册时间</strong></td>
                  <td width="50%"><strong>所属用户组</strong></td>
                </tr>
              </thead>
              <tbody id="tbody">
              <c:forEach items="${userList}" var="user">
                <tr>
                  <td align="center">
	               	  <c:if test="${flag }">
	                  	 <input id="check_${user.id }" name="ids" type="checkbox" onchange=''>
	                  </c:if>
	                  <c:if test="${!flag }">
						 <input type="radio" name="id" value="${user.id }" onclick="selectUser(this.value,'${user.name }');" />
	                  </c:if>
				  </td>
                  <td>${user.name}</td>
                  <td>${user.registerDate}</td>
                  <td>${user.group.name}</td>
                </tr>
              </c:forEach>
              	<tr>
              		<td class="fun_area" colspan="4" align="center"><c:out value="${page }" escapeXml="false"/></td>
              	</tr>
              </tbody>
          </table>
         </form>
	</div>
    
</body>

</html>
