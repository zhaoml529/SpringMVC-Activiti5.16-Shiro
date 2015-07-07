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
    <div class="ftitle"><img src="${ctx }/extend/fromedit.png" style="margin-bottom: -3px;"/> 薪资调整审批</div>
    <form id="audit_form" method="post" >
    	<input type="hidden" name="userId" value="${user.id }" />
		<input type="hidden" name="salaryAdjustId" value="${salary.id }" />
		<input type="hidden" id="completeFlag" name="completeFlag" value="" />
        <div class="fitem">
            <label>姓名：</label>
            <input id="beginDate" name="beginDate" value = "${salary.user_name }" readonly="readonly" class="textbox-text easyui-validatebox" required="true">
        </div>
        <div class="fitem">
            <label>调薪金额:</label>
            <input id="adjustMoney" name="adjustMoney" value = "${salary.adjustMoney }" readonly="readonly" class="easyui-numberbox easyui-validatebox" required="true" data-options="min:0,precision:2,groupSeparator:',',prefix:'￥'">
        </div>
        <div class="fitem">
            <label>描述:</label>
            <textarea readonly="readonly" rows="4" cols="61">${salary.dscp }</textarea>
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
        <div class="fitem">
            <label>我的意见:</label>
            <textarea rows="4" cols="61"  name="content"></textarea>
        </div>
    </form>
</div>
