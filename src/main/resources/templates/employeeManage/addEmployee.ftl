<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>添加员工</title>
    <base href="/"/>
    <link rel="stylesheet" href="/layui/css/layui.css">
    <link rel="stylesheet" href="/font-awesome/css/font-awesome.css">
    <link rel="stylesheet" href="/css/index.css"/>
    <style>
        .layui-upload {
            width: 100%;
            float: left;
            margin: 10px 0;
        }

        .layui-upload-list {
            margin: 0;
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

        .layui-form-select input {
            height: 32px;
            line-height: 32px;
            border-radius: 4px;
        }

        .layui-table-main {
            width: 100%;
        }
        
    </style>
</head>
<body style="margin: 0">
<#--start:添加员工第一步-->
<form class='layui-form' id='addEmployeeOne'>
    <#--start:第1,2,3步标签样式-->
    <div style="width: 100%;float: left;height: 24px;"></div>
    <div style="width: 100%;float: left;height: 24px;">
        <div style="width: 178px;float: left;height: 100%;"></div>
        <div style="width: 24px;float: left;height: 100%;">
            <img src="/css/img/step1blue.png">
        </div>
        <div style="width: 20px;float: left;height: 100%;"></div>
        <div style="width: 276px;float: left;height: 100%;">
            <div class="layui-progress" style="background-color: #d8d8d8;height: 2px;margin-top: 11px;"></div>
        </div>
        <div style="width: 20px;float: left;height: 100%;"></div>
        <div style="width: 24px;float: left;height: 100%;">
            <img src="/css/img/step2no.png">
        </div>
        <div style="width: 20px;float: left;height: 100%;"></div>
        <div style="width: 276px;float: left;height: 100%;">
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
        <div style="width: 64px;float: left;height: 100%;font-size: 16px;color: #54b5ff;">基本信息</div>
        <div style="width: 276px;float: left;height: 100%;"></div>
        <div style="width: 64px;float: left;height: 100%;font-size: 16px;color: #333333;">其它信息</div>
        <div style="width: 276px;float: left;height: 100%;"></div>
        <div style="width: 64px;float: left;height: 100%;font-size: 16px;color: #333333;">权限信息</div>
    </div>
    <div style="width: 100%;float: left;height: 18px;"></div>
    <#--end:第1,2,3步标签样式-->
    <#--start:身份证信息-->
    <div style="width: 100%;float: left;height: 20px;">
        <div style="width: 3%;float: left;height: 100%;">
        </div>
        <div style="width: 97%;float: left;height: 100%;">
            <fieldset class="layui-elem-field layui-field-title" style="margin: 0;float: left;width: 100%;">
                <legend style="color: #999999;font-size: 14px;margin-left: 0;padding-left: 0;padding-right: 12px;">
                    身份证信息
                </legend>
            </fieldset>
        </div>
    </div>
    <#--id:message,读卡器提示语-->
    <div id="message" style="width: 100%;float: left;height: 28px;text-align: center;color: #F55366"></div>
    <div style="width: 100%;float: left;height: 216px;">
        <div style="width: 51px;height: 100%;float: left;text-align: center;"></div>
        <div style="height: 100%;float: left;">
            <div class='layui-form-item' style="margin-bottom: 14px;">
                <label class='layui-form-label' style="line-height: 14px;padding-right: 11px;color: #333333;">姓名</label>
                <img src="/css/img/required.png" style="margin-top: 11px;float: left;margin-right: 11px;">
                <div class='layui-input-inline' style='width: 420px;'>
                    <input type='text' id="empName" name='empName' placeholder="请输入姓名..." lay-verify='required|name'
                           maxlength="20" style="height: 32px;line-height: 32px;border-radius: 4px;"
                           class='layui-input'>
                </div>
            </div>
            <div class='layui-form-item' style="margin-bottom: 14px;">
                <label class='layui-form-label' style="line-height: 14px;padding-right: 11px;color: #333333;">性别</label>
                <img src="/css/img/required.png" style="margin-top: 11px;float: left;margin-right: 11px;">
                <div class='layui-input-inline' style="width: 170px;">
                    <input lay-filter="sex" type='radio' style="height: 32px;line-height: 32px;" id="man" name='sex'
                           value='1' title='男' checked/>
                    <input lay-filter="sex" type='radio' style="height: 32px;line-height: 32px;" id="woman" name='sex'
                           value='2' title='女'/>
                </div>
                <label class='layui-form-label' style="line-height: 14px;padding-right: 22px;color: #333333;">民族</label>
                <div class='layui-input-inline' style='width: 123px;'>
                    <select lay-filter="nation" id='nation' name='nation' class='layui-input'
                            style="height: 32px;line-height: 32px;border-radius: 4px;" lay-verify="" lay-search>
                        <option value="请选择" selected>请选择</option>
                    </select>
                </div>
            </div>
            <div class='layui-form-item' style="margin-bottom: 14px;">
                <label class='layui-form-label'
                       style="line-height: 14px;padding-right: 32px;color: #333333;">身份证号</label>
                <div class='layui-input-inline' style='width: 420px;'>
                    <input type='text' id="IDCardNum" checkFaceId="0" maxlength="18" lay-verify="IDCardNum"
                           name='IDCardNum' placeholder="请输入身份证号码..."
                           style="height: 32px;line-height: 32px;border-radius: 4px;" class='layui-input'>
                </div>
            </div>
            <div class='layui-form-item' style="margin-bottom: 14px;">
                <label class='layui-form-label' style="line-height: 14px;padding-right: 32px;color: #333333;">地址</label>
                <div class='layui-input-inline' style='width: 420px;'>
                    <input type='text' id="address" name='address' maxlength="80" lay-verify="addr" placeholder="请输入地址..."
                           style="height: 32px;line-height: 32px;border-radius: 4px;" class='layui-input'>
                </div>
            </div>
            <div class='layui-form-item' style="margin-bottom: 14px;">
                <label class='layui-form-label'
                       style="line-height: 14px;padding-right: 32px;color: #333333;">有效期</label>
                <div class='layui-input-inline' style='width: 420px;'>
                    <input type='text' id="IDCardDate" name='IDCardDate' disabled
                           style="height: 32px;line-height: 32px;border-radius: 4px;" class='layui-input'>
                </div>
            </div>
        </div>
        <#--此object为读卡器所需,页面中不显示-->
        <object classid="CLSID:5EB842AE-5C49-4FD8-8CE9-77D4AF9FD4FF" id="IdrControl1" width="0" height="0">
        </object>
        <div id="IDCardPhotoDiv" style="height: 100%;float: right;margin-right: 162px;display: none;">
            <img class='layui-upload-img' id='IDCardPhoto' style="border: 1px solid #d8d8d8;border-radius: 4px;"
                 width='102' height='126' src="/css/img/IDCardPhotoDefault.png">
        </div>
    </div>
    <#--end:身份证信息-->
    <#--start:比对照片-->
    <div style="width: 100%;float: left;height: 28px;"></div>
    <div style="width: 100%;float: left;height: 20px;">
        <div style="width: 3%;float: left;height: 100%;">
        </div>
        <div style="width: 97%;float: left;height: 100%;">
            <fieldset class="layui-elem-field layui-field-title" style="margin: 0;float: left;width: 100%;">
                <legend style="color: #999999;font-size: 14px;margin-left: 0;padding-left: 0;padding-right: 12px;">
                    比对照片
                </legend>
            </fieldset>
        </div>
    </div>
    <#--id:message2,Flash提示语-->
    <div id="message2" style="width: 100%;float: left;height: 28px;text-align: center;color: #F55366"></div>
    <div style="width: 100%;float: left;height: 249px;">
        <div style="width: 178px;height: 100%;float: left;"></div>
        <div style="width: 142px;height: 100%;float: left;">
            <div style="width: 100%;height: 176px;line-height: 176px;text-align: center;">
                <img class='layui-upload-img' id="photo1" width='142' height='176' picId="0" option="1" pass="0"
                     style="border: 1px solid #d8d8d8;border-radius: 4px;" src="/css/img/comparePhotoDefault.png">
                <#--hidePhoto,实现照片比例压缩功能需要一个隐藏的图片作为中转-->
                <img id="hidePhoto1" style="display: none;">
            </div>
            <div style="width: 100%;height: 8px;"></div>
            <div style="width: 100%;height: 30px;">
                <button type='button' class="button1 uploadBtn" id="uploadPhoto1">上传</button>
                <button type='button' class="button1 takePhoto" onclick="takePhoto(1)" style="margin-left: 1px;">拍照
                </button>
                <button type='button' class="button1" onclick="deletePhoto(1)" style="margin-left: 1px;">删除</button>
            </div>
            <div style="width: 100%;height: 5px;"></div>
            <div style="width: 100%;height: 30px;">
                <button type='button' class="button1" id="passCompare1" onclick="passCompare(1)"
                        style="width: 142px;display: none;">手动通过
                </button>
            </div>
        </div>
        <div style="width: 158px;height: 100%;float: left;"></div>
        <div style="width: 142px;height: 100%;float: left;">
            <div style="width: 100%;height: 176px;line-height: 176px;text-align: center;">
                <img class='layui-upload-img' id="photo2" width='142' height='176' picId="0" option="1" pass="0"
                     style="border: 1px solid #d8d8d8;border-radius: 4px;" src="/css/img/comparePhotoDefault.png">
                <img id="hidePhoto2" style="display: none;">
            </div>
            <div style="width: 100%;height: 8px;"></div>
            <div style="width: 100%;height: 30px;">
                <button type='button' class="button1 uploadBtn" id="uploadPhoto2">上传</button>
                <button type='button' class="button1 takePhoto" onclick="takePhoto(2)" style="margin-left: 1px;">拍照
                </button>
                <button type='button' class="button1" onclick="deletePhoto(2)" style="margin-left: 1px;">删除</button>
            </div>
            <div style="width: 100%;height: 5px;"></div>
            <div style="width: 100%;height: 30px;">
                <button type='button' class="button1" id="passCompare2" onclick="passCompare(2)"
                        style="width: 142px;display: none;">手动通过
                </button>
            </div>
        </div>
        <div style="width: 158px;height: 100%;float: left;"></div>
        <div style="width: 142px;height: 100%;float: left;">
            <div style="width: 100%;height: 176px;line-height: 176px;text-align: center;">
                <img class='layui-upload-img' id="photo3" width='142' height='176' picId="0" option="1" pass="0"
                     style="border: 1px solid #d8d8d8;border-radius: 4px;" src="/css/img/comparePhotoDefault.png">
                <img id="hidePhoto3" style="display: none;">
            </div>
            <div style="width: 100%;height: 8px;"></div>
            <div style="width: 100%;height: 30px;">
                <button type='button' class="button1 uploadBtn" id="uploadPhoto3">上传</button>
                <button type='button' class="button1 takePhoto" onclick="takePhoto(3)" style="margin-left: 1px;">拍照
                </button>
                <button type='button' class="button1" onclick="deletePhoto(3)" style="margin-left: 1px;">删除</button>
            </div>
            <div style="width: 100%;height: 5px;"></div>
            <div style="width: 100%;height: 30px;">
                <button type='button' class="button1" id="passCompare3" onclick="passCompare(3)"
                        style="width: 142px;display: none;">手动通过
                </button>
            </div>
        </div>
    </div>
    <#--end:比对照片-->
    <#--start:操作按钮-->
    <div style="width: 100%;float: left;height: 30px;"></div>
    <div style="width: 100%;float: right;height: 34px;margin-right: 140px;">
        <button id="step1Save" class='button2' type='button' lay-submit='' lay-filter='addEmployeeOneFilterSave'
                style="float: right;margin-left: 10px;display: none;">保存
        </button>
        <button type='button' lay-submit='' lay-filter='addEmployeeOneFilter' class="button2" style="float: right;">
            下一步
        </button>
    </div>
    <#--end:操作按钮-->
</form>
<#--end:添加员工第一步-->
<#--start:添加员工第二步-->
<form class='layui-form' id='addEmployeeTwo' style="display: none;">
    <div style="width: 100%;float: left;height: 24px;"></div>
    <div style="width: 100%;float: left;height: 24px;">
        <div style="width: 178px;float: left;height: 100%;"></div>
        <div style="width: 24px;float: left;height: 100%;">
            <img src="/css/img/step1blue.png">
        </div>
        <div style="width: 20px;float: left;height: 100%;"></div>
        <div style="width: 276px;float: left;height: 100%;">
            <div class="layui-progress" style="background-color: #54b5ff;height: 2px;margin-top: 11px;"></div>
        </div>
        <div style="width: 20px;float: left;height: 100%;"></div>
        <div style="width: 24px;float: left;height: 100%;">
            <img src="/css/img/step2blue.png">
        </div>
        <div style="width: 20px;float: left;height: 100%;"></div>
        <div style="width: 276px;float: left;height: 100%;">
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
        <div style="width: 64px;float: left;height: 100%;font-size: 16px;color: #54b5ff;">基本信息</div>
        <div style="width: 276px;float: left;height: 100%;"></div>
        <div style="width: 64px;float: left;height: 100%;font-size: 16px;color: #54b5ff;">其它信息</div>
        <div style="width: 276px;float: left;height: 100%;"></div>
        <div style="width: 64px;float: left;height: 100%;font-size: 16px;color: #333333;">权限信息</div>
    </div>
    <div style="width: 100%;float: left;height: 86px;"></div>
    <div style="width: 100%;float: left;">
        <div style="width: 51px;height: 100%;float: left;"></div>
        <div style="height: 100%;float: left;">
            <div class="layui-form-item" style="margin-bottom: 14px;">
                <label class="layui-form-label" style="line-height: 14px;padding-right: 11px;color: #333333;">部门</label>
                <img src="/css/img/required.png" style="margin-top: 11px;float: left;margin-right: 11px;">
                <div class="layui-input-inline" style="width: 420px;">
                    <select lay-filter="depart" id="depart" name="depart" lay-verify="depart" lay-search="">
                        <option value="" selected="">请搜索/选择部门</option>
                        <#list departList as depart>
                            <option value="${depart.depart_id}">${depart.depart_name}</option>
                        </#list>
                    </select>
                </div>
            </div>
            <div class='layui-form-item' style="margin-bottom: 14px;">
                <label id="telNumLabel" class='layui-form-label'
                       style="line-height: 14px;padding-right: 32px;color: #333333;">手机号</label>
                <img id="telNumRequiredPhoto" src="/css/img/required.png"
                     style="margin-top: 11px;float: left;margin-right: 11px;display: none;">
                <div class='layui-input-inline' style='width: 420px;'>
                    <input type='text' id="telNum" checkFaceId="0" name='telPhoneNum' placeholder="请输入员工手机号码..."
                           onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)"
                           onblur="this.v();" lay-verify='telNum' maxlength="11"
                           style="height: 32px;line-height: 32px;border-radius: 4px;" class='layui-input'>
                </div>
            </div>
            <div class="layui-form-item" style="margin-bottom: 14px;">
                <label class="layui-form-label"
                       style="line-height: 14px;padding-right: 32px;color: #333333;">上级领导</label>
                <div class="layui-input-inline" style="width: 420px;">
                    <select lay-filter="leader" id="leader" lay-search=""></select>
                </div>
            </div>
            <div class='layui-form-item' style="margin-bottom: 14px;">
                <label class='layui-form-label' style="line-height: 14px;padding-right: 32px;color: #333333;">昵称</label>
                <div class='layui-input-inline' style='width: 420px;'>
                    <input type='text' id="nickName" name='nickName' lay-verify="nickName" placeholder="请输入员工昵称..." maxlength="10"
                           style="height: 32px;line-height: 32px;border-radius: 4px;" class='layui-input'>
                </div>
            </div>
            <div class='layui-form-item' style="margin-bottom: 14px;">
                <label class='layui-form-label' style="line-height: 14px;padding-right: 32px;color: #333333;">职位</label>
                <div class='layui-input-inline' style='width: 420px;'>
                    <input type='text' id="position" name='position' lay-verify="position" placeholder="请输入员工职位..." maxlength="10"
                           style="height: 32px;line-height: 32px;border-radius: 4px;" class='layui-input'>
                </div>
            </div>
            <div class='layui-form-item' style="margin-bottom: 14px;">
                <label class='layui-form-label' style="line-height: 14px;padding-right: 32px;color: #333333;">工号</label>
                <div class='layui-input-inline' style='width: 420px;'>
                    <input type='text' id="employeeId" name='employeeId' placeholder="请输入员工工号..."
                           onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"
                           onKeyPress="if(event.keyCode ==92 || event.keyCode ==47
                               || event.keyCode ==58|| event.keyCode ==42
                               || event.keyCode ==63|| event.keyCode ==34
                               || event.keyCode ==60|| event.keyCode ==62
                               || event.keyCode ==124) event.returnValue = false;"
                           maxlength="16"
                           style="height: 32px;line-height: 32px;border-radius: 4px;" class='layui-input'>
                </div>
            </div>
            <div class='layui-form-item' style="margin-bottom: 14px;">
                <#if is_need_employee_card??>
                    <#if is_need_employee_card == "1">
                        <label class='layui-form-label' style="line-height: 14px;padding-right: 11px;color: #333333;">工牌号</label>
                        <img src="/css/img/required.png" style="margin-top: 11px;float: left;margin-right: 11px;">
                        <div class='layui-input-inline' style='width: 420px;'>
                            <input type='text' id="empcard" name='empcard' placeholder="请输入员工工牌号，可在支持刷工牌的设备上使用..."
                                   onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"
                                   onKeyPress="if(event.keyCode ==92 || event.keyCode ==47
                                                    || event.keyCode ==58|| event.keyCode ==42
                                                    || event.keyCode ==63|| event.keyCode ==34
                                                    || event.keyCode ==60|| event.keyCode ==62
                                                    || event.keyCode ==124) event.returnValue = false;"
                                   maxlength="16"
                                   style="height: 32px;line-height: 32px;border-radius: 4px;" class='layui-input'
                                   lay-verify="required">
                        </div>
                    </#if>
                    <#if is_need_employee_card == "0">
                        <label class='layui-form-label' style="line-height: 14px;padding-right: 32px;color: #333333;">工牌号</label>
                        <div class='layui-input-inline' style='width: 420px;'>
                            <input type='text' id="empcard" name='empcard' placeholder="请输入员工工牌号，可在支持刷工牌的设备上使用..."
                                   onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"
                                   onKeyPress="if(event.keyCode ==92 || event.keyCode ==47
                                                    || event.keyCode ==58|| event.keyCode ==42
                                                    || event.keyCode ==63|| event.keyCode ==34
                                                    || event.keyCode ==60|| event.keyCode ==62
                                                    || event.keyCode ==124) event.returnValue = false;"
                                   maxlength="16"
                                   style="height: 32px;line-height: 32px;border-radius: 4px;" class='layui-input'>
                        </div>
                    </#if>
                <#else >
                    <label class='layui-form-label'
                           style="line-height: 14px;padding-right: 11px;color: #333333;">工牌号</label>
                    <img src="/css/img/required.png" style="margin-top: 11px;float: left;margin-right: 11px;">
                    <div class='layui-input-inline' style='width: 420px;'>
                        <input type='text' id="empcard" name='empcard' placeholder="请输入员工工牌号，可在支持刷工牌的设备上使用..."
                               onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"
                               onKeyPress="if(event.keyCode ==92 || event.keyCode ==47
                                                    || event.keyCode ==58|| event.keyCode ==42
                                                    || event.keyCode ==63|| event.keyCode ==34
                                                    || event.keyCode ==60|| event.keyCode ==62
                                                    || event.keyCode ==124) event.returnValue = false;"
                               maxlength="16"
                               style="height: 32px;line-height: 32px;border-radius: 4px;" class='layui-input'
                               lay-verify="required">
                    </div>
                </#if>
            </div>
            <#--添加车辆信息-->
            <div class='layui-form-item' style="margin-bottom: 14px;">
             <div class="layui-form-item" style="margin-bottom: 0px">
                <label class='layui-form-label' style="line-height: 14px;padding-right: 32px;color: #333333;">车辆</label>
                <div class="layui-input-inline">
                    <div class='layui-input-inline' id="carInfodiv1" style='width: 420px;'>
	                    <input type='text' name='position' placeholder="请点击添加车辆按钮添加车辆信息..." 
	                           style="height: 32px;line-height: 32px;border-radius: 4px;" class='layui-input'>
                    </div>
                    <div style="display: none;float: left;width: 420px;border-radius: 4px;border:1px solid #e2e2e2" id="carInfodiv">
                    </div>
                </div>
                <button onmouseover="changeImg(this,2)" onmouseout="changeImg(this,1)" onmousedown="changeImg(this,3)"
                        onmouseup="changeImg(this,2)" class="layui-btn" type="button" onclick="carbtn();"
                        style="left: 40%;border: none;height: 40px;line-height: 40px;background-color: white;color: #333333;padding-left: 10px;position: relative;">
                    <img style="position: relative;" imgName="create" src="/css/img/create1.png"/>  新增车辆
                </button>
             </div>
            </div>
                     
        </div>
        <div style="height: 100%;width:168px;float: right;margin-right: 144px;">
            <div style="height: 168px;width: 100%;float: left;text-align: center;line-height: 168px;">
                <img class='layui-upload-img' id='photo4' picId="0"
                     style="border-radius: 50%;border: 2px solid #d8d8d8;" width='164' height='164'
                     src="/css/img/headPhotoDefault.png">
                <img id="hidePhoto4" style="display: none;">
            </div>
            <div style="height: 20px;width: 100%;float: left;"></div>
            <div style="height: 30px;width: 100%;float: left;">
                <button type='button' class="button1 uploadBtn" id="uploadPhoto4" style="width: 80px;">上传头像</button>
                <button type='button' class="button1" onclick="deletePhoto(4)" style="width: 80px;margin-left: 4px;">
                    删除
                </button>
            </div>
        </div>
    </div>
    <#--<div style="width: 100%;float: left;height: 229px;"></div>-->
    <div style="width: 100%;float: right;height: 34px;margin-right: 140px;">
        <button id="step2Save" class='button2' type='button' lay-submit='' lay-filter='addEmployeeTwoFilterSave'
                style="float: right;margin-left: 10px;display: none;">保存
        </button>
        <button class='button2' type='button' lay-submit='' lay-filter='addEmployeeTwoFilter'
                style="float: right;margin-left: 10px;">下一步
        </button>
        <button class='button2' type='button' onclick="backAddEmployeeOne();" style="float: right;">上一步</button>
    </div>
