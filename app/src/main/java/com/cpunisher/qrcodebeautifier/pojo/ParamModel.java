package com.cpunisher.qrcodebeautifier.pojo;

import java.io.Serializable;

public class ParamModel implements Serializable {

    public String id;
    public String name;
    public String type;
    public String def;
    public OptionModel[] extra;
}
