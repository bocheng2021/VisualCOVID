package com.visualcovid.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class CountryData implements Serializable {
    String Country;
    String Slug;
    String ISO2;
}
