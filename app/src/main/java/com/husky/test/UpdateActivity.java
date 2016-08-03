package com.husky.test;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.husky.library.update.PreferenceUtils;
import com.husky.library.update.UpdateManager;

public class UpdateActivity extends AppCompatActivity {

    //测试APK地址为QQ官方网站的下载地址，如果不可用请替换为自己应用的下载地址
    private String path = "http://116.55.250.17/sqdd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk?mkey=57a024b2cd385683&f=d388&c=0&p=.apk";

    private DownloadManager manager;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btn = (Button) findViewById(R.id.check);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getVerCode(UpdateActivity.this);
                getVerName(UpdateActivity.this);
                //在此之前需要访问服务器判断服务器版本号，在这里就不写了
                showUpdateDialog(path);
            }
        });

        Button status = (Button) findViewById(R.id.status);
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkDownloadState();
            }
        });
    }

    CheckRunnable runnable = new CheckRunnable();

    private void startCheck(){
        handler.postDelayed(runnable,1000);
    }

    private void stopCheck(){
        handler.removeCallbacks(runnable);
    }

    /**
     * 版本更新Dialog
     */
    private void showUpdateDialog(final String downPath) {
        new AlertDialog.Builder(this).setTitle("提示").setMessage("发现新版本，是否更新？")
                .setPositiveButton("马上更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        new UpdateManager.Builder().setTitle("下载").setWifiOnly(false).create(UpdateActivity.this).download(downPath);
                        dialog.dismiss();
//                        startCheck();
                    }
                })
                .setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void checkDownloadState() {
        DownloadManager.Query query = new DownloadManager.Query();
        String id = PreferenceUtils.getInstance(UpdateActivity.this).getApkId();
        query.setFilterById(Long.parseLong(id));
        Cursor c = manager.query(query);
        if (c == null) {
            Toast.makeText(UpdateActivity.this, "未下载", Toast.LENGTH_SHORT).show();
            return;
        }
        if (c.moveToFirst()) {
            int statusColumn = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = c.getInt(statusColumn);
            System.out.println("status : " + status);
            int reasonColumn = c.getColumnIndex(DownloadManager.COLUMN_REASON);
            int reason = c.getInt(reasonColumn);
            System.out.println("reason : " + reason);
            switch (status) {
                case DownloadManager.STATUS_FAILED:
                    checkReason(reason);
                    stopCheck();
                    break;
                case DownloadManager.STATUS_PAUSED:
                    checkReason(reason);
                    break;
                case DownloadManager.STATUS_PENDING:
                    break;
                case DownloadManager.STATUS_RUNNING:
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    Toast.makeText(UpdateActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
                    stopCheck();
                    break;
            }
        }
        c.close();
    }

    private void checkReason(int reason) {
        switch (reason) {
            case DownloadManager.ERROR_CANNOT_RESUME:
                break;
            case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                break;
            case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                break;
            case DownloadManager.ERROR_FILE_ERROR:
                break;
            case DownloadManager.ERROR_HTTP_DATA_ERROR:
                break;
            case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                Toast.makeText(UpdateActivity.this, "下载失败，存储空间不足", Toast.LENGTH_SHORT).show();
                break;
            case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                break;
            case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                break;
            case DownloadManager.ERROR_UNKNOWN:
                break;
            case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                break;
            case DownloadManager.PAUSED_UNKNOWN:
                break;
            case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                break;
            case DownloadManager.PAUSED_WAITING_TO_RETRY:
                break;
        }
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("msg", e.getMessage());
        }
        return verCode;
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("msg", e.getMessage());
        }
        return verName;
    }

    class CheckRunnable implements Runnable{
        @Override
        public void run() {
            checkDownloadState();
            handler.postDelayed(runnable,1000);
        }
    }
}
