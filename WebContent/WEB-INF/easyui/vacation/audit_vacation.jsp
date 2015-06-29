<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>请假申请</title>
<script type="text/javascript">
function complete( flag ) {
	$("#completeFlag").val(flag);
	$("#audit").submit();
}
</script>
</head>

<body>
<form id="audit" action="<c:url value="/vacationAction/complate/${vacation.task.id }"/>" method="post">
	<input type="hidden" name="userId" value="${user.id }" />
	<input type="hidden" name="vacationId" value="${vacation.id }" />
	<input type="hidden" id="completeFlag" name="completeFlag" value="" />
	<div id="main">
        <div class="where">
            <ul>
            </ul>
        </div>
        
      <div class="sort_switch">
          <ul id="TabsNav">
          	  <li class="selected"><a href="#">请假申请</a></li>
          </ul>
      </div>
      
      <div id="tagContent0" class="sort_content">
			
        	<div class="currency_area hue9">
            	<div class="the_content">
                	<table class="tableHue2" width="100%" border="1" bordercolor="#dddddd" cellspacing="0" cellpadding="0">
                      <tbody>
                        <tr>
                          <td width="15%" class="title1">开始日期：</td>
                          <td class="left"><input name="beginDate" value="<fmt:formatDate value="${vacation.beginDate}" type="date" />" id="beginDate" type="text" class="input_text2" size="30" readonly/></td>
                        </tr>
                        <tr>
                          <td width="15%" class="title1">结束日期：</td>
                          <td class="left"><input name="endDate" value="<fmt:formatDate value="${vacation.endDate}" type="date" />" id="endDate" type="text" class="input_text2" size="30" readonly/></td>
                        </tr>
                        <tr>
                          <td width="15%" class="title1">请假天数：</td>
                          <td class="left"><input name="days" value="${vacation.days }" type="text" class="input_text2" size="30" readonly/></td>
						  </td>
                        </tr>
                        <tr>
                          <td width="15%" class="title1">休假类型：</td>
                          <td class="left">
							<select name="vacationType" disabled="disabled">
									<option ${vacation.vacationType == 0?"selected":"" } value="0">年假</option>
									<option ${vacation.vacationType == 1?"selected":"" } value="1">事假</option>
									<option ${vacation.vacationType == 2?"selected":"" } value="2">病假</option>
							</select>
						  </td>
                        </tr>
                        <tr>
                          <td width="15%" class="title1">原因：</td>
                          	<td class="left">
								<textarea cols="33" rows="5" name="reason" readonly="readonly">${vacation.reason }</textarea>
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
