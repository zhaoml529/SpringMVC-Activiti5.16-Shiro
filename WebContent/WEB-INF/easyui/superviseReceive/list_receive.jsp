<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <title>督察接收</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  	<%-- <script type="text/javascript" src="${ctx}/js/app/choose/user/user.js?_=${sysInitTime}"></script> --%>
  	<script type="text/javascript">
  		var s_datagrid;
  		var g_datagrid;
  		var o_datagrid;
	  	$(function() {
	  		//初始化省政府文件
	  		shengzhengfu();
	  		$('#tabs').tabs({
	  		    border:false,
	  		    onSelect:function(title, index){
	  				if(title == "省政府文件") {
	  					tunhuo();
	  				} else if(title == "国务院文件") {
	  					buhuo();
	  				} else if(title == "其他文件") {
	  					caigoujihua();
	  				}
	  		    }
	  		});
	  		
	  	});
	  	
	  	function shengzhengfu() {
	  		debugger;
	  		s_datagrid = $('#s_datagrid').datagrid({
	  	        url: ctx+"/superviserReveive/toSuperviseReceive",
	  	        width: 'auto',
	  			height: 200,
	  			pagination:true,
	  			rownumbers:true,
	  			fitColumns:true,
	  			border:false,
	  			singleSelect:true,
	  			striped:true,
	  			nowrap: false,
	  			columns: [ 
	  	            [ 
					  {field: 'id', title : '#',width : 50, align : 'center',checkbox: true},
	  	              {field: 'status',title: '状态',align: 'center'},
	  	              {field: 'title',title: '标题',align: 'center', halign: 'left', sortable: true},
	  	              {field: 'number',title: '文号',align: 'center', sortable: true},
	  	              {field: 'createDate',title: '立项时间',align: 'center'},
	  	              {field: 'handleDate',title: '交办时间',align: 'center'},
	  	              {field: 'feedback_cycle',title: '反馈周期',align: 'center',
	  	            	  formatter:function(value, row){
	  	            		  return "自定义显示内容 - " + value;
	  	    			  }
	  	              },
	  	              {field : 'feedback_limit',title : '反馈时限',align : 'center',
	  	            	formatter:function(value, row){
	  	            		return moment(value).format("YYYY-MM-DD HH:mm:ss"); //格式化时间  moment.js
	  	  				}
	  	              }
	  	    	    ] 
	  	        ],
		        onCheck: function(index, row) {
		        	//勾选一行时触发
		        	setValue(row);
		        },
		        onCheckAll: function(rows) {
		        	//勾选全部行时触发
		        },
		        onUncheckAll: function(rows) {
		        	//取消勾选全部行时触发
		        },
		        onDblClickRow: function(index, row) {
		        	//双击一行时触发
		        	productPartDetails(row);
		        },
	  	        toolbar: "#toolbar"
	  	    });
	  		
	  	}
  	</script>

  </head>
  <body>
	<div id="toolbar" style="padding:2px 0; display: none;">
		<table>
			<tr>
				<td style="padding-left:2px">
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="showPro('tunhuo');">查看</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="edit('tunhuo');">签收</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="del();">拒绝</a>&nbsp;|&nbsp;
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="details('tunhuo');">代签收</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="details('tunhuo');">办理中</a>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="details('tunhuo');">已办理</a>
				</td>
			</tr>
		</table>
	</div>
	
	<div id="tabs" class="easyui-tabs">
		<div title="省政府文件" data-options="selected:true, closable:false" style="padding:5 0 0 0;">
			<div class="well well-small" style="margin-left: 5px;margin-top: 5px">
				<span class="badge">提示</span>
				<p>
					在此你可以对<span class="label-info"><strong>采购审批表</strong></span>进行管理!<br/>
					其中<span class="label-info"><strong>补货</strong></span>是对<span class="label-info"><strong>销售订单</strong></span>中的缺货产品或配件进行补仓。
				</p>
			</div>
			<table id="s_datagrid"></table>
		</div>
		<div title="国务院文件" data-options="closable:false" style="padding:5 0 0 0;">
			<table id="g_datagrid"></table>
		</div>
		<div title="其他文件" data-options="closable:false" style="padding:5 0 0 0;">
			<table id="o_datagrid"></table>
		</div>
	</div>
  </body>
</html>
