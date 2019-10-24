<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>添加嘉宾</title>
    <base href="/"/>
    <!-- <link rel="stylesheet" href="/layui/css/layui.css">
    <link rel="stylesheet" href="/font-awesome/css/font-awesome.css"> -->
    <link rel="stylesheet" href="/layui/css/layui.css" media="all"/>
	<link rel="stylesheet" href="/layui/formSelects-v4.css" media="all"/>
	<link rel="stylesheet" href="/font-awesome/css/font-awesome.css"> 
	
    <style>
        .layui-upload {
            width: 100%;
            float: left;
            margin: 10px 0;
        }

        .layui-upload-list {
            margin: 0;
        }

        .layui-form-radio {
            margin-top: 3px;
        }

        .layui-form-radio:nth-of-type(1) {
            margin-right: 30px;
        }

        .layui-form-radio:nth-of-type(2) {
            margin-right: 0px;
            padding-right: 0px;
        }

        .layui-form-radio i {
            margin-right: 14px;
        }

        .button1 {
            width: 38px;
            height: 25px;
            line-height: 25px;
            color: #666666;
            font-size: 10px;
            border: 1px solid #d8d8d8;
            border-radius: 4px;
            background-color: white;
            cursor: pointer;
            margin-left: 0px;
        }

        .button1 :hover {
            border: #999999;
        }

        .button1 :active {
            border: #54b5ff;
        }

        .button2 {
            width: 110px;
            height: 34px;
            line-height: 34px;
            color: #ffffff;
            font-size: 16px;
            border: none;
            border-radius: 4px;
            background-color: #54b5ff;
            cursor: pointer;
        }

        .button2 :hover {
            background-color: #77c4ff;
        }

        .button2 :active {
            background-color: #4a96ec;
        }

        .layui-form-select input {
            height: 32px;
            line-height: 32px;
            border-radius: 4px;
        }

        .layui-table-main {
            width: 100%;
        }
        .layui-form-label {
        	float:left;
    		display:block;
    		padding:9px 15px;
    		width:80px;
    		font-weight:400;
    		line-height:20px;
    		text-align:right
        }
    </style>
    <style>
.layui-form-checkbox[lay-skin="primary"] i{
  width:18px;
  height:18px;
  
}
.layui-form-checkbox[lay-skin="primary"]{
  padding-left: 30px;
  
}
 .layui-form-select input {
            height: 32px;
            line-height: 32px;
            border-radius: 4px;
        }
/* .layui-input, .layui-textarea {
  padding-left:20px;
  border-radius:4px;
} */
.xm-form-checkbox > i {
  border:1px solid #54b5ff
}
.xm-select-parent .xm-select {
   min-height: 32px;
   padding: 0 0 0 0;
}
.xm-select-parent .xm-input {
  /* padding-left:20px; */
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 420px;
}
div[xm-select-skin] .xm-select-title div.xm-select-label > span {
    border: none;
}
.xm-select-parent .xm-select-title div.xm-select-label > span {
   background-color:white;
   color:#000;
}
/*  .layui-form-checked[lay-skin=primary] i {
      background:url(/css/img/addImg2.png) left center no-repeat;
}  */
</style>

