package com.cpunisher.qrcodebeautifier.db.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Option {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String optionId;
    public String name;

    public long paramId;

    public Option() {}

    @Ignore
    public Option(String optionId, String name, long paramId) {
        this.optionId = optionId;
        this.name = name;
        this.paramId = paramId;
    }
}
