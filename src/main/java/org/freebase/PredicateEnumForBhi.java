package org.freebase;

import java.util.EnumSet;

/**
 * Created by qingqingcai on 11/30/15.
 */
public enum PredicateEnumForBhi {

    type_object_type("type.object.type"),
    type_object_name("type.object.name"),
    location_mailing_address_postal_code("location.mailing_address.postal_code"),
    location_mailing_address_country("location.mailing_address.country"),
    location_mailing_address_citytown("location.mailing_address.citytown"),
    location_mailing_address_street_address("location.mailing_address.street_address"),
    UNKNOWN("unknown predicate");

    private String description;

    PredicateEnumForBhi(String description) {
        this.description = description;
    }

    public boolean isOneOf(PredicateEnumForBhi pos, PredicateEnumForBhi... rest) {
        return EnumSet.of(pos, rest).contains(this);
    }

    public static PredicateEnumForBhi fromString(String str) {
        switch (str) {
            case "location.mailing_address.postal_code":
                return location_mailing_address_postal_code;
            case "location.mailing_address.country":
                return location_mailing_address_country;
            case "location.mailing_address.citytown":
                return location_mailing_address_citytown;
            case "location.mailing_address.street_address":
                return location_mailing_address_street_address;
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
