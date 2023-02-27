package com.rgr.type;

import java.io.Serializable;

public class IntegerType implements Serializable {
    private int typeValue;

    public IntegerType(int typeValue) {
        this.typeValue = typeValue;
    }

    public int getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(int typeValue) {
        this.typeValue = typeValue;
    }

    @Override
    public String toString() {
        return String.valueOf(typeValue);
    }
}
