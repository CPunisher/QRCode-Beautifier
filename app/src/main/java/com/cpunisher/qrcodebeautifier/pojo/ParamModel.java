package com.cpunisher.qrcodebeautifier.pojo;

import java.io.Serializable;

public class ParamModel implements Serializable {

    public String id;
    public String name;
    public String type;
    public String def;
    public OptionModel[] extra;

    public long databaseId;

    public ParamModel() {}

    public ParamModel(String id, String name, String type, String def, OptionModel[] extra) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.def = def;
        this.extra = extra;
    }
}
