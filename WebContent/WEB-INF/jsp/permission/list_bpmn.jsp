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
	$(function() {
		var message = "${message}";
		if(message != ""){
			$( "#dialog-complete" ).dialog({
			      modal: true,
			      buttons: {
			        Ok: function() {
			          $( this ).dialog( "close" );
			        }
			      }
		    });
		}
	});
	function modelDialog( data ){
		$('#dialog-form').dialog({
		      height: 300,
		      width: 'auto',
		      modal: true,
		      buttons: [
		        {text: '添加',
				 click: function() {
	                for(var i=0;i<data.length;i++) {  
	                   var key = data[i].taskDefKey;
	                   var taskName = data[i].taskName;
	                   if (!$('#'+key+"_name").val()) {
							alert("对不起，您还没设置 \'"+taskName+"\'");
							$('#'+key+"_name").focus();
							return;
						}
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
		if(window.confirm("此操作将会删除所有已设定审批人员，确定吗？")){
			$.ajax({
	            type: "POST",
	            url: "${ctx }/permissionAction/initialization",
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
	           			       	  window.location.reload();
	           			        }
	           			      },
		           			  close: function() {
		           				$("#message").html("");
		           				window.location.reload();
		           	          },
	           		    	})},
	      		    	500	//延时500ms
	           		 );
	            },
	            beforeSend:function(){
	            	$.blockUI({
	                    theme:     true,              // true to enable jQuery UI CSS support
	                    draggable: true,              // draggable option requires jquery UI
	                    title:    '提示',              // only used when theme == true
	                    message:  '<img src="${ctx }/images/ui-anim_basic_16x16.gif" alt="Loading" />正在初始化，请稍候...'   // message to display
	                    //timeout:   2000            // close block after 2 seconds (good for demos, etc)
	            	});
	        	},
	        	complete: function(){
	        		$.unblockUI();
	       		}
			});
		}
			
	}
		
		//选择人或候选人
	function chooseUser( multiSelect, taskDefKey ){
		$('#choose-user').dialog({
		      height: 400,
		      width: 1000,
		      modal: true,
		      open: function () { 
					$("#choose-user").html('<iframe src="${ctx}/userAction/chooseUser_page?groupId=-1&flag='+multiSelect+'&key='+taskDefKey+'" frameborder="0" height="100%" width="100%" id="dialogFrame" name="dialogFrame" scrolling="auto"></iframe>'); 
			  }, 
		      buttons: [
		        {text: '确定',
				 click: function() {
					 //调用子页面方法,dialogFrame不能为id，因为在FireFox下id不能获取iframe对象
					 dialogFrame.window.getValue();
					$(this).dialog("close");
				}},
				{text:'取消',
				 click: function() {
					 $(this).dialog("close");
				}}
				]
		});
	}
	
	//选择候选组
	function chooseGroup( taskDefKey ){
		$('#choose-group').dialog({
		      height: 400,
		      width: 1000,
		      modal: true,
		      open: function () { 
					$("#choose-group").html('<iframe src="${ctx}/groupAction/chooseGroup_page?key='+taskDefKey+'" frameborder="0" height="100%" width="100%" id="dialogGroupFrame" name="dialogGroupFrame" scrolling="auto"></iframe>'); 
			  }, 
		      buttons: [
		        {text: '确定',
				 click: function() {
					 //调用子页面方法
					 dialogGroupFrame.window.getValue();
					$(this).dialog("close");
				}},
				{text:'取消',
				 click: function() {
					 $(this).dialog("close");
				}}
				]
		});
}
	
	//子窗口调用-关闭选人或组页面
	function closeDialogFrame(){
		$("#choose-user").dialog("close");
	}
	
	function setAuthor( proKey ){
		$.ajax({
            type: "POST", 
            url: "${ctx}/permissionAction/listUserTask",
            data: {processKey: proKey},
            success: function (data) {
              if(data.length == 0){
            	  $("#message").html("请先初始化流程定义，再设定审批人员！");
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
       		      })
              }else{
	              for(var i=0;i<data.length;i++) {  
	            	  	 //逐个显示审批人员
	                     outputData(data[i]);
	              }
	              //最后显示model
	              modelDialog( data );
	              $("#modelForm").attr("action","${ctx}/permissionAction/setPermission?processKey="+data[0].procDefKey);
              }
           }
		});
	}
	
	function outputData( obj ){
		var taskDefKey = obj.taskDefKey;
		var taskType = obj.taskType;
		//普通用户节点
		var modal = 
		'<td>\
			<table style="border: 2px solid red;padding: 5px;margin: 5px;">\
			<tr>\
				<td>名称:</td>\
				<td>'+obj.taskName+'</td>\
			</tr>\
			<tr><td colspan="2" style="height: 10px"></td></tr>\
			<tr>\
				<td>类型:</td>\
				<td>\
					<input type="radio" name="'+taskDefKey+'_taskType" value="assignee" id="assignee" onclick="chooseUser(false,\''+taskDefKey+'\');" />人员\
			        <input type="radio" name="'+taskDefKey+'_taskType" value="candidateUser" id="candidateUser" onclick="chooseUser(true,\''+taskDefKey+'\');" />候选人\
			        <input type="radio" name="'+taskDefKey+'_taskType" value="candidateGroup" id="candidateGroup" onclick="chooseGroup(\''+taskDefKey+'\');" />候选组\
				</td>\
			</tr>\
			<tr><td colspan="2" style="height: 10px"></td></tr>\
			<tr>\
				<td>选择:</td>\
				<td>\
					<input type="text" id="'+taskDefKey+'_name" name="'+taskDefKey+'_name" readonly class="text ui-widget-content ui-corner-all"/>\
					<input type="hidden" id="'+taskDefKey+'_id" name="'+taskDefKey+'_id" class="text ui-widget-content ui-corner-all"/>\
				</td>\
			</tr>\
			</table>\
    	</td>\
		';
    	//修改任务的节点已经在配置文件的 initiator 中设置，此处不用选择任务办理人。
    	var modify = 
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
					任务发起人\
					<input type="hidden" value="modify" name="'+taskDefKey+'_taskType" />\
				</td>\
			</tr>\
			<tr><td colspan="2" style="height: 10px"></td></tr>\
			<tr>\
				<td>选择:</td>\
				<td>\
					<input type="text" id="'+taskDefKey+'_name" value="任务发起人" name="'+taskDefKey+'_name" class="text ui-widget-content ui-corner-all"/>\
					<input type="hidden" id="'+taskDefKey+'_id" value="0" name="'+taskDefKey+'_id" class="text ui-widget-content ui-corner-all"/>\
				</td>\
			</tr>\
			</table>\
    	</td>\
		';
    	if(taskDefKey == "modifyApply"){
    		$(modify).appendTo($("#modelTable"));
    	}else{
    		var modal = $(modal).appendTo($("#modelTable"));
    		if(taskType == "assignee"){
	    		modal.find("table input[id=assignee]").attr("checked","checked");
    		}else if(taskType == "candidateUser"){
    			modal.find("table input[id=candidateUser]").attr("checked","checked");
    		}else if(taskType == "candidateGroup"){
    			modal.find("table input[id=candidateGroup]").attr("checked","checked");
    		}
    		modal.find("table input[id="+taskDefKey+"_name]").attr("value",obj.candidate_name);
    		modal.find("table input[id="+taskDefKey+"_id]").attr("value",obj.candidate_ids);
    	}
	}
