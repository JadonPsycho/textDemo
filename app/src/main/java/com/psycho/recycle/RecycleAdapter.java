package com.psycho.recycle;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by mr.psycho on 2017/1/13.
 */

public class RecycleAdapter extends BaseQuickAdapter<DateEntity.ResultEntity,BaseViewHolder> {


    public RecycleAdapter(int layoutResId, List<DateEntity.ResultEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, DateEntity.ResultEntity entity) {

    }
}
