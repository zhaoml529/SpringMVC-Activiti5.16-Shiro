/**
 * 设定委派人员
 */

var user_datagrid;
var group_combobox;


$(function() {
	//数据列表
	var groupId = $("#groupId").val();
    user_datagrid = $('#user_datagrid').datagrid({
        url: ctx+"/userAction/chooseUser?groupId="+groupId,
        width : 'auto',
		height :  $(this).height(),
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field:'ck', title : '#',width : fixWidth(0.1),align : 'center',
            	  formatter:function(value,row){
            		  return '<input type="radio" name="id" value="'+row.id+'" onclick="selectUser(\''+row.id+'\',\''+row.name+'\');" />';
				  }
              },
              {field : 'name',title : '用户名',width : fixWidth(0.3),align : 'center'},
              {field : 'registerDate', title : '注册时间', width : fixWidth(0.3),align : 'center'},
              {field : 'group',title : '用户组',width : fixWidth(0.3),align : 'center'}
    	    ] 
        ]
        //toolbar: "#toolbar"
    });
    //组下来菜单
//    group_combobox = $('#group').combobox({
//        url:ctx+'/groupAction/getAllGroup',
//        valueField:'id',
//        textField:'name',
//        onSelect: function(rec){
//            var url = ctx+"/userAction/chooseUser?groupId="+rec.id;
//            user_datagrid.datagrid('reload', url);
//        }
//    });
    
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

//选择委派人，对父页面赋值
function selectUser( userId, userName ){
	$("#userId", window.parent.document).val(userId);
	$("#userName", window.parent.document).val(userName);
	//window.parent.closeDialogFrame();
}

