package com.husky.library;

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
                } else if (current == ivs.size() - 1) {
                    pager.setCurrentItem(1);
                } else {
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                }
                sendEmptyMessageDelayed(0, delayTime);
            }
        }
    };


    public BannerView(Context context) {
        this(context, null);//调用两个参数的构造函数
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);//调用三个参数的的构造函数
    }

    //最终都会调用这个构造函数
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

    /**
     * 填充数据
     *
     * @param ts       数据源
     * @param listener 回调接口 用来显示banner图片
     */
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

    /**
     * 自动播放
     *
     * @param isAutoPlay
     */
    public void autoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
    }

    public void startAutoPlay() {
        handler.sendEmptyMessageDelayed(0, delayTime);
    }

    private void setDotResource(int position) {
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
        if (position == 0) {//如果是第0页 设置圆点为最后一个
            setDotResource(dots.size() - 1);
        } else if (position == ivs.size() - 1) {//如果是最后一页,设置圆点为第0个
            setDotResource(0);
        } else {
            setDotResource(position - 1);
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
            final int pos;
            if (position == 0) {//当第一页时  真实数据是datas的最后一个
                t = datas.get(datas.size() - 1);
                pos = datas.size() - 1;
            } else if (position == ivs.size() - 1) {//当最后一页时，真实数据是datas的第一个
                t = datas.get(0);
                pos = 0;
            } else {//其他情况：真实数据是data.get(当前页码 - 1)
                t = datas.get(position - 1);
                pos = position - 1;
            }
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(iv, pos, t);
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

    /**
     * 图片显示回调接口
     * 用于给每页的image view 设置对应的图片
     *
     * @param <T>
     */
    public interface OnLoadImageListener<T> {
        /**
         * @param iv 当前的页面image view
         * @param t  当前对应的数据
         */
        void onLoad(ImageView iv, T t);
    }

    /**
     * banner点击事件回调接口
     */
    public interface OnBannerClickListener<T> {
        /**
         * banner图片点击事件
         *
         * @param view     当前点击的图片
         * @param position 当前点击的真实位置(对应datas)
         * @param t        当前的真实数据
         */
        void onClick(View view, int position, T t);
    }
}
