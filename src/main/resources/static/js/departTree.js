function setIcon() {
	    $(".layui-tree-iconClick").each(function () { //根据图标父元素data-key设置样式
	        var level = $(this).parents(".layui-tree-set").attr("level");
	        if (level==" 1") {//空格1，空格加层级。
	            $(this).css("height","25px");
	            $(this).parent().siblings(".layui-tree-btnGroup").hide();
	            $(this).children("i").removeClass();
	            $(this).children("i").addClass("icon1");
	        }
	        else {
	            if($(this).children("i").hasClass("layui-icon-group")) {
	                $(this).children("i").hide();
	                $(this).siblings(".layui-tree-txt").css("margin-left","-7px");
	            }
	            else{
	                $(this).siblings(".layui-tree-txt").css("margin-left","-7px");
	
	            }
	
	        }
	    });
	}