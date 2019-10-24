<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>编辑非批量访客</title>
    <link rel="stylesheet" href="/layui/css/layui.css">
    <link rel="stylesheet" href="/font-awesome/css/font-awesome.css">
    <style>
        .layui-form-label{
            width: 100px;
        }
        .layui-input-block{
            margin-left: 130px;
        }
        .layui-form-radio{
            margin: 6px 0 0 0;
        }
        .layui-table-main{
            width: 100%;
        }
    </style>
</head>
<body>
<form class='layui-form' id='visitorRegisterOne'>
        <div style="width: 100%;float: left;height: 24px;"></div>
        <div style="width: 100%;float: left;height: 24px;">
            <div style="width: 178px;float: left;height: 100%;"></div>
            <div style="width: 24px;float: left;height: 100%;">
                <img src="/css/img/step1blue.png">
            </div>
            <div style="width: 20px;float: left;height: 100%;"></div>
            <div class="step2Progress" style="width: 196px;float: left;height: 100%;">
                <div class="layui-progress" style="background-color: #d8d8d8;height: 2px;margin-top: 11px;"></div>
            </div>
            <div style="width: 20px;float: left;height: 100%;"></div>
            <div style="width: 24px;float: left;height: 100%;">
                <img src="/css/img/step2no.png">
            </div>
            <div class="step3Nav" style="width: 20px;float: left;height: 100%;"></div>
            <div class="step3Nav" style="width: 196px;float: left;height: 100%;">
                <div class="layui-progress" style="background-color: #d8d8d8;height: 2px;margin-top: 11px;"></div>
            </div>
            <div class="step3Nav" style="width: 20px;float: left;height: 100%;"></div>
            <div class="step3Nav" style="width: 24px;float: left;height: 100%;">
                <img src="/css/img/step3no.png">
            </div>
        </div>
        <div style="width: 100%;float: left;height: 12px;"></div>
        <div style="width: 100%;float: left;height: 16px;">
            <div style="width: 158px;float: left;height: 100%;"></div>
            <div style="width: 64px;float: left;height: 100%;font-size: 16px;color: #54b5ff;">来访信息</div>
            <div class="step2Progress" style="width: 196px;float: left;height: 100%;"></div>
            <div style="width: 64px;float: left;height: 100%;font-size: 16px;color: #333333;">权限信息</div>
            <div class="step3Nav" style="width: 196px;float: left;height: 100%;"></div>
            <div class="step3Nav" style="width: 64px;float: left;height: 100%;font-size: 16px;color: #333333;">访客信息</div>
        </div>
        <div style="width: 100%;height: 86px;float: left;"></div>
        <div style="width: 100%;float: left;height: 404px;">
            <div style="width: 150px;height: 100%;float: left;"></div>
            <div style="width: 597px;height: 100%;float: left;">
                <div class="layui-form-item" style="margin-bottom: 30px;">
                    <label class="layui-form-label" style="line-height: 14px;padding-right: 11px;color: #333333;">来访事由</label>
                    <img src="/css/img/required.png" style="margin-top: 11px;float: left;margin-right: 11px;">
                    <div class="layui-input-inline" style="width: 420px;">
                        <select id="visitReason" lay-filter="visitReasonFilter" lay-verify="visitReasonRequired" lay-search="">
                            <option value="">请选择</option>
                            <#list visitReasonList as visitReason>
                                <#if visitQueryInfo.reason_id?string == visitReason.visitReasonId>
                                    <option value="${visitReason.visitReasonId}" selected>${visitReason.visitReasonName}</option>
                                <#else >
                                    <option value="${visitReason.visitReasonId}">${visitReason.visitReasonName}</option>
                                </#if>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="layui-form-item" style="margin-bottom: 30px;">
                    <label class="layui-form-label" style="line-height: 14px;padding-right: 11px;color: #333333;">被访人部门</label>
                    <img src="/css/img/required.png" style="margin-top: 11px;float: left;margin-right: 11px;">
                    <div class="layui-input-inline" style="width: 420px;">
                        <select id="depart" lay-filter="departFilter" lay-verify="departRequired" lay-search="">
                            <option value="">请选择</option>
                            <#list departList as depart>
                                <#if visitQueryInfo.depart_id == depart.depart_id>
                                    <option value="${depart.depart_id}" selected="">${depart.depart_name}</option>
                                <#else >
                                    <option value="${depart.depart_id}">${depart.depart_name}</option>
                                </#if>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="layui-form-item" style="margin-bottom: 30px;">
                    <label class="layui-form-label" style="line-height: 14px;padding-right: 11px;color: #333333;">被访人</label>
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
                    <label class='layui-form-label' style="line-height: 14px;padding-right: 11px;color: #333333;">被访人手机号</label>
                    <img src="/css/img/required.png" style="margin-top: 11px;float: left;margin-right: 11px;">
                    <div class='layui-input-inline' style='width: 420px;'>
                        <input type='text' id='telNum' onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" lay-verify='telNumRequired' class='layui-input' maxlength="11" style="height: 32px;line-height: 32px;border-radius: 4px;">
                    </div>
                </div>
                <div class='layui-form-item' style="margin-bottom: 30px;">
                    <label class='layui-form-label' style="line-height: 14px;padding-right: 11px;color: #333333;">来访时间</label>
                    <img src="/css/img/required.png" style="margin-top: 11px;float: left;margin-right: 11px;">
                    <div class='layui-input-inline' style='width: 200px;margin-right: 0'>
                        <input type='text' id="startTime" class='layui-input' style="height: 32px;line-height: 32px;border-radius: 4px;">
                    </div>
                    <div class='layui-input-inline' style='width: 10px;line-height: 32px;margin: 0 5px'>
                        -
                    </div>
                    <div class='layui-input-inline' style='width: 200px;'>
                        <input type='text' id="endTime" class='layui-input' style="height: 32px;line-height: 32px;border-radius: 4px;">
                    </div>
                </div>
            </div>
        </div>
        <div style="position: fixed; bottom:20px;right:56px">
            <button type='button' lay-submit='' lay-filter='visitorRegisterOneFilter' class="button2" style="float: right;">下一步</button>
        </div>
