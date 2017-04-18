package com.kpn.opib.bam.model;

public enum HeatMapStatus {
OK(0), NOTOK(1), UNKNOWN(2);

private final int value;

	HeatMapStatus(final int newValue) {
    value = newValue;
}
public int getValue() { return value; }
}