</head>
<body style="margin: 0;">
<!--start:添加嘉宾-->
<form class='layui-form' id='addGuest'>
    <!--start:嘉宾基本信息-->
    <div style="width: 100%;float: left;height: 20px;margin-top:10px;">    	
        <div style="width: 3%;float: left;height: 100%;">
        </div>
        <div style="width: 97%;float: left;height: 100%;">
            <fieldset class="layui-elem-field layui-field-title" style="margin: 0;float: left;width: 100%;">
                <legend style="color: #999999;font-size: 14px;margin-left: 0;padding-left: 0;padding-right: 12px;">
                    	基本信息
                </legend>
            </fieldset>
        </div>
    </div>
    <div id="message" style="width: 100%;float: left;height: 28px;text-align: center;color: #F55366"></div>
    <div style="width: 100%;float: left;height: 286px;">
        <div style="width: 51px;height: 100%;float: left;text-align: center;"></div>
        <div style="height: 100%;float: left;">
        	<!-- 姓名 -->
            <div class='layui-form-item' style="margin-bottom: 14px;">
                <label class='layui-form-label' style="line-height: 14px;padding-right: 11px;color: #333333;">姓名</label>
                <img src="/css/img/required.png" style="margin-top: 11px;float: left;margin-right: 11px;">
                <div class='layui-input-inline' style='width: 420px;'>
                    <input type='text' id="guestName" name='guestName' placeholder="请输入姓名..." lay-verify='guestName'
                           maxlength="20" style="height: 32px;line-height: 32px;border-radius: 4px;"
                           class='layui-input'>
                </div>
            </div>
            <!-- 性别 & 民族 -->
            <div class='layui-form-item' style="margin-bottom: 14px;">
                <label class='layui-form-label' style="line-height: 14px;padding-right: 11px;color: #333333;">性别</label>
                <img src="/css/img/required.png" style="margin-top: 11px;float: left;margin-right: 11px;">
                <div class='layui-input-inline' style="width: 170px;">
                    <input lay-filter="sex" type='radio' style="height: 32px;line-height: 32px;" id="man" name='sex' value='1' title='男' checked/>
                    <input lay-filter="sex" type='radio' style="height: 32px;line-height: 32px;" id="woman" name='sex' value='2' title='女'/>
                </div>
                <label class='layui-form-label' style="line-height: 14px;padding-right: 22px;color: #333333;">民族</label>
                <div class='layui-input-inline' style='width: 123px;'>
                    <select lay-filter="nation" id='nation' name='nation' class='layui-input'
                            style="height: 32px;line-height: 32px;border-radius: 4px;" lay-verify="" lay-search>
                        <option value="请选择" selected>请选择</option>
                    </select>
                </div>
            </div>
            <#--此object为读卡器所需,页面中不显示-->
        <object classid="CLSID:5EB842AE-5C49-4FD8-8CE9-77D4AF9FD4FF" id="IdrControl1" width="0" height="0" style="display: none;">
        </object>
            <!-- 身份证号 -->
            <div class='layui-form-item' style="margin-bottom: 14px;">
                <label class='layui-form-label'
                       style="line-height: 14px;padding-right: 32px;color: #333333;">身份证号</label>
                <div class='layui-input-inline' style='width: 420px;'>
                    <input type='text' id="IDCardNum" checkFaceId="0" maxlength="18" lay-verify="IDCardNum" name='IDCardNum' placeholder="请输入身份证号码..." style="height: 32px;line-height: 32px;border-radius: 4px;" class='layui-input'>
                </div>
            </div>
            <input type='hidden' id="IDCardDate" name='IDCardDate' disabled
                           style="height: 32px;line-height: 32px;border-radius: 4px;" class='layui-input'>
            <!-- 手机号 -->
            <div class='layui-form-item' style="margin-bottom: 14px;">
                <label class='layui-form-label' style="line-height: 14px;padding-right: 11px;color: #333333;">手机号</label>
                <img src="/css/img/required.png" style="margin-top: 11px;float: left;margin-right: 11px;">
                <div class='layui-input-inline' style='width: 420px;'>
                    <input type='text' id="telNum" name='telNum' style="height: 32px;line-height: 32px;border-radius: 4px;" class='layui-input' lay-verify="telNum">
                </div>
            </div>
            <!-- 公司 -->
            <div class='layui-form-item' style="margin-bottom: 14px;">
                <label class='layui-form-label' style="line-height: 14px;padding-right: 11px;color: #333333;">公司</label>
                <img src="/css/img/required.png" style="margin-top: 11px;float: left;margin-right: 11px;">
                <div class='layui-input-inline' style='width: 420px;'>
                    <input type='text' id="company" name='company' maxlength="80" style="height: 32px;line-height: 32px;border-radius: 4px;" class='layui-input' lay-verify="company" value="default">
                </div>
            </div>
            <!-- 职位 -->
            <div class='layui-form-item' style="margin-bottom: 14px;">
                <label class='layui-form-label'
                       style="line-height: 14px;padding-right: 32px;color: #333333;">职位</label>
                <div class='layui-input-inline' style='width: 420px;'>
                    <input type='text' id="position" name='position' 
                           style="height: 32px;line-height: 32px;border-radius: 4px;" class='layui-input'>
                </div>
            </div>
            
            <!-- 地址 -->
            <div class='layui-form-item' style="margin-bottom: 0px;">
                <label class='layui-form-label' style="line-height: 14px;padding-right: 32px;color: #333333;">地址</label>
                <div class='layui-input-inline' style='width: 420px;'>
                    <input type='text' id="address" name='address' maxlength="80" placeholder="请输入地址..."
                           style="height: 32px;line-height: 32px;border-radius: 4px;" class='layui-input'>
                </div>
            </div>
        </div>
        <!-- 上传头像 -->
        <div style="height: 100%;width:168px;float: right;margin-right: 144px;margin-top:35px;">
            <div style="height: 168px;width: 100%;float: left;text-align: center;line-height: 168px;">
                <img class='layui-upload-img' id='photo4' picId="0"
                     style="border-radius: 50%;border: 2px solid #d8d8d8;" width='164' height='164'
                     src="/css/img/headPhotoDefault.png">
                 <!-- 身份证照片 -->
                <img id="hidePhoto4" style="display: none;">
                <img id="IDCardPhoto" style="display: none;" src="/css/img/IDCardPhotoDefault.png">
            </div>
            <div style="height: 20px;width: 100%;float: left;"></div>
            <div style="height: 30px;width: 100%;float: left;">
                <button type='button' class="button1 uploadBtn" id="uploadPhoto4" style="width: 80px;margin-left: 0px;">上传头像</button>
                <button type='button' class="button1" onclick="deletePhoto(4)" style="width: 80px;margin-left: 4px;">删除</button>
            </div>
        </div>
    </div>
    <!--end:嘉宾基本信息-->
    <!--start:比对照片-->
    <div style="width: 100%;float: left;height: 28px;"></div>
    <div style="width: 100%;float: left;height: 20px;">
        <div style="width: 3%;float: left;height: 100%;">
        </div>
        <div style="width: 97%;float: left;height: 100%;">
            <fieldset class="layui-elem-field layui-field-title" style="margin:0px 0px;float: left;width: 100%;">
                <legend style="color: #999999;font-size: 14px;margin-left: 0;padding-left: 0;padding-right: 12px;">
                    比对照片
                </legend>
            </fieldset>
        </div>
    </div>
    <!--id:message2,Flash提示语-->
    <div id="message2" style="width: 100%;float: left;height: 28px;text-align: center;color: #F55366"></div>
    <div style="width: 100%;float: left;height: 219px;">
        <div style="width: 175px;height: 100%;float: left;"></div>
        <div style="width: 123px;height: 100%;float: left;">
            <div style="width: 100%;height: 153px;line-height: 153px;text-align: center;">
                <img class='layui-upload-img' id="photo1" width='123' height='153' picId="0" option="1" pass="0"
                     style="border: 1px solid #d8d8d8;border-radius: 4px;" src="/css/img/comparePhotoDefault.png">
                <!--hidePhoto,实现照片比例压缩功能需要一个隐藏的图片作为中转-->
                <img id="hidePhoto1" style="display: none;">
            </div>
            <div style="width: 100%;height: 7px;"></div>
            <div style="width: 100%;height: 25px;">
                <button type='button' class="button1 uploadBtn" id="uploadPhoto1" style="margin-left:1px;">上传</button>
                <button type='button' class="button1 takePhoto" onclick="takePhoto2(1)" >拍照</button>
                <button type='button' class="button1" onclick="deletePhoto2(1)" >删除</button>
            </div>
            <div style="width: 100%;height: 5px;"></div>
            <div style="width: 100%;height: 25px;">
                <button type='button' class="button1" id="passCompare1" onclick="passCompare_(1)"
                        style="width: 123px;display: none;">手动通过
                </button>
            </div>
        </div>
        <div style="width: 158px;height: 100%;float: left;"></div>
        <div style="width: 123px;height: 100%;float: left;">
            <div style="width: 100%;height: 153px;line-height: 153px;text-align: center;">
                <img class='layui-upload-img' id="photo2" width='123' height='153' picId="0" option="1" pass="0"
                     style="border: 1px solid #d8d8d8;border-radius: 4px;" src="/css/img/comparePhotoDefault.png">
                <img id="hidePhoto2" style="display: none;">
            </div>
            <div style="width: 100%;height: 7px;"></div>
            <div style="width: 100%;height: 25px;">
                <button type='button' class="button1 uploadBtn" id="uploadPhoto2" style="margin-left:1px;">上传</button>
                <button type='button' class="button1 takePhoto" onclick="takePhoto2(2)" >拍照</button>
                <button type='button' class="button1" onclick="deletePhoto2(2)" >删除</button>
            </div>
            <div style="width: 100%;height: 5px;"></div>
            <div style="width: 100%;height: 25px;">
                <button type='button' class="button1" id="passCompare2" onclick="passCompare_(2)"
                        style="width: 123px;display: none;">手动通过
                </button>
            </div>
        </div>
        <div style="width: 158px;height: 100%;float: left;"></div>
        <div style="width: 123px;height: 100%;float: left;">
            <div style="width: 100%;height: 153px;line-height: 153px;text-align: center;">
                <img class='layui-upload-img' id="photo3" width='123' height='153' picId="0" option="1" pass="0"
                     style="border: 1px solid #d8d8d8;border-radius: 4px;" src="/css/img/comparePhotoDefault.png">
                <img id="hidePhoto3" style="display: none;">
            </div>
            <div style="width: 100%;height: 7px;"></div>
            <div style="width: 100%;height: 25px;">
                <button type='button' class="button1 uploadBtn" id="uploadPhoto3" style="margin-left:1px;" >上传</button>
                <button type='button' class="button1 takePhoto" onclick="takePhoto2(3)"  >拍照</button>
                <button type='button' class="button1" onclick="deletePhoto2(3)" >删除</button>
            </div>
            <div style="width: 100%;height: 5px;"></div>
            <div style="width: 100%;height: 25px;">
                <button type='button' class="button1" id="passCompare3" onclick="passCompare_(3)"
                        style="width: 123px;">手动通过
                </button>
            </div>
        </div>
    </div>
    <!--end:比对照片-->
    <!-- start:其它信息 -->
    <div style="width: 100%;float: left;height: 20px;">    	
        <div style="width: 3%;float: left;height: 100%;">
        </div>
        <div style="width: 97%;float: left;height: 100%;">
            <fieldset class="layui-elem-field layui-field-title" style="margin: 0;float: left;width: 100%;">
                <legend style="color: #999999;font-size: 14px;margin-left: 0;padding-left: 0;padding-right: 12px;">
                    	其它信息
                </legend>
            </fieldset>
        </div>
    </div>
    <div style="width: 100%;float: left;height: 88px;">
    	<div style="width: 51px;height: 100%;float: left;text-align: center;">
    	</div>
    	<div style="height: 100%;float: left;">
	    	<div class="layui-form-item" style="padding:0px 0px;margin:0px 0px;padding-bottom:9px">
	  			<label class="layui-form-label" style="line-height: 14px;padding-right: 32px;color: #333333;">选择会议</label>
	     		<div class="layui-input-inline" style="width: 420px;">
	         		<select id="meeting" xm-select="select1" xm-select-search="" xm-select-show-count="3" xm-select-search-type="dl"  >
	         			<option value="">请选择</option>
	            		<option value="102">apple</option>
	            		<option value="103">san</option> 
	            		<option value="104">peach</option>
	            		<option value="105">黄桃</option>
	            		
	         		</select>
	     		</div>
	  		</div>
	  		
	  		<div class="layui-form-item" style="padding:0px 0px;margin:0px 0px;padding-bottom:3px">
	  			<label class="layui-form-label" style="line-height: 14px;padding-right: 32px;color: #333333;">选择分组</label>
	     		<div class="layui-input-inline" style="width: 420px;">
	         		<select id="group" xm-select="select2" xm-select-search="" xm-select-show-count="3" xm-select-search-type="dl" style="height: 32px;line-height: 32px;border-radius: 4px;" >
	            		<option value="">请选择</option>
	            		<option value="2">apple</option>
	            		<option value="3">san</option> 
	            		<option value="4">peach</option>
	            		<option value="5">黄桃</option>
	         		</select>
	     		</div>
	  		</div>
  		</div>
    </div>
    <!--end: 其它信息 -->
    
    
    <!--start:操作按钮-->
    
    <div style="width: 100%;float: right;height: 34px;margin-right: 140px;margin-bottom:6px;">
        <button type='button' lay-submit='' lay-filter='addGuestFilterSave' class="button2" style="float: right;">
            	保存
        </button>
    </div>
    <!--end:操作按钮-->
