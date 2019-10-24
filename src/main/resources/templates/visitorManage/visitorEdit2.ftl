<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>编辑批量访客</title>
    <link rel="stylesheet" href="/layui/css/layui.css">
    <link rel="stylesheet" href="/font-awesome/css/font-awesome.css">
    <style>
        .layui-form-label {
            width: 100px;
        }

        .layui-input-block {
            margin-left: 130px;
        }

        .layui-form-radio {
            margin: 6px 0 0 0;
        }
    </style>
</head>
<body>
<#--引入访客登记第三步的页面-->
<#include "/visitorManage/visitorRegisterStep3.ftl">
</body>
<script type="text/javascript" src="/js/jquery.min.js"></script>
<script type="text/javascript" src="/layui/layui.js"></script>
<#--照片的js-->
<script type="text/javascript" src="/js/photoJS.js"></script>
<#--访客登记第三步的js-->
<script type="text/javascript" src="/js/visitorRegisterSetp3.js"></script>
<#--ajax拦截-->
<script type="text/javascript" src="/js/ajaxFilter.js"></script>
<script>
    /*
    *以下全局变量均在visitorRegisterSetp3.js中有使用
    */
    var onlyStep3 = true;//仅有第三步的标识位,此处为编辑属于批量的某一访客信息,所以仅有第三步
    var editFaceId = "${visitQueryInfo.face_id}".replace(',', '');//编辑的人员id
    var editAttribute = "${visitQueryInfo.attribute}";//编辑的人员身份,因前端有控制不能编辑员工,所以此处必为访客
    var batStartTime = "${visitQueryInfo.visit_time}";//批量来访的开始时间
    var batEndTime = "${visitQueryInfo.expire_time}";//批量来访的结束时间
    layui.use(['table', 'layer', 'form', 'laydate', 'upload'], function () {
        var $ = layui.$;
        var table = layui.table;
        var layer = layui.layer;
        var form = layui.form;
        var contentHeight = document.documentElement.clientHeight - 76;
        $("#content").css("height", contentHeight);
        form.verify({
            nameRequired: function (value) {
                if (value == "") {
                    return "请输入姓名";
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
    });
    window.onresize = function () {
        var contentHeight = document.documentElement.clientHeight - 76;
        $("#content").css("height", contentHeight);
    };
</script>
</html>