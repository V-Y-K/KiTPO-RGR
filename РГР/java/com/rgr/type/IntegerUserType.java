package com.rgr.type;

import com.rgr.comparator.Comparator;
import com.rgr.comparator.IntegerComparator;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class IntegerUserType implements UserType{

    private final int MAX = 1000;
    private final int MIN = -1000;

    @Override
    public String typeName() {
        return "Integer";
    }

    @Override
    public Object create() {
        Random random = new Random();
        return new IntegerType(random.nextInt((MAX - MIN)) + MIN);
    }

    @Override
    public Object clone(Object object) {
        return new IntegerType(((IntegerType)object).getTypeValue());
    }

    @Override
    public Object readValue(InputStreamReader in) throws IOException {
        return in.read();
    }

    @Override
    public Object parseValue(String ss) {
        return new IntegerType(Integer.parseInt(ss));
    }

    @Override
    public Comparator getTypeComparator() {
        return new IntegerComparator();
    }


    @Override
    public String toString(Object object) {
        return object.toString();
    }
}