</form>
<#--end:添加员工第二步-->
<#--start:添加员工第三步-->
<form class='layui-form' id='addEmployeeThree' style="display: none;">
    <div style="width: 100%;float: left;height: 24px;"></div>
    <div style="width: 100%;float: left;height: 24px;">
        <div style="width: 178px;float: left;height: 100%;"></div>
        <div style="width: 24px;float: left;height: 100%;">
            <img src="/css/img/step1blue.png">
        </div>
        <div style="width: 20px;float: left;height: 100%;"></div>
        <div style="width: 276px;float: left;height: 100%;">
            <div class="layui-progress" style="background-color: #54b5ff;height: 2px;margin-top: 11px;"></div>
        </div>
        <div style="width: 20px;float: left;height: 100%;"></div>
        <div style="width: 24px;float: left;height: 100%;">
            <img src="/css/img/step2blue.png">
        </div>
        <div style="width: 20px;float: left;height: 100%;"></div>
        <div style="width: 276px;float: left;height: 100%;">
            <div class="layui-progress" style="background-color: #54b5ff;height: 2px;margin-top: 11px;"></div>
        </div>
        <div style="width: 20px;float: left;height: 100%;"></div>
        <div style="width: 24px;float: left;height: 100%;">
            <img src="/css/img/step3blue.png">
        </div>
    </div>
    <div style="width: 100%;float: left;height: 12px;"></div>
    <div style="width: 100%;float: left;height: 16px;">
        <div style="width: 158px;float: left;height: 100%;"></div>
        <div style="width: 64px;float: left;height: 100%;font-size: 16px;color: #54b5ff;">基本信息</div>
        <div style="width: 276px;float: left;height: 100%;"></div>
        <div style="width: 64px;float: left;height: 100%;font-size: 16px;color: #54b5ff;">其它信息</div>
        <div style="width: 276px;float: left;height: 100%;"></div>
        <div style="width: 64px;float: left;height: 100%;font-size: 16px;color: #54b5ff;">权限信息</div>
    </div>
    <div style="width: 100%;float: left;height: 28px;"></div>
    <div style="width: 100%;float: left;height: 20px;">
        <div style="width: 3%;float: left;height: 100%;">
        </div>
        <div style="width: 97%;float: left;height: 100%;">
            <fieldset class="layui-elem-field layui-field-title" style="margin: 0;float: left;width: 100%;">
                <legend style="color: #999999;font-size: 14px;margin-left: 0;padding-left: 0;padding-right: 12px;">
                    人员可通过区域
                </legend>
            </fieldset>
        </div>
    </div>
    <div style="width: 90%;float: left;margin-left: 5%;box-sizing: border-box;">
        <table class="layui-table" id="deviceAuthor"></table>
    </div>
    <!-- <div style="width: 100%;float: left;height: 20px;">
        <div style="width: 3%;float: left;height: 100%;">
        </div>
        <div style="width: 97%;float: left;height: 100%;">
            <fieldset class="layui-elem-field layui-field-title" style="margin: 0;float: left;width: 100%;">
                <legend style="color: #999999;font-size: 14px;margin-left: 0;padding-left: 0;padding-right: 12px;">
                    车辆可通过区域
                </legend>
            </fieldset>
        </div>
    </div>
    <div style="width: 90%;float: left;height: 300px;margin-left: 5%;box-sizing: border-box;">
        <table class="layui-table" id="carAuthor"></table>
    </div> -->
    <div style="width: 100%;float: left;height: 10px;"></div>
    <div style="width: 100%;float: right;height: 34px;margin-right: 140px;">
        <button class='button2' type='button' lay-submit='' lay-filter='addEmployeeThreeFilter'
                style="float: right;margin-left: 10px;">保存
        </button>
        <button class='button2' type='button' onclick="backAddEmployeeTwo();" style="float: right;">上一步</button>
    </div>
    <div style="width: 100%;float: left;height: 30px;"></div>
