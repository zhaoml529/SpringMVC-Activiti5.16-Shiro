<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
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

<script type="text/javascript">
$(function() {
	$("#parentId").combotree({
		width:171,
		url:"${ctx }/resourceAction/getMenuList",
		idFiled:'id',
	 	textFiled:'name',
	 	parentField:'parentId'
	});
});
</script>
<div class="easyui-layout" style="padding:10px 20px">
    <div class="ftitle"><img src="${ctx }/extend/fromedit.png" style="margin-bottom: -3px;"/>用户组信息</div>
    <form id="resource_form" method="post" >
        <div class="fitem">
            <label>资源名称:</label>
            <input id="name" name="name" class="easyui-textbox" required="required">
        </div>
        <div class="fitem">
            <label>资源类型:</label>
			<select id="type" class="easyui-combobox" name="type" style="width:171px;" data-options="required:true">
				<option value="menu">菜单</option>
				<option value="button">操作</option>
			</select>
        </div>
        <div class="fitem">
            <label>资源路径:</label>
            <input id="url" name="url" class="easyui-textbox easyui-validatebox">
        </div>
        <div class="fitem">
            <label>权限字符串:</label>
            <input id="url" name="permission" class="easyui-textbox" required="required">
        </div>
        <div class="fitem">
            <label>父编号名称:</label>
            <input name="parentId"  class="easyui-combotree" id="parentId" type="text"/>
        </div>
        <div class="fitem">
            <label>父编号列表:</label>
            <input name="parentIds"  class="easyui-textbox" id="parentIds" type="text"/>
        </div>
        <div class="fitem">
            <label>是否启用:</label>
            <select id="available" class="easyui-combobox" name="available" style="width:171px;" data-options="required:true">
				<option value="1">是</option>
				<option value="0">否</option>
			</select>
        </div>
    </form>
</div>

