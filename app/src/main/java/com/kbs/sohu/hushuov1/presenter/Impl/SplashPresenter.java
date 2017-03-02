package com.kbs.sohu.hushuov1.presenter.Impl;

import com.kbs.sohu.hushuov1.model.model.IModel.ISplashModel;
import com.kbs.sohu.hushuov1.model.model.IModel.Imodel;
import com.kbs.sohu.hushuov1.model.model.Impl.SplashModel;
import com.kbs.sohu.hushuov1.presenter.Ipresenter.ISplashPresenter;
import com.kbs.sohu.hushuov1.ui.view.ISplashView;

/**
 * Created by cs on 2017/02/25.
 */

public class SplashPresenter implements ISplashPresenter {

    private ISplashModel splashModel;
    private ISplashView splashView;

    public SplashPresenter(ISplashView splashView){
        this.splashModel = new SplashModel();
        this.splashView = splashView;
    }

    @Override
    public void loadAdPic() {
        splashModel.getAdPic(new Imodel.CallBack() {
            @Override
            public void onSuccess(Object success) {
                String AdPic = (String) success;
                splashView.updateAd(AdPic);
            }

            @Override
            public void onFailure(Object failure) {

            }
        });
    }
}
