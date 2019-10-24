<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>业务参数</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <base href="/"/>
    <link rel="stylesheet" href="/layui/css/layui.css">
    <style>
        .layui-form-radio {
            padding-right: 35px;
        }

        .layui-form-radio div {
            color: #333333;
        }

        .layui-form-radio i {
            margin-right: 10px;
        }

        .layui-slider-input {
            top: 12px;
        }
    </style>
</head>
<body>
<div class="layui-body" style="left: 11.5%;width:88.5%;">
    <form class="layui-form">
        <div id="content" style="overflow-y: scroll;width:100%;">
            <div style="width: 2.95%;height:100%;float: left;"></div>
            <div style="width: 97.05%;height: 100%;float: left;">
                <div style="width: 100%;height: 40px;float: left;"></div>
                <fieldset class="layui-elem-field layui-field-title" style="margin: 0;float: left;width: 100%;">
                    <legend style="color: #999999;font-size: 16px;margin-left: 0;padding-left: 0;padding-right: 20px;">
                        访客来访事由设置
                    </legend>
                </fieldset>
                <div style="width: 100%;height: 30px;float: left;"></div>
                <div id="visitReasonDiv">
                    <#list visitReasonList as visitReason>
                        <#if visitReason.visitReasonName == "参会">
                            <div class="layui-form-item" style="margin-left: 4.7%;position: relative;">
                            <div class="layui-input-inline" style="width: 460px;margin-right: 14px;">
                            <input type="text" maxlength="60" id="${visitReason.visitReasonId}" value="${visitReason.visitReasonName}" class="layui-input layui-disabled visitReason" disabled style="height: 32px;line-height: 32px;border-radius: 4px;padding-left: 14px;">
                            </div>
                            </div>
                        </#if>
                        <#if visitReason.visitReasonName != "参会">
                            <div class="layui-form-item" style="margin-left: 4.7%;position: relative;">
                            <div class="layui-input-inline" style="width: 460px;margin-right: 14px;">
                            <input type="text" maxlength="60" lay-verify="visitReasonRequired" id="${visitReason.visitReasonId}" value="${visitReason.visitReasonName}" class="layui-input visitReason" maxlength="80" style="height: 32px;line-height: 32px;border-radius: 4px;padding-left: 14px;">
                            </div>
                            <button onmouseover="changeImg(this,2)" onmouseout="changeImg(this,1)"
                                    onmousedown="changeImg(this,3)" onmouseup="changeImg(this,2)" type="button"
                                    onclick="removeVisitReason(this)"
                                    style="height: 32px;line-height: 32px;padding: 0 16px;border: none;background-color: white;cursor: pointer;">
                                <img imgName="delete" src="/css/img/delete1.png"/></button>
                            </div>
                        </#if>
                    </#list>
                </div>
                <button onmouseover="changeImg(this,2)" onmouseout="changeImg(this,1)" onmousedown="changeImg(this,3)"
                        onmouseup="changeImg(this,2)" class="layui-btn" type="button" onclick="addVisitReason();"
                        style="margin-left: 4.7%;border: none;height: 40px;line-height: 40px;background-color: white;color: #333333;padding-left: 29px;padding-right: 0;position: relative;">
                    <img style="position: absolute;left: 0;top: 12.5px;" imgName="create" src="/css/img/create1.png"/>新建
                </button>
                <div style="width: 100%;height: 50px;float: left;"></div>
                <fieldset class="layui-elem-field layui-field-title" style="margin: 0;float: left;width: 100%;">
                    <legend style="color: #999999;font-size: 16px;margin-left: 0;padding-left: 0;padding-right: 20px;">
                        员工信息录入设置
                    </legend>
                </fieldset>
                <div style="width: 100%;height: 46px;float: left;"></div>
                <#list employeeInformationList as employeeInformation>
                    <#if employeeInformation.param_key == "face_info_input_mod">
                        <div class="layui-form-item" style="margin-left: 3.4%;">
                        <label class="layui-form-label" style="width: 150px;padding-right: 50px;color: #333333">员工身份信息录入方式</label>
                        <div class="layui-input-block">
                        <#if employeeInformation.param_value == "0">
                            <input type="radio" param_key="${employeeInformation.param_key}" param_name="${employeeInformation.param_name}" name="inputFunction" value="0" title="仅读身份证" checked>
                            <input type="radio" param_key="${employeeInformation.param_key}" param_name="${employeeInformation.param_name}" name="inputFunction" value="1" title="读身份证或手动录入">
                        </#if>
                        <#if employeeInformation.param_value == "1">
                            <input type="radio" param_key="${employeeInformation.param_key}" param_name="${employeeInformation.param_name}" name="inputFunction" value="0" title="仅读身份证">
                            <input type="radio" param_key="${employeeInformation.param_key}" param_name="${employeeInformation.param_name}" name="inputFunction" value="1" title="读身份证或手动录入" checked>
                        </#if>
                        </div>
                        </div>
                    </#if>
                    <#if employeeInformation.param_key == "is_need_employee_card">
                        <div class="layui-form-item" style="margin-left: 3.4%;">
                        <label class="layui-form-label"
                               style="width: 150px;padding-right: 50px;color: #333333">员工工牌号必填</label>
                        <div class="layui-input-block">
                        <#if employeeInformation.param_value == "0">
                            <input param_key="${employeeInformation.param_key}" param_name="${employeeInformation.param_name}" lay-skin="switch" type="checkbox" name="workCardNum" title="必填" lay-text="ON|OFF">
                        </#if>
                        <#if employeeInformation.param_value == "1">
                            <input param_key="${employeeInformation.param_key}" param_name="${employeeInformation.param_name}" lay-skin="switch" type="checkbox" name="workCardNum" title="必填" checked lay-text="ON|OFF">
                        </#if>
                        </div>
                        </div>
                    </#if>
                    <#if employeeInformation.param_key == "life_photo_max_size">
                        <div class="layui-form-item" style="margin-left: 3.4%;">
                        <label class="layui-form-label" style="width: 150px;padding-right: 50px;color: #333333">上传照片最大文件大小(1~1024)KB</label>
                        <div id="photoSizeSlide" class="demo-slider"
                             style="width: 300px;padding-top: 27px;float: left;"></div>
                        <div style="float: left;margin-left: 10px;padding-top: 19px;width: 100px;">KB</div>
                        <div class="layui-input-block" style="display: none;">
                        <input id="photoMaxSize" type="text" style="width: 300px;height: 32px;line-height: 32px;border-radius: 4px;" lay-verify='photoKBRequired' param_key="${employeeInformation.param_key}" param_name="${employeeInformation.param_name}"
                    name="photoWidth" value="${employeeInformation.param_value}" class="layui-input" maxlength="4" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)">
                        </div>
                        </div>
                    </#if>
                </#list>
                    <div style="width: 100%;height: 50px;float: left;"></div>
                    <fieldset class="layui-elem-field layui-field-title" style="margin: 0;float: left;width: 100%;">
                        <legend style="color: #999999;font-size: 16px;margin-left: 0;padding-left: 0;padding-right: 20px;">
                            访客现场补登信息设置
                        </legend>
                    </fieldset>
                    <div style="width: 100%;height: 40px;float: left;"></div>
                <#list visitorRegisterList as visitorRegister>
                    <div class="layui-form-item" style="margin-left: 3.4%">
                    <label class="layui-form-label" style="width: 230px;padding-right: 50px;color: #333333">${visitorRegister.param_name}</label>
                    <div class="layui-input-block">
                    <#if visitorRegister.param_value == "1">
                        <input type="checkbox" lay-skin="switch" value="${visitorRegister.param_key}"
                    name="visitorRegister" title="${visitorRegister.param_name}" checked lay-text="ON|OFF">
                    </#if>
                    <#if visitorRegister.param_value == "0">
                        <input type="checkbox" lay-skin="switch" value="${visitorRegister.param_key}"
                    name="visitorRegister" title="${visitorRegister.param_name}" lay-text="ON|OFF">
                    </#if>
                    </div>
                    </div>
                </#list>

            </div>
        </div>
        <button lay-submit='' lay-filter="save"
                style="position: absolute;left: 7.7%;bottom:10px;height: 34px;line-height: 34px;font-size: 16px;padding: 0 39px;z-index: 99"
                class="layui-btn" type="button">保存
        </button>
    </form>
