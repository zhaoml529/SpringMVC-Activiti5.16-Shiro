/**
 * 用户页面相关
 */

var model_datagrid;
var model_form;
var model_dialog;


$(function() {
	//数据列表
    model_datagrid = $('#model_datagrid').datagrid({
        url: ctx+"/modelAction/listModel",
        width : 'auto',
		height :  $(this).height()-85,
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field : 'id',title : 'ID',width : fixWidth(0.1),align : 'cneter',},
              {field : 'key',title : 'KEY',width : fixWidth(0.1),align : 'left'},
              {field : 'name', title : 'NAME', width : fixWidth(0.1), align : 'left',},
              {field : 'version', title : 'Version', width : fixWidth(0.1), align : 'center',},
              {field : 'createTime',title : '创建时间',width : fixWidth(0.1),
            	  formatter:function(value,row){
            		  return moment(value).format("YYYY-MM-DD HH:mm:ss");
				  }
              },
              {field : 'lastUpdateTime',title : '最后更新时间',width : fixWidth(0.1),
            	  formatter:function(value,row){
            		  return moment(value).format("YYYY-MM-DD HH:mm:ss");
            	  }
              },
              {field : 'metaInfo', title : '元数据', width : fixWidth(0.4), align : 'left',}
    	    ] 
        ],
        toolbar: "#toolbar"
    });

    
    //修正宽高
	function fixHeight(percent)   
	{   
		return parseInt($(this).width() * percent);
		//return (document.body.clientHeight) * percent ;    
	}

	function fixWidth(percent)   
	{   
		return parseInt(($(this).width() - 30) * percent);
		//return (document.body.clientWidth - 50) * percent ;    
	}
});


//创建模型
function add(){
	//弹出对话窗口
    model_dialog = $('<div/>').dialog({
    	title : "模型信息",
		top: 20,
		width : 600,
		height : 300,
        modal: true,
        minimizable: true,
        maximizable: true,
        href: ctx+"/modelAction/toAddModel",
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-save',
                handler: function () {
                    $.messager.progress({
                        title: '提示信息！',
                        text: '数据处理中，请稍后....'
                    });
                    setTimeout(function() {
	                      //location.reload();
	                      $.messager.progress("close");
	                      model_dialog.dialog('destroy');
	                      model_datagrid.datagrid("reload");
                    }, 1000);
					$('#model_form').submit();
                }
            },
            {
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                    model_dialog.dialog('destroy');
                }
            }
        ],
        onClose: function () {
            model_dialog.dialog('destroy');
        }
    });
}

//编辑
function edit(){
	var row = model_datagrid.datagrid('getSelected');
	if (row) {
        window.open(ctx+"/modeler/service/editor?id="+row.id);
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//部署
function deploy(){
	var row = model_datagrid.datagrid('getSelected');
	if (row) {
        $.ajax({
            url: ctx + '/modelAction/deploy/'+row.id,
            type: 'post',
            dataType: 'json',
            data: {},
    		success: function (data) {
    			$.messager.progress("close");
    			if (data.status) {
    				model_datagrid.datagrid("reload"); //reload the process data
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
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

//导出
function exportmodel(){
	var row = model_datagrid.datagrid('getSelected');
	if (row) {
		window.open(ctx+"/modelAction/export/"+row.id);
	} else {
		$.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
	}
}

//删除
function del(){
	var row = model_datagrid.datagrid('getSelected');
	if (row) {
        $.ajax({
            url: ctx + '/modelAction/delete/'+row.id,
            type: 'post',
            dataType: 'json',
            data: {},
    		success: function (data) {
    			$.messager.progress("close");
    			if (data.status) {
    				model_datagrid.datagrid("reload"); //reload the process data
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
    } else {
        $.messager.alert("提示", "您未选择任何操作对象，请选择一行数据！");
    }
}

