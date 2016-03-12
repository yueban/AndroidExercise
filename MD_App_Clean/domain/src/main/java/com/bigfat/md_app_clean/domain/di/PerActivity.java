package com.bigfat.md_app_clean.domain.di;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by yueban on 15:46 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Scope
@Documented
@Retention(RUNTIME)
public @interface PerActivity {
}
