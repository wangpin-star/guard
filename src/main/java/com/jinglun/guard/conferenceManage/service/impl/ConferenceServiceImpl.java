package com.jinglun.guard.conferenceManage.service.impl;

import com.jinglun.guard.conferenceManage.domain.Conference;
import com.jinglun.guard.conferenceManage.service.ConferenceService;
import com.jinglun.guard.dataservice.DataService;
import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.devicemanage.domain.Device;
import com.jinglun.guard.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@Service
@Slf4j
public class ConferenceServiceImpl implements ConferenceService {

    @Override
    public ResultDomain<List<Conference>> selectConference(User user, Conference conf, int termid) {
        ResultDomain<List<Conference>> faceInfoResultDomain;
        faceInfoResultDomain = DataService.ConferenceInfoQuery(user, conf,termid);
        return faceInfoResultDomain;
    }

    @Override
    public List<Device> selectAllDevice(User user) {
        Device device = new Device();
        device.setTerm_id(0);
        device.setTerm_type(0);
        device.setDel_flag(Device.DeviceFlag.ALL);
        ResultDomain<List<Device>> resultDomain;
        try {
            resultDomain = DataService.TermInfoQuery(user, device, "");
            if (resultDomain.getResultData()!=null&&!resultDomain.getResultData().isEmpty()){
                return resultDomain.getResultData();
            }
        }catch (Exception e) {
            log.error("查询设备接口出错");
        }
        return Collections.emptyList();
    }

    @Override
    public ResultDomain<Conference> addConference(User user,Conference conference) {
        ResultDomain<Conference> resultDomain= DataService.ConferenceInfoUpload(user,conference);
        return resultDomain;
    }
}
