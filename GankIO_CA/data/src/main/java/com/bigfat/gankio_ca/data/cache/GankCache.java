package com.bigfat.gankio_ca.data.cache;

import com.bigfat.gankio_ca.data.entity.GankEntity;
import java.util.List;
import rx.Observable;

/**
 * Created by yueban on 12:46 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public interface GankCache {
    Observable<List<GankEntity>> getData(String type, int pageSize, int pageIndex);

    void saveData(List<GankEntity> gankEntities);

    void clearData();
}
