package com.husky.library;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author husky
 */
public class CustomDialog extends Dialog {
    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private int positiveButtonBg;
        private int negativeButtonBg;
        private int positiveTextColor;
        private int negativeTextColor;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message message
         * @return builder
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title title
         * @return builder
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText 按钮文字
         * @param btnBg 按钮背景 res id
         * @param textColor 按钮文字颜色
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText, int btnBg, int textColor, OnClickListener listener) {
            this.positiveButtonText = (String) context.getText(positiveButtonText);
            this.positiveTextColor = textColor;
            this.positiveButtonBg = btnBg;
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText 按钮文字
         * @param btnBg 按钮背景 res id
         * @param textColor 按钮文字颜色
         * @return
         */
        public Builder setPositiveButton(String positiveButtonText, int btnBg, int textColor, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveTextColor = textColor;
            this.positiveButtonBg = btnBg;
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * Set the negative button resource and it's listener
         *
         * @param negativeButtonText 按钮文字
         * @param btnBg 按钮背景 res id
         * @param textColor 按钮文字颜色
         * @return
         */
        public Builder setNegativeButton(int negativeButtonText, int btnBg, int textColor, OnClickListener listener) {
            this.negativeButtonText = (String) context.getText(negativeButtonText);
            this.negativeButtonBg = btnBg;
            this.negativeTextColor = textColor;
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * Set the negative button resource and it's listener
         *
         * @param negativeButtonText 按钮文字
         * @param btnBg 按钮背景 res id
         * @param textColor 按钮文字颜色
         * @return
         */
        public Builder setNegativeButton(String negativeButtonText, int btnBg, int textColor, OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonBg = btnBg;
            this.negativeTextColor = textColor;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context, R.style.CustomDialog);
            View layout = inflater.inflate(R.layout.message_dialog, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            // set the dialog title
            if (!TextUtils.isEmpty(title)) {
                ((TextView) layout.findViewById(R.id.dialog_title)).setText(title);
            } else {
                layout.findViewById(R.id.dialog_title).setVisibility(View.GONE);
            }
            // set the confirm button
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.dialog_btn_positive)).setText(positiveButtonText);
                if (positiveButtonBg != 0) {
                    layout.findViewById(R.id.dialog_btn_positive).setBackgroundResource(positiveButtonBg);
                }
                if (positiveTextColor != 0){
                    ((Button) layout.findViewById(R.id.dialog_btn_positive)).setTextColor(positiveTextColor);
                }
                if (positiveButtonClickListener != null) {
                    layout.findViewById(R.id.dialog_btn_positive)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.dialog_btn_positive).setVisibility(View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.dialog_btn_negative)).setText(negativeButtonText);
                if (negativeButtonBg != 0) {
                    layout.findViewById(R.id.dialog_btn_negative).setBackgroundResource(negativeButtonBg);
                }
                if (negativeTextColor != 0){
                    ((Button) layout.findViewById(R.id.dialog_btn_negative)).setTextColor(negativeTextColor);
                }
                if (negativeButtonClickListener != null) {
                    layout.findViewById(R.id.dialog_btn_negative)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.dialog_btn_negative).setVisibility(View.GONE);
            }
            // set the content message
            if (message != null) {
                ((TextView) layout.findViewById(R.id.dialog_content)).setText(message);
            } else {
                if (contentView != null) {
                    // if no message set
                    // add the contentView to the dialog body
                    ((LinearLayout) layout.findViewById(R.id.dialog_content_ll))
                            .removeAllViews();
                    ((LinearLayout) layout.findViewById(R.id.dialog_content_ll))
                            .addView(contentView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                } else {
                    layout.findViewById(R.id.dialog_content_ll).setVisibility(View.GONE);
                }
            }
            dialog.setContentView(layout);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = context.getResources().getDimensionPixelOffset(R.dimen.dialog_width);
            window.setAttributes(lp);
            return dialog;
        }
    }
}