</form>
<form class='layui-form' id='visitorRegisterTwo' style="display: none;">
    <div style="width: 100%;float: left;height: 24px;"></div>
    <div style="width: 100%;float: left;height: 24px;">
        <div style="width: 178px;float: left;height: 100%;"></div>
        <div style="width: 24px;float: left;height: 100%;">
            <img src="/css/img/step1blue.png">
        </div>
        <div style="width: 20px;float: left;height: 100%;"></div>
        <div class="step2Progress" style="width: 196px;float: left;height: 100%;">
            <div class="layui-progress" style="background-color: #54b5ff;height: 2px;margin-top: 11px;"></div>
        </div>
        <div style="width: 20px;float: left;height: 100%;"></div>
        <div style="width: 24px;float: left;height: 100%;">
            <img src="/css/img/step2blue.png">
        </div>
        <div class="step3Nav" style="width: 20px;float: left;height: 100%;"></div>
        <div class="step3Nav" style="width: 196px;float: left;height: 100%;">
            <div class="layui-progress" style="background-color: #d8d8d8;height: 2px;margin-top: 11px;"></div>
        </div>
        <div class="step3Nav" style="width: 20px;float: left;height: 100%;"></div>
        <div class="step3Nav" style="width: 24px;float: left;height: 100%;">
            <img src="/css/img/step3no.png">
        </div>
    </div>
    <div style="width: 100%;float: left;height: 12px;"></div>
    <div style="width: 100%;float: left;height: 16px;">
        <div style="width: 158px;float: left;height: 100%;"></div>
        <div style="width: 64px;float: left;height: 100%;font-size: 16px;color: #54b5ff;">来访信息</div>
        <div class="step2Progress" style="width: 196px;float: left;height: 100%;"></div>
        <div style="width: 64px;float: left;height: 100%;font-size: 16px;color: #54b5ff;">权限信息</div>
        <div class="step3Nav" style="width: 196px;float: left;height: 100%;"></div>
        <div class="step3Nav" style="width: 64px;float: left;height: 100%;font-size: 16px;color: #333333;">访客信息</div>
    </div>
    <div style="width: 100%;float: left;height: 28px;"></div>
    <div style="width: 90%;float: left;margin-left: 5%;box-sizing: border-box;">
        <table class="layui-table" id="deviceTable"></table>
    </div>
    <div style="position: fixed; bottom:0px;width:100%;background-color:white;height:66px;">
    <div style="position: fixed; bottom:20px;right:41px">
        <button class='button2' type='button' onclick="backVisitorRegisterOne();">上一步</button>
        <button id="step2Next" class='button2' type='button' lay-submit='' lay-filter='visitorRegisterTwoFilter' style="margin-left: 10px;">下一步</button>
        <button id="step2Save" class='button2' type='button' lay-submit='' lay-filter='step2Save' style="margin-left: 10px;display: none;">保存</button>
    </div>
    </div>
