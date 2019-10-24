<style>
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
        height: 32px;
        line-height: 32px;
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

    tr th:nth-of-type(1) {
        border-left: 0px;
    }

    tr td:nth-of-type(1) {
        border-left: 0px;
    }

    tr th:nth-of-type(4) {
        border-right: 0px;
    }

    tr td:nth-of-type(4) {
        border-right: 0px;
    }

</style>
<form class='layui-form' id='visitorRegisterThree' style="display: none;">
    <div id="nav">
        <div style="width: 100%;float: left;height: 24px;"></div>
        <div style="width: 100%;float: left;height: 24px;">
            <div style="width: 178px;float: left;height: 100%;"></div>
            <div style="width: 24px;float: left;height: 100%;">
                <img src="/css/img/step1blue.png">
            </div>
            <div style="width: 20px;float: left;height: 100%;"></div>
            <div style="width: 490px;float: left;height: 100%;" class="progressWidth">
                <div class="layui-progress" style="background-color: #54b5ff;height: 2px;margin-top: 11px;"></div>
            </div>
            <div style="width: 20px;float: left;height: 100%;"></div>
            <div style="width: 24px;float: left;height: 100%;">
                <img src="/css/img/step2blue.png">
            </div>
            <div style="width: 20px;float: left;height: 100%;"></div>
            <div style="width: 490px;float: left;height: 100%;" class="progressWidth">
                <div class="layui-progress" style="background-color: #54b5ff;height: 2px;margin-top: 11px;"></div>
            </div>
            <div style="width: 20px;float: left;height: 100%;"></div>
            <div style="width: 24px;float: left;height: 100%;">
                <img src="/css/img/step3blue.png">
            </div>
        </div>
        <div style="width: 100%;float: left;height: 12px;"></div>
        <div style="width: 100%;float: left;height: 20px;">
            <div style="width: 158px;float: left;height: 100%;"></div>
            <div style="width: 64px;float: left;height: 100%;font-size: 16px;color: #54b5ff;">来访信息</div>
            <div style="width: 490px;float: left;height: 100%;" class="progressWidth"></div>
            <div style="width: 64px;float: left;height: 100%;font-size: 16px;color: #54b5ff;">权限信息</div>
            <div style="width: 490px;float: left;height: 100%;" class="progressWidth"></div>
            <div style="width: 64px;float: left;height: 100%;font-size: 16px;color: #54b5ff;">访客信息</div>
        </div>
    </div>
    <div id="content" style="overflow-x: auto; overflow-y: auto;width:100%;">
        <div style="width: 100%;float: left;height: 18px;"></div>
        <div style="width: 765px;float: left;" id="registerDiv">
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
            <object classid="CLSID:5EB842AE-5C49-4FD8-8CE9-77D4AF9FD4FF" id="IdrControl1" width="0" height="0"></object>
            <div id="message" style="width: 100%;height: 20px;float: left;text-align: center;color: #F55366;"></div>
            <#--刷证自动保存的DIV,仅在可正常读卡的情况下并且是新增才会显示-->
            <div id="onlyBrushCardDiv" style="width: 100%;height: 46px;float: left;display: none;">
                <div style="width: 31px;height: 100%;float: left;"></div>
                <div style="width: 600px;height: 100%;float: left;">
                    <div class="layui-form-item" style="margin-bottom: 14px;">
                        <label class="layui-form-label" style="line-height: 14px;padding-right: 32px;color: #333333;">刷证自动保存</label>
                        <div class="layui-input-inline">
                            <input type="checkbox" id="brushCardAutoSave" lay-filter="brushCardAutoSave"
                                   lay-skin="switch">
                        </div>
                    </div>
                </div>
            </div>
            <div style="width: 31px;height: 246px;float: left;"></div>
            <div style="width: 568px;height: 246px;float: left;">
                <div class='layui-form-item' style="margin-bottom: 14px;">
                    <label class='layui-form-label'
                           style="line-height: 14px;padding-right: 11px;color: #333333;">姓名</label>
                    <img src="/css/img/required.png" style="margin-top: 11px;float: left;margin-right: 11px;">
                    <div class='layui-input-inline' style='width: 410px;'>
                        <input type='text' id='visitorName' lay-verify='nameRequired|name' placeholder="请输入访客姓名..."
                               maxlength="20" style="height: 32px;line-height: 32px;border-radius: 4px;"
                               class='layui-input addDisabled'>
                    </div>
                </div>
                <div class='layui-form-item' style="margin-bottom: 14px;">
                    <label class='layui-form-label'
                           style="line-height: 14px;padding-right: 11px;color: #333333;">性别</label>
                    <img src="/css/img/required.png" style="margin-top: 11px;float: left;margin-right: 11px;">
                    <div class='layui-input-inline' style="width: 170px;">
                        <input lay-filter="sex" type='radio' class="addDisabled" style="height: 32px;line-height: 32px;"
                               id="man" name='sex' value='1' title='男' checked/>
                        <input lay-filter="sex" type='radio' class="addDisabled" style="height: 32px;line-height: 32px;"
                               id="woman" name='sex' value='2' title='女'/>
                    </div>
                    <label class='layui-form-label'
                           style="line-height: 14px;padding-right: 22px;color: #333333;width: 70px;">民族</label>
                    <div class='layui-input-inline' style='width: 123px;'>
                        <select lay-filter="nation" id='nation' name='nation' class='layui-input addDisabled'
                                style="height: 32px;line-height: 32px;border-radius: 4px;" lay-verify="" lay-search>
                            <option value="请选择" selected>请选择</option>
                        </select>
                        <#--<input type='text' id='nation' name='nation' class='layui-input' style="height: 32px;line-height: 32px;border-radius: 4px;">-->
                    </div>
                </div>
                <div class='layui-form-item' style="margin-bottom: 14px;">
                    <label id="IDCardNumLabel" class='layui-form-label'
                           style="line-height: 14px;padding-right: 32px;color: #333333;">身份证号</label>
                    <img id="IDCardNumRequiredImg" src="/css/img/required.png"
                         style="margin-top: 11px;float: left;margin-right: 11px;display: none;">
                    <div class='layui-input-inline' style='width: 410px;'>
                        <input type='text' maxlength="18" id='IDCardNum' placeholder="请输入访客身份证号码..." checkFaceId="0"
                               attribute="1" lay-verify='IDCardNum'
                               style="height: 32px;line-height: 32px;border-radius: 4px;"
                               class='layui-input addDisabled'>
                    </div>
                </div>
                <div class='layui-form-item' style="margin-bottom: 14px;">
                    <label class='layui-form-label'
                           style="line-height: 14px;padding-right: 32px;color: #333333;">地址</label>
                    <div class='layui-input-inline' style='width: 410px;'>
                        <input type='text' id="address" lay-verify='addr' name='address' placeholder="请输入访客地址..." maxlength="80"
                               style="height: 32px;line-height: 32px;border-radius: 4px;"
                               class='layui-input addDisabled'>
                        <#--<p id="name"  style="width:150px;white-space:nowrap;font-size:20px;color:#1E9FFF; text-overflow:ellipsis; overflow:hidden;float:left" th:title="${names}" th:text="${names}" ></p>-->
                    </div>
                </div>
                <div class='layui-form-item' style="margin-bottom: 30px;">
                    <label class='layui-form-label'
                           style="line-height: 14px;padding-right: 32px;color: #333333;">有效期</label>
                    <div class='layui-input-inline' style='width: 410px;'>
                        <input type='text' id="IDCardDate" name='IDCardDate' disabled
                               style="height: 32px;line-height: 32px;border-radius: 4px;"
                               class='layui-input addDisabled'>
                    </div>
                </div>
            </div>
            <div style="width: 34px;height: 246px;float: left;"></div>
            <div id="IDCardPhotoDiv" style="width: 104px;height : 128px;float: left;display: none;">
                <img width='102' class="layui-upload-img clearImg" id="IDCardPhoto"
                     style="border: 1px solid #d8d8d8;border-radius: 4px;" height='126'
                     src="/css/img/IDCardPhotoDefault.png"/>
            </div>

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
            <div id="message2" style="width: 100%;height: 28px;float: left;text-align: center;color: #F55366;"></div>
            <div style="width: 100%;float: left;height: 249px;">
                <div id="suff" style="width: 178px;height: 100%;float: left;"></div>
                <div style="width: 142px;height: 100%;float: left;">
                    <div style="width: 100%;height: 176px;text-align: center;line-height: 176px;">
                        <img class='layui-upload-img' id="photo1" width='142' height='176' picId="0" option="1" pass="0"
                             style="border: 1px solid #d8d8d8;border-radius: 4px;"
                             src="/css/img/comparePhotoDefault.png">
                        <img id="hidePhoto1" style="display: none;">
                    </div>
                    <div style="width: 100%;height: 8px;"></div>
                    <div style="width: 100%;height: 30px;">
                        <button type='button' class="button1 uploadBtn" id="uploadPhoto1">上传</button>
                        <button type='button' class="button1 takePhoto" onclick="takePhoto(1)"
                                style="margin-left: 1px;">拍照
                        </button>
                        <button type='button' class="button1" onclick="deletePhoto(1)" style="margin-left: 1px;">删除
                        </button>
                    </div>
                    <div style="width: 100%;height: 5px;"></div>
                    <div style="width: 100%;height: 30px;">
                        <button type='button' class="button1" id="passCompare1" onclick="passCompare(1)"
                                style="width: 142px;display: none;">手动通过
                        </button>
                    </div>
                </div>
                <div style="width: 80px;height: 100%;float: left;"></div>
                <div style="width: 142px;height: 100%;float: left;">
                    <div style="width: 100%;height: 176px;text-align: center;line-height: 176px;">
                        <img class='layui-upload-img' id="photo2" width='142' height='176' picId="0" option="1" pass="0"
                             style="border: 1px solid #d8d8d8;border-radius: 4px;"
                             src="/css/img/comparePhotoDefault.png">
                        <img id="hidePhoto2" style="display: none;">
                    </div>
                    <div style="width: 100%;height: 8px;"></div>
                    <div style="width: 100%;height: 30px;">
                        <button type='button' class="button1 uploadBtn" id="uploadPhoto2">上传</button>
                        <button type='button' class="button1 takePhoto" onclick="takePhoto(2)"
                                style="margin-left: 1px;">拍照
                        </button>
                        <button type='button' class="button1" onclick="deletePhoto(2)" style="margin-left: 1px;">删除
                        </button>
                    </div>
                    <div style="width: 100%;height: 5px;"></div>
                    <div style="width: 100%;height: 30px;">
                        <button type='button' class="button1" id="passCompare2" onclick="passCompare(2)"
                                style="width: 142px;display: none;">手动通过
                        </button>
                    </div>
                </div>
                <div style="width: 80px;height: 100%;float: left;"></div>
                <div style="width: 142px;height: 100%;float: left;">
                    <div style="width: 100%;height: 176px;text-align: center;line-height: 176px;">
                        <img class='layui-upload-img' id="photo3" width='142' height='176' picId="0" option="1" pass="0"
                             style="border: 1px solid #d8d8d8;border-radius: 4px;"
                             src="/css/img/comparePhotoDefault.png">
                        <img id="hidePhoto3" style="display: none;">
                    </div>
                    <div style="width: 100%;height: 8px;"></div>
                    <div style="width: 100%;height: 30px;">
                        <button type='button' class="button1 uploadBtn" id="uploadPhoto3">上传</button>
                        <button type='button' class="button1 takePhoto" onclick="takePhoto(3)"
                                style="margin-left: 1px;">拍照
                        </button>
                        <button type='button' class="button1" onclick="deletePhoto(3)" style="margin-left: 1px;">删除
                        </button>
                    </div>
                    <div style="width: 100%;height: 5px;"></div>
                    <div style="width: 100%;height: 30px;">
                        <button type='button' class="button1" id="passCompare3" onclick="passCompare(3)"
                                style="width: 142px;display: none;">手动通过
                        </button>
                    </div>
                </div>
            </div>
            <div style="width: 100%;height: 19px;float: left;"></div>
            <div style="width: 100%;float: left;height: 20px;">
                <div style="width: 3%;float: left;height: 100%;">
                </div>
                <div style="width: 97%;float: left;height: 100%;">
                    <fieldset class="layui-elem-field layui-field-title" style="margin: 0;float: left;width: 100%;">
                        <legend style="color: #999999;font-size: 14px;margin-left: 0;padding-left: 0;padding-right: 12px;">
                            其它信息
                        </legend>
                    </fieldset>
                </div>
            </div>
            <div style="width: 599px;height: 198px;float: left;">
                <div style="width: 100%;height: 28px;float: left;"></div>
                <div style="width: 31px;height: 170px;float: left;"></div>
                <div style="width: 568px;height: 170px;float: left;">
                    <div class='layui-form-item' style="margin-bottom: 14px;">
                        <label id="visitorTelNumLabel" class='layui-form-label'
                               style="line-height: 14px;padding-right: 11px;color: #333333;">手机号</label>
                        <img id="telNumRequiredImg" src="/css/img/required.png"
                             style="margin-top: 11px;float: left;margin-right: 11px;">
                        <div class='layui-input-inline' style='width: 410px;'>
                            <input type='text' checkFaceId="0" attribute="1" placeholder="请输入访客手机号码..."
                                   id='visitorTelNum'
                                   onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)"
                                   lay-verify="telNumRequired" maxlength="11" class='layui-input addDisabled'
                                   style="height: 32px;line-height: 32px;border-radius: 4px;">
                        </div>
                    </div>
                    <div class='layui-form-item' style="margin-bottom: 14px;">
                        <label class='layui-form-label'
                               style="line-height: 14px;padding-right: 32px;color: #333333;">昵称</label>
                        <div class='layui-input-inline' style='width: 410px;'>
                            <input maxlength="10" type='text' id='visitorNickName' lay-verify="nickName" placeholder="请输入访客昵称..."
                                   class='layui-input addDisabled'
                                   style="height: 32px;line-height: 32px;border-radius: 4px;">
                        </div>
                    </div>
                    <div class='layui-form-item' style="margin-bottom: 14px;">
                        <label class='layui-form-label' style="line-height: 14px;padding-right: 32px;color: #333333;">公司名称</label>
                        <div class='layui-input-inline' style='width: 410px;'>
                            <input maxlength="40" type='text' id='visitorCompanyName' lay-verify="visitorCompanyName" placeholder="请输入访客所在公司名称..."
                                   class='layui-input addDisabled'
                                   style="height: 32px;line-height: 32px;border-radius: 4px;">
                        </div>
                    </div>
                    <div class='layui-form-item' style="margin-bottom: 14px;">
                        <label class='layui-form-label'
                               style="line-height: 14px;padding-right: 32px;color: #333333;">职位</label>
                        <div class='layui-input-inline' style='width: 410px;'>
                            <input maxlength="10" type='text' id='visitorPosition' lay-verify="position"  placeholder="请输入访客职位..."
                                   class='layui-input addDisabled'
                                   style="height: 32px;line-height: 32px;border-radius: 4px;">
                        </div>
                    </div>
                    <!-- style="width:600px;height:100px;background:#d8d8d8;"   -->
                    <div class="layui-form-item" >
                    	<!-- 屏蔽 -->
                    	<!-- <label class='layui-form-label' style="line-height: 14px;padding-right: 32px;color: #333333;">车辆信息</label>
                    	<div class='layui-input-inline' style='width: 410px;' id="cars"></div>
                    	<div class='layui-input-inline' style='width: 410px;margin-top:4px;float:right;'>
                        	<img src="/css/img/addImg1.png" style="width:24px;height:24px;float:left;">
                        	<input type="button" value="新增车辆" onclick="addCar(0)" />
                        </div> -->
                    	<!-- <div id="car1" class="layui-input-inline" style="margin:10px 0px 0px 10px;display:inline;">
                    		<input type="text" id="carNum1" name="carNum1" readonly class="layui-input" style="height: 32px;line-height: 32px;border-radius: 4px;width:80px;float:left;" value="鄂A·12345">
                    		<button type="button" onclick="deleteCar(1)"><img src="/css/img/delete2.png" style="float:left;width:32;htight: 32px;margin:2px 2px 1px 2px;"></button>
                    	</div> -->
                    	
                    </div>
                    <!-- 新增车辆 -->
                    
                    <!-- <div class='layui-form-item' style="margin-bottom: 14px;">
                        <label class='layui-form-label' style="line-height: 14px;padding-right: 32px;color: #333333;"></label>
                        <div class='layui-input-inline' style='width: 410px;'>
                        	<img src="/css/img/addImg1.png" style="width:24px;height:24px;float:left;">
                        	<input type="button" value="新增车辆" onclick="addCar()" />
                        </div>
                    </div> -->
                    <!-- <div id="cars" style="width:500px;height:200px"></div> -->
                    
                </div>
            </div>
            <div style="width: 20px;height: 198px;float: left;"></div>
            <div style="width: 140px;height: 198px;float: left;">
                <div style="width: 100%;height: 19px;float: left;"></div>
                <div style="width: 100%;height: 140px;float: left;text-align: center;line-height: 140px;">
                    <img class='layui-upload-img' id='photo4' picId="0"
                         style="border: 1px solid #d8d8d8;border-radius: 50%;" width='138' height='138'
                         src="/css/img/headPhotoDefault.png">
                    <img id="hidePhoto4" style="display: none;">
                </div>
                <div style="height: 8px;width: 100%;float: left;"></div>
                <div style="height: 32px;width: 100%;float: left;">
                    <button type='button' class="button1 uploadBtn" id="uploadPhoto4" style="width: 68px;">上传头像</button>
                    <button type='button' class="button1" onclick="deletePhoto2(4)" style="width: 68px;">删除</button>
                </div>
            </div>
        </div>
        <div id="hasRegistered">
            <div style="width: 82px;height: 866px;float: left;"></div>
            <div style="width: 582px;height: 866px;float: left;">
                <div style="width: 100%;float: left;height: 20px;">
                    <div style="width: 3%;float: left;height: 100%;">
                    </div>
                    <div style="width: 97%;float: left;height: 100%;">
                        <fieldset class="layui-elem-field layui-field-title" style="margin: 0;float: left;width: 100%;">
                            <legend style="color: #999999;font-size: 14px;margin-left: 0;padding-left: 0;padding-right: 12px;">
                                已成功登记的访客
                            </legend>
                        </fieldset>
                    </div>
                </div>
                <div style="width: 100%;height: 28px;float: left;"></div>
                <div style="width: 100%;height: 818px;float: left;border: #e6e6e6 1px solid;border-top: none;">
                    <table class="layui-table" style="margin: 0">
                        <thead>
                        <tr>
                            <th>姓名</th>
                            <th>到访时间</th>
                            <th>结束时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="registeredVisitors">
                        <#if faceVisitInfoList?? && (faceVisitInfoList?size > 0) >
                            <#list faceVisitInfoList as faceVisitInfo>
                                <tr>
                                <td>${faceVisitInfo.name}</td>
                                <td>${faceVisitInfo.visit_time}</td>
                                <td>${faceVisitInfo.expire_time}</td>
                                <#if faceVisitInfo.status == 0 || faceVisitInfo.status == 1>
                                    <td style="cursor: pointer;" class='recFaceData' faceid="${faceVisitInfo.face_id}" recid="${faceVisitInfo.rec_id}" onclick='deleteVisitorReg(this)'>
                                    <a class='layui-btn layui-btn-danger layui-btn-xs'>删除</a></td>
                                <#else >
                                    <td></td>
                                </#if>
                                </tr>
                            </#list>
                        <#else >
                            <tr class="noDataMsg">
                                <td colspan="4" style="color: #999999;text-align: center;">显示已成功登记的访客记录</td>
                            </tr>
                        </#if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div style="position: fixed; bottom:20px;right:56px">
        <button id="backVisitorRegisterTwoBtn" class='button2' type='button' onclick="backVisitorRegisterTwo();"
                style="margin-right: 8px;">上一步
        </button>
        <#--保存并继续-->
        <button class='button2' type='button' id="continueRegister" lay-submit='' lay-filter="continueRegister"
                style="margin-right: 8px;">保存
        </button>
        <#--保存并退出-->
        <button class='button2' type='button' id="saveRegister" lay-submit='' lay-filter='visitorRegisterThreeFilter'
                style="margin-right: 8px;display: none;">保存
        </button>
        <button class='button2' type='button' id="exit">退出</button>
    </div>
</form>
<input id="ifWriteFlag" style="display: none;" value="0">