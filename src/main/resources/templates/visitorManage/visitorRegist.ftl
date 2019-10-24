<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>批量管理登记</title>
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
<script type="text/javascript" src="/js/jquery.min.js"></script>
<script type="text/javascript" src="/layui/layui.js"></script>
<script type="text/javascript" src="/js/ajaxFilter.js"></script>
<script>
    /*
    *以下全局变量均在visitorRegisterSetp3.js中有使用
    */
    var batId = "${batVisitor.bat_id}";//批量id
    var onlyStep3 = true;//仅有第三步的标识位,此处为批量访客管理中为某一批量继续登记访客,所以只有第三步
    var batEmployeeId = "${batVisitor.employee_id}";//批量的被访人id
    var batStartTime = "${batVisitor.visit_time}";//批量的开始来访时间
    var batEndTime = "${batVisitor.expire_time}";//批量的结束来访时间
    var batReasonId = "${batVisitor.reason_id}";//批量的来访事由id
    var batContent = "${batVisitor.content}";//批量的来访主题名称
    var batTermIdList = ${termIdList};//批量的设备权限
    var batchRegister = true;//批量登记的标识位,此页面为批量管理中的登记,所以为true.(与访客登记第三步共用页面及js,所以此参数用于js中的判断)
    var editFaceId = "0";//编辑的faceId,此页面不是编辑,为0.(与访客登记第三步共用页面及js,所以此参数用于js中的判断)
    var editRecId = '0';//编辑的来访记录id,此页面不是编辑,为0.(与访客登记第三步共用页面及js,所以此参数用于js中的判断)
    layui.use(['table', 'layer', 'form', 'laydate', 'upload'], function () {

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
                }
            },
            IDCardNumRequired: function (value) {
                if (!checkIDCard(value)) {
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
    });
    window.onresize = function () {
        var contentHeight = document.documentElement.clientHeight - 76;
        $("#content").css("height", contentHeight);
    };
</script>
<#--引入照片JS及访客登记第三步的JS-->
<script type="text/javascript" src="/js/photoJS.js"></script>
<script type="text/javascript" src="/js/visitorRegisterSetp3.js"></script>
</body>
</html>