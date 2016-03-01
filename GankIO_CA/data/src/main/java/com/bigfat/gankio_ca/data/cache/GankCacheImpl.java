package com.bigfat.gankio_ca.data.cache;

import com.bigfat.gankio_ca.data.entity.GankEntity;
import com.bigfat.gankio_ca.data.entity.GankEntity_Table;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;

/**
 * Created by yueban on 09:18 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Singleton
public class GankCacheImpl implements GankCache {
    @Inject
    public GankCacheImpl() {
    }

    public Observable<List<GankEntity>> getData(String type, int pageSize, int pageIndex) {
        return Observable.just(
            SQLite
                .select()
                .from(GankEntity.class)
                .where(GankEntity_Table.type.eq(type))
                .limit(pageSize)
                .queryList());
    }

    public void saveData(List<GankEntity> gankEntities) {
        for (GankEntity gankEntity : gankEntities) {
            gankEntity.insert();
        }
    }

    public void clearData() {
        Delete.table(GankEntity.class);
    }
}
