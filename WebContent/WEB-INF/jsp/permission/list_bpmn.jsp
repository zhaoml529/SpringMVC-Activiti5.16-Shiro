<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />    
<title>设定审批人员</title>
<script type="text/javascript" src="${ctx}/js/jquery.blockUI.js"></script>
<script type="text/javascript">
	function modelDialog(){
		$('#dialog-form').dialog({
		      height: 300,
		      width: 'auto',
		      modal: true,
		      buttons: [
		        {text: '创建',
				 click: function() {
					if (!$('#name').val()) {
						alert('请填写名称！');
						$('#name').focus();
						return;
					}
	                setTimeout(function() {
	                      location.reload();
	                  }, 1000);
					$('#modelForm').submit();
				}},
				{text:'取消',
				 click: function() {
					 $(this).dialog("close");
				}}
			],
			close: function () { 
				$("#modelTable").html("");
			}
		});
	}
	function initialization(){
		//window.location.href = '${ctx }/permissionAction/initialization';
		$.ajax({
            type: "POST", //使用post方法访问后台
            url: "${ctx }/permissionAction/initialization", //要访问的后台地址
            data: {},
            success: function (data) {
            	if(data == "success"){
            		$("#message").html("初始化成功！");
            	}else{
            		$("#message").html("初始化失败！");
            	}
            	setTimeout(function () {
       				$( "#dialog-complete" ).dialog({
           			      modal: true,
           			      buttons: {
           			        Ok: function() {
           			          $( this ).dialog( "close" );
           			        }
           			      },
	           			  close: function() {
	           				$("#message").html("");
	           	          },
           		    	})},
      		    	500	//延时500ms
           		 );
            },
            beforeSend:function(){
            	$.blockUI({
                    theme:     true,              // true to enable jQuery UI CSS support
                    draggable: true,                  // draggable option requires jquery UI
                    title:    '提示',             // only used when theme == true
                    message:  '<img src="${ctx }/images/ui-anim_basic_16x16.gif" alt="Loading" />正在初始化，请稍候...'   // message to display
                    //timeout:   2000                   // close block after 2 seconds (good for demos, etc)
            	});
        	},
        	complete: function(){
        		$.unblockUI();
       		}
		});
	}
	
	function chooseUser( multiSelect, taskDefKey ){
			$('#choose-user').dialog({
			      height: 400,
			      width: 1000,
			      modal: true,
			      open: function () { 
						$("#choose-user").html('<iframe src="${ctx}/userAction/chooseUser?groupId=-1&flag='+multiSelect+'&key='+taskDefKey+'" frameborder="0" height="100%" width="100%" id="dialogFrame" scrolling="auto"></iframe>'); 
				  }, 
			      buttons: [
			        {text: '创建',
					 click: function() {
						if (!$('#name').val()) {
							alert('请填写名称！');
							$('#name').focus();
							return;
						}
		                setTimeout(function() {
		                      location.reload();
		                  }, 1000);
						$('#modelForm').submit();
					}},
					{text:'取消',
					 click: function() {
						 $(this).dialog("close");
					}}
					]
			});
	}
	
	function setAuthor( proKey ){
		$.ajax({
            type: "POST", //使用post方法访问后台
            url: "${ctx}/permissionAction/listUserTask", //要访问的后台地址
            data: {processKey: proKey},
            success: function (data) {
              for(var i=0;i<data.length;i++) {  
                     outputData(data[i]);
                     //obj.id = data[i].bh;  
                     //obj.title = data[i].yhtmz==null?'无':data[i].yhtmz +" ( "+data[i].zt+" ) ";  
              }
              modelDialog();
           }
		});
	}
	
	function outputData( obj ){
		var modal = 
		'<td>\
			<table style="border: 2px solid red;padding: 5px;margin: 5px">\
			<tr>\
				<td>名称:</td>\
				<td>'+obj.taskName+'</td>\
			</tr>\
			<tr><td colspan="2" style="height: 10px"></td></tr>\
			<tr>\
				<td>类型:</td>\
				<td>\
					<input type="radio" name="taskType" value="0" id="key" onclick="chooseUser(false,\''+obj.taskDefKey+'\');" />人员\
			        <input type="radio" name="taskType" value="1" id="key" onclick="chooseUser(true,\''+obj.taskDefKey+'\');" />候选人\
			        <input type="radio" name="taskType" value="2" id="key" />候选组\
				</td>\
			</tr>\
			<tr><td colspan="2" style="height: 10px"></td></tr>\
			<tr>\
				<td>选择:</td>\
				<td>\
					<input type="text" id="'+obj.taskDefKey+'_name" name="name" class="text ui-widget-content ui-corner-all"/>\
					<input type="hidden" id="'+obj.taskDefKey+'_id" name="name" class="text ui-widget-content ui-corner-all"/>\
				</td>\
			</tr>\
			</table>\
    	</td>\
		';
    	var modal = $(modal).appendTo($("#modelTable"));
	}
</script>

</head>

<body>

	<div id="main">
	  <div id="dialog-complete" title="complete" style="display: none;">
		    <span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 1px 5px 0;"></span>
		  	<span id="message"></span>
	  </div>
      <div class="sort_switch">
          <ul id="TabsNav">
          	  <li class="selected"><a href="#">审批设定</a></li>
          </ul>
      </div>
	  <div style="text-align: right;padding: 2px 1em 2px">
      	<button onclick="initialization();" class="input_button4">初始化</button>
      </div>
      <div class="sort_content">
      	<form action=" ${ctx }/permissionAction/loadBpmn_page" method="post">
          <table class="tableHue1" width="100%" border="1" bordercolor="#a4d5e3" cellspacing="0" cellpadding="0">
              <thead>
                <tr>
					<th>名称</th>
					<th>KEY</th>
					<th>XML</th>
					<th>图片</th>
					<th>操作</th>
                </tr>
              </thead>
              <tbody id="tbody">
	              <c:forEach items="${proDefList }" var="process">
					<tr>
						<td>${process.name }</td>
						<td>${process.key }</td>
						<td><a target="_blank" href='${ctx }/processAction/process/process-definition?processDefinitionId=${process.id}&resourceType=xml'>${process.resourceName }</a></td>
						<td><a target="_blank" href='${ctx }/processAction/process/process-definition?processDefinitionId=${process.id}&resourceType=image'>${process.diagramResourceName }</a></td>
						<td>
	                        <!-- <a href='javascript:void(0)' id="create">设定人员</a>| -->
	                        <a href='javascript:void(0)' onclick="setAuthor('${process.key}')">设定人员</a>|
	                        <a href='${ctx }/permissionAction/loadSingleBpmn?processDefinitionId=${process.id}'>加载 ${name }</a>
	                    </td>
					</tr>
				  </c:forEach>
              	<tr>
              		<td class="fun_area" colspan="5" align="center"><c:out value="${page }" escapeXml="false" /></td>
              	</tr>
              </tbody>
          </table>
         </form>
      </div>
      
      <div id="dialog-form" title="设定审批人员" style="display: none;">
		 <p class="validateTips">请选择各个节点需要审批的人员、候选人和候选组</p>
		 <form id="modelForm" action="${ctx}/modelAction/create" target="_blank" method="post">
			<table>
				<tr id="modelTable">
				</tr>
			</table>
		 </form>
	</div>
	
	<div id="choose-user" title="选择人员" style="display: none;">
	</div>

</div>
    
</body>

</html>
