<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html>
<head>
  <meta charset="utf-8">
  <!--[if IE]><meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"><![endif]-->
  <title>OA System</title>
  <meta name="keywords" content="" />
  <meta name="description" content="" />
  <meta name="viewport" content="width=device-width"> 
</head>
<body>
  <div id="main-wrapper">
    <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="navbar-header">
        <div class="logo"><h1>OA系统演示</h1></div>
      </div>   
    </div>
    <div class="template-page-wrapper">
      <form class="form-horizontal templatemo-signin-form" role="form" action="${ctx }/loginAction/login" method="post">
        <div id="username" class="form-group">
          <div class="col-md-12">
            <label for="username" class="col-sm-2 control-label">用户名</label>
            <div class="col-sm-10">
              <label id="errorUserName" class="control-label sr-only">${msg }</label>
              <input type="text" class="form-control" name="name" value="${username }" placeholder="Username">
              <span id="username_sr" class="glyphicon glyphicon-remove form-control-feedback sr-only"></span>
            </div>
          </div>              
        </div>
        <div id="password" class="form-group">
          <div class="col-md-12">
            <label for="password" class="col-sm-2 control-label">密&nbsp;&nbsp;码</label>
            <div class="col-sm-10">
              <label id="errorPassWord" class="control-label sr-only">${msg }</label>
              <input type="password" class="form-control" name="passwd" placeholder="Password">
              <span id="password_sr" class="glyphicon glyphicon-remove form-control-feedback sr-only"></span>
            </div>
          </div>
        </div>
        <div id="password" class="form-group">
          <div class="col-md-12">
            <label for="password" class="col-sm-6 control-label">验证码:</label>
            <div class="col-sm-6">
             	<input type="text" name="jcaptchaCode" style="width: 100px"> 
				<img class="jcaptcha-btn jcaptcha-img" src="${ctx}/jcaptcha.jpg" title="点击更换验证码"> 
				<a class="jcaptcha-btn" href="javascript:;">换一张</a> 
            </div>
          </div>
        </div>
        <div class="form-group">
          <div class="col-md-12">
            <div class="col-sm-offset-2 col-sm-10">
              <div class="checkbox">
                <label>
                  <input type="checkbox" style="width: 50px" name="rememberMe" value="true">Remember me
                </label>
              </div>
            </div>
          </div>
        </div>
        <div class="form-group">
          <div class="col-md-12">
            <div class="col-sm-offset-2 col-sm-10">
              <input type="submit" value="登录" class="btn btn-default">
            </div>
          </div>
        </div>
      </form>
    </div>
  </div>
  <script type="text/javascript">
  	var msg = '${msg}';
  	if(msg != ''){
  		if(msg == '用户名不存在'){
  			$("#username").removeClass().addClass('form-group has-error has-feedback');
  			$("#errorUserName").removeClass("sr-only");
  			$("#username_sr").removeClass("sr-only");
  			$("#username").focus();
  		}
  		if(msg == '密码不正确'){
  			$("#password").removeClass().addClass('form-group has-error has-feedback');
  			$("#errorPassWord").removeClass("sr-only");
  			$("#password_sr").removeClass("sr-only");
  			$("#password").focus();
  		}
  	}
  </script>
</body>
</html>