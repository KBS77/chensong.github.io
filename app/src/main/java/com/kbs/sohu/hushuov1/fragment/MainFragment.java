package com.kbs.sohu.hushuov1.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kbs.sohu.hushuov1.R;
import com.kbs.sohu.hushuov1.handler.HandlerUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chensong on 2017/02/8.
 */

public class MainFragment extends Fragment {

    private Toolbar toolbar;
    private ViewPager vp_mf_recommend;
    private TextView tv_mf_title;
    private LinearLayout ll_point_group;
    private static final int POINTSIZE = 10;
    private int pointMargin = 10;
    private List<ImageView> mList = new ArrayList<>();
    private int[] images = {R.drawable.sa,R.drawable.sb,R.drawable.sc,R.drawable.sd,R.drawable.se,R.drawable.sf};
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HandlerUtil.getInstance(getActivity().getApplicationContext()).postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentItem = vp_mf_recommend.getCurrentItem();
                if(currentItem == vp_mf_recommend.getAdapter().getCount() - 1){
                    vp_mf_recommend.setCurrentItem(1);
                }else{
                    vp_mf_recommend.setCurrentItem(currentItem + 1);
                }
                HandlerUtil.getInstance(getActivity().getApplicationContext()).postDelayed(this,3500);
            }
        },3500);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        vp_mf_recommend = (ViewPager) view.findViewById(R.id.vp_mf_recommend);
        tv_mf_title = (TextView) view.findViewById(R.id.tv_mf_title);
        ll_point_group = (LinearLayout) view.findViewById(R.id.ll_point_group);
        for(int i = 0;i < images.length;i++){
            ImageView img = new ImageView(getActivity());
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            img.setImageResource(images[i]);
            mList.add(img);
        }
        for(int i = 0;i < images.length;i++){
            ImageView point = new ImageView(getActivity());
            point.setImageResource(R.drawable.shape_point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(POINTSIZE,POINTSIZE);
            if(i > 0){
                params.leftMargin = pointMargin;
                point.setSelected(false);
            }else{
                point.setSelected(true);
            }
            point.setLayoutParams(params);
            ll_point_group.addView(point);
        }
        PagerAdapter adapter = new FirstAdapter();
        vp_mf_recommend.setAdapter(adapter);
        vp_mf_recommend.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            int lastPosition;
            @Override
            public void onPageSelected(int position) {
                position = position%mList.size();
                ll_point_group.getChildAt(position).setSelected(true);
                ll_point_group.getChildAt(lastPosition).setSelected(false);
                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    class FirstAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position%mList.size();
            final View child = mList.get(position);
            if(child.getParent() != null){
                container.removeView(child);
            }
            container.addView(mList.get(position));
            return mList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
