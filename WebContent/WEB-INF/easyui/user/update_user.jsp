<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<<script type="text/javascript">
$(function(){
	$("#group1").combobox({
		width:160,
		url:"${ctx }/groupAction/getGroupList",
		valueField: 'id',
		textField: 'name',
		onSelect:function(value){
			$("#group_name").val(value.name);
		},
		required: true,
		onLoadSuccess: function (data) {
			var groupId = $("#group_id").val();
            $("#group").combobox('setValue',groupId);
        }
	});
})
</script>
<style type="text/css">
    #fm{
        margin:0;
        padding:10px 30px;
    }
    .ftitle{
        font-size:14px;
        font-weight:bold;
        padding:5px 0;
        margin-bottom:10px;
        border-bottom:1px solid #ccc;
    }
    .fitem{
        margin-bottom:5px;
    }
    .fitem label{
        display:inline-block;
        width:80px;
    }
    .fitem input{
        width:160px;
    }
</style>

<div id="dlg" class="easyui-layout" style="padding:10px 20px">
    <div class="ftitle"><img src="${ctx }/extend/fromedit.png" style="margin-bottom: -3px;"/> 用户信息</div>
    <form id="user_form" method="post" >
		<input type="hidden" name="id" id="id" />
		<input type="hidden" name="salt" id="salt" />
		<input type="hidden" name="registerDate" id="registerDate" />
        <div class="fitem">
            <label>用户名:</label>
            <input id="name" name="name" class="easyui-textbox easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>密码:</label>
            <input type="password" id="passwd"
                   name="passwd" class="easyui-textbox easyui-validatebox" maxLength="36"
                   data-options="required:true,missingMessage:'请输入密码.',validType:['minLength[1]']">
        </div>
        <div class="fitem">
            <label>确认密码:</label>
            <input type="password" id="repasswd"
                   name="repasswd" class="easyui-textbox easyui-validatebox" required="true"
                   missingMessage="请再次填写密码." validType="equalTo['#passwd']"
                   invalidMessage="两次输入密码不匹配.">
        </div>
        <div class="fitem">
            <label>用户组:</label>
<%--             <select id="group" class="easyui-combobox" name="group" style="width:160px;" data-options="required:true">
				<c:forEach var="group" items="${groupList}">
					<option value="${group.id}" >${group.name}-${group.type}</option>
				</c:forEach>
			</select> --%>
			<select name="group.id">
				<c:forEach var="group" items="${groupList}">
					<option value="${group.id}" ${user.group.id == group.id?'selected':'' }>${group.name}-${group.type}</option>
				</c:forEach>
			</select>
			<input name="group_name" id="group_name" type="hidden" />
        </div>
        <div class="fitem">
            <label>状态:</label>
            <label style="text-align: left;width: 60px;">
                <input type="radio" id="locked" name="locked" style="width: 20px;" value="0" /> 启用
            </label>
            <label style="text-align: left;width: 60px;">
                <input type="radio" id="locked" name="locked" style="width: 20px;" value="3" /> 停用
            </label>
        </div>
    </form>
</div>

