<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>添加/编辑公司</title>
    <base href="/"/>
    <link rel="stylesheet" href="/layui/css/layui.css">
    <style>
        .button1{
            width: 110px;height: 40px;line-height: 40px;color: #666666;font-size: 16px;border: 1px solid #d8d8d8;border-radius: 4px;background-color: white;cursor:pointer;
        }
        .button2{
            width: 110px;height: 40px;line-height: 40px;color: #ffffff;font-size: 16px;border: none;border-radius: 4px;background-color: #54b5ff;cursor:pointer;
        }
    </style>
</head>
<body>
<form class='layui-form' lay-filter="addCompanyForm">
    <div style="width: 100%;height: 42px;"></div>
    <div style="width: 100%;">
        <input id="companyId" value="${companyId}" style="display: none;">
        <div class='layui-form-item' style="margin-bottom: 14px;">
            <label class='layui-form-label' style="line-height: 14px;padding-right: 11px;color: #333333;margin-left: 61px;">公司名称</label>
            <img src="/css/img/required.png" style="margin-top: 11px;float: left;margin-right: 11px;">
            <div class='layui-input-inline' style='width: 420px;'>
                <input type='text' id="companyName" value="${companyName!}" placeholder="请输入公司名称..." lay-verify='companyNameRequired' style="height: 32px;line-height: 32px;border-radius: 4px;" class='layui-input'>
            </div>
        </div>
        <div class='layui-form-item' style="margin-bottom: 14px;">
            <label class='layui-form-label' style="line-height: 14px;padding-right: 32px;color: #333333;margin-left: 61px;">公司地址</label>
            <div class='layui-input-inline' style='width: 420px;'>
                <input type='text' id="companyAddress" value="${companyAddress!}" placeholder="请输入公司地址..." style="height: 32px;line-height: 32px;border-radius: 4px;" class='layui-input'>
            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label" style="line-height: 14px;padding-right: 32px;color: #333333;margin-left: 61px;">备注</label>
            <div class="layui-input-inline" style="width: 420px;">
                <textarea id="remarks" name="remarks" placeholder="请输入备注..." class="layui-textarea" style="min-height: 120px;border-radius: 4px;"></textarea>
            </div>
        </div>
    </div>
    <div style="width: 100%;height: 45px;"></div>
    <div style="width: 100%;float: right;height: 40px;margin-right: 93px;">
        <button id="cancel" class='button1' type='button' style="float: right;margin-left: 10px;">取消</button>
        <button type='button' lay-submit='' lay-filter='saveCompany' class="button2" style="float: right;">保存</button>
    </div>
</form>
</body>
<script type="text/javascript" src="/js/jquery.min.js"></script>
<script type="text/javascript" src="/layui/layui.js"></script>
<script type="text/javascript" src="/js/ajaxFilter.js"></script>
<script>
    layui.use(['layer','form'], function() {
        var layer = layui.layer;
        var form = layui.form;
        form.val('addCompanyForm', {
            "remarks": "${remarks!}"
        });
        form.verify({
            companyNameRequired: function(value){
                if (value == "") {
                    return '请输入公司名称';
                }
            }
        });

        form.on('submit(saveCompany)', function() {
            var companyId = $("#companyId").val();
            var companyName = $("#companyName").val();
            var companyAddress = $("#companyAddress").val();
            var remarks = $("#remarks").val();
            var company = {
                companyId:companyId,
                companyName:companyName
            };
            if (companyAddress != "") {
                company.companyAddress = companyAddress;
            }
            if (remarks != "") {
                company.remarks = remarks;
            }
            $.ajax({
                url: "/companyManage/addCompany",
                type: "post",
                data: company,
                async: false,
                success: function(data) {
                    if (data == "0") {
                        layer.alert("保存成功",{icon:1,title:"提示",end:function () {
                                parent.location.reload();
                        }});
                    }
                },
                error:function () {

                }
            });
        });
        $("#cancel").click(function () {
           parent.layer.closeAll();
        });
    });
</script>
</html>