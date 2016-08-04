package com.husky.library.update;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.husky.library.SpecialToast;
import com.husky.library.tools.CustomTool;

/**
 * @author husky
 */
public class UpdateManager {

    private DownloadManager manager;
    private String title;
    private String description;
    private boolean wifiOnly;
    private Context context;

    public UpdateManager(Context context) {
        this.context = context;
        manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public void download(String path) {
        if (!CustomTool.isNetworkAvailable(context)) {
            SpecialToast.make(context, SpecialToast.TYPE_ERROR, "网络连接不可用", SpecialToast.LENGTH_SHORT).show();
            return;
        }
        if (wifiOnly && !CustomTool.isWifi(context)) {
            SpecialToast.make(context, SpecialToast.TYPE_ERROR, "您在非wifi环境下，下载已取消", SpecialToast.LENGTH_SHORT).show();
            return;
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(path));
        if (!TextUtils.isEmpty(title)) {
            request.setTitle(title);
        }
        if (!TextUtils.isEmpty(description)) {
            request.setDescription(description);
        }
        if (wifiOnly) {
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        } else {
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        }
        request.setVisibleInDownloadsUi(false);
        request.setMimeType("application/vnd.android.package-archive");
        long id = manager.enqueue(request);
        PreferenceUtils.getInstance(context).setApkId(Long.toString(id));
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWifiOnly(boolean wifiOnly) {
        this.wifiOnly = wifiOnly;
    }

    public static class Builder {
        private String title;
        private String description;
        private boolean wifiOnly;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setWifiOnly(boolean wifiOnly) {
            this.wifiOnly = wifiOnly;
            return this;
        }

        public UpdateManager create(Context context) {
            UpdateManager manager = new UpdateManager(context);
            if (!TextUtils.isEmpty(title)) {
                manager.setTitle(title);
            }

            if (!TextUtils.isEmpty(description)) {
                manager.setDescription(description);
            }

            manager.setWifiOnly(wifiOnly);

            return manager;
        }
    }

}
