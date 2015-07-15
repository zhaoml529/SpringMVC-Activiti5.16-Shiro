<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/taglibs/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />    
<title>选取委派人员</title>
<script type="text/javascript">

</script>
</head>
<body>
	<div title="委派人" style="width:986px;height:364px;">
		<div class="easyui-layout" fit="true">
			<div region="west" split="true" style="width:120px;">
				<ul class="easyui-tree">
					<li>
						<span>部门</span>
						<ul>
							<c:choose>
								<c:when test="${empty groupList }">
									暂无部门信息，请联系管理员！
								</c:when>
								<c:otherwise>
									<c:forEach items="${groupList}" var="group">
										<li><a href="javascript:void(0);" onclick="addTab('${group.name}','${group.id }');"><span>${group.name}</span></a></li>
									</c:forEach>
								</c:otherwise>
							</c:choose>  
						</ul>
					</li>
				</ul>
			</div>
			<div region="center" border="false" border="false">
				<div id="userTabs" class="easyui-tabs" fit="true">
					<div title="Home" style="padding:10px;">
						<div class="well well-small">
							<span class="badge" iconCls="icon-save" plain="true" id="tishi" title="提示">提示</span>
							<p>
								请点击左侧<span class="label-info"><strong>部门</strong></span>来选择人员，选择人员后点击<span class="label-info"><strong>确认</strong></span>键设定委派人员！
							</p>
						</div>
					</div>
				</div>
			</div>
			<div region="south" border="false" style="text-align:right;height:30px;line-height:30px;">
				<a class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" onclick="set_chooseUser();">确认</a>
				<a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)" onclick="destroy_chooseUser();">取消</a>
			</div>
		</div>
	</div>
    
</body>

</html>
