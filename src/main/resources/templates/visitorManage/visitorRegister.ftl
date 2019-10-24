<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>访客登记</title>
    <link rel="stylesheet" href="/layui/css/layui.css">
    <link rel="stylesheet" href="/font-awesome/css/font-awesome.css">
    <style>
        /*.layui-form-label{
            width: 100px;
        }
        .layui-input-block{
            margin-left: 130px;
        }
        .layui-form-radio{
            margin: 6px 0 0 0;
        }*/
        .layui-form-label {
            width: 100px;
        }

        .layui-upload {
            width: 100%;
            float: left;
            margin: 10px 0;
        }

        .layui-upload-list {
            margin: 0;
        }

        .layui-form-select input {
            height: 32px;
            line-height: 32px;
            border-radius: 4px;
        }

        .layui-form-switch {
            margin-top: 4px;
        }

        .button1 {
            width: 44px;
            height: 30px;
            line-height: 30px;
            color: #666666;
            font-size: 12px;
            border: 1px solid #d8d8d8;
            border-radius: 4px;
            background-color: white;
            cursor: pointer;
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

        .layui-table-main {
            width: 100%;
        }
    </style>
</head>
<body>
<#--start:访客登记第一步-->
<form class='layui-form' id='visitorRegisterOne'>
    <div style="width: 100%;float: left;height: 24px;"></div>
    <div style="width: 100%;float: left;height: 24px;">
        <div style="width: 178px;float: left;height: 100%;"></div>
        <div style="width: 24px;float: left;height: 100%;">
            <img src="/css/img/step1blue.png">
        </div>
        <div style="width: 20px;float: left;height: 100%;"></div>
        <div style="width: 490px;float: left;height: 100%;">
            <div class="layui-progress" style="background-color: #d8d8d8;height: 2px;margin-top: 11px;"></div>
        </div>
        <div style="width: 20px;float: left;height: 100%;"></div>
        <div style="width: 24px;float: left;height: 100%;">
            <img src="/css/img/step2no.png">
        </div>
        <div style="width: 20px;float: left;height: 100%;"></div>
        <div style="width: 490px;float: left;height: 100%;">
            <div class="layui-progress" style="background-color: #d8d8d8;height: 2px;margin-top: 11px;"></div>
        </div>
        <div style="width: 20px;float: left;height: 100%;"></div>
        <div style="width: 24px;float: left;height: 100%;">
            <img src="/css/img/step3no.png">
        </div>
    </div>
    <div style="width: 100%;float: left;height: 12px;"></div>
    <div style="width: 100%;float: left;height: 16px;">
        <div style="width: 158px;float: left;height: 100%;"></div>
        <div style="width: 64px;float: left;height: 100%;font-size: 16px;color: #54b5ff;">来访信息</div>
        <div style="width: 490px;float: left;height: 100%;"></div>
        <div style="width: 64px;float: left;height: 100%;font-size: 16px;color: #333333;">权限信息</div>
        <div style="width: 490px;float: left;height: 100%;"></div>
        <div style="width: 64px;float: left;height: 100%;font-size: 16px;color: #333333;">访客信息</div>
    </div>
    <div style="width: 100%;height: 86px;float: left;"></div>
    <div style="width: 100%;float: left;height: 404px;">
        <div style="width: 399px;height: 100%;float: left;"></div>
        <div style="width: 597px;height: 100%;float: left;">
            <div class="layui-form-item" style="margin-bottom: 30px;">
                <label class="layui-form-label"
                       style="line-height: 14px;padding-right: 11px;color: #333333;">来访事由</label>
                <img src="/css/img/required.png" style="margin-top: 11px;float: left;margin-right: 11px;">
                <div class="layui-input-inline" style="width: 420px;">
                    <select id="visitReason" lay-filter="visitReasonFilter" lay-verify="visitReasonRequired"
                            lay-search="">
                        <option value="">请选择</option>
                        <#list visitReasonList as visitReason>
                            <option value="${visitReason.visitReasonId}">${visitReason.visitReasonName}</option>
                        </#list>
                    </select>
                </div>
            </div>
            <div class="layui-form-item" style="margin-bottom: 30px;">
                <label class="layui-form-label"
                       style="line-height: 14px;padding-right: 11px;color: #333333;">被访人部门</label>
                <img src="/css/img/required.png" style="margin-top: 11px;float: left;margin-right: 11px;">
                <div class="layui-input-inline" style="width: 420px;">
                    <select id="depart" lay-filter="departFilter" lay-verify="departRequired" lay-search="">
                        <option value="">请选择</option>
                        <#list departList as depart>
                            <option value="${depart.depart_id}">${depart.depart_name}</option>
                        </#list>
                    </select>
                </div>
            </div>
            <div class="layui-form-item" style="margin-bottom: 30px;">
                <label class="layui-form-label"
                       style="line-height: 14px;padding-right: 11px;color: #333333;">被访人</label>
                <img src="/css/img/required.png" style="margin-top: 11px;float: left;margin-right: 11px;">
                <div class="layui-input-inline" style="width: 420px;">
                    <select id="employee" lay-filter="employeeFilter" lay-verify="employeeRequired" lay-search="">
                        <option value="">请选择</option>
                        <#list faceInfoList as faceInfo>
                            <option value="${faceInfo.face_id},${faceInfo.tel_no},${faceInfo.depart_id}">${faceInfo.name}</option>
                        </#list>
                    </select>
                </div>
            </div>
            <div class='layui-form-item' style="margin-bottom: 30px;">
                <label class='layui-form-label'
                       style="line-height: 14px;padding-right: 11px;color: #333333;">被访人手机号</label>
                <img src="/css/img/required.png" style="margin-top: 11px;float: left;margin-right: 11px;">
                <div class='layui-input-inline' style='width: 420px;'>
                    <input type='text' id='telNum' maxlength="11" placeholder="请输入被访人手机号..."
                           onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)"
                           lay-verify='telNumRequired' class='layui-input'
                           style="height: 32px;line-height: 32px;border-radius: 4px;">
                </div>
            </div>
            <div class='layui-form-item' style="margin-bottom: 30px;">
                <label class='layui-form-label'
                       style="line-height: 14px;padding-right: 11px;color: #333333;">到访时间</label>
                <img src="/css/img/required.png" style="margin-top: 11px;float: left;margin-right: 11px;">
                <div class='layui-input-inline' style='width: 200px;margin-right: 0'>
                    <input type='text' id="startTime" class='layui-input'
                           style="height: 32px;line-height: 32px;border-radius: 4px;">
                </div>
                <div class='layui-input-inline' style='width: 10px;line-height: 32px;margin: 0 5px'>
                    <img src="/css/img/-.png"/>
                </div>
                <div class='layui-input-inline' style='width: 200px;'>
                    <input type='text' id="endTime" class='layui-input'
                           style="height: 32px;line-height: 32px;border-radius: 4px;">
                </div>
            </div>
            <div class="layui-form-item" pane="" style="margin-bottom: 30px;">
                <label class="layui-form-label"
                       style="line-height: 14px;padding-right: 32px;color: #333333;">多人来访</label>
                <div class="layui-input-inline">
                    <input type="checkbox" lay-skin="switch" lay-filter="batchRegister" id="batchRegisterSwitch">
                </div>
            </div>
            <div class='layui-form-item' id="batchNameDiv" style="display: none;">
                <label class='layui-form-label'
                       style="line-height: 14px;padding-right: 11px;color: #333333;">来访主题</label>
                <img src="/css/img/required.png" style="margin-top: 11px;float: left;margin-right: 11px;">
                <div class='layui-input-inline' style='width: 420px;'>
                    <input type='text' id="batchName" class='layui-input' placeholder="请输入多人来访主题名称..."
                           style="height: 32px;line-height: 32px;border-radius: 4px;">
                </div>
            </div>
        </div>
    </div>
    <div style="position: fixed; bottom:20px;right:56px">
        <button type='button' lay-submit='' lay-filter='visitorRegisterOneFilter' class="button2">下一步</button>
    </div>
