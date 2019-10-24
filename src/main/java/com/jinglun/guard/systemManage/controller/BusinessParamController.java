package com.jinglun.guard.systemManage.controller;

import com.jinglun.guard.dataservice.DataService;
import com.jinglun.guard.dataservice.DataService.ParamConfigInfo;
import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.systemManage.domain.BusinessParam;
import com.jinglun.guard.systemManage.domain.SaveVisitReasonParam;
import com.jinglun.guard.systemManage.domain.VisitReason;
import com.jinglun.guard.systemManage.service.BusinessParamService;
import com.jinglun.guard.user.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/businessParam")
@Slf4j
public class BusinessParamController {

    @Resource
    private BusinessParamService businessParamService;

    @RequestMapping("/showBusinessParam")
    public String showBusinessParam(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        List<VisitReason> visitReasonList = new ArrayList<>();
        ResultDomain<List<VisitReason>> visitReasonResultDomain = DataService.VisitReasonQuery(user, 0);
        if (null != visitReasonResultDomain.getResultData() && visitReasonResultDomain.getResultCode() == 0 && visitReasonResultDomain.getResultData().size() > 0) {
            visitReasonList = visitReasonResultDomain.getResultData();
        }
        model.addAttribute("visitReasonList",visitReasonList);
        List<DataService.ParamConfigInfo> paramConfigInfoList = new ArrayList<>();
        ResultDomain<List<DataService.ParamConfigInfo>> ParamListResult = DataService.LibraryParamQuery(user);
        if (ParamListResult.getResultCode() == 0 && ParamListResult.getResultData().size() > 0) {
            paramConfigInfoList = ParamListResult.getResultData();
        } else {
            log.info("查询业务参数接口出错或没有数据");
        }
        List<DataService.ParamConfigInfo> employeeInformationList = paramConfigInfoList.stream().filter(e ->
                e.getParam_key().equals("face_info_input_mod") ||
                e.getParam_key().equals("is_need_employee_card") ||
                e.getParam_key().equals("life_photo_max_size")).collect(Collectors.toList());
        model.addAttribute("employeeInformationList",employeeInformationList);
        List<DataService.ParamConfigInfo> visitorRegisterList = paramConfigInfoList.stream().filter(e ->
                e.getParam_key().equals("is_need_visit_cmp") ||
                e.getParam_key().equals("is_need_visit_tel") ||
                e.getParam_key().equals("is_need_visit_photo") ||
                e.getParam_key().equals("is_need_visit_idcard")).collect(Collectors.toList());
        model.addAttribute("visitorRegisterList",visitorRegisterList);
        return "systemManage/businessParam";
    }

    @RequestMapping("/showApplyParam")
    public String showApplyParam(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        List<DataService.ParamConfigInfo> paramConfigInfoList = new ArrayList<>();
        ResultDomain<List<DataService.ParamConfigInfo>> ParamListResult = DataService.LibraryParamQuery(user);
        if (ParamListResult.getResultCode() == 0 && ParamListResult.getResultData().size() > 0) {
            paramConfigInfoList = ParamListResult.getResultData();
        } else {
            log.info("查询业务参数接口出错或没有数据");
        }
        List<DataService.ParamConfigInfo> applyParamList= paramConfigInfoList.stream().filter(e ->
                e.getParam_key().equals("auto_refresh_interval")).collect(Collectors.toList());
        
        if(null!=applyParamList&&!applyParamList.isEmpty()) {
        	model.addAttribute("applyParamList",applyParamList);
        }
        else{
        	List<DataService.ParamConfigInfo> applyParam=new ArrayList<>();
        	ParamConfigInfo paramconfig=new ParamConfigInfo();
        	paramconfig.setParam_key("auto_refresh_interval");
        	paramconfig.setParam_name("自动刷新间隔时间");
        	paramconfig.setParam_value("30");
        	applyParam.add(paramconfig);
        	model.addAttribute("applyParamList",applyParam);
        }
        return "systemManage/applyParam";
    }

    @RequestMapping("/saveVisitParam")
    @ResponseBody
    public String saveVisitParam(@RequestBody SaveVisitReasonParam saveVisitReasonParam, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        Integer result = businessParamService.saveVisitReason(user, saveVisitReasonParam);
        return String.valueOf(result);
    }

    @RequestMapping("/saveParamConfigInfo")
    @ResponseBody
    public String saveEmployeeInformation(@RequestBody ArrayList<DataService.ParamConfigInfo> paramConfigInfoList, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        ResultDomain<List<DataService.ParamConfigInfo>> listResultDomain = DataService.LibraryParamConfig(user, paramConfigInfoList);
        return String.valueOf(listResultDomain.getResultCode());
    }

    @RequestMapping("/saveParamConfigInfo1")
    @ResponseBody
    public String saveEmployeeInformation1(HttpSession httpSession) {
        ArrayList<DataService.ParamConfigInfo> paramConfigInfoList = new ArrayList<>();
        DataService.ParamConfigInfo paramConfigInfo = new DataService.ParamConfigInfo();
        paramConfigInfo.setParam_key("auto_refresh_interval");
        paramConfigInfo.setParam_name("自动刷新间隔时间");
        paramConfigInfo.setParam_value("60");
        paramConfigInfoList.add(paramConfigInfo);
        User user = (User) httpSession.getAttribute("user");
        ResultDomain<List<DataService.ParamConfigInfo>> listResultDomain = DataService.LibraryParamConfig(user, paramConfigInfoList);
        return String.valueOf(listResultDomain.getResultCode());
    }
}
