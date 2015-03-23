<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加资源信息</title>
<script type="text/javascript">
	$(function(){
		var validationEngine = $("#resourceFrom").validationEngine();
		<oa:showFieldError commandName="res"/>
	})
	function selChange( type ){
		if(type == "button"){
			$("#url").attr("disabled",true);
		}else if(type == "menu"){
			$("#url").attr("disabled",false);
		}
	}
</script>  
</head>

<body>
<oa:showMessage/>
<form:form action="${ctx }/resourceAction/doAdd" id="resourceFrom" commandName="res" method="post" cssClass="form-horizontal">
	<div id="main">
      <div class="sort_switch">
          <ul id="TabsNav">
          	  <li class="selected"><a href="#">添加资源信息</a></li>
          </ul>
      </div>
      <div id="tagContent0" class="sort_content">
        	<div class="currency_area hue9">
            	<div class="the_content">
                	<table class="tableHue2" width="100%" border="1" bordercolor="#dddddd" cellspacing="0" cellpadding="0">
                      <tbody>
                        <tr>
                          <td width="15%" class="title1">资源名称：</td>
                          <td class="left"><form:input path="name" cssClass="validate[required]" placeholder="资源名称"/></td>
                        </tr>
                        <tr>
                          <td width="15%" class="title1">资源类型：</td>
                          <td class="left">
                          	<select name="type" id="type" onchange="selChange(this.value);">
									<option ${res.type == 'menu'?"selected='selected'":"" } id="menu" value="menu"> menu </option>
									<option ${res.type == 'button'?"selected='selected'":"" } id="button" value="button"> button </option>
							</select>
                          </td>
                        </tr>
                        <tr>
                          <td width="15%" class="title1">资源路径：</td>
                          <td class="left"><input path="permission" ${res.type eq 'button'?'disabled':'' } cssClass="validate[required]" placeholder="id 顺序" /></td>
                        </tr>
                        <tr>
                          <td width="15%" class="title1">权限字符串：</td>
                          <td class="left"><form:input path="permission" cssClass="validate[required]" placeholder="程序中使用的名称"/></td>
                        </tr>
                        <tr>
                          <td width="15%" class="title1">父编号：</td>
                          <td class="left">
                          	<form:select path="parentId">
                          		<c:forEach items="${list }" var="menu">
									<option ${res.parentId == menu.id?"selected='selected'":"" } value="${menu.id }">${menu.name }</option>
								</c:forEach>
                          	</form:select>
						  </td>
                        </tr>
                        <tr>
                          <td width="15%" class="title1">父编号列表：</td>
                          <td class="left">
                          	<%-- <input id="parentIds" name="parentIds" type="text" value="${res.parentIds }" class="input_text2 validate[required]" size="30" /> --%>
                          	<form:input path="parentIds" cssClass="validate[required]" placeholder="长度在5-200个字符之间"/>
                          </td>
                        </tr>
                      </tbody>
                  </table>
				  
                </div>
            </div>
            
            <div class="fun_area" style="text-align:center;">
            	<input type="submit" value="添   加" class="input_button1"/>
            </div>

      </div>
</div>
</form:form>
<script type="text/javascript">
	$(function(){
		var validationEngine = $("#resourceFrom").validationEngine();
		<oa:showFieldError commandName="res"/>
	})
</script>
</body>
</html>