</script>

</head>

<body>

	<div id="main">
	  <div id="dialog-complete" title="complete" style="display: none;">
		    <span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 1px 5px 0;"></span>
		  	<span id="message">${message }</span>
	  </div>
      <div class="sort_switch">
          <ul id="TabsNav">
          	  <li class="selected"><a href="#">审批设定</a></li>
          </ul>
      </div>
	  <div style="text-align: right;padding: 2px 1em 2px">
	  	<div class="tableHue1" style="display:inline;"><b>提示：</b>如果bpmn文件结构没有变化，则不需要初始化。哪个文件发生变化则单个加载再设定人员即可。</div>
      	<button onclick="initialization();" class="input_button4" title="删除user_task表中现有流程定义，全部重新部署">初始化</button>
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
						<td align="center">
	                        <!-- <a href='javascript:void(0)' id="create">设定人员</a>| -->
	                        <a href='javascript:void(0)' onclick="setAuthor('${process.key}')">设定人员</a>|
	                        <a href='${ctx }/permissionAction/loadSingleBpmn?processDefinitionId=${process.id}' title="加载单个流程文件">加载</a>
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
		 <form id="modelForm" method="post">
			<table>
				<tr id="modelTable">
				</tr>
			</table>
		 </form>
	  </div>
	
	<div id="choose-user" title="选择人员" style="display: none;">
	</div>
	
	<div id="choose-group" title="选择候选组" style="display: none;">
	</div>

</div>
    
</body>

</html>
