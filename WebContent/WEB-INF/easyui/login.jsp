<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/taglibs/login_tags.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>欢迎登陆</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<style type="text/css">
	html {
		background-image: none;
	}
	
	label.iPhoneCheckLabelOn span {
		padding-left: 0px
	}

	#versionBar {
		background-color: #212121;
		position: fixed;
		width: 100%;
		height: 35px;
		bottom: 0;
		left: 0;
		text-align: center;
		line-height: 35px;
		z-index: 11;
		-webkit-box-shadow: black 0px 10px 10px -10px inset;
		-moz-box-shadow: black 0px 10px 10px -10px inset;
		box-shadow: black 0px 10px 10px -10px inset;
	}
	
	.copyright {
		text-align: center;
		font-size: 10px;
		color: #CCC;
	}
	
	.copyright a {
		color: #A31F1A;
		text-decoration: none
	}
	
	/*update-begin--Author:tanghong  Date:20130419 for：【是否】按钮错位*/
	.on_off_checkbox{
		width:0px;
	}
	/*update-end--Author:tanghong  Date:20130419 for：【是否】按钮错位*/
	#login .logo {
		width: 500px;
		height: 51px;
	}
	#cap{
	margin-left: 88px;
	}
	</style>
	<script language=”JavaScript”> 
		if (window != top) 
		top.location.href = location.href; 
	</script>
  </head>
  <body>
	<div id="alertMessage">${msg }</div>
	<div id="successLogin"></div>
	<div class="text_success">
		<img src="../extend/loader_green.gif" alt="Please wait" /> <span>登陆成功!请稍后....</span>
	</div>
	<div id="login">
		<div class="ribbon" style="background-image:url(../extend/typelogin.png);"></div>
		<div class="inner">
			<div class="logo">
				<img src="../extend/toplogo-jeecg.png" />
			</div>
			<div class="formLogin">
				<form name="formLogin" action="" id="formLogin" method="post">
					<div class="tip">
						<input class="userName" type="text" name="name" id="name" value="admin" title="用户名" nullmsg="请输入用户名!" iscookie="true" />
					</div>
					<div class="tip">
						<input class="password" name="passwd" type="password" id="passwd" value="123" title="密码" nullmsg="请输入密码!" />
					</div>
					<div id="cap" class="tip">
						<c:if test="${jcaptchaEbabled}">
							<input class="captcha" name="jcaptchaCode" id="jcaptchaCode" type="text" nullmsg="请输入验证码!" />
							<img style="width:85px;height:35px;margin-top: -10px;" align="absmiddle" id="jcaptcha" src="${ctx }/jcaptcha.jpg"/>
						</c:if>
					</div>
					<div class="loginButton">
						<div style="float: left; margin-left: -9px;">
							<input type="checkbox" id="on_off" name="rememberMe" checked="ture" class="on_off_checkbox" value="0" /> <span class="f_help">下次自动登录?</span>
						</div>
						<div style="float: right; padding: 3px 0; margin-right: -12px;">
							<div>
								<ul class="uibutton-group">
									<li><a class="uibutton normal" href="javascript:void(0);" id="but_login">登陆</a>
									</li>
									<li><a class="uibutton normal" href="javascript:void(0);" id="forgetpass">重置</a>
									</li>
								</ul>
							</div>
							<div style="float: left; margin-left: 30px;">
								<a href="javascript:void(0);" onclick=""><span class="f_help">是否初始化admin的密码</span></a>
							</div>
						</div>
						<div class="clear"></div>
					</div>
				</form>
			</div>
		</div>
		<div class="shadow"></div>
	</div>
	<!--Login div-->
	<div class="clear"></div>
	<div id="versionBar">
		<div class="copyright">&copy; 版权所有 <span class="tip"><a href="javascript:void(0);" title="SpringOA">SpringOA</a>
				- 技术支持:<a href="javascript:void(0);" title="zhaoml529@163.com">zml</a> </span>
		</div>
	</div>
	<script>
	    $(function() {
	        $("#jcaptcha").click(function() {
	            $("#jcaptcha").attr("src", '${ctx}/jcaptcha.jpg?'+new Date().getTime());
	        });
	        var msg = $("#alertMessage").html();
	        if(msg != ''){
		        showError(msg);
	        }
	    });
	</script>
</body>
</html>
