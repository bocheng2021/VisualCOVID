package com.visualcovid.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Date;

@Setter
@Getter
@ToString
public class VaccineData implements Serializable {
    public String Country_Region;
    public Date Date;
    public String Doses_admin;
    public String People_partially_vaccinated;
    public String People_fully_vaccinated;
}
