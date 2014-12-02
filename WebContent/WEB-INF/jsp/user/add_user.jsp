<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加用户</title>
</head>
<body>
<form action="<c:url value="/userAction/doAdd"/>" method="post">
	<div id="main">
        <div class="where">
            <ul>
            </ul>
        </div>
      <div class="sort_switch">
          <ul id="TabsNav">
          	  <li class="selected"><a href="#">添加用户</a></li>
          </ul>
      </div>
      <div id="tagContent0" class="sort_content">
        	<div class="currency_area hue9">
          		<div class="the_title T10">
            		<h5><strong>用户</strong></h5>
            	</div>
            	<div class="the_content">
					
                	<table class="tableHue2" width="100%" border="1" bordercolor="#dddddd" cellspacing="0" cellpadding="0">
                      <tbody>
                        <tr>
                          <td width="15%" class="title1">用户名称：</td>
                          <td class="left"><input name="name" type="text" class="input_text2" size="30" /></td>
                        </tr>
                        <tr>
                          <td width="15%" class="title1">密码：</td>
                          <td class="left"><input name="passwd" type="text" class="input_text2" size="30" /></td>
                        </tr>
                        <tr>
                          <td width="15%" class="title1">用户组：</td>
                          <td class="left">
							<select name="group.id">
								<c:forEach var="group" items="${groupList}">
									<option value="${group.id}">${group.name}-${group.type}</option>
								</c:forEach>
							</select>
						  </td>
                        </tr>
                      </tbody>
                  </table>
				  
                </div>
            </div>
            
            <div class="fun_area" style="text-align:center;"><input type="submit" value="确 定" class="input_button1"/></div>
      </div>
	</div>
</form>
</body>

</html>
