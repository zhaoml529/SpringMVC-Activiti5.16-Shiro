/**
 * 添加报销申请
 */

var expense_form;

//初始化表单
$(function(){
	expense_form = $('#expense_form').form({
	    url: ctx+"/expenseAction/doAdd",
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
	        	$("#save").linkbutton('disable');
	        	$("#clear").linkbutton('disable');
	        } 
	        $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
	    }
	});
	//只能选取今天以后的日期
	$('#occurDate').datebox().datebox('calendar').calendar({
		validator: function(date){
			var now = new Date();
			var d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
			return date<=d1;
		}
	});
	
})

//表单提交
function submitForm(){
	expense_form.submit();
}

//表单重置
function clearForm(){
	expense_form.form('clear');
}
