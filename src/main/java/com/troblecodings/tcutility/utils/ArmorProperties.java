package com.troblecodings.tcutility.utils;

import java.util.List;

public class ArmorProperties {

    private int durability = 1000;
    private int enchantability = 30;
    private float toughness = 0F;
    private List<String> slots;

    public ArmorCreateInfo getArmorInfo() {
        return new ArmorCreateInfo(durability, enchantability, toughness);
    }

    public List<String> getSlots() {
        return slots;
    }
}