</form>
<#--end:访客登记第一步-->
<#--start:访客登记第二步-->
<form class='layui-form' id='visitorRegisterTwo' style="display: none;">
    <div style="width: 100%;float: left;height: 24px;"></div>
    <div style="width: 100%;float: left;height: 24px;">
        <div style="width: 178px;float: left;height: 100%;"></div>
        <div style="width: 24px;float: left;height: 100%;">
            <img src="/css/img/step1blue.png">
        </div>
        <div style="width: 20px;float: left;height: 100%;"></div>
        <div style="width: 490px;float: left;height: 100%;">
            <div class="layui-progress" style="background-color: #54b5ff;height: 2px;margin-top: 11px;"></div>
        </div>
        <div style="width: 20px;float: left;height: 100%;"></div>
        <div style="width: 24px;float: left;height: 100%;">
            <img src="/css/img/step2blue.png">
        </div>
        <div style="width: 20px;float: left;height: 100%;"></div>
        <div style="width: 490px;float: left;height: 100%;">
            <div class="layui-progress" style="background-color: #d8d8d8;height: 2px;margin-top: 11px;"></div>
        </div>
        <div style="width: 20px;float: left;height: 100%;"></div>
        <div style="width: 24px;float: left;height: 100%;">
            <img src="/css/img/step3no.png">
        </div>
    </div>
    <div style="width: 100%;float: left;height: 12px;"></div>
    <div style="width: 100%;float: left;height: 16px;">
        <div style="width: 158px;float: left;height: 100%;"></div>
        <div style="width: 64px;float: left;height: 100%;font-size: 16px;color: #54b5ff;">来访信息</div>
        <div style="width: 490px;float: left;height: 100%;"></div>
        <div style="width: 64px;float: left;height: 100%;font-size: 16px;color: #54b5ff;">权限信息</div>
        <div style="width: 490px;float: left;height: 100%;"></div>
        <div style="width: 64px;float: left;height: 100%;font-size: 16px;color: #333333;">访客信息</div>
    </div>
    <div style="width: 100%;float: left;height: 28px;"></div>
    <div style="width: 90%;float: left;margin-left: 5%;box-sizing: border-box;">
        <table class="layui-table" id="deviceTable"></table>
    </div>
    <div style="width: 100%;float: left;height: 75px;"></div>
    <div style="position: fixed; bottom:0px;width:100%;background-color:white;height:66px;">
    	<div style="position: fixed; bottom:20px;right:56px">
        <button class='button2' type='button' onclick="backVisitorRegisterOne();" >上一步</button>
        <button class='button2' type='button' lay-submit='' lay-filter='visitorRegisterTwoFilter'
                style="margin-left: 10px;">下一步
        </button>
        </div>
    </div>
    
