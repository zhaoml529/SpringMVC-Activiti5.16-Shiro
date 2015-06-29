/**
 * 查看请假申请、薪资调整申请、报销申请
 */

var vacation_datagrid;
var salary_datagrid;
var expense_datagrid;

var apply_datagrid;
var details_dialog;


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
	return parseInt(($(this).width() - 55) * percent);
}

function showApply( businessType ){
	var url_ = ctx+"/processAction/process/runingProcessInstance/"+businessType+"/list";
	vacation_datagrid = $("#"+businessType+"_datagrid").datagrid({
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
                {field : 'userName',title : '申请人',width : fixWidth(0.1),align : 'center' , editor : {type:'validatebox',options:{required:true}}},
                {field : 'title',title : '标题',width : fixWidth(0.3),align : 'center'},
                {field : 'taskName',title : '当前节点',width : fixWidth(0.2),align : 'center',
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
                },
                {field : 'details', title : '操作', width : fixWidth(0.1), align : 'center',
            	    formatter:function(value, row){
            	    	if("vacation" == businessType){
            	    		//return "<a href='"+ctx+"/vacationAction/details/"+row.businessKey+"'>详情</a>";
            	    		
            	    		return "<a href='javascript:void(0);' onclick=\"showDetails("+row+")\">详情</a>";
            	    	}else if("salary" == businessType){
            	    		return "<a href='"+ctx+"/salaryAction/details/"+row.businessKey+"'>详情</a>";
            	    	}else{
//            	    		return "<a href='"+ctx+"/expenseAction/details/"+row.businessKey+"'>详情</a>";
            	    		return "<a href='javascript:void(0);' onclick=\"showDetails("+row+")\">详情</a>";
            	    	}
				    }
                }
            ] 
		]
	});
}

function showDetails( row ){
	alert(row.businessType);
	var url_;
	if("vacation" == row.businessType){
		url_ = ctx + "/vacationAction/details/"+row.businessKey;
	}else if("salary" == row.businessType){
		url_ = ctx + "/salaryAction/details/"+row.businessKey;
	}else{
		url_ = ctx + "/expenseAction/details/"+row.businessKey;
	}
	details_dialog = $('<div/>').dialog({
    	title : "申请信息",
		top: 20,
		width : 500,
		height : 200,
        modal: true,
        minimizable: true,
        maximizable: true,
        href: _url,
        onLoad: function () {
        	if(row){
        		//$('#vacation_form').form('load', row);
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
}

