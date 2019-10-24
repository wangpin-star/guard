package com.jinglun.guard.visitorManage.service;

import java.util.List;

import com.jinglun.guard.dataservice.PageResult;

import com.jinglun.guard.attendance.domain.FaceRecordInfo;

import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.systemManage.domain.Depart;
import com.jinglun.guard.systemManage.domain.VisitReason;
import com.jinglun.guard.user.domain.User;
import com.jinglun.guard.visitorManage.domain.BatVisitor;
import com.jinglun.guard.visitorManage.domain.FaceVisitInfo;
import com.jinglun.guard.visitorManage.domain.VisitQueryInfo;
import com.jinglun.guard.visitorManage.domain.VisitorDetailShow;

public interface VisitorManagerService {

	public ResultDomain<List<VisitQueryInfo>> queryAllVisitor(User user, Integer statuval,Integer photonumval,String visit_time,
			String expire_time, String query_info, int visit_num, int start);

	public PageResult batQuery(User user, int start, int limit);

	public int batDel(User user, int batid);

	public int visitorDel(User user, int recId);

	public VisitorDetailShow queryVisitorDetails(User user, Integer status, Integer faceid, Integer recid,
			Integer bookid,Integer attribute);

	public BatVisitor batQuerybyId(User user, int batid);

	public int visitorLeave(User user, Integer recid,Integer status);

	public List<FaceRecordInfo> visitorHistory(User user, Integer faceid, Integer attribute, String visitime,
			String expiretime);

	public int facebookDelete(User user, Integer bookid);
	
	public String queryDataStatus(User user,String revo);
	
	public int queryVisitrefreshTime(User user);

}
