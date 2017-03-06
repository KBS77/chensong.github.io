package com.kbs.sohu.hushuov1.ui.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kbs.sohu.hushuov1.R;

import static com.kbs.sohu.hushuov1.utils.ScreenUtil.screenWidth;
import static com.kbs.sohu.hushuov1.utils.UITool.settingBgAlpha;

/**
 * Created by cs on 2017/03/6.
 */

public class TopMenuDialog extends PopupWindow {

    public interface OnTopMenuSelectListener{
        void onSelected(int position);
    }
    private OnTopMenuSelectListener topMenuSelectListener;


    private Activity activity;
    private int[] icons;
    private String[] menus = new String[]{};
    private ListView lv;
    private BaseAdapter adapter;
    private LayoutInflater inflater;


    public TopMenuDialog(Activity activity,OnTopMenuSelectListener listener){
        this.activity = activity;
        this.topMenuSelectListener = listener;
        inflater = LayoutInflater.from(activity);
        init();
    }

    public void setMenus(int[] icons,String[] menus){
        this.icons = icons;
        this.menus = menus;
    }

    public void show(View targetView){
        adapter.notifyDataSetChanged();

//		int[] location = new int[2];
//		targetView.getLocationOnScreen(location);
//		showAtLocation(targetView, Gravity.NO_GRAVITY, location[0] - getWidth() + targetView.getMeasuredWidth() / 2, location[1] + targetView.getMeasuredHeight());
        showAsDropDown(targetView);
        settingBgAlpha(0.6f, activity);
    }

    public void setWhiteBg(){
        setBackgroundDrawable(new ColorDrawable(0xffffffff));
    }

    public void showCenterHori(View targetView){
        int[] location = new int[2];
        targetView.getLocationOnScreen(location);
        showAtLocation(activity.getWindow().getDecorView(), Gravity.NO_GRAVITY, screenWidth / 4,location[1] + targetView.getMeasuredHeight());
        settingBgAlpha(0.6f, activity);
    }

    private void init(){
        DisplayMetrics dm = activity.getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        setWidth((int)(screenWidth * 0.5));
        setHeight(-1);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        //设置背景，去掉黑边
        ColorDrawable dw = new ColorDrawable(0x00000000);
        setBackgroundDrawable(dw);

        setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                dismiss();
                settingBgAlpha(1f, activity);
            }
        });
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_top_menu,null);
        setContentView(view);

        lv = (ListView) view.findViewById(R.id.lv_top_menu);
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return menus.length;
            }

            @Override
            public Object getItem(int position) {
                return menus[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = inflater.inflate(R.layout.item_menu_top,parent,false);
                ImageView iv = (ImageView) view.findViewById(R.id.iv_menu_icon);
                TextView tv = (TextView) view.findViewById(R.id.tv_menu_name);
                tv.setText(menus[position]);
                if(icons != null){
                    iv.setVisibility(View.VISIBLE);
                    iv.setImageResource(icons[position]);
                }else{
                    iv.setVisibility(View.GONE);
                }
                return view;
            }
        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();
                if(topMenuSelectListener != null){
                    topMenuSelectListener.onSelected(position);
                }
            }
        });
    }
}
