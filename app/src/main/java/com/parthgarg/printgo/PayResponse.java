package com.parthgarg.printgo;

public class PayResponse {

    String status;

    public PayResponse(String status) {

        this.status = status;
    }
    PayResponse()
    {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
