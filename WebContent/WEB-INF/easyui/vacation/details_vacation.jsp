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
    <div class="ftitle"><img src="${ctx }/extend/fromedit.png" style="margin-bottom: -3px;"/> 请假信息</div>
    <form id="vacation_form" method="post" >
        <div class="fitem">
            <label>开始日期：</label>
            <input id="beginDate" name="beginDate" value = "${vacation.beginDate }" readonly="readonly" class="easyui-textbox easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>结束日期:</label>
            <input id="endDate" name="endDate" value = "${vacation.endDate }" readonly="readonly" class="easyui-textbox easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>请假天数:</label>
            <input id="days" name="days" value="${vacation.days }" readonly="readonly" class="easyui-textbox easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>休假类型:</label>
            <input type="hidden" id="vacationType" value="${vacation.vacationType }">
			<select id="type" class="easyui-combobox" disabled="disabled" name="vacationType" style="width:160px;">
			    <option value="0">年假</option>
			    <option value="1">事假</option>
			    <option value="2">病假</option>
			</select>
        </div>
        <div class="fitem">
            <label>原因:</label>
            <textarea readonly="readonly" rows="4" cols="50">${vacation.reason }</textarea>
            <%-- <input id="reason" name="reason"  value = "${vacation.reason }" class="easyui-textbox" required="true"> --%>
        </div>
    </form>
</div>
