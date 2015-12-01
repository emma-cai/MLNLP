package org.freebase;

import java.util.EnumSet;

/**
 * Created by qingqingcai on 12/1/15.
 */
public enum TypeEnumForBhi {

    // prefix = "<http://rdf.freebase.com/ns/"
    // suffix = ">"
    LOCATION_CITYTOWN("location.citytown"),
    LOCATION_POSTAL_CODE("location.postal_code"),
    LOCATION_COUNTRY("location.country"),
    LOCATION_US_STATE("location.us_state"),
    UNKNOWN("unkown type");

    private String description;

    TypeEnumForBhi(String description) {
        this.description = description;
    }

    public boolean isOneOf(TypeEnumForBhi pos, TypeEnumForBhi... rest) {
        return EnumSet.of(pos, rest).contains(this);
    }

    public static TypeEnumForBhi fromString(String str) {

        switch (str) {
            case "location.citytown":
                return LOCATION_CITYTOWN;
            case "location.postal_code":
                return LOCATION_POSTAL_CODE;
            case "location.country":
                return LOCATION_COUNTRY;
            case "location.us_state":
                return LOCATION_US_STATE;
            default: {
                try {
                    return valueOf(str);
                } catch (Exception e) {
                    return UNKNOWN;
                }
            }
        }
    }
}
