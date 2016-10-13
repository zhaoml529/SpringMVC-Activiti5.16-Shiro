<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<c:set var="ctx" value="${pageContext.request.contextPath}" />

<%-- <script type="text/javascript" src="${ctx }/js/jquery-1.8.0.min.js"></script> --%>
<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${ctx }/js/easyui/login/jquery.cookie.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx }/css/zice.style.css">
<link rel="stylesheet" type="text/css" href="${ctx }/css/tipsy.css">
<link rel="stylesheet" type="text/css" href="${ctx }/css/icon.css">
<link rel="stylesheet" type="text/css" href="${ctx }/css/buttons.css">
<script type="text/javascript" src="${ctx }/js/easyui/login/iphone.check.js"></script>
<script type="text/javascript" src="${ctx }/js/easyui/login/jquery-jrumble.js"></script>
<script type="text/javascript" src="${ctx }/js/easyui/login/jquery.tipsy.js"></script>
<script type="text/javascript" src="${ctx }/js/easyui/login/login.js"></script>
<script type="text/javascript">
	var ctx = "${ctx}";
	if(top!=self){
		if(top.location != self.location)
		 top.location=self.location; 
	}
</script>
