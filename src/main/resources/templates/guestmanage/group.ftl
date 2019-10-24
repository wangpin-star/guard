<!DOCTYPE html>
<html style="height: 100%">
<head>
    <meta charset="UTF-8">
    <title>创建编辑嘉宾分组</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <base href="/"/>
    <link rel="stylesheet" href="/layui2.5.4/css/layui.css" media="all">
    <style>
   		
   </style>
</head>

<body style="height: 100%">
<form class="layui-form" id="addGroup" style="height: 100%">
<div class="layui-container" style="width: 100%;">
  <div class="layui-row" style="width: 100%; height:46px;">
     <div style="float: left;width:55%;margin-top: 16px;">
         <div class='layui-form-item' style="margin-bottom: 14px;">
             <label class='layui-form-label' id="edit" style="line-height: 14px;font-size: 18px;width:115px;color: #333333;display: none">
                 <img imgName="editGroup" style="margin-bottom: 3px;" src="/css/img/editGroup1.png"></img>编辑分组:
             </label>
             <label class='layui-form-label' id="create" style="line-height: 14px;font-size:18px;width:115px;color: #333333;">
                 <img imgName="addGroup" style="margin-bottom: 3px;" src="/css/img/addGroup1.png">创建分组：
             </label>
         </div>
      	 <div class='layui-form-item' style="margin-bottom: 14px;">
             <label class='layui-form-label' style="line-height: 14px;padding-right: 11px;color: #333333;">分组名称</label>
                <img src="/css/img/required.png" style="margin-top: 11px;float: left;margin-right: 11px;">
                <div class='layui-input-inline' style='width: 320px;'>
                    <input type='text' id="groupName" name='groupName' placeholder="请输入组名..." lay-verify='title'
                        maxlength="20" style="height: 32px;line-height: 32px;border-radius: 4px;"class='layui-input'>
                </div>
         </div>
     </div>
  </div> 
  <div class="layui-row" style="width:100%; margin-top: 16px;">
      <div style="float: left;margin-left: 38px;">
      	<span style="font-size: 14px; color: #333333;">选择嘉宾</span>
      </div>
      <div style="float: left;margin-left:35px">
      	<div id="group" class="demo-transfer" style="width: 100%;"></div>
      	<button type="button" data-type="auto" style="float: right;margin-top: 16px;height:30px;text-align: center;line-height: 30px;" 
      	  class="layui-btn layui-btn-normal"  lay-submit='' lay-filter='addGroup'>保存</button>
      </div>
  </div>
