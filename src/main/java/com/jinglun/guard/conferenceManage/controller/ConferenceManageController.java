package com.jinglun.guard.conferenceManage.controller;

import com.alibaba.fastjson.JSON;
import com.jinglun.guard.conferenceManage.domain.Conference;
import com.jinglun.guard.conferenceManage.service.ConferenceService;
import com.jinglun.guard.dataservice.PageResult;
import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.devicemanage.domain.Device;
import com.jinglun.guard.employeeManage.domain.TermFace;
import com.jinglun.guard.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/***
 * 会议管理模块
 * @author cq
 */
@Controller
@RequestMapping("/conferenceManage")
@Slf4j
public class ConferenceManageController {
    @Autowired
    ConferenceService conferenceService;
    /**
     * 跳转会议管理主界面
     * @return
     */
    @RequestMapping("/toConferenceManage")
    public String toConferenceManage(){
        return "conferenceManage/conferenceList";
    }

    /**
     * 跳转添加会议主界面
     * @return
     */
    @RequestMapping("/toAddConference")
    public String toAddConference(){
       return "conferenceManage/addConference";
    }

    /**
     * 查询会议列表
     * @param searchContent
     * @param httpSession
     * @return
     */
    @RequestMapping("/selectConferenceList")
    @ResponseBody
    public String selectEmployeeList(@RequestParam(value = "searchContent", defaultValue = "") String searchContent,
                                     HttpSession httpSession) {
        log.info("searchContent={}", searchContent);
        User user = (User) httpSession.getAttribute("user");
        List<Conference> conferenceList = new ArrayList<>();
        ResultDomain<List<Conference>> conferenceResultDomain;
        Conference conference = new Conference();
        conference.setConf_id(0);
        TermFace termFace=new TermFace();
        termFace.setTerm_id(0);
        conferenceResultDomain = conferenceService.selectConference(user, conference,termFace.getTerm_id());
        if (null != conferenceResultDomain.getResultData() && !conferenceResultDomain.getResultData().isEmpty()) {
            conferenceList = conferenceResultDomain.getResultData();
        } else {
            log.info("查询会议列表错误");
        }
        PageResult pageResult = new PageResult(conferenceResultDomain.getResultCode(), conferenceList);
        return JSON.toJSONString(pageResult);
    }

    /**
     * 门禁设备查询
     * @param request
     * @return
     */
    @RequestMapping("/selectDoorDevice")
    @ResponseBody
    public String selectDoorDevice(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<Device> deviceList = conferenceService.selectAllDevice(user);
        List<Device> deviceList1=new ArrayList<>();
        for (Device device:deviceList) {
            if(device.getBusiness_type()==0&&device.getDel_flag()!= Device.DeviceFlag.DELETED&&device.getDel_flag()!= Device.DeviceFlag.DEACTIVATED){
                deviceList1.add(device);
            }
        }
        PageResult pageResult = new PageResult();
        pageResult.setData(deviceList1);
        return JSON.toJSONString(pageResult);
    }

    /**
     * 会议设备查询
     * @param request
     * @return
     */
    @RequestMapping("/selectConferenceDevice")
    @ResponseBody
    public String selectConferenceDevice(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<Device> deviceList = conferenceService.selectAllDevice(user);
        List<Device> deviceList1=new ArrayList<>();
        for (Device device:deviceList) {
            if(device.getBusiness_type()!=0&&device.getDel_flag()!= Device.DeviceFlag.DELETED&&device.getDel_flag()!= Device.DeviceFlag.DEACTIVATED){
                deviceList1.add(device);
            }
        }
        PageResult pageResult = new PageResult();
        pageResult.setData(deviceList1);
        return JSON.toJSONString(pageResult);
    }

    /**
     * 添加会议
     * @param conference
     * @param httpSession
     * @return
     */
    @RequestMapping("/addConference")
    @ResponseBody
    public String addFaceInfo(@RequestBody Conference conference, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        conference.setBackground("");
        ResultDomain<Conference> faceInfoResultDomain = conferenceService.addConference(user, conference);
        if (null == faceInfoResultDomain.getResultData() || faceInfoResultDomain.getResultCode() != 0) {
            log.info("添加FaceInfo失败,FaceInfoUpload接口错误");
        }
        return JSON.toJSONString(faceInfoResultDomain);
    }

    /**
     * 下载导入模板
     * @return 静态资源访问
     */
    @RequestMapping("/download")
    public String downloadFiles() {
        String title = "会议list模板.zip";
        try {
            title = java.net.URLEncoder.encode(title, "UTF-8");
        } catch (Exception ex) {
            log.info("下载失败"+ex);
        }
        return "redirect:/" + title;
    }
}
