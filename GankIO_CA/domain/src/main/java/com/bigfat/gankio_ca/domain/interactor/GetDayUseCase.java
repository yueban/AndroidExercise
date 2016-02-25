package com.bigfat.gankio_ca.domain.interactor;

import com.bigfat.gankio_ca.domain.executor.PostExecutionTread;
import com.bigfat.gankio_ca.domain.executor.ThreadExecutor;
import com.bigfat.gankio_ca.domain.repository.GankRepository;
import javax.inject.Inject;
import rx.Observable;

/**
 * 用例:每日数据
 *
 * Created by yueban on 13:18 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class GetDayUseCase extends UseCase {
    private final GankRepository mGankRepository;
    private final String date;

    @Inject
    public GetDayUseCase(ThreadExecutor threadExecutor, PostExecutionTread postExecutionTread, GankRepository gankRepository,
        String date) {
        super(threadExecutor, postExecutionTread);
        mGankRepository = gankRepository;
        this.date = date;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return mGankRepository.day(date);
    }
}
