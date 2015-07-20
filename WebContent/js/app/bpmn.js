/**
 * 设定审批人员
 */

var bpmn_datagrid;
var bpmn_dialog;
var model_dialog;
var model_form;
var model_width = 0;

var group_datagrid;


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
			    		return "<a id='tip' target='_blank' title='点击查看' href='../processAction/process/process-definition?processDefinitionId="+row.id+"&resourceType=xml'>"+row.resourceName+"</a>"
			    	}
              },
              {field : 'diagramResourceName',title : '图片',width : fixWidth(0.2),align : 'center',
			    	formatter:function(value, row){
			    		return "<a id='tip' target='_blank' title='点击查看' href='../processAction/process/process-definition?processDefinitionId="+row.id+"&resourceType=image'>"+row.diagramResourceName+"</a>"
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


//初始化所有
function initialization(){
	$.messager.confirm('提示','此操作将会删除所有已设定的审批人员，确定初始化所有流程定义的审批人员吗？', function (result) {
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

            } 
            $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
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
        href: ctx+"/userAction/toChooseUser?multiSelect="+multiSelect+"&taskDefKey="+taskDefKey,
        onClose: function () {
        	$("#"+taskDefKey+"_id").val("");
        	$("#"+taskDefKey+"_name").val("");
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
        href: ctx+"/groupAction/toChooseGroup?taskDefKey="+taskDefKey,
        onLoad: function () {
        	//$("#group_datagrid").html('<iframe src="'+ctx+'/groupAction/chooseGroup_page?key='+taskDefKey+'" frameborder="0" height="100%" width="100%" id="dialogGroupFrame" name="dialogGroupFrame" scrolling="auto"></iframe>');
            //显示候选组
            group_datagrid = $('#group_datagrid').datagrid({
                url: ctx+"/groupAction/chooseGroup",
                width : 'auto',
        		height :  $(this).height()-40,
        		pagination:true,
        		rownumbers:true,
        		border:false,
        		singleSelect:true,
        		striped:true,
                columns : [ 
                    [ 
                      {field:'ck', title : '#',width : ($(this).width() - 50) * 0.1,align : 'center',
                    	  formatter:function(value,row){
                    		  return '<input type="checkbox" id="check_'+row.id+'" value="'+row.id+'_'+row.name+'" name="groupIds" />';
        				  }
                      },
                      {field : 'name',title : '用户名',width : ($(this).width() - 50) * 0.45,align : 'center'},
                      {field : 'type',title : '用户组',width : ($(this).width() - 50) * 0.45,align : 'center'}
            	    ] 
                ]
            });
        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                	//调用子页面方法,dialogFrame不能为id，因为在FireFox下id不能获取iframe对象
   				 	//dialogFrame.window.getValue();
                	getValue($("#taskDefKey").val());
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

//取出候选组的值
function getValue(taskDefKey){
    var ids='';
    var names='';
    var checked = $("input[name=groupIds]:checked");//获取所有被选中的标签元素
    for(i=0;i<checked.length;i++){
     	//将所有被选中的标签元素的值保存成一个字符串，以逗号隔开
   	 	var obj = checked[i].value.split("_");
        if(i<checked.length-1){
           ids+=obj[0]+',';
           names+=obj[1]+',';
        }else{
           ids+=obj[0];
           names+=obj[1];
        }
    }
    $("#"+taskDefKey+"_id").val(ids);
	$("#"+taskDefKey+"_name").val(names); 
}

//子窗口调用-关闭选人或组页面
//function closeDialogFrame(){
//	$("#bpmn_dialog").dialog("close");
//}

//组合流程节点
function outputData( obj ){
	var taskDefKey = obj.taskDefKey;
	var taskType = obj.taskType;
	//普通用户节点
	var modal = 
	'<td>\
		<table style="border: 2px solid;padding: 5px;margin: 5px; width: 280px;" class="well well-small">\
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
				<input type="text" id="'+taskDefKey+'_name" name="'+taskDefKey+'_name" readonly class="easyui-textbox"/>\
				<a href="#" onclick="clearChoose(\''+taskDefKey+'\');" class="easyui-linkbutton">清空</a>\
				<input type="hidden" id="'+taskDefKey+'_id" name="'+taskDefKey+'_id" class="easyui-textbox"/>\
			</td>\
		</tr>\
		</table>\
	</td>\
	';
	//修改任务的节点已经在配置文件的 initiator 中设置，此处不用选择任务办理人。
	var modify = 
		'<td>\
		<table style="border: 2px solid;padding: 5px;margin: 5px; width: 280px;" class="easyui-propertygrid well well-small">\
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
				<input type="text" id="'+taskDefKey+'_name" value="任务发起人" name="'+taskDefKey+'_name" readonly="readonly" class="easyui-textbox"/>\
				<input type="hidden" id="'+taskDefKey+'_id" value="0" name="'+taskDefKey+'_id" class="easyui-textbox"/>\
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

//初始化表单
function formInit( procDefKey ) {
    model_form = $('#model_form').form({
    	url: ctx+"/permissionAction/setPermission?procDefKey="+procDefKey,
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
            	model_dialog.dialog('close');//销毁对话框
                bpmn_datagrid.datagrid('reload');//重新加载列表数据
                
            } 
            $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
        }
    });
}

function initModelTable( procDefKey ){
    //显示节点信息
	model_dialog = $('#dialog-form').dialog({
    	top: ($(window).height()-450) * 0.5,
        left: ($(window).width()-model_width-5) * 0.5,
		width : model_width+5,
		height : 400,
		closed: false,
        modal: true,
        shadow: true,
        iconCls: 'icon-save',
        minimizable: false,
        maximizable: false,
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                    $.messager.progress({
                        title: '提示信息！',
                        text: '数据处理中，请稍后....'
                    });
                    model_form.submit();
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	model_dialog.dialog('close');
                }
            }
        ],
        onClose: function () {
        	$("#modelTable").html("");
        	model_width = 0;
        }
    });
}

//设定审批人员
function setAuthor(){
	var row = bpmn_datagrid.datagrid('getSelected');
	if (row) {
		$.ajax({
			type: "POST", 
			url: ctx+"/permissionAction/listUserTask",
			data: {procDefKey: row.key},
			success: function (data) {
				if(data.length == 0){
					$.messager.show({
						title:'提示',
						msg:'请先【加载】所选流程定义文件，再设定审批人员！',
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
						model_width += 300;	//每个节点的宽度
					}
					//显示model
					initModelTable(row.key);
					formInit( row.key );
//					$("#model_form").attr("action",ctx+"/permissionAction/setPermission?procDefKey="+row.key);
				}
			}
		});
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//根据groupId显示人员列表的标签--choose_user.jsp
function addTab(title, groupId, taskDefKey, multiSelect){
	if ($('#userTabs').tabs('exists', title)){
		$('#userTabs').tabs('select', title);
	} else {
		var url = ctx+"/userAction/toShowUser?groupId="+groupId+"&taskDefKey="+taskDefKey+"&multiSelect="+multiSelect;
		var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
		$('#userTabs').tabs('add',{
			title:title,
			content:content,
			closable:true
		});
	}
}

//取消选择--choose_user.jsp
function destroy_chooseUser(taskDefKey){
	$("#"+taskDefKey+"_id").val("");
	$("#"+taskDefKey+"_name").val("");
	bpmn_dialog.dialog('destroy');
}

//选择人时，同时也对父页面赋值了。所以，确认键就只关闭页面就好--choose_user.jsp
function set_chooseUser(){
	bpmn_dialog.dialog('destroy');
}

function clearChoose(taskDefKey){
	$("#"+taskDefKey+"_id").val("");
	$("#"+taskDefKey+"_name").val("");
}

