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
<script type="text/javascript">
	var key = "${key }";
	function selectUser( userId, userName ){
		$("#"+key+"_id", window.parent.document).val(userId);
		$("#"+key+"_name", window.parent.document).val(userName);
		window.parent.closeDialogFrame();
	}
	
	function getUserByGroup( groupId, flag ){
		window.location.href = '${ctx }/userAction/chooseUser_page?groupId='+groupId+'&flag='+flag+'&key='+key;
	}
	
	function getValue(){
        var ids='';
        var names='';
        var checked = $("input:checked");//获取所有被选中的标签元素
        for(i=0;i<checked.length;i++){
         	//将所有被选中的标签元素的值保存成一个字符串，以逗号隔开
       	 	var obj = checked[i].value.split("_");
            if(i<checked.length-1){
	           ids+=obj[0]+',';
	           names+=obj[1]+',';
            }else{
               ids+=obj[0];
               names+=obj[1];
            }
        }
        $("#"+key+"_id", window.parent.document).val(ids);
		$("#"+key+"_name", window.parent.document).val(names); 
	}
	
</script>
</head>
<body>
	<input type="hidden" id="key" value="${key }"/>
	<input type="hidden" id="multiSelect" value="${multiSelect }"/>

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
	  
	<input id="group" style="width:150px">
	
	<table id="user_datagrid" title="候选人"></table>
    
</body>

</html>
