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
    <div class="ftitle"><img src="${ctx }/extend/fromedit.png" style="margin-bottom: -3px;"/> 调薪申请信息</div>
    <form id="salary_form" method="post" >
        <div class="fitem">
            <label>姓名：</label>
            <input id="beginDate" name="beginDate" value = "${user_name }" readonly="readonly" class="textbox-text easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>调薪金额:</label>
            <input id="adjustMoney" name="adjustMoney" value = "${salary.adjustMoney }" readonly="readonly" class="easyui-textbox easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>描述:</label>
            <textarea readonly="readonly" rows="4" cols="50">${salary.dscp }</textarea>
        </div>
    </form>
</div>
