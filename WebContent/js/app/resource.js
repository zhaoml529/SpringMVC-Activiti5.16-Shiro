/**
 * 资源管理
 */

var resource_treegrid;
var resource_form;
var resource_dialog;

$(function() {
	resource_treegrid = $("#resource").treegrid({
		width : 'auto',
		height : $(this).height()-85,
		url : ctx+"/resourceAction/listResource",
		rownumbers:true,
		animate: true,
		collapsible: true,
		fitColumns: true,
		border:false,
		striped:true,
		cascadeCheck:true,
		deepCascadeCheck:true,
		idField: 'id',
		treeField: 'name',
		parentField : 'parentId',
		columns : [ 
		    [ 
                {field : 'name',title : '资源名称',width : fixWidth(0.2)},
                {field : 'type',title : '资源类型',width : fixWidth(0.1),align : 'center',
            	    formatter:function(value,row){
            		    if("menu"==row.type){
            		    	return "<font color=green>菜单<font>";
            		    }else{
            		    	return "<font color=red>操作<font>";  
            		    }
					}
                },
                {field : 'url',title : '资源路径',width : fixWidth(0.2),align : 'center'},
                {field : 'permission',title : '权限字符串',width : fixWidth(0.2),align : 'left'},
			    {field : 'available',title : '是否启用',width : fixWidth(0.1),align : 'center',
		            formatter:function(value,row){
		            	if("1"==row.available){
		            		return "<font color=green>是<font>";
		            	}else{
		            		return "<font color=red>否<font>";  
		            	}
					}
                }
                
            ] 
		],
		toolbar:'#tb',
        onContextMenu: function (e, row) {
            e.preventDefault();
            $('#resource_treegrid_menu').menu('show', {
                left: e.pageX,
                top: e.pageY
            });
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

//收缩
function collapseAll(){
	var node = resource_treegrid.treegrid('getSelected');
	if (node) {
		resource_treegrid.treegrid('collapseAll', node.id);
	} else {
		resource_treegrid.treegrid('collapseAll');
	}
}

//展开
function expandAll(){
	var node = resource_treegrid.treegrid('getSelected');
	if (node) {
		resource_treegrid.treegrid('expandAll', node.id);
	} else {
		resource_treegrid.treegrid('expandAll');
	}
}

//刷新
function refresh(){
	resource_treegrid.treegrid('reload');
}


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
