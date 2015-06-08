/**
 * 流程定义管理
 */

var process_datagrid;
var process_form;
var process_dialog;

$(function() {
	process_datagrid = $("#process").datagrid({
        width : 'auto',
		height :  $(this).height()-85,
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
		columns : [ 
		    [ 
                {field : 'id',title : 'ProcessDefinitionId',width : fixWidth(0.2)},
                {field : 'deploymentId',title : 'DeploymentId',width : fixWidth(0.05),align : 'center'},
                {field : 'name',title : '名称',width : fixWidth(0.1),align : 'center'},
                {field : 'key',title : 'key',width : fixWidth(0.1),align : 'left'},
			    {field : 'version',title : '版本号',width : fixWidth(0.05),align : 'center'},
                {field : 'resourceName',title : 'XML',width : fixWidth(0.1),align : 'center'},
                {field : 'diagramResourceName',title : '图片',width : fixWidth(0.1),align : 'center'},
                {field : 'deploymentTime',title : '部署时间',width : fixWidth(0.1),align : 'center'},
                {field : 'suspended',title : '是否挂起',width : fixWidth(0.1),align : 'center'},
                {field : 'caozuo',title : '操作',width : fixWidth(0.1),align : 'center'}
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



//----------------Resource-------------------

function resourceFormInit(row) {
	var _url = ctx+"/resourceAction/doAdd";
	if (row != undefined && row.id) {
		_url = ctx+"/resourceAction/doUpdate";
	}
	resource_form = $('#resource_form').form({
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
            	resource_dialog.dialog('destroy');//销毁对话框
            	resource_treegrid.treegrid('reload');//重新加载列表数据
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

function showResource(row){
	var _url = ctx+"/resourceAction/toAdd";
	if (row != undefined && row.id) {
		_url = ctx+"/resourceAction/toUpdate/"+row.id;
	}
    //弹出对话窗口
    resource_dialog = $('<div/>').dialog({
    	title : "资源信息",
		top: 20,
		width : 600,
		height : 400,
        modal: true,
        minimizable: true,
        maximizable: true,
        href: _url,
        onLoad: function () {
            resourceFormInit(row);
            if (row) {
            	resource_form.form('load', row);
            }

        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                	resource_form.submit();
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                	resource_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
            resource_dialog.dialog('destroy');
        }
    });
}

//添加修改操作
function openResource() {
    var row = resource_treegrid.treegrid('getSelected');
    if (row) {
    	showResource(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除组
function delRows() {
	var row = resource_treegrid.treegrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中数据? 同时也会删除此用户组所对应的权限信息！', function (result) {
            if (result) {
                var id = row.id;
                $.ajax({
                    url: ctx + '/resourceAction/doDelete/'+id,
                    type: 'post',
                    dataType: 'json',
                    data: {},
                    success: function (data) {
                        if (data.status) {
                            resource_treegrid.treegrid('reload'); //reload the resource data
                            $.messager.show({
            					title : data.title,
            					msg : data.message,
            					timeout : 1000 * 2
            				});
                        } else {
                        	$.messager.show({
            					title : data.title,
            					msg : data.message,
            					timeout : 1000 * 2
            				});
                        }
                    }
                });
            }
        });
    } else {
    	$.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}
