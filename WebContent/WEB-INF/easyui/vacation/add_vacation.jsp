<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>

<!DOCTYPE html>
<html>
  <head>
    <title>请假管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${ctx}/js/app/vacation.js?_=${sysInitTime}"></script>
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
	        text-align: right;
	    }
	    .fitem input{
	        width:160px;
	        margin-left: 150px;
	    }
	</style>
  </head>
  <body>
  	<div id="dlg" class="easyui-layout" style="padding:10px 20px">
	    <div class="ftitle"><img src="${ctx }/extend/fromedit.png" style="margin-bottom: -3px;"/> 请假信息</div>
	    <form id="vacation_form" method="post">
	        <div class="fitem">
	            <label>开始日期:</label>
	            <input id="beginDate" name="beginDate" class="easyui-datebox" required="true" style="width: 160px;">
	        </div>
	        <div class="fitem">
	            <label>结束日期:</label>
	            <input id="endDate" name="endDate" class="easyui-datebox" required="true" style="width: 160px;">
	        </div>
	        <div class="fitem">
	            <label>请假天数:</label>
	            <input id="days" name="days" class="easyui-numberspinner easyui-validatebox" required="true" data-options="min:1,max:60" style="width: 160px;">
	        </div>
	        <div class="fitem">
	            <label>休假类型:</label>
				<select id="vacationType" class="easyui-combobox" name="vacationType" style="width:160px;">
				    <option value="0">年假</option>
				    <option value="1">事假</option>
				    <option value="2">病假</option>
				</select>
	        </div>
	        <div class="fitem">
	            <label>原因:</label>
	            <input id="reason" name="reason" class="easyui-textbox" data-options="multiline:true" style="height:70px; width: 300px">
	        </div>
	        <div style="padding:20px 0px 0px 0px;">
				<a href="#" id="save" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="submitForm()" style="width:80px">申请</a>
				<a href="#" id="clear" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" onclick="clearForm()" style="width:80px">重置</a>
			</div>
	    </form>
	</div>
  </body>
</html>