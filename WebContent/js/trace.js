function graphTrace(id) {
    // 获取图片资源
    var imageUrl = ctx + "/processAction/process/process-instance?pid=" + id + "&type=png";
    
	$.ajax({
        type: "POST",
        url: ctx+"/processAction/process/trace/" + id,
        data: {},
        success: function (data) {
        	$.messager.progress("close");
        	
            var positionHtml = "";
            // 生成图片
            var varsArray = new Array();
            $.each(data, function(i, v) {
                var $positionDiv = $('<div/>', {
                    'class': 'activity-attr'
                }).css({
                    position: 'absolute',
                    left: (v.x + 4),
                    top: (v.y + 26),
                    width: (v.width - 2),
                    height: (v.height - 2),
                    backgroundColor: 'black',
                    opacity: 0,
                    zIndex: $.fn.qtip.zindex - 1
                });

                // 跟踪节点边框
                var $border = $('<div/>', {
                    'class': 'activity-attr-border'
                }).css({
                    position: 'absolute',
                    left: (v.x + 4),
                    top: (v.y + 26),
                    width: (v.width - 2),
                    height: (v.height - 2),
                    zIndex: $.fn.qtip.zindex - 2
                });

                if (v.currentActiviti) {
                    $border.addClass('ui-corner-all-12').css({
                        border: '3px solid red'
                    });
                }
                positionHtml += $positionDiv.outerHTML() + $border.outerHTML();
                varsArray[varsArray.length] = v.vars;
            });
            
            if ($('#workflowTraceDialog').length == 0) {
                $('<div/>', {
                    id: 'workflowTraceDialog',
//                    title: '查看流程（按ESC键可以关闭）',
                    html: "<div class='easyui-layout'><img src='" + imageUrl + "'style='left:0px; top:0px;' />" +
                    "<div id='processImageBorder'>" +
                    positionHtml +
                    "</div>" +
                    "</div>"
                }).appendTo('body');
            } else {
                $('#workflowTraceDialog img').attr('src', imageUrl);
                $('#workflowTraceDialog #processImageBorder').html(positionHtml);
            }

            // 设置每个节点的data
            $('#workflowTraceDialog .activity-attr').each(function(i, v) {
                $(this).data('vars', varsArray[i]);
            });
        	
	         // 这里创建一个图像保存到内存，并没有添加到 HTML 中，只是个参考
	         $("<img/>").attr("src", imageUrl).load(function() {
		            //弹出对话窗口
		           var workflowTraceDialog = $('#workflowTraceDialog').dialog({
		            	title : "查看流程",
		        		top: 20,
		        		width : this.width+30,
		        		height : this.height+90,
		                modal: true,
		                minimizable: true,
		                maximizable: true,
		                resizable: true,
		                onLoad: function () {
		                	
		                },
		                buttons: [
		                    {
		                        text: '关闭',
		                        iconCls: 'icon-cancel',
		                        handler: function () {
		                        	workflowTraceDialog.dialog('destroy');
		                        }
		                    }
		                ],
		                onClose: function () {
		                	workflowTraceDialog.dialog('destroy');
		                }
		            });
		        // 此处用于显示每个节点的信息，如果不需要可以删除
                   $('.activity-attr').qtip({
                       content: function() {
                           var vars = $(this).data('vars');
                           var tipContent = "<table class='easyui-datagrid'><thead>";
                           $.each(vars, function(varKey, varValue) {
                               if (varValue) {
                                   tipContent += "<tr><td class='title1'>" + varKey + "</td><td class='left'>" + varValue + "<td/></tr>";
                               }
                           });
                           tipContent += "</thead></table>";
                           return tipContent;
                       },
                       position: {
                           at: 'bottom left',
                           adjust: {
                               x: 60
                           }
                       }
                   });
	         });
            
        },
        beforeSend:function(){
        	$.messager.progress({
                title: '提示信息！',
                text: '数据处理中，请稍后....'
            });
    	},
    	complete: function(){
    		$.messager.progress("close");
   		}
	});
    

}
