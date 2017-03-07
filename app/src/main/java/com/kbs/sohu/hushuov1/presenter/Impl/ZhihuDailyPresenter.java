package com.kbs.sohu.hushuov1.presenter.Impl;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.kbs.sohu.hushuov1.model.bean.BeanType;
import com.kbs.sohu.hushuov1.model.bean.ZhihuNewsinfo;
import com.kbs.sohu.hushuov1.model.db.ZhiDBHelper;
import com.kbs.sohu.hushuov1.model.model.IModel.Imodel;
import com.kbs.sohu.hushuov1.model.model.Impl.ZhihuModel;
import com.kbs.sohu.hushuov1.presenter.Ipresenter.ZhihuNewsContract;
import com.kbs.sohu.hushuov1.service.CacheService;
import com.kbs.sohu.hushuov1.ui.activity.NewsDetailActivity;
import com.kbs.sohu.hushuov1.utils.APi;
import com.kbs.sohu.hushuov1.utils.DateFormatter;
import com.kbs.sohu.hushuov1.utils.NetworkState;
import com.kbs.sohu.hushuov1.utils.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by cs on 2017/03/7.
 */

public class ZhihuDailyPresenter implements ZhihuNewsContract.Presenter {

    private ZhihuNewsContract.View view;
    private Context context;
    private ZhihuModel zhmodel;

    private DateFormatter formatter = new DateFormatter();
    private Gson gson = new Gson();

    private ArrayList<ZhihuNewsinfo.Question> list = new ArrayList<ZhihuNewsinfo.Question>();

    private ZhiDBHelper dbHelper;
    private SQLiteDatabase db;

    public ZhihuDailyPresenter(Context context, ZhihuNewsContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        zhmodel = new ZhihuModel(context);
        dbHelper = new ZhiDBHelper(context, "History.db",null,5);
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public void loadPosts(long date, final boolean clearing) {

        if (clearing) {
            view.showLoading();
        }

        if (NetworkState.networkConnected(context)) {

            zhmodel.load(APi.ZHIHU_HISTORY + formatter.ZhihuDailyDateFormat(date), new Imodel.CallBack() {

                @Override
                public void onSuccess(Object success) {
                    try {
                        String result = (String)success;
                        ZhihuNewsinfo post = Utility.handleZhihuResponse(result);
                        ContentValues values = new ContentValues();

                        if (clearing) {
                            list.clear();
                        }

                        for (ZhihuNewsinfo.Question item : post.getStories()) {
                            list.add(item);
                            if ( !queryIfIDExists(item.getId())) {
                                db.beginTransaction();
                                try {
                                    DateFormat format = new SimpleDateFormat("yyyyMMdd");
                                    Date date = format.parse(post.getDate());
                                    values.put("zhihu_id", item.getId());
                                    values.put("zhihu_news", gson.toJson(item));
                                    values.put("zhihu_content", "");
                                    values.put("zhihu_time", date.getTime() / 1000);
                                    db.insert("Zhihu", null, values);
                                    values.clear();
                                    db.setTransactionSuccessful();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    db.endTransaction();
                                }

                            }
                            Intent intent = new Intent("com.kbs.sohu.hushuov1.LOCAL_BROADCAST");
                            intent.putExtra("type", CacheService.TYPE_ZHIHU);
                            intent.putExtra("id", item.getId());
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                        }
                        view.showResults(list);
                    } catch (JsonSyntaxException e) {
                        view.showError();
                    }

                    view.stopLoading();
                }

                @Override
                public void onFailure(Object failure) {
                    view.stopLoading();
                    view.showError();
                }
            });
        } else {

            if (clearing) {

                list.clear();

                Cursor cursor = db.query("Zhihu", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        ZhihuNewsinfo.Question question = gson.fromJson(cursor.getString(cursor.getColumnIndex("zhihu_news")), ZhihuNewsinfo.Question.class);
                        list.add(question);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                view.stopLoading();
                view.showResults(list);

            } else {
                view.showError();
            }

        }
    }

    @Override
    public void refresh() {
        loadPosts(Calendar.getInstance().getTimeInMillis(), true);
    }

    @Override
    public void loadMore(long date) {
        loadPosts(date, false);
    }

    @Override
    public void startReading(int position) {

        context.startActivity(new Intent(context, NewsDetailActivity.class)
                .putExtra("type", BeanType.TYPE_ZHIHU)
                .putExtra("id", list.get(position).getId())
                .putExtra("title", list.get(position).getTitle())
                .putExtra("coverUrl", list.get(position).getImages().get(0)));

    }

    @Override
    public void feelLucky() {
        if (list.isEmpty()) {
            view.showError();
            return;
        }
        startReading(new Random().nextInt(list.size()));
    }

    @Override
    public void start() {
        loadPosts(Calendar.getInstance().getTimeInMillis(), true);
    }

    private boolean queryIfIDExists(int id){

        Cursor cursor = db.query("Zhihu",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                if (id == cursor.getInt(cursor.getColumnIndex("zhihu_id"))){
                    return true;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return false;
    }
}
