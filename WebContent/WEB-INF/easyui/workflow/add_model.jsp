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

<div id="dlg" class="easyui-layout" style="padding:10px 20px">
    <div class="ftitle"><img src="${ctx }/extend/fromedit.png" style="margin-bottom: -3px;"/> 模型信息</div>
    <form id="model_form" action="${ctx}/modelAction/create" method="post" target="_blank">
        <div class="fitem">
            <label>名称:</label>
            <input id="name" name="name" class="easyui-textbox easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>KEY:</label>
            <input id="key" name="key" class="easyui-textbox easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>描述:</label>
            <textarea name="description" id="description" class="easyui-textarea  easyui-validatebox"></textarea>
        </div>
    </form>
</div>

