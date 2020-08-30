package com.cpunisher.qrcodebeautifier.model;

import java.io.Serializable;
import java.util.Arrays;

public class StyleModel implements Serializable {

    public String id;
    public String name;
    public String description;
    public String img;
    public ParamModel[] params;
}
