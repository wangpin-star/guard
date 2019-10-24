package com.jinglun.guard.carManage.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/***
 * 车辆记录管理模块
 * @author cq
 */
@Controller
@Slf4j
@RequestMapping("/carManage")
public class CarManageController {
    /**
     * 跳转车辆记录主界面
     * @return
     */
    @RequestMapping("/toCarManage")
    public String toVehicleManage(){
        return "carManage/carList";
    }

    /**
     *查询车辆记录列表
     * @param searchContent
     * @param httpSession
     * @return
     */
    @RequestMapping("/selectCarList")
    @ResponseBody
    public String selectVehicleList(@RequestParam(value = "page", defaultValue = "1") String page,
                                    @RequestParam(value = "limit", defaultValue = "10") String limit,
                                    @RequestParam(value = "ownerAttribute", defaultValue = "0") Integer ownerAttribute,
                                    @RequestParam(value = "throughArea", defaultValue = "0") Integer throughArea,
                                    @RequestParam(value = "startTime", defaultValue = "") String startTime,
                                    @RequestParam(value = "endTime", defaultValue = "") String endTime,
                                    @RequestParam(value = "searchContent", defaultValue = "") String searchContent,
                                    HttpSession httpSession) {
        log.info("page={},limit={},ownerAttribute={},throughArea={},startTime={},endTime={},searchContent={}",
                page, limit, ownerAttribute, throughArea,startTime,endTime,searchContent);
        /*
        User user = (User) httpSession.getAttribute("user");
        List<Conference> conferenceList = new ArrayList<>();
        ResultDomain<List<Conference>> faceInfoResultDomain;
        Conference conference = new Conference();
        conference.setConf_id(0);file:///C:/Users/CQ/Documents/HBuilderProjects/娴峰悍濞佽鎽勫儚澶�/demo/demo.css
        TermFace termFace=new TermFace();
        termFace.setTerm_id(0);
        faceInfoResultDomain = conferenceService.selectConference(user, conference,termFace.getTerm_id());
        if (null != faceInfoResultDomain.getResultData() && !faceInfoResultDomain.getResultData().isEmpty()) {
            conferenceList = faceInfoResultDomain.getResultData();
        } else {
            log.info("查询会议列表错误");
        }
        PageResult pageResult = new PageResult(faceInfoResultDomain.getResultCode(), conferenceList);
        return JSON.toJSONString(pageResult);*/
        return "";
    }

}
