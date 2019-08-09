package com.ps.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Order implements Serializable {

    private int orderId;//订单id

    private int thing_id;//商品id

    private int member_id;//用户id

    private String times;//订单时间


}
