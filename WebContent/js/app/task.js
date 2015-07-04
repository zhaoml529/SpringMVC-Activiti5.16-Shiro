/**
 * 任务管理（待办任务、已完成任务）
 */

var todoTask_datagrid;
var endTask_datagrid;
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
	        	showEndTask();
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
				{field : 'assign',title : '任务状态',width : fixWidth(0.1),align : 'center',
					formatter:function(value, row){
						if(value == null){
							return "待签收";
						}else{
							return "待办理";  
						}
					}
				},
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
                {field : 'createTime',title : '任务创建时间',width : fixWidth(0.1),align : 'center',
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
function showEndTask(){
	todoTask_datagrid = $("#endTask").datagrid({
        url: ctx+"/processAction/endTask",
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
                {field : 'user_name',title : '申请人',width : fixWidth(0.1),align : 'center'},                
                {field : 'title',title : '标题',width : fixWidth(0.3),align : 'center'},
                {field : 'startTime',title : '任务开始时间',width : fixWidth(0.1),align : 'center',
					formatter:function(value,row){
						return moment(value).format("YYYY-MM-DD HH:mm:ss");
					}
                },
                {field : 'claimTime',title : '任务签收时间',width : fixWidth(0.1),align : 'center',
                	formatter:function(value,row){
                		if(value != null){
                			return moment(value).format("YYYY-MM-DD HH:mm:ss");
                		}else{
                			return "无需签收"
                		}
                	}
                },
                {field : 'endTime',title : '任务结束时间',width : fixWidth(0.1),align : 'center',
                	formatter:function(value,row){
                		return moment(value).format("YYYY-MM-DD HH:mm:ss");
                	}
                },
                {field : 'deleteReason',title : '流程结束原因',width : fixWidth(0.1),align : 'center',
                	formatter:function(value,row){
                		/** The reason why this task was deleted {'completed' | 'deleted' | any other user defined string }. */
                		return value;
                	}
                },
                {field : 'version',title : '流程版本号',width : fixWidth(0.1),align : 'center'}
            ] 
		]
	});
	
}

//初始化审批表单
function approvalFormInit( taskDefinitionKey, businessType, taskId ) {
	var _url;
	if("vacation" == businessType){
		//正常审批
		_url = ctx+'/vacationAction/complate/'+taskId;
		if("modifyApply" == taskDefinitionKey){
			//申请人修改申请
			_url = ctx+'/vacationAction/modifyVacation/'+taskId;
		}
	}else if("salary" == businessType){
		//正常审批
		_url = ctx+'/salaryAction/complate/'+taskId;
		if("modifyApply" == taskDefinitionKey){
			//申请人修改申请
			_url = ctx+'/salaryAction/modifySalary/'+taskId;
		}
	}else if("expense" == businessType){
		//正常审批
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

//办理
function handleTask(){
	var row = todoTask_datagrid.datagrid('getSelected');
    if (row) {
    	if(row.assign == null){
    		$.messager.alert("提示", "此任务您还没有签收，请【签收】任务后再进行办理任务！");
    	}else{
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
    			width : 700,
    			height : 500,
    			modal: true,
    			minimizable: true,
    			maximizable: true,
    			href: _url,
    			onLoad: function () {
    				approvalFormInit( row.taskDefinitionKey, row.businessType, row.taskId );
    				if("vacation" == row.businessType){
    					$("#type").combobox('select', type);
    				}
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
    			          }
    			],
	            onClose: function () {
	        	    audit_dialog.dialog('destroy');
	            }
    		});
    		
    		if(row.taskDefinitionKey.indexOf("modify") != -1){
    			audit_dialog.dialog({
    				buttons: [
        			          {
        			        	  text: '重新申请',
        			        	  iconCls: 'icon-ok',
        			        	  handler: function () {
        			        		  $("#reApply").val("true");
        			        		  audit_form.submit();
        			        	  }
        			          },
        			          {
        			        	  text: '取消申请',
        			        	  iconCls: 'icon-remove',
        			        	  handler: function () {
        			        		  $("#reApply").val("false");
        			        		  audit_form.submit();
        			        	  }
        			          },
        			          {
        			        	  text: '关闭',
        			        	  iconCls: 'icon-cancel',
        			        	  handler: function () {
        			        		  audit_dialog.dialog('destroy');
        			        	  }
        			          }
        			]
    			})
    			alert(row.taskDefinitionKey);
    		}
    	}
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//签收
function claimTask(){
	var row = todoTask_datagrid.datagrid('getSelected');
    if (row) {
    	if(row.assign != null){
    		$.messager.alert("提示", "您已经签收此任务，根据任务状态进行【办理】任务！");
    	}else{
    		$.ajax({
    			type: "POST",
    			url: ctx+"/processAction/claim/"+row.taskId,
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
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}


