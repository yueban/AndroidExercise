package com.bigfat.gankio_ca.presentation.ui.meizhi.view;

import com.bigfat.gankio_ca.data.entity.GankEntity;
import com.bigfat.gankio_ca.presentation.common.ui.LoadDataView;
import java.util.Collection;

/**
 * Created by yueban on 17:22 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public interface MeizhiView extends LoadDataView {
    void renderDataList(Collection<GankEntity> gankEntityCollection, boolean isRefresh);

    void viewDay(GankEntity gankEntity);

    void loadMoreData();
}
