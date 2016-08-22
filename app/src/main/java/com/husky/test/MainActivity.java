package com.husky.test;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.husky.library.BannerView;
import com.husky.library.CustomDialog;
import com.husky.library.SpecialToast;
import com.husky.library.photo.photo.widget.PickConfig;
import com.husky.library.rv.GridRecyclerViewDivider;
import com.husky.library.rv.HeaderAndFooterWrapperAdapter;
import com.husky.library.tools.SizeUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> pics;
    private List<String> contents = new ArrayList<>();
    private HeaderAndFooterWrapperAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));

        pics = new ArrayList<>();
        pics.add("http://file27.mafengwo.net/M00/B2/1A/wKgB6lO0alyAJXh6AAVhjQksfsQ81.rbook_comment.w600_h400.jpeg");
        pics.add("http://file27.mafengwo.net/M00/52/F2/wKgB6lO_PTyAKKPBACID2dURuk410.jpeg");
        pics.add("http://s3.lvjs.com.cn/trip/original/20140818131519_1500748202.jpg");

        for (int i = 0; i < 30; i++) {
            contents.add(String.valueOf(i));
        }

//        BannerView bannerView = (BannerView) LayoutInflater.from(this).inflate(R.layout.header,null).findViewById(R.id.banner);
        BannerView bannerView = new BannerView(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(this, 200));
        bannerView.setLayoutParams(lp);
        bannerView.setData(pics, new BannerView.OnLoadImageListener<String>() {
            @Override
            public void onLoad(ImageView iv, String o) {
                Glide.with(MainActivity.this).load(o).into(iv);
            }
        });

        bannerView.setOnBannerClickListener(new BannerView.OnBannerClickListener<String>() {
            @Override
            public void onClick(View view, int position, String o) {
                if (position == 0) {
                    new CustomDialog.Builder(MainActivity.this)
                            .setTitle("提示")
                            .setMessage(o)
                            .setPositiveButton("确定", R.drawable.pos_bg, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    startActivity(new Intent(MainActivity.this, UpdateActivity.class));
                                }
                            })
                            .setNegativeButton("取消", R.drawable.neg_bg, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .create()
                            .show();
                } else if (position == 1) {
                    SpecialToast.make(MainActivity.this, SpecialToast.TYPE_SUCCESS, o, SpecialToast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, CountDownActivity.class));
                } else if (position == 2) {
                    SpecialToast.make(MainActivity.this, SpecialToast.TYPE_ERROR, o, SpecialToast.LENGTH_SHORT).show();
                    new PickConfig.Builder(MainActivity.this)
                            .actionBarcolor(getResources().getColor(R.color.colorPrimary))
                            .statusBarcolor(getResources().getColor(R.color.colorPrimaryDark))
                            .isneedcamera(true)
                            .isneedcrop(true)
                            .isSqureCrop(true).spanCount(3).pickMode(PickConfig.MODE_SINGLE_PICK).build();
                }
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new GridRecyclerViewDivider(this));
        adapter = new HeaderAndFooterWrapperAdapter(new SimpleAdapter());
        adapter.addHeaderView(bannerView);
        recyclerView.setAdapter(adapter);
    }

    private void refresh() {
        for (int i = 'a'; i < 'z'; i++) {
            contents.add(String.valueOf((char) i));
        }
//        adapter.refreshContent();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        refresh();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PickConfig.PICK_REQUEST_CODE) {
            ArrayList<String> paths = data.getStringArrayListExtra("data");
            SpecialToast.make(this, SpecialToast.TYPE_SUCCESS, paths.toString(), SpecialToast.LENGTH_SHORT).show();
        }
    }

    class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.Holder> {

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.textView.setText(contents.get(position));
        }

        @Override
        public int getItemCount() {
            return contents.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            private TextView textView;

            public Holder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.item_tv);
            }
        }
    }

}
