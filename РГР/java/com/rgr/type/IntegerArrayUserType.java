package com.rgr.type;

import com.rgr.comparator.Comparator;
import com.rgr.comparator.IntegerArrayComparator;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IntegerArrayUserType implements UserType{

    private final int MAX = 1000;
    private final int MIN = 1;
    @Override
    public String typeName() {
        return "IntegerArray";
    }

    @Override
    public Object create() {
        Random random = new Random();
        return new IntegerArrayType(random.nextInt((MAX - MIN)) + MIN);
    }

    @Override
    public Object clone(Object object) {
        return new IntegerArrayType(((IntegerArrayType)object).getTypeValue());
    }

    @Override
    public Object readValue(InputStreamReader in) throws IOException {
        return in.read();
    }

    @Override
    public Object parseValue(String ss) {

        ss = ss.replace("[", "").replace("]", "");
        String[] values = ss.split(", ");
        List<Integer> list = new ArrayList<>();
        for (String value : values){
            list.add(Integer.parseInt(value));
        }
        return new IntegerArrayType(list);
    }

    @Override
    public Comparator getTypeComparator() {
        return new IntegerArrayComparator();
    }


    @Override
    public String toString(Object object) {
        return object.toString();
    }
}
