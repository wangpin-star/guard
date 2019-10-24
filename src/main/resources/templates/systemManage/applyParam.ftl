<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>应用参数</title>
    <base href="/"/>
    <link rel="stylesheet" href="/layui/css/layui.css">
    <style>
        .layui-slider-input{
            top:12px;
        }
    </style>
</head>
<body>
    <div class="layui-body" style="left: 11.5%;width:88.5%;">
        <form class="layui-form">
            <div style="width: 2.95%;height:100%;float: left;"></div>
            <div style="width: 97.05%;height: 100%;float: left;">
                <div style="width: 100%;height: 30px;float: left;"></div>
                <div class="layui-form-item" style="margin-left: 3.4%;">
                    <label class="layui-form-label" style="width: 150px;padding-right: 50px;color: #333333">自动刷新间隔时间(1~60)秒</label>
                    <div id="autoRefreshIntervalSlide" class="demo-slider" style="width: 300px;padding-top: 27px;float: left;"></div>
                    <div style="float: left;margin-left: 10px;padding-top: 19px;width: 100px;">秒</div>
                    <div class="layui-input-block" style="display: none;">
                        <input id="autoRefreshInterval" type="text" param_key="${applyParamList[0].param_key}" param_name="${applyParamList[0].param_name}" value="${applyParamList[0].param_value}">
                    </div>
                </div>
                <button style="height: 34px;line-height: 34px;font-size: 16px;padding: 0 39px;margin-top: 30px;margin-left: 8%;" lay-submit='' lay-filter="save" class="layui-btn" type="button">保存</button>
            </div>
        </form>
    </div>
    <script type="text/javascript" src="/js/jquery.min.js"></script>
    <script type="text/javascript" src="/layui/layui.js" charset="utf-8"></script>
    <script type="text/javascript" src="/js/ajaxFilter.js"></script>
    <script type="text/javascript">
        //把高亮改为应用参数
        $(function () {
            window.parent.$("#toApplyParamLi").siblings("li").removeClass("layui-this");
            window.parent.$("#toApplyParamLi").addClass("layui-this");
        });
        layui.use(['layer','form','slider'], function() {
            var layer = layui.layer;
            var form = layui.form;
            var slider = layui.slider;
            //初始化
            slider.render({
                elem: '#autoRefreshIntervalSlide'
                ,value:$("#autoRefreshInterval").val()
                ,input: true
                ,min: 1
                ,max: 60
                ,setTips: function(value) {
                    return value+"秒";
                }
                ,change: function(value) {
                    var val=value.replace("秒","");
                    $("#autoRefreshInterval").attr("value",val);
                }
            });

            form.on('submit(save)', function(obj) {
                var paramConfigInfoList = [];
                var autoRefreshInterval = $("#autoRefreshInterval").val().replace("秒","");
                var autoRefreshIntervalKey = $("#autoRefreshInterval").attr("param_key");
                var autoRefreshIntervalName = $("#autoRefreshInterval").attr("param_name");
                var autoRefreshIntervalObj = {};
                autoRefreshIntervalObj.param_key = autoRefreshIntervalKey;
                autoRefreshIntervalObj.param_value = autoRefreshInterval;
                autoRefreshIntervalObj.param_name = autoRefreshIntervalName;
                paramConfigInfoList.push(autoRefreshIntervalObj);
                $.ajax({
                    url: "/businessParam/saveParamConfigInfo",
                    type: "post",
                    data:JSON.stringify(paramConfigInfoList),
                    contentType : 'application/json',
                    async: false,
                    success: function(data) {
                        if (data == "0") {
                            layer.alert('参数保存成功', {
                                icon: 1,
                                title:"提示",
                                shadeClose: false,
                                shade: [0.2, '#000000'],
                                end:function () {
                                    window.location.reload();
                                }
                            });
                        } else {
                            layer.alert("参数保存失败",{icon: 2,title:"提示"},function (index) {
                                layer.close(index);
                            });
                        }
                    }
                });
            });
        });
    </script>
</body>
</html>