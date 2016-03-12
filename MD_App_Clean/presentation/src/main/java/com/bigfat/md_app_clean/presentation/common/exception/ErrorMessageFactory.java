package com.bigfat.md_app_clean.presentation.common.exception;

import android.content.Context;
import com.bigfat.md_app_clean.data.exception.APIException;
import com.bigfat.md_app_clean.data.exception.ApiErrorModel;
import com.bigfat.md_app_clean.data.exception.ModelParseException;
import com.bigfat.md_app_clean.data.exception.NetworkConnectionException;
import com.bigfat.md_app_clean.presentation.R;

/**
 * 错误信息工厂
 *
 * Created by yueban on 16:04 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class ErrorMessageFactory {
    private ErrorMessageFactory() {
    }

    /**
     * 根据 {@code throwable} 对象生成错误信息
     *
     * @param context   上下文对象
     * @param throwable 错误对象
     * @return {@code throwable} 对应的错误信息
     */
    public static String create(Context context, Throwable throwable) {
        String message;
        if (throwable instanceof NetworkConnectionException) {
            message = context.getString(R.string.exception_message_no_connection);
        } else if (throwable instanceof APIException) {
            APIException apiException = (APIException) throwable;
            ApiErrorModel apiErrorModel = apiException.getApiErrorModel();
            if (apiErrorModel != null) {
                message = apiErrorModel.getError_code() + ":" + apiErrorModel.getError_msg();
            } else {
                message = context.getString(R.string.exception_message_api_unknown_error);
            }
        } else if (throwable instanceof ModelParseException) {
            message = context.getString(R.string.exception_message_api_data_parse_error);
        } else {
            message = createInternalExceptionMessage(context, throwable.getCause());
        }
        return message;
    }

    /**
     * 根据 {@code throwable} 对象生成错误信息,该方法仅供 {@link #create(Context, Throwable)} 使用,以处理 {@link Exception} 嵌套的情况
     *
     * @param context   上下文对象
     * @param throwable 错误对象
     * @return {@code throwable} 对应的错误信息
     */
    private static String createInternalExceptionMessage(Context context, Throwable throwable) {
        String message = context.getString(R.string.exception_message_generic);
        if (throwable instanceof NetworkConnectionException) {
            message = context.getString(R.string.exception_message_no_connection);
        } else if (throwable instanceof APIException) {
            APIException apiException = (APIException) throwable;
            ApiErrorModel apiErrorModel = apiException.getApiErrorModel();
            if (apiErrorModel != null) {
                message = apiErrorModel.getError_code() + ":" + apiErrorModel.getError_msg();
            } else {
                message = context.getString(R.string.exception_message_api_unknown_error);
            }
        } else if (throwable instanceof ModelParseException) {
            message = context.getString(R.string.exception_message_api_data_parse_error);
        }
        return message;
    }
}