</div>     
</form>
<script type="text/javascript" src="/js/jquery.min.js"></script>
<script src="/layui2.5.4/layui.js" charset="utf-8"></script>
<script type="text/javascript" src="/js/ajaxFilter.js"></script>
<script>
layui.use(['transfer','form', 'layer','util'], function(){
	  var $ = layui.$
	  ,transfer = layui.transfer
	  ,form = layui.form
	  ,layer = layui.layer
	  ,util = layui.util;


	  form.verify({
		    title: function(value){
		      if(value.length == 0){
		    	  return '分组名称不能为空';  
		      }else{
		    	  addSubmit(); 
		      }
		    }
	  });
	  
	  //显示穿梭框左侧数据
	  var guestlist = ${guestlist};
	  //显示穿梭框右侧数据
	  var groupFace = '${groupFace}';
	  var groupFacelist = groupFace.split(',');
	  //编辑时回填组名
	  var groupNamedit = '${groupNamedit}';
	  if(groupNamedit != null){
		  $('#groupName').val(groupNamedit);
	  }
	  //groupId不为0，则表示编辑分组
	  var groupId = ${groupId};
	  if(groupId!=0){
	      $("#edit").show();
	      $("#create").hide()
      }else {
          $("#edit").hide()
          $("#create").show();
      }

	  transfer.render({
		    elem: '#group'
		    ,title: ['未选择', '已选择']  //自定义标题
		    ,data: guestlist
		    ,width: 380 //定义宽度
		    ,height: 600 //定义高度
		    ,showSearch: true
		    ,id: 'getgroupValue'
		    ,value:groupFacelist
		  })
		
	  //$("#addSubmit").on('click', function () {
		 function addSubmit() {
		  //获取分组名        	
		   var groupName = $('#groupName').val();
        	//获取右侧已选择数据
        	var groupValues = transfer.getData('getgroupValue');
        	var emp = 'empoyee';
        	var vist = 'visitor';
        	
        	//组装上报的数据集合
        	var faceList=[];
        	
        	for (var i in groupValues) {
        	   var GroupFace = {};
        	   var attribute = groupValues[i].attribute;
        	   GroupFace.attribute = attribute;
        	   if(attribute==0){
        		   var faceid = groupValues[i].value.replace(emp, '');
        		   GroupFace.face_id = faceid;
        	   }else{
        		   var faceid = groupValues[i].value.replace(vist, '');
        		   GroupFace.face_id = faceid;
        	   }
        	   
        	   faceList.push(GroupFace);
        	}

        	var jsonData = JSON.stringify(faceList);
        	//编辑分组
        	if(groupId>0){
        		$.ajax({ 
                    url:"/guest/createGroup",
            		type:'post',
            		data:{groupName : groupName,groupId:groupId},
            		async:false,	
            		success: function(data) {
            			var group = data;
            			var jsonGroup = JSON.stringify(group);
            			if(group.uploadStatus == 0){
            				$.ajax({ 
            	                url:"/guest/groupFaceConfig",
            	        		type:'post',
            	        		dataType : 'json',
            	        		data:{jsonGroup:jsonGroup,jsonData:jsonData},
            	        		async:false,	
            	        		success: function(code) {
            	        			if(code==0){
                        				layer.alert("编辑分组成功",{icon: 1,title:"提示"},function(){
                                            var index = parent.layer.getFrameIndex(window.name);
                                            parent.layer.close(index);
                                            window.parent.location.reload();
                                        });
                        			}else{
                        				layer.alert('编辑分组失败', {
                         		    		  title:"提示",
                         		    		  icon: 2,
                         		    	});
                        			}
            	        		},
            	        		error: function() {
            	                    layer.alert('服务器错误,请联系管理员',{icon: 2,title:"提示"},function (index) {
            	                        layer.close(index);
            	                    });
            	                }
            	        	});
            			}else{
            				layer.alert('服务器错误,请联系管理员', {
           		    		  title:"提示",
           		    		  icon: 2,
           		    		});
            			}
            		},
            		error: function() {
                        layer.alert('服务器错误,请联系管理员',{icon: 2,title:"提示"},function (index) {
                            layer.close(index);
                        });
                    }
            	});
        	}else{//创建分组
        		$.ajax({ 
                    url:"/guest/createGroup",
            		type:'post',
            		data:{groupName : groupName},
            		async:false,	
            		success: function(data) {
            			var group = data;
            			var jsonGroup = JSON.stringify(group);
            			if(group.uploadStatus == 0){
            				$.ajax({ 
            	                url:"/guest/groupFaceConfig",
            	        		type:'post',
            	        		dataType : 'json',
            	        		data:{jsonGroup:jsonGroup,jsonData:jsonData},
            	        		async:false,	
            	        		success: function(code) {
            	        			if(code==0){
                        				layer.alert("创建分组成功",{icon: 1,title:"提示"},function(){
                                            var index = parent.layer.getFrameIndex(window.name);
                                            parent.layer.close(index);
                                            window.parent.location.reload();
                                        });
                        			}else{
                        				layer.alert('创建分组失败', {
                         		    		  title:"提示",
                         		    		  icon: 2,
                         		    	});
                        			}
            	        		},
            	        		error: function() {
            	                    layer.alert('服务器错误,请联系管理员',{icon: 2,title:"提示"},function (index) {
            	                        layer.close(index);
            	                    });
            	                }
            	        	});
            			}else if(group.uploadStatus == 2){
            				var append = 'append';
            				layer.confirm('当前分组名称已存在，是否添加嘉宾到该组？',{btn:['是','否'],icon:3,title:"提示"},function(){
                 	    	    //保存并退出
                            	$.ajax({ 
                                    url:"/guest/groupFaceConfig",
                            		type:'post',
                            		dataType : 'json',
                            		data:{jsonGroup:jsonGroup,jsonData:jsonData,append:append},
                            		async:false,	
                            		success: function(code) {
                            			if(code==0){
                            				layer.alert("添加分组成功",{icon: 1,title:"提示"},function(){
                                                var index = parent.layer.getFrameIndex(window.name);
                                                parent.layer.close(index);
                                                window.parent.location.reload();
                                            });
                            			}else{
                            				layer.alert('添加分组失败', {
                             		    		  title:"提示",
                             		    		  icon: 2,
                             		    	});
                            			}
                            		},
                            		error: function() {
                                        layer.alert('服务器错误,请联系管理员',{icon: 2,title:"提示"},function (index) {
                                            layer.close(index);
                                        });
                                    }
                            	});
                 	         },function(){
                 	            //不保存并退出
                 	            var index = parent.layer.getFrameIndex(window.name);
                   	            parent.layer.close(index);
                 	        });
            			}else{
            				layer.alert('服务器错误,请联系管理员', {
           		    		  title:"提示",
           		    		  icon: 2,
           		    		});
            			}
            		},
            		error: function() {
                        layer.alert('服务器错误,请联系管理员',{icon: 2,title:"提示"},function (index) {
                            layer.close(index);
                        });
                    }
            	});
        	}
        	  
       // }); 
}
        document.onkeydown = function (e) { // 回车提交表单
            var theEvent = window.event || e;
            var code = theEvent.keyCode || theEvent.which;
            if (code == 13) {
                doSearch();
            }
        };

    });
</script>
</body>
</html> 



