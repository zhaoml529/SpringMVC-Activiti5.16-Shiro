/**
 * 管理运行中的流程
 */

var process_datagrid;
var process_form;
var process_dialog;

$(function() {
	process_datagrid = $("#process_running").datagrid({
        url: ctx+"/processAction/process/runningProcess",
        width : 'auto',
		height :  $(this).height()-85,
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
//      pageList : [2, 5, 10, 15, 20 ],
//      pageSize:2,
		columns : [ 
		    [ 
                {field : 'id',title : '执行ID',width : fixWidth(0.2)},
                {field : 'processInstanceId',title : '流程实例ID',width : fixWidth(0.1),align : 'center'},
                {field : 'processDefinitionId',title : '流程定义ID',width : fixWidth(0.1),align : 'center'},
                {field : 'activityId',title : 'activityId',width : fixWidth(0.1),align : 'center'},
                {field : 'suspended',title : '是否挂起',width : fixWidth(0.1),align : 'center',
		          	formatter:function(value, row){
		        		if(value){
		        			return "<a href='javascript:void(0);' onclick=\"suspended('active','"+row.id.toString()+"')\">激活</a>";
		        		}else{
		        			return "<a href='javascript:void(0);' onclick=\"suspended('suspend','"+row.id.toString()+"')\">挂起</a>";  
		        		}
					}
                }
            ] 
		],
		toolbar:'#tb'
	});
	
    //修正宽高
	function fixHeight(percent)   
	{   
		return parseInt($(this).width() * percent);
	}

	function fixWidth(percent)   
	{   
		return parseInt(($(this).width() - 50) * percent);
	}
	
});

//部署流程
function deploy(){
	$("#deployFieldset").toggle("normal");
}

//部署全部流程
function deployAll(){
	$.ajax({
        type: "POST",
        url: ctx+"/processAction/process/redeploy/all",
        data: {},
        success: function (data) {
        	$.messager.progress("close");
        	if (data.status) {
        		process_datagrid.datagrid("reload"); //reload the process data
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

//挂起、激活
function suspended( status, id ){
	var url_ = ctx+"/processAction/process/updateProcessStatusByProDefinitionId?status=active&processDefinitionId="+id;
	if(status == "suspend"){
		url_ = ctx+"/processAction/process/updateProcessStatusByProDefinitionId?status=suspend&processDefinitionId="+id;
	}
	
	$.ajax({
        type: "POST",
        url: url_,
        data: {},
        success: function (data) {
        	$.messager.progress("close");
        	if (data.status) {
        		process_datagrid.datagrid("reload"); //reload the process data
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

//删除
function delRows() {
	var row = process_datagrid.datagrid('getSelected');
    if (row) {
    	$.messager.confirm('确认提示！', '您确定要删除选中流程信息? 此操作同时也会删除与此流程相关的审批数据！', function (result) {
            if (result) {
            	$.messager.confirm('再次确认！', '请再次确认您的选择，此操作很重要!', function (result) {
            		if(result){
            			var id = row.deploymentId;
            			$.ajax({
            				type: "POST",
            				url: ctx+"/processAction/process/delete?deploymentId="+id,
            				data: {},
            				success: function (data) {
            					$.messager.progress("close");
            					if (data.status) {
            						process_datagrid.datagrid("reload");
            					} 
            					$.messager.show({
            						title : data.title,
            						msg : data.message,
            						timeout : 1000 * 2
            					});
            				}
            			});
            		}
            	});
            }
    	});
    }else {
    	$.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}


//转换为Model
function convert_to_model(){
	var row = process_datagrid.datagrid('getSelected');
    if (row) {
    	$.ajax({
    		type: "POST",
    		url: ctx+"/processAction/process/convert_to_model?processDefinitionId="+row.id,
    		data: {},
    		success: function (data) {
    			$.messager.progress("close");
    			if (data.status) {
    				process_datagrid.datagrid("reload"); //reload the process data
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
    }else {
    	$.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

