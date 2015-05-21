<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
	$(function() {
	user_form = $('#form').form({
			url :"${ctx}/userAction/doUpdate",
			onSubmit : function() {
				//在表单提交之前触发，返回false将阻止表单提交
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				var isValid = $(this).form('validate');
				if (!isValid) {
					parent.$.messager.progress('close');
				}
				return isValid;
			},
			success : function(result) {
				parent.$.messager.progress('close');
				alert(result.status);
				if (result.status) {
					parent.reload;
					parent.$.modalDialog.openner.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_datagrid这个对象，是因为role.jsp页面预定义好了
					parent.$.modalDialog.handler.dialog('close');
					parent.$.messager.show({
						title : result.title,
						msg : result.message,
						timeout : 1000 * 2
					});
				}else{
					parent.$.messager.show({
						title :  result.title,
						msg : result.message,
						timeout : 1000 * 2
					});
				}
			}
		});
	});
	
</script>
<style>

	
	textarea:focus, input[type="text"]:focus{
	    border-color: rgba(82, 168, 236, 0.8);
	    box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset, 0 0 8px rgba(82, 168, 236, 0.6);
	    outline: 0 none;
		}
		table {
	    background-color: transparent;
	    border-collapse: collapse;
	    border-spacing: 0;
	    max-width: 100%;
	}

	fieldset {
	    border: 0 none;
	    margin: 0;
	    padding: 0;
	}
	legend {
	    -moz-border-bottom-colors: none;
	    -moz-border-left-colors: none;
	    -moz-border-right-colors: none;
	    -moz-border-top-colors: none;
	    border-color: #E5E5E5;
	    border-image: none;
	    border-style: none none solid;
	    border-width: 0 0 1px;
	    color: #999999;
	    line-height: 20px;
	    display: block;
	    margin-bottom: 10px;
	    padding: 0;
	    width: 100%;
	}
	input, textarea {
	    font-weight: normal;
	}
	table ,th,td{
		text-align:left;
		padding: 6px;
	}
</style>

<div class="easyui-layout" data-options="fit:true,border:false">
<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 10px;">
	<form id="form" method="post" novalidate>
		<fieldset>
			<legend><img src="${ctx }/extend/fromedit.png" style="margin-bottom: -3px;"/> 用户编辑</legend>
			<input type="hidden" name="id" id="id" />
			<input type="hidden" name="salt" id="salt" />
			<input type="hidden" name="registerDate" id="registerDate" />
			 <table>
				 <tr>
				    <th>用户名</th>
					<td><input name="name" id="name" placeholder="请输入用户名" class="easyui-textbox"  type="text" required="required"/></td>
					<th>密码</th>
					<td><input id="passwd" name="passwd" type="password" class="easyui-textbox easyui-validatebox"  required="required" /></td>
				 </tr>
				 <tr>
				    <th>用户组</th>
					<td>
						<select id="group" class="easyui-combobox" name="group.id" style="width:171px;" data-options="required:true">
							<c:forEach var="group" items="${groupList}">
								<option value="${group.id}" >${group.name}-${group.type}</option>
							</c:forEach>
						</select>
					</td>
				    <th>状态</th>
					<td>
						<select id="locked" class="easyui-combobox" name="locked" style="width:171px;" data-options="required:true">
							<option value="0">正常</option>
							<option value="1">锁定</option>
						</select>
					</td>
				 </tr>
			 </table>
		</fieldset>
	</form>
</div>
</div>

