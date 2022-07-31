package com.visualcovid.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReliabilityData {
    String name;
    String slug;
    int number;
    int flag;
    double d_factor;
    double MAD;
    double SSD;
    double freq_1;
    double freq_2;
    double freq_3;
    double freq_4;
    double freq_5;
    double freq_6;
    double freq_7;
    double freq_8;
    double freq_9;

}
