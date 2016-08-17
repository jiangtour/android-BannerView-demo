package com.husky.library;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 带状态的Toast 成功 失败 普通三种状态
 * @author husky
 */
public class SpecialToast {

    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_ERROR = 1;
    public static final int TYPE_NONE = 2;

    public static final int LENGTH_LONG = Toast.LENGTH_LONG;
    public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;

    private Toast toast;

    public SpecialToast(Context context){
        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER,0,0);
    }
    public static SpecialToast make(Context context, int type, String content, int duration){
        SpecialToast toast = new SpecialToast(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = view = inflater.inflate(R.layout.special_toast_img,null);
        ImageView imageView = (ImageView)view.findViewById(R.id.toast_img_iv);
        TextView textView = (TextView)view.findViewById(R.id.toast_img_tv);
        switch (type){
            case TYPE_SUCCESS:
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.icon_prompt_correct);
                textView.setText(content);
                break;
            case TYPE_ERROR:
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.icon_prompt_error);
                textView.setText(content);
                break;
            case TYPE_NONE:
                imageView.setVisibility(View.GONE);
                textView.setText(content);
                break;
        }
        toast.setView(view);
        toast.setDuration(duration);
        return toast;
    }

    public void setDuration(int duration){
        toast.setDuration(duration);
    }

    public void setView(View view){
        toast.setView(view);
    }

    public void show(){
        toast.show();
    }
}
