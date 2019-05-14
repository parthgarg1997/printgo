package com.parthgarg.printgo;

public class PayRequest {

    String order_id;

    public PayRequest(String order_id) {
        this.order_id = order_id;
    }

    public PayRequest()
    {

    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
