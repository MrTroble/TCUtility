package com.troblecodings.tcutility.enums;

public enum ArmorTypes {

    HEAD("head"), CHEST("chest"), LEGS("legs"), FEET("feet");

    private final String name;

    private ArmorTypes(final String name) {
        this.name = name;
    }

    public String getRegistryName(final String regName) {
        return regName + "_" + name;
    }
}