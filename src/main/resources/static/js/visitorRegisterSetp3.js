var t1 = "";//读卡方法的定时任务
var IDCardPhotoData = "";//身份证照片元数据
var brushCardAutoSave = false;//刷证是否自动保存
var autoSaveFlag = 0;//首次开启自动保存的标识位,开启一次后会设置成1
var readIDCardComparePhotoFlag = true;//读身份证之后依次与三张比对照片检测相似度,是否通过检测,若有一张不通过就改为false
//右侧已登记的访客无数据时会显示此HTML
var noDataMsgHTML = "<tr class=\"noDataMsg\">\n" +
    "                        <td colspan=\"4\" style=\"color: #999999;text-align: center;\">显示已成功登记的访客记录</td>\n" +
    "                    </tr>";
var editIDCardIfExit = false;//编辑的访客身份证号是否存在
var carList = [];

//页面加载时添加56个民族
$(function () {
    var nations = ["汉族", "蒙古族", "回族", "藏族", "维吾尔族", "苗族", "彝族", "壮族", "布依族", "朝鲜族", "满族", "侗族", "瑶族", "白族",
        "土家族", "哈尼族", "哈萨克族", "傣族", "黎族", "傈僳族", "佤族", "畲族", "高山族", "拉祜族", "水族", "东乡族", "纳西族", "景颇族", "柯尔克孜族", "土族",
        "达斡尔族", "仫佬族", "羌族", "布朗族", "撒拉族", "毛南族", "仡佬族", "锡伯族", "阿昌族", "普米族", "塔吉克族", "怒族", "乌孜别克族", "俄罗斯族", "鄂温克族",
        "德昂族", "保安族", "裕固族", "京族", "塔塔尔族", "独龙族", "鄂伦春族", "赫哲族", "门巴族", "珞巴族", "基诺族"];
    var option = "";
    for (var i = 0; i < nations.length; i++) {
        option += '<option value="' + nations[i] + '">' + nations[i] + '</option>';
    }
    $("#nation").append(option);
});

