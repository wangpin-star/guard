//检测浏览器类型及插件是否安装
var browserType = BrowserType();
layui.use(['layer'], function() {
    var layer = layui.layer;
    if (ifIE()) {
        var fls = flashChecker();
        if (!fls.h) {
            $(".takePhoto").addClass("layui-disabled").attr("disabled","true");
            var message2HTML = "提示:检测到您的浏览器未安装Flash插件,如不安装则无法使用拍照功能!<a style='color: #54b5ff;text-decoration:underline' href='/employeeManage/downloadPlugin?pluginName=flash'>点此下载</a>";
            $("#message2").append(message2HTML);
        }
        //获取IE版本,如果是IE10以下，则隐藏上传按钮
        var IEVersion = browserType.replace("IE","");
        if (IEVersion < 10) {
            $(".uploadBtn").hide();
        }
    }
    //这里延迟0.5秒是因为需要等页面加载完之后再给提示框,不然提示框会在最上面
    setTimeout('checkReadCardMsg()',500);
});

//检测读卡
function checkReadCardMsg() {
    if (!checkReadCard()) {
        layer.confirm('检测到您未安装读卡器控件，如不安装则无法使用读身份证功能，是否下载并安装？',{btn:['是','否'],icon:3,title:"提示"},function (index) {
            document.location.href = '/employeeManage/downloadPlugin?pluginName=ocx';
            layer.close(index);
        });
    }
}

//检测人脸信息
function checkFaceNum(result,photoNo,photoFrom) {
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
                        var width = "142";
                        var height = "176";
                        $("#" + photoId).attr('width', "142");
                        $("#" + photoId).attr('height', "176");
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
                    $("#" + photoId).attr('feature_ver', FDResult.feature_ver);
                    $("#" + photoId).attr('option', '1');
                    //获取身份证照片进行比对
                    var IDCardPhoto = $("#IDCardPhoto").attr("src");
                    if (IDCardPhoto !== "/css/img/IDCardPhotoDefault.png") {
                        IDCardPhoto = IDCardPhoto.replace('data:;base64,','');
                        IDCardPhoto = IDCardPhoto.replace('data:image/jpeg;base64,','');
                        IDCardPhoto = IDCardPhoto.replace('data:image/png;base64,','');
                        IDCardPhoto = IDCardPhoto.replace('data:image/bmp;base64,','');
                        comparePhoto(IDCardPhoto,imgBase64,photoNo);
                    } else {
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

//把比对照片和身份证照片进行比对
function comparePhoto(img1,img2,photoNo) {//photoNo为比对照片的序号
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
            } else {
                //把pass设置为1,隐藏手动通过按钮
                $("#" + photoId).attr("pass","1");
                $("#" + compareId).hide();
            }
        }
    });
}

//手动通过
function passCompare(photoNo) {
    layer.alert("已手动通过",{icon:1,title:"提示"},function (index) {
        layer.close(index);
    });
    var photoId = "photo" + photoNo;
    //把pass改为1
    $("#" + photoId).attr('pass','1');
    var passCompareId = "passCompare" + photoNo;
    //隐藏手动通过按钮
    $("#" + passCompareId).hide();
}

//删除照片
function deletePhoto(photoNo) {
    var photoId = "photo" + photoNo;
    if (photoNo != "4") {//照片id不为4就是比对照片
        //把压缩后的宽高复原
        $("#" + photoId).attr('width',142);
        $("#" + photoId).attr('height',176);
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

//拍照
function takePhoto(photoNo) {
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
            checkFaceNum(result,photoNo,"takePhoto");
        }
    });
}

