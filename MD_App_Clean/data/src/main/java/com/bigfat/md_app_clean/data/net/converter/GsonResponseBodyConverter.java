package com.bigfat.md_app_clean.data.net.converter;

import com.bigfat.md_app_clean.data.entity.IsNull;
import com.bigfat.md_app_clean.data.exception.APIException;
import com.bigfat.md_app_clean.data.exception.ApiErrorModel;
import com.bigfat.md_app_clean.data.exception.ModelParseException;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by yueban on 18:17 2/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = null;
        try {
            response = value.string();
            T t = adapter.fromJson(response);
            if (t != null && t instanceof IsNull && !((IsNull) t).isNull()) {
                return t;
            }
            throw new ModelParseException();
        } catch (ModelParseException e) {
            ApiErrorModel apiErrorModel = gson.fromJson(response, ApiErrorModel.class);
            if (apiErrorModel != null) {
                throw new APIException(apiErrorModel);
            }
            throw e;
        } finally {
            value.close();
        }
    }
}