</form>
<#--end:添加员工第三步-->
<#--id:ifWriteFlag,是否有操作的标识位,默认为0,当操作页面时会修改为1,未保存时退出会给出提示-->
<input id="ifWriteFlag" style="display: none;" value="0">
</body>
<script type="text/javascript" src="/js/jquery.min.js"></script>
<script type="text/javascript" src="/layui/layui.js"></script>
<#--ajax登录拦截须引入-->
<script type="text/javascript" src="/js/ajaxFilter.js"></script>
<script type="text/javascript" src="/layui/lay/extra-modules/jtagsinput/jtagsinput.js"></script>
<#--使用控件拍照时所需,现因拍照实现改为H5或Flash,可注释掉这段代码-->
<script language="javascript" for="OpenCamera_ocx" event="onChecked(type)" type="text/javascript">
    showResult(type);
</script>
<#--车辆信息录入js-->
<script>
    function changeImg(obj, status) {
        var imgName = $(obj).find("img").attr("imgName");
        $(obj).find("img").attr("src", "/css/img/" + imgName + status + ".png");
    }
    
    layui.use(['layer', 'form'], function () {
        var $ = layui.$;
        var layer = layui.layer;
        var form = layui.form;
    });
    
    var MaxInputs    = 8; //maximum input boxes allowed  
	var carInfodiv   = $("#carInfodiv"); //Input boxes wrapper ID  
	var x = carInfodiv.length; //initlal text box count
	var FieldCount=1; //to keep track of text box added

	var carId = '';
	var carnum = '';
    var licensetype = '';
    var carcolor = '';
    var cartype = '';
	var carList=[];

    function carbtn(){
        	layer.open({
                type: 2,
                title: '新增车辆',
                area: ['500px', '350px'], //宽高
                content: '/employeeManage/toCar',
                shadeClose: false,
                shade: [0.2, '#000000'],
                btn: ['确定', '取消'],
                yes: function (index, layero) {
                    var body = layer.getChildFrame('body', index);
                    var carInfo = {};
                    carnum = body.find('#carNum').val();
                    licensetype = body.find('#licenseType').find("option:selected").val();
                    carcolor = body.find('#carColor').find("option:selected").val();
                    cartype = body.find('#carType').val();
                    
                    if(x <= MaxInputs){  
            			$(carInfodiv).append(
            			'<div style="width:200px;height:32px;float:left;margin-left: 10px;margin-top: 2px;">\n'+
            			'<input type="text" style="width:100px;border:none;" id="car_'+ FieldCount +'" name="car_'+ FieldCount +'" value="'+ carnum +'"/>\n'+
            			<#--'<button type="button" id="button_'+ FieldCount +'" onclick="editCarinfo(this)">edit</button>\n'+
            			'<button type="button" id="button_'+ FieldCount +'" onclick="removeCarinfo(this)">del</button></div>'
            			-->
            			'<button id="button_'+ FieldCount +'" onmouseover="changeImg(this,2)" onmouseout="changeImg(this,1)" onmousedown="changeImg(this,3)" onmouseup="changeImg(this,2)" type="button" onclick="editCarinfo(this)" style="position:relative;height: 32px;line-height: 32px;border: none;background-color: white;cursor: pointer;"><img imgName="editCar" src="/css/img/editCar1.png"/></button>\n'+
            			'<button id="button_'+ FieldCount +'" onmouseover="changeImg(this,2)" onmouseout="changeImg(this,1)" onmousedown="changeImg(this,3)" onmouseup="changeImg(this,2)" type="button" onclick="removeCarinfo(this)" style="position:relative;height: 32px;line-height: 32px;border: none;background-color: white;cursor: pointer;"><img imgName="delCar" src="/css/img/delCar1.png"/></button></div>'
            			);  
            			x++;
            			
                    	carInfo.carId = 'car_'+ FieldCount;
                   	 	carInfo.carnum = carnum;
	                    carInfo.licensetype = licensetype;
	                    carInfo.carcolor = carcolor;
	                    carInfo.cartype = cartype;
	                    carList.push(carInfo);
	                    FieldCount++;
        			}else{
        				layer.alert('每个用户最多可添加'+MaxInputs+'辆车',{icon: 0,title:"提示"},function (index) {
            	             layer.close(index);
            	        });
        			}
        			
                    layer.close(index); //如果设定了yes回调，需进行手工关闭
                    var display = document.getElementById('carInfodiv').style.display;
		        	if(display == 'none'){
		        		document.getElementById('carInfodiv').style.display ="";
		                document.getElementById('carInfodiv1').style.display ="none";
		        	}
                }
            });
    }
    
    function editCarinfo(obj){
        var buttonid = obj.id;
        var array = buttonid.split('_');
        var num = array[1];
        var inputid = 'car_'+ num;
        
        for (var i in carList) {
        	if (carList[i].carId.indexOf(inputid) >= 0) {
               carnum = carList[i].carnum;
    		   licensetype = carList[i].licensetype;
    		   carcolor = carList[i].carcolor;
    		   cartype = carList[i].cartype;
            }   
        }
        
        layer.open({
                type: 2,
                title: '编辑车辆',
                area: ['500px', '350px'], //宽高
                content: '/employeeManage/toCar?carnum='+carnum+'&licensetype='+licensetype+'&carcolor='+carcolor+'&cartype='+cartype,
                shadeClose: false,
                shade: [0.2, '#000000'],
                btn: ['确定', '取消'],
                yes: function (index, layero) {
                    var body = layer.getChildFrame('body', index);
                    var carnum = body.find('#carNum').val();
                    var licensetype = body.find('#licenseType').val();
                    var carcolor = body.find('#carColor').val();
                    var cartype = body.find('#carType').val();
                    
                    for (var i in carList) {
			        	if (carList[i].carId.indexOf(inputid) >= 0) {
			               carList[i].carnum = carnum;
			    		   carList[i].licensetype = licensetype;
			    		   carList[i].carcolor = carcolor;
			    		   carList[i].cartype = cartype;
			            }   
		       	    }
		       	    
		       	    document.getElementById(inputid).value = carnum;
                    layer.close(index); //如果设定了yes回调，需进行手工关闭
                }
            });
    }
    
    function removeCarinfo(obj){
        var buttonid = obj.id;
        var array = buttonid.split('_');
        var num = array[1];
        var inputid = 'car_'+ num;
        
        for (var i in carList) {
        	if (carList[i].carId.indexOf(inputid) >= 0) {
               carList.splice(i, 1);
         	   console.log(carList); 
            }   
        }
        
        if( x > 1 ) {  
          if(x == 2){
	        document.getElementById('carInfodiv1').style.display ="";
			document.getElementById('carInfodiv').style.display ="none";
			
			$(obj).parent('div').remove();   
            x--;
    	  }else{
	    	  $(obj).parent('div').remove();   
	          x--;
    	  } 
        }
        
		return false;
    }
    
