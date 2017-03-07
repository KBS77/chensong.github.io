package com.kbs.sohu.hushuov1.ui.view;

import android.view.View;

/**
 * Created by cs on 2017/03/7.
 */

public interface BaseView<T>{

    void setPresenter(T presenter);

    void initView(View view);
}
