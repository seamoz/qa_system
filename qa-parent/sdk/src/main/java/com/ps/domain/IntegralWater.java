package com.ps.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class IntegralWater implements Serializable {

    private int Integral_water_id;

    private int memberId;

    private int Integral_water_detail;
}
