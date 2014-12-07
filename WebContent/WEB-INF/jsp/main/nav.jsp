<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" href="${ctx}/css/nav.css" type="text/css" />

<script type="text/javascript">
 
</script>

</head>

<body>
	<div class="admin_memu">
		<ul id="nav">
        	<li><a href="#" onclick="clickNav(this);return false;" class="parent" id="1"><h5><strong>用户功能</strong></h5></a>
            	<ul id="opt_1" class="child_area">
					<li class="last"><a href="${ctx}/processAction/process/getRuningProcessInstance/vacation" target="mainFrame"><h6>申请查看</h6></a></li>
					<li class="last"><a href="${ctx}/processAction/todoTaskList_page" target="mainFrame"><h6>我的任务</h6></a></li>
					<li class="last"><a href="${ctx}/vacationAction/toAdd" target="mainFrame"><h6>请假</h6></a></li>
					<li class="last"><a href="finance/expenseAccount.jsp" target="mainFrame"><h6>报捎</h6></a></li>
					<li class="last"><a href="salary/salaryAdjust.jsp" target="mainFrame"><h6>薪资调整</h6></a></li>
                </ul>
            </li>

            <li><a href="#" onclick="clickNav(this);return false;" class="parent" id="15"><h5><strong>管理员功能</strong></h5></a>
            	<ul id="opt_15" class="child_area">
                	<li class="last"><a href="${ctx}/groupAction/toList" target="mainFrame"><h6>用户组管理</h6></a></li>
                	<li class="last"><a href="${ctx}/userAction/toList_page" target="mainFrame"><h6>用户管理</h6></a></li>
                	<li class="last"><a href="${ctx}/pd-list.action" target="mainFrame"><h6>流程定义 </h6></a></li>
                </ul>
            </li>
        </ul>
	</div>

</body>
<script>

	window.onload = function() {
		
	};

	function clickNav(a) {
		var childUl = document.getElementById("opt_" + a.id);
		if (childUl.style.display == "block" || childUl.style.display == "")	{
			childUl.style.display = "none";
		} else {
			childUl.style.display = "block";
		}
	}

</script>
</html>
