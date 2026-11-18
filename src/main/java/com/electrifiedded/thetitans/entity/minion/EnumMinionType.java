package com.electrifiedded.thetitans.entity.minion;

import net.minecraft.client.resources.I18n;

public enum EnumMinionType {
    LOYALIST(0, "loyalist"),
            PRIEST(1, "priest"),
            ZEALOT(2, "zealot"),
            BISHOP(3, "bishop"),
            GUARDIAN(4, "guardian"),
            TEMPLAR(5, "templar"),
            ARBITRATOR(6, "arbitrator"),
            GRANDMASTER(7, "grandmaster"),
            LORD(8, "lord"),
            SPECIAL(9, ""),
            DEMITITAN(10, "");
    private final int id;
    private final String localizationKey;

    private EnumMinionType(int id, String localizationKey) {
        this.id = id;
        this.localizationKey = localizationKey;
    }

    public int getID() {
        return id;
    }

    public String getKey() {
        return localizationKey;
    }

    public String getName() {
        return I18n.format(localizationKey);
    }

    public String getName(int localizationKeySuffix) {
        if (localizationKeySuffix != 0)
            return I18n.format(localizationKey + "." + localizationKeySuffix);
        return I18n.format(localizationKey);
    }
}
