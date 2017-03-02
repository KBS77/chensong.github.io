package com.kbs.sohu.hushuov1.handler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import com.kbs.sohu.hushuov1.XingApplication;
import com.kbs.sohu.hushuov1.activity.SplashActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tarena on 2017/02/25.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;
    private static final String PATH = Environment.getExternalStorageDirectory().getPath()+"/crash/log/";
    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFFIX = ".trace";

    private static CrashHandler sInstance = new CrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;

    private CrashHandler(){

    }

    public static CrashHandler getsInstance(){
        return sInstance;
    }

    public void init(XingApplication application){
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.mContext = application.getApplicationContext();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            dumpExceptionToSD(ex);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ex.printStackTrace();

        if(mDefaultCrashHandler != null){
            mDefaultCrashHandler.uncaughtException(thread,ex);
        }else{
            Intent intent = new Intent(mContext, SplashActivity.class);
            PendingIntent restartPi = PendingIntent.getActivity(mContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            manager.set(AlarmManager.RTC,System.currentTimeMillis() + 1000,restartPi);
            android.os.Process.killProcess(Process.myPid());
        }
    }

    private void dumpExceptionToSD(Throwable ex) throws IOException{

        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            if(DEBUG){
                Log.w(TAG,"SD is unmounted");
                return;
            }
        }

        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-mm-dd HH:MM:SS").format(new Date(current));

        File file = new File(PATH + FILE_NAME + time + FILE_NAME_SUFFIX);
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(time);
            dumpPhoneInfo(pw);
            pw.println();
            ex.printStackTrace(pw);
            pw.close();
        } catch (Exception e) {
            Log.e(TAG,"dump crash info failed");
        }
    }

    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),PackageManager.GET_ACTIVITIES);
        pw.print("APP VERSION:");
        pw.print(pi.versionName);
        pw.print("_");
        pw.println(pi.versionCode);

        pw.print("OS VERSION:");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);

        pw.print("Vendor:");
        pw.println(Build.MANUFACTURER);

        pw.print("Model:");
        pw.println(Build.MODEL);

        pw.print("CPU ABI");
        pw.println(Build.CPU_ABI);
    }

}
