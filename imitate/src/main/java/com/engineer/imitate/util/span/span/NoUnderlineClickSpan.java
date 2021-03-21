package com.engineer.imitate.util.span.span;


import android.text.TextPaint;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;

import androidx.annotation.NonNull;

/**
 * 无下划线可点击的文本，参阅{@link android.text.SpannableStringBuilder}，
 * 需要调用{@link android.widget.TextView#setMovementMethod(MovementMethod)}点击事件才生效。
 * <p>
 * Created by liyujiang on 2017/8/15 11:09.
 */
public abstract class NoUnderlineClickSpan extends ClickableSpan {

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        ds.setUnderlineText(false);
    }

}