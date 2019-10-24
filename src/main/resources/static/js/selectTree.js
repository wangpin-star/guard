//id div 的id，isMultiple 是否多选，chkboxType 多选框类型{"Y": "ps", "N": "s"} 详细请看ztree官网
function initSelectTree(id, isMultiple, chkboxType,ids,dataArr,userDepart) {
    var setting = {
        view: {
            dblClickExpand: false,
            showLine: false
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        check: {
            enable: false,
            chkboxType: {"Y": "ps", "N": "p"}
        },
        callback: {
        	
        	onClick: onClick,
            onCheck: onCheck
            
        }

    };
    if (isMultiple) {
        setting.check.enable = isMultiple;
    }
    if (chkboxType !== undefined && chkboxType != null) {
        setting.check.chkboxType = chkboxType;
    }
    var html = '<div class = "layui-select-title" >' +
        '<input id="' + id + 'Show"' + 'type = "text" title=""  placeholder = "请选择" value = "" class = "layui-input" style="width:300px;height:30px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;" readonly>' +
        '<i class= "layui-edge" ></i>' +
        '</div>';
    $("#" + id).append(html);
    $("#" + id).parent().append('<div class="tree-content scrollbar">' +
        '<input type="hidden" id="' + id + 'Hide" ' +
        'name="' + $(".select-tree").attr("id") + '">' +
        '<ul id="' + id + 'Tree" class="ztree scrollbar" style="margin-top:0;"></ul>' +
        '</div>');
    $("#" + id).bind("click", function () {
    	if (event.stopPropagation) { 
    		// 针对 Mozilla 和 Opera 
    		event.stopPropagation(); 
    		} 
    		else if (window.event) { 
    		// 针对 IE 
    		window.event.cancelBubble = true; 
    		}
    	
    	if(isIE()){
    		window.event.returnValue = false;
    	}else{
    		event.preventDefault();//展开
    	}
        if ($(this).parent().find(".tree-content").css("display") !== "none") {
            hideMenu();
        } else {
            $(this).addClass("layui-form-selected");
            var Offset = $(this).offset();
            var width = $(this).width() - 2;
            $(this).parent().find(".tree-content").css({
                left: Offset.left + "px",
                top: Offset.top + $(this).height() + "px"
            }).slideDown("fast");
            $(this).parent().find(".tree-content").css({
                width: width
            });
           // $("body").bind("mousedown", onBodyDown);
        }
    });
    $.fn.zTree.init($("#" + id + "Tree"), setting, dataArr);
    var names="";
    var idss=""
    var zTreeobj = $.fn.zTree.getZTreeObj(id+"Tree");
    zTreeobj.expandAll(true);
    
   /* var disabledNode = zTreeobj.getNodeByParam("id", 0);  
    zTreeobj.setChkDisabled(disabledNode, true);*/
    
    var nodes = zTreeobj.getNodes();
   /* var names=$("#" + id + "Show").attr("value");
    var name=names.split(",");*/
    if(ids!=null){
    for(var j=0;j<ids.length;j++){
    	//console.log(ids[j]);
      /*  for (var i = 0, l = nodes[0].children.length; i < l; i++) {
        	
    		if(ids[j]==nodes[0].children[i].id){
            	zTreeobj.checkNode(nodes[0].children[i]);
            	names+=(nodes[0].children[i].name)+",";
            	idss+=(nodes[0].children[i].id)+",";
            }
    	} */
    	var node=zTreeobj.getNodeByParam('depart_id',ids[j]);
    			if(node){//判断有查询到节点
    			zTreeobj.checkNode(node,true);
        		names+=(node.name)+",";
            	idss+=(node.depart_id)+",";
    	}
    	var strn=names.substring(0,names.length-1);
    	var stri=idss.substring(0,idss.length-1);
    }
    $("#" + id + "Show").attr("value",strn);
    $("#" + id + "Show").attr("title",strn);
    $("#" + id + "Hide").attr("value",stri);
    }
    var node_id=[];
    var nodes = zTreeobj.transformToArray(zTreeobj.getNodes()); 
    if(nodes!=null&&userDepart!=null){
    	 for(var i=0;i<nodes.length;i++){ 
    		 node_id.push(nodes[i].depart_id);
         }
    	 var difference= node_id.filter(function(v){ return userDepart.indexOf(v) === -1 })
    	 for(var i=0;i<difference.length;i++){ 
    		 for(var j=0;j<nodes.length;j++){
    			 if(nodes[j].depart_id!=0&&nodes[j].depart_id==difference[i]){
    				 zTreeobj.setChkDisabled(nodes[j], true);
    			 }
    		 }
         }
    }
}
function isIE() { //ie?  
    if (!!window.ActiveXObject || "ActiveXObject" in window)  
        { return true; }  
    else  
        { return false; }  
}
function beforeClick(treeId, treeNode) {
    var check = (treeNode && !treeNode.isParent);
    if (!check) 
    alert("只能选择部门");
    return check;
}

function onClick(event, treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj(treeId);
    if (zTree.setting.check.enable == true) {
        zTree.checkNode(treeNode, !treeNode.checked, false)
        assignment(treeId, zTree.getCheckedNodes());
    } else {
        assignment(treeId, zTree.getSelectedNodes());
        hideMenu();
    }
}

function onCheck(event, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj(treeId);
    assignment(treeId, zTree.getCheckedNodes());
    
}

function hideMenu() {
    $(".select-tree").removeClass("layui-form-selected");
    $(".tree-content").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);
}

function assignment(treeId, nodes) {
	var names = "";
    var ids = "";
    for (var i = 0, l = nodes.length; i < l; i++) {
        if(nodes[i].level!=0){
        	names += nodes[i].name + ",";
            ids += nodes[i].depart_id + ",";
        }
    	
    }
    if (names.length > 0) {
        names = names.substring(0, names.length - 1);
        ids = ids.substring(0, ids.length - 1);
    }
    treeId = treeId.substring(0, treeId.length - 4);
    $("#" + treeId + "Show").attr("value", names);
    $("#" + treeId + "Show").attr("title", names);
    $("#" + treeId + "Hide").attr("value", ids);
    
}
function configDept(names){
	var flag=0;
	layui.use(['tree','table','layer'], function(){
		var $ = layui.$;
		var layer = layui.layer;
		
	});
	var treeId="demo";
	var ids= $("#" + treeId + "Hide").attr("value");
	var dept_name=$("#demoShow").attr("value");
	if(dept_name==""){
		layer.alert("请选择部门",{icon: 0,title:"提示"},function (index) {
            layer.close(index);
        });
	}
	else{
		$.ajax({ 
	        url:"/device/deptConfig",
			type:'get',
			data:{
				'ids':ids,
				'names':names
				  },
			async:false,	
			success: function(data) {
				if(data==0){
			        layer.alert("修改成功",{icon: 1,title:"提示"},function(){
			            window.parent.location.reload();//刷新父页面
			            //layer.closeAll(index);//关闭弹出层
			        });
				}
			},
			error: function() {
				layer.alert("修改出错",{icon:2,title:"提示"},function(){
		            window.parent.location.reload();//刷新父页面
		        });
	        }
		});
	}
	//return flag;
}

function onBodyDown(event) {
    if ($(event.target).parents(".tree-content").html() == null) {
        hideMenu();
    }
}


