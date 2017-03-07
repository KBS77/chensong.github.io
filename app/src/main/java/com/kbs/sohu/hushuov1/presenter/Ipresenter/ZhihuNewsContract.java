package com.kbs.sohu.hushuov1.presenter.Ipresenter;

import com.kbs.sohu.hushuov1.model.bean.ZhihuNewsinfo;
import com.kbs.sohu.hushuov1.presenter.BasePresenter;
import com.kbs.sohu.hushuov1.ui.view.BaseView;

import java.util.ArrayList;

/**
 * Created by cs on 2017/03/7.
 */

public interface ZhihuNewsContract {

    interface View extends BaseView<Presenter> {

        void showError();

        void showLoading();

        void stopLoading();

        void showResults(ArrayList<ZhihuNewsinfo.Question> list);

    }

    interface Presenter extends BasePresenter {

        void loadPosts(long date, boolean clearing);

        void refresh();

        void loadMore(long date);

        void startReading(int position);

        void feelLucky();

    }
}
