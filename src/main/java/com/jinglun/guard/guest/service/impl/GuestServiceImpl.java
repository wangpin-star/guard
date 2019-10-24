package com.jinglun.guard.guest.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jinglun.guard.dataservice.DataService;
import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.employeeManage.domain.FaceInfo;
import com.jinglun.guard.employeeManage.domain.TreeDomain;
import com.jinglun.guard.employeeManage.domain.TreeUtil;
import com.jinglun.guard.guest.domain.Group;
import com.jinglun.guard.guest.domain.GroupFace;
import com.jinglun.guard.guest.domain.GuestInfo;
import com.jinglun.guard.guest.exception.GuestruntimeException;
import com.jinglun.guard.guest.service.GuestService;
import com.jinglun.guard.user.domain.User;


@Service
public class GuestServiceImpl implements GuestService{

	private static final String EMPOYEE = "empoyee";
	private static final String VISITOR = "visitor";
	
	@Override
	public TreeDomain queryGroup(User user) {
		List<Group> grouplist = new ArrayList<>();
		TreeDomain treeDomain = new TreeDomain(0, "嘉宾分组","嘉宾分组",0,null);
		ArrayList<TreeDomain> treeDomainList = new ArrayList<>();
		try {
			ResultDomain<List<Group>> r = DataService.GroupQuery(user);
			int resut = r.getResultCode();
			if(resut > 0){
				grouplist = r.getResultData();
				for (Group group : grouplist) {
					TreeDomain domain = new TreeDomain();
					domain.setDepart_id(group.getGroup_id());
					domain.setParentId(0);
					domain.setName(group.getName());
					treeDomainList.add(domain);
				}
				treeDomain = TreeUtil.getTree(treeDomain, treeDomainList);
			}
			
		} catch (Exception e) {
			throw new GuestruntimeException(e,"获取分组信息接口GroupQuery调用失败");
		}
		return treeDomain;
	}

	@Override
	public List<GuestInfo> queryAllguest(User user) {
		List<GuestInfo> guestlistinfo = new ArrayList<>();
		FaceInfo queryFace = new FaceInfo();
		queryFace.setFace_id(0);
		queryFace.setDepart_id(0);
		queryFace.setStatus(0);
		queryFace.setPic_num(0);
		
		//查询员工嘉宾
		List<FaceInfo> list1 = queryFaceinfo(user, queryFace, 0);
		if(!list1.isEmpty()){
			getGuestinfo(guestlistinfo,list1,0);
		}
		//查询访客嘉宾
		List<FaceInfo> list2 = queryFaceinfo(user, queryFace, 1);
		if(!list2.isEmpty()){
			getGuestinfo(guestlistinfo,list2,1);
		}
		
		return guestlistinfo;
	}

	private void getGuestinfo(List<GuestInfo> guestlist,List<FaceInfo> list, int attribute) {
		
		for(FaceInfo faceinfo : list){
			GuestInfo guest = new GuestInfo();
			String name = faceinfo.getName();
			String phone = faceinfo.getTel_no();
			String title = "";
			if(phone == null || "".equals(phone)){
				title += name;
			}else{
				title = name+"("+phone+")";
			}
			
			if(attribute == 0){
				guest.setValue(faceinfo.getFace_id()+EMPOYEE);
			}else{
				guest.setValue(faceinfo.getFace_id()+VISITOR);
			}
			guest.setTitle(title);
			guest.setAttribute(attribute);
			guestlist.add(guest);
		}
	}
	
	public List<FaceInfo> queryFaceinfo(User user, FaceInfo faceinfo,int attribute){
		
		List<FaceInfo> faceinfolist = new ArrayList<>();
		try {
			
			faceinfo.setAttribute(attribute);
			
			ResultDomain<List<FaceInfo>> r = DataService.FaceInfoQuery(user, faceinfo, "", 0, -1);
			int result = r.getResultCode();
			if (result > 0) {
				faceinfolist = r.getResultData();
				//判断需要循环调用多少次FaceInfoQuery接口
				int count = (int)Math.ceil(result/(double)200);
				if(count > 1){
					repeatMethod(user, faceinfo, faceinfolist, count);
				}
				
				return faceinfolist;
			}
		} catch (Exception e) {
			throw new GuestruntimeException(e,"查询人员信息接口FaceInfoQuery调用失败");
		}
		return faceinfolist;
	}

	private void repeatMethod(User user, FaceInfo faceinfo, List<FaceInfo> faceinfolist, int count) {
		for(int i = 1; i<count ; i++){
			List<FaceInfo> list = DataService.FaceInfoQuery(user, faceinfo, "", i*200 ,-1).getResultData();
			for(FaceInfo face:list){
				faceinfolist.add(face);
			}
		}
	}

	@Override
	public Group createGroup(User user, String groupName,Integer groupId) {
		//1.上报分组
		Group groupInfo = new Group();
		try {
			if(null != groupId && groupId > 0){
				groupInfo.setGroup_id(groupId);
			}else{
				groupInfo.setGroup_id(0);
			}
			groupInfo.setName(groupName);
			ResultDomain<Group> r = DataService.GroupUpload(user, groupInfo);
			int result = r.getResultCode();
			groupInfo.setUploadStatus(result);
			return groupInfo;
		} catch (Exception e) {
			throw new GuestruntimeException(e,"上报分组接口GroupUpload调用失败");
		}
	}

	@Override
	public int groupFaceConfig(User user, Group group, List<GroupFace> list) {
		//2.配置组员
		try {
			@SuppressWarnings("rawtypes")
			ResultDomain rd = DataService.GroupFaceConfig(user, group, list);
			int result1 = rd.getResultCode();
			if(result1 == 0){
				return 0;
			}
		} catch (Exception e) {
			throw new GuestruntimeException(e,"配置分组名单接口GroupFaceConfig调用失败");
		}
		
		return 1;
	}

	@Override
	public String queryGroupface(User user,Group groupInfo) {
		//查询分组名单
		StringBuilder sb = new StringBuilder();
		try {
			ResultDomain<List<GroupFace>> r = DataService.GroupFaceQuery(user, groupInfo);
			int result = r.getResultCode();
			if(result>0){
				List<GroupFace> list = r.getResultData();
				
				for(int i = 0;i<list.size();i++){
					int attribute = list.get(i).getAttribute();
					int faceId = list.get(i).getFace_id();
					queryFace(sb, list, i, attribute, faceId);
				}
				return sb.toString();
			}
		} catch (Exception e) {
			throw new GuestruntimeException(e,"获取分组名单接口GroupFaceQuery调用失败");
		}
		return sb.toString();
	}

	private void queryFace(StringBuilder sb, List<GroupFace> list, int i, int attribute, int faceId) {
		if(i<list.size()-1){
			if(attribute == 0){
				sb.append(faceId).append(EMPOYEE);
				sb.append(",");
			}else{
				sb.append(faceId).append(VISITOR);
				sb.append(",");
			}
		}else{
			if(attribute == 0){
				sb.append(faceId).append(EMPOYEE);
			}else{
				sb.append(faceId).append(VISITOR);
			}
		}
	}

	@Override
	public int delGroup(User user, Integer groupId) {
		//删除分组
		Group groupInfo = new Group();
		groupInfo.setGroup_id(groupId);
		try {
			@SuppressWarnings("rawtypes")
			ResultDomain r = DataService.GroupDelete(user, groupInfo);
			int result = r.getResultCode();
			if(result == 0){
				return 0;
			}
		} catch (Exception e) {
			throw new GuestruntimeException(e,"注销分组信息接口GroupDelete调用失败");
		}
		return 1;
	}

}
