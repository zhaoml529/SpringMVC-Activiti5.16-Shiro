<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>用户管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript">
			var $dg;
			var $temp;
			var $grid;
			$(function() {
				$dg = $("#dg");
				$grid=$dg.datagrid({
					url : "${ctx}/userAction/toList",
					width : 'auto',
					height :  $(this).height()-120,
					pagination:true,
					rownumbers:true,
					border:false,
					singleSelect:true,
					striped:true,
					columns : [ [ {field : 'name',title : '用户名',width : fixWidth(0.3),align : 'left',editor : {type:'validatebox',options:{required:true}}},
					              {field : 'passwd',title : '密码',width : fixWidth(0.3),align : 'left',editor : {type:'validatebox',options:{required:true}}},
					              {field : 'registerDate', title : '注册时间', width : fixWidth(0.2), editor : "datebox"},
					              {field : 'locked',title : '状态',width : fixWidth(0.1),
					            	  formatter:function(value,row){
					            		  if("0"==row.locked){
											return "<font color=green>正常<font>";
					            		  }else{
					            			return "<font color=red>停用<font>";  
					            		  }
									  },
					            	  editor : "text"},
					              {field : 'group_name',title : '用户组',width : fixWidth(0.1),editor : "text"}
					              ] ],toolbar:'#tb'
				});
				//搜索框
				$("#searchbox").searchbox({ 
					 menu:"#mm", 
					 prompt :'模糊查询',
				    searcher:function(value,name){   
				    	var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
			            var obj = eval('('+str+')');
			            $dg.datagrid('reload',obj); 
				    }
				   
				});
			});
			
			function fixHeight(percent)   
			{   
				return (document.body.clientHeight) * percent ;    
			}

			function fixWidth(percent)   
			{   
				return (document.body.clientWidth - 50) * percent ;    
			}
			
			function updCellTree(record){
				/*var rows = $dg.datagrid('getSelections');
				$.each(rows,function(i,row){
					if (row) {
						var rowIndex = $dg.datagrid('getRowIndex', row);
						$dg.datagrid('getEditor',{'index':rowIndex,'field':'organizeName'}).target.val(record.text);
					}
				});*/
				var rowIdx = $dg.datagrid('getRowIndex',$dg.datagrid('getSelected'));
				 $dg.datagrid('getEditor',{'index':rowIdx,'field':'organizeName'}).target.val(record.text);

			}
			function endEdit(){
				var flag=true;
				var rows = $dg.datagrid('getRows');
				for ( var i = 0; i < rows.length; i++) {
					$dg.datagrid('endEdit', i);
					var temp=$dg.datagrid('validateRow', i);
					if(!temp){flag=false;}
				}
				return flag;
			}
			function addRows(){
				$dg.datagrid('appendRow', {});
				var rows = $dg.datagrid('getRows');
				$dg.datagrid('beginEdit', rows.length - 1);
				$dg.datagrid('selectRow', rows.length - 1);
				
			}
			function editRows(){
				var rows = $dg.datagrid('getSelections');
				$.each(rows,function(i,row){
					if (row) {
						var rowIndex = $dg.datagrid('getRowIndex', row);
						$dg.datagrid('beginEdit', rowIndex);
					}
				});
			}
			function removeRows(){
				var rows = $dg.datagrid('getSelections');
				$.each(rows,function(i,row){
					if (row) {
						var rowIndex = $dg.datagrid('getRowIndex', row);
						$dg.datagrid('deleteRow', rowIndex);
					}
				});
			}
			function saveRows(){
				if(endEdit()){
					if ($dg.datagrid('getChanges').length) {
						var inserted = $dg.datagrid('getChanges', "inserted");
						var deleted = $dg.datagrid('getChanges', "deleted");
						var updated = $dg.datagrid('getChanges', "updated");
						
						var effectRow = new Object();
						if (inserted.length) {
							effectRow["inserted"] = JSON.stringify(inserted);
						}
						if (deleted.length) {
							effectRow["deleted"] = JSON.stringify(deleted);
						}
						if (updated.length) {
							effectRow["updated"] = JSON.stringify(updated);
						}
						$.post("user/userAction!persistenceUsers.action", effectRow, function(rsp) {
							if(rsp.status){
								$dg.datagrid('acceptChanges');
							}
							$.messager.alert(rsp.title, rsp.message);
						}, "JSON").error(function() {
							$.messager.alert("提示", "提交错误了！");
						});
					}
				}else{
					$.messager.alert("提示", "字段验证未通过!请查看");
				}
			}
			
			//删除
			function delRows(){
				var row = $dg.datagrid('getSelected');
				if(row){
					var rowIndex = $dg.datagrid('getRowIndex', row);
					$dg.datagrid('deleteRow', rowIndex);
					$.ajax({
						url:"user/userAction!delUsers.action",
						data: "userId="+row.userId,
						success: function(rsp){
							parent.$.messager.show({
								title : rsp.title,
								msg : rsp.message,
								timeout : 1000 * 2
							});
						}
					});
				}else{
					parent.$.messager.show({
						title : "提示",
						msg : "请选择行数据!",
						timeout : 1000 * 2
					});
				}
			}
			
			//弹窗修改
			function updRowsOpenDlg() {
				var row = $dg.datagrid('getSelected');
				if (row) {
					parent.$.modalDialog({
						title : "编辑用户",
						width : 600,
						height : 300,
						href : "${ctx}/userAction/toUpdate/"+row.id,
						onLoad:function(){
							var f = parent.$.modalDialog.handler.find("#form");
							f.form("load", row);
						},			
						buttons : [ {
							text : '编辑',
							iconCls : 'icon-ok',
							handler : function() {
								parent.$.modalDialog.openner= $grid;
								var f = parent.$.modalDialog.handler.find("#form");
								f.submit();
							}
						}, {
							text : '取消',
							iconCls : 'icon-cancel',
							handler : function() {
								parent.$.modalDialog.handler.dialog('destroy');
								parent.$.modalDialog.handler = undefined;
							}
						}
						]
					});
				}else{
					parent.$.messager.show({
						title :"提示",
						msg :"请选择一行记录!",
						timeout : 1000 * 2
					});
				}
			}
			//弹窗增加
			function addRowsOpenDlg() {
				parent.$.modalDialog({
					title : "添加用户",
					width : 600,
					height : 400,
					href : "jsp/user/userEditDlg.jsp",
					buttons : [ {
						text : '保存',
						iconCls : 'icon-ok',
						handler : function() {
							parent.$.modalDialog.openner= $grid;
							var f = parent.$.modalDialog.handler.find("#form");
							f.submit();
						}
					}, {
						text : '取消',
						iconCls : 'icon-cancel',
						handler : function() {
							parent.$.modalDialog.handler.dialog('destroy');
							parent.$.modalDialog.handler = undefined;
						}
					}
					]
				});
			}
			//高级搜索 del row
			function userSearchRemove(curr) {
					$(curr).closest('tr').remove();
			}
			//高级查询
			function  userSearch() {
				jqueryUtil.gradeSearch($dg,"#userSearchFm","jsp/user/userSearchDlg.jsp");
			}
		</script>
  </head>
  <body>
 		<div class="well well-small" style="margin-left: 5px;margin-top: 5px">
		<span class="badge">提示</span>
		<p>
			在此你可以对<span class="label-info"><strong>用户</strong></span>进行编辑!
		</p>
	</div>
	
	<div id="tb" style="padding:2px 0">
		<table cellpadding="0" cellspacing="0">
			<tr>
				<td style="padding-left:2px">
					<shiro:hasRole name="admin">
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addRowsOpenDlg();">添加</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="updRowsOpenDlg();">编辑</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delRows();">删除</a>
					</shiro:hasRole>
				<!--  	<shiro:hasPermission name="userEndEdit">
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="endEdit();">结束编辑</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="userSave">
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="saveRows();">保存</a>
					</shiro:hasPermission>-->
				</td>
				<td style="padding-left:2px">
					<input id="searchbox" type="text"/>
				</td>
				<td style="padding-left:2px">
					<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="userSearch();">高级查询</a>
				</td>
			</tr>
		</table>
	</div>
	<div id="mm">
		<div name="myid">用户编码</div>
		<div name="account">用户账户</div>
		<div name="name">用户名</div>
		<div name="email">邮箱</div>
		<div name="tel">电话</div>
		<div name="organizeName">组织</div>
		<div name="description">描述</div>
	</div>
	<table id="dg" title="用户管理"></table>
	
<%-- 	<table class="easyui-datagrid" width="100%" border="1"
			url="${ctx }/userAction/toList"
			toolbar="#toolbar"
			pagination="true"
			rownumbers="true" fitColumns="true" singleSelect="true">
        <thead>
          <tr>
            <th field="name" width="30%">姓名</th>
            <th field="passwd" width="30%">密码(两次加密)</th>
            <th field="registerDate" width="20%">注册时间</th>
            <th field=locked width="10%">状态</th>
            <th field=group_name width="10%">用户组</th>
          </tr>
        </thead>
    </table> --%>
    
  </body>
</html>
