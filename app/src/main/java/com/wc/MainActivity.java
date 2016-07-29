package com.wc;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wc.widgets.BannerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BannerView bannerView;
    private ImageView iv;
    private List<String> ds ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ds = new ArrayList<>();
//        ds.add("http://img5.imgtn.bdimg.com/it/u=771704229,1948185026&fm=21&gp=0.jpg");
        ds.add("http://img2.imgtn.bdimg.com/it/u=490192972,1718629516&fm=21&gp=0.jpg");
        ds.add("http://pic18.nipic.com/20111221/8277927_142720680000_2.jpg");
        ds.add("http://www.pptbz.com/pptpic/UploadFiles_6909/201110/20111014111307895.jpg");

        bannerView = (BannerView) findViewById(R.id.banner);
        bannerView.setData(ds, new BannerView.OnLoadImageListener<String>() {
            @Override
            public void onLoad(ImageView iv, String o) {
                Glide.with(MainActivity.this).load(o).into(iv);
            }
        });
        bannerView.setOnBannerClickListener(new BannerView.OnBannerClickListener<String>() {
            @Override
            public void onClick(View view, int position, String o) {
                System.out.println(o);
                Glide.with(MainActivity.this).load(o).into(iv);
            }
        });

        iv = (ImageView) findViewById(R.id.image);
        Glide.with(this).load("http://pic18.nipic.com/20111221/8277927_142720680000_2.jpg").into(iv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
