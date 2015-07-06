/**
 * 请假管理
 */

var vacation_form;

//初始化表单
/*$(function(){
	
})*/

//表单提交
function submitForm(){
	alert("submit");
	vacation_form = $('#vacation_form').form({
	    url: ctx+"/vacationAction/doAdd",
	    onSubmit: function () {
	        $.messager.progress({
	            title: '提示信息！',
	            text: '数据处理中，请稍后....'
	        });
	        var isValid = $(this).form('validate');
	        if (!isValid) {
	            $.messager.progress('close');
	        }
	        return isValid;
	    },
	    success: function (data) {
	        $.messager.progress('close');
	        var json = $.parseJSON(data);
	        if (json.status) {
	        	audit_dialog.dialog('destroy');//销毁对话框
	        	todoTask_datagrid.datagrid('reload');//重新加载列表数据

	        } 
	        $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
	    }
	});
	vacation_form.submit();
}

//表单重置
function clearForm(){
	vacation_form.form('clear');
}
