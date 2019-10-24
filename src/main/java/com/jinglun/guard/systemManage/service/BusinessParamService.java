package com.jinglun.guard.systemManage.service;

import com.jinglun.guard.systemManage.domain.SaveVisitReasonParam;
import com.jinglun.guard.systemManage.domain.VisitReason;
import com.jinglun.guard.user.domain.User;

import java.util.List;

public interface BusinessParamService {
    Integer saveVisitReason(User user, SaveVisitReasonParam saveVisitReasonParam);
}
