package com.rgr.comparator;

import com.rgr.type.IntegerArrayType;

import java.io.Serializable;
import java.util.List;

public class IntegerArrayComparator implements Comparator, Serializable {
    @Override
    public double compare(Object firstObject, Object secondObject) {
        List<Integer> typeValueFirst =  ((IntegerArrayType)firstObject).getTypeValue();
        List<Integer> typeValueSecond = ((IntegerArrayType)secondObject).getTypeValue();
        int firstNum = 1, secondNum = 1;
        for (Integer integer : typeValueFirst) {
            firstNum *= integer;
        }

        for (Integer integer : typeValueSecond) {
            secondNum *= integer;
        }
        return firstNum - secondNum;
    }
}
