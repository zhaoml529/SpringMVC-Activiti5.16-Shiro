/**
 * 用户页面相关
 */

var user_datagrid;
var user_form;
var user_dialog;


$(function() {
	//数据列表
    user_datagrid = $('#user_datagrid').datagrid({
        url: ctx+"/userAction/toList",
        width : 'auto',
		height :  $(this).height()-120,
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field : 'name',title : '用户名',width : 20,align : 'left',sortable: true, editor : {type:'validatebox',options:{required:true}}},
              {field : 'passwd',title : '密码',width : 20,align : 'left',editor : {type:'validatebox',options:{required:true}}},
              {field : 'registerDate', title : '注册时间', width : 20, editor : "datebox"},
              {field : 'locked',title : '状态',width : 20,
            	  formatter:function(value,row){
            		  if("0"==row.locked){
						return "<font color=green>正常<font>";
            		  }else{
            			return "<font color=red>停用<font>";  
            		  }
				  },
            	  editor : "text"},
              {field : 'group_name',title : '用户组',width : 20,editor : "text"}
    	    ] 
        ],
        toolbar: "#toolbar"
    });
    
    //搜索框
/*    $("#searchbox").searchbox({ 
    	menu:"#mm", 
    	prompt :'模糊查询',
    	searcher:function(value,name){   
    		var str="{\"searchName\":\""+name+"\",\"searchValue\":\""+value+"\"}";
    		var obj = eval('('+str+')');
    		$dg.datagrid('reload',obj); 
    	}
    
    });*/
    
    //修正宽高
	function fixHeight(percent)   
	{   
		return (document.body.clientHeight) * percent ;    
	}

	function fixWidth(percent)   
	{   
		return (document.body.clientWidth - 50) * percent ;    
	}
});

//初始化表单
function formInit() {
    user_form = $('#user_form').form({
        url: ctx+'/userAction/doUpdate',
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
            alert(result.status);
            var json = $.parseJSON(data);
            alert(json)
            if (json.code == 1) {
                user_dialog.dialog('destroy');//销毁对话框
                user_datagrid.datagrid('reload');//重新加载列表数据
                parent.$.messager.show({
					title : result.title,
					msg : result.message,
					timeout : 1000 * 2
				});
            } else if (json.code == 2) {
                $.messager.alert('提示信息！', json.msg, 'warning', function () {
                    if (json.obj) {
                        $('#user_form input[name="' + json.obj + '"]').focus();
                    }
                });
            } else {
                eu.showAlertMsg(json.msg, 'error');
            }
        }
    });
}


//显示弹出窗口 新增：row为空 编辑:row有值
function showUser(row) {
	alert("showUserDialog");
    //弹出对话窗口
    user_dialog = $('<div/>').dialog({
    	title : "用户信息",
		top: 20,
		width : 600,
		height : 300,
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/userAction/toUpdate/"+row.id,
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                    user_form.submit();
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                    user_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
            user_dialog.dialog('destroy');
        },
        onLoad: function () {
            formInit();
            if (row) {
                user_form.form('load', row);
            } else {
                alert("这个是多行吗？");
            }

        }
    });

	
	/*parent.$.modalDialog({
		title : "编辑用户",
		width : 600,
		height : 300,
		href : ctx+"/userAction/toUpdate/"+row.id,
		onLoad:function(){
			var f = parent.$.modalDialog.handler.find("#form");
			f.form("load", row);
		},			
		buttons : [ {
			text : '编辑',
			iconCls : 'icon-ok',
			handler : function() {
				parent.$.modalDialog.openner= $grid;
				var f = parent.$.modalDialog.handler.find("#form");
				f.submit();
			}
		}, {
			text : '取消',
			iconCls : 'icon-cancel',
			handler : function() {
				parent.$.modalDialog.handler.dialog('destroy');
				parent.$.modalDialog.handler = undefined;
			}
		}
		]
	});*/
}



//编辑
function edit() {
	alert("edit...");
    //选中的行（第一次选择的行）
    var row = user_datagrid.datagrid('getSelected');
    if (row) {
        showUser(row);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}