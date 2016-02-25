package com.bigfat.gankio_ca.domain.interactor;

import com.bigfat.gankio_ca.domain.executor.PostExecutionTread;
import com.bigfat.gankio_ca.domain.executor.ThreadExecutor;
import com.bigfat.gankio_ca.domain.repository.GankRepository;
import javax.inject.Inject;
import rx.Observable;

/**
 * 用例:数据列表
 *
 * Created by yueban on 13:18 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class GetDataUseCase extends UseCase {
    private final GankRepository mGankRepository;
    private final String type;
    private final int pageIndex;
    private final static int PAGE_SIZE = 10;

    @Inject
    public GetDataUseCase(ThreadExecutor threadExecutor, PostExecutionTread postExecutionTread, GankRepository gankRepository,
        String type, int pageIndex) {
        super(threadExecutor, postExecutionTread);
        mGankRepository = gankRepository;
        this.type = type;
        this.pageIndex = pageIndex;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return mGankRepository.data(type, PAGE_SIZE, pageIndex);
    }
}
