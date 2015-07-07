/**
 * 添加薪资调整申请
 */

var salary_form;

//初始化表单
$(function(){
	salary_form = $('#salary_form').form({
	    url: ctx+"/salaryAction/doAdd",
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
})

//表单提交
function submitForm(){
	salary_form.submit();
}

//表单重置
function clearForm(){
	salary_form.form('clear');
}
