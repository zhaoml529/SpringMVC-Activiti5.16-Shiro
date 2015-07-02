/**
 * 任务管理（待办任务、已完成任务）
 */

var todoTask_datagrid;
var audit_form;

var process_dialog;

$(function() {
	showToDoTask();
	
	$('#tabs').tabs({
	    border:false,
	    onSelect:function(title,index){
	        if(index == 0){
	        	showToDoTask();
	        }else if(index == 1){
	        	showFinishedTask();
	        }
	    }
	});

});

//修正宽高
function fixHeight(percent)   
{   
	return parseInt($(this).width() * percent);
}

function fixWidth(percent)   
{   
	return parseInt(($(this).width() - 80) * percent);
}

//待办任务列表
function showToDoTask(){
	todoTask_datagrid = $("#todoTask").datagrid({
        url: ctx+"/processAction/todoTask",
        width : 'auto',
		height :  $(this).height()-135,
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
		columns : [ 
		    [ 
                {field : 'businessType',title : '单据类型',width : fixWidth(0.1),align : 'center',
                	formatter:function(value, row){
                		if(value == "vacation"){
                			return "请假申请";
                		}else if(value == "salary"){
                			return "薪资调整";
                		}else if(value == "expense"){
                			return "报销申请";
                		}
					}
                },
                {field : 'user_name',title : '申请人',width : fixWidth(0.2),align : 'center'},                
                {field : 'title',title : '标题',width : fixWidth(0.3),align : 'center'},
                {field : 'taskName',title : '当前节点',width : fixWidth(0.1),align : 'center',
                	formatter:function(value, row){
                		return "<a class='trace' onclick=\"graphTrace('"+row.processInstanceId+"')\" id='diagram' href='#' pid='"+row.id+"' pdid='"+row.processDefinitionId+"' title='see'>"+value+"</a>";
                	}
                },
                {field : 'createTime',title : '任务创建时间',width : fixWidth(0.2),align : 'center',
					formatter:function(value,row){
						return moment(value).format("YYYY-MM-DD HH:mm:ss");
					}
                },
                {field : 'suspended',title : '流程状态',width : fixWidth(0.1),align : 'center',
                	formatter:function(value, row){
                		if(value){
                			return "已挂起";
                		}else{
                			return "正常";  
                		}
                	}
                }
            ] 
		],
		toolbar: "#toolbar"
	});
}


//已完成的任务列表
function showFinishedTask(){
	alert("finished task!");
}

//初始化审批表单
function approvalFormInit( businessType, taskId ) {
	var _url;
	if("vacation" == businessType){
		_url = ctx+'/vacationAction/complate/'+taskId;
	}else if("salary" == businessType){
		_url = ctx+'/salaryAction/complate/'+taskId;
	}else if("expense" == businessType){
		_url = ctx+'/expenseAction/complate/'+taskId;
	}
	audit_form = $('#audit_form').form({
        url: _url,
        onSubmit: function (param) {
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
}

function handleTask(){
	var row = todoTask_datagrid.datagrid('getSelected');
    if (row) {
    	var _url;
    	if("vacation" == row.businessType){
    		_url = ctx + "/vacationAction/toApproval/"+row.taskId;
    	}else if("salary" == row.businessType){
    		_url = ctx + "/salaryAction/toApproval/"+row.taskId;
    	}else if("expense" == row.businessType){
    		_url = ctx + "/expenseAction/toApproval/"+row.taskId;
    	}
    	
    	//弹出对话窗口
    	audit_dialog = $('<div/>').dialog({
        	title : "任务信息",
    		top: 20,
    		width : 600,
    		height : 420,
            modal: true,
            minimizable: true,
            maximizable: true,
            href: _url,
            onLoad: function () {
                approvalFormInit( row.businessType, row.taskId );
            },
            buttons: [
				{
				    text: '通过',
				    iconCls: 'icon-ok',
				    handler: function () {
				    	$("#completeFlag").val("true");
				    	audit_form.submit();
				    }
				},
                {
                    text: '不通过',
                    iconCls: 'icon-remove',
                    handler: function () {
                    	$("#completeFlag").val("false");
				    	audit_form.submit();
                    }
                },
                {
                    text: '关闭',
                    iconCls: 'icon-cancel',
                    handler: function () {
                    	audit_dialog.dialog('destroy');
                    }
                },
            ],
            onClose: function () {
            	audit_dialog.dialog('destroy');
            }
        });
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}



//挂起、激活
function suspended( status, id ){
	var url_ = ctx+"/processAction/process/updateProcessStatusByProInstanceId/active/"+id;
	if(status == "suspend"){
		url_ = ctx+"/processAction/process/updateProcessStatusByProInstanceId/suspend/"+id;
	}
	
	$.ajax({
		type: "POST",
		url: url_,
		data: {},
		success: function (data) {
			$.messager.progress("close");
			if (data.status) {
				todoTask_datagrid.datagrid("reload"); //reload the process data
			} 
			$.messager.show({
				title : data.title,
				msg : data.message,
				timeout : 1000 * 2
			});
		},
		beforeSend:function(){
			$.messager.progress({
				title: '提示信息！',
				text: '数据处理中，请稍后....'
			});
		},
		complete: function(){
			$.messager.progress("close");
		}
	});
}


