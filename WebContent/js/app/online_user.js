/**
 * 在线用户管理
 */

var user_datagrid;


$(function() {
	//数据列表
    user_datagrid = $('#user_datagrid').datagrid({
        url: ctx+"/userAction/listOnlineUser",
        width : 'auto',
		height :  $(this).height()-85,
//		pagination:true,
		rownumbers:true,
		border:false,
		singleSelect:true,
		striped:true,
        columns : [ 
            [ 
              {field : 'id',title : '会话ID',width : fixWidth(0.2),align : 'left',},
              {field : 'userName',title : '用户名',width : fixWidth(0.2),align : 'left'},
              {field : 'host', title : '主机地址', width : fixWidth(0.2),align : 'left'},
              {field : 'lastAccessTime',title : '最后访问时间',width : fixWidth(0.2),
            	  formatter:function(value, row){
            		  return moment(value).format("YYYY-MM-DD HH:mm:ss");
				  }
              },
              {field : 'forceLogout',title : '操作',width : fixWidth(0.2),align: 'center',
            	  formatter:function(value, row){
            		  if(!value){
            			  return "<a href='#' onclick=\"forceLogout('"+row.id+"')\" >强制退出</a>";
            		  }
				  }
              }
    	    ] 
        ],
        toolbar: "#toolbar"
    });
    
    //修正宽高
	function fixHeight(percent)   
	{   
		return parseInt($(this).width() * percent);
	}

	function fixWidth(percent)   
	{   
		return parseInt(($(this).width() - 55) * percent);
	}
});


function forceLogout( id ){
	$.ajax({
		type: "POST",
		url: ctx+"/userAction/forceLogout/"+id,
		data: {},
		success: function (data) {
			$.messager.progress("close");
			if (data.status) {
				user_datagrid.datagrid("reload"); //reload the process data
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