package com.bigfat.gankio_ca.presentation.common.exception;

import android.content.Context;
import com.bigfat.gankio_ca.data.exception.NetworkConnectionException;
import com.bigfat.gankio_ca.presentation.R;

/**
 * Created by yueban on 17:35 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class ErrorMessageFactory {
    private ErrorMessageFactory() {
    }

    public static String create(Context context, Exception exception) {
        String message = context.getString(R.string.exception_message_generic);

        if (exception instanceof NetworkConnectionException) {
            message = context.getString(R.string.exception_message_no_connection);
        }

        return message;
    }
}
