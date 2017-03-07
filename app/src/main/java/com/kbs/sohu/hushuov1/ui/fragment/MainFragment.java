package com.kbs.sohu.hushuov1.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kbs.sohu.hushuov1.R;
import com.kbs.sohu.hushuov1.model.OnRecyclerViewOnClickListener;
import com.kbs.sohu.hushuov1.model.bean.ZhihuNewsinfo;
import com.kbs.sohu.hushuov1.presenter.Impl.ZhihuDailyPresenter;
import com.kbs.sohu.hushuov1.presenter.Ipresenter.ZhihuNewsContract;
import com.kbs.sohu.hushuov1.ui.adapter.ZhihuNewsAdapter;
import com.kbs.sohu.hushuov1.ui.widget.LoodView;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chensong on 2017/02/8.
 */

public class MainFragment extends Fragment implements ZhihuNewsContract.View{

    private SwipeRefreshLayout refresh;
    @BindView(R.id.vp_mf_recommend)
    LoodView vp_recommend;
    @BindView(R.id.tv_main_zaitu)
    TextView tv_main_zaitu;
    @BindView(R.id.tv_main_dream)
    TextView tv_main_dream;
    @BindView(R.id.tv_main_time)
    TextView tv_main_time;
    @BindView(R.id.tv_main_jiangxin)
    TextView tv_main_jiangxin;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;

    private ZhihuNewsAdapter adapter;

    private static final String TAG = MainFragment.class.getSimpleName();

    private int mYear = Calendar.getInstance().get(Calendar.YEAR);
    private int mMonth = Calendar.getInstance().get(Calendar.MONTH);
    private int mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    private ZhihuNewsContract.Presenter presenter = new ZhihuDailyPresenter(getActivity(),this);


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        ButterKnife.bind(view);
        initView(view);

        presenter.start();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                presenter.refresh();
            }

        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 获取最后一个完全显示的item position
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部并且是向下滑动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        Calendar c = Calendar.getInstance();
                        c.set(mYear, mMonth, --mDay);
                        presenter.loadMore(c.getTimeInMillis());
                    }
                }

                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isSlidingToLast = dy > 0;

                // 隐藏或者显示fab
                if(dy > 0) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });
        return view;
    }

    @Override
    public void setPresenter(ZhihuNewsContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_main_list);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //设置下拉刷新的按钮的颜色
        refresh.setColorSchemeResources(R.color.colorPrimary);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setRippleColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    @Override
    public void showError() {
        Snackbar.make(fab, R.string.loaded_failed,Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.refresh();
                    }
                })
                .show();
    }

    @Override
    public void showLoading() {
        refresh.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(true);
            }
        });
    }

    @Override
    public void stopLoading() {
        refresh.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void showResults(ArrayList<ZhihuNewsinfo.Question> list) {
        if (adapter == null) {
            adapter = new ZhihuNewsAdapter(getContext(), list);
            adapter.setItemClickListener(new OnRecyclerViewOnClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    presenter.startReading(position);
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}
