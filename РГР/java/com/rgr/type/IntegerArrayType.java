package com.rgr.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IntegerArrayType implements Serializable {

    private List<Integer> typeValue;

    public IntegerArrayType(int number) {
        List<Integer> primeFactors = new ArrayList<>();
        int divisor = 2;
        while (number > 1) {
            if (number % divisor == 0) {
                primeFactors.add(divisor);
                number /= divisor;
            } else {
                divisor++;
            }
        }
        this.typeValue = primeFactors;
    }

    public IntegerArrayType(List<Integer> typeValue){
        this.typeValue = typeValue;

    }

    public List<Integer> getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(List<Integer> typeValue) {
        this.typeValue = typeValue;
    }

    @Override
    public String toString() {
        return String.valueOf(typeValue);
    }
}