layui.use(['table', 'layer', 'form', 'upload'], function () {
    var layer = layui.layer;
    var form = layui.form;
    var upload = layui.upload;
    if (onlyStep3) {//只有第三步的情况(多人来访管理继续登记访客或编辑批量访客)
        //隐藏第三步中的"上一步"按钮
        $("#backVisitorRegisterTwoBtn").hide();
        //显示第三步
        $("#visitorRegisterThree").show();
        //隐藏导航条
        $("#nav").hide();
        if (ifIE()) {//IE浏览器开启读证监听
            t1 = window.setInterval("readIDCard()", 1000);
        } else {
            $("#message").text("(提示:当前浏览器非IE,如需读身份证请使用IE浏览器并连接读卡器!)");
        }
    }

    if (editFaceId != '0') {//编辑访客
        $(".progressWidth").css("width", '196px');
        var param = {
            face_id: editFaceId,
            attribute: editAttribute,
            depart_id: 0,
            status: 0
        };
        $.ajax({
            url: "/visitorManage/selectByParam",
            type: "post",
            data: param,
            async: false,
            success: function (data) {
                var jsonData = $.parseJSON(data);
                if (jsonData.count == 1) {
                    var faceInfo = jsonData.data[0];
                    $("#visitorName").val(faceInfo.name);
                    if (faceInfo.sex == 1) {
                        $('#man').attr("checked", true);
                    }
                    if (faceInfo.sex == 2) {
                        $('#woman').attr("checked", true);
                    }
                    if (faceInfo.idcard != undefined) {
                        $("#IDCardNum").val(faceInfo.idcard);
                        editIDCardIfExit = true;
                    }
                    if (faceInfo.nation != undefined) {
                        $('#nation').val(faceInfo.nation);
                    }
                    $('#address').val(faceInfo.addr);
                    $("#IDCardDate").val(faceInfo.idcard_expire);
                    if (faceInfo.photo_wlt != undefined) {
                        $("#IDCardPhotoDiv").show();
                        $('#IDCardPhoto').attr("src", "data:image/jpeg;base64," + faceInfo.photo_wlt);
                    }
                    $('#visitorTelNum').val(faceInfo.tel_no);
                    $('#visitorNickName').val(faceInfo.nick_name);
                    $('#visitorPosition').val(faceInfo.position);
                    $('#visitorCompanyName').val(faceInfo.company);
                    $.ajax({
                        url: "/employeeManage/selectFacePhoto",
                        type: "post",
                        data: param,
                        async: false,
                        success: function (data) {
                            var jsonData = $.parseJSON(data);
                            if (jsonData.resultCode == "0") {
                                var comparePictureList = jsonData.comparePictureList;
                                if (comparePictureList[0] != undefined) {
                                    var result1 = "data:image/jpeg;base64," + comparePictureList[0].data;
                                    $("#hidePhoto1").attr('src', result1);
                                    var theImage1 = new Image();
                                    theImage1.src = $("#hidePhoto1").attr("src");
                                    theImage1.onload = function () {
                                        var width = "142";
                                        var height = "176";
                                        $("#photo1").attr('width', "142");
                                        $("#photo1").attr('height', "176");
                                        if (width / height > this.width / this.height) {
                                            var changeWidth = (this.width / this.height) * height;
                                            $("#photo1").attr('width', changeWidth);
                                        } else {
                                            var changeHeight = (width * this.height) / this.width;
                                            $("#photo1").attr('height', changeHeight);
                                        }
                                        $("#photo1").attr('src', result1);
                                    }
                                    $("#photo1").attr("picId", comparePictureList[0].pic_id);
                                    $("#photo1").attr('pass', "1");
                                    $("#photo1").attr('option', "0");
                                }
                                if (comparePictureList[1] != undefined) {
                                    var result2 = "data:image/jpeg;base64," + comparePictureList[1].data;
                                    $("#hidePhoto2").attr('src', result2);
                                    var theImage2 = new Image();
                                    theImage2.src = $("#hidePhoto2").attr("src");
                                    theImage2.onload = function () {
                                        var width = "142";
                                        var height = "176";
                                        $("#photo2").attr('width', "142");
                                        $("#photo2").attr('height', "176");
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
                                if (comparePictureList[2] != undefined) {
                                    var result3 = "data:image/jpeg;base64," + comparePictureList[2].data;
                                    $("#hidePhoto3").attr('src', result3);
                                    var theImage3 = new Image();
                                    theImage3.src = $("#hidePhoto3").attr("src");
                                    theImage3.onload = function () {
                                        var width = "142";
                                        var height = "176";
                                        $("#photo3").attr('width', "142");
                                        $("#photo3").attr('height', "176");
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
                                var headPictureList = jsonData.headPictureList;
                                if (headPictureList.length > 0) {
                                    var result4 = "data:image/jpeg;base64," + headPictureList[0].data;
                                    $("#hidePhoto4").attr('src', result4);
                                    var theImage4 = new Image();
                                    theImage4.src = $("#hidePhoto4").attr("src");
                                    theImage4.onload = function () {
                                        var width = "138";
                                        var height = "138";
                                        $("#photo4").attr('width', "138");
                                        $("#photo4").attr('height', "138");
                                        if (width / height > this.width / this.height) {
                                            var changeWidth = (this.width / this.height) * height;
                                            $("#photo4").attr('width', changeWidth);
                                        } else {
                                            var changeHeight = (width * this.height) / this.width;
                                            $("#photo4").attr('height', changeHeight);
                                        }
                                        $("#photo4").attr('src', result4);
                                    }
                                    $("#photo4").attr("picId", headPictureList[0].pic_id);
                                }
                            } else {
                                layer.alert('服务器开小差了,查询照片错误,请重试!', {icon: 2, title: "提示"}, function (index) {
                                    layer.close(index)
                                });
                            }
                        }
                    });
                } else {
                    layer.alert("数据异常,请联系系统管理员", {icon: 2, title: "提示"}, function (index) {
                        layer.close(index);
                    });
                }
            }
        });
        $("#registerDiv").css("width", "100%");
        $("#hasRegistered").hide();
        $("#continueRegister").hide();
        $("#saveRegister").show();
    }
    form.render();

    var uploadListIns1 = upload.render({
        elem: '#uploadPhoto1'
        , auto: false
        , acceptMime: '.jpg,.png,.bmp'
        , size: 1024
        , choose: function (obj) {
            obj.preview(function (index, file, result) {
                $("#ifWriteFlag").val("1");
                uploadListIns1.config.elem.next()[0].value = '';
                checkFaceNum(result, "1", "upload");
            });
        }
    });

    var uploadListIns2 = upload.render({
        elem: '#uploadPhoto2'
        , auto: false
        , acceptMime: '.jpg,.png,.bmp'
        , size: 1024
        , choose: function (obj) {
            obj.preview(function (index, file, result) {
                $("#ifWriteFlag").val("1");
                uploadListIns2.config.elem.next()[0].value = '';
                checkFaceNum(result, "2", "upload");
            });
        }
    });

    var uploadListIns3 = upload.render({
        elem: '#uploadPhoto3'
        , auto: false
        , acceptMime: '.jpg,.png,.bmp'
        , size: 1024
        , choose: function (obj) {
            obj.preview(function (index, file, result) {
                $("#ifWriteFlag").val("1");
                uploadListIns3.config.elem.next()[0].value = '';
                checkFaceNum(result, "3", "upload");
            });
        }
    });

    var uploadListIns4 = upload.render({
        elem: '#uploadPhoto4'
        , auto: false
        , acceptMime: '.jpg,.png,.bmp'
        , size: 1024
        , choose: function (obj) {
            obj.preview(function (index, file, result) {
                $("#ifWriteFlag").val("1");
                uploadListIns4.config.elem.next()[0].value = '';
                $("#hidePhoto4").attr('src', result);
                var theImage = new Image();
                theImage.src = $("#hidePhoto4").attr("src");
                theImage.onload = function () {
                    var width = "138";
                    var height = "138";
                    $("#photo4").attr('width', "138");
                    $("#photo4").attr('height', "138");
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

    //监听第三步的"刷证自动保存"按钮开关
    form.on('switch(brushCardAutoSave)', function (data) {
        if (this.checked) {//开启状态
            //修改身份证号的验证规则为必填
            $("#IDCardNum").attr("lay-verify", "telNumRequired");
            //调整样式
            $("#IDCardNumLabel").css("padding-right", "11px");
            //显示身份证号必填标识
            $("#IDCardNumRequiredImg").show();
            //修改访客手机号的验证规则为非必填
            $("#visitorTelNum").attr("lay-verify", "telNum");
            //隐藏访客手机号的必填标识
            $("#telNumRequiredImg").hide();
            //调整样式
            $("#visitorTelNumLabel").css("padding-right", "32px");
            //把刷证自动保存的全局变量改为true,之后需要使用
            brushCardAutoSave = true;
        } else {//关闭
            $("#IDCardNum").attr("lay-verify", "telNum");
            $("#IDCardNumRequiredImg").hide();
            $("#IDCardNumLabel").css("padding-right", "32px");
            $("#visitorTelNum").attr("lay-verify", "telNumRequired");
            brushCardAutoSave = false;
            $("#visitorTelNumLabel").css("padding-right", "11px");
            $("#telNumRequiredImg").show();
        }
    });

    form.on('radio(sex)', function () {
        $("#ifWriteFlag").val("1");
    });
    form.on('select(nation)', function () {
        $("#ifWriteFlag").val("1");
    });

    //第二步点击"保存"按钮事件(非批量的员工点击编辑时只有前两步)
    form.on('submit(step2Save)', function (obj) {
        employeeSaveStep2();
    });

    //保存并退出事件
    form.on('submit(visitorRegisterThreeFilter)', function (obj) {
        submitVisitorRegister("2");
    });

    //保存并继续事件
    form.on('submit(continueRegister)', function (obj) {
        submitVisitorRegister("1");
    });

    /**
     * 保存
     * @param ifContinue 保存后是否继续,1:继续,2:不继续
     */
    function submitVisitorRegister(ifContinue) {
        //效验三张照片都是否比对通过
        var ifPasscompare = false;
        //这里是通过判断"手动通过"按钮有无隐藏,三个手动通过按钮都隐藏时说明都通过了
        if ($("#passCompare1").is(":hidden") && $("#passCompare2").is(":hidden") && $("#passCompare3").is(":hidden")) {
            ifPasscompare = true;
        }
        if (!ifPasscompare) {
            layer.alert('存在人证比对不通过的照片，如继续登记需更换照片或手动通过!', {icon: 0, title: "提示"}, function (index) {
                layer.close(index);
            });
        } else {//效验照片成功,执行保存业务
            //获取来访开始时间和结束时间
            var timeResult = getTime();
            if (onlyStep3 && editFaceId != "0") {//visitorEdit2,此处为编辑属于批量的某一访客信息,只需调用addFaceInfo即可
                addFaceInfo(timeResult.startTime, timeResult.endTime, function (addFaceInfoResult) {
                    if (addFaceInfoResult.resultCode == "0") {
                        layer.alert('访客信息修改成功', {icon: 1, title: "提示"}, function () {
                            window.parent.location.reload();
                        });
                    } else {
                        layer.alert('登记访客基本信息失败,错误代码:' + jsonData.resultCode, {icon: 2, title: "提示"}, function () {
                            window.parent.location.reload();
                        });
                    }
                });
            } else {//先添加人的基本信息,再添加访客记录,再配置设备权限
                addFaceInfo(timeResult.startTime, timeResult.endTime, function (addFaceInfoResult) {
                    if (addFaceInfoResult.resultCode == "0") {
                        var termIdList = getTermIdList();
                        var faceId = addFaceInfoResult.resultData.face_id;
                        addFaceVisitInfo(faceId, 1, termIdList, timeResult.startTime, timeResult.endTime, function (addFaceVisitInfoResult, ifExist) {
                            if (addFaceVisitInfoResult.resultCode == "0") {
                                var recId = addFaceVisitInfoResult.resultData.rec_id;
                                facePermitConfig(addFaceInfoResult.resultData, termIdList, recId, function (facePermitConfigResult) {
                                    if (facePermitConfigResult.resultCode != "0") {
                                        layer.alert("配置权限失败,请重试", {icon: 2, title: "提示"}, function (index) {
                                            layer.close(index);
                                        });
                                    }
                                });
                                if (ifContinue == "1") {//保存并继续
                                    continueRegister(ifExist, timeResult.startTime, timeResult.endTime, faceId, recId, "1");
                                }
                                if (ifContinue == "2") {//保存并退出
                                    if (editFaceId == '0') {
                                        layer.alert('访客登记成功', {icon: 1, title: "提示"}, function () {
                                            window.parent.location.reload();
                                        });
                                    } else {
                                        layer.alert('访客信息修改成功', {icon: 1, title: "提示"}, function () {
                                        	
                                            window.parent.location.reload();
                                        });
                                    }
                                }
                            } else {
                                layer.alert("登记来访记录失败", {icon: 2, title: "提示"}, function (index) {
                                    layer.close(index);
                                });
                            }
                        });
                    }else {
                            if (addFaceInfoResult.resultCode==-16) {
                                layer.alert('保存访客基本信息失败,照片不符合要求', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else if (addFaceInfoResult.resultCode==1) {
                                layer.alert('保存访客基本信息失败,信息保存失败', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else if (addFaceInfoResult.resultCode==2) {
                                layer.alert('保存访客基本信息失败,身份证号码冲突', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else if (addFaceInfoResult.resultCode==3) {
                                layer.alert('保存访客基本信息失败,手机号码冲突', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else if (addFaceInfoResult.resultCode==5) {
                                layer.alert('保存访客基本信息失败,Openid冲突', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else if (addFaceInfoResult.resultCode==-1) {
                                layer.alert('保存访客基本信息失败,操作失败', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else if (addFaceInfoResult.resultCode==-13) {
                                layer.alert('保存访客基本信息失败,图片格式不正确', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else if (addFaceInfoResult.resultCode==-14) {
                                layer.alert('保存访客基本信息失败,未检测到人脸', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else if (addFaceInfoResult.resultCode==-15) {
                                layer.alert('保存访客基本信息失败,人脸图片建库超时', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else if (addFaceInfoResult.resultCode==-17) {
                                layer.alert('保存访客基本信息失败,图片身份不确定', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else if (addFaceInfoResult.resultCode==-18) {
                                layer.alert('保存访客基本信息失败,超时', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else if (addFaceInfoResult.resultCode==-99) {
                                layer.alert('保存访客基本信息失败,不支持该命令', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else {
                                layer.alert('保存访客基本信息失败,错误代码:' + addFaceInfoResult.resultCode, {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }
                        }
                });
            }
        }
    }

    /**
     * 点击退出
     */
    $("#exit").click(function () {
        if ($("#ifWriteFlag").val() != "0") {
            layer.confirm('访客未登记完毕,是否退出?', {btn: ['是', '否'], icon: 3, title: "提示"}, function (index1) {
                parent.layer.closeAll();
                layer.close(index1);
            }, function (index1) {
                layer.close(index1);
            });
        } else {
            parent.layer.closeAll();
        }
    });
});

//保存员工的来访记录和设备权限(员工无法编辑基本信息,所以只有前两步)
function employeeSaveStep2() {
    var timeResult = getTime();
    var termIdList = getTermIdList();
    addFaceVisitInfo(editFaceId, 0, termIdList, timeResult.startTime, timeResult.endTime, function (addFaceVisitInfoResult, ifExist) {
        if (addFaceVisitInfoResult.resultCode == "0") {
            var recId = addFaceVisitInfoResult.resultData.rec_id;
            var faceInfo = {
                face_id: editFaceId,
                visit_time: timeResult.startTime,
                expire_time: timeResult.endTime
            };
            facePermitConfig(faceInfo, termIdList, recId, function (facePermitConfigResult) {
                if (facePermitConfigResult.resultCode != "0") {
                    layer.alert("编辑配置权限失败,请重试", {icon: 2, title: "提示"}, function (index) {
                        layer.close(index);
                    });
                } else {
                    layer.alert('编辑登记来访记录成功', {icon: 1, title: "提示"}, function () {
                        window.parent.location.reload();
                    });
                }
            });
        } else {
            layer.alert("编辑登记来访记录失败", {icon: 2, title: "提示"}, function (index) {
                layer.close(index);
            });
        }
    });
}

//新增faceInfo
function addFaceInfo(startTime, endTime, callBack) {
    //添加访客参数
    var visitorName = $("#visitorName").val();
    var IDCardNum = $("#IDCardNum").val();
    var IDCardPhoto = IDCardPhotoData;
    var nation = $("#nation").val();
    var address = $("#address").val();
    var IDCardDate = $("#IDCardDate").val();
    var sex = $("input[name='sex']:checked").val();
    var visitorTelNum = $("#visitorTelNum").val();
    var visitorNickName = $("#visitorNickName").val();
    var visitorCompanyName = $("#visitorCompanyName").val();
    var visitorPosition = $("#visitorPosition").val();
    var faceInfo = {
        face_id: editFaceId,
        attribute: 1,
        sex: sex,
        name: visitorName,
        visit_time: startTime,
        expire_time: endTime
    };
    if ("" != IDCardPhoto) {
        faceInfo.photo_wlt = IDCardPhoto;
        faceInfo.type = 1;
    }
    var pictureList = new Array();
    for (var i = 1; i <= 3; i++) {
        var photoId = "photo" + i;
        var pass = $("#" + photoId).attr("pass");
        var picId = $("#" + photoId).attr('picId');
        var src = $("#" + photoId).attr('src');
        var feature_ver = $("#" + photoId).attr('feature_ver');
        //照片为空并且ID为0,则不传入后台
        var flag1 = (src == "" || src == undefined) && picId == "0";
        //照片不为空且pass为0,则不传入后台
        var flag2 = src != "" && src != undefined && pass == "0";
        if (flag1 || flag2) {
            //不操作
        } else {
            var option = $("#" + photoId).attr('option');
            var picture = {};
            if (option == "1") {//新增或修改
                var faceWidth = $("#" + photoId).attr('faceWidth');
                var faceHeight = $("#" + photoId).attr('faceHeight');
                var faceData = $("#" + photoId).attr('faceData');
                var feature = $("#" + photoId).attr('feature');
                var feature_ver = $("#" + photoId).attr('feature_ver');
                picture = {
                    pic_id: picId,
                    data: faceData,
                    width: faceWidth,
                    height: faceHeight,
                    feature: feature,
                    feature_ver:feature_ver,
                    option: option
                };
            } else {//删除或不修改
                picture = {
                    pic_id: picId,
                    option: option
                };
            }
            pictureList.push(picture);
        }
    }
    var photo4 = $("#photo4").attr('src');
    if (undefined != photo4 && "" != photo4 && "/css/img/headPhotoDefault.png" != photo4) {
        photo4 = photo4.replace('data:;base64,', '');
        photo4 = photo4.replace('data:image/jpeg;base64,', '');
        photo4 = photo4.replace('data:image/png;base64,', '');
        photo4 = photo4.replace('data:image/bmp;base64,', '');
        faceInfo.face_info_base64 = photo4;
    } else {
        var photo4Id = $("#photo4").attr('picId');
        if (photo4Id != '0') {
            var picture = {
                pic_id: photo4Id,
                option: 2,
                image_type: 1
            };
            pictureList.push(picture);
        }
    }
    if (IDCardNum != "") {
        faceInfo.idcard = IDCardNum;
    }
    if (nation != "请选择") {
        faceInfo.nation = nation;
    }
    if (IDCardDate != "") {
        faceInfo.idcard_expire = IDCardDate;
    }
    if (address != "") {
        faceInfo.addr = address;
    }
    if (visitorCompanyName != "") {
        faceInfo.company = visitorCompanyName;
    }
    if (visitorNickName != "") {
        faceInfo.nick_name = visitorNickName;
    }
    if (visitorTelNum != "") {
        faceInfo.tel_no = visitorTelNum;
    }
    if (visitorPosition != "") {
        faceInfo.position = visitorPosition;
    }
    faceInfo.pictures = pictureList;
    $.ajax({
        url: "/employeeManage/addFaceInfo",
        type: "post",
        data: JSON.stringify(faceInfo),
        contentType: 'application/json',
        async: false,
        traditional: true,
        success: function (data) {
            var addFaceInfoResult = $.parseJSON(data);
            callBack(addFaceInfoResult);
        }
    });
}

//新增addFaceVisitInfo
function addFaceVisitInfo(faceId, attribute, termIdList, startTime, endTime, callBack) {
    //判断此人员是否重复添加(右侧列表存在则为重复添加)
    var ifExist = false;
    var recId = editRecId;
    $(".recFaceData").each(function () {
        //如果人员是重复的,则令recId也一致
        if (faceId == $(this).attr('faceid')) {
            ifExist = true;
            recId = $(this).attr('recid');
        }
    });
    //来访记录参数
    var visitorName = $("#visitorName").val();
    var IDCardNum = $("#IDCardNum").val();
    var sex = $("input[name='sex']:checked").val();
    var visitorTelNum = $("#visitorTelNum").val();
    var visitorCompanyName = $("#visitorCompanyName").val();
    var faceVisitInfo = {
        rec_id: recId,
        face_id: faceId,
        attribute: attribute,
        name: visitorName,
        sex: sex,
        tel: visitorTelNum,
        idcard: IDCardNum,
        company: visitorCompanyName,
        visit_time: startTime,
        expire_time: endTime,
        visit_num: 0,
        conf_id: 0,
        business: 0
    };
    if (onlyStep3) {//只有第三步的情况,因没有前两步的信息,所以需要查
        var param = {
            attribute: 0,
            face_id: batEmployeeId,
            depart_id: 0,
            status: 0
        };
        $.ajax({
            url: "/visitorManage/selectByParam",
            type: "post",
            data: param,
            async: false,
            success: function (data) {
                var jsonData = $.parseJSON(data);
                if (jsonData.count == 1) {
                    faceVisitInfo.employee_id = batEmployeeId;
                    faceVisitInfo.bat_id = batId;
                    faceVisitInfo.depart_id = jsonData.data[0].depart_id;
                    faceVisitInfo.employee = jsonData.data[0].name;
                    faceVisitInfo.reason_id = batReasonId;
                    faceVisitInfo.content = batContent;
                }
            }
        });
    } else {
        var employee = $("#employee").val();
        var valArr = employee.split(",");
        var face_id = valArr[0];
        var depart = $("#depart").val();
        var employeeName = $("#employee").next().find(".layui-input").val();
        var visitReasonId = $("#visitReason").val();
        var visitReasonName = $("#visitReason").next().find(".layui-input").val();
        faceVisitInfo.book_id = editBookId;
        faceVisitInfo.employee_id = face_id;
        faceVisitInfo.bat_id = batId;
        faceVisitInfo.depart_id = depart;
        faceVisitInfo.employee = employeeName;
        faceVisitInfo.reason_id = visitReasonId;
        faceVisitInfo.content = visitReasonName;
        faceVisitInfo.terms = termIdList;
    }
    if (IDCardNum != "") {
        faceVisitInfo.idcard = IDCardNum;
    }
    $.ajax({
        url: "/visitorManage/addFaceVisitInfo",
        type: "post",
        data: JSON.stringify(faceVisitInfo),
        contentType: 'application/json',
        async: false,
        traditional: true,
        success: function (data) {
            var addFaceVisitInfoResult = $.parseJSON(data);
            callBack(addFaceVisitInfoResult, ifExist);
        }
    });
}

//配置设备权限
function facePermitConfig(faceInfo, termIdList, recId, callBack) {
    //即使是员工,也是以访客身份配置临时的权限,这里固定为1
    //faceInfo.attribute = 1;
    var facePermitParam = {
        faceInfo: faceInfo,
        termIdList: termIdList,
        recId: recId
    };
    $.ajax({
        url: "/employeeManage/facePermitConfig",
        type: "post",
        data: JSON.stringify(facePermitParam),
        contentType: 'application/json',
        async: false,
        traditional: true,
        success: function (data) {
            var facePermitConfigResult = $.parseJSON(data);
            callBack(facePermitConfigResult);
        },
        error: function () {
            layer.alert('添加访客设备权限失败,服务器错误,请重试', {icon: 2, title: "提示"}, function (index) {
                layer.close(index);
            });
        }
    });
}

//继续登记
function continueRegister(ifExist, startTime, endTime, faceId, recId, ifMsg) {
    var visitorName = $("#visitorName").val();
    if (batchRegister) {//是否批量登记
        if (ifExist) {//已登记的访客在右侧重复
            //删除之前的记录
            $(".recFaceData").each(function () {
                if (recId == $(this).attr('recid')) {
                    $(this).parent().remove();
                }
            });
        }
        if (brushCardAutoSave || ifMsg == "2") {//不需要弹框提示
            $(".noDataMsg").remove();
            var addTableHTML = "<tr>" +
                "                        <td>" + visitorName + "</td>" +
                "                        <td>" + startTime + "</td>" +
                "                        <td>" + endTime + "</td>" +
                "                        <td style='cursor: pointer;' class='recFaceData' faceid='" + faceId + "' recid='" + recId + "' onclick='deleteVisitorReg(this)'><a class='layui-btn layui-btn-danger layui-btn-xs'>删除</a></td>" +
                "                    </tr>";
            $("#registeredVisitors").append(addTableHTML);
        } else {//需要弹框提示
            $(".noDataMsg").remove();
            layer.alert("登记成功", {icon: 1}, function (index) {
                var addTableHTML = "<tr>" +
                    "                        <td>" + visitorName + "</td>" +
                    "                        <td>" + startTime + "</td>" +
                    "                        <td>" + endTime + "</td>" +
                    "                     <td style='cursor: pointer;' class='recFaceData' faceid='" + faceId + "' recid='" + recId + "' onclick='deleteVisitorReg(this)'><a class='layui-btn layui-btn-danger layui-btn-xs'>删除</a></td>" +
                    "                    </tr>";
                $("#registeredVisitors").append(addTableHTML);
                layer.close(index);
            });
        }
        clearSetp3Form();
    } else {//非批量登记
        if (ifMsg == "2") {
            window.location.reload();
        } else {
            layer.alert("登记成功", {icon: 1}, function (index) {
                window.location.reload();
                layer.close(index);
            });
        }
    }
}

//获取来访的开始时间和结束时间
function getTime() {
    var startTime = "";
    var endTime = "";
    if (onlyStep3) {
        startTime = batStartTime;
        endTime = batEndTime;
    } else {
        startTime = $("#startTime").val();
        endTime = $("#endTime").val();
    }
    var timeResult = {
        startTime: startTime,
        endTime: endTime
    };
    return timeResult;
}

//获取设备权限
function getTermIdList() {
    var termIdList = [];
    var deviceList = [];
    if (onlyStep3) {
        termIdList = batTermIdList;
    } else {
        layui.use(['table'], function () {
            var table = layui.table;
            var checkStatus = table.checkStatus('deviceTable');
            deviceList = checkStatus.data;
            $.each(deviceList, function (i, item) {
                termIdList.push(item.term_id);
            });
        });
    }
    return termIdList;
}

//删除来访记录
function deleteVisitorReg(obj) {
    var rec_id = $(obj).parent().find(".recFaceData").attr("recid");
    var faceVisitInfo = {
        rec_id: rec_id,
        status: 2
    };
    $.ajax({
        url: "/visitorManage/deleteVisitorRegister",
        type: "post",
        data: faceVisitInfo,
        async: false,
        success: function (data) {
            if (data == "1") {
                $(obj).parent().remove();
                var a = document.getElementById("registeredVisitors");
                var tr = a.getElementsByTagName("tr");
                if (tr.length == 0) {//删除后如果没有数据了,则显示相应HTML
                    $("#registeredVisitors").append(noDataMsgHTML);
                }
            } else {
                layer.alert("删除失败,请联系管理员", {icon: 2, title: "提示"}, function (index) {
                    layer.close(index);
                });
            }
        }
    });
}

//清空第三步表单
function clearSetp3Form() {
    //把比对照片/头像/身份证照片的宽高复原,并且设置为默认照片
    $("#photo1").attr('width', '142');
    $("#photo1").attr('height', '176');
    $("#photo1").attr('src', '/css/img/comparePhotoDefault.png');
    $("#photo2").attr('width', '142');
    $("#photo2").attr('height', '176');
    $("#photo2").attr('src', '/css/img/comparePhotoDefault.png');
    $("#photo3").attr('width', '142');
    $("#photo3").attr('height', '176');
    $("#photo3").attr('src', '/css/img/comparePhotoDefault.png');
    $("#photo4").attr('width', '138');
    $("#photo4").attr('height', '138');
    $("#photo4").attr('src', '/css/img/headPhotoDefault.png');
    $("#IDCardPhoto").attr('src', '/css/img/IDCardPhotoDefault.png');
    //把手动通过的按钮隐藏
    $(".passCompareBtn").each(function () {
        $(this).hide();
    });
    //重置表单
    $("#visitorRegisterThree")[0].reset();
    layui.use(['form'], function () {
        var form = layui.form;
        form.render();
    });
    //如果之前开了刷证自动保存,这里再开启
    if (brushCardAutoSave) {
        $("#brushCardAutoSave").next().click();
    }
}

//身份证号失去焦点事件
$("#IDCardNum").blur(function () {
    var IDCardNum = $("#IDCardNum").val();
    if (IDCardNum != "" && !checkIDCard(IDCardNum)) {
        layer.msg('请输入合法的身份证号码', {icon: 0, anim: 6});
    } else if (IDCardNum == "") {//若身份证号为空,则把check的faceId变为0
        $("#IDCardNum").attr('checkFaceId', '0');
    } else if (IDCardNum.length == 18) {
        var param = {
            idcard: IDCardNum,
            attribute: 0,
            face_id: 0,
            depart_id: 0,
            status: 1
        };
        //传入身份证号检测人员是否重复
        checkFaceInfo(param, "身份证号", function () {});
    }
});

//访客手机号失去焦点事件
$("#visitorTelNum").blur(function () {
    var visitorTelNum = $("#visitorTelNum").val();
    reg = /0?(13|14|15|16|17|18|19)[0-9]{9}/;
    if (visitorTelNum != "" && !reg.test(visitorTelNum)) {
        layer.msg('请输入合法的手机号码', {icon: 0, anim: 6});
    } else if (visitorTelNum == "") {//若手机号为空,则把check的faceId变为0
        $("#visitorTelNum").attr('checkFaceId', '0');
    } else if (visitorTelNum.length == 11) {
        var param = {
            tel_no: visitorTelNum,
            attribute: 0,
            face_id: 0,
            depart_id: 0,
            status: 1
        };
        //传入手机号检测人员是否重复
        checkFaceInfo(param, "手机号", function () {});
    }
});

//被访人手机号失去焦点事件,自动回填所在部门及部门下的所有人员,并自动选择此手机号的员工
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
            status: 0
        };
        $.ajax({
            url: "/visitorManage/selectByParam",
            type: "post",
            data: param,
            async: false,
            success: function (data) {
                var jsonData = $.parseJSON(data);
                if (jsonData.count == 1) {
                    var faceInfo = jsonData.data[0];
                    var telNum = faceInfo.tel_no;
                    var faceId = faceInfo.face_id;
                    var departId = faceInfo.depart_id;
                    var value = faceId + "," + telNum + "," + departId;
                    param = {
                        depart_id: departId,
                        attribute: 0,
                        status: 0
                    };
                    $.ajax({
                        url: "/employeeManage/selectByDepart",
                        type: "post",
                        data: param,
                        async: true,
                        success: function (data) {
                            $("#employee").empty();
                            var jsonData1 = $.parseJSON(data);
                            var count = jsonData1.count;
                            if (count != 0) {
                                var empList = jsonData1.data;
                                var selectEmpHTML = "<option value=''>请选择</option>";
                                $.each(empList, function (index, value) {
                                    var telNum = value.tel_no;
                                    var faceId = value.face_id;
                                    var name = value.name;
                                    var departId = value.depart_id;
                                    var value = faceId + "," + telNum + "," + departId;
                                    var optionHTML = "<option value='" + value + "'>" + name + "</option>";
                                    selectEmpHTML = selectEmpHTML + optionHTML;
                                });
                                $("#employee").append(selectEmpHTML);
                                $("#employee").val(value);
                                $("#depart").val(departId);
                                layui.use(['form'], function () {
                                    var form = layui.form;
                                    form.render("select");
                                });
                            }
                        }
                    });

                } else if (jsonData.count > 1) {
                    layer.alert("此手机号码已登记过多个员工,数据异常,请联系系统管理员", {icon: 2, title: "提示"}, function (index) {
                        layer.close(index);
                    });
                } else {
                    layer.alert("根据此手机号码未查询到员工", {icon: 0, title: "提示"}, function (index) {
                        $("#employee").val("");
                        $("#depart").val("");
                        layui.use(['form'], function () {
                            var form = layui.form;
                            form.render("select");
                        });
                        layer.close(index);
                    });
                }
            }
        });
    }
});

//效验身份证号和手机号是否重复,
function checkFaceInfo(param, paramName, readCardCallBack) {
    //先使读证的定时任务失效,以免在检测过程中重复读证
    clearInterval(t1);
    //先检测是否是离职员工,再检测是否是员工,最后检测访客
    $.ajax({
        url: "/visitorManage/selectByParam",
        type: "post",
        data: param,
        async: false,
        success: function (data) {
            var jsonData = $.parseJSON(data);
            var faceInfo = jsonData.data[0];
            if (jsonData.count == 1) {
                var departId = jsonData.data[0].depart_id;
                //如果检测出是离职员工,需要查出所在部门,用于提示
                $.ajax({
                    url: "/employeeManage/selectDepart",
                    type: "post",
                    data: {departId: departId},
                    async: false,
                    success: function (departData) {
                        var jsonDepartData = $.parseJSON(departData);
                        if (jsonDepartData.resultCode == "0") {
                            var depatName = jsonDepartData.resultData[0].depart_name;
                            layer.confirm("此" + paramName + "为已离职员工[" + depatName + ":" + jsonData.data[0].name + "],是否使用此离职员工的信息登记访客?",
                                {
                                    btn: ['是', '否'], icon: 3, cancel: function () {
                                        parent.layer.closeAll();
                                    }, end: function () {
                                        if (ifIE()) {
                                            t1 = window.setInterval("readIDCard()", 1000);
                                        }
                                    }
                                },
                                function (index) {
                                    checkSubmit(faceInfo);
                                },
                                function () {
                                    parent.layer.closeAll();
                                }
                            );
                        } else {//若查询部门出错,则不显示部门,仍可正常运行
                            layer.confirm("此" + paramName + "为已离职员工[" + jsonData.data[0].name + "],是否使用此离职员工的信息登记访客?",
                                {
                                    btn: ['是', '否'], icon: 3, cancel: function () {
                                        parent.layer.closeAll();
                                    }, end: function () {
                                        if (ifIE()) {
                                            t1 = window.setInterval("readIDCard()", 1000);
                                        }
                                    }
                                },
                                function (index) {
                                    checkSubmit(faceInfo);
                                },
                                function () {
                                    parent.layer.closeAll();
                                }
                            );
                        }
                    }
                });
            } else if (jsonData.count > 1) {//此处是根据身份证或手机号查询,查询数量必然<=1,超过一条数据必然需要检查数据库
                layer.alert("此" + paramName + "已登记过多个离职员工,请联系管理员检查数据库", {icon: 2, title: "提示"}, function (index) {
                    layer.close(index);
                });
            } else {
                //把状态改为在职
                param.status = 0;
                $.ajax({
                    url: "/visitorManage/selectByParam",
                    type: "post",
                    data: param,
                    async: false,
                    success: function (data) {
                        var jsonData = $.parseJSON(data);
                        if (jsonData.count == 1) {
                            var faceInfo = jsonData.data[0];
                            if (editFaceId == "0") {
                                faceInfo.attribute = 0;
                                layer.confirm("此" + paramName + "与已存在的员工[" + faceInfo.name + "]重复,是否使用此员工的信息登记访客?", {
                                    btn: ['是', '否'],
                                    icon: 3,
                                    title: "提示",
                                    cancel: function () {
                                        if (paramName == "身份证号") {
                                            $("#IDCardNum").val("");
                                        }
                                        if (paramName == "手机号") {
                                            $("#visitorTelNum").val("");
                                        }
                                    },
                                    end: function () {
                                        if (ifIE()) {
                                            t1 = window.setInterval("readIDCard()", 1000);
                                        }
                                    }
                                }, function () {
                                    checkSubmit(faceInfo);
                                }, function () {
                                    if (paramName == "身份证号") {
                                        $("#IDCardNum").val("");
                                    }
                                    if (paramName == "手机号") {
                                        $("#visitorTelNum").val("");
                                    }
                                });
                            } else {
                                layer.alert("无法更改为此" + paramName + ",与已存在的员工[" + faceInfo.name + "]重复", {
                                    icon: 0, title: "提示", end: function () {
                                        if (ifIE()) {
                                            t1 = window.setInterval("readIDCard()", 1000);
                                        }
                                    }
                                }, function (index) {
                                    layer.close(index);
                                });
                                if (paramName == "身份证号") {
                                    $("#IDCardNum").val("");
                                }
                                if (paramName == "手机号") {
                                    $("#visitorTelNum").val("");
                                }
                            }
                        } else if (jsonData.count > 1) {
                            layer.alert("此" + paramName + "已登记过多个员工,数据异常,请联系系统管理员", {icon: 2, title: "提示"}, function (index) {
                                layer.close(index);
                            });
                        } else {
                            param.attribute = 1;
                            $.ajax({
                                url: "/visitorManage/selectByParam",
                                type: "post",
                                data: param,
                                async: false,
                                success: function (data) {
                                    jsonData = $.parseJSON(data);
                                    if (jsonData.count == 1) {
                                        faceInfo = jsonData.data[0];
                                        faceInfo.attribute = 1;
                                        var checkFaceId = faceInfo.face_id;
                                        if (checkFaceId != editFaceId) {
                                            if (editFaceId == "0") {
                                                layer.confirm("此" + paramName + "已登记过访客[" + faceInfo.name + "],是否使用之前的信息登记?", {
                                                    btn: ['是', '否'],
                                                    icon: 3,
                                                    title: "提示",
                                                    cancel: function () {
                                                        if (paramName == "身份证号") {
                                                            $("#IDCardNum").val("");
                                                        }
                                                        if (paramName == "手机号") {
                                                            $("#visitorTelNum").val("");
                                                        }
                                                    },
                                                    end: function () {
                                                        if (ifIE()) {
                                                            t1 = window.setInterval("readIDCard()", 1000);
                                                        }
                                                    }
                                                }, function () {
                                                    checkSubmit(faceInfo);
                                                }, function () {
                                                    if (paramName == "身份证号") {
                                                        $("#IDCardNum").val("");
                                                    }
                                                    if (paramName == "手机号") {
                                                        $("#visitorTelNum").val("");
                                                    }
                                                })
                                            } else {
                                                layer.alert("无法更改为此" + paramName + ",与已登记的其他访客[" + faceInfo.name + "]重复", {
                                                    icon: 0, title: "提示", end: function () {
                                                        if (ifIE()) {
                                                            t1 = window.setInterval("readIDCard()", 1000);
                                                        }
                                                    }
                                                }, function (index) {
                                                    layer.close(index);
                                                });
                                                if (paramName == "身份证号") {
                                                    $("#IDCardNum").val("");
                                                }
                                                if (paramName == "手机号") {
                                                    $("#visitorTelNum").val("");
                                                }
                                            }
                                        } else {
                                            if (ifIE()) {
                                                t1 = window.setInterval("readIDCard()", 1000);
                                            }
                                        }
                                    } else if (jsonData.count > 1) {
                                        layer.alert("此" + paramName + "已登记过多个访客,数据错误,请联系管理员", {
                                            icon: 2,
                                            title: "提示"
                                        }, function (index) {
                                            layer.close(index);
                                        });
                                    } else {
                                        if (ifIE()) {
                                            t1 = window.setInterval("readIDCard()", 1000);
                                        }
                                        readCardCallBack();
                                        if (paramName == "身份证号") {
                                            $("#IDCardNum").attr('checkFaceId', '0');
                                        }
                                        if (paramName == "手机号") {
                                            $("#visitorTelNum").attr('checkFaceId', '0');
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    });
}

/**
 * 身份证号或手机重复之后,会提示是否使用重复的信息直接保存,点击保存则调用此方法
 * 只保存来访记录和设备权限,人员信息是重复的所以不用调用
 * @param faceInfo
 */

function checkSubmit(faceInfo) {
    $("#visitorName").val(faceInfo.name);
    if (faceInfo.sex == 1) {
        $('#man').attr("checked", true);
        $('#woman').attr("checked", false);
    }
    if (faceInfo.sex == 2) {
        $('#man').attr("checked", false);
        $('#woman').attr("checked", true);
    }
    $("#IDCardNum").val(faceInfo.idcard);
    $("#visitorTelNum").val(faceInfo.tel_no);
    if (faceInfo.attribute != 0) {//访客会比员工多一个公司名称
        $("#visitorCompanyName").val(faceInfo.company);
    }
    layui.use(['form'], function () {
        var form = layui.form;
        form.render();
    });
    var termIdList = getTermIdList();
    var faceId = faceInfo.face_id;
    var timeResult = getTime();
    addFaceVisitInfo(faceId, faceInfo.attribute, termIdList, timeResult.startTime, timeResult.endTime, function (addFaceVisitInfoResult, ifExist) {
        if (addFaceVisitInfoResult.resultCode == "0") {
            var recId = addFaceVisitInfoResult.resultData.rec_id;
            faceInfo.visit_time = timeResult.startTime;
            faceInfo.expire_time = timeResult.endTime;
            facePermitConfig(faceInfo, termIdList, recId, function (facePermitConfigResult) {
                if (facePermitConfigResult.resultCode != "0") {
                    layer.alert("配置权限失败,请重试", {icon: 2, title: "提示"}, function (index) {
                        layer.close(index);
                    });
                } else {
                    layer.alert('登记成功', {icon: 1, title: "提示"}
                        , function (index) {
                            continueRegister(ifExist, timeResult.startTime, timeResult.endTime, faceId, recId, "2");
                            layer.close(index);
                        }
                    );
                }
            });
        }
    });
}

/**
 * 读身份证时需要依次比对三张比对照片
 * @param img1 身份证照片
 * @param img2 比对照片
 * @param photoNo 比对照片的序号(1,2,3)
 */
function comparePhotoReadCard(img1, img2, photoNo) {
    var param = {
        img1: img1,
        img2: img2
    };
    $.ajax({
        url: "/employeeManage/comparePhoto",
        type: "post",
        data: param,
        async: false,
        success: function (data) {
            var compareId = "passCompare" + photoNo;
            var photoId = "photo" + photoNo;
            if (data < 0.5) {
                layer.alert('人证比对不通过,如需继续登记可手动通过', {icon: 2, title: "提示"}, function (index) {
                    layer.close(index);
                });
                $("#" + photoId).attr("pass", "0");
                $("#" + compareId).show();
                readIDCardComparePhotoFlag = false;//有任意一张照片不通过比对,就设置为false
            } else {
                $("#" + photoId).attr("pass", "1");
                $("#" + compareId).hide();
            }
        }
    });
}

/**
 * 此方法专为访客头像服务,因此处头像尺寸与其它地方不一致,所以另外写方法
 * @param photoNo.图片编号:1/2/3是比对照片,4是头像
 */
function deletePhoto2(photoNo) {
    var photoId = "photo" + photoNo;
    if (photoNo != "4") {
        $("#" + photoId).attr('width', 142);
        $("#" + photoId).attr('height', 176);
        $("#" + photoId).attr('src', '/css/img/comparePhotoDefault.png');
        var passCompareId = "passCompare" + photoNo;
        $("#" + passCompareId).hide();
        $("#" + photoId).attr('option', '2');
        $("#ifWriteFlag").val("1");
    } else {
        var headPhotoPicId = $("#" + photoId).attr('picId');
        var firstPhotoPicId = $("#photo1").attr('picId');
        if (headPhotoPicId == firstPhotoPicId) {
            layer.confirm("如删除此头像,则会把第一张比对照片一并删除,确认删除吗?", {btn: ['确认', '取消'], icon: 3, title: '提示'},
                function (index) {//确定回调
                    $("#" + photoId).attr('width', 138);
                    $("#" + photoId).attr('height', 138);
                    $("#" + photoId).attr('src', '/css/img/headPhotoDefault.png');
                    $("#" + photoId).attr('option', '2');
                    $("#ifWriteFlag").val("1");
                    layer.close(index);
                }, function (index) {//取消回调
                    layer.close(index);
                }
            );
        } else {
            $("#" + photoId).attr('width', 138);
            $("#" + photoId).attr('height', 138);
            $("#" + photoId).attr('src', '/css/img/headPhotoDefault.png');
            $("#" + photoId).attr('option', '2');
            $("#ifWriteFlag").val("1");
        }
    }
}

$("#visitorRegisterOne :input").change(function () {
    $("#ifWriteFlag").val("1");
});
$("#visitorRegisterTwo :input").change(function () {
    $("#ifWriteFlag").val("1");
});
$("#visitorRegisterThree :input").change(function () {
    $("#ifWriteFlag").val("1");
});

function readIDCard() {
    var result;
    var cardtype;
    try {
        var ax = new ActiveXObject("IDRCONTROL.IdrControlCtrl.1");
        var IdrControl1 = document.getElementById("IdrControl1");
        result = IdrControl1.RepeatRead(0);   //设置是否重复读卡  0-不重复  1-重复
        //注意：第一个参数为对应的设备端口，USB型为1001，串口型为1至16
        result = IdrControl1.ReadCard("1001", "c:\\ocx\\test.jpg");
        cardtype = IdrControl1.DecideReadCardType();//判断卡类型 1-身份证 2-外国居留证
        if (result == 1) {
            $("#message").text("(提示:读卡器连接正常,可刷身份证!)");
            if (editFaceId == '0' && autoSaveFlag == 0) {//非编辑并且标识位为0
                //显示"刷证自动保存"
                $("#onlyBrushCardDiv").show();
                if (batchRegister) {
                    $("#brushCardAutoSave").next().click();
                    autoSaveFlag = 1;//修改标识位避免重复开启
                }
            }
            //显示身份证的框
            $("#IDCardPhotoDiv").show();
            if (cardtype == 1) {
                var IDCardNum = IdrControl1.GetCode();
                var param = {
                    idcard: IDCardNum,
                    attribute: 0,
                    face_id: 0,
                    depart_id: 0,
                    status: 1
                };
                //检测身份证号是否重复
                checkFaceInfo(param, "身份证号", function () {
                    //进入回调方法说明验证通过
                    $("#IDCardNum").val(IDCardNum);
                    $("#visitorName").val(IdrControl1.GetName());
                    var sex = IdrControl1.GetSex();
                    if (sex == "男") {
                        $('#man').next().click();
                    }
                    if (sex == "女") {
                        $('#woman').next().click();
                    }
                    $("#nation").val(IdrControl1.GetFolk() + "族");
                    $("#address").val(IdrControl1.GetAddress());
                    $("#IDCardDate").val(IdrControl1.GetValid());
                    $("#IDCardPhoto").attr('src', 'data:image/jpeg;base64,' + IdrControl1.GetJPGPhotobuf());
                    IDCardPhotoData = IdrControl1.GetWLTbuf();
                    layui.use(['form'], function () {
                        var form = layui.form;
                        form.render();
                    });
                    //把三张比对照片依次与身份证照片比对
                    for (var i = 1; i <= 3; i++) {
                        var photoId = "photo" + i;
                        var photoSrc = $("#" + photoId).attr('src');
                        if (undefined != photoSrc && "" != photoSrc && '/css/img/comparePhotoDefault.png' != photoSrc) {
                            photoSrc = photoSrc.replace('data:;base64,', '');
                            photoSrc = photoSrc.replace('data:image/png;base64,', '');
                            photoSrc = photoSrc.replace('data:image/bmp;base64,', '');
                            comparePhotoReadCard(IdrControl1.GetJPGPhotobuf(), photoSrc, i);
                        }
                    }
                    //刷证自动保存的情况下,若照片比对都通过,才能自动保存
                    if (brushCardAutoSave && readIDCardComparePhotoFlag) {
                        if (batchRegister) {
                            $("#continueRegister").click();
                        } else {
                            $("#saveRegister").click();
                        }
                    }
                    //把比对照片通过的标识位改为通过
                    readIDCardComparePhotoFlag = true;
                });
            } else {
                $("#message").text("提示:当前卡片不是身份证,请将身份证放到读卡器上!");
            }
        } else if (result == -2) {
            $("#message").text("(提示:读卡器连接正常,可刷身份证!)");
            if (editFaceId == '0' && autoSaveFlag == 0) {
                $("#onlyBrushCardDiv").show();
                if (batchRegister) {
                    $("#brushCardAutoSave").next().click();
                    autoSaveFlag = 1;
                }
            }
            $("#IDCardPhotoDiv").show();
        } else if (result == -1) {
            $("#message").text("(提示:未检测到身份证读卡器设备,如需使用读身份证功能请将读卡器连接至PC!)");
            if (brushCardAutoSave) {
                $("#brushCardAutoSave").next().click();
            }
            autoSaveFlag = 0;
            $("#onlyBrushCardDiv").hide();
        } else if (result == -3) {
            $("#message").text("(提示:读取数据失败!)");
        } else if (result == -4) {
            $("#message").text("(提示:生成照片文件失败，请检查设定路径和磁盘空间!)");
        }
    } catch (e) {
        clearInterval(t1);
        var downloadOCXHTML = "提示:身份证读卡控件未安装!<a style='color: #54b5ff;text-decoration:underline' href='/employeeManage/downloadPlugin?pluginName=ocx'>点此下载</a>";
        $("#message").html(downloadOCXHTML);
        if (brushCardAutoSave) {
            $("#brushCardAutoSave").next().click();
        }
        autoSaveFlag = 0;
        $("#onlyBrushCardDiv").hide();
    }
}

function addCar(id){
	layer.open({
		type:2,
		title:'车辆',
		shadeClose:false,
		scrollbar: false,
		shade:[0.2, '#000000'],
		area: ['650px','310px'],
		btn: ['保存','取消'],
		btnAlign: 'c',
		content:'/visitorManage/toAddCar',
		yes: function (index, layero) {
			var res = window["layui-layer-iframe"+index].callbackdata();
			if(res.carNum==""){
				layer.msg("车牌号不能为空");
				return;
			}
			if(res.carId==0){	//新增车辆信息
				for(var i=0;i<carList.length;i++){
					if(carList[i].carId!=0 && res.carNum==carList[i].carNum){
						layer.msg("此车牌号已存在，请勿重复登记");
						return;
					}
				}
			}else {				//编辑车辆信息
				for(var i=0;i<carList.length;i++){
					if(carList[i].carId!=0 && res.carNum==carList[i].carNum&&res.carId!=carList[i].carId){
						layer.msg("已经存在此信息，无法编辑");
						return;
					}
				}
			}
			
			if(res.carKind=="请选择"){
				layer.msg("请选择车辆类型");
				return;
			}
			if(res.carColor==""){
				layer.msg("请输入车辆颜色");
				return;
			}
			if(res.carId!=0){	//编辑
				$("#carNum"+res.carId).val(res.carNum);
				for(var i=0;i<carList.length;i++){
					if(carList[i].carId==res.carId){
						carList[i] = res;
					}
				}
				layer.close(index);
				return;
			}
			var str = "<div id='car"+index+"' class='layui-input-inline' style='margin:0px 15px 10px 0px;'>" +
                    		"<input type='text' ondblclick='addCar("+index+")' id='carNum"+index+"' name='carNum"+index+"' readonly class='layui-input' style='height: 32px;line-height: 32px;border-radius: 4px;width:155px;float:left;' value='"+res.carNum+"'>"+
                    		"<input type='hidden' id='carData"+index+"' value='"+res+"'>"+
                    		"<button type='button' onclick='deleteCar("+index+")'><img src='/css/img/delete2.png' style='float:left;width:32;htight: 32px;margin-left:4px;margin-top:4px;'></button>"+
                    	"</div>"
            $("#cars").append(str);
			res.carId = index;
			carList.push(res);
			layer.close(index);
		},
		btn2: function (index, layero) {
			layer.close(index);
		},
		cancel: function (index, layero) {
			var res = window["layui-layer-iframe"+index].callbackdata();
			console.log(res);
			console.log("cancel-index:"+index)
			$("#carData").text("nothing");
		},
		//初始化弹窗，如编辑功能的数据回显
		success: function (layero, index) {
			if(id==0){
				return;
			}
			var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：
			for(var i=0;i<carList.length;i++){
				if(carList[i].carId==id){
					iframeWin.editInit(carList[i]);
					break;
				}
			}
		},
		end:function (){
			console.log("end");
		}
	});
}

function deleteCar(carId){
	var msg = carList[carId];
	/*layer.confirm(msg,{icon: 3,title: '提示'},function(index){
		console.log("确认删除");
		layer.close(index);
	},function(index){
		console.log("取消删除");
		layer.close(index);
	});*/
	for(var i=0;i<carList.length;i++){
		if(carList[i].carId==carId){
			carList[i].carId = 0;
			break;
		}
	}
	var divId = 'car' + carId;
	var inputId = 'carNum' + carId;
	//var parentId = 'cars';
	//var parent = document.getElementById('cars');
	//var child = document.getElementById(divId);
	$("#"+divId).css("display","none");
}
//车牌号合法性校验
function checkCarNum(carNum){
	var result = false;
      if (carNum.length == 7){
        var express = /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$/;
        result = express.test(vehicleNumber);
      }
      return result;
}
