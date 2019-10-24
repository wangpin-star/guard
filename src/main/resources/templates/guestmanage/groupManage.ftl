<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>分组管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <base href="/"/>
    <link rel="stylesheet" href="/layui/css/layui.css">
    <link rel="stylesheet" href="/font-awesome/css/font-awesome.css">
</head>
<style type="text/css">
    .layui-tree li {
        overflow: unset;
    }

    .layui-input:hover {
        border-color: #54b5ff !important;
    }

    .layui-btn2 {
        display: inline-block;
        height: 38px;
        line-height: 38px;
        padding: 0 18px;
        background-image: url(/css/img/upload1.png);
        white-space: nowrap;
        text-align: center;
        font-size: 14px;
        border: none;
        border-radius: 2px;
        cursor: pointer
    }

    .layui-btn2:hover {
        background-image: url(/css/img/upload2.png);
    }

    .layui-btn2:active {
        background-image: url(/css/img/upload3.png);
    }

    .button2 {
        width: 80px;
        height: 30px;
        line-height: 30px;
        color: #ffffff;
        font-size: 14px;
        border: none;
        border-radius: 4px;
        background-color: #54b5ff;
        cursor: pointer;
        margin-right: 30px;
        padding: 0;
    }

    .button2 :hover {
        background-color: #77c4ff;
    }

    .button2 :active {
        background-color: #4a96ec;
    }

    .layui-input, .layui-select, .layui-textarea {
        height: 30px;
        line-height: 30px;
        border-width: 1px;
        border-style: solid;
        background-color: #fff;
        border-radius: 2px
    }
    body {
        font-family: \5FAE\8F6F\96C5\9ED1 !important;
    }

    iframe {
        border-style: inset;
        border-color: initial;
        border-image: initial;
        border: 10px solid #f2f2f2;
    }
    .box{
        border: 10px solid #f2f2f2;
        border-right: unset;
    }

</style>

<body>
<div style="width:100%;height:46px;background-color: #f2f2f2">
    <form class="layui-form">
        <div style="margin-bottom: 8px;">
            <div style="margin-top:8px;float:right;">
                <button type="button" class="select-on  layui-btn1"
                        style="border: none;height: 30px;line-height: 30px;background-color: #f2f2f2;margin-left: 5px" id="addGroup">
                    <a href="/guest/showGuest"><img style="margin-bottom: 3px;" src="/css/img/addGroup1.png"> </img>返回</a>
                </button>

                <button class="layui-btn-disabled" type="button" onmouseover="changeImg(this,2)" onmouseout="changeImg(this,1)"
                        onmousedown="changeImg(this,3)" onmouseup="changeImg(this,2)" class="select-on  layui-btn1"
                        style="border: none;height: 30px;line-height: 30px;background-color: #f2f2f2;" id="addGroup" disabled="disabled">
                    <img imgName="addGroup" style="margin-bottom: 3px;" src="/css/img/addGroup1.png"> </img>创建分组
                </button>

                <button type="button" onmouseover="changeImg(this,2)" onmouseout="changeImg(this,1)"
                        onmousedown="changeImg(this,3)" onmouseup="changeImg(this,2)" class="select-on  layui-btn1"
                        style="border: none;height: 30px;line-height: 30px;background-color: #f2f2f2;" id="delGroup">
                    <img imgName="delGroup" style="margin-bottom: 3px;" src="/css/img/delGroup1.png"> </img>注销分组
                </button>
            </div>
        </div>
    </form>
</div>

<div style="margin-top:8px;width:100%">
    <div id="hidediv" style="width:16%;float: left;">
        <a style="margin-left:30px;">当前暂无分组,请创建嘉宾组</a>
    </div>
    <!-- <div id="groupTree" class="demo-tree demo-tree-box" style="width:16%;float: left;"></div> -->
    <div id="treehe" class="box" style="width: 16%;float: left;overflow-y: scroll;">
        <ul style="width:100%" id="groupTree"></ul>
    </div>
    <div id="listhe" class="" style="width:82.5%;margin-left:0.3%;float: left;box-sizing: border-box;">
        <iframe src="guest/showGroup" id="firstIframe"></iframe>
    </div>
