/**
 * 管理已结束的流程
 */

var process_datagrid;
var process_form;
var process_dialog;

$(function() {
	process_datagrid = $("#process_finished").datagrid({
        url: ctx+"/processAction/process/finishedProcess",
        width : 'auto',
		height :  $(this).height()-135,
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
//      pageList : [2, 5, 10, 15, 20 ],
//      pageSize:2,
		columns : [ 
		    [ 
                {field : 'businessType',title : '单据类型',width : fixWidth(0.2), align : 'center',
                	formatter:function(value, row){
                		if(value == 'vacation'){
                			return "请假申请";
                		}else if(value == "salary"){
                			return "薪资调整";
                		}else if(value == "expense"){
                			return "爆笑申请";
                		}
					}
                },
                {field : 'user_name',title : '申请人',width : fixWidth(0.1),align : 'center'},
                {field : 'title',title : '标题',width : fixWidth(0.2),align : 'center'}
                //{field : 'historicProcessInstance.startTime',title : '流程启动时间',width : fixWidth(0.1),align : 'center'},
                //{field : 'historicProcessInstance.endTime',title : '流程结束时间',width : fixWidth(0.3),align : 'center'}
            ] 
		],
		onLoadSuccess: function (data) {
            if (data.total == 0) {
            	alert("no record");
                //添加一个新数据行，第一列的值为你需要的提示信息，然后将其他列合并到第一列来，注意修改colspan参数为你columns配置的总列数
            	//process_datagrid.datagrid('appendRow', { businessType: '<div style="text-align:center;color:red;height: 100px;">没有相关记录！</div>' }).datagrid('mergeCells', { index: 0, field: 'businessType', colspan: 3 });
                //隐藏分页导航条，这个需要熟悉datagrid的html结构，直接用jquery操作DOM对象，easyui datagrid没有提供相关方法隐藏导航条
            	//process_datagrid.closest('div.datagrid-wrap').find('div.datagrid-pager').hide();
            	var body = $(this).data().datagrid.dc.body2;
				body.find('table tbody').append('<tr><td width="'+$(this).width()+'" style="height: 25px; text-align: center;" colspan="3">没有数据</td></tr>');
            }else{
            	//如果通过调用reload方法重新加载数据有数据时显示出分页导航容器
            	process_datagrid.closest('div.datagrid-wrap').find('div.datagrid-pager').show();
            }
        }
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
	

});


//挂起、激活
function suspended( status, id ){
	var id = row.processInstanceId.toString();
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


