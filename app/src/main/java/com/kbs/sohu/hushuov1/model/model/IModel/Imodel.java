package com.kbs.sohu.hushuov1.model.model.IModel;

/**
 * Created by tarena on 2017/02/24.
 */

public interface Imodel {

    interface CallBack{

        void onSuccess(Object success);

        void onFailure(Object failure);
    }
}
