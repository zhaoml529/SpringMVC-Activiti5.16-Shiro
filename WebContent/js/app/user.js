/**
 * 用户页面相关
 */

var user_datagrid;
var user_form;
var user_dialog;


$(function() {
	alert("${ctx}/userAction/toList");
	//数据列表
    user_datagrid = $('#user_datagrid').datagrid({
        url: "${ctx}/userAction/toList",
        fit: true,
        pagination: true,//底部分页
        rownumbers: true,//显示行数
        fitColumns: false,//自适应列宽
        striped: true,//显示条纹
        pageSize: 10,//每页记录数
        remoteSort: false,//是否通过远程服务器对数据排序
        sortName: 'name',//默认排序字段
        sortOrder: 'asc,asc',//默认排序方式 'desc' 'asc'
        idField: 'id',
        frozen: true,
        collapsible: true,
        columns : [ 
            [ 
              {field: 'id', title: '主键', hidden: true, sortable: true, align: 'right', width: 80} ,
              {field : 'name',title : '用户名',width : fixWidth(0.3),align : 'left',editor : {type:'validatebox',options:{required:true}}},
              {field : 'passwd',title : '密码',width : fixWidth(0.3),align : 'left',editor : {type:'validatebox',options:{required:true}}},
              {field : 'registerDate', title : '注册时间', width : fixWidth(0.2), editor : "datebox"},
              {field : 'locked',title : '状态',width : fixWidth(0.1),
            	  formatter:function(value,row){
            		  if("0"==row.locked){
						return "<font color=green>正常<font>";
            		  }else{
            			return "<font color=red>停用<font>";  
            		  }
				  },
            	  editor : "text"},
              {field : 'group_name',title : '用户组',width : fixWidth(0.1),editor : "text"}
    	    ] 
        ],
        toolbar: [
          {
              text: '新增',
              iconCls: 'easyui-icon-add',
              handler: function () {
                  showUser()
              }
          },
          '-',
          {
              text: '编辑',
              iconCls: 'easyui-icon-edit',
              handler: function () {
                  edit()
              }
          },
          '-',
          {
              text: '删除',
              iconCls: 'easyui-icon-remove',
              handler: function () {
                  del()
              }
          },
          '-',
          {
              text: '修改密码',
              iconCls: 'eu-icon-lock',
              handler: function () {
                  editPassword()
              }
          },
          '-',
          {
              text: '设置机构',
              iconCls: 'eu-icon-group',
              handler: function () {
                  editUserOrgan()
              }
          },
          '-',
          {
              text: '设置岗位',
              iconCls: 'eu-icon-user',
              handler: function () {
                  editUserPost()
              }
          },
          '-',
          {
              text: '设置角色',
              iconCls: 'eu-icon-group',
              handler: function () {
                  editUserRole()
              }
          },
          '-',
          {
              text: '设置资源',
              iconCls: 'eu-icon-folder',
              handler: function () {
                  editUserResource()
              }
          },
          '-',
          {
              text: '上移',
              iconCls: 'eu-icon-up',
              handler: function () {
                  move(true);
              }
          },
          '-',
          {
              text: '下移',
              iconCls: 'eu-icon-down',
              handler: function () {
                  move();
              }
          },
          '-',
          {
              text: '启用',
              iconCls: 'eu-icon-user',
              handler: function () {
                  lock(false);
              }
          },
          '-',
          {
              text: '停用',
              iconCls: 'eu-icon-lock',
              handler: function () {
                  lock(true);
              }
          }
        ],
        onRowContextMenu: function (e, rowIndex, rowData) {
            e.preventDefault();
            $(this).datagrid('selectRow', rowIndex);
            $('#user_datagrid_menu').menu('show', {
                left: e.pageX,
                top: e.pageY
            });
        },
        onDblClickRow: function (rowIndex, rowData) {
            edit(rowIndex, rowData);
        }
    });
    
    //修正宽高
	function fixHeight(percent)   
	{   
		return (document.body.clientHeight) * percent ;    
	}

	function fixWidth(percent)   
	{   
		return (document.body.clientWidth - 50) * percent ;    
	}
    
	//初始化表单
    function formInit() {
        user_form = $('#user_form').form({
            url: '${ctx}/userAction/doUpdate',
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
        var inputUrl;
        if (row != undefined && row.id) {
            inputUrl = "${ctx}/userAction/toUpdate/"+row.id;
        }else{
        	inputUrl = "${ctx}/userAction/toAdd/";
        }

        //弹出对话窗口
        user_dialog = $('<div/>').dialog({
            title: '用户信息',
            top: 20,
            width: 500,
            height: 360,
            modal: true,
            maximizable: true,
            href: inputUrl,
            buttons: [
                {
                    text: '保存',
                    iconCls: 'easyui-icon-save',
                    handler: function () {
                        user_form.submit();
                    }
                },
                {
                    text: '关闭',
                    iconCls: 'easyui-icon-cancel',
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

    }
    
    
    
  //编辑
    function edit(rowIndex, rowData) {
        //响应双击事件
        if (rowIndex != undefined) {
            showUser(rowData);
            return;
        }
        //选中的所有行
        var rows = user_datagrid.datagrid('getSelections');
        //选中的行（第一次选择的行）
        var row = user_datagrid.datagrid('getSelected');
        if (row) {
            if (rows.length > 1) {
                row = rows[rows.length - 1];
                $.messager.alert("提示", "您选择了多个操作对象，默认操作第一次被选中的记录！");
            }

            showUser(row);
        } else {
            $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
        }
    }
    
})