</form>
<#--end:访客登记第二步-->
<#--引入访客登记第三步-->
<#include "/visitorManage/visitorRegisterStep3.ftl">
</body>
<script type="text/javascript" src="/js/jquery.min.js"></script>
<script type="text/javascript" src="/layui/layui.js"></script>
<script type="text/javascript" src="/js/photoJS.js"></script>
<script type="text/javascript" src="/js/visitorRegisterSetp3.js"></script>
<script type="text/javascript" src="/js/ajaxFilter.js"></script>
<script>
    /*
    *以下全局变量均在visitorRegisterSetp3.js中有使用
    */
    var batId = 0;//批量Id
    var batchRegister = false;//是否批量登记
    var onlyStep3 = false;//仅有第三步的标识位,访客登记三步都有,所以是false
    var editFaceId = "0";//编辑的faceId,访客登记为新增所以是0
    var editRecId = '0';//编辑的recId,是新增所以是0
    var editBookId = '0';//编辑的bookId,是新增所以是0
    layui.use(['table', 'layer', 'form', 'laydate'], function () {
        var $ = layui.$;
        var table = layui.table;
        var layer = layui.layer;
        var form = layui.form;
        var laydate = layui.laydate;
        var contentHeight = document.documentElement.clientHeight - 154;
        $("#content").css("height", contentHeight);
        //监听第一步的"批量登记"按钮开关
        form.on('switch(batchRegister)', function (data) {
            if (this.checked) {//若开,则显示批量名称输入框,且设置其为必填,同时把"是否批量登记"的全局变量更改为true
                $("#batchNameDiv").show();
                $("#visitorNumDiv").show();
                $("#batchName").attr("lay-verify", "batchNameRequired|batchName");
                batchRegister = true;
            } else {//若关,则隐藏批量名称输入框,且取消必填,同时把"是否批量登记"的全局变量更改为false
                $("#batchNameDiv").hide();
                $("#visitorNumDiv").hide();
                $("#batchName").val("").attr("lay-verify", "");
                batchRegister = false;
            }
        });

        //表单验证
        form.verify({
            nameRequired: function (value) {
                if (value == "") {
                    return "请输入访客姓名";
                }
            },
            visitReasonRequired: function (value) {
                if (value == "") {
                    return '请选择来访事由';
                }
            },
            departRequired: function (value) {
                if (value == "") {
                    return '请选择被访人所在部门';
                }
            },
            employeeRequired: function (value) {
                if (value == "") {
                    return '请选择被访人';
                }
            },
            IDCardNum: function (value) {
                if (value != "" && !checkIDCard(value)) {
                    return '请输入合法的身份证号码';
                }
            },
            telNumRequired: function (value) {
                reg = /0?(13|14|15|16|17|18|19)[0-9]{9}/;
                if (!reg.test(value)) {
                    return '请输入合法的手机号码';
                }
            },
            telNum: function (value) {
                if (value != "") {
                    reg = /0?(13|14|15|16|17|18|19)[0-9]{9}/;
                    if (!reg.test(value)) {
                        return '请输入合法的手机号码';
                    }
                }
            },
            batchNameRequired: function (value) {
                if (value == "") {
                    return "多人来访请输入来访主题";
                }
            }
            , batchName: function (value, item) {
                if (!new RegExp("^$|[^`~!@#$%^&*()_+<>?:\"{},.\\/\\\\;'[\\]·！#￥（——）：；“”‘、，|《。》？、【】[\\]]+$").test(value)) {
                    return '来访主题不能有特殊字符';
                }
            }
            , name: function (value, item) {
                if (!new RegExp("^[^`~!@#$%^&*()_+<>?:\"{},.\\/\\\\;'[\\]·！#￥（——）：；“”‘、，|《。》？、【】[\\]]+$").test(value)) {
                    return '姓名不能有特殊字符';
                }
            }
            , addr: function (value, item) {
                if (!new RegExp("^$|[^`~!@#$%^&*()_+<>?:\"{},.\\/\\\\;'[\\]·！#￥（——）：；“”‘、，|《。》？、【】[\\]]+$").test(value)) {
                    return '住址不能有特殊字符';
                }
            }
            , nickName: function (value, item) {
                if (!new RegExp("^$|[^`~!@#$%^&*()_+<>?:\"{},.\\/\\\\;'[\\]·！#￥（——）：；“”‘、，|《。》？、【】[\\]]+$").test(value)) {
                    return '昵称不能有特殊字符';
                }
            }
            , position: function (value, item) {
                if (!new RegExp("^$|[^`~!@#$%^&*()_+<>?:\"{},.\\/\\\\;'[\\]·！#￥（——）：；“”‘、，|《。》？、【】[\\]]+$").test(value)) {
                    return '职位不能有特殊字符';
                }
            }
            , visitorCompanyName: function (value, item) {
                if (!new RegExp("^$|[^`~!@#$%^&*()_+<>?:\"{},.\\/\\\\;'[\\]·！#￥（——）：；“”‘、，|《。》？、【】[\\]]+$").test(value)) {
                    return '公司名称不能有特殊字符';
                }
            }
        });

        var nowDate = new Date();
        nowDate = Format(nowDate, "yyyy-MM-dd HH:mm:ss");
        //开始时间默认为当前时间
        laydate.render({
            elem: '#startTime' //指定元素
            , type: 'datetime'
            , value: nowDate
        });

        //结束时间默认为当天18点
        var endTime = new Date();
        endTime.setHours(18);
        endTime.setMinutes(0);
        endTime.setSeconds(0);
        laydate.render({
            elem: '#endTime' //指定元素
            , type: 'datetime'
            , value: endTime
        });

        //第一步->第二步事件监听
        form.on('submit(visitorRegisterOneFilter)', function (obj) {
            //验证时间
            var startTime = $("#startTime").val();
            var endTime = $("#endTime").val();
            var date = new Date();
            date.setDate(date.getDate() + 7);
            date = Format(date, "yyyy-MM-dd HH:mm:ss");
            if (startTime < nowDate) {
                layer.alert('访客到访时间不得早于登记时间', {icon: 0, title: "提示"}, function (index) {
                    layer.close(index);
                });
            } else if (endTime > date) {
                layer.alert('访客来访有限期限应在一周以内，请重新设置到访时间!', {icon: 0, title: "提示"}, function (index) {
                    layer.close(index);
                });
            } else if (startTime > endTime) {
                layer.alert('离开时间必须晚于到访时间', {icon: 0, title: "提示"}, function (index) {
                    layer.close(index);
                });
            } else {//验证时间通过
                var employee = $("#employee").val();
                var valArr = employee.split(",");
                var face_id = valArr[0];
                var param = {
                    face_id: face_id,
                    attribute: 0
                };
                //默认查询被访人的设备权限
                table.render({
                    elem: '#deviceTable'
                    , url: '/employeeManage/queryDevice' //数据接口
                    , where: param
                    
                    , cols: [
                        [ //表头
                            {type: 'checkbox'}
                            , {field: 'term_name', title: '设备名称'}
                            , {field: 'depart_name', title: '所属部门'}
                        ]
                    ]
                });
                $("#visitorRegisterOne").hide();
                $("#visitorRegisterTwo").show();
            }
        });

        //第二步->第三步事件监听
        form.on('submit(visitorRegisterTwoFilter)', function (obj) {
            if (batchRegister) {//若是批量登记,则需要添加批量
                var visitReason = $("#visitReason").val();
                var visitReasonName = $("#visitReason").next().find(".layui-input").val();
                var employee = $("#employee").val();
                var valArr = employee.split(",");
                var face_id = valArr[0];
                var startTime = $("#startTime").val();
                var endTime = $("#endTime").val();
                var batchName = $("#batchName").val();
                var checkStatus = table.checkStatus('deviceTable')
                    , deviceList = checkStatus.data;
                var batVisitor = {
                    bat_id: batId,
                    employee_id: face_id,
                    reason_id: visitReason,
                    title: batchName,
                    content: visitReasonName,
                    visit_time: startTime,
                    expire_time: endTime
                };
                var addBatParam = {
                    batVisitor: batVisitor,
                    deviceList: deviceList
                };
                $.ajax({
                    url: "/visitorManage/addBatVisitor",
                    type: "post",
                    data: JSON.stringify(addBatParam),
                    contentType: 'application/json',
                    async: false,
                    traditional: true,
                    success: function (data) {
                        if (data != "0") {//后台返回批量id,需赋值给全局变量
                            batId = data;
                        } else {
                            layer.alert("添加多人来访失败,请联系管理员", {icon: 2, title: "提示"}, function (index) {
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
            } else {//若不是批量登记,则修改第三步的样式
                $("#hasRegistered").hide();
                $("#registerDiv").css("margin-left", '24%');
                $("#suff").css("width", "89.5px");
            }
            $("#visitorRegisterTwo").hide();
            $("#visitorRegisterThree").show();
            if (ifIE()) {//IE浏览器开启读证监听
                t1 = window.setInterval("readIDCard()", 1000);
            } else {//非IE隐藏刷证自动保存的开关
                $("#message").text("(提示:当前使用浏览器非IE,如需读身份证请用IE浏览器并连接读卡器!)");
            }
            //把第一步的批量开关禁用
            $("#batchRegisterSwitch").attr("disabled", "true").addClass("layui-disabled");
        });

        form.on('select(visitReasonFilter)', function (obj) {
            $("#ifWriteFlag").val("1");
        });

        //被访人下拉框事件
        form.on('select(employeeFilter)', function (obj) {
            $("#ifWriteFlag").val("1");
            var valArr = obj.value.split(",");
            var telNum = valArr[1];
            var departId = valArr[2];
            $("#telNum").val(telNum);
            $("#depart").val(departId);
            form.render('select');
        });

        //部门下拉框事件
        form.on('select(departFilter)', function (obj) {
            $("#ifWriteFlag").val("1");
            var departId = obj.value;
            var index = layer.load(1, {
                shade: [0.1, '#fff'] //0.1透明度的白色背景
            });
            var param = {
                depart_id: departId,
                attribute: 0
            };
            $.ajax({
                url: "/employeeManage/selectByDepart",
                type: "post",
                data: param,
                async: true,
                success: function (data) {
                    $("#employee").empty();
                    $("#telNum").val("");
                    var jsonData = $.parseJSON(data);
                    var count = jsonData.count;
                    if (count != 0) {
                        var empList = jsonData.data;
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
                    } else {
                        layer.alert("该部门无人员", {icon: 0, title: "提示"}, function (index) {
                            layer.close(index);
                        });
                    }
                    form.render('select');
                    layer.close(index);
                }
            });
        });
    });

    function backVisitorRegisterOne() {
        $("#visitorRegisterOne").show();
        $("#visitorRegisterTwo").hide();
    }

    function backVisitorRegisterTwo() {
        $("#visitorRegisterTwo").show();
        $("#visitorRegisterThree").hide();
        clearInterval(t1);
    }

    function Format(now, mask) {
        var d = now;
        var zeroize = function (value, length) {
            if (!length) length = 2;
            value = String(value);
            for (var i = 0, zeros = ''; i < (length - value.length); i++) {
                zeros += '0';
            }
            return zeros + value;
        };

        return mask.replace(/"[^"]*"|'[^']*'|\b(?:d{1,4}|m{1,4}|yy(?:yy)?|([hHMstT])\1?|[lLZ])\b/g, function ($0) {
            switch ($0) {
                case 'd':
                    return d.getDate();
                case 'dd':
                    return zeroize(d.getDate());
                case 'ddd':
                    return ['Sun', 'Mon', 'Tue', 'Wed', 'Thr', 'Fri', 'Sat'][d.getDay()];
                case 'dddd':
                    return ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'][d.getDay()];
                case 'M':
                    return d.getMonth() + 1;
                case 'MM':
                    return zeroize(d.getMonth() + 1);
                case 'MMM':
                    return ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'][d.getMonth()];
                case 'MMMM':
                    return ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'][d.getMonth()];
                case 'yy':
                    return String(d.getFullYear()).substr(2);
                case 'yyyy':
                    return d.getFullYear();
                case 'h':
                    return d.getHours() % 12 || 12;
                case 'hh':
                    return zeroize(d.getHours() % 12 || 12);
                case 'H':
                    return d.getHours();
                case 'HH':
                    return zeroize(d.getHours());
                case 'm':
                    return d.getMinutes();
                case 'mm':
                    return zeroize(d.getMinutes());
                case 's':
                    return d.getSeconds();
                case 'ss':
                    return zeroize(d.getSeconds());
                case 'l':
                    return zeroize(d.getMilliseconds(), 3);
                case 'L':
                    var m = d.getMilliseconds();
                    if (m > 99) m = Math.round(m / 10);
                    return zeroize(m);
                case 'tt':
                    return d.getHours() < 12 ? 'am' : 'pm';
                case 'TT':
                    return d.getHours() < 12 ? 'AM' : 'PM';
                case 'Z':
                    return d.toUTCString().match(/[A-Z]+$/);
                // Return quoted strings with the surrounding quotes removed
                default:
                    return $0.substr(1, $0.length - 2);
            }
        });
    };
    window.onresize = function () {
        var contentHeight = document.documentElement.clientHeight - 154;
        $("#content").css("height", contentHeight);
    };
</script>
</html>