</form>
<!--end:添加嘉宾-->

<!--id:ifWriteFlag,是否有操作的标识位,默认为0,当操作页面时会修改为1,未保存时退出会给出提示-->
<input id="ifWriteFlag" style="display: none;" value="0">
</body>
<script type="text/javascript" src="/js/jquery.min.js"></script>
<script type="text/javascript" src="/layui/layui.js"></script>
<!--ajax登录拦截须引入-->
<script type="text/javascript" src="/js/ajaxFilter.js"></script>

<script type="text/javascript" src="/layui/formSelects-v4.js"></script>
<script type="text/javascript" th:inline="javascript">
</script>
<script>
	var t1 = "";	//
    var photoMaxSize = "${life_photo_max_size}";//比对照片最大kb
    var IDCardPhotoData = "";//身份证照片元数据
    var editFaceId = "${faceId}".replace(',', '');//编辑员工的id,为0则是添加员工,超过千位会有千位分隔符",",因此使用replace去掉
    console.log(editFaceId);
    var face_info_input_mod = "${face_info_input_mod}";//身份证信息的录入方式,0为仅读身份证,1为手输或读身份证
    var editIDCardIfExit = false;//编辑员工时该员工是否有身份证号。默认为没有,在回显编辑员工信息的方法中若有身份证号的字段则会改为true。用于实现"编辑员工若有身份证号则不能删除"的功能
    var meetings = '${meetingList}';
    
    layui.use(['table', 'layer', 'form', 'upload'], function () {
        var $ = layui.$;
        var table = layui.table;
        var layer = layui.layer;
        var form = layui.form;
        var upload = layui.upload;
        var formSelects = layui.formSelects;	
        
        //下拉框选项
        var json = JSON.parse(meetings);	//json字符串转json对象
        var list = json.data;
        for(var i=0;i<list.length;i++){		//下拉框选项生成建议放在ajax的回调函数中
        	var option = "<option value='"+list[i].id+"'>"+list[i].name+"</option>"
        	$("#meeting").append(option);
        }
        $("#meeting").append("<option>ttttt</option>");
        formSelects.render();				//
        
        if (photoMaxSize == "") {//若后台返回比对照片最大kb为空,则默认给100.后台也做了非空判断,所以此处理论上是不会为空的
            photoMaxSize = "1000";
        }
        
        if(editFaceId!="0") {	//编辑员工时回显数据
        	var param = {
        		face_id: editFaceId,
                attribute: 0,
                depart_id: 0,
                status: 0
        	};
        	$.ajax({
        		url: "/visitorManage/selectByParam",
        		type: "post",
        		data: param,
        		async: true,
        		success: function(data) {
        			var jsonData = $.parseJSON(data);
        			if(jsonData.count == 1){	//正常情况下返回一条结果
        				var faceInfo = jsonData.data[0];
        				var guestName = faceInfo.name;
        				$("#guestName").attr("title",guestName);
        				$("#guestName").val(guestName);
        				//回显性别
        				if(faceInfo.sex == 1){
        					$("#man").attr("checked",true);
        					$("#woman").attr("checked",false);
        					
        					console.log(faceInfo.sex);
        				}  
        				if(faceInfo.sex == 2){
        					$("#man").attr("checked",false);
        					$("#woman").attr("checked",true); 
        					console.log(faceInfo.sex);
        				}
        				//回显手机号
                        $('#telNum').val(faceInfo.tel_no);
        				//设置身份证是否存在标识
        				if (faceInfo.idcard != undefined) {
                            $("#IDCardNum").val(faceInfo.idcard);
                            editIDCardIfExit = true;
                        }
        				//回显民族
        				if(faceInfo.nation != undefined) {
        					$("#nation").val(faceInfo.nation);
        				}
        				//回显公司
        				var company = faceInfo.company;
        				$("#company").val();
        				//回显地址
                        var address = faceInfo.addr;
                        $('#address').attr("title", address);
                        $('#address').val(address);
                      //回显身份证有效期
                        $("#IDCardDate").val(faceInfo.idcard_expire);
                        //回显身份证照片
                        if (faceInfo.photo_wlt != undefined) {
                            //因身份证照片的div框是默认不显示的,如果有身份证照片才显示
                            //$("#IDCardPhotoDiv").show();
                            $('#IDCardPhoto').attr("src", "data:image/jpeg;base64," + faceInfo.photo_wlt);
                        }
        				//回显照片
        				$.ajax({
        					url: "/employeeManage/selectFacePhoto",
        					type: "post",
        					data: param,
        					async: true,
        					success: function(data){
        						var jsonData = $.parseJSON(data);
        						if(jsonData.resultCode == "0"){ //查询照片成功
        							//获取比对照片的数组
                                    var comparePictureList = jsonData.comparePictureList;
                                  	//判断是否存在第一张比对照片
                                    if (comparePictureList[0] != undefined) {
                                        var result1 = "data:image/jpeg;base64," + comparePictureList[0].data;
                                        $("#hidePhoto1").attr('src', result1);
                                        var theImage1 = new Image();
                                        theImage1.src = $("#hidePhoto1").attr("src");
                                        theImage1.onload = function () {
                                            var width = "123";
                                            var height = "153";
                                            $("#photo1").attr('width', width);
                                            $("#photo1").attr('height', height);
                                            if (width / height > this.width / this.height) {
                                                var changeWidth = (this.width / this.height) * height;
                                                $("#photo1").attr('width', changeWidth);
                                            } else {
                                                var changeHeight = (width * this.height) / this.width;
                                                $("#photo1").attr('height', changeHeight);
                                            }
                                            $("#photo1").attr('src', result1);
                                        }
                                        $("#photo1").attr("picId", comparePictureList[0].pic_id);//照片id
                                        $("#photo1").attr('pass', "1");//编辑员工的比对照片默认人脸比对通过(0-不通过,1-通过)
                                        $("#photo1").attr('option', "0");//操作默认不变(0-不变 ,1-新增或修改, 2-删除)
                                    }
                                  	//判断是否存在第二张比对照片
                                    if (comparePictureList[1] != undefined) {
                                        var result2 = "data:image/jpeg;base64," + comparePictureList[1].data;
                                        $("#hidePhoto2").attr('src', result2);
                                        var theImage2 = new Image();
                                        theImage2.src = $("#hidePhoto2").attr("src");
                                        theImage2.onload = function () {
                                            var width = "123";
                                            var height = "153";
                                            $("#photo2").attr('width', "123");
                                            $("#photo2").attr('height', "153");
                                            if (width / height > this.width / this.height) {
                                                var changeWidth = (this.width / this.height) * height;
                                                $("#photo2").attr('width', changeWidth);
                                            } else {
                                                var changeHeight = (width * this.height) / this.width;
                                                $("#photo2").attr('height', changeHeight);
                                            }
                                            $("#photo2").attr('src', result2);
                                        }
                                        $("#photo2").attr("picId", comparePictureList[1].pic_id);
                                        $("#photo2").attr('pass', "1");
                                        $("#photo2").attr('option', "0");
                                    }
                                  	//判断是否存在第三张比对照片
                                    if (comparePictureList[2] != undefined) {
                                        var result3 = "data:image/jpeg;base64," + comparePictureList[2].data;
                                        $("#hidePhoto3").attr('src', result3);
                                        var theImage3 = new Image();
                                        theImage3.src = $("#hidePhoto3").attr("src");
                                        theImage3.onload = function () {
                                            var width = "123";
                                            var height = "153";
                                            $("#photo3").attr('width', "123");
                                            $("#photo3").attr('height', "153");
                                            if (width / height > this.width / this.height) {
                                                var changeWidth = (this.width / this.height) * height;
                                                $("#photo3").attr('width', changeWidth);
                                            } else {
                                                var changeHeight = (width * this.height) / this.width;
                                                $("#photo3").attr('height', changeHeight);
                                            }
                                            $("#photo3").attr('src', result3);
                                        }
                                        $("#photo3").attr("picId", comparePictureList[2].pic_id);
                                        $("#photo3").attr('pass', "1");
                                        $("#photo3").attr('option', "0");
                                    }
                                  	//获取头像照片
                                    var headPictureList = jsonData.headPictureList;
                                    if (headPictureList.length > 0) {//判断是否存在头像照片
                                        var result4 = "data:image/jpeg;base64," + headPictureList[0].data;
                                        $("#hidePhoto4").attr('src', result4);
                                        var theImage4 = new Image();
                                        theImage4.src = $("#hidePhoto4").attr("src");
                                        theImage4.onload = function () {
                                            var width = "164";
                                            var height = "164";
                                            $("#photo4").attr('width', "164");
                                            $("#photo4").attr('height', "164");
                                            if (width / height > this.width / this.height) {
                                                var changeWidth = (this.width / this.height) * height;
                                                $("#photo4").attr('width', changeWidth);
                                            } else {
                                                var changeHeight = (width * this.height) / this.width;
                                                $("#photo4").attr('height', changeHeight);
                                            }
                                            $("#photo4").attr('src', result4);
                                        }
                                        //头像不进行比对,所以没有pass
                                        //头像的新增和修改是通过face_info_base64字段,只有删除才使用Picture对象,所以没有option
                                        $("#photo4").attr("picId", headPictureList[0].pic_id);//图片id
                                    }
        						}else{
        							layer.alert('查询照片失败,请重试!', {icon: 2, title: "提示"}, function (index) {
                                        layer.close(index);
                                    });
        						}
        					}
        				}); 
        				//回显照片结束
        				form.render();
        			} else{
        				layer.alert("数据异常,请联系管理员检查数据库", {icon: 2, title: "提示"}, function (index) {
                            layer.close(index);
                        });
        			}
        		},
        		error:function () {
        			layer.alert('服务器错误,请联系管理员', {icon: 2, title: "提示"}, function (index) {
                        layer.close(index);
        			});
        		}
        		
        	});
        } //回显结束
        
        if (face_info_input_mod == "0") {//录入方式为"仅读身份证"时,禁用输入
            $("#guestName").addClass("layui-disabled").attr("disabled", true);
            $("#nation").addClass("layui-disabled").attr("disabled", true);
            $("#address").addClass("layui-disabled").attr("disabled", true);
            //$("#IDCardDate").addClass("layui-disabled").attr("disabled", true);
            $("#IDCardNum").addClass("layui-disabled").attr("disabled", true);
        }
        
        if (ifIE()) {//IE浏览器开启读证定时任务,1秒1次
            t1 = window.setInterval("readIDCard()", 1000);
        } else {//非IE浏览器给出提示
            $("#message").text("(提示:当前浏览器非IE,如需读身份证请使用IE浏览器并连接读卡器!)");
        }
        
        //表单数据验证
        form.verify({
            guestName: function (value) {
                if (value == "") {
                    return '请输入姓名';
                }
            },
            company: function (value) {
                if (value == "") {
                    return '请输入公司';
                }
            },
            IDCardNum: function (value) {
                if (value != "" && !checkIDCard(value)) {
                    return '请输入合法的身份证号码';
                } 
            },
            telNum: function (value) {
                var IDCardNum = $("#IDCardNum").val();
                if (IDCardNum == "" && value == "") {
                	
                    return '身份证号码与手机号码必须至少填写一项';
                } else if (value != "") {
                    reg = /0?(13|14|15|16|17|18|19)[0-9]{9}/;
                    if (!reg.test(value)) {
                        return '请输入合法的手机号码';
                    }
                }
            }
        });

        //监听单选和下拉框,如果有操作则更改操作标识
        form.on('radio(sex)', function () {
            $("#ifWriteFlag").val("1");
        });
        form.on('select(nation)', function () {
            $("#ifWriteFlag").val("1");
        });    
        //操作input时更改操作标识
        $("#addGuest :input").change(function () {
            $("#ifWriteFlag").val("1");
        });
        
		
        //点击保存按钮
        form.on('submit(addGuestFilterSave)', function (obj) {
            saveGuest();
        });


        //保存
        function saveGuest() {
        	//判断有无未通过比对的照片,若有则给出提示
            var ifPasscompare = false;
            if ($("#passCompare1").is(":hidden") && $("#passCompare2").is(":hidden") && $("#passCompare3").is(":hidden")) {
                ifPasscompare = true;
                console.log(ifPasscompare);
                console.log("select1:" + formSelects.value('select1').toString());
                console.log("all:" + formSelects.value('select1','all'));
                console.log("val:" + formSelects.value('select1','val'));
                console.log("valStr:" + formSelects.value('select1','valStr'));
                console.log("name:" + formSelects.value('select1','name'));
                console.log("nameStr:" + formSelects.value('select1','nameStr'));
                var list = formSelects.value('select2','all');
                console.log(list);
            }
            
            if (!ifPasscompare) {
                layer.alert('存在人证比对不通过的照片，如继续登记需更换照片或手动通过!', {icon: 0, title: "提示"}, function (index) {
                    layer.close(index);
                });
                return false;	//存在比对未通过并且未手动通过的照片则无法提交保存
            } else {
                var IDCardNum = $("#IDCardNum").val();
                if (IDCardNum == "") {//如果身份证号没填,则第二步的手机号必填
                    $("#telNumLabel").css("padding-right", "11px");
                    $("#telNumRequiredPhoto").show();
                } else {
                    $("#telNumLabel").css("padding-right", "32px");
                    $("#telNumRequiredPhoto").hide();
                }
                
                //关闭读证的定时任务
                clearInterval(t1);
            }
        	
            //开启加载转圈圈动画
            var loadingAddGuest = layer.load(1, { shade: [0.1, '#fff'] });
            //获取页面数据
            
            var guestName = $("input[name='guestName']").val();
            var sex = $("input[name='sex']:checked").val();
            var nation = $("#nation").val();
            var IDCardNum = $("input[name='IDCardNum']").val();
            var company = $("input[name='company']").val();
            var position = $("input[name='position']").val();
            var telNum = $("input[name='telNum']").val();
            var address = $("input[name='address']").val();
            //var IDCardDate = $("input[name='IDCardDate']").val();
            //var depart = $("#depart").val();
            //var employeeId = $("input[name='employeeId']").val();
            //var empcard = $("input[name='empcard']").val();
            var faceInfo = {};
            if (editFaceId != "0") {
                faceInfo.face_id = editFaceId;
            } else {
                faceInfo.face_id = "0";
            }
            /* if (IDCardPhoto != "") {
                faceInfo.photo_wlt = IDCardPhoto;
                faceInfo.type = 1;
            } */
            var pictureList = new Array();
            //获取三张比对照片
            for (var i = 1; i <= 3; i++) {
                var photoId = "photo" + i;
                var pass = $("#" + photoId).attr("pass");//是否通过比对
                var picId = $("#" + photoId).attr('picId');//照片id
                var src = $("#" + photoId).attr('src');//照片数据
                //照片为空并且ID为0,则不传入后台
                var flag1 = (src == "" || src == undefined || src == "/css/img/comparePhotoDefault.png") && picId == "0";
                //照片不为空且pass为0,则不传入后台
                var flag2 = (src !== "" && src !== undefined && src !== "/css/img/comparePhotoDefault.png" && pass == "0");
                if (flag1 || flag2) {
                    //不操作
                } else {
                    var option = $("#" + photoId).attr('option');
                    var picture = {};
                    if (option == "1") {//新增或修改
                        var faceWidth = $("#" + photoId).attr('faceWidth');
                        var faceHeight = $("#" + photoId).attr('faceHeight');
                        //比对照片的数据是调用人脸检测接口返回的,所以不用过滤格式
                        var faceData = $("#" + photoId).attr('faceData');
                        var feature = $("#" + photoId).attr('feature');
                        picture = {
                            pic_id: picId,
                            data: faceData,
                            width: faceWidth,
                            height: faceHeight,
                            feature: feature,
                            option: option
                        };
                    } else {//删除或不修改,不需要传入图片数据
                        picture = {
                            pic_id: picId,
                            option: option
                        };
                    }
                    pictureList.push(picture);
                }
            }
            //获取头像
            var photo4 = $("#photo4").attr('src');
            if (undefined !== photo4 && "" !== photo4 && "/css/img/headPhotoDefault.png" != photo4) {
                //头像没有进行人脸检测,需要过滤格式
                photo4 = photo4.replace('data:;base64,', '');
                photo4 = photo4.replace('data:image/jpeg;base64,', '');
                photo4 = photo4.replace('data:image/png;base64,', '');
                photo4 = photo4.replace('data:image/bmp;base64,', '');
                faceInfo.face_info_base64 = photo4;
            } else {//若不存在头像,则获取页面中头像的照片id,若有照片id,则是删除了头像
                var photo4Id = $("#photo4").attr('picId');
                if (photo4Id != '0') {
                    var picture = {
                        pic_id: photo4Id,
                        option: 2,//option为2是删除操作
                        image_type: 1//image_type为1是头像
                    };
                    pictureList.push(picture);
                }
            }
            
            if (nation != "请选择") {
                faceInfo.nation = nation;
            }
            faceInfo.name = guestName;
            faceInfo.sex = sex;
            faceInfo.idcard = IDCardNum;
            faceInfo.addr = address;
            faceInfo.idcard_expire = ""; //
            faceInfo.depart_id = "";	//
            faceInfo.tel_no = telNum;	
            faceInfo.empcard = "";		//
            faceInfo.employee_id = "";//
            faceInfo.position = position;
            faceInfo.nick_name = "";	//
            faceInfo.attribute = 0;		//暂时设置为0 便于测试
            faceInfo.status = 0;
            faceInfo.pictures = pictureList;
            $.ajax({//调用添加faceInfo接口
                url: "/employeeManage/addFaceInfo",
                type: "post",
                data: JSON.stringify(faceInfo),
                contentType: 'application/json',
                async: false,
                traditional: true,
                success: function (data) {
            		
                    var jsonData = $.parseJSON(data);
                    if (jsonData.resultCode == "0") {//添加faceInfo成功
                        if(editFaceId != "0") {
                        	layer.alert('编辑成功', {icon: 1, title: "提示"}, function () {
                                window.parent.location.reload();
                            });
                        } 
                        else {
                        	layer.alert('添加成功', {icon: 1, title: "提示"}, function () {
                                window.parent.location.reload();
                            });	
                        }
                    } 
                    else {
                        if (editFaceId != "0") {
                            layer.alert('编辑嘉宾基本信息失败,错误代码:' + jsonData.resultCode, {icon: 2, title: "提示"}, function () {
                                window.parent.location.reload();
                            });
                        } 
                        else {
                            layer.alert('添加嘉宾基本信息失败,错误代码:' + jsonData.resultCode, {icon: 2, title: "提示"}, function () {
                                window.parent.location.reload();
                            });
                        }
                    }
                },
                error: function () {
                    layer.alert('服务器错误,请联系管理员', {icon: 2, title: "提示"}, function (index) {
                        layer.close(index);
                    });
                }
            });
            //关闭转圈圈的加载框
            layer.close(loadingAddGuest);
        }

        //第一张比对照片
        var uploadListIns1 = upload.render({
            elem: '#uploadPhoto1'
            , auto: false
            , acceptMime: '.jpg,.png,.bmp' //限制选择本地文件的类型
            , size: photoMaxSize //最大图片kb
            , choose: function (obj) {
                obj.preview(function (index, file, result) {
                    //修改已经操作的标识位
                    $("#ifWriteFlag").val("1");
                    //清除缓存
                    uploadListIns1.config.elem.next()[0].value = '';
                    //检测人脸。参数依次是照片数据,比对照片的序号,图片的来源(上传)
                    checkFaceNum2(result, "1", "upload");
                });
            }
        });

        //第二张比对照片
        var uploadListIns2 = upload.render({
            elem: '#uploadPhoto2'
            , auto: false
            , acceptMime: '.jpg,.png,.bmp'
            , size: photoMaxSize
            , choose: function (obj) {
                obj.preview(function (index, file, result) {
                    $("#ifWriteFlag").val("1");
                    uploadListIns2.config.elem.next()[0].value = '';
                    checkFaceNum2(result, "2", "upload");
                });
            }
        });

        //第三张比对照片
        var uploadListIns3 = upload.render({
            elem: '#uploadPhoto3'
            , auto: false
            , acceptMime: '.jpg,.png,.bmp'
            , size: photoMaxSize
            , choose: function (obj) {
                obj.preview(function (index, file, result) {
                    $("#ifWriteFlag").val("1");
                    uploadListIns3.config.elem.next()[0].value = '';
                    checkFaceNum2(result, "3", "upload");
                });
            }
        });

        //头像
        var uploadListIns4 = upload.render({
            elem: '#uploadPhoto4'
            , auto: false
            , acceptMime: '.jpg,.png,.bmp'
            , size: 1024
            , choose: function (obj) {
                obj.preview(function (index, file, result) {
                    $("#ifWriteFlag").val("1");
                    uploadListIns4.config.elem.next()[0].value = '';
                    //根据图片原本宽高比压缩
                    $("#hidePhoto4").attr('src', result);
                    var theImage = new Image();
                    theImage.src = $("#hidePhoto4").attr("src");
                    theImage.onload = function () {
                        var width = "164";
                        var height = "164";
                        $("#photo4").attr('width', "164");
                        $("#photo4").attr('height', "164");
                        if (width / height > this.width / this.height) {
                            var changeWidth = (this.width / this.height) * height;
                            $("#photo4").attr('width', changeWidth);
                        } else {
                            var changeHeight = (width * this.height) / this.width;
                            $("#photo4").attr('height', changeHeight);
                        }
                        $("#photo4").attr('src', result);
                    };
                });
            }
        });
    });

   
	//end layui
   

    //身份证input框失去焦点事件
    $("#IDCardNum").blur(function () {
        var IDCardNum = $("#IDCardNum").val();
        if (IDCardNum != "" && !checkIDCard(IDCardNum)) {
            layer.msg('请输入合法的身份证号码', {icon: 0, anim: 6});
        } else if (IDCardNum.length == 18) {
            var param = {
                idcard: IDCardNum,
                attribute: 0,
                face_id: 0,
                depart_id: 0,
                status: 1
            };
            checkFaceInfo(param, "身份证号", function () {
            });
        }
    });

    //手机号input框失去焦点事件
    $("#telNum").blur(function () {
        var telNum = $("#telNum").val();
        reg = /0?(13|14|15|16|17|18|19)[0-9]{9}/;
        if (telNum != "" && !reg.test(telNum)) {
            layer.msg('请输入合法的手机号码', {icon: 0, anim: 6});
        } else if (telNum.length == 11) {
            var param = {
                tel_no: telNum,
                attribute: 0,
                face_id: 0,
                depart_id: 0,
                status: 1
            };
            checkFaceInfo(param, "手机号", function () {
            });
        }
    });
    
  //效验身份证号和手机号是否重复
    function checkFaceInfo(param, paramName, readCardCallBack) {
        //效验期间不许读证,清除定时任务
        clearInterval(t1);
        //嘉宾怎么检测
        return true;
    }
    
  //读证
    function readIDCard() {
        var result;
        var cardtype;
        try {
            var ax = new ActiveXObject("IDRCONTROL.IdrControlCtrl.1");
            var IdrControl1 = document.getElementById("IdrControl1");
            result = IdrControl1.RepeatRead(0);   //设置是否重复读卡  0-不重复  1-重复
            //  result=IdrControl1.setMute(1);   //设置是否静音读卡  0-不静音  1-静音
            //注意：第一个参数为对应的设备端口，USB型为1001，串口型为1至16
            result = IdrControl1.ReadCard("1001", "c:\\ocx\\test.jpg");
            cardtype = IdrControl1.DecideReadCardType();//判断卡类型 1-身份证 2-外国居留证
            if (result == 1) {//读卡器连接正常
                $("#message").text("(提示:读卡器连接正常,可刷身份证!)");
                //显示身份证照片的框
                //$("#IDCardPhotoDiv").show();
                if (cardtype == 1) {//成功读到信息
                    var IDCardNum = IdrControl1.GetCode();
                    $("input[name='IDCardNum']").val(IDCardNum);
                    $("input[name='guestName']").val(IdrControl1.GetName());
                    var sex = IdrControl1.GetSex();
                    if (sex == "男") {
                        $('#man').next().click();
                    }
                    if (sex == "女") {
                        $('#woman').next().click();
                    }
                    //民族需要加"族"这个字
                    $("#nation").val(IdrControl1.GetFolk() + "族");
                    $("input[name='address']").val(IdrControl1.GetAddress());
                    console.log(IdrControl1.GetAddress());
                    
                  //$("input[name='IDCardDate']").val(IdrControl1.GetValid());
                    $("#IDCardPhoto").attr('src', 'data:image/jpeg;base64,' + IdrControl1.GetJPGPhotobuf());
                    IDCardPhotoData = IdrControl1.GetWLTbuf();
                    //依次与三张比对照片进行比对
                    for (var i = 1; i <= 3; i++) {
                        var photoId = "photo" + i;
                        var photoSrc = $("#" + photoId).attr('src');
                        if (undefined !== photoSrc && "" !== photoSrc && "/css/img/comparePhotoDefault.png" !== photoSrc) {
                            photoSrc = photoSrc.replace('data:;base64,', '');
                            photoSrc = photoSrc.replace('data:image/jpeg;base64,', '');
                            photoSrc = photoSrc.replace('data:image/png;base64,', '');
                            photoSrc = photoSrc.replace('data:image/bmp;base64,', '');
                            comparePhoto2(IdrControl1.GetJPGPhotobuf(), photoSrc, i);
                        }
                    }
                    
                    
                    
                    //--------------------------------------------------------------
                    //效验身份证重复
                    var param = {
                        idcard: IDCardNum,
                        attribute: 0,
                        face_id: 0,
                        depart_id: 0,
                        status: 1
                    };
                    if(false)	//不校验身份信息，直接填入表单
                    checkFaceInfo(param, "身份证号", function () {
                        //效验通过后的回调,回显各种信息,checkFaceInfo的回调只有这里有,其他情况调用checkFaceInfo的回调都为空

                        $("input[name='IDCardNum']").val(IDCardNum);
                        $("input[name='guestName']").val(IdrControl1.GetName());
                        var sex = IdrControl1.GetSex();
                        if (sex == "男") {
                            $('#man').next().click();
                        }
                        if (sex == "女") {
                            $('#woman').next().click();
                        }
                        //民族需要加"族"这个字
                        $("#nation").val(IdrControl1.GetFolk() + "族");
                        $("input[name='address']").val(IdrControl1.GetAddress());
                        console.log(IdrControl1.GetAddress());
                        //$("input[name='IDCardDate']").val(IdrControl1.GetValid());
                        $("#IDCardPhoto").attr('src', 'data:image/jpeg;base64,' + IdrControl1.GetJPGPhotobuf());
                        IDCardPhotoData = IdrControl1.GetWLTbuf();
                        //依次与三张比对照片进行比对
                        for (var i = 1; i <= 3; i++) {
                            var photoId = "photo" + i;
                            var photoSrc = $("#" + photoId).attr('src');
                            if (undefined !== photoSrc && "" !== photoSrc && "/css/img/comparePhotoDefault.png" !== photoSrc) {
                                photoSrc = photoSrc.replace('data:;base64,', '');
                                photoSrc = photoSrc.replace('data:image/jpeg;base64,', '');
                                photoSrc = photoSrc.replace('data:image/png;base64,', '');
                                photoSrc = photoSrc.replace('data:image/bmp;base64,', '');
                                comparePhoto2(IdrControl1.GetJPGPhotobuf(), photoSrc, i);
                            }
                        }
                        layui.use(['form'], function () {
                            var form = layui.form;
                            form.render();
                        });
                    });
                    //------------------------------------------------------------------
                    layui.use(['form'], function () {	//#
                        var form = layui.form;
                        form.render();
                    });
                } else {
                    $("#message").text("(提示:当前卡片不是身份证,请将身份证放到读卡器上!)");
                }
            } else {
                if (result == -1) {
                    $("#message").text("(提示:未检测到身份证读卡器设备,如需使用读身份证功能请将读卡器连接至PC!)");
                }
                if (result == -2) {
                    $("#message").text("(提示:读卡器连接正常,可刷身份证!)");
                    //$("#IDCardPhotoDiv").show();
                }
                if (result == -3) {
                    $("#message").text("(提示:读取数据失败!)");
                }
                if (result == -4) {
                    $("#message").text("(提示:生成照片文件失败，请检查设定路径和磁盘空间!)");
                }
            }
        } catch (e) {
            if (e.message.indexOf("GetWLT") == -1) {
                var downloadOCXHTML = "提示:身份证读卡控件未安装!<a style='color: #54b5ff;text-decoration:underline' href='/employeeManage/downloadPlugin?pluginName=ocx'>点此下载</a>";
                $("#message").html(downloadOCXHTML);
                window.clearInterval(t1);
            }
        }
    }
    
  	


    //56个民族下拉框
    var nations = ["汉族", "蒙古族", "回族", "藏族", "维吾尔族", "苗族", "彝族", "壮族", "布依族", "朝鲜族", "满族", "侗族", "瑶族", "白族",
        "土家族", "哈尼族", "哈萨克族", "傣族", "黎族", "傈僳族", "佤族", "畲族", "高山族", "拉祜族", "水族", "东乡族", "纳西族", "景颇族", "柯尔克孜族", "土族",
        "达斡尔族", "仫佬族", "羌族", "布朗族", "撒拉族", "毛南族", "仡佬族", "锡伯族", "阿昌族", "普米族", "塔吉克族", "怒族", "乌孜别克族", "俄罗斯族", "鄂温克族",
        "德昂族", "保安族", "裕固族", "京族", "塔塔尔族", "独龙族", "鄂伦春族", "赫哲族", "门巴族", "珞巴族", "基诺族"];
    var option = "";
    for (var i = 0; i <
    nations.length; i++) {
        option += '<option value="' + nations[i] + '">' + nations[i] + '</option>';
    }
    $("#nation").append(option);
  //检测人脸信息
    function checkFaceNum2(result,photoNo,photoFrom) {
        //过滤base64格式
        var imgBase64 = result.replace('data:image/jpeg;base64,','');
        imgBase64 = imgBase64.replace('data:;base64,','');
        imgBase64 = imgBase64.replace('data:image/png;base64,','');
        imgBase64 = imgBase64.replace('data:image/bmp;base64,','');
        var param = {
            imgBase64:imgBase64
        };
        $.ajax({
            url: "/employeeManage/checkFace",
            type: "post",
            data: param,
            async: false,
            success: function(data) {
                var jsonData = $.parseJSON(data);
                if (jsonData.resultCode > 0) {
                    var FDResult = jsonData.resultData[0];
                    if (FDResult.face_width < 200) {//判断检测的宽度是否符合要求
                        //根据照片来源给出不同的提示,分为拍照和上传
                        if (photoFrom == "takePhoto") {
                            layer.alert('照片中可识别人脸区域过小,请您离摄像头更近一点重新拍照!',{icon:2,title:"提示"},function (index) {
                                layer.close(index);
                            });
                        }
                        if (photoFrom == "upload") {
                            layer.alert('照片中可识别人脸区域过小,请重新上传人脸区域更大的照片!',{icon:2,title:"提示"},function (index) {
                                layer.close(index);
                            });
                        }
                    } else {//图片宽度符合要求
                        //进行图片宽高比压缩
                        var photoId = "photo" + photoNo;
                        var hidePhotoId = "hidePhoto" + photoNo;
                        $("#" + hidePhotoId).attr('src', result);
                        var theImage = new Image();
                        theImage.src = $("#" + hidePhotoId).attr("src");
                        theImage.onload = function() {
                            var width = "123";
                            var height = "153";
                            $("#" + photoId).attr('width', "123");
                            $("#" + photoId).attr('height', "153");
                            if (width / height > this.width / this.height) {
                                var changeWidth = (this.width / this.height) * height;
                                $("#" + photoId).attr('width', changeWidth);
                            } else {
                                var changeHeight = (width * this.height) / this.width;
                                $("#" + photoId).attr('height', changeHeight);
                            }
                            $("#" + photoId).attr('src', result);
                        };
                        //把数据存入标签中
                        $("#" + photoId).attr('faceWidth', FDResult.face_width);
                        $("#" + photoId).attr('faceHeight', FDResult.face_height);
                        $("#" + photoId).attr('faceData', FDResult.face_data);
                        $("#" + photoId).attr('feature', FDResult.feature);
                        $("#" + photoId).attr('option', '1');
                        //获取身份证照片进行比对
                        var IDCardPhoto = $("#IDCardPhoto").attr("src");
                        if (IDCardPhoto !== "/css/img/IDCardPhotoDefault.png") {
                            IDCardPhoto = IDCardPhoto.replace('data:;base64,','');
                            IDCardPhoto = IDCardPhoto.replace('data:image/jpeg;base64,','');
                            IDCardPhoto = IDCardPhoto.replace('data:image/png;base64,','');
                            IDCardPhoto = IDCardPhoto.replace('data:image/bmp;base64,','');
                            comparePhoto2(IDCardPhoto,imgBase64,photoNo);
                        } else {
                        	//没有身份证
                            var photoId = "photo" + photoNo;
                            $("#" + photoId).attr("pass","1");
                        }
                    }
                } else {
                    layer.alert('照片不符合要求,未检测到人脸',{icon:2,title:"提示"},function (index) {
                        layer.close(index);
                    });
                }
            },
            error: function () {
                layer.alert('服务器开小差了',{icon:2,title:"提示"},function (index) {
                    layer.close(index);
                });
            }
        });
    }
  	//删除照片
    function deletePhoto2(photoNo) {
        var photoId = "photo" + photoNo;
        if (photoNo != "4") {//照片id不为4就是比对照片
            //把压缩后的宽高复原
            $("#" + photoId).attr('width',123);
            $("#" + photoId).attr('height',153);
            //把显示内容改为默认图片
            $("#" + photoId).attr('src','/css/img/comparePhotoDefault.png');
            //隐藏"手动通过"按钮
            var passCompareId = "passCompare" + photoNo;
            $("#" + passCompareId).hide();
            //把操作改为删除,option为2是删除
            $("#" + photoId).attr('option','2');
            $("#ifWriteFlag").val("1");
        } else {//照片id为4就是头像
            //获取头像的照片ID和第一张比对照片的ID,如果相同,则删除头像的话也会删除第一张比对照片,要给出提示
            var headPhotoPicId = $("#" + photoId).attr('picId');
            var firstPhotoPicId = $("#photo1").attr('picId');
            if (headPhotoPicId == firstPhotoPicId) {
                layer.confirm("如删除此头像,则会把第一张比对照片一并删除,确认删除吗?", {btn:['确认','取消'],icon: 3, title:'提示'},
                    function(index) {//确定回调
                        $("#" + photoId).attr('width',164);
                        $("#" + photoId).attr('height',164);
                        $("#" + photoId).attr('src','/css/img/headPhotoDefault.png');
                        $("#" + photoId).attr('option','2');
                        $("#ifWriteFlag").val("1");
                        layer.close(index);
                    },function (index) {//取消回调
                        layer.close(index);
                    }
                );
            } else {
                $("#" + photoId).attr('width',164);
                $("#" + photoId).attr('height',164);
                $("#" + photoId).attr('src','/css/img/headPhotoDefault.png');
                $("#" + photoId).attr('option','2');
                $("#ifWriteFlag").val("1");
            }
        }
    }
  	//把比对照片和身份证照片进行比对
    function comparePhoto2(img1,img2,photoNo) {//photoNo为比对照片的序号
        var param = {
            img1:img1,
            img2:img2
        };
        $.ajax({
            url: "/employeeManage/comparePhoto",
            type: "post",
            data: param,
            async: false,
            success: function(data) {
                var compareId = "passCompare" + photoNo;//手动通过按钮的id
                var photoId = "photo" + photoNo;
                if (data < 0.5) {
                    layer.alert('人证比对不通过,如需完成登记可手动通过',{icon:2,title:"提示"},function (index) {
                        layer.close(index);
                    });
                    //把pass设置为0,显示手动通过按钮
                    $("#" + photoId).attr("pass","0");
                    $("#" + compareId).show();
                    $("#" + photoId).attr("style","border: 2px solid #f55366;border-radius: 4px;");
                } else {
                    //把pass设置为1,隐藏手动通过按钮
                    $("#" + photoId).attr("pass","1");
                    $("#" + compareId).hide();
                    
                }
            }
        });
    }
  	//手动通过
    function passCompare_(photoNo) {
        layer.alert("已手动通过",{icon:1,title:"提示"},function (index) {
            layer.close(index);
        });
        var photoId = "photo" + photoNo;
        //把pass改为1
        $("#" + photoId).attr('pass','1');
        $("#" + photoId).attr('style','border: 1px solid #d8d8d8;border-radius: 4px;');
        var passCompareId = "passCompare" + photoNo;
        //隐藏手动通过按钮
        $("#" + passCompareId).hide();
    }
  //拍照
    function takePhoto2(photoNo) {
        //打开拍照的页面
        layer.open({
            type: 2,
            title: '拍照',
            shadeClose: false,
            shade:[0.2, '#000000'],
            area: ['800px', '400px'],
            btn: ['确定'],
            content: '/employeeManage/toTakePhoto?browserType=' + browserType,
            yes: function(index, layero){
                $("#ifWriteFlag").val("1");
                var body = layer.getChildFrame('body', index);//获取拍照页面的HTML的body
                var result = body.find("#base64image").attr('src');//获取拍照的图片数据
                layer.close(index);
                //调用检测人脸,参数依次为照片数据、比对照片的序号、照片来源(拍照或上传)
                checkFaceNum2(result,photoNo,"takePhoto");
            }
        });
    }
</script>
<script type="text/javascript" src="/js/photoJS.js"></script>
</html>