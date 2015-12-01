package org.freebase;

import java.util.EnumSet;

/**
 * Created by qingqingcai on 11/30/15.
 */
public enum PredicateEnumForBhi {

    POSTAL_CODE("location.postal_code.postal_code"),
    COUNTRY("location.mailing_address.country"),
    CITYTOWN("location.mailing_address.citytown"),
    DISPLAY_NAME("common.notable_for.display_name"),
    UNKNOWN("unknown predicate");

    private String description;

    PredicateEnumForBhi(String description) {
        this.description = description;
    }

    public boolean isOneOf(PredicateEnumForBhi pos, PredicateEnumForBhi... rest) {
        return EnumSet.of(pos, rest).contains(this);
    }

    public static PredicateEnumForBhi fromString(String str) {
        try {
            return valueOf(str);
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}
