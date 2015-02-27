<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />    
<title>流程设计模型</title>
<style>
    label, input { display:block; text-align: left;}
    input.text { margin-bottom:12px; width:95%; padding: .4em; }
    textarea { margin:0 auto; width:100%; height:60px; padding: .4em; }
    fieldset { padding:0; border:0; margin-top:25px; }
    h1 { font-size: 1.2em; margin: .6em 0; }
    div#users-contain { width: 350px; margin: 20px 0; }
    div#users-contain table { margin: 1em 0; border-collapse: collapse; width: 100%; }
    div#users-contain table td, div#users-contain table th { border: 1px solid #eee; padding: .6em 10px; text-align: left; }
    .ui-dialog .ui-state-error { padding: .3em; }
    .validateTips { border: 1px solid transparent; padding: 0.3em; }
</style>
<script type="text/javascript">
	$(function() {
	    
	    $('#create').click(function() {
    		$('#dialog-form').dialog({
    		      height: 400,
    		      width: 500,
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
    				]
    		})
    	});
	});
</script>
</head>

<body>

	<div id="main">
      <c:if test="${not empty message}">
		<div class="ui-widget">
			<div class="ui-state-highlight ui-corner-all" style="margin-top: 20px; padding: 0 .7em;"> 
				<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
				<strong>提示：</strong>${message}</p>
			</div>
		</div>
	  </c:if>  
      <div class="sort_switch">
          <ul id="TabsNav">
          	  <li class="selected"><a href="#">模型列表</a></li>
          </ul>
      </div>
      <div style="text-align: right;padding: 2px 1em 2px">
      	<button id="create" class="input_button4">
      		创建
      	</button>
      </div>
      <div class="sort_content">
      	<form action=" ${ctx }/modelAction/listModel_page" method="post">
          <table class="tableHue1" width="100%" border="1" bordercolor="#a4d5e3" cellspacing="0" cellpadding="0">
              <thead>
                <tr>
                  	<th>ID</th>
					<th>KEY</th>
					<th>Name</th>
					<th>Version</th>
					<th>创建时间</th>
					<th>最后更新时间</th>
					<th>元数据</th>
					<th>操作</th>
                </tr>
              </thead>
              <tbody id="tbody">
              <c:forEach items="${list }" var="model">
				<tr>
					<td>${model.id }</td>
					<td>${model.key }</td>
					<td>${model.name}</td>
					<td>${model.version}</td>
					<td>${model.createTime}</td>
					<td>${model.lastUpdateTime}</td>
					<td>${model.metaInfo}</td>
					<td>
						<a href="${ctx}/modeler/service/editor?id=${model.id}" target="_blank">编辑</a>|
						<a href="${ctx}/modelAction/deploy/${model.id}">部署</a>|
						<a href="${ctx}/modelAction/export/${model.id}" target="_blank">导出</a>|
                        <a href="${ctx}/modelAction/delete/${model.id}">删除</a>
					</td>
				</tr>
			</c:forEach>
              	<tr>
              		<td class="fun_area" colspan="8" align="center">${page }</td>
              	</tr>
              </tbody>
          </table>
         </form>
      </div>
      
      
      <div id="dialog-form" title="创建模型" style="display: none;">
		 <p class="validateTips">All form fields are required.</p>
		 <form id="modelForm" action="${ctx}/modelAction/create" target="_blank" method="post">
		    <fieldset>
		      <label for="name">名称:</label>
		      <input type="text" name="name" id="name" class="text ui-widget-content ui-corner-all">
		      <label for="key">KEY:</label>
		      <input type="text" name="key" id="key" class="text ui-widget-content ui-corner-all">
		      <label for="description">描述:</label>
		 	  <textarea name="description" id="description" class="text ui-widget-content ui-corner-all"></textarea>
		      <!-- Allow form submission with keyboard without duplicating the dialog button -->
		      <input type="submit" tabindex="-1" style="position:absolute; top:-1000px">
		    </fieldset>
		 </form>
	</div>
      


</div>
    
</body>

</html>
