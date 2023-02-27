package com.rgr.factory;

import com.rgr.type.IntegerArrayUserType;
import com.rgr.type.IntegerUserType;
import com.rgr.type.UserType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Factory {
    private final static ArrayList<UserType> typeList = new ArrayList<>();

    static {
        ArrayList<UserType> buildersClasses = new ArrayList<>(Arrays.asList(
                new IntegerUserType(),
                new IntegerArrayUserType()));
        typeList.addAll(buildersClasses);
    }
    public static ArrayList<String> getTypeNameList() {
        ArrayList<String> typeNameListString = new ArrayList<>();
        for (UserType userType : typeList) {
            typeNameListString.add(userType.typeName());
        }
        return typeNameListString;
    }
    public static UserType getBuilderByName(String name){
        if (name == null){
            throw new RuntimeException("Error! Name of type is empty!");
        }
        for (UserType userType : typeList) {
            if (name.equals(userType.typeName()))
                return userType;
        }
        return null;
    }
}
