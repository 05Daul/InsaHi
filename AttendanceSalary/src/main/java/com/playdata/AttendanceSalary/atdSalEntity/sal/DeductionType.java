package com.playdata.AttendanceSalary.atdSalEntity.sal;

import lombok.Getter;

@Getter
public enum DeductionType {

    NATIONAL_PENSION("NP", "국민연금", 0.045),        //  국민 연금 4.5%
    HEALTH_INSURANCE("HI", "건강보험", 0.03545),       // 건강 보험 3.545%
    LONG_TERM_CARE("LTC", "장기요양보험", 0.004595775), // 건강 보험료의 12.95% → 3.545% * 12.95% = 0.004595775
    EMPLOYMENT_INSURANCE("EI", "고용보험", 0.009),     // 고용 보험0.9%
    INDUSTRIAL_ACCIDENT("IA", "산재보험", 0.014);      // 인원마다 다른 산재보험의 평균 1.4%


    private final String code;
    private final String description;
    private final double rate;


    DeductionType(String code, String description, double rate) {
        this.code = code;
        this.description = description;
        this.rate = rate;
    }


    public static DeductionType fromCode(String code) {
        for (DeductionType type : DeductionType.values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
