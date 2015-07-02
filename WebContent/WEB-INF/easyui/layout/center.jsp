<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" charset="utf-8">
	var centerTabs;
	var tabsMenu;
	$(function() {
		tabsMenu = $('#tabsMenu').menu({
			onClick : function(item) {
				var curTabTitle = $(this).data('tabTitle');
				var type = $(item.target).attr('type');

				if (type === 'refresh') {
					refreshTab(curTabTitle);
					return;
				}

				if (type === 'close') {
					var t = centerTabs.tabs('getTab', curTabTitle);
					if (t.panel('options').closable) {
						centerTabs.tabs('close', curTabTitle);
					}
					return;
				}

				var allTabs = centerTabs.tabs('tabs');
				var closeTabsTitle = [];

				$.each(allTabs, function() {
					var opt = $(this).panel('options');
					if (opt.closable && opt.title != curTabTitle && type === 'closeOther') {
						closeTabsTitle.push(opt.title);
					} else if (opt.closable && type === 'closeAll') {
						closeTabsTitle.push(opt.title);
					}
				});

				for ( var i = 0; i < closeTabsTitle.length; i++) {
					centerTabs.tabs('close', closeTabsTitle[i]);
				}
			}
		});

		centerTabs = $('#centerTabs').tabs({
			tools : [ {
					iconCls : 'icon-reload',
					handler : function() {
						var href = $('#centerTabs').tabs('getSelected').panel('options').href;
						if (href) {/*说明tab是以href方式引入的目标页面*/
							var index = $('#centerTabs').tabs('getTabIndex', $('#centerTabs').tabs('getSelected'));
							$('#centerTabs').tabs('getTab', index).panel('refresh');
						} else {   /*说明tab是以content方式引入的目标页面*/
							var panel = $('#centerTabs').tabs('getSelected').panel('panel');
							var frame = panel.find('iframe');
							try {
								if (frame.length > 0) {
									for ( var i = 0; i < frame.length; i++) {
										frame[i].contentWindow.document.write('');
										frame[i].contentWindow.close();
										frame[i].src = frame[i].src;
									}
									if ($.browser.msie) {
										CollectGarbage();
									}
								}
							} catch (e) {
							}
						}
					}
				}, {
					iconCls : 'icon-cancel',
					handler : function() {
						var index = $('#centerTabs').tabs('getTabIndex', $('#centerTabs').tabs('getSelected'));
						var tab = $('#centerTabs').tabs('getTab', index);
						if (tab.panel('options').closable) {
							$('#centerTabs').tabs('close', index);
						} else {
							$.messager.alert('提示', '[' + tab.panel('options').title + ']不可以被关闭', 'error');
						}
                  			}
                 		},{
                   iconCls:'icon-color',
                   handler:function(){
	                   $('#theme').menu({   
							onClick:function(item){   
	                        	var cookiesColor1=jqueryUtil.cookies.get("cookiesColor");
	                        	if(cookiesColor1!=item.id){
									jqueryUtil.cookies.set("cookiesColor",item.id,30);
									jqueryUtil.chgSkin(item.id,cookiesColor1);
	                            }
	                        	/* var curTabTitle = $(this).data('tabTitle');
	                        	alert(curTabTitle);
	                        	refreshTab(curTabTitle); */
							}   
							});
	                    $('#theme').menu('show', {   
								 left: '91%',   
								 top: 97   
						});
	                    }
            }],
			fit : true,
			border : false,
			onContextMenu : function(e, title) {
				e.preventDefault();
				tabsMenu.menu('show', {
					left : e.pageX,
					top : e.pageY
				}).data('tabTitle', title);
			}
		});
		
	});

	function addTab(title, url) {
		if (centerTabs.tabs('exists', title)) {
			centerTabs.tabs('select', title);
		} else {
			centerTabs.tabs('add', {
				title : title,
				closable : true,
				//border:false,
				//iconCls : nodes[1],
				//href : 'error/error.jsp',
				content : "<iframe src="+url+" frameborder=\"0\" style=\"border:0;width:100%;height:99.4%;\"></iframe>",
				tools : [ {
					iconCls : 'icon-mini-refresh',
					handler : function() {
						refreshTab(title);
					}
				} ]
			});
			
		}
	}
	function refreshTab(title) {
		var tab = centerTabs.tabs('getTab', title);
		centerTabs.tabs('update', {
			tab : tab,
			options : tab.panel('options')
		});
	}
	
</script>

<div id="centerTabs">
	<div iconCls="icon-home" title="首页" border="false" style="overflow: hidden;">
		<iframe src="${ctx }/main" frameborder="0" style="border:0;width:100%;height:99.4%;"></iframe>
	</div>
</div>
<div id="tabsMenu" style="width: 120px;display:none;">
	<div type="refresh">刷新</div>
	<div class="menu-sep"></div>
	<div type="close">关闭</div>
	<div type="closeOther">关闭其他</div>
	<div type="closeAll">关闭所有</div>
</div>
<div id="theme" class="easyui-menu" style="width:120px;display: none">  
		    <div id="default" data-options="iconCls:'icon-save'">Default</div>  
		    <div id="black" data-options="iconCls:'icon-save'">Black</div> 
		    <div id="bootstrap" data-options="iconCls:'icon-save'">Bootstrap</div>  
		    <div id="gray" data-options="iconCls:'icon-save'">Gray</div>  
		    <div id="metro" data-options="iconCls:'icon-save'">Metro</div>  
		    <div class="menu-sep"></div>
		    <div id="metro-blue" data-options="iconCls:'icon-save'">Metro-blue</div> 
		    <div id="metro-gray" data-options="iconCls:'icon-save'">Metro-gray</div> 
		    <div id="metro-green" data-options="iconCls:'icon-save'">Metro-green</div> 
		    <div id="metro-orange" data-options="iconCls:'icon-save'">Metro-orange</div> 
		    <div id="metro-red" data-options="iconCls:'icon-save'">Metro-red</div> 
</div>