<%@ tag pageEncoding="UTF-8" description="显示操作成功/失败的消息，内容为:message/error" %>
<%@ attribute name="successMessage" type="java.lang.String" required="false" description="成功消息" %>
<%@ attribute name="errorMessage" type="java.lang.String" required="false" description="失败消息" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(function() {
		$( "#dialog-complete" ).dialog({
		      modal: true,
		      buttons: {
		        Ok: function() {
		          $( this ).dialog( "close" );
		        }
		      }
	    });
	});
</script>
<c:if test="${not empty successMessage}">
    <c:set var="message" value="${successMessage}"/>
</c:if>
<c:if test="${not empty errorMessage}">
    <c:set var="message" value="${errorMessage}"/>
</c:if>
<c:if test="${not empty message}">
   <div id="dialog-complete" title="Message">
 	 <span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 1px 5px 0;"></span>
	 <span id="message">${message }</span>
   </div>
</c:if>