package com.cpunisher.qrcodebeautifier.listener;

import com.cpunisher.qrcodebeautifier.model.StyleModel;

public interface ParamUpdatedListener {
    void onParamUpdated(StyleModel styleModel, String[] params);
}