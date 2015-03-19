<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib prefix="oa" tagdir="/WEB-INF/tags" %>

<link rel="stylesheet" href="${ctx}/css/style.css" type="text/css" />
<link rel="stylesheet" href="${ctx}/css/main.css" type="text/css" />
<link rel="stylesheet" href="${ctx}/css/jquery-ui.min.css" type="text/css" />
<link rel="stylesheet" href="${ctx}/css/jquery-ui.structure.min.css" type="text/css" />
<link rel="stylesheet" href="${ctx}/css/jquery-ui.theme.min.css" type="text/css" />
<link rel="stylesheet" href="${ctx}/css/jquery_validation_engine/validationEngine.jquery.css" type="text/css" />
<link rel="stylesheet" href="${ctx}/css/jquery_validation_engine/template.css" type="text/css" />

<script type="text/javascript" src="${ctx}/js/util.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery_validation_engine/jquery.validationEngine.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery_validation_engine/jquery.validationEngine-zh_CN.js"></script>

<c:set var="ctx" value="${pageContext.request.contextPath}" />