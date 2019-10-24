$.ajaxSetup({
    type: 'POST',
    complete: function(xhr,status) {
        var sessionStatus = xhr.getResponseHeader('sessionStatus');
        if(sessionStatus === 'timeout') {
            layui.use(['layer'], function(){
                var layer = layui.layer;
                layer.alert('登陆超时,请重新登陆',{icon: 0,title:'提示',end:function(){
                    var top = getTopWinow();
                    top.location.href = '/';
                }});
            });
        }
    }
});

function getTopWinow(){
    var p = window;
    while(p !== p.parent){
        p = p.parent;
    }
    return p;
}