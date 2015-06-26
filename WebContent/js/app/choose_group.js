/**
 * 设定审批组
 */

var group_datagrid;
var group_combobox;


$(function() {
	//数据列表
	var groupId = $("#groupId").val();
    group_datagrid = $('#group_datagrid').datagrid({
        url: ctx+"/groupAction/chooseGroup",
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
            		  return '<input type="checkbox" id="check_'+row.id+'" value="'+row.id+'_'+row.name+'" name="ids" onclick="selectGroups();" />';
				  }
              },
              {field : 'name',title : '用户名',width : fixWidth(0.3),align : 'center'},
              {field : 'type',title : '用户组',width : fixWidth(0.3),align : 'center'}
    	    ] 
        ]
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

//选择候选人，对父页面赋值
function selectGroups(){
	var taskDefKey = $("#taskDefKey").val();
    var ids='';
    var names='';
    var checked = $("input:checked");//获取所有被选中的标签元素
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
    $("#"+taskDefKey+"_id", window.parent.document).val(ids);
	$("#"+taskDefKey+"_name", window.parent.document).val(names); 
}
