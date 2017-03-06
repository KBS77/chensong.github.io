package com.kbs.sohu.hushuov1.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.kbs.sohu.hushuov1.R;
import com.kbs.sohu.hushuov1.model.Model;
import com.kbs.sohu.hushuov1.model.bean.UserInfo;
import com.kbs.sohu.hushuov1.ui.fragment.ChatFragment;
import com.kbs.sohu.hushuov1.ui.fragment.ContactListFragment;
import com.kbs.sohu.hushuov1.ui.fragment.MainFragment;
import com.kbs.sohu.hushuov1.ui.fragment.SettingFragment;
import com.kbs.sohu.hushuov1.ui.widget.HeaderDialog;
import com.kbs.sohu.hushuov1.ui.widget.RoadViewPager;
import com.kbs.sohu.hushuov1.ui.widget.TopMenuDialog;
import com.kbs.sohu.hushuov1.utils.handler.HandlerUtil;
import com.znq.zbarcode.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.tl_main) TabLayout tl_main;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout mdrawerLayout;
    @BindView(R.id.nv_left_menu) NavigationView nv_left_menu;
    @BindView(R.id.toolbar_add)
    Button toolbar_add;
    private ImageView iv_menu_bg;
    private CircleImageView civ_user_photo;
    private TextView tv_user_nick;
    private TopMenuDialog top_menu_dialog;
    @BindView(R.id.vp_fragment) RoadViewPager vp_fragment;
    private ActionBar ab;
    private long time = 0;
    private UserInfo user;
    private static String[] mTitles = new String[]{"行图", "消息", "联系人", "设置"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        setToolBar();
        setViewPagers();
        setDrawers();
        toolbar_add.setOnClickListener(this);
//        setHeader();
//        initListener();
    }

    private void initView() {
        iv_menu_bg = (ImageView)findViewById(R.id.iv_menu_bg);
        civ_user_photo = (CircleImageView)findViewById(R.id.civ_user_photo);
        tv_user_nick = (TextView)findViewById(R.id.tv_user_nick1);
        top_menu_dialog = new TopMenuDialog(MainActivity.this, new TopMenuDialog.OnTopMenuSelectListener() {
            @Override
            public void onSelected(int position) {
                switch (position) {
                    case 0:
                        //添加
                        NewGroupActivity.start(MainActivity.this,NewGroupActivity.class); //创建群聊
                        break;
                    case 1:
                        //添加朋友
                        AddFriendActivity.start(MainActivity.this,AddFriendActivity.class);
                        break;
                    case 2:
                        Intent intent = new Intent(MainActivity.this,CaptureActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
        int[] icons = {R.mipmap.chat_create, R.mipmap.add_friend, R.mipmap.menu_sao};
        String[] menus = {"创建群聊", "添加朋友", "扫一扫"};
        top_menu_dialog.setMenus(icons, menus);
    }


    private void setToolBar() {
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setTitle("");
    }

//    private void setHeader() {
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(XingApplication.getGlobalApplication());
//        String menuBc = prefs.getString("ad_pic",null);
//        Glide.with(this).load(R.drawable.steve_jobs).into(civ_user_photo);
//        Glide.with(this).load(menuBc).bitmapTransform(new BlurTransformation(this,5),new CenterCrop(this)).into(iv_menu_bg);
//        refreshNick();
//    }

    private void initListener() {
        civ_user_photo.setOnClickListener(this);
        tv_user_nick.setOnClickListener(this);
    }

    private void setViewPagers() {
        final MainFragment mainFragment = new MainFragment();
        final ChatFragment chatFragment = new ChatFragment();
        final ContactListFragment contactListFragment = new ContactListFragment();
        final SettingFragment settingFragment = new SettingFragment();
        final RoadViewPagerAdapter roadViewPagerAdapter = new RoadViewPagerAdapter(getSupportFragmentManager());
        roadViewPagerAdapter.addFragment(mainFragment);
        roadViewPagerAdapter.addFragment(chatFragment);
        roadViewPagerAdapter.addFragment(contactListFragment);
        roadViewPagerAdapter.addFragment(settingFragment);
        vp_fragment.setAdapter(roadViewPagerAdapter);

        tl_main.setupWithViewPager(vp_fragment);

        tl_main.getTabAt(0).setIcon(R.drawable.selector_ico03);
        tl_main.getTabAt(1).setIcon(R.drawable.selector_ico01);
        tl_main.getTabAt(2).setIcon(R.drawable.selector_ico02);
        tl_main.getTabAt(3).setIcon(R.drawable.selector_ico04);


        tl_main.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                // 设置ViewPager的页面切换
                vp_fragment.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.civ_user_photo:
                HeaderDialog headerDialog = new HeaderDialog();
                headerDialog.show(getSupportFragmentManager(),"dialog");
                break;
            case R.id.tv_user_nick1:
                ChangeNickActivity.actionStart(MainActivity.this,tv_user_nick.getText().toString());
                break;
            case R.id.toolbar_add:
                top_menu_dialog.show(v);
        }
    }

    static class RoadViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<>();

        public RoadViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment) {
            mFragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

    }

    private void setDrawers() {
        nv_left_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                try {
                    switch (item.getItemId()){
                        case R.id.nav_call:
                            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                            }else{
                                openAlbum();
                                mdrawerLayout.closeDrawers();
                            }
                            break;
                        case R.id.nav_location:
                            Intent intent1 = new Intent(MainActivity.this, LBSActivity.class);
                            startActivity(intent1);
                            mdrawerLayout.closeDrawers();
                            break;
                        case R.id.nav_mail:
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                            if(prefs.getString("weather",null) != null) {
                                Intent intent2 = new Intent(MainActivity.this, WeatherActivity.class);
                                startActivity(intent2);
                                mdrawerLayout.closeDrawers();
                            }else{
                                Intent intent3 = new Intent(MainActivity.this,ChooseAreaActivity.class);
                                startActivity(intent3);
                                mdrawerLayout.closeDrawers();
                            }
                            break;
                        case R.id.nav_quit:
                            quit();
                            mdrawerLayout.closeDrawers();
                            break;
                    }
                } finally {
                    return true;
                }
            }
        });
    }

    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivity(intent);
    }

    private void refreshNick() {
        user = Model.getInstance().getUserAccountDao().getAccountByHxId(EMClient.getInstance().getCurrentUser());
        tv_user_nick.setText(user.getName());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(MainActivity.this,"你拒绝了此权限",Toast.LENGTH_SHORT).show();
                }
            break;
            default:
        }
    }

    private void quit() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Model.getInstance().getDbManager().close();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,"退出成功",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onError(int i,final String s) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,"退出失败"+s,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mdrawerLayout.openDrawer(Gravity.LEFT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        HandlerUtil.getInstance(this).removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - time > 1000)) {
                Toast.makeText(this, "再按一次返回桌面", Toast.LENGTH_SHORT).show();
                time = System.currentTimeMillis();
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
