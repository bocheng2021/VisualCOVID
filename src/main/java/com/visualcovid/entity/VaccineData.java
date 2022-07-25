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
    public String country_Region;
    public Date date;
    public String doses_admin;
    public String people_partially_vaccinated;
    public String people_fully_vaccinated;
}