//效验身份证号合法性
function checkIDCard(idcode) {
    // 加权因子
    var weight_factor = [7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2];
    // 校验码
    var check_code = ['1', '0', 'X' , '9', '8', '7', '6', '5', '4', '3', '2'];
    var code = idcode + "";
    var last = idcode[17];//最后一个
    var seventeen = code.substring(0,17);
    // ISO 7064:1983.MOD 11-2
    // 判断最后一位校验码是否正确
    var arr = seventeen.split("");
    var len = arr.length;
    var num = 0;
    for(var i = 0; i < len; i++){
        num = num + arr[i] * weight_factor[i];
    }
    // 获取余数
    var resisue = num%11;
    var last_no = check_code[resisue];
    // 格式的正则
    // 正则思路
    /*
    第一位不可能是0
    第二位到第六位可以是0-9
    第七位到第十位是年份，所以七八位为19或者20
    十一位和十二位是月份，这两位是01-12之间的数值
    十三位和十四位是日期，是从01-31之间的数值
    十五，十六，十七都是数字0-9
    十八位可能是数字0-9，也可能是X
    */
    var idcard_patter = /^[1-9][0-9]{5}([1][9][0-9]{2}|[2][0][0|1][0-9])([0][1-9]|[1][0|1|2])([0][1-9]|[1|2][0-9]|[3][0|1])[0-9]{3}([0-9]|[X])$/;
    // 判断格式是否正确
    var format = idcard_patter.test(idcode);
    // 返回验证结果，校验码和格式同时正确才算是合法的身份证号码
    return last === last_no && format ? true : false;
}

//只判断是否是IE
function ifIE() {
    var ua = navigator.userAgent.toLowerCase();
    if (ua.indexOf('trident') > -1) {
        return true;
    } else {
        return false;
    }
}

//判断浏览器类型
function BrowserType() {
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
    var isOpera = userAgent.indexOf("Opera") > -1; //判断是否Opera浏览器
    // var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera; //判断是否IE浏览器
    var isIE=window.ActiveXObject || "ActiveXObject" in window
    // var isEdge = userAgent.indexOf("Windows NT 6.1; Trident/7.0;") > -1 && !isIE; //判断是否IE的Edge浏览器
    var isEdge = userAgent.indexOf("Edge") > -1; //判断是否IE的Edge浏览器
    var isFF = userAgent.indexOf("Firefox") > -1; //判断是否Firefox浏览器
    var isSafari = userAgent.indexOf("Safari") > -1 && userAgent.indexOf("Chrome") == -1; //判断是否Safari浏览器
    var isChrome = userAgent.indexOf("Chrome") > -1 && userAgent.indexOf("Safari") > -1&&!isEdge; //判断Chrome浏览器

    if (isIE)
    {
        var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
        reIE.test(userAgent);
        var fIEVersion = parseFloat(RegExp["$1"]);
        if(userAgent.indexOf('MSIE 6.0')!=-1){
            return "IE6";
        }else if(fIEVersion == 7)
        { return "IE7";}
        else if(fIEVersion == 8)
        { return "IE8";}
        else if(fIEVersion == 9)
        { return "IE9";}
        else if(fIEVersion == 10)
        { return "IE10";}
        else if(userAgent.toLowerCase().match(/rv:([\d.]+)\) like gecko/)){
            return "IE11";
        }
        else
        { return "IE0"}//IE版本过低
    }

    if (isFF) { return "Firefox";}
    if (isOpera) { return "Opera";}
    if (isSafari) { return "Safari";}
    if (isChrome) { return "Chrome";}
    if (isEdge) { return "Edge";}
}

//检测读证的环境,true/false
function checkReadCard() {
    if (ifIE()) {
        try {
            var ax = new ActiveXObject("IDRCONTROL.IdrControlCtrl.1");
            return true;
        } catch (e) {
            return false;
        }
    } else {
        return true;
    }
}

//检测flash
function flashChecker() {
    //是否安装了flash
    var hasFlash = 0;
    //flash版本
    var flashVersion = 0;
    //是否IE浏览器
    if (ifIE()) {
        try {
            var swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
        } catch (e) {}
        if (swf) {
            hasFlash = 1;
            VSwf = swf.GetVariable("$version");
            flashVersion = parseInt(VSwf.split(" ")[1].split(",")[0]);
        }
    } else {
        if (navigator.plugins && navigator.plugins.length > 0) {
            try {
                var swf = navigator.plugins["Shockwave Flash"];
            } catch (e) {}
            if (swf) {
                hasFlash = 1;
                var words = swf.description.split(" ");
                for (var i = 0; i < words.length; ++i) {
                    if (isNaN(parseInt(words[i])))
                        continue;
                    flashVersion = parseInt(words[i]);
                }
            }
        }
    }
    return {
        h : hasFlash,
        v : flashVersion
    };
}