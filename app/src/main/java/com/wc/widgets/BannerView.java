package com.wc.widgets;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author husky
 */
public class BannerView<T> extends RelativeLayout implements ViewPager.OnPageChangeListener {

    private int selectedIndicator;
    private int normalIndicator;

    private ViewPager pager;
    private LinearLayout layout;

    private List<T> datas;
    private List<ImageView> ivs;
    private List<ImageView> dots;

    private Context context;
    private OnBannerClickListener listener;
    private int count;
    private boolean isAutoPlay = true;
    private int delayTime = 3000;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isAutoPlay) {
                int current = pager.getCurrentItem();
                if (current == 0) {
                    pager.setCurrentItem(datas.size());
                }else if (current == ivs.size() - 1){
                    pager.setCurrentItem(1);
                }else {
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                }
                sendEmptyMessageDelayed(0, delayTime);
            }
        }
    };


    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_layout, this);

        pager = (ViewPager) view.findViewById(R.id.pager_banner);
        layout = (LinearLayout) view.findViewById(R.id.banner_indicator);

        ivs = new ArrayList<>();
        dots = new ArrayList<>();

        selectedIndicator = R.drawable.dot_focused;
        normalIndicator = R.drawable.dot_normal;
    }

    public void setData(List<T> ts, OnLoadImageListener<T> listener) {
        this.datas = ts;
        count = ts.size();
        createIndicator();
        int len = ts.size() + 2;
        for (int i = 0; i < len; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivs.add(imageView);
            if (listener != null) {
                if (i == 0) {
                    listener.onLoad(imageView, ts.get(ts.size() - 1));
                } else if (i == len - 1) {
                    listener.onLoad(imageView, ts.get(0));
                } else {
                    listener.onLoad(imageView, ts.get(i - 1));
                }
            }
        }
        setData();
    }

    private void createIndicator() {
        dots.clear();
        layout.removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(16, 16);
            lp.leftMargin = 8;
            lp.rightMargin = 8;
            imageView.setLayoutParams(lp);
            layout.addView(imageView);
            dots.add(imageView);
        }
    }

    private void setData() {
        pager.setAdapter(new BannerAdapter());
        pager.addOnPageChangeListener(this);
        pager.setCurrentItem(1);
        startAutoPlay();
    }

    public void setOnBannerClickListener(OnBannerClickListener listener) {
        this.listener = listener;
    }

    public void auutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
    }

    public void startAutoPlay() {
        handler.sendEmptyMessageDelayed(0, delayTime);
    }

    private void setDotResourse(int position){
        for (int i = 0; i < dots.size(); i++) {
            if (i == position) {
                dots.get(i).setBackgroundResource(selectedIndicator);
            } else {
                dots.get(i).setBackgroundResource(normalIndicator);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0){
            setDotResourse(dots.size() - 1);
        }else if (position == ivs.size() - 1){
            setDotResourse(0);
        }else {
            setDotResourse(position - 1);
        }

        int pageIndex = position;

        if (position == 0) {
            pageIndex = datas.size();
        } else if (position == datas.size() + 1) {
            pageIndex = 1;
        }
        if (position != pageIndex) {
            pager.setCurrentItem(pageIndex, false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case 1:
                isAutoPlay = false;
                break;
            case 2:
                isAutoPlay = true;
                break;
            case 0:
                isAutoPlay = true;
                break;
        }
    }

    public class BannerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return ivs.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final ImageView iv = ivs.get(position);
            final T t;
            if (position == 0){
                t = datas.get(datas.size() - 1);
            }else if (position == ivs.size() - 1){
                t = datas.get(0);
            }else {
                t = datas.get(position - 1);
            }
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(iv, position, t);
                    }
                }
            });

            container.addView(iv);
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(ivs.get(position));
        }
    }

    public interface OnLoadImageListener<T> {
        void onLoad(ImageView iv, T t);
    }

    public interface OnBannerClickListener<T> {
        void onClick(View view, int position, T t);
    }
}
