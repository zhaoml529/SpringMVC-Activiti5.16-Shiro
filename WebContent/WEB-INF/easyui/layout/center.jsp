<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
	var d =$("#dataTree").tree({
					animate:true,
					checkbox:false,
					onlyLeafCheck:true,
					url:'data/tree_data2.json',
					onClick: function(node){
							addTab(node);  // alert node text property when clicked
					}
					
					
				});
	function addTab(node) {
		var nodes=node.split("||");
		if (centerTabs.tabs('exists', nodes[0])) {
			centerTabs.tabs('select', nodes[0]);
		} else {
			/*if (node.attributes.url && node.attributes.url.length > 0) {
				if (node.attributes.url.indexOf('!druid.action') == -1) {//数据源监控页面不需要开启等待提示
					$.messager.progress({
						text : '页面加载中....',
						interval : 100
					});
					window.setTimeout(function() {
						try {
							$.messager.progress('close');
						} catch (e) {
						}
					}, 5000);
				}
				centerTabs.tabs('add', {
					title : node.text,
					closable : true,
					iconCls : node.iconCls,
					content : '<iframe src="' + node.attributes.url + '" frameborder="0" style="border:0;width:100%;height:99.4%;"></iframe>',
					tools : [ {
						iconCls : 'icon-mini-refresh',
						handler : function() {
							refreshTab(node.text);
						}
					} ]
				});
			} else {}*/
				centerTabs.tabs('add', {
					title : nodes[0],
					closable : true,
					//border:false,
					iconCls : nodes[1],
					//href : 'error/error.jsp',
					content : "<iframe src="+nodes[2]+" frameborder=\"0\" style=\"border:0;width:100%;height:99.4%;\"></iframe>",
					tools : [ {
						iconCls : 'icon-mini-refresh',
						handler : function() {
							refreshTab(nodes[0]);
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
		<iframe src="layout/portal.jsp" frameborder="0" style="border:0;width:100%;height:99.4%;"></iframe>
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
		    <div id="default" data-options="iconCls:'icon-save'">default</div>  
		    <div id="black">black</div> 
		    <div id="bootstrap" data-options="iconCls:'icon-save'">bootstrap</div>  
		    <div id="gray" data-options="iconCls:'icon-save'">gray</div>  
		    <div id="metro" data-options="iconCls:'icon-save'">metro</div>  
</div>