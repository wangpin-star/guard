package com.jinglun.guard.systemManage.controller;

import com.alibaba.fastjson.JSON;
import com.jinglun.guard.dataservice.DataService;
import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.employeeManage.domain.TreeDomain;
import com.jinglun.guard.employeeManage.domain.TreeUtil;
import com.jinglun.guard.systemManage.domain.Depart;
import com.jinglun.guard.systemManage.domain.DepartResult;
import com.jinglun.guard.systemManage.service.PublicparaterService;
import com.jinglun.guard.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.swing.plaf.PanelUI;
import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/departManage")
@Controller
@Slf4j
public class DepartManageController {

    @Resource
    private PublicparaterService publicparaterService;

    @RequestMapping("/toDepartManage")
    public String toDepartManage(Model model,HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        List<DataService.ParamConfigInfo> publicparaterlist = publicparaterService.queryAllPublicParater(user);
        if (null!=publicparaterlist&&!publicparaterlist.isEmpty()) {
            publicparaterlist = publicparaterlist.stream().filter(e ->
                    e.getParam_key().equals("max_organizational_structure"))
                    .collect(Collectors.toList());
            if(null!=publicparaterlist&&!publicparaterlist.isEmpty()) {
            	String max = publicparaterlist.get(0).getParam_value();
                model.addAttribute("max",max);
            }
            else {
            	model.addAttribute("max",8);
            }
        } else {
            model.addAttribute("max",8);
        }
        model.addAttribute("user",user);
        return "departManage/departList";
    }

    @RequestMapping("/selectDepartTree")
    @ResponseBody
    public String selectDepartTree(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        ResultDomain<List<Depart>> listResultDomain = DataService.DepartInfoQuery(user, -1, 0);
        TreeDomain treeDomain = new TreeDomain(0, user.getCompany_name(),user.getCompany_name(),1,null);
        if (null != listResultDomain.getResultData() && listResultDomain.getResultCode() == 0
                && !listResultDomain.getResultData().isEmpty()) {
            List<Depart> resultData = listResultDomain.getResultData();
            List<Depart> resultDatas = new ArrayList<>();
            for(int i=0;i<resultData.size();i++) {
            	if(resultData.get(i).getDel_flag()==0) {
            		resultDatas.add(resultData.get(i));
            	}
            }
            //把部门List以部门名称按照中文字母排序
            resultDatas.sort((o1, o2) -> {
                Collator collator = Collator.getInstance(Locale.CHINA);
                return collator.compare(o1.getDepart_name(), o2.getDepart_name());
            });
            ArrayList<TreeDomain> treeDomainList = new ArrayList<>();
            for (Depart resultDatum : resultDatas) {
                if(resultDatum.getDel_flag()==0) {
                    TreeDomain domain = new TreeDomain();
                    domain.setData(resultDatum);
                    domain.setDepart_id(resultDatum.getDepart_id());
                    domain.setParentId(resultDatum.getParent_depart_id());
                    domain.setName(resultDatum.getDepart_name());
                    domain.setLabel(resultDatum.getDepart_name());
                    treeDomainList.add(domain);
                }
            }
            treeDomain = TreeUtil.getTree(treeDomain, treeDomainList);
        } else {
            log.info("查询部门树错误");
        }
        return JSON.toJSONString(treeDomain);
    }

    @RequestMapping("/selectDepartTreeByAuth")
    @ResponseBody
    public String selectDepartTreeByAuth(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        ResultDomain<List<Depart>> listResultDomain = DataService.DepartInfoQueryByAuth(user, -1, 0);
        TreeDomain treeDomain = new TreeDomain(0, user.getCompany_name(),user.getCompany_name(),1,null);
        if (null != listResultDomain.getResultData() && listResultDomain.getResultCode() == 0
                && !listResultDomain.getResultData().isEmpty()) {
            List<Depart> resultData = listResultDomain.getResultData();
            //把部门List以部门名称按照中文字母排序
            resultData.sort((o1, o2) -> {
                Collator collator = Collator.getInstance(Locale.CHINA);
                return collator.compare(o1.getDepart_name(), o2.getDepart_name());
            });
            ArrayList<TreeDomain> treeDomainList = new ArrayList<>();
            for (Depart resultDatum : resultData) {
                if(resultDatum.getDel_flag()==0) {
                    TreeDomain domain = new TreeDomain();
                    domain.setData(resultDatum);
                    domain.setDepart_id(resultDatum.getDepart_id());
                    domain.setParentId(resultDatum.getParent_depart_id());
                    domain.setName(resultDatum.getDepart_name());
                    domain.setLabel(resultDatum.getDepart_name());
                    treeDomainList.add(domain);
                }
            }
            treeDomain = TreeUtil.getTree(treeDomain, treeDomainList);
        } else {
            log.info("查询部门树错误");
        }
        return JSON.toJSONString(treeDomain);
    }

