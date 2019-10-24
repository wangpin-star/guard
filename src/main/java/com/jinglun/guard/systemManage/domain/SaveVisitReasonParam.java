package com.jinglun.guard.systemManage.domain;

import java.util.List;

public class SaveVisitReasonParam {
    private List<VisitReason> addOrUpdateList;
    private List<VisitReason> deleteList;

    public List<VisitReason> getAddOrUpdateList() {
        return addOrUpdateList;
    }

    public void setAddOrUpdateList(List<VisitReason> addOrUpdateList) {
        this.addOrUpdateList = addOrUpdateList;
    }

    public List<VisitReason> getDeleteList() {
        return deleteList;
    }

    public void setDeleteList(List<VisitReason> deleteList) {
        this.deleteList = deleteList;
    }
}
