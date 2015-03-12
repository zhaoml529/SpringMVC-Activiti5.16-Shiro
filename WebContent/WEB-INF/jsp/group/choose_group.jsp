<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">    
<title>选择候选用户组</title>
<script type="text/javascript">
	var key = "${key }";
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
	<div id="main">
      <div class="sort_content">
      	<form action="${ctx }/groupAction/chooseGroup_page?key=${key}" method="post">
          <table class="tableHue1" width="100%" border="1" bordercolor="#a4d5e3" cellspacing="0" cellpadding="0">
              <thead>
                <tr>
                  <td width="5%"></td>
                  <td width="45%"><strong>名称</strong></td>
                  <td width="50%"><strong>类型</strong></td>
                </tr>
              </thead>
              <tbody id="tbody">
              	<c:forEach var="group" items="${groupList}">
	                <tr>
	                  <td align="center"><input id="check_${user.id }" value="${group.id }_${group.name }" name="ids" type="checkbox" /></td>
	                  <td>${group.name}</td>
	                  <td>${group.type}</td>
	                </tr>
                </c:forEach>
                <tr>
              		<td class="fun_area" colspan="3" align="center"><c:out value="${page }" escapeXml="false" /></td>
              	</tr>
              </tbody>
          </table>
        </form>
      </div>
      
	</div>
    
</body>

</html>
