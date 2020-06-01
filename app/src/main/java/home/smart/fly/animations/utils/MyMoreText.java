package home.smart.fly.animations.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import home.smart.fly.animations.R;

/**
 * @author zhuyongqing @ Zhihu Inc.
 * @since 04-07-2020
 */
public class MyMoreText implements View.OnClickListener {
    private static final String TAG = "MyMoreText";
    private SpannableString foldString;    //收起的文字
    private SpannableString expandString; //展开的文字
    private TextView textView;

    private static class MoreTextHolder {
        @SuppressLint("StaticFieldLeak")
        private static final MyMoreText INSTANCE = new MyMoreText();
    }

    public static MyMoreText getInstance() {
        return MyMoreText.MoreTextHolder.INSTANCE;
    }

    public void getLastIndexForLimit(TextView tv, int maxLine, String content) {
        Context context = tv.getContext();
        this.textView = tv;
        //获取TextView的画笔对象
        TextPaint paint = textView.getPaint();
        //每行文本的布局宽度
        int width = context.getResources().getDisplayMetrics().widthPixels - dip2px(context, 40);
        //实例化StaticLayout 传入相应参数
        StaticLayout staticLayout = new StaticLayout(content, paint, width, Layout.Alignment.ALIGN_NORMAL,
                1, 0, false);
        //判断content是行数是否超过最大限制行数3行
        if (staticLayout.getLineCount() > maxLine) {
            //定义展开后的文本内容
            String string1 = content + "    收起";
            expandString = new SpannableString(string1);
            //给收起两个字设成蓝色
//            notEclipseString.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")),
//                    string1.length() - 2, string1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_expand_less_black_24dp);
            if (drawable != null) {
                Log.e(TAG, "getLastIndexForLimit: w " + drawable.getIntrinsicWidth());
                Log.e(TAG, "getLastIndexForLimit: h " + drawable.getIntrinsicHeight());
                Log.e(TAG, "24dp                :   " + ActivityExtKt.getDp(24));
                string1 = string1 + "v";
                expandString = new SpannableString(string1);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2);
                ImageSpan imageSpan = new ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BASELINE);
                drawable.setTintMode(PorterDuff.Mode.SRC_OUT);
                drawable.setTint(Color.RED);
                expandString.setSpan(imageSpan, string1.length() - 1, string1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            //获取到第三行最后一个文字的下标
            int index = staticLayout.getLineStart(maxLine) - 1;
            //定义收起后的文本内容
            String substring = content.substring(0, index - 1) + "..." + "∨";
            foldString = new SpannableString(substring);
            drawable = ContextCompat.getDrawable(context, R.drawable.ic_expand_more_black_24dp);
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2);
                drawable.setTintMode(PorterDuff.Mode.SRC_OUT);
                drawable.setTint(Color.RED);
                ImageSpan imageSpan = new ImageSpan(drawable, DynamicDrawableSpan.ALIGN_CENTER);
                foldString.setSpan(imageSpan, substring.length() - 1, substring.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            //给查看全部设成蓝色
//            eclipseString.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), substring.length() - 1, substring.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //设置收起后的文本内容
            tv.setText(foldString);
            tv.setOnClickListener(this);
            //将textView设成选中状态 true用来表示文本未展示完全的状态,false表示完全展示状态，用于点击时的判断
            tv.setSelected(true);
        } else {
            //没有超过 直接设置文本
            tv.setText(content);
            tv.setOnClickListener(null);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private static int dip2px(Context mContext, float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onClick(View view) {
        if (view.isSelected()) {
            //如果是收起的状态
            textView.setText(expandString);
            textView.setSelected(false);
        } else {
            //如果是展开的状态
            textView.setText(foldString);
            textView.setSelected(true);
        }
    }
}
