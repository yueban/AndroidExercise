package com.bigfat.gankio_ca.presentation.ui.day.presenter;

import com.bigfat.gankio_ca.data.entity.DayEntity;
import com.bigfat.gankio_ca.data.entity.GankEntity;
import com.bigfat.gankio_ca.domain.interactor.GankUseCase;
import com.bigfat.gankio_ca.presentation.common.di.PerActivity;
import com.bigfat.gankio_ca.presentation.common.ui.Presenter;
import com.bigfat.gankio_ca.presentation.ui.day.view.DayView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.inject.Inject;
import rx.Subscriber;

/**
 * Created by yueban on 23:47 29/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@PerActivity
public class DayPresenter implements Presenter {
    private final GankUseCase mGankUseCase;
    DayView mView;

    @Inject
    public DayPresenter(GankUseCase gankUseCase) {
        mGankUseCase = gankUseCase;
    }

    public void setView(DayView view) {
        mView = view;
    }

    public void initialize() {

    }

    public void initData(GankEntity gankEntity) {
        if (gankEntity == null) {
            return;
        }
        try {
            String date = new SimpleDateFormat("yyyy/MM/dd").format(
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(gankEntity.getPublishedAt()));
            mGankUseCase.day(date, new Subscriber<DayEntity>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(DayEntity dayEntity) {
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
}
