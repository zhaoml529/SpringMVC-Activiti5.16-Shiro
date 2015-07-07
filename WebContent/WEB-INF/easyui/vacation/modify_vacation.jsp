<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
    <form id="audit_form" method="post" >
    	<input type="hidden" name="id" value="${vacation.id }" />
		<input type="hidden" name="processInstanceId" value="${vacation.processInstanceId }" />
		<input type="hidden" name="userId" value="${user.id }" />
		<input type="hidden" id="reApply" name="reApply" value="" />
        <div class="fitem">
            <label>开始日期：</label>
            <input id="beginDate" name="beginDate" value = "<fmt:formatDate value="${vacation.beginDate }" type="date" />" class="easyui-datebox" required="true">
        </div>
        <div class="fitem">
            <label>结束日期:</label>
            <input id="endDate" name="endDate" value = "<fmt:formatDate value="${vacation.endDate }" type="date" />" class="easyui-datebox" required="true">
        </div>
        <div class="fitem">
            <label>请假天数:</label>
            <input id="days" name="days" value="${vacation.days }" class="easyui-numberspinner easyui-validatebox" required="true" data-options="min:1,max:60">
        </div>
        <div class="fitem">
            <label>休假类型:</label>
            <input type="hidden" id="vacationType" value="${vacation.vacationType }">
			<select id="type" class="easyui-combobox easyui-validatebox" name="vacationType" style="width:160px;" required="true">
			    <option value="0">年假</option>
			    <option value="1">事假</option>
			    <option value="2">病假</option>
			</select>
        </div>
        <div class="fitem">
            <label>原因:</label>
            <input id="reason" name="reason" class="easyui-textbox" value="${vacation.reason }" data-options="multiline:true" style="height:70px; width: 300px">
        </div>
        <div class="fitem">
            <label>评论:</label>
            <c:choose>
         		<c:when test="${empty commentList }">
         			暂无评论！
           		</c:when>
            	<c:otherwise>
            		<div style="display: inline-block;">
            		<table class="easyui-datagrid" style="width:450px;" data-options="fitColumns:true,singleSelect:true">
					    <thead>
							<tr>
								<th data-options="field:'userName',width:100,align:'center'">评论人</th>
								<th data-options="field:'time',width:100,align:'center'">评论时间</th>
								<th data-options="field:'content',width:200,align:'center'">评论内容</th>
							</tr>
					    </thead>
					    <tbody>
					    	<c:forEach var="comment" items="${commentList}">
								<tr>
									<td>${comment.userName}</td>
									<td><fmt:formatDate value="${comment.time }" type="date" /></td>
									<td>${comment.content}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					</div>
            	</c:otherwise>
            </c:choose>
        </div>
    </form>
</div>
