/**
 * 设定审批人员
 */

var bpmn_datagrid;
var bpmn_win;


$(function() {
	//数据列表
    bpmn_datagrid = $('#bpmn_datagrid').datagrid({
        url: ctx+"/permissionAction/listBpmn",
        width : 'auto',
		height :  $(this).height()-85,
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field : 'id',title : 'ID',width : fixWidth(0.2),align : 'cneter',},
              {field : 'name', title : '名称', width : fixWidth(0.2), align : 'left',},
              {field : 'key',title : 'KEY',width : fixWidth(0.2),align : 'left'},
              {field : 'resourceName',title : 'XML',width : fixWidth(0.2),align : 'center',
			    	formatter:function(value, row){
			    		return "<a id='tip' target='_blank' title='点击查看' href='process-definition?processDefinitionId="+row.id+"&resourceType=xml'>"+row.resourceName+"</a>"
			    	}
              },
              {field : 'diagramResourceName',title : '图片',width : fixWidth(0.2),align : 'center',
			    	formatter:function(value, row){
			    		return "<a id='tip' target='_blank' title='点击查看' href='process-definition?processDefinitionId="+row.id+"&resourceType=image'>"+row.diagramResourceName+"</a>"
			    	}
              }
//              {field : 'setApprovalPersonnel', title : '操作', width : fixWidth(0.4), align : 'left',
//            	    formatter:function(value, row){
//            	    	return "<a href='#' onclick=\"forceLogout('"+row.id+"')\" >强制退出</a>";
//				    }
//              }
    	    ] 
        ],
        toolbar: "#toolbar"
    });

    //显示节点信息
    bpmn_win = $('#dialog-form').window({
        top: ($(window).height()-300) * 0.5,
        left: ($(window).width()-1500) * 0.5,
		width : 1500,
		height : 300,
		closed: true,
		shadow: true,
        modal: true,
        iconCls: 'icon-save',
        minimizable: false,
        maximizable: false,
        onBeforeClose: function(){
        	$("#modelTable").html("");
        }
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


//初始化所有
function initialization(){
	$.messager.confirm('此操作将会删除所有已设定的审批人员，确定初始化所有流程定义的审批人员吗？', function (result) {
		if(result){
			$.ajax({
				type: "POST",
				url: ctx+"/permissionAction/initialization",
				data: {},
				success: function (data) {
					$.messager.progress("close");
	    			if (data.status) {
	    				bpmn_datagrid.datagrid("reload"); 
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
	});
	
}


//候选人变淡初始化
function userFormInit( multiSelect, taskDefKey ) {
    user_form = $('#user_form').form({
        url: ctx+'/userAction/chooseUser_page?flag='+multiSelect+'&key='+taskDefKey,
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
                user_dialog.dialog('destroy');//销毁对话框
                user_datagrid.datagrid('reload');//重新加载列表数据
                $.messager.show({
					title : json.title,
					msg : json.message,
					timeout : 1000 * 2
				});
            } else {
				$.messager.show({
					title :  json.title,
					msg : json.message,
					timeout : 1000 * 2
				});
            } 
        }
    });
}

//选择人或候选人
function chooseUser( multiSelect, taskDefKey ){
	//弹出对话窗口
	bpmn_dialog = $('<div/>').dialog({
    	title : "设定候选人",
		top: 20,
		width : 1000,
		height : 400,
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/userAction/toChooseUser?multiSelect="+multiSelect+"&key="+taskDefKey,
//        onLoad: function () {
//        	//$("#choose-user").html('<iframe src="'+ctx+'/userAction/chooseUser_page?groupId=-1&flag='+multiSelect+'&key='+taskDefKey+'" frameborder="0" height="100%" width="100%" id="dialogFrame" name="dialogFrame" scrolling="auto"></iframe>');
//        	userFormInit( multiSelect, taskDefKey );
//        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                	//调用子页面方法,dialogFrame不能为id，因为在FireFox下id不能获取iframe对象
   				 	dialogFrame.window.getValue();
   				 	bpmn_dialog.dialog('destroy');
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	bpmn_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	bpmn_dialog.dialog('destroy');
        }
    });
}

//选择候选组
function chooseGroup( taskDefKey ){
	bpmn_dialog = $('<div/>').dialog({
    	title : "设定候选人",
		top: 20,
		width : 1000,
		height : 400,
        modal: true,
        minimizable: true,
        maximizable: true,
        href: _url,
        onLoad: function () {
        	$("#choose-group").html('<iframe src="'+ctx+'/groupAction/chooseGroup_page?key='+taskDefKey+'" frameborder="0" height="100%" width="100%" id="dialogGroupFrame" name="dialogGroupFrame" scrolling="auto"></iframe>');
        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                	//调用子页面方法,dialogFrame不能为id，因为在FireFox下id不能获取iframe对象
   				 	dialogFrame.window.getValue();
   				 	bpmn_dialog.dialog('destroy');
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	bpmn_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
        	bpmn_dialog.dialog('destroy');
        }
    });
}

//子窗口调用-关闭选人或组页面
function closeDialogFrame(){
	$("#choose-user").dialog("close");
}



//显示流程节点
function modelDialog( data ){
	alert("open dialog");
	bpmn_win=$('#dialog-form').window('open'); // open a window
	//弹出对话窗口
	
}

//组合流程节点
function outputData( obj ){
	var taskDefKey = obj.taskDefKey;
	var taskType = obj.taskType;
	//普通用户节点
	var modal = 
	'<td>\
		<table style="border: 2px solid red;padding: 5px;margin: 5px; width: 280px;">\
		<tr>\
			<td>名称:</td>\
			<td>'+obj.taskName+'</td>\
		</tr>\
		<tr><td colspan="2" style="height: 10px"></td></tr>\
		<tr>\
			<td>类型:</td>\
			<td>\
				<input type="radio" name="'+taskDefKey+'_taskType" value="assignee" id="assignee" onclick="chooseUser(false,\''+taskDefKey+'\');" />人员\
		        <input type="radio" name="'+taskDefKey+'_taskType" value="candidateUser" id="candidateUser" onclick="chooseUser(true,\''+taskDefKey+'\');" />候选人\
		        <input type="radio" name="'+taskDefKey+'_taskType" value="candidateGroup" id="candidateGroup" onclick="chooseGroup(\''+taskDefKey+'\');" />候选组\
			</td>\
		</tr>\
		<tr><td colspan="2" style="height: 10px"></td></tr>\
		<tr>\
			<td>选择:</td>\
			<td>\
				<input type="text" id="'+taskDefKey+'_name" name="'+taskDefKey+'_name" readonly class="text ui-widget-content ui-corner-all"/>\
				<input type="hidden" id="'+taskDefKey+'_id" name="'+taskDefKey+'_id" class="text ui-widget-content ui-corner-all"/>\
			</td>\
		</tr>\
		</table>\
	</td>\
	';
	//修改任务的节点已经在配置文件的 initiator 中设置，此处不用选择任务办理人。
	var modify = 
		'<td>\
		<table style="border: 2px solid red;padding: 5px;margin: 5px">\
		<tr>\
			<td>名称:</td>\
			<td>'+obj.taskName+'</td>\
		</tr>\
		<tr><td colspan="2" style="height: 10px"></td></tr>\
		<tr>\
			<td>类型:</td>\
			<td>\
				任务发起人\
				<input type="hidden" value="modify" name="'+taskDefKey+'_taskType" />\
			</td>\
		</tr>\
		<tr><td colspan="2" style="height: 10px"></td></tr>\
		<tr>\
			<td>选择:</td>\
			<td>\
				<input type="text" id="'+taskDefKey+'_name" value="任务发起人" name="'+taskDefKey+'_name" class="text ui-widget-content ui-corner-all"/>\
				<input type="hidden" id="'+taskDefKey+'_id" value="0" name="'+taskDefKey+'_id" class="text ui-widget-content ui-corner-all"/>\
			</td>\
		</tr>\
		</table>\
	</td>\
	';
	if(taskDefKey == "modifyApply"){
		$(modify).appendTo($("#modelTable"));
	}else{
		var modal = $(modal).appendTo($("#modelTable"));
		if(taskType == "assignee"){
    		modal.find("table input[id=assignee]").attr("checked","checked");
		}else if(taskType == "candidateUser"){
			modal.find("table input[id=candidateUser]").attr("checked","checked");
		}else if(taskType == "candidateGroup"){
			modal.find("table input[id=candidateGroup]").attr("checked","checked");
		}
		modal.find("table input[id="+taskDefKey+"_name]").attr("value",obj.candidate_name);
		modal.find("table input[id="+taskDefKey+"_id]").attr("value",obj.candidate_ids);
	}
}

//设定审批人员
function setAuthor(){
	var row = bpmn_datagrid.datagrid('getSelected');
	if (row) {
		$.ajax({
			type: "POST", 
			url: ctx+"/permissionAction/listUserTask",
			data: {processKey: row.key},
			success: function (data) {
				if(data.length == 0){
					$.messager.show({
						title:'提示',
						msg:'请先加载所选流程定义文件，再设定审批人员！',
						showType:'fade',
						style:{
							right:'',
							bottom:''
						}
					});
				}else{
					for(var i=0;i<data.length;i++) {  
						//逐个显示审批人员
						outputData(data[i]);
					}
					//最后显示model
//					modelDialog( data );
					bpmn_win.window('open');
					//$("#modelForm").attr("action","${ctx}/permissionAction/setPermission?processKey="+data[0].procDefKey);
				}
			}
		});
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
	
}

