/**
 * 添加请假申请
 */

var vacation_form;

//初始化表单
$(function(){
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
	$('#beginDate').datebox().datebox('calendar').calendar({
		validator: function(date){
			var now = new Date();
			var d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
			return d1<=date;
		}
	});
	
	//只能选取beginDate之后的日期
	$('#beginDate').datebox({
		onSelect: function(beginDate){
			$('#endDate').datebox().datebox('calendar').calendar({
				validator: function(date){
					var d1 = new Date(beginDate.getFullYear(), beginDate.getMonth(), beginDate.getDate());
					return d1<=date;
				}
			});
		}
	});
	
})

//表单提交
function submitForm(){
	vacation_form.submit();
}

//表单重置
function clearForm(){
	vacation_form.form('clear');
}
