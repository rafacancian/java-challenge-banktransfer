package com.cajucard.models;

public enum TransferStatus {

    APPROVED("00"),
    REJECTED("51"),
    ABORTED("07");

    private String status;

    TransferStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
