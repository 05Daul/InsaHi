package com.playdata.AttendanceSalary.atdSalEntity.sal;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum AllowanceType {
    MEAL("식대", true, new BigDecimal("200000")),
    TRANSPORT("통근비", true, BigDecimal.valueOf(Long.MAX_VALUE)),
    CHILD_CARE("보육수당", true, new BigDecimal("100000")),
    OVERTIME("연장근로수당", false, BigDecimal.ZERO),
    POSITION("직급수당", false, BigDecimal.ZERO),
    CAR_ALLOWANCE("자가운전보조금", true, new BigDecimal("200000")),
    DANGER("위험수당", true, new BigDecimal("50000")),
    FIELD_WORK("현장 근무수당", true, new BigDecimal("200000")),
    ETC("기타", false, BigDecimal.ZERO);

    private final String displayName;
    private final boolean taxExemption;
    private final BigDecimal taxExemptionLimit;

    AllowanceType(String displayName, boolean taxExemption, BigDecimal taxExemptionLimit) {
        this.displayName = displayName;
        this.taxExemption = taxExemption;
        this.taxExemptionLimit = taxExemptionLimit;
    }

    public static AllowanceType fromDisplayName(String name) {
        for (AllowanceType type : AllowanceType.values()) {
            if (type.getDisplayName().equals(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown AllowanceType: " + name);
    }

}