</div>
<script type="text/javascript" src="/js/jquery.min.js"></script>
<script type="text/javascript" src="/layui/layui.js" charset="utf-8"></script>
<script type="text/javascript" src="/js/ajaxFilter.js"></script>
<script type="text/javascript">
    $(function () {
        window.parent.$("#toBusinessParamLi").siblings("li").removeClass("layui-this");
        window.parent.$("#toBusinessParamLi").addClass("layui-this");
    });

    function changeImg(obj, status) {
        var imgName = $(obj).find("img").attr("imgName");
        $(obj).find("img").attr("src", "/css/img/" + imgName + status + ".png");
    }

    var removeVisitReasonArr = new Array();//临时存储将要删除的来访事由
    layui.use(['layer', 'form', 'slider'], function () {
        var form = layui.form;
        var layer = layui.layer;
        var slider = layui.slider;
        var contentHeight = document.documentElement.clientHeight - 64;
        $("#content").css("height", contentHeight);
        form.verify({
            visitReasonRequired: function (value) {
                if (value == "") {
                    return "来访事由不能为空";
                } else {

                }
            },
            photoKBRequired: function (value) {
                if (value <= 0 || value > 1024) {
                    return "上传照片大小范围应在1~1024KB";
                }
            }
        });
        form.on('submit(save)', function (obj) {
            //定义添加和修改的来访事由数组
            var addOrUpdateList = [];
            //遍历来访事由
            $(".visitReason").each(function () {
                //获取来访事由名称
                var visitReasonName = $(this).val();
                if (visitReasonName != "") {
                    //定义来访事由的对象
                    var visitReason = {};
                    //获取来访事由id
                    var visitReasonId = $(this).attr("id");
                    if (visitReasonId == undefined) {//若没有id则默认为0
                        visitReasonId = "0"
                    }
                    //封装对象并添加到数组中
                    visitReason.visitReasonId = visitReasonId;
                    visitReason.visitReasonName = visitReasonName;
                    addOrUpdateList.push(visitReason);
                }
            });
            //定义来访事由传入后台的大对象
            var saveVisitReasonParam = {};
            saveVisitReasonParam.addOrUpdateList = addOrUpdateList;
            saveVisitReasonParam.deleteList = removeVisitReasonArr;
            $.ajax({
                url: "/businessParam/saveVisitParam",
                type: "post",
                data: JSON.stringify(saveVisitReasonParam),
                contentType: 'application/json',
                async: false,
                success: function (data) {
                    if (data == "0") {//保存来访事由成功,之后保存员工参数
                        //照片最大KB
                        var photo = $("input[name='photoWidth']").val().replace("KB", "");
                        var photoKey = $("input[name='photoWidth']").attr("param_key");
                        var photoName = $("input[name='photoWidth']").attr("param_name");
                        //员工录入方式
                        var inputFunction = $("input[name='inputFunction']:checked").val();
                        var inputFunctionKey = $("input[name='inputFunction']:checked").attr("param_key");
                        var inputFunctionName = $("input[name='inputFunction']:checked").attr("param_name");
                        //员工号是否必填
                        var workCardNum = $("input[name='workCardNum']").is(":checked");
                        if (workCardNum) {
                            workCardNum = "1";
                        } else {
                            workCardNum = "0";
                        }
                        var workCardNumKey = $("input[name='workCardNum']").attr("param_key");
                        var workCardNumName = $("input[name='workCardNum']").attr("param_name");
                        //把参数封装为大对象,方便传入后台
                        var paramConfigInfoList = [];
                        var inputFunctionObj = {};
                        var workCardNumObj = {};
                        var photoObj = {};
                        inputFunctionObj.param_key = inputFunctionKey;
                        inputFunctionObj.param_value = inputFunction;
                        inputFunctionObj.param_name = inputFunctionName;
                        workCardNumObj.param_key = workCardNumKey;
                        workCardNumObj.param_value = workCardNum;
                        workCardNumObj.param_name = workCardNumName;
                        photoObj.param_key = photoKey;
                        photoObj.param_value = photo;
                        photoObj.param_name = photoName;
                        paramConfigInfoList.push(inputFunctionObj);
                        paramConfigInfoList.push(workCardNumObj);
                        paramConfigInfoList.push(photoObj);
                        $.ajax({
                            url: "/businessParam/saveParamConfigInfo",
                            type: "post",
                            data: JSON.stringify(paramConfigInfoList),
                            contentType: 'application/json',
                            async: false,
                            success: function (data) {
                                if (data == "0") {//保存员工参数成功
                                    var paramConfigInfoList = [];
                                    //遍历访客补登参数
                                    $("input[name='visitorRegister']").each(function () {
                                        var visitorRegister = {};
                                        var ischecked = $(this).is(":checked");
                                        if (ischecked) {
                                            ischecked = "1";
                                        } else {
                                            ischecked = "0";
                                        }
                                        var visitorRegisterKey = $(this).val();
                                        var visitorRegisterName = $(this).attr("title");
                                        visitorRegister.param_key = visitorRegisterKey;
                                        visitorRegister.param_value = ischecked;
                                        visitorRegister.param_name = visitorRegisterName;
                                        paramConfigInfoList.push(visitorRegister);
                                    });
                                    $.ajax({
                                        url: "/businessParam/saveParamConfigInfo",
                                        type: "post",
                                        data: JSON.stringify(paramConfigInfoList),
                                        contentType: 'application/json',
                                        async: false,
                                        success: function (data) {
                                            if (data == "0") {
                                                layer.alert('参数保存成功', {
                                                    icon: 1,
                                                    title: "提示",
                                                    shadeClose: false,
                                                    shade: [0.2, '#000000'],
                                                    end: function () {
                                                        window.location.reload();
                                                    }
                                                });
                                            } else {
                                                layer.alert("参数保存失败", {icon: 2, title: "提示"}, function (index) {
                                                    layer.close(index);
                                                });
                                            }
                                        },
                                        error: function () {
                                            layer.alert('服务器错误,请联系系统管理员', {icon: 2, title: "提示"}, function (index) {
                                                layer.close(index);
                                            });
                                        }
                                    });
                                } else {
                                    layer.alert("保存失败", {icon: 2, title: "提示"}, function (index) {
                                        layer.close(index);
                                    });
                                }
                            },
                            error: function () {
                                layer.alert('服务器错误,请联系管理员', {icon: 2, title: "提示"}, function (index) {
                                    layer.close(index);
                                });
                            }
                        });
                    } else {//保存来访事由失败
                        if (data == "2") {//返回结果为2说明有重复的名称
                            layer.alert("业务参数名称重复,保存失败", {icon: 2, title: "提示"}, function (index) {
                                layer.close(index);
                            });
                        } else {
                            layer.alert("保存失败", {icon: 2, title: "提示"}, function (index) {
                                layer.close(index);
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
        });

        //初始化滑块
        slider.render({
            elem: '#photoSizeSlide'
            , value: $("#photoMaxSize").val()//赋初始值
            , input: true //开启输入功能
            , min: 1 //最小值
            , max: 1024 //最大值
            , setTips: function (value) {//设置显示单位
                return value + 'KB';
            }
            , change: function (value) {//改变值时的事件
                $("#photoMaxSize").val(value);
            }
        });
    });

    //新增来访事由
    function addVisitReason() {
        var appendHTML =
            "<div class=\"layui-form-item\" style=\"margin-left: 4.7%;position: relative;\">\n" +
            "                            <div class=\"layui-input-inline\" style=\"width: 460px;margin-right: 14px;\">\n" +
            "                                <input type=\"text\" maxlength=\"60\" lay-verify=\"visitReasonRequired\" class=\"layui-input visitReason\" maxlength=\"80\" style=\"height: 32px;line-height: 32px;border-radius: 4px;padding-left: 14px;\">\n" +
            "                            </div>\n" +
            "                            <button onmouseover=\"changeImg(this,2)\" onmouseout=\"changeImg(this,1)\" onmousedown=\"changeImg(this,3)\" onmouseup=\"changeImg(this,2)\" type=\"button\" onclick=\"removeVisitReason(this)\" style=\"height: 32px;line-height: 32px;padding: 0 16px;border: none;background-color: white;cursor: pointer;\"><img imgName=\"delete\" src=\"/css/img/delete1.png\"/></button>\n" +
            "                        </div>";
        $("#visitReasonDiv").append(appendHTML);
    }

    //删除来访事由(非即时删除,是临时保存到数组中)
    function removeVisitReason(obj) {
        var visitReasonId = $(obj).parent().find(".layui-input").attr("id");
        if (visitReasonId != undefined) {
            var delVisitReason = {
                visitReasonId: visitReasonId
            };
            removeVisitReasonArr.push(delVisitReason);
        }
        $(obj).parent().remove();
    }
</script>
</body>
</html>