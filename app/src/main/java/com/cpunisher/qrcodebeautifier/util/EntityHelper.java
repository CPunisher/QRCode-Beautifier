package com.cpunisher.qrcodebeautifier.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import com.cpunisher.qrcodebeautifier.db.entity.*;
import com.cpunisher.qrcodebeautifier.pojo.OptionModel;
import com.cpunisher.qrcodebeautifier.pojo.ParamModel;
import com.cpunisher.qrcodebeautifier.pojo.StyleModel;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

public class EntityHelper {

    public static List<StyleModel> toStyleModels(List<Collection> collections) {
        List<StyleModel> styleModels = new LinkedList<>();
        for (Collection collection : collections) {
            StyleModel styleModel = new StyleModel();
            styleModel.databaseId = collection.style.id;
            styleModel.id = collection.style.styleId;
            styleModel.name = collection.style.name;
            styleModel.description = collection.style.description;
            styleModel.imgBlog = collection.style.img;
            styleModel.params = new ParamModel[collection.params.size()];

            for (int i = 0; i < collection.params.size(); i++) {
                ParamWithOptions paramWithOptions = collection.params.get(i);
                styleModel.params[i] = new ParamModel();
                styleModel.params[i].databaseId = paramWithOptions.param.id;
                styleModel.params[i].id = paramWithOptions.param.paramId;
                styleModel.params[i].name = paramWithOptions.param.name;
                styleModel.params[i].def = paramWithOptions.param.value;
                styleModel.params[i].type = paramWithOptions.param.type;

                styleModel.params[i].extra = new OptionModel[paramWithOptions.options.size()];
                for (int j = 0; j < paramWithOptions.options.size(); j++) {
                    Option option = paramWithOptions.options.get(j);
                    styleModel.params[i].extra[j] = new OptionModel();
                    styleModel.params[i].extra[j].id = option.optionId;
                    styleModel.params[i].extra[j].name = option.name;
                }
            }
            styleModels.add(styleModel);
        }
        return styleModels;
    }

    public static Collection toCollection(StyleModel styleModel, List<String> values, String collectionName, byte[] img, final Context context) {
        Style style = new Style();
        style.styleId = styleModel.id;
        style.name = collectionName;
        style.description = styleModel.description;
        style.img = img;

        List<ParamWithOptions> paramWithOptionsList = new LinkedList<>();
        for (int pos = 0; pos < styleModel.params.length; pos++) {
            ParamModel paramModel = styleModel.params[pos];
            List<Option> optionList = new LinkedList<>();

            Param param = new Param();
            param.paramId = paramModel.id;
            param.name = paramModel.name;
            param.type = paramModel.type;
            param.value = values.get(pos);
            if (paramModel.extra != null) {
                for (OptionModel optionModel : paramModel.extra) {
                    Option option = new Option();
                    option.optionId = optionModel.id;
                    option.name = optionModel.name;

                    optionList.add(option);
                }
            }
            paramWithOptionsList.add(new ParamWithOptions(param, optionList));
        }
        return new Collection(style, paramWithOptionsList);
    }

    public static byte[] imageToByteArray(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
