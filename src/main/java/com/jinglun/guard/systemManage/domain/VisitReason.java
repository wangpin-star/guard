package com.jinglun.guard.systemManage.domain;

public class VisitReason {

    private String visitReasonId;

    private String visitReasonName;

    public String getVisitReasonId() {
        return visitReasonId;
    }

    public void setVisitReasonId(String visitReasonId) {
        this.visitReasonId = visitReasonId;
    }

    public String getVisitReasonName() {
        return visitReasonName;
    }

    public void setVisitReasonName(String visitReasonName) {
        this.visitReasonName = visitReasonName;
    }

    public VisitReason(String visitReasonId, String visitReasonName) {
        this.visitReasonId = visitReasonId;
        this.visitReasonName = visitReasonName;
    }

    public VisitReason() {
    }
}
