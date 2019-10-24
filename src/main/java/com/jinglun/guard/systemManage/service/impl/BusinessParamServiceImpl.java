package com.jinglun.guard.systemManage.service.impl;

import com.jinglun.guard.dataservice.DataService;
import com.jinglun.guard.dataservice.ResultDomain;
import com.jinglun.guard.systemManage.domain.SaveVisitReasonParam;
import com.jinglun.guard.systemManage.domain.VisitReason;
import com.jinglun.guard.systemManage.service.BusinessParamService;
import com.jinglun.guard.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BusinessParamServiceImpl implements BusinessParamService {

    @Override
    public Integer saveVisitReason(User user, SaveVisitReasonParam saveVisitReasonParam) {
        Integer result = 0;
        List<VisitReason> addOrUpdateList = saveVisitReasonParam.getAddOrUpdateList();
        for (int i = 0;i < addOrUpdateList.size();i++) {
            VisitReason visitReason = addOrUpdateList.get(i);
            ResultDomain<VisitReason> visitReasonResultDomain = DataService.VisitReasonUpload(user, visitReason,0);
            if (visitReasonResultDomain.getResultCode() != 0) {
                result = visitReasonResultDomain.getResultCode();
                log.info("保存出错,来访事由名称为:" + visitReasonResultDomain.getResultData().getVisitReasonName());
                break;
            }
        }
        List<VisitReason> deleteList = saveVisitReasonParam.getDeleteList();
        if (null != deleteList && deleteList.size() > 0) {
            for (int j = 0;j < deleteList.size();j++) {
                VisitReason visitReason = deleteList.get(j);
                ResultDomain<VisitReason> visitReasonResultDomain = DataService.VisitReasonUpload(user, visitReason, 1);
                if (visitReasonResultDomain.getResultCode() != 0) {
                    result = -2;
                    log.info("删除出错,来访事由名称为:" + visitReasonResultDomain.getResultData().getVisitReasonName());
                    break;
                }
            }
        }
        return result;
    }
}
