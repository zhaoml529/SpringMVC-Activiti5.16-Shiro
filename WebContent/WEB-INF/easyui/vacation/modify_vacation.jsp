<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>调整请假申请</title>
<script type="text/javascript">
	$(function() {
		$('#beginDate').datepicker({
			currentText: 'Now',
			showAnim: 'slideDown',
			dateFormat: 'yy-mm-dd'
		}); 
		$('#endDate').datepicker({
			currentText: 'Now',
			showAnim: 'slideDown',
			dateFormat: 'yy-mm-dd'
		});
	  });
	function complete( flag ) {
		$("#reApply").val(flag);
		$("#vacation").submit();
	}
</script>  
</head>

<body>
<form:form action="${ctx }/vacationAction/modifyVacation/${vacation.task.id }" id="vacation" modelAttribute="vacation" method="POST">
	<input type="hidden" name="id" value="${vacation.id }" />
	<input type="hidden" name="processInstanceId" value="${vacation.processInstanceId }" />
	<input type="hidden" name="userId" value="${user.id }" />
	<input type="hidden" id="reApply" name="reApply" value="" />
	<div id="main">
        <div class="where">
            <ul>
            	<form:errors path="*" cssStyle="font-color:red"/>
            </ul>
        </div>
        
      <div class="sort_switch">
          <ul id="TabsNav">
          	  <li class="selected"><a href="#">调整请假申请</a></li>
          </ul>
      </div>
      
      <div id="tagContent0" class="sort_content">
			
        	<div class="currency_area hue9">
            	<div class="the_content">
                	<table class="tableHue2" width="100%" border="1" bordercolor="#dddddd" cellspacing="0" cellpadding="0">
                      <tbody>
                        <tr>
                          <td width="15%" class="title1">开始日期：</td>
                          <td class="left"><input name="beginDate" id="beginDate" value="<fmt:formatDate value="${vacation.beginDate }" pattern="yyyy-MM-dd" />" type="text" class="input_text2" size="30" readonly/></td>
                        </tr>
                        <tr>
                          <td width="15%" class="title1">结束日期：</td>
                          <td class="left"><input name="endDate" id="endDate" value="<fmt:formatDate value="${vacation.endDate }" pattern="yyyy-MM-dd" />" type="text" class="input_text2" size="30" readonly/></td>
                        </tr>
                        <tr>
                          <td width="15%" class="title1">请假天数：</td>
                          <td class="left"><input name="days" type="text" value="${vacation.days }" class="input_text2" size="30" /></td>
						  </td>
                        </tr>
                        <tr>
                          <td width="15%" class="title1">休假类型：</td>
                          <td class="left">
							<select name="vacationType">
									<option ${vacation.vacationType == 0?"selected='selected'":"" } value="0">年假</option>
									<option ${vacation.vacationType == 1?"selected='selected'":"" } value="1">事假</option>
									<option ${vacation.vacationType == 2?"selected='selected'":"" } value="2">病假</option>
							</select>
						  </td>
                        </tr>
                        <tr>
                          <td width="15%" class="title1">原因：</td>
                          	<td class="left">
								<textarea cols="33" rows="5" name="reason">${vacation.reason }</textarea>
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
                      </tbody>
                  </table>
				  
                </div>
            </div>
            
            <div class="fun_area" style="text-align:center;">
            	<input type="button" onclick="complete(true)" value="提 交" class="input_button1"/>
            	<input type="button" onclick="complete(false)" value="取消申请" class="input_button1"/>
            </div>

      </div>
</div>
</form:form>
</body>
</html>
