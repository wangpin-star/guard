package com.jinglun.guard.conferenceManage.service;

import com.jinglun.guard.conferenceManage.domain.Conference;
import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.devicemanage.domain.Device;
import com.jinglun.guard.user.domain.User;

import java.util.List;

public interface ConferenceService {
    public ResultDomain<List<Conference>> selectConference(User user, Conference conf, int term_id);
    public List<Device> selectAllDevice(User user);
    public ResultDomain<Conference> addConference(User user,Conference conference);
}
