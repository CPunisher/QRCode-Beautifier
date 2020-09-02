package com.cpunisher.qrcodebeautifier.http;

import android.util.Log;
import android.widget.Toast;
import com.android.volley.Response;
import com.cpunisher.qrcodebeautifier.adapter.StyleAdapter;
import com.cpunisher.qrcodebeautifier.pojo.OptionModel;
import com.cpunisher.qrcodebeautifier.pojo.ParamModel;
import com.cpunisher.qrcodebeautifier.pojo.StyleModel;
import com.cpunisher.qrcodebeautifier.util.References;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class StyleListListener implements Response.Listener<JSONArray> {

    private StyleAdapter adapter;

    public StyleListListener(StyleAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onResponse(JSONArray response) {
        Log.d(References.TAG, response.toString());

        List<StyleModel> styleModelList = new LinkedList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonObject = response.getJSONObject(i);
                JSONArray paramJsonArray = jsonObject.getJSONArray("params");

                StyleModel styleModel = new StyleModel();
                styleModel.id = jsonObject.optString("id");
                styleModel.name = jsonObject.optString("name");
                styleModel.description = jsonObject.optString("description");
                styleModel.img = jsonObject.optString("img");

                styleModel.params = new ParamModel[paramJsonArray.length()];
                for (int j = 0; j < paramJsonArray.length(); j++) {
                    JSONObject paramJsonObject = paramJsonArray.getJSONObject(j);
                    styleModel.params[j] = new ParamModel();

                    styleModel.params[j].id = paramJsonObject.optString("id");
                    styleModel.params[j].name = paramJsonObject.optString("name");
                    styleModel.params[j].type = paramJsonObject.optString("type");
                    styleModel.params[j].def = paramJsonObject.optString("default");

                    JSONArray extraJsonArray = paramJsonObject.optJSONArray("extra");
                    if (extraJsonArray != null) {
                        styleModel.params[j].extra = new OptionModel[extraJsonArray.length()];
                        for (int k = 0; k < extraJsonArray.length(); k++) {
                            styleModel.params[j].extra[k] = new OptionModel();
                            styleModel.params[j].extra[k].id = extraJsonArray.getJSONObject(k).optString("id");
                            styleModel.params[j].extra[k].name = extraJsonArray.getJSONObject(k).optString("name");
                        }
                    }
                }
                styleModelList.add(styleModel);
            }
            this.adapter.setData(styleModelList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
