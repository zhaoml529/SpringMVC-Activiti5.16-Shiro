<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<TITLE>登录 </TITLE>


<link rel="stylesheet" href="${ctx}/css/style.css" type="text/css" />
<link rel="stylesheet" href="${ctx}/css/top.css" type="text/css" />

<style>

.mainDiv {
	background-color:#CFE8E9;
	height: 100%;
}

.formDiv {
	font-size: 20px; 
	margin-bottom: 20px;
}

.formDiv input {
	width: 200px; 
	height: 30px;
}

.textDiv {
	margin-top: 30px;
}

.textDiv input {
	width: 200px; 
	height: 30px;
}

.buttonDiv {
	margin-top: 30px;
}

.buttonDiv input {
	width: 80px; 
	height: 40px;
	margin-left: 40px;
}

</style>

</HEAD>

<BODY>

<div class="mainDiv">

	<div style="margin-top: 90px; ">
		<div style="height: 40px;"></div>
		<div style="height: 40px; margin-top: 100px;">
			<h1 style="font-size:30px; margin-top: 100px;"><strong>欢迎使用OA系统</strong></h1>
	    </div>
		
		<div class="formDiv">
			<span style="color:red; height: 30px;">${msg}</span>
			<form method="post" action="">
			<div style="margin: auto auto auto 40%; text-align: left;">
				<div class="textDiv">
					<span style="width: 300px; margin-right: 20px;">用户名: </span>
					<input type="text" name="name" value="admin"/>
				
				</div>
				<div class="textDiv">
					<span style="width: 300px; margin-right: 40px;">密码:</span> 
					<input type="password" name="passwd" value="123"/>
				</div>
				<div class="textDiv">
				<c:if test="${jcaptchaEbabled}"> 
					<span style="width: 300px; margin-right: 20px;">验证码:</span> 
					<input type="text" name="jcaptchaCode" style="width: 100px"> 
					<img class="jcaptcha-btn jcaptcha-img" src="${ctx}/jcaptcha.jpg" title="点击更换验证码"> 
					<a class="jcaptcha-btn" href="javascript:;">换一张</a> 
				</c:if> 
				</div>
				<div class="textDiv">
					<input type="checkbox" style="width: 50px" name="rememberMe" value="true">下次自动登录
				</div>
				</div>
				<div class="buttonDiv">
					<input type="submit" value="确定"/>
					<input type="button" value="注册" onclick="window.location.href='${ctx }/userAction/toAdd'"/>
				</div>
			</form>
		</div>
	</div>

</div>
<script>
    $(function() {
        $(".jcaptcha-btn").click(function() {
            $(".jcaptcha-img").attr("src", '${ctx}/jcaptcha.jpg?'+new Date().getTime());
        });
    });
</script>


</BODY>

</HTML>
