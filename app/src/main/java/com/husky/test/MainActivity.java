package com.husky.test;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.husky.library.BannerView;
import com.husky.library.CustomDialog;
import com.husky.library.SpecialToast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> pics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pics = new ArrayList<>();
        pics.add("http://file27.mafengwo.net/M00/B2/1A/wKgB6lO0alyAJXh6AAVhjQksfsQ81.rbook_comment.w600_h400.jpeg");
        pics.add("http://file27.mafengwo.net/M00/52/F2/wKgB6lO_PTyAKKPBACID2dURuk410.jpeg");
        pics.add("http://s3.lvjs.com.cn/trip/original/20140818131519_1500748202.jpg");

        BannerView bannerView = (BannerView) findViewById(R.id.banner);
        bannerView.setData(pics, new BannerView.OnLoadImageListener<String>() {
            @Override
            public void onLoad(ImageView iv, String o) {
                Glide.with(MainActivity.this).load(o).into(iv);
            }
        });

        bannerView.setOnBannerClickListener(new BannerView.OnBannerClickListener<String>() {
            @Override
            public void onClick(View view, int position, String o) {
                if (position == 0){
                    new CustomDialog.Builder(MainActivity.this).setTitle("提示").setMessage(o).setPositiveButton("确定", R.drawable.pos_bg, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).setNegativeButton("取消", R.drawable.neg_bg, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();
                }else if (position == 1){
                    SpecialToast.make(MainActivity.this,SpecialToast.TYPE_SUCCESS,o,SpecialToast.LENGTH_SHORT).show();
                }else if (position == 2){
                    SpecialToast.make(MainActivity.this,SpecialToast.TYPE_ERROR,o,SpecialToast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
