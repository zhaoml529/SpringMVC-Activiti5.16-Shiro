<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>薪资调整审批</title>
<script type="text/javascript">
function complete( flag ) {
	$("#completeFlag").val(flag);
	$("#audit").submit();
}
</script>
</head>

<body>
<form id="audit" action="<c:url value="/salaryAction/complate/${salary.task.id }"/>" method="post">
	<input type="hidden" name="userId" value="${user.id }" />
	<input type="hidden" name="salaryAdjustId" value="${salary.id }" />
	<input type="hidden" id="completeFlag" name="completeFlag" value="" />
	<div id="main">
        <div class="where">
            <ul>
            </ul>
        </div>
        
      <div class="sort_switch">
          <ul id="TabsNav">
          	  <li class="selected"><a href="#">薪资调整审批</a></li>
          </ul>
      </div>
      
      <div id="tagContent0" class="sort_content">
			
        	<div class="currency_area hue9">
            	<div class="the_content">
                	<table class="tableHue2" width="100%" border="1" bordercolor="#dddddd" cellspacing="0" cellpadding="0">
                      <tbody>
                        <tr>
							<td width="15%" class="title1">员工姓名：</td>
							<td class="left"><input name="user_name" type="text" value="${salary.user_name }" class="input_text2" size="30" /></td>
                        </tr>
                        <tr>
							<td width="15%" class="title1">调薪金额 ：</td>
							<td class="left"><input name="adjustMoney" type="text" value="${salary.adjustMoney }" class="input_text2" size="30" /></td>
                        </tr>
                        <tr>
                        <td width="15%" class="title1">描述：</td>
                          	<td class="left">
								<textarea cols="33" rows="5" name="dscp">${salary.dscp }</textarea>
							</td>
                        </tr>
                        <tr>
                        <td width="15%" class="title1">评论：</td>
                          	<td class="left">
                          		
								<table>
									<c:forEach var="comment" items="${commentList}">
									<tr>
										<td>${comment.userName}- <fmt:formatDate value="${comment.time }" type="date" /> </td>
									</tr>
									<tr>
										<td>${comment.content}</td>
									</tr>
									</c:forEach>
								</table>
							</td>
                        </tr>
                        <tr>
                          <td width="15%" class="title1">我的意见：</td>
                          	<td class="left">
								<textarea cols="33" rows="5" name="content"></textarea>
							</td>
                        </tr>
                      </tbody>
                   </table>
                </div>
            </div>
            
            <div class="fun_area" style="text-align:center;">
            	<input type="button" onclick="complete(true);" value="同 意" class="input_button1"/>
            	<input type="button" onclick="complete(false);" value="不同意" class="input_button1"/>
            </div>

      </div>
</div>
</form>
</body>
</html>