</div>
<script type="text/javascript" src="/js/jquery.min.js"></script>
<script type="text/javascript" src="/layui/layui.js"></script>
<script type="text/javascript" src="/js/ajaxFilter.js"></script>

<script>

    function changeImg(obj, status) {
        var imgName = $(obj).find("img").attr("imgName");
        $(obj).find("img").attr("src", "/css/img/" + imgName + status + ".png");
    }
    function changeFrameSize(){
        var ifm= document.getElementById("firstIframe");
        $("#treehe").css("height",document.documentElement.clientHeight-120);
        ifm.height=document.documentElement.clientHeight-120;
        ifm.width="100%";
    }

    layui.use(['tree','form','util'], function() {
        var $ = layui.$
            , tree = layui.tree
            , form = layui.form;

        var groupId = 0;
        var groupName = "";
        changeFrameSize();//设置框架和树尺寸
        layer.load(1);
        $.ajax({
            url: "/guest/queryGroup",
            type: "post",
            contentType: 'application/json',
            dataType: 'json',
            async: false,
            success: function (data) {
                //console.log(data);
                if (data.hasOwnProperty("children")) {
                    document.getElementById('treehe').style.display = "";
                    document.getElementById('hidediv').style.display = "none";
                } else {
                    document.getElementById('hidediv').style.display = "";
                    document.getElementById('treehe').style.display = "none";
                }
                var dataArr = new Array();
                dataArr.push(data);
                layui.tree({
                    elem: '#groupTree'
                    , nodes: dataArr
                    , click: function (node) {//点击节点事件
                        groupId = node.depart_id;
                        groupName = node.name;
                        var parentHeight = document.documentElement.clientHeight;
                        var height = parentHeight*0.95;
                        console.log(groupId);
                        if(groupId != 0){
                            //var name = groupName.substring(1, groupName.length-1);
                            $("#firstIframe").attr("src","/guest/showGroup?groupName="+groupName+"&groupId="+groupId);
                            $("#addGroup").removeClass("layui-btn-disabled");
                            $("#addGroup").removeAttr("disabled");
                            layer.load(1);
                        }
                        /* table.reload('employeeList', {
                            where: {
                                departId: departId,
                            }
                            , page: {
                                curr: 1 //重新从第 1 页开始
                            }
                        }); */
                    }
                });
                //点击第一个节点(默认展开)
                $(".layui-tree a:first").click();

            },
            error: function () {
                layer.alert('服务器错误,请联系管理员', {icon: 2, title: "提示"}, function (index) {
                    layer.close(index);
                });
            }
        });

        //点击数节点改变高亮颜色
        $(".layui-tree a").click(function () {
            $(".layui-tree a cite").css('color', '#333');
            $(this).find('cite').css('color', '#54b5ff');
        });

        //创建分组
        $("#addGroup").on('click', function () {
            $("#firstIframe").attr("src","/guest/showGroup");
            $("#addGroup").addClass("layui-btn-disabled");
            $("#addGroup").attr("disabled","disabled")
            layer.load(1);
        });

        //关闭所有弹出层
        $("#firstIframe").load(function() {
            layer.closeAll();
        });

        //注销分组
        $("#delGroup").on('click', function () {
            if(groupId == 0){
                layer.alert('请选择组进行注销', {
                    title:"提示",
                    icon: 0,
                });
            }else{
                layer.confirm('是否注销当前分组？',{btn:['是','否'],icon:3,title:"提示"},function(){
                    //保存并退出
                    $.ajax({
                        url: "/guest/delGroup",
                        type: "post",
                        data:{groupId : groupId},
                        async: false,
                        success: function(code) {
                            if(code==0){
                                layer.alert("注销分组成功",{icon: 1,title:"提示"},function(){
                                    layer.close();
                                    window.location.reload();
                                });
                            }else{
                                layer.alert('注销分组失败', {
                                    title:"提示",
                                    icon: 2,
                                });
                            }
                        },
                        error: function() {
                            layer.alert('服务器错误,请联系管理员',{icon: 2,title:"提示"},function (index) {
                                layer.close(index);
                            });
                        }
                    });
                },function(){
                    //不保存并退出
                    layer.close();
                });
            }

        });
    });

</script>
</body>
</html>