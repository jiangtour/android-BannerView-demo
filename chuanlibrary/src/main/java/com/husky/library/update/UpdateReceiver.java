package com.husky.library.update;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * @author husky
 */
public class UpdateReceiver extends BroadcastReceiver {

    private DownloadManager downloadManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("receive broadcast");
        String action = intent.getAction();
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(id);
            downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Cursor cursor = downloadManager.query(query);
            int columnCount = cursor.getColumnCount();
            String path = "";
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

                    if (columnName.equals("status")){
                        System.out.println("status : " + cursor.getInt(j));
                    }
                }
            }
            cursor.close();

            //如果sdcard不可用时下载下来的文件，那么这里将是一个内容提供者的路径，这里打印出来，有什么需求就怎么样处理  if(path.startsWith("content:")) {
            if(path.startsWith("content:")) {
                System.out.println("-----------------------CompleteReceiver 下载完了----路径path = " + path);
            }

            if(id == Long.parseLong(PreferenceUtils.getInstance(context).getApkId())){
                Intent install = new Intent(Intent.ACTION_VIEW);
                String uriString = getFilePathFromUri(context, Uri.parse(path));
                System.out.println("-----------------------CompleteReceiver 转换后----路径uriString = " + uriString);
                install.setDataAndType(Uri.fromFile(new File(uriString)), "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            }
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

//    private void checkDownloadState() {
//        DownloadManager.Query query = new DownloadManager.Query();
//        String id = PreferenceUtils.getInstance(UpdateActivity.this).getApkId();
//        query.setFilterById(Long.parseLong(id));
//        Cursor c = manager.query(query);
//        if (c == null) {
//            Toast.makeText(UpdateActivity.this, "未下载", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (c.moveToFirst()) {
//            int statusColumn = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
//            int status = c.getInt(statusColumn);
//            System.out.println("status : " + status);
//            int reasonColumn = c.getColumnIndex(DownloadManager.COLUMN_REASON);
//            int reason = c.getInt(reasonColumn);
//            System.out.println("reason : " + reason);
//            switch (status) {
//                case DownloadManager.STATUS_FAILED:
//                    checkReason(reason);
//                    break;
//                case DownloadManager.STATUS_PAUSED:
//                    checkReason(reason);
//                    break;
//                case DownloadManager.STATUS_PENDING:
//                    break;
//                case DownloadManager.STATUS_RUNNING:
//                    break;
//                case DownloadManager.STATUS_SUCCESSFUL:
//                    Toast.makeText(UpdateActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
//        c.close();
//    }
//
//    private void checkReason(int reason) {
//        switch (reason) {
//            case DownloadManager.ERROR_CANNOT_RESUME:
//                break;
//            case DownloadManager.ERROR_DEVICE_NOT_FOUND:
//                break;
//            case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
//                break;
//            case DownloadManager.ERROR_FILE_ERROR:
//                break;
//            case DownloadManager.ERROR_HTTP_DATA_ERROR:
//                break;
//            case DownloadManager.ERROR_INSUFFICIENT_SPACE:
//                Toast.makeText(UpdateActivity.this, "下载失败，存储空间不足", Toast.LENGTH_SHORT).show();
//                break;
//            case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
//                break;
//            case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
//                break;
//            case DownloadManager.ERROR_UNKNOWN:
//                break;
//            case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
//                break;
//            case DownloadManager.PAUSED_UNKNOWN:
//                break;
//            case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
//                break;
//            case DownloadManager.PAUSED_WAITING_TO_RETRY:
//                break;
//        }
//    }

}
