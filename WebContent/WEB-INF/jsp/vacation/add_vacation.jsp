<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
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
	$(function() {
		var message = "${message}";
		var error = "${error}";
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
		if(error != ""){
			$( "#dialog-error" ).dialog({
			      modal: true,
			      buttons: {
			        Ok: function() {
			          $( this ).dialog( "close" );
			        }
			      }
		    });
		}
	});
	
</script>
</head>
<body>
	<form:form action="${ctx }/vacationAction/doAdd" modelAttribute="vacation" method="POST">
        <div id="main">
        <div class="where">
            <ul>
            	<form:errors path="*" cssStyle="font-color:red"/>
            </ul>
        </div>
        <div id="dialog-complete" title="complete">
		  <p>
		    <span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 1px 5px 0;"></span>
		    ${message}
		  </p>
		</div>
		<div id="dialog-error" title="error">
		  <p>
		    <span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 1px 5px 0;"></span>
		    ${error}
		  </p>
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
                          <td width="15%" class="title1">
                          	<label for="beginDate" class="title1">开始日期：</label>
                          </td>
                          <td class="left">
                          	<form:input id="beginDate" path="beginDate" cssClass="input_text2"/>
                          </td>
                        </tr>
                        <tr>
                          <td width="15%" class="title1">结束日期：</td>
                          <td class="left">
                          	<form:input id="endDate" path="endDate" cssClass="input_text2"/>
                          </td>
                        </tr>
                        <tr>
                          <td width="15%" class="title1">请假天数：</td>
                          <td class="left">
                          	<form:input path="days" cssClass="input_text2" size="30"/>
                          </td>
                        </tr>
                        <tr>
                          <td width="15%" class="title1">休假类型：</td>
                          <td class="left">
							<form:select path="vacationType">
								<option>--请选择--</option>
								<form:option value="0">年假</form:option>
								<form:option value="1">事假</form:option>
								<form:option value="2">病假</form:option>
							</form:select>
						  </td>
                        </tr>
                        <tr>
                          <td width="15%" class="title1">原因：</td>
                          	<td class="left">
								<form:textarea path="reason" cols="33" rows="5"/>
							</td>
                        </tr>
                      </tbody>
                  </table>
                </div>
            </div>
            <div class="fun_area" style="text-align:center;"><input type="submit" value="确 定" class="input_button1"/></div>
      	</div>
	</div>        
    </form:form>

</body>
</html>