package com.jinglun.guard.systemManage.service;

//import java.util.ArrayList;
import java.util.List;

import com.jinglun.guard.dataservice.DataService.ParamConfigInfo;
import com.jinglun.guard.user.domain.User;
//import com.jinglun.guard.systemManage.domain.PublicParater;
/**
 * 
 * @author huanggang
 * 公共参数service
 */
public interface PublicparaterService {
	
	public List<ParamConfigInfo> queryAllPublicParater(User user);

}