    /*@RequestMapping("/selectDepartList")
    @ResponseBody
    public String selectDepartList(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        List<Depart> departList = new ArrayList<>();
        DepartResult departResult = new DepartResult();
        departResult.setCode(0);
        departResult.setMsg("");
        departResult.setCount(100);
        departResult.setIs(true);
        departResult.setTip("success");
        ResultDomain<List<Depart>> listResultDomain = DataService.DepartInfoQuery(user, -1, 0);
        if (null != listResultDomain.getResultData() && listResultDomain.getResultCode() == 0
                && listResultDomain.getResultData().size() > 0) {
            departList = listResultDomain.getResultData();
        }
        departResult.setData(departList);
        return JSON.toJSONString(departResult);
    }*/

    @RequestMapping("/departInfoUpload")
    @ResponseBody
    public String departInfoUpload(HttpSession httpSession,Depart depart) {
        User user = (User) httpSession.getAttribute("user");
        List<Integer> departs=user.getDepartList();
        boolean check = null != depart && depart.getParent_depart_id() >= 0 &&
                null != depart.getDepart_name() && !depart.getDepart_name().equals("");
        if (check) {
            Integer parentId = depart.getParent_depart_id();
            ResultDomain<List<Depart>> listResultDomain = DataService.DepartInfoQuery(user, parentId, 0);
            if (null != listResultDomain.getResultData() && listResultDomain.getResultCode() == 0 && !listResultDomain.getResultData().isEmpty()) {
                List<Depart> departLists = listResultDomain.getResultData();
                List<Depart> departList = new ArrayList<>();
                for(int i=0;i<departLists.size();i++) {
                	if(departLists.get(i).getDel_flag()==0) {
                		departList.add(departLists.get(i));
                	}
                }
                List<String> departNameList = departList.stream().map(Depart::getDepart_name).collect(Collectors.toList());
                if (!departNameList.contains(depart.getDepart_name())) {
                    ResultDomain<Depart> departResultDomain = DataService.DepartInfoUpload(user, depart);
                    if (null != departResultDomain && null != departResultDomain.getResultData() && departResultDomain.getResultCode() == 0) {
                        Depart data = departResultDomain.getResultData();
                        Integer departId = data.getDepart_id();
                        departs.add(departId);
                        user.setDepartList(departs);
                        return String.valueOf(departId);
                    } else {
                        log.error("DepartInfoUpload接口出错");
                        return "-1";
                    }
                } else {
                    return "-2";
                }
            } else {
                ResultDomain<Depart> departResultDomain = DataService.DepartInfoUpload(user, depart);
                if (null != departResultDomain && null != departResultDomain.getResultData() && departResultDomain.getResultCode() == 0) {
                    Depart data = departResultDomain.getResultData();
                    Integer departId = data.getDepart_id();
                    departs.add(departId);
                    user.setDepartList(departs);
                    return String.valueOf(departId);
                } else {
                    log.error("DepartInfoUpload接口出错");
                    return "-1";
                }
            }
        } else {
            log.error("部门上报参数传递错误");
            return "-1";
        }
    }

    @RequestMapping("/departInfoDelete")
    @ResponseBody
    public String departInfoDelete(HttpSession httpSession,Depart depart) {
        User user = (User) httpSession.getAttribute("user");
        if (depart.getDepart_id() == -1 || depart.getDepart_id() == 0) {
            return "-2";
        } else {
            ResultDomain<Depart> departResultDomain = DataService.DepartInfoDelete(user, depart);
            if (null != departResultDomain && null != departResultDomain.getResultData() && departResultDomain.getResultCode() == 0) {
                return "0";
            } else {
                log.error("DepartInfoDelete接口出错");
                return "-1";
            }
        }
    }
}
