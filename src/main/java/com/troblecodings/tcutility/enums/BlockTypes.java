package com.troblecodings.tcutility.enums;

public enum BlockTypes {

    CUBE("block"), CUBE_ROT("block_rot"), STAIR("stair"), SLAB("slab"), FENCE("fence"),
    FENCE_GATE("fence_gate"), WALL("wall"), TRAPDOOR("latch"), WINDOW("window"), LADDER("ladder"),
    DOOR("door"), BIGDOOR("bigdoor"), HANGING("hanging"), CUBE_ROT_ALL("block_rot_all"),
    GARAGE("garage");

    private final String name;

    private BlockTypes(final String name) {
        this.name = name;
    }

    public String getRegistryName(final String objectName) {
        if (isDoor()) {
            return name + "_" + objectName + "_block";
        } else {
            return name + "_" + objectName;
        }
    }

    public boolean isDoor() {
        return this == DOOR || this == BIGDOOR;
    }

}