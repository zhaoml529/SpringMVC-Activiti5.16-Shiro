(function ($)
{
    //全局系统对象
    window['jqueryUtil'] = {};
	//修改ajax默认设置
		$.ajaxSetup({
			type : 'POST',
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				$.messager.progress('close');
				$.messager.alert('错误11111', XMLHttpRequest.responseText);
			}
		});
		
		var easyuiErrorFunction = function(XMLHttpRequest) {
			$.messager.progress('close');
			$.messager.alert('错误', XMLHttpRequest.responseText);
		};
		$.fn.datagrid.defaults.onLoadError = easyuiErrorFunction;
		$.fn.treegrid.defaults.onLoadError = easyuiErrorFunction;
		$.fn.tree.defaults.onLoadError = easyuiErrorFunction;
		$.fn.combogrid.defaults.onLoadError = easyuiErrorFunction;
		$.fn.combobox.defaults.onLoadError = easyuiErrorFunction;
		$.fn.form.defaults.onLoadError = easyuiErrorFunction;
		/**
		 * 取消easyui默认开启的parser
		 * 在页面加载之前，先开启一个进度条
		 * 然后在页面所有easyui组件渲染完毕后，关闭进度条
		 * @requires jQuery,EasyUI
		 */
		$.parser.auto = false;
		$(function() {
			$.messager.progress({
				text : '加载中....',
				interval : 100
			});
			$.parser.parse(window.document);
			window.setTimeout(function() {
				$.messager.progress('close');
				if (self != parent) {
					window.setTimeout(function() {
						try {
							parent.$.messager.progress('close');
						} catch (e) {
						}
					}, 500);
				}
			}, 1);
			$.parser.auto = true;
		});
		//IE检测
			jqueryUtil.isLessThanIe8 = function() {
				return ($.browser.msie && $.browser.version < 8);
			};
		/**
		 * 使panel和datagrid在加载时提示
		 * @requires jQuery,EasyUI
		 */
		$.fn.panel.defaults.loadingMessage = '加载中....';
		$.fn.datagrid.defaults.loadMsg = '加载中....';
		
		
		/**
		 * @requires jQuery,EasyUI
		 * 防止panel/window/dialog组件超出浏览器边界
		 * @param left
		 * @param top
		 */
		var easyuiPanelOnMove = function(left, top) {
			var l = left;
			var t = top;
			if (l < 1) {
				l = 1;
			}
			if (t < 1) {
				t = 1;
			}
			var width = parseInt($(this).parent().css('width')) + 14;
			var height = parseInt($(this).parent().css('height')) + 14;
			var right = l + width;
			var buttom = t + height;
			var browserWidth = $(window).width();
			var browserHeight = $(window).height();
			if (right > browserWidth) {
				l = browserWidth - width;
			}
			if (buttom > browserHeight) {
				t = browserHeight - height;
			}
			$(this).parent().css({/* 修正面板位置 */
				left : l,
				top : t
			});
		};
		$.fn.dialog.defaults.onMove = easyuiPanelOnMove;
		$.fn.window.defaults.onMove = easyuiPanelOnMove;
		$.fn.panel.defaults.onMove = easyuiPanelOnMove;
		
		/**
		 * @author 孙宇
		 * 
		 * @requires jQuery,EasyUI
		 * 
		 * 为datagrid、treegrid增加表头菜单，用于显示或隐藏列，注意：冻结列不在此菜单中
		 */
		var createGridHeaderContextMenu = function(e, field) {
			e.preventDefault();
			var grid = $(this);/* grid本身 */
			var headerContextMenu = this.headerContextMenu;/* grid上的列头菜单对象 */
			if (!headerContextMenu) {
				var tmenu = $('<div style="width:100px;"></div>').appendTo('body');
				var fields = grid.datagrid('getColumnFields');
				for ( var i = 0; i < fields.length; i++) {
					var fildOption = grid.datagrid('getColumnOption', fields[i]);
					if (!fildOption.hidden) {
						$('<div iconCls="icon-ok" field="' + fields[i] + '"/>').html(fildOption.title).appendTo(tmenu);
					} else {
						$('<div iconCls="icon-empty" field="' + fields[i] + '"/>').html(fildOption.title).appendTo(tmenu);
					}
				}
				headerContextMenu = this.headerContextMenu = tmenu.menu({
					onClick : function(item) {
						var field = $(item.target).attr('field');
						if (item.iconCls == 'icon-ok') {
							grid.datagrid('hideColumn', field);
							$(this).menu('setIcon', {
								target : item.target,
								iconCls : 'icon-empty'
							});
						} else {
							grid.datagrid('showColumn', field);
							$(this).menu('setIcon', {
								target : item.target,
								iconCls : 'icon-ok'
							});
						}
					}
				});
			}
			headerContextMenu.menu('show', {
				left : e.pageX,
				top : e.pageY
			});
		};
		$.fn.datagrid.defaults.onHeaderContextMenu = createGridHeaderContextMenu;
		$.fn.treegrid.defaults.onHeaderContextMenu = createGridHeaderContextMenu;
		
		
		/**
		 * @requires jQuery,EasyUI
		 * panel关闭时回收内存
		 */
		$.fn.panel.defaults.onBeforeDestroy = function() {
			var frame = $('iframe', this);
			try {
				if (frame.length > 0) {
					frame[0].contentWindow.document.write('');
					frame[0].contentWindow.close();
					frame.remove();
					if ($.browser.msie) {
						CollectGarbage();
					}
				}
			} catch (e) {
			}
		};
		/**
		 * @author 孙宇
		 * 
		 * @requires jQuery,EasyUI
		 * 
		 * 扩展treegrid，使其支持平滑数据格式
		 */
		$.fn.treegrid.defaults.loadFilter = function(data, parentId) {
			var opt = $(this).data().treegrid.options;
			var idFiled, textFiled, parentField;
			if (opt.parentField) {
				idFiled = opt.idFiled || 'id';
				textFiled = opt.textFiled || 'text';
				parentField = opt.parentField;
				var i, l, treeData = [], tmpMap = [];
				for (i = 0, l = data.length; i < l; i++) {
					tmpMap[data[i][idFiled]] = data[i];
				}
				for (i = 0, l = data.length; i < l; i++) {
					if (tmpMap[data[i][parentField]] && data[i][idFiled] != data[i][parentField]) {
						if (!tmpMap[data[i][parentField]]['children'])
							tmpMap[data[i][parentField]]['children'] = [];
						data[i]['text'] = data[i][textFiled];
						tmpMap[data[i][parentField]]['children'].push(data[i]);
					} else {
						data[i]['text'] = data[i][textFiled];
						treeData.push(data[i]);
					}
				}
				return treeData;
			}
			return data;
		};
		
		/**
		 * @author 夏悸
		 * 
		 * @requires jQuery,EasyUI
		 * 
		 * 扩展tree，使其支持平滑数据格式
		 */
		$.fn.tree.defaults.loadFilter = function(data, parent) {
			var opt = $(this).data().tree.options;
			var idFiled, textFiled, parentField;
			if (opt.parentField) {
				idFiled = opt.idFiled || 'id';
				textFiled = opt.textFiled || 'text';
				parentField = opt.parentField;
				var i, l, treeData = [], tmpMap = [];
				for (i = 0, l = data.length; i < l; i++) {
					tmpMap[data[i][idFiled]] = data[i];
				}
				for (i = 0, l = data.length; i < l; i++) {
					if (tmpMap[data[i][parentField]] && data[i][idFiled] != data[i][parentField]) {
						if (!tmpMap[data[i][parentField]]['children'])
							tmpMap[data[i][parentField]]['children'] = [];
						data[i]['text'] = data[i][textFiled];
						tmpMap[data[i][parentField]]['children'].push(data[i]);
					} else {
						data[i]['text'] = data[i][textFiled];
						treeData.push(data[i]);
					}
				}
				return treeData;
			}
			return data;
		};
		/**
		 * @author sy
		 * 
		 * @requires jQuery,EasyUI
		 * 
		 * 扩展combotree，使其支持平滑数据格式
		 */
		$.fn.combotree.defaults.loadFilter = function(data, parent) {
			var opt = $(this).data().tree.options;
			var idFiled, textFiled, parentField;
			if (opt.parentField) {
				idFiled = opt.idFiled || 'id';
				textFiled = opt.textFiled || 'text';
				parentField = opt.parentField;
				var i, l, treeData = [], tmpMap = [];
				for (i = 0, l = data.length; i < l; i++) {
					tmpMap[data[i][idFiled]] = data[i];
				}
				for (i = 0, l = data.length; i < l; i++) {
					if (tmpMap[data[i][parentField]] && data[i][idFiled] != data[i][parentField]) {
						if (!tmpMap[data[i][parentField]]['children'])
							tmpMap[data[i][parentField]]['children'] = [];
						data[i]['text'] = data[i][textFiled];
						tmpMap[data[i][parentField]]['children'].push(data[i]);
					} else {
						data[i]['text'] = data[i][textFiled];
						treeData.push(data[i]);
					}
				}
				return treeData;
			}
			return data;
		};
		//序列化表单到对象
		jqueryUtil.serializeObject = function(form) {
			//console.dir(form.serializeArray());
			var o = {};
			$.each(form.serializeArray(), function(index) {
				if (o[this['name']]) {
					o[this['name']] = o[this['name']] + "," + (this['value']==''?' ':this['value']);
				} else {
					o[this['name']] = this['value']==''?' ':this['value'];
				}
			});
			//console.dir(o);
			return o;
		};
		/*$.extend($.fn.datagrid.defaults.editors, {
	        combotree: {
	            init: function(container, options){
	                var editor = jQuery('<input type="text">').appendTo(container);
	                editor.combotree(options);
	                return editor;
	            },
	            destroy: function(target){
	                jQuery(target).combotree('destroy');
	            },
	            getValue: function(target){
	                var temp = jQuery(target).combotree('getValue');
	                var temp2 = jQuery(target).combotree('getText');
	                alert("getValue=>"+temp+"===>"+temp2);
	                return temp+","+temp2;
	            },
	            setValue: function(target, value){
	               //var temp = value.toString.split(",");
	                alert("setValue=11>"+value);
	               // jQuery(target).combotree('setValue', temp[0]);
	            },
	            resize: function(target, width){
	                jQuery(target).combotree('resize', width);
	            }
	        }
		});*/

		
		
		/**
		 * 扩展树表格级联选择（点击checkbox才生效）：
		 * 		自定义两个属性：
		 * 		cascadeCheck ：普通级联（不包括未加载的子节点）
		 * 		deepCascadeCheck ：深度级联（包括未加载的子节点）
		 */
		$.extend($.fn.treegrid.defaults,{
			onLoadSuccess : function() {
				var target = $(this);
				var opts = $.data(this, "treegrid").options;
				var panel = $(this).datagrid("getPanel");
				var gridBody = panel.find("div.datagrid-body");
				var idField = opts.idField;//这里的idField其实就是API里方法的id参数
				gridBody.find("div.datagrid-cell-check input[type=checkbox]").unbind(".treegrid").click(function(e){
					if(opts.singleSelect) return;//单选不管
					if(opts.cascadeCheck||opts.deepCascadeCheck){
						var id=$(this).parent().parent().parent().attr("node-id");
						var status = false;
						if($(this).attr("checked")){
							target.treegrid('select',id);
							status = true;
						}else{
							target.treegrid('unselect',id);
						}
						//级联选择父节点
						selectParent(target,id,idField,status);
						selectChildren(target,id,idField,opts.deepCascadeCheck,status);
					}
					e.stopPropagation();//停止事件传播
				});
			}
		});
		
		/**
		 * 扩展树表格级联勾选方法：
		 * @param {Object} container
		 * @param {Object} options
		 * @return {TypeName} 
		 */
		$.extend($.fn.treegrid.methods,{
			/**
			 * 级联选择
		     * @param {Object} target
		     * @param {Object} param 
			 *		param包括两个参数:
		     *			id:勾选的节点ID
		     *			deepCascade:是否深度级联
		     * @return {TypeName} 
			 */
			cascadeCheck : function(target,param){
				var opts = $.data(target[0], "treegrid").options;
				if(opts.singleSelect)
					return;
				var idField = opts.idField;//这里的idField其实就是API里方法的id参数
				var status = false;//用来标记当前节点的状态，true:勾选，false:未勾选
				var selectNodes = $(target).treegrid('getSelections');//获取当前选中项
				for(var i=0;i<selectNodes.length;i++){
					if(selectNodes[i][idField]==param.id)
						status = true;
				}
				//级联选择父节点
				selectParent(target,param.id,idField,status);
				selectChildren(target,param.id,idField,param.deepCascade,status);
			}
		});
		
		
		
		/**
		 * 级联选择父节点
		 * @param {Object} target
		 * @param {Object} id 节点ID
		 * @param {Object} status 节点状态，true:勾选，false:未勾选
		 * @return {TypeName} 
		 */
		function selectParent(target,id,idField,status){
			var parent = target.treegrid('getParent',id);
			if(parent){
				var parentId = parent[idField];
				if(status)
					target.treegrid('select',parentId);
				else
					target.treegrid('unselect',id);
				selectParent(target,parentId,idField,status);
			}
		}
		/**
		 * 级联选择子节点
		 * @param {Object} target
		 * @param {Object} id 节点ID
		 * @param {Object} deepCascade 是否深度级联
		 * @param {Object} status 节点状态，true:勾选，false:未勾选
		 * @return {TypeName} 
		 */
		function selectChildren(target,id,idField,deepCascade,status){
			//深度级联时先展开节点
			if(status&&deepCascade)
				target.treegrid('expand',id);
			//根据ID获取下层孩子节点
			var children = target.treegrid('getChildren',id);
			for(var i=0;i<children.length;i++){
				var childId = children[i][idField];
				if(status)
					target.treegrid('select',childId);
				else
					target.treegrid('unselect',childId);
				selectChildren(target,childId,idField,deepCascade,status);//递归选择子节点
			}
		}
	//cookies
	 jqueryUtil.cookies = (function ()
    {
        var fn = function ()
        {
        };
        fn.prototype.get = function (name)
        {
            var cookieValue = "";
            var search = name + "=";
            if (document.cookie.length > 0)
            {
                offset = document.cookie.indexOf(search);
                if (offset != -1)
                {
                    offset += search.length;
                    end = document.cookie.indexOf(";", offset);
                    if (end == -1) end = document.cookie.length;
                    cookieValue = decodeURIComponent(document.cookie.substring(offset, end))
                }
            }
            return cookieValue;
        };
        fn.prototype.set = function (cookieName, cookieValue, DayValue)
        {
            var expire = "";
            var day_value = 1;
            if (DayValue != null)
            {
                day_value = DayValue;
            }
            expire = new Date((new Date()).getTime() + day_value * 86400000);
            expire = "; expires=" + expire.toGMTString();
            document.cookie = cookieName + "=" + encodeURIComponent(cookieValue) + ";path=/" + expire;
        }
        fn.prototype.remvoe = function (cookieName)
        {
            var expire = "";
            expire = new Date((new Date()).getTime() - 1);
            expire = "; expires=" + expire.toGMTString();
            document.cookie = cookieName + "=" + escape("") + ";path=/" + expire;
            /*path=/*/
        };

        return new fn();
    })();
	//获取随机时间
	 jqueryUtil.getRandTime=function(){
		 	var nowDate=new Date();
		 	var str="";
			var hour=nowDate.getHours();//HH
			str+=((hour<10)?"0":"")+hour;
			var min=nowDate.getMinutes();//MM
			str+=((min<10)?"0":"")+min;
			var sec=nowDate.getSeconds(); //SS
			str+=((sec<10)?"0":"")+sec;
			return Number(str);
	 };
	//切换皮肤
	jqueryUtil.chgSkin=function(selectId,cookiesColor){
        	 docchgskin(document,selectId,cookiesColor);
            $("iframe").each(function (){   
                var dc=this.contentWindow.document;
                docchgskin(dc,selectId,cookiesColor);
            });
            function docchgskin(dc,selectId,cookiesColor){
						removejscssfile(dc,ctx+"/css/themes/"+cookiesColor+"/easyui.css", "css");
                        createLink(dc,ctx+"/css/themes/"+selectId+"/easyui.css");
        	}
            function createLink(dc,url){
		    	var urls = url.replace(/[,]\s*$/ig,"").split(",");
		    	var links = [];
		    	for( var i = 0; i < urls.length; i++ ){
			    links[i] = dc.createElement("link");
			    links[i].rel = "stylesheet";
			    links[i].href = urls[i];
			    dc.getElementsByTagName("head")[0].appendChild(links[i]);
		     	}
	    	}
            function removejscssfile(dc,filename, filetype){
	            var targetelement=(filetype=="js")? "script" : (filetype=="css")? "link" : "none"
	            var targetattr=(filetype=="js")? "src" : (filetype=="css")? "href" : "none"
	            var allsuspects=dc.getElementsByTagName(targetelement)
	            for (var i=allsuspects.length; i>=0; i--)
	            {
	                if (allsuspects[i] && allsuspects[i].getAttribute(targetattr)!=null && allsuspects[i].getAttribute(targetattr).indexOf(filename)!=-1)
	                allsuspects[i].parentNode.removeChild(allsuspects[i])
	            }
        	}  
        };
        /* 
    	 * 格式化字符串
    	 */
        $.formatString = function(str) {
        	for ( var i = 0; i < arguments.length - 1; i++) {
        		str = str.replace("{" + i + "}", arguments[i + 1]);
        	}
        	return str;
        };
        
      //高级查询
        jqueryUtil.gradeSearch=function($dg,formId,url) {
			$("<div/>").dialog({
				href : url,
				modal : true,
				title : '高级查询',
				top : 120,
				width : 480,
				buttons : [ {
					text : '增加一行',
					iconCls : 'icon-add',
					handler : function() {
						var currObj = $(this).closest('.panel').find('table');
						currObj.find('tr:last').clone().appendTo(currObj);
						currObj.find('tr:last a').show();
					}
				}, {
					text : '确定',
					iconCls : 'icon-ok',
					handler : function() {
						$dg.datagrid('reload',jqueryUtil.serializeObject($(formId)));
					}
				}, {
					text : '取消',
					iconCls : 'icon-cancel',
					handler : function() {
						$(this).closest('.window-body').dialog('destroy');
					}
				} ],
				onClose : function() {
					$(this).dialog('destroy');
				}
			});
		};
		
		/**
		 * @author 孙宇
		 * 
		 * @requires jQuery,EasyUI
		 * 
		 * 创建一个模式化的dialog
		 * 
		 * @returns $.modalDialog.handler 这个handler代表弹出的dialog句柄
		 * 
		 * @returns $.modalDialog.xxx 这个xxx是可以自己定义名称，主要用在弹窗关闭时，刷新某些对象的操作，可以将xxx这个对象预定义好
		 */
		$.modalDialog = function(options) {
			if ($.modalDialog.handler == undefined) {// 避免重复弹出
				var opts = $.extend({
					title : '',
					width : 840,
					height : 680,
					modal : true,
					onClose : function() {
						$.modalDialog.handler = undefined;
						$(this).dialog('destroy');
					}
					/*onOpen : function() {
						parent.$.messager.progress({
							title : '提示',
							text : '数据处理中，请稍后....'
						});
					}*/
				}, options);
				opts.modal = true;// 强制此dialog为模式化，无视传递过来的modal参数
				return $.modalDialog.handler = $('<div/>').dialog(opts);
			}
		};
		
	/* 
	 * 定义图标样式的数组
	 */
	$.iconData = [ {
		value : '',
		text : '默认'
	},{
		value : 'icon-adds',
		text : 'icon-adds'
	}
	,{
		value : 'icon-ok',
		text : 'icon-ok'
	},{
		value : 'icon-edit',
		text : 'icon-edit'
	} ,{
		value : 'icon-tip',
		text : 'icon-tip'
	},{
		value : 'icon-back',
		text : 'icon-back'
	},{
		value : 'icon-remove',
		text : 'icon-remove'
	},{
		value : 'icon-undo',
		text : 'icon-undo'
	},{
		value : 'icon-cancel',
		text : 'icon-cancel'
	}
	,{
		value : 'icon-save',
		text : 'icon-save'
	}
	,{
		value : 'icon-config',
		text : 'icon-config'
	},{
		value : 'icon-comp',
		text : 'icon-comp'
	},{
		value : 'icon-sys',
		text : 'icon-sys'
	},{
		value : 'icon-db',
		text : 'icon-db'
	},{
		value : 'icon-pro',
		text : 'icon-pro'
	},{
		value : 'icon-role',
		text : 'icon-role'
	},{
		value : 'icon-end',
		text : 'icon-end'
	},{
		value : 'icon-bug',
		text : 'icon-bug'
	},{
		value : 'icon-badd',
		text : 'icon-badd'
	},{
		value : 'icon-bedit',
		text : 'icon-bedit'
	},{
		value : 'icon-bdel',
		text : 'icon-bdel'
	},{
		value : 'icon-item',
		text : 'icon-item'
	},{
		value : 'icon-excel',
		text : 'icon-excel'
	},{
		value : 'icon-auto',
		text : 'icon-auto'
	},{
		value : 'icon-time',
		text : 'icon-time'
	}
	];
})(jQuery);