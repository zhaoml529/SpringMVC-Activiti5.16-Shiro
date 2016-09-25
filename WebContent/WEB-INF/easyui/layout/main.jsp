<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>首页</title>
</head>

<body>

	<div data-options="region:'north',border:false" title="" style="overflow: hidden; padding: 5px;">
		<div class="well well-small">
			<h1 style="font-size:30px;"><strong>欢迎使用OA系统</strong></h1>
			<span class="badge" iconCls="icon-save" plain="true" >系统初始化</span>
			<p>
				不同角色拥有不同的功能菜单，可通过 <span class="label-info"><strong>用户组管理</strong></span>进行权限控制。<br/>
				用admin(所有用户密码默认都为123)进入系统后：<br/><br/>
				1、在<span class="label-info"><strong>用户管理</strong></span>界面点击<span class="label-info"><strong>同步用户</strong></span>按钮，把系统用户和组关系同步到activiti默认的用户表和组表中。<br/><br/>
				
				2、在<span class="label-info"><strong>流程定义管理</strong></span>界面点击<span class="label-info"><strong>重新部署全部流程</strong></span>按钮，系统可以把resources/deploy目录下得所有以.zip或.bar结尾的流程文件部署到系统当中。<br/>
				   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;只有完成了流程的部署，才可以发布任务和完成任务。如果某一个流程描述文件改变了，也可以单个部署这个文件或者点击加载。<br/><br/>
				
				3、在<span class="label-info"><strong>审批设定</strong></span>界面点击<span class="label-info"><strong>初始化</strong></span>按钮，将每个流程描述文件中的用户任务节点初始化到t_user_task表中，此表是用来保存流程文件中设定好的<br/>
				   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;审批人员。初始化完成后，逐个设定审批人员（点击设定人员按钮即可）。如果某个流程描述文件新增或减少了用户任务，则点击设定人员后面的加载按钮即可同步节点。<br/>
			</p>
		</div>
	</div>
    
</body>
</html>
