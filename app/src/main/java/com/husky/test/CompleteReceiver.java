package com.husky.test;

import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;

/**
 * 下载广播接受者，用于下载完成后，安装应用
 *
 * 需要在mainfest里面注册
 */
public class CompleteReceiver extends BroadcastReceiver {
    private DownloadManager downloadManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            Toast.makeText(context, "下载完成！", Toast.LENGTH_LONG).show();

            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);//TODO 判断这个id与之前的id是否相等，如果相等说明是之前的那个要下载的文件

            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(id);
            downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Cursor cursor = downloadManager.query(query);
            int columnCount = cursor.getColumnCount();
//            String path = null;
            String path = "";//TODO 这里把所有的列都打印一下，有什么需求，就怎么处理,文件的本地路径就是path
            while (cursor.moveToNext()) {
                for (int j = 0; j < columnCount; j++) {
                    String columnName = cursor.getColumnName(j);
                    String string = cursor.getString(j);
                    if (columnName.equals("local_uri")) {
                        path = string;
                    }
                    if (string != null) {
                        System.out.println(columnName + ": " + string);
                    } else {
                        System.out.println(columnName + ": null");
                    }
                }
            }
            cursor.close();

            //如果sdcard不可用时下载下来的文件，那么这里将是一个内容提供者的路径，这里打印出来，有什么需求就怎么样处理  if(path.startsWith("content:")) {
            if(path.startsWith("content:")) {
                System.out.println("-----------------------CompleteReceiver 下载完了----路径path = " + path);
            }

            if(id == Long.parseLong(MyApplication.getInstance().getApkId())){//TODO 判断这个id与之前的id是否相等，如果相等说明是之前的那个要下载的文件
                Intent install = new Intent(Intent.ACTION_VIEW);
//                Uri downloadFileUri = downloadManager.getUriForDownloadedFile(id);
                String uriString = getFilePathFromUri(context, Uri.parse(path));//TODO 转换path路径 否则报解析包错误
                System.out.println("-----------------------CompleteReceiver 转换后----路径uriString = " + uriString);
                install.setDataAndType(Uri.fromFile(new File(uriString)), "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
                Toast.makeText(context, "正在启动安装程序", Toast.LENGTH_SHORT).show();
            }
        } else if (action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
            Toast.makeText(context,  "点击通知了....", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 转换 path路径
     * */
    public static String getFilePathFromUri(Context c, Uri uri) {
        String filePath = null;
        if ("content".equals(uri.getScheme())) {
            String[] filePathColumn = { MediaStore.MediaColumns.DATA };
            ContentResolver contentResolver = c.getContentResolver();

            Cursor cursor = contentResolver.query(uri, filePathColumn, null,
                    null, null);

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        } else if ("file".equals(uri.getScheme())) {
            filePath = new File(uri.getPath()).getAbsolutePath();
        }
        return filePath;
    }

}
