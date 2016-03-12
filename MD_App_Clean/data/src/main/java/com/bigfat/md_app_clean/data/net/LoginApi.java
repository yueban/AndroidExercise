package com.bigfat.md_app_clean.data.net;

import com.bigfat.md_app_clean.data.entity.viewentity.AuthEntity;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by yueban on 17:10 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public interface LoginApi {
    /**
     * @param app_key       分配到的App Key
     * @param app_secret    分配到的App Secret
     * @param grant_type    请求的类型，可以为authorization_code、password、refresh_token。
     * @param username      授权用户的用户名。
     * @param password      授权用户的密码。
     * @param p_signature   企业网络ID，首次默认传空（用户多网络返回企业网络列表）
     * @param code          调用authorize获得的code值。
     * @param redirect_uri  回调地址，需要与注册应用里的回调地址一致。
     * @param refresh_token 与授权令牌同时获取的刷新令牌。
     */
    @POST(NetConstant.API_OAUTH2_ACCESS_TOKEN)
    Observable<AuthEntity> access_token(
        @Query("app_ke") String app_key,
        @Query("app_secret") String app_secret,
        @Query("grant_type") String grant_type,
        @Query("username") String username,
        @Query("password") String password,
        @Query("p_signature") String p_signature,
        @Query("code") String code,
        @Query("redirect_uri") String redirect_uri,
        @Query("refresh_token") String refresh_token);

    class GrantType {
        public static final String AUTHORIZATION_CODE = "authorization_code ";
        public static final String PASSWORD = "password";
        public static final String REFRESH_TOKEN = "refresh_token";
    }
}