</form>
<#--引入访客登记第三步的页面-->
<#include "/visitorManage/visitorRegisterStep3.ftl">
</body>
<script type="text/javascript" src="/js/jquery.min.js"></script>
<script type="text/javascript" src="/layui/layui.js"></script>
<#--引入照片js-->
<script type="text/javascript" src="/js/photoJS.js"></script>
<#--引入访客登记第三步js-->
<script type="text/javascript" src="/js/visitorRegisterSetp3.js"></script>
<#--ajax拦截-->
<script type="text/javascript" src="/js/ajaxFilter.js"></script>
<script>
    /*
    *以下全局变量均在visitorRegisterSetp3.js中有使用
    */
    var batId = '0';//编辑非批量访客登记,批量Id为0
    var onlyStep3 = false;//仅有第三步的标识位,非批量的访客编辑三步都有,所以是false
    var editFaceId = "${visitQueryInfo.face_id}".replace(',','');//编辑的faceId,超过千位会有分隔符',',因此replace掉
    var editAttribute = "${visitQueryInfo.attribute}";//此人的身份,员工/访客
    var editBookId = "${visitQueryInfo.book_id}".replace(',','');//预约id
    var editRecId = "${visitQueryInfo.rec_id}".replace(',','');//访客记录id
    var employeeId = "${visitQueryInfo.employee_id}".replace(',','');//被访人id
    var startTime = "${visitQueryInfo.visit_time}";//来访的开始时间
    var endTime = "${visitQueryInfo.expire_time}";//来访的结束时间
    $(function () {
        if (editAttribute == 0) {//如果是员工,则只有前两步,此处仅改变"123步的样式"
            $(".step3Nav").hide();
            $(".step2Progress").css("width","456px");
            $("#step2Next").hide();
            $("#step2Save").show();
        }
    });
    layui.use(['table','layer','form','laydate','upload'], function() {
        var $ = layui.$;
        var table = layui.table;
        var layer = layui.layer;
        var form = layui.form;
        var laydate = layui.laydate;
        var contentHeight = document.documentElement.clientHeight-154;
        $("#content").css("height",contentHeight);
        //查询被访人
        selectEmployee();
        function selectEmployee() {
            var departId = $("#depart").val();
            if (departId != "请选择") {
                var index = layer.load(1, {
                    shade: [0.1,'#fff'] //0.1透明度的白色背景
                });
                var param = {
                    depart_id:departId,
                    attribute:0,
                    status:0
                };
                $.ajax({
                    url: "/employeeManage/selectByDepart",
                    type: "post",
                    data: param,
                    async: true,
                    success: function(data) {
                        var jsonData = $.parseJSON(data);
                        var count = jsonData.count;
                        if (count != 0) {//判断返回结果的数量
                            //先置空之前的数据
                            $("#employee").empty();
                            var empList = jsonData.data;
                            var selectEmpHTML = "<option value=''>请选择</option>";
                            //遍历并拼接HTML
                            $.each(empList,function(index,value){
                                var telNum = value.tel_no;
                                var faceId = value.face_id;
                                var name = value.name;
                                var departId = value.depart_id;
                                var value = faceId + "," + telNum + "," + departId;
                                if (faceId == employeeId) {
                                    var optionHTML = "<option value='" + value + "' selected>" + name + "</option>";
                                    $("#telNum").val(telNum);
                                } else {
                                    var optionHTML = "<option value='" + value + "'>" + name + "</option>";
                                }
                                selectEmpHTML = selectEmpHTML + optionHTML;
                            });
                            $("#employee").append(selectEmpHTML);
                        } else {
                            layer.alert("该部门无人员",{icon: 0,title:"提示"},function (index) {
                                layer.close(index);
                            });
                        }
                        form.render('select');
                        layer.close(index);
                    }
                });
            }
        }
        laydate.render({
            elem: '#startTime' //指定元素
            ,type: 'datetime'
            ,value: startTime
        });
        laydate.render({
            elem: '#endTime' //指定元素
            ,type: 'datetime'
            ,value: endTime
        });

        form.on('select(visitReasonFilter)', function(obj) {
            $("#ifWriteFlag").val("1");
        });

        //部门下拉框监听事件
        form.on('select(departFilter)', function(obj) {
            $("#ifWriteFlag").val("1");
            var departId = obj.value;
            if (departId != "请选择") {
                var index = layer.load(1, {
                    shade: [0.1,'#fff'] //0.1透明度的白色背景
                });
                var param = {
                    depart_id:departId,
                    attribute:0,
                    status:0
                };
                $.ajax({
                    url: "/employeeManage/selectByDepart",
                    type: "post",
                    data: param,
                    async: true,
                    success: function(data) {
                        $("#employee").empty();
                        $("#telNum").val("");
                        var jsonData = $.parseJSON(data);
                        var count = jsonData.count;
                        if (count != 0) {
                            var empList = jsonData.data;
                            var selectEmpHTML = "<option value=''>请选择</option>";
                            $.each(empList,function(index,value){
                                var telNum = value.tel_no;
                                var faceId = value.face_id;
                                var name = value.name;
                                var value = faceId + "," + telNum;
                                var optionHTML = "<option value='" + value + "'>" + name + "</option>";
                                selectEmpHTML = selectEmpHTML + optionHTML;
                            });
                            $("#employee").append(selectEmpHTML);
                        } else {
                            layer.alert("该部门无人员",{icon: 0,title:"提示"},function (index) {
                                layer.close(index);
                            });
                        }
                        form.render('select');
                        layer.close(index);
                    }
                });
            }
        });

        //被访人下拉框监听事件
        form.on('select(employeeFilter)', function(obj) {
            $("#ifWriteFlag").val("1");
            var valArr = obj.value.split(",");
            var telNum = valArr[1];
            //填充此被访人的手机号
            $("#telNum").val(telNum);
        });

        form.verify({
            nameRequired: function(value) {
                if (value == "") {
                    return "请输入姓名";
                }
            },
            visitReasonRequired: function(value){
                if(value == ""){
                    return '请选择来访事由';
                }
            },
            departRequired: function(value){
                if(value == ""){
                    return '请选择被访人部门';
                }
            },
            employeeRequired: function(value){
                if(value == ""){
                    return '请选择被访人';
                }
            },
            IDCardNum: function (value) {
                if (value != "" && !checkIDCard(value)) {
                    return '请输入合法的身份证号码';
                } else if (value == "" && editIDCardIfExit) {
                    return '编辑访客不能删除身份证号码,只可做修改';
                }
            },
            IDCardNumRequired: function (value) {
                if (value == "" && editIDCardIfExit) {
                    return '编辑访客不能删除身份证号码,只可做修改';
                } else if (!checkIDCard(value)) {
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
            }
        });

        //第一步点击下一步的事件
        form.on('submit(visitorRegisterOneFilter)', function(obj) {
            //判断时间是否符合业务要求
            var startTime1 = $("#startTime").val();
            var endTime1 = $("#endTime").val();
            var date = new Date();
            date.setDate(date.getDate()+7);
            date = Format(date,"yyyy-MM-dd HH:mm:ss");
            if (startTime1 < startTime) {
                layer.alert('访客到访时间不得早于登记时间',{icon: 0,title:"提示"},function (index) {
                    layer.close(index);
                });
            } else if (endTime1 > date) {
                layer.alert('访客来访有限期限应在一周以内，请重新设置到访时间!',{icon: 0,title:"提示"},function (index) {
                    layer.close(index);
                });
            } else if (startTime1 > endTime1) {
                layer.alert('离开时间必须晚于到访时间',{icon: 0,title:"提示"},function (index) {
                    layer.close(index);
                });
            } else {//时间判断通过
                var param = {};
                var url = "";
                //预约id为0和不为0,查询设备权限的接口不同
                if (editBookId == "0") {
                    param = {
                        face_id:editFaceId,
                        attribute:1,
                        visit_id:editRecId
                    };
                    url = '/employeeManage/queryDevice';
                }
                if (editBookId != "0") {
                    param = {
                        book_id:editBookId
                    };
                    url = '/visitorManage/queryYuYueDevice';
                }
                table.render({
                    elem: '#deviceTable'
                    ,url: url
                    ,where:param
                    ,height:600
                    ,cols: [
                        [ //表头
                            {type: 'checkbox'}
                            ,{field: 'term_name', title: '设备名称'}
                            ,{field: 'depart_name', title: '所属部门'}
                        ]
                    ]
                });
                $("#visitorRegisterOne").hide();
                $("#visitorRegisterTwo").show();
            }
        });

        //第二步中点击下一步的事件
        form.on('submit(visitorRegisterTwoFilter)', function(obj) {
            $("#visitorRegisterTwo").hide();
            $("#visitorRegisterThree").show();
            if (ifIE()) {//是IE
                t1 = window.setInterval("readIDCard()", 1000);
            } else {
                $("#message").text("提示:当前浏览器非IE,仅IE浏览器才能读身份证!");
                $("#onlyBrushCardDiv").hide();
            }
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

    //格式化时间
    function Format(now,mask) {
        var d = now;
        var zeroize = function (value, length)
        {
            if (!length) length = 2;
            value = String(value);
            for (var i = 0, zeros = ''; i < (length - value.length); i++)
            {
                zeros += '0';
            }
            return zeros + value;
        };

        return mask.replace(/"[^"]*"|'[^']*'|\b(?:d{1,4}|m{1,4}|yy(?:yy)?|([hHMstT])\1?|[lLZ])\b/g, function ($0)
        {
            switch ($0)
            {
                case 'd': return d.getDate();
                case 'dd': return zeroize(d.getDate());
                case 'ddd': return ['Sun', 'Mon', 'Tue', 'Wed', 'Thr', 'Fri', 'Sat'][d.getDay()];
                case 'dddd': return ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'][d.getDay()];
                case 'M': return d.getMonth() + 1;
                case 'MM': return zeroize(d.getMonth() + 1);
                case 'MMM': return ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'][d.getMonth()];
                case 'MMMM': return ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'][d.getMonth()];
                case 'yy': return String(d.getFullYear()).substr(2);
                case 'yyyy': return d.getFullYear();
                case 'h': return d.getHours() % 12 || 12;
                case 'hh': return zeroize(d.getHours() % 12 || 12);
                case 'H': return d.getHours();
                case 'HH': return zeroize(d.getHours());
                case 'm': return d.getMinutes();
                case 'mm': return zeroize(d.getMinutes());
                case 's': return d.getSeconds();
                case 'ss': return zeroize(d.getSeconds());
                case 'l': return zeroize(d.getMilliseconds(), 3);
                case 'L': var m = d.getMilliseconds();
                    if (m > 99) m = Math.round(m / 10);
                    return zeroize(m);
                case 'tt': return d.getHours() < 12 ? 'am' : 'pm';
                case 'TT': return d.getHours() < 12 ? 'AM' : 'PM';
                case 'Z': return d.toUTCString().match(/[A-Z]+$/);
                // Return quoted strings with the surrounding quotes removed
                default: return $0.substr(1, $0.length - 2);
            }
        });
    };

    //拖动浏览器窗口时改变高度
    window.onresize=function(){
        var contentHeight = document.documentElement.clientHeight-154;
        $("#content").css("height",contentHeight);
    };
</script>
</html>