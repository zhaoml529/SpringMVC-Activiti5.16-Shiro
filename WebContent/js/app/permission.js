/**
 * 组、权限控制
 */

var group_datagrid;
var group_form;
var group_dialog;

var resource_datagrid;
var resource_form;
var resource_dialog;

$(function() {
	$("#panel").panel({   
		   width:'auto',   
		   height:$(this).height(),   
		   title: '权限编辑',   
	});
	
	group_datagrid = $("#group").datagrid({
		url : ctx+"/groupAction/getGroupList",
		width : 'auto',
		height : $(this).height()-120,
		pagination:true,
		border:false,
		rownumbers:true,
		singleSelect:true,
		striped:true,
		idField: 'id',
		sortName: 'type',//默认排序字段
        sortOrder: 'desc',//默认排序方式 'desc' 'asc'
		columns : [ 
		    [ 
		        {field : 'name',title : '组名称',width : fixWidth(0.1),sortable: true,align : 'center',editor : {type:'validatebox',options:{required:true}}},
		        {field : 'type',title : '组类型',width : fixWidth(0.1),sortable: true,align : 'center',editor : "text"}
            ] 
		],
		toolbar:'#toolbar',
		onDblClickRow:getPermission
	})
	
	resource_datagrid = $("#resource").treegrid({
		width : 'auto',
		height : $(this).height()-120,
		url : ctx+"/permissionAction/getResourceList",
		rownumbers:true,
		animate: true,
		collapsible: true,
		fitColumns: true,
		border:false,
		striped:true,
		singleSelect:false,
		cascadeCheck:true,
		deepCascadeCheck:true,
		idField: 'id',
		treeField: 'name',
		parentField : 'parentId',
		columns : [ 
		    [ 
                {field:'ck', checkbox:true},
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
		onClickRow:function(row){   
           //级联选择   
			resource_datagrid.treegrid('cascadeCheck',{   
                  id:row.id, //节点ID   
                  deepCascade:true //深度级联   
           });   
        },
        onContextMenu: function (e, row) {
            e.preventDefault();
            resource_datagrid.treegrid('unselectAll');
            $('#resource_datagrid_menu').menu('show', {
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
	var node = resource_datagrid.treegrid('getSelected');
	if (node) {
		resource_datagrid.treegrid('collapseAll', node.id);
	} else {
		resource_datagrid.treegrid('collapseAll');
	}
}

//展开
function expandAll(){
	var node = resource_datagrid.treegrid('getSelected');
	if (node) {
		resource_datagrid.treegrid('expandAll', node.id);
	} else {
		resource_datagrid.treegrid('expandAll');
	}
}

//刷新
function refresh(){
	resource_datagrid.treegrid('reload');
}

//双击组时，关联组权限
function getPermission(rowIndex, rowData){ 
    $.ajax({
        url: ctx + '/permissionAction/getGroupPermission',
        type: 'post',
        dataType: 'json',
        data: {groupId: rowData.id},
        success: function (data) {
        	resource_datagrid.treegrid('reload');
        	setTimeout(function () { 
        		if(data.length!=0){
      	    	    $.each(data,function(i,e){
      	    		    resource_datagrid.treegrid('select',e.resourceId);
      	    	    });
      			}else{
      				$.messager.show({
      					title :"提示",
      					msg :"该角色暂无权限!",
      					timeout : 1000 * 2
      				});
      			}
            }, 100);
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


//初始化表单
function formInit(row) {
	var _url = ctx+"/groupAction/doAdd";
	if (row != undefined && row.id) {
		_url = ctx+"/groupAction/doUpdate";
	}
    group_form = $('#group_form').form({
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
                group_dialog.dialog('destroy');//销毁对话框
                group_datagrid.datagrid('reload');//重新加载列表数据
                
            }
            $.messager.show({
				title : json.title,
				msg : json.message,
				timeout : 1000 * 2
			});
        }
    });
}

//显示弹出窗口 新增：row为空 编辑:row有值
function showGroup(row) {
	var _url = ctx+"/groupAction/toAdd";
	if (row != undefined && row.id) {
		_url = ctx+"/groupAction/toUpdate";
	}
    //弹出对话窗口
    group_dialog = $('<div/>').dialog({
    	title : "组信息",
		top: 20,
		width : 500,
		height : 200,
        modal: true,
        minimizable: true,
        maximizable: true,
        href: _url,
        onLoad: function () {
            formInit(row);
            if (row) {
            	group_form.form('load', row);
            }

        },
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                    group_form.submit();
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                    group_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
            group_dialog.dialog('destroy');
        }
    });
}

//添加修改操作
function operation() {
    var row = group_datagrid.datagrid('getSelected');
    if (row) {
        showGroup(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//删除组
function delRows() {
    var row = group_datagrid.datagrid('getSelected');
    if (row) {
        $.messager.confirm('确认提示！', '您确定要删除选中数据? 同时也会删除此用户组所对应的资源信息！', function (result) {
            if (result) {
                var id = row.id;
                $.ajax({
                    url: ctx + '/groupAction/delete/'+id,
                    type: 'post',
                    dataType: 'json',
                    data: {},
                    success: function (data) {
                    	$.messager.progress("close");
                        if (data.status) {
                            group_datagrid.datagrid('load');	  // reload the group data
                            resource_datagrid.treegrid('reload'); //reload the resource data
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
    } else {
    	$.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//保存权限设置
function savePermission(){
	var selections=resource_datagrid.treegrid('getSelections');
	var selectionGroup=group_datagrid.datagrid('getSelected');
	var checkedIds=[];
	if(selections.length!=0){
		$.each(selections,function(i,e){
			checkedIds.push(e.id);
		});
		if(selectionGroup){
			$.ajax({
				url: ctx + '/permissionAction/savePermission',
				type: 'post',
				dataType: 'json',
				data: {groupId:selectionGroup.id, resourceIds:checkedIds},
                success: function (data) {
                	$.messager.progress("close");
                	resource_datagrid.datagrid("reload");
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
			    },
				error:function(){
					$.messager.show({
						title :"提示",
						msg : "分配失败！",
						timeout : 1000 * 2
					});
				}
				
			});
		}else{
			$.messager.show({
				title :"提示",
				msg : "请选择角色！",
				timeout : 1000 * 2
			});
		}
	}else{
		$.messager.show({
			title :"提示",
			msg : "请选择资源信息！",
			timeout : 1000 * 2
		});
		expandAll();
	}
}