</script>
    
<script>
    var t1 = "";//读身份证方法定时执行的全局变量,因开启和关闭定时在不同方法中,故定义全局
    var photoMaxSize = "${life_photo_max_size}";//比对照片最大kb
    var IDCardPhotoData = "";//身份证照片元数据
    var editFaceId = "${faceId}".replace(',', '');//编辑员工的id,为0则是添加员工,超过千位会有千位分隔符",",因此使用replace去掉
    var face_info_input_mod = "${face_info_input_mod}";//身份证信息的录入方式,0为仅读身份证,1为手输或读身份证
    var editLeaderId = "";//编辑员工时的上级领导id,用于下拉框回显
    var editIDCardIfExit = false;//编辑员工时该员工是否有身份证号。默认为没有,在回显编辑员工信息的方法中若有身份证号的字段则会改为true。用于实现"编辑员工若有身份证号则不能删除"的功能
    layui.use(['table', 'layer', 'form', 'upload'], function () {
        var $ = layui.$;
        var table = layui.table;
        var layer = layui.layer;
        var form = layui.form;
        var upload = layui.upload;

        //校验特殊字符
        form.verify({
            name: function (value, item) {
                if (!new RegExp("^[^`~!@#$%^&*()_+<>?:\"{},.\\/\\\\;'[\\]·！#￥（——）：；“”‘、，|《。》？、【】[\\]]+$").test(value)) {
                    return '姓名不能有特殊字符';
                }
            }
            ,addr: function (value, item) {
                if (!new RegExp("^$|[^`~!@#$%^&*()_+<>?:\"{},.\\/\\\\;'[\\]·！#￥（——）：；“”‘、，|《。》？、【】[\\]]+$").test(value)) {
                    return '住址不能有特殊字符';
                }
            }
            ,nickName: function (value, item) {
                if (!new RegExp("^$|[^`~!@#$%^&*()_+<>?:\"{},.\\/\\\\;'[\\]·！#￥（——）：；“”‘、，|《。》？、【】[\\]]+$").test(value)) {
                    return '昵称不能有特殊字符';
                }
            }
            ,position: function (value, item) {
                if (!new RegExp("^$|[^`~!@#$%^&*()_+<>?:\"{},.\\/\\\\;'[\\]·！#￥（——）：；“”‘、，|《。》？、【】[\\]]+$").test(value)) {
                    return '职位不能有特殊字符';
                }
            }
        });



        if (photoMaxSize == "") {//若后台返回比对照片最大kb为空,则默认给100.后台也做了非空判断,所以此处理论上是不会为空的
            photoMaxSize = "100";
        }
        if (editFaceId != "0") {//编辑员工回显数据
            var param = {
                face_id: editFaceId,
                attribute: 0,
                depart_id: 0,
                status: 0
            };
            $.ajax({//查询员工基本信息
                url: "/visitorManage/selectByParam",
                type: "post",
                data: param,
                async: true,
                success: function (data) {
                    var jsonData = $.parseJSON(data);
                    if (jsonData.count == 1) {//返回结果为一条是正常情况
                        var faceInfo = jsonData.data[0];//获取faceInfo对象
                        //回显员工姓名
                        var empName = faceInfo.name;
                        $('#empName').attr("title", empName);
                        $("#empName").val(empName);
                        //回显员工性别
                        console.log(faceInfo.sex);
                        if (faceInfo.sex == 1) {
                            $('#man').attr("checked", true);
                        }
                        if (faceInfo.sex == 2) {
                            $('#woman').attr("checked", true);
                        }
                        //回显员工身份证号,如果存在身份证号,则把标识位改为true,用于实现"编辑员工若有身份证号则不能删除"的功能
                        if (faceInfo.idcard != undefined) {
                            $("#IDCardNum").val(faceInfo.idcard);
                            editIDCardIfExit = true;
                        }
                        //回显民族
                        if (faceInfo.nation != undefined) {
                            $('#nation').val(faceInfo.nation);
                        }
                        //回显地址
                        var address = faceInfo.addr;
                        $('#address').attr("title", address);
                        $('#address').val(address);
                        //回显身份证有效期
                        $("#IDCardDate").val(faceInfo.idcard_expire);
                        //回显身份证照片
                        if (faceInfo.photo_wlt != undefined) {
                            //因身份证照片的div框是默认不显示的,如果有身份证照片才显示
                            $("#IDCardPhotoDiv").show();
                            $('#IDCardPhoto').attr("src", "data:image/jpeg;base64," + faceInfo.photo_wlt);
                        }
                        //回显部门下拉框
                        $("#depart").val(faceInfo.depart_id);
                        //给领导id的变量赋值
                        editLeaderId = faceInfo.leader_id;
                        //回显手机号
                        $('#telNum').val(faceInfo.tel_no);
                        //回显昵称
                        var nickname = faceInfo.nick_name;
                        $('#nickName').attr("title", nickname);
                        $("#nickName").val(nickname);
                        //回显职位
                        var position = faceInfo.position;
                        $('#position').attr("title", position);
                        $("#position").val(position);
                        //回显工号
                        var employee_id = faceInfo.employee_id;
                        $('#employeeId').attr("title", employee_id);
                        $("#employeeId").val(employee_id);
                        //回显工牌号
                        var empcard = faceInfo.empcard;
                        $('#empcard').attr("title", empcard);
                        $("#empcard").val(empcard);

                        $.ajax({//查询图片数据(比对照片+头像)
                            url: "/employeeManage/selectFacePhoto",
                            type: "post",
                            data: param,
                            async: true,
                            success: function (data) {
                                var jsonData = $.parseJSON(data);
                                if (jsonData.resultCode == "0") {//查询照片成功
                                    //获取比对照片的数组
                                    var comparePictureList = jsonData.comparePictureList;

                                    //照片压缩暂不能使用for循环,因为我不知道怎么定义动态的变量名,也没查到,需要一张一张的压缩
                                    if (comparePictureList[0] != undefined) {//判断是否存在第一张比对照片
                                        //获取第一张比对照片的数据,并拼接为img中显示的格式
                                        var result1 = "data:image/jpeg;base64," + comparePictureList[0].data;
                                        //开始照片宽高比压缩
                                        //把图片数据赋值给"隐藏照片1",需使用这张隐藏照片获取图片数据的宽高
                                        $("#hidePhoto1").attr('src', result1);
                                        //定义一个图片对象
                                        var theImage1 = new Image();
                                        //把图片对象的src指定为"隐藏照片1"
                                        theImage1.src = $("#hidePhoto1").attr("src");
                                        theImage1.onload = function () {//计算照片比例
                                            //定义图片框的宽高,需要与页面布局的宽高一致
                                            var width = "142";
                                            var height = "176";
                                            //需要复原页面中的照片宽高,因为之前可能已经压缩过.此处为编辑员工首次加载图片,也可以不加复原的代码
                                            $("#photo1").attr('width', width);
                                            $("#photo1").attr('height', height);
                                            if (width / height > this.width / this.height) {//如果图片框的宽比高大于这张图片的宽比高,则说明需要压缩图片的宽度,显示出来为"竖"着
                                                var changeWidth = (this.width / this.height) * height;
                                                $("#photo1").attr('width', changeWidth);
                                            } else {//反之,需要压缩图片的高度,显示出来为"横"着
                                                var changeHeight = (width * this.height) / this.width;
                                                $("#photo1").attr('height', changeHeight);
                                            }
                                            $("#photo1").attr('src', result1);
                                        }
                                        $("#photo1").attr("picId", comparePictureList[0].pic_id);//照片id
                                        $("#photo1").attr('pass', "1");//编辑员工的比对照片默认人脸比对通过(0-不通过,1-通过)
                                        $("#photo1").attr('option', "0");//操作默认不变(0-不变 ,1-新增或修改, 2-删除)

                                    }
                                    if (comparePictureList[1] != undefined) {//判断是否存在第二张比对照片
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
                                    if (comparePictureList[2] != undefined) {//判断是否存在第三张比对照片
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
                                    //获取头像照片
                                    var headPictureList = jsonData.headPictureList;
                                    if (headPictureList.length > 0) {//判断是否存在头像照片
                                        var result4 = "data:image/jpeg;base64," + headPictureList[0].data;
                                        $("#hidePhoto4").attr('src', result4);
                                        var theImage4 = new Image();
                                        theImage4.src = $("#hidePhoto4").attr("src");
                                        theImage4.onload = function () {
                                            var width = "164";
                                            var height = "164";
                                            $("#photo4").attr('width', "164");
                                            $("#photo4").attr('height', "164");
                                            if (width / height > this.width / this.height) {
                                                var changeWidth = (this.width / this.height) * height;
                                                $("#photo4").attr('width', changeWidth);
                                            } else {
                                                var changeHeight = (width * this.height) / this.width;
                                                $("#photo4").attr('height', changeHeight);
                                            }
                                            $("#photo4").attr('src', result4);
                                        }
                                        //头像不进行比对,所以没有pass
                                        //头像的新增和修改是通过face_info_base64字段,只有删除才使用Picture对象,所以没有option
                                        $("#photo4").attr("picId", headPictureList[0].pic_id);//图片id
                                    }
                                } else {
                                    layer.alert('查询照片失败,请重试!', {icon: 2, title: "提示"}, function (index) {
                                        layer.close(index);
                                    });
                                }
                            }
                        });
                        //渲染表单(单选框,下拉框)
                        form.render();
                        //编辑员工需要每一步都能保存:显示前两步的保存按钮
                        $("#step1Save").show();
                        $("#step2Save").show();
                    } else {//因是根据id查询,所以返回结果为0或超过1条时,数据必然错误,需检查数据库
                        layer.alert("数据异常,请联系管理员检查数据库", {icon: 2, title: "提示"}, function (index) {
                            layer.close(index);
                        });
                    }
                }
            });
        }

        if (face_info_input_mod == "0") {//录入方式为"仅读身份证"时,禁用输入
            $("#empName").addClass("layui-disabled").attr("disabled", true);
            $("#nation").addClass("layui-disabled").attr("disabled", true);
            $("#address").addClass("layui-disabled").attr("disabled", true);
            $("#IDCardDate").addClass("layui-disabled").attr("disabled", true);
            $("#IDCardNum").addClass("layui-disabled").attr("disabled", true);
        }

        if (ifIE()) {//IE浏览器开启读证定时任务,1秒1次
            t1 = window.setInterval("readIDCard()", 1000);
        } else {//非IE浏览器给出提示
            $("#message").text("(提示:当前浏览器非IE,如需读身份证请使用IE浏览器并连接读卡器!)");
        }

        //查询所有员工,用于回显选择上级领导的下拉框
        var step2param = {
            attribute: 0,
            status: 0
        };
        $.ajax({
            url: "/employeeManage/selectAllFaceInfo",
            type: "post",
            data: step2param,
            async: true,
            success: function (data) {
                //先把下拉框清空
                $("#leader").empty();
                var jsonData = $.parseJSON(data);
                var empList = jsonData.data;
                //拼接下拉框数据
                var selectEmpHTML = "<option value='' selected style='color: #c8c8c8'>请搜索/选择上级领导</option>";
                $.each(empList, function (index, value) {
                    var faceId = value.face_id;
                    var name = value.name;
                    var optionHTML = "<option value='" + faceId + "'>" + name + "</option>";
                    selectEmpHTML = selectEmpHTML + optionHTML;
                });
                $("#leader").append(selectEmpHTML);
                //回显编辑员工的上级领导
                if (editLeaderId != "") {
                    $("#leader").val(editLeaderId);
                }
                form.render('select');
            }
        });

        //查询设备权限
        var step3Param = {
            face_id: editFaceId,
            attribute: 0
        };
        table.render({
            elem: '#deviceAuthor'
            , url: '/employeeManage/queryDevice'
            //, height: 250
            //, height: 500
            , where: step3Param
            , cols: [
                [ //表头
                    {type: 'checkbox'}
                    , {field: 'term_name', title: '设备名称'}
                    , {field: 'depart_name', title: '所属部门'}
                ]
            ]
        });
        table.render({
            elem: '#carAuthor'
            , url: '/employeeManage/queryDevice'
            , height: 250
            , where: step3Param
            , cols: [
                [ //表头
                    {type: 'checkbox'}
                    , {field: 'term_name', title: '通过区域'}
                ]
            ]
        });

        //表单验证
        form.verify({
            nameRequired: function (value) {
                if (value == "") {
                    return '请输入姓名';
                }
            },
            depart: function (value) {
                if (value == "") {
                    return '请选择部门';
                }
            },
            IDCardNum: function (value) {
                if (value != "" && !checkIDCard(value)) {
                    return '请输入合法的身份证号码';
                } else if (value == "" && editIDCardIfExit) {
                    return '编辑员工不能删除身份证号码,只可做修改';
                }
            },
            telNum: function (value) {
                var IDCardNum = $("#IDCardNum").val();
                if (IDCardNum == "" && value == "") {
                    return '身份证号码与手机号码必须至少填写一项';
                } else if (value != "") {
                    reg = /0?(13|14|15|16|17|18|19)[0-9]{9}/;
                    if (!reg.test(value)) {
                        return '请输入合法的手机号码';
                    }
                }
            }
        });

        //监听单选和下拉框,如果有操作则更改操作标识
        form.on('radio(sex)', function () {
            $("#ifWriteFlag").val("1");
        });
        form.on('select(nation)', function () {
            $("#ifWriteFlag").val("1");
        });
        form.on('select(depart)', function () {
            $("#ifWriteFlag").val("1");
        });
        form.on('select(leader)', function () {
            $("#ifWriteFlag").val("1");
        });

        //操作input时更改操作标识
        $("#addEmployeeOne :input").change(function () {
            $("#ifWriteFlag").val("1");
        });
        $("#addEmployeeTwo :input").change(function () {
            $("#ifWriteFlag").val("1");
        });
        $("#addEmployeeThree :input").change(function () {
            $("#ifWriteFlag").val("1");
        });

        //第一步->第二步的事件
        form.on('submit(addEmployeeOneFilter)', function (obj) {
            //获取检测出身份证号所属的faceId,在身份证号Input失去焦点时会检测,为0则是数据库不存在的
            var checkFaceId1 = $("#IDCardNum").attr('checkFaceId');
            if (editFaceId != '0' && checkFaceId1 != '0') {//编辑员工的情况下,身份证号不能修改为数据库中其他人的
                layer.alert('身份证号码不能与已登记的员工或访客重复', {icon: 0, title: "提示"}, function (index) {
                    layer.close(index);
                });
            } else {
                //判断有无未通过比对的照片,若有则给出提示
                var ifPasscompare = false;
                if ($("#passCompare1").is(":hidden") && $("#passCompare2").is(":hidden") && $("#passCompare3").is(":hidden")) {
                    ifPasscompare = true;
                }
                if (!ifPasscompare) {
                    layer.alert('存在人证比对不通过的照片，如继续登记需更换照片或手动通过!', {icon: 0, title: "提示"}, function (index) {
                        layer.close(index);
                    });
                } else {
                    var IDCardNum = $("#IDCardNum").val();
                    if (IDCardNum == "") {//如果身份证号没填,则第二步的手机号必填
                        $("#telNumLabel").css("padding-right", "11px");
                        $("#telNumRequiredPhoto").show();
                    } else {
                        $("#telNumLabel").css("padding-right", "32px");
                        $("#telNumRequiredPhoto").hide();
                    }
                    //隐藏第一步的HTML,显示第二步的HTML
                    $("#addEmployeeOne").hide();
                    $("#addEmployeeTwo").show();
                    //关闭读证的定时任务(仅第一步有读证的需求)
                    clearInterval(t1);
                }
            }
        });

        //第二步->第三步的事件
        form.on('submit(addEmployeeTwoFilter)', function (obj) {
            //获取检测出身份证号所属的faceId,在身份证号Input失去焦点时会检测,为0则是数据库不存在的
            var checkFaceId1 = $("#IDCardNum").attr('checkFaceId');
            //获取检测出手机号所属的faceId,在身份证号Input失去焦点时会检测,为0则是数据库不存在的
            var checkFaceId2 = $("#telNum").attr('checkFaceId');
            //若是添加员工,则不允许身份证和手机号分别于不同的人相同。若是编辑员工,则身份证和手机号都不能与其他人相同
            if (editFaceId == '0' && checkFaceId1 !== '0' && checkFaceId2 !== '0' && checkFaceId1 !== checkFaceId2) {
                layer.alert('身份证号码和手机号码已分别被不同的员工或访客登记使用，不允许再次登记为同一人使用', {icon: 0, title: "提示"}, function (index) {
                    layer.close(index);
                });
            } else if (editFaceId != '0' && checkFaceId2 !== '0') {
                layer.alert('手机号码不能与已登记员工或访客重复', {icon: 0, title: "提示"}, function (index) {
                    layer.close(index);
                });
            } else {
                //加载设备权限
                table.reload('deviceAuthor');
                //隐藏第二步,显示第三步
                $("#addEmployeeTwo").hide();
                $("#addEmployeeThree").show();
            }
        });

        //点击第一步的保存按钮
        form.on('submit(addEmployeeOneFilterSave)', function (obj) {
            saveEmployee();
        });

        //点击第二步的保存按钮
        form.on('submit(addEmployeeTwoFilterSave)', function (obj) {
            saveEmployee();
        });

        //点击第三步的保存按钮
        form.on('submit(addEmployeeThreeFilter)', function (obj) {
            saveEmployee();
        });

        //保存
        function saveEmployee() {
            //开启加载转圈圈动画
            var loadingAddEmployee = layer.load(1, {
                shade: [0.1, '#fff']
            });
            //获取页面数据
            var checkStatus = table.checkStatus('deviceAuthor')
                , deviceList = checkStatus.data;
            var IDCardPhoto = IDCardPhotoData;
            var empName = $("input[name='empName']").val();
            var IDCardNum = $("input[name='IDCardNum']").val();
            var nation = $("#nation").val();
            var address = $("input[name='address']").val();
            var IDCardDate = $("input[name='IDCardDate']").val();
            var depart = $("#depart").val();
            var telPhoneNum = $("input[name='telPhoneNum']").val();
            var leader = $("#leader").val();
            var nickName = $("input[name='nickName']").val();
            var position = $("input[name='position']").val();
            var employeeId = $("input[name='employeeId']").val();
            var empcard = $("input[name='empcard']").val();
            var sex = $("input[name='sex']:checked").val();
            var faceInfo = {};
            if (editFaceId != "0") {
                faceInfo.face_id = editFaceId;
            } else {
                faceInfo.face_id = 0;
            }
            if (IDCardPhoto != "") {
                faceInfo.photo_wlt = IDCardPhoto;
                faceInfo.type = 1;
            }
            var pictureList = new Array();
            //获取三张比对照片
            for (var i = 1; i <= 3; i++) {
                var photoId = "photo" + i;
                var pass = $("#" + photoId).attr("pass");//是否通过比对
                var picId = $("#" + photoId).attr('picId');//照片id
                var src = $("#" + photoId).attr('src');//照片数据
                //照片为空并且ID为0,则不传入后台
                var flag1 = (src == "" || src == undefined || src == "/css/img/comparePhotoDefault.png") && picId == "0";
                //照片不为空且pass为0,则不传入后台
                var flag2 = (src !== "" && src !== undefined && src !== "/css/img/comparePhotoDefault.png" && pass == "0");
                if (flag1 || flag2) {
                    //不操作
                } else {
                    var option = $("#" + photoId).attr('option');
                    var picture = {};
                    if (option == "1") {//新增或修改
                        var faceWidth = $("#" + photoId).attr('faceWidth');
                        var faceHeight = $("#" + photoId).attr('faceHeight');
                        //比对照片的数据是调用人脸检测接口返回的,所以不用过滤格式
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
                    } else {//删除或不修改,不需要传入图片数据
                        picture = {
                            pic_id: picId,
                            option: option
                        };
                    }
                    pictureList.push(picture);
                }
            }
            //获取头像
            var photo4 = $("#photo4").attr('src');
            if (undefined !== photo4 && "" !== photo4 && "/css/img/headPhotoDefault.png" != photo4) {
                //头像没有进行人脸检测,需要过滤格式
                photo4 = photo4.replace('data:;base64,', '');
                photo4 = photo4.replace('data:image/jpeg;base64,', '');
                photo4 = photo4.replace('data:image/png;base64,', '');
                photo4 = photo4.replace('data:image/bmp;base64,', '');
                faceInfo.face_info_base64 = photo4;
            } else {//若不存在头像,则获取页面中头像的照片id,若有照片id,则是删除了头像
                var photo4Id = $("#photo4").attr('picId');
                if (photo4Id != '0') {
                    var picture = {
                        pic_id: photo4Id,
                        option: 2,//option为2是删除操作
                        image_type: 1//image_type为1是头像
                    };
                    pictureList.push(picture);
                }
            }
            if (leader != "请选择") {
                faceInfo.leader_id = leader;
            }
            if (nation != "请选择") {
                faceInfo.nation = nation;
            }
            faceInfo.name = empName;
            faceInfo.sex = sex;
            faceInfo.idcard = IDCardNum;
            faceInfo.addr = address;
            faceInfo.idcard_expire = IDCardDate;
            faceInfo.depart_id = depart;
            faceInfo.tel_no = telPhoneNum;
            faceInfo.empcard = empcard;
            faceInfo.employee_id = employeeId;
            faceInfo.position = position;
            faceInfo.nick_name = nickName;
            faceInfo.attribute = 0;
            faceInfo.status = 0;
            faceInfo.pictures = pictureList;
            $.ajax({//调用添加faceInfo接口
                url: "/employeeManage/addFaceInfo",
                type: "post",
                data: JSON.stringify(faceInfo),
                contentType: 'application/json',
                async: false,
                traditional: true,
                success: function (data) {
                    var jsonData = $.parseJSON(data);
                    if (jsonData.resultCode == "0") {//添加faceInfo成功
                        var termIdList = new Array();
                        $.each(deviceList, function (i, item) {
                            termIdList.push(item.term_id);
                        });
                        var facePermitParam = {
                            faceInfo: jsonData.resultData,
                            termIdList: termIdList
                        };
                        $.ajax({//配置设备权限
                            url: "/employeeManage/facePermitConfig",
                            type: "post",
                            data: JSON.stringify(facePermitParam),
                            contentType: 'application/json',
                            async: false,
                            traditional: true,
                            success: function (data) {
                                if (jsonData.resultCode == "0") {
                                    if (editFaceId != "0") {
                                        layer.alert('编辑成功', {icon: 1, title: "提示"}, function () {
                                            window.parent.location.reload();
                                        });
                                    } else {
                                        layer.alert('添加成功', {icon: 1, title: "提示"}, function () {
                                            window.parent.location.reload();
                                        });
                                    }
                                } else {
                                    if (editFaceId != "0") {
                                        layer.alert('编辑员工设备权限失败,错误代码:' + jsonData.resultCode, {
                                            icon: 2,
                                            title: "提示"
                                        }, function () {
                                            window.parent.location.reload();
                                        });
                                    } else {
                                        layer.alert('添加员工设备权限失败,错误代码:' + jsonData.resultCode, {
                                            icon: 2,
                                            title: "提示"
                                        }, function () {
                                            window.parent.location.reload();
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
                    } else {
                        if (editFaceId != "0") {
                            layer.alert('编辑员工基本信息失败,错误代码:' + jsonData.resultCode, {icon: 2, title: "提示"}, function (index, layero) {
                                layer.close(index);
                            });
                        } else {
                            if (jsonData.resultCode==-16) {
                                layer.alert('添加员工基本信息失败,照片不符合要求', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else if (jsonData.resultCode==1) {
                                layer.alert('添加员工基本信息失败,信息保存失败', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else if (jsonData.resultCode==2) {
                                layer.alert('添加员工基本信息失败,身份证号码冲突', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else if (jsonData.resultCode==3) {
                                layer.alert('添加员工基本信息失败,手机号码冲突', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else if (jsonData.resultCode==5) {
                                layer.alert('添加员工基本信息失败,Openid冲突', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else if (jsonData.resultCode==-1) {
                                layer.alert('添加员工基本信息失败,操作失败', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else if (jsonData.resultCode==-13) {
                                layer.alert('添加员工基本信息失败,图片格式不正确', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else if (jsonData.resultCode==-14) {
                                layer.alert('添加员工基本信息失败,未检测到人脸', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else if (jsonData.resultCode==-15) {
                                layer.alert('添加员工基本信息失败,人脸图片建库超时', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else if (jsonData.resultCode==-17) {
                                layer.alert('添加员工基本信息失败,图片身份不确定', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else if (jsonData.resultCode==-18) {
                                layer.alert('添加员工基本信息失败,超时', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else if (jsonData.resultCode==-99) {
                                layer.alert('添加员工基本信息失败,不支持该命令', {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }else {
                                layer.alert('添加员工基本信息失败,错误代码:' + jsonData.resultCode, {icon: 2, title: "提示"}, function (index, layero) {
                                    layer.close(index);
                                });
                            }
                        }
                    }
                },
                error: function () {
                    layer.alert('服务器错误,请联系管理员', {icon: 2, title: "提示"}, function (index) {
                        layer.close(index);
                    });
                }
            });
            //关闭转圈圈的加载框
            layer.close(loadingAddEmployee);
        }

        //第一张比对照片
        var uploadListIns1 = upload.render({
            elem: '#uploadPhoto1'
            , auto: false
            , acceptMime: '.jpg,.png,.bmp' //限制选择本地文件的类型
            , size: photoMaxSize //最大图片kb
            , choose: function (obj) {
                obj.preview(function (index, file, result) {
                    //修改已经操作的标识位
                    $("#ifWriteFlag").val("1");
                    //清除缓存
                    uploadListIns1.config.elem.next()[0].value = '';
                    //检测人脸。参数依次是照片数据,比对照片的序号,图片的来源(上传)
                    checkFaceNum(result, "1", "upload");
                });
            }
        });

        //第二张比对照片
        var uploadListIns2 = upload.render({
            elem: '#uploadPhoto2'
            , auto: false
            , acceptMime: '.jpg,.png,.bmp'
            , size: photoMaxSize
            , choose: function (obj) {
                obj.preview(function (index, file, result) {
                    $("#ifWriteFlag").val("1");
                    uploadListIns2.config.elem.next()[0].value = '';
                    checkFaceNum(result, "2", "upload");
                });
            }
        });

        //第三张比对照片
        var uploadListIns3 = upload.render({
            elem: '#uploadPhoto3'
            , auto: false
            , acceptMime: '.jpg,.png,.bmp'
            , size: photoMaxSize
            , choose: function (obj) {
                obj.preview(function (index, file, result) {
                    $("#ifWriteFlag").val("1");
                    uploadListIns3.config.elem.next()[0].value = '';
                    checkFaceNum(result, "3", "upload");
                });
            }
        });

        //头像
        var uploadListIns4 = upload.render({
            elem: '#uploadPhoto4'
            , auto: false
            , acceptMime: '.jpg,.png,.bmp'
            , size: 1024
            , choose: function (obj) {
                obj.preview(function (index, file, result) {
                    $("#ifWriteFlag").val("1");
                    uploadListIns4.config.elem.next()[0].value = '';
                    //根据图片原本宽高比压缩
                    $("#hidePhoto4").attr('src', result);
                    var theImage = new Image();
                    theImage.src = $("#hidePhoto4").attr("src");
                    theImage.onload = function () {
                        var width = "164";
                        var height = "164";
                        $("#photo4").attr('width', "164");
                        $("#photo4").attr('height', "164");
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
    });

    //回到第一步
    function backAddEmployeeOne() {
        $("#addEmployeeOne").show();
        $("#addEmployeeTwo").hide();
        if (ifIE()) {//是IE
            t1 = window.setInterval("readIDCard()", 1000);
        } else {
            $("#message").text("(提示:当前浏览器非IE,如需读身份证请使用IE浏览器并连接读卡器!)");
        }
    }

    //回到第二步
    function backAddEmployeeTwo() {
        $("#addEmployeeTwo").show();
        $("#addEmployeeThree").hide();
    }

    //身份证input框失去焦点事件
    $("#IDCardNum").blur(function () {
        var IDCardNum = $("#IDCardNum").val();
        if (IDCardNum != "" && !checkIDCard(IDCardNum)) {
            layer.msg('请输入合法的身份证号码', {icon: 0, anim: 6});
        } else if (IDCardNum.length == 18) {
            var param = {
                idcard: IDCardNum,
                attribute: 0,
                face_id: 0,
                depart_id: 0,
                status: 1
            };
            checkFaceInfo(param, "身份证号", function () {
            });
        }
    });

    //手机号input框失去焦点事件
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
                status: 1
            };
            checkFaceInfo(param, "手机号", function () {
            });
        }
    });

    //效验身份证号和手机号是否重复
    function checkFaceInfo(param, paramName, readCardCallBack) {
        //效验期间不许读证,清除定时任务
        clearInterval(t1);
        //先检测是否是离职员工,再检测是否是员工,最后检测访客
        $.ajax({
            url: "/visitorManage/selectByParam",
            type: "post",
            data: param,
            async: false,
            success: function (data) {
                var jsonData = $.parseJSON(data);
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
                                layer.confirm("此" + paramName + "为已离职员工[" + depatName + ":" + jsonData.data[0].name + "],是否继续登记?",
                                    {
                                        btn: ['是', '否'], icon: 3, cancel: function () {
                                            parent.layer.closeAll();
                                        }, end: function () {
                                            if (ifIE() && paramName == "身份证号") {
                                                t1 = window.setInterval("readIDCard()", 1000);
                                            }
                                        }
                                    },
                                    function (index) {
                                        readCardCallBack();
                                        layer.close(index);
                                    },
                                    function () {
                                        parent.layer.closeAll();
                                    }
                                );
                            } else {
                                layer.confirm("此" + paramName + "为已离职员工[" + jsonData.data[0].name + "],是否继续登记?",
                                    {
                                        btn: ['是', '否'], icon: 3, cancel: function () {
                                            parent.layer.closeAll();
                                        }, end: function () {
                                            //如果当前在检测身份证号,提示框关闭时重新启用读卡定时任务
                                            if (ifIE() && paramName == "身份证号") {
                                                t1 = window.setInterval("readIDCard()", 1000);
                                            }
                                        }
                                    },
                                    function (index) {
                                        readCardCallBack();
                                        layer.close(index);
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
                        t1 = window.setInterval("readIDCard()", 1000);
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
                            jsonData = $.parseJSON(data);
                            if (jsonData.count == 1) {
                                var checkFaceId = jsonData.data[0].face_id;
                                if (checkFaceId != editFaceId) {//排除编辑员工是本人的情况
                                    //需要把检测出的faceId赋值给相应的元素
                                    if (paramName == "身份证号") {
                                        $("#IDCardNum").attr('checkFaceId', checkFaceId);
                                    }
                                    if (paramName == "手机号") {
                                        $("#telNum").attr('checkFaceId', checkFaceId);
                                    }
                                    layer.confirm("已存在使用此" + paramName + "登记的员工[" + jsonData.data[0].name + "],是否继续使用此" + paramName + "登记?如继续,则会修改此员工的信息!", {
                                            icon: 3, title: "提示",
                                            btn: ['继续', '取消'], cancel: function () {
                                                //直接关闭提示框会清空
                                                if (paramName == "身份证号") {
                                                    $("#IDCardNum").val("");
                                                }
                                                if (paramName == "手机号") {
                                                    $("#telNum").val("");
                                                }
                                            }, end: function () {
                                                //提示框消失时,如果是身份证检测,则会重新开启读卡的定时任务
                                                if (ifIE() && paramName == "身份证号") {
                                                    t1 = window.setInterval("readIDCard()", 1000);
                                                }
                                            }
                                        },
                                        function (index) {
                                            //点击确认,执行回调,只有读卡才有回调,其它情况的回调为空
                                            readCardCallBack();
                                            layer.close(index);
                                        },
                                        function (index) {
                                            //点击取消,会清空
                                            if (paramName == "身份证号") {
                                                $("#IDCardNum").val("");
                                            }
                                            if (paramName == "手机号") {
                                                $("#telNum").val("");
                                            }
                                            layer.close(index);
                                        }
                                    );
                                } else {//如果检测出为编辑员工本人,则不用进行后续的检测.如果为身份证号,则重新开启读卡
                                    if (ifIE() && paramName == "身份证号") {
                                        t1 = window.setInterval("readIDCard()", 1000);
                                    }
                                }
                            } else if (jsonData.count > 1) {//同理,数据必然<=1条,超过1条需检查数据库
                                layer.alert("此" + paramName + "已登记过多个员工,数据异常,请联系管理员检查数据库", {
                                    icon: 2,
                                    title: "提示"
                                }, function (index) {
                                    layer.close(index);
                                });
                            } else {
                                //如果离职员工和在职员工都没查到,再查访客
                                param.attribute = 1;
                                $.ajax({
                                    url: "/visitorManage/selectByParam",
                                    type: "post",
                                    data: param,
                                    async: false,
                                    success: function (data) {
                                        jsonData = $.parseJSON(data);
                                        if (jsonData.count == 1) {
                                            //赋值检查的faceId
                                            var checkFaceId2 = jsonData.data[0].face_id;
                                            if (paramName == "身份证号") {
                                                $("#IDCardNum").attr('checkFaceId', checkFaceId2);
                                            }
                                            if (paramName == "手机号") {
                                                $("#telNum").attr('checkFaceId', checkFaceId2);
                                            }
                                            layer.alert("此" + paramName + "已登记过访客", {
                                                icon: 0, title: "提示", end: function () {
                                                    if (ifIE() && paramName == "身份证号") {
                                                        t1 = window.setInterval("readIDCard()", 1000);
                                                    }
                                                    readCardCallBack();
                                                }
                                            }, function (index) {
                                                layer.close(index);
                                            });
                                        } else if (jsonData.count > 1) {
                                            layer.alert("此" + paramName + "已登记过多个访客,数据错误,请联系管理员", {
                                                icon: 2,
                                                title: "提示"
                                            }, function (index) {
                                                layer.close(index);
                                            });
                                        } else {
                                            if (ifIE() && paramName == "身份证号") {
                                                t1 = window.setInterval("readIDCard()", 1000);
                                            }
                                            readCardCallBack();
                                            //都没检测出重复的,令checkFaceId=0
                                            if (paramName == "身份证号") {
                                                $("#IDCardNum").attr('checkFaceId', '0');
                                            }
                                            if (paramName == "手机号") {
                                                $("#telNum").attr('checkFaceId', '0');
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

    //读证
    function readIDCard() {
        var result;
        var cardtype;
        try {
            var ax = new ActiveXObject("IDRCONTROL.IdrControlCtrl.1");
            var IdrControl1 = document.getElementById("IdrControl1");
            result = IdrControl1.RepeatRead(0);   //设置是否重复读卡  0-不重复  1-重复
            //  result=IdrControl1.setMute(1);   //设置是否静音读卡  0-不静音  1-静音
            //注意：第一个参数为对应的设备端口，USB型为1001，串口型为1至16
            result = IdrControl1.ReadCard("1001", "c:\\ocx\\test.jpg");
            cardtype = IdrControl1.DecideReadCardType();//判断卡类型 1-身份证 2-外国居留证
            if (result == 1) {//读卡器连接正常
                $("#message").text("(提示:读卡器连接正常,可刷身份证!)");
                //显示身份证照片的框
                $("#IDCardPhotoDiv").show();
                if (cardtype == 1) {//成功读到信息
                    var IDCardNum = IdrControl1.GetCode();
                    //效验身份证重复
                    var param = {
                        idcard: IDCardNum,
                        attribute: 0,
                        face_id: 0,
                        depart_id: 0,
                        status: 1
                    };
                    checkFaceInfo(param, "身份证号", function () {
                        //效验通过后的回调,回显各种信息,checkFaceInfo的回调只有这里有,其他情况调用checkFaceInfo的回调都为空

                        $("input[name='IDCardNum']").val(IDCardNum);
                        $("input[name='empName']").val(IdrControl1.GetName());
                        var sex = IdrControl1.GetSex();
                        if (sex == "男") {
                            $('#man').next().click();
                        }
                        if (sex == "女") {
                            $('#woman').next().click();
                        }
                        //民族需要加"族"这个字
                        $("#nation").val(IdrControl1.GetFolk() + "族");
                        $("input[name='address']").val(IdrControl1.GetAddress());
                        $("input[name='IDCardDate']").val(IdrControl1.GetValid());
                        $("#IDCardPhoto").attr('src', 'data:image/jpeg;base64,' + IdrControl1.GetJPGPhotobuf());
                        IDCardPhotoData = IdrControl1.GetWLTbuf();
                        //依次与三张比对照片进行比对
                        for (var i = 1; i <= 3; i++) {
                            var photoId = "photo" + i;
                            var photoSrc = $("#" + photoId).attr('src');
                            if (undefined !== photoSrc && "" !== photoSrc && "/css/img/comparePhotoDefault.png" !== photoSrc) {
                                photoSrc = photoSrc.replace('data:;base64,', '');
                                photoSrc = photoSrc.replace('data:image/jpeg;base64,', '');
                                photoSrc = photoSrc.replace('data:image/png;base64,', '');
                                photoSrc = photoSrc.replace('data:image/bmp;base64,', '');
                                comparePhoto(IdrControl1.GetJPGPhotobuf(), photoSrc, i);
                            }
                        }
                        layui.use(['form'], function () {
                            var form = layui.form;
                            form.render();
                        });
                    });
                } else {
                    $("#message").text("(提示:当前卡片不是身份证,请将身份证放到读卡器上!)");
                }
            } else {
                if (result == -1) {
                    $("#message").text("(提示:未检测到身份证读卡器设备,如需使用读身份证功能请将读卡器连接至PC!)");
                }
                if (result == -2) {
                    $("#message").text("(提示:读卡器连接正常,可刷身份证!)");
                    $("#IDCardPhotoDiv").show();
                }
                if (result == -3) {
                    $("#message").text("(提示:读取数据失败!)");
                }
                if (result == -4) {
                    $("#message").text("(提示:生成照片文件失败，请检查设定路径和磁盘空间!)");
                }
            }
        } catch (e) {
            if (e.message.indexOf("GetWLT") == -1) {
                var downloadOCXHTML = "提示:身份证读卡控件未安装!<a style='color: #54b5ff;text-decoration:underline' href='/employeeManage/downloadPlugin?pluginName=ocx'>点此下载</a>";
                $("#message").html(downloadOCXHTML);
                window.clearInterval(t1);
            }
        }
    }

    //56个民族下拉框
    var nations = ["汉族", "蒙古族", "回族", "藏族", "维吾尔族", "苗族", "彝族", "壮族", "布依族", "朝鲜族", "满族", "侗族", "瑶族", "白族",
        "土家族", "哈尼族", "哈萨克族", "傣族", "黎族", "傈僳族", "佤族", "畲族", "高山族", "拉祜族", "水族", "东乡族", "纳西族", "景颇族", "柯尔克孜族", "土族",
        "达斡尔族", "仫佬族", "羌族", "布朗族", "撒拉族", "毛南族", "仡佬族", "锡伯族", "阿昌族", "普米族", "塔吉克族", "怒族", "乌孜别克族", "俄罗斯族", "鄂温克族",
        "德昂族", "保安族", "裕固族", "京族", "塔塔尔族", "独龙族", "鄂伦春族", "赫哲族", "门巴族", "珞巴族", "基诺族"];
    var option = "";
    for (var i = 0; i <
    nations.length; i++) {
        option += '<option value="' + nations[i] + '">' + nations[i] + '</option>';
    }
    $("#nation").append(option);
</script>
<script type="text/javascript" src="/js/photoJS.js"></script>
</html>