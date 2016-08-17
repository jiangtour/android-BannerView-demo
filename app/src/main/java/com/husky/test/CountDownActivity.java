package com.husky.test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.husky.library.tools.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CountDownActivity extends AppCompatActivity {

    private RecyclerView rv;
    private List<TimeBean> times;

    private static final long time = 1000 * 60 * 3;
    private CountDownAdapter adapter;
    private String[] pics = {"http://file27.mafengwo.net/M00/B2/1A/wKgB6lO0alyAJXh6AAVhjQksfsQ81.rbook_comment.w600_h400.jpeg",
            "http://file27.mafengwo.net/M00/52/F2/wKgB6lO_PTyAKKPBACID2dURuk410.jpeg",
            "http://s3.lvjs.com.cn/trip/original/20140818131519_1500748202.jpg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.notifyDataSetChanged();
            }
        });

        initData();
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new CountDownAdapter();
        rv.setAdapter(adapter);
        adapter.start();

    }

    private void initData() {
        times = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            TimeBean bean = new TimeBean();
            long l = time * i / 2;
            bean.setTime(l);
            bean.setUrl(pics[i % pics.length]);
            times.add(bean);
        }
    }

    class CountDownAdapter extends RecyclerView.Adapter<CountDownAdapter.Holder> {

        private Handler handler = new Handler() {
            boolean isNeedCountDown = false;

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                for (int i = 0; i < times.size(); i++) {
                    TimeBean timeBean = times.get(i);
                    long time = timeBean.getTime();
                    if (time > 0) {
                        isNeedCountDown = true;
                        timeBean.setTime(time - 64);
                    } else {
                        timeBean.setTime(0L);
                    }
                }
                notifyDataSetChanged();
                if (isNeedCountDown) {
                    handler.sendEmptyMessageDelayed(0, 64);
                }
            }
        };

        public CountDownAdapter() {

        }

        public void start() {
            handler.sendEmptyMessage(0);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            long ll = times.get(position).getTime();
            holder.textView.setText(TimeUtils.getDate(ll, "mm:ss:SSS"));
            Glide.with(CountDownActivity.this).load(times.get(position).getUrl()).into(holder.iv);
        }

        @Override
        public int getItemCount() {
            return times.size();
        }

        class Holder extends RecyclerView.ViewHolder {

            private TextView textView;
            private ImageView iv;

            public Holder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.text);
                iv = (ImageView) itemView.findViewById(R.id.iv);
            }
        }
    }

}
