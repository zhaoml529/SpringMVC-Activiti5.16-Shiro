/**
 * 设定审批人员
 */

var user_datagrid;
var group_combobox;


$(function() {
	//数据列表
    user_datagrid = $('#user_datagrid').datagrid({
        url: ctx+"/userAction/chooseUser",
        width : 'auto',
		height :  $(this).height()-85,
		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field:'ck', 
            	  formatter:function(value,row){
            		  value = $("#multiSelect").val();
            		  if(value){
            			  return '<input id="check_'+row.id+'" value="'+row.id+'_'+row.name+'" name="ids" type="checkbox" />';
            		  }else{
            			  return '<input type="radio" name="id" value="'+row.id+'" onclick="selectUser(this.value,\''+row.name+'\');" />';
            		  }
				  }
              },
              {field : 'name',title : '用户名',width : fixWidth(0.2),align : 'left'},
              {field : 'registerDate', title : '注册时间', width : fixWidth(0.2)},
              {field : 'group',title : '用户组',width : fixWidth(0.2)}
    	    ] 
        ]
        //toolbar: "#toolbar"
    });
    //组下来菜单
    group_combobox = $('#group').combobox({
        url:ctx+'/groupAction/getAllGroup',
        valueField:'id',
        textField:'name',
        onSelect: function(rec){
            var url = ctx+"/userAction/chooseUser?groupId="+rec.id;
            user_datagrid.datagrid('reload', url);
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
