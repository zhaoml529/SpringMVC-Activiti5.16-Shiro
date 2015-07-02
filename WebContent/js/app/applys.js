/**
 * 查看请假申请、薪资调整申请、报销申请
 */

var apply_datagrid;
var details_dialog;

var vacation_form;


$(function() {
	//初始化vacation页面
	showApply("vacation");
	
	$('#tabs').tabs({
	    border:false,
	    onSelect:function(title,index){
	        if(index == 0){
	        	showApply("vacation");
	        }else if(index == 1){
	        	showApply("salary");
	        }else{
	        	showApply("expense");
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

function showApply( businessType ){
	var url_ = ctx+"/processAction/process/runingProcessInstance/"+businessType+"/list";
	apply_datagrid = $("#"+businessType+"_datagrid").datagrid({
        url: url_,
        width : 'auto',
		height :  $(this).height()-135,
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
		columns : [ 
		    [ 
                {field : 'userName',title : '申请人',width : fixWidth(0.1),align : 'center'},
                {field : 'title',title : '标题',width : fixWidth(0.3),align : 'center'},
                {field : 'taskName',title : '当前节点',width : fixWidth(0.3),align : 'center',
                	formatter:function(value, row){
                		return "<a class='trace' onclick=\"graphTrace('"+row.pi_id+"')\" id='diagram' href='#' pid='"+row.pi_id+"' pdid='"+row.pi_processDefinitionId+"' title='see'>"+value+"</a>";
                	}
                },
                {field : 'taskCreateTime',title : '申请时间',width : fixWidth(0.2),align : 'center',
                	formatter:function(value, row){
                		return moment(value).format("YYYY-MM-DD HH:mm:ss");
                	}
                },
                {field : 'pi_suspended',title : '流程状态',width : fixWidth(0.1),align : 'center',
		          	formatter:function(value, row){
		        		if(value){
		        			return "已挂起; <b title='流程版本号'>V: "+row.pd_version+"</b>";
		        		}else{
		        			return "正常; <b title='流程版本号'>V: "+row.pd_version+"</b>";  
		        		}
					}
                }
            ] 
		],
		toolbar: "#toolbar"
	});
}

function showDetails(){
	var row = apply_datagrid.datagrid('getSelected');
    if (row) {
    	var _url;
    	if("vacation" == row.businessType){
    		_url = ctx + "/vacationAction/details/"+row.businessKey;
    	}else if("salary" == row.businessType){
    		_url = ctx + "/salaryAction/details/"+row.businessKey;
    	}else{
    		_url = ctx + "/expenseAction/details/"+row.businessKey;
    	}
    	
    	//弹出对话窗口
    	details_dialog = $('<div/>').dialog({
        	title : "用户信息",
    		top: 20,
    		width : 600,
    		height : 400,
            modal: true,
            minimizable: true,
            maximizable: true,
            href: _url,
            onLoad: function () {
                if (row) {
                	if("vacation" == row.businessType){
                		var type = $("#vacationType").val();
                		$("#type").combobox('select', type);
    		    	}else if("salary" == row.businessType){
    		    		$('#salary_form').form('load', row);
    		    	}else{
    		    		$('#expense_form').form('load', row);
    		    	}
                } else {
                	alert("no row!");
                }
            	
            },
            buttons: [
                {
                    text: '关闭',
                    iconCls: 'icon-cancel',
                    handler: function () {
                    	details_dialog.dialog('destroy');
                    }
                }
            ],
            onClose: function () {
            	details_dialog.dialog('destroy');
            }
        });
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
	
}

