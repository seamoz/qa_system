package com.ps.domain;

import lombok.Data;
import java.io.Serializable;

/**
 * @author JIANGZI
 */
@Data
public class Thing implements Serializable {

    private int id ;//商品id

    private String thing_name;//商品名称

    private int thing_count;//商品库存

    private int exchange_integral;//兑换积分

    private String thing_costl;//商品价值

    private String thing_describe;//商品描述

    private String start_time;//开始时间

    private String end_time;//结束时间

    private int version;//版本哈


}
