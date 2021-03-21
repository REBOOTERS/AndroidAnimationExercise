package com.engineer.imitate.util.span.toolkit;

import android.graphics.BlurMaskFilter;
import android.graphics.Typeface;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * 多样式文本构建器
 * <p>
 * Created by liyujiang on 2018/9/5 11:40
 */
public class TextSpanBuilder {
    private int defaultValue = 0x12000000;
    private CharSequence text;
    private int flag;
    @ColorInt
    private int backgroundColor;
    @ColorInt
    private int foregroundColor;
    @ColorInt
    private int quoteColor;
    private boolean isLeadingMargin;
    private int firstMargin;
    private int restMargin;
    private boolean isBullet;
    private int gapWidth;
    private int bulletColor;
    private float proportion;
    private float xProportion;
    private boolean isStrikeThrough;
    private boolean isUnderline;
    private boolean isSuperscript;
    private boolean isSubscript;
    private boolean isBold;
    private boolean isItalic;
    private boolean isBoldItalic;
    private int size = 0;
    private String fontFamily;
    private Layout.Alignment align;
    private ClickableSpan clickSpan;
    private String url;
    private boolean isBlur;
    private float blurRadius;
    private BlurMaskFilter.Blur blurStyle;
    private SpannableStringBuilder builder;
    private List<Object> spans = new ArrayList<>();

    public static TextSpanBuilder create(@NonNull CharSequence text) {
        return new TextSpanBuilder(text);
    }

    private TextSpanBuilder(@NonNull CharSequence text) {
        this.text = text;
        flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        foregroundColor = defaultValue;
        backgroundColor = defaultValue;
        quoteColor = defaultValue;
        proportion = -1;
        xProportion = -1;
        builder = new SpannableStringBuilder();
    }

    /**
     * 设置标识
     *
     * @param flag <ul>
     *             <li>{@link Spanned#SPAN_INCLUSIVE_EXCLUSIVE}</li>
     *             <li>{@link Spanned#SPAN_INCLUSIVE_INCLUSIVE}</li>
     *             <li>{@link Spanned#SPAN_EXCLUSIVE_EXCLUSIVE}</li>
     *             <li>{@link Spanned#SPAN_EXCLUSIVE_INCLUSIVE}</li>
     *             </ul>
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder flag(int flag) {
        this.flag = flag;
        return this;
    }

    /**
     * 设置背景色
     *
     * @param color 背景色
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder backgroundColor(@ColorInt int color) {
        this.backgroundColor = color;
        return this;
    }

    /**
     * 设置前景色
     *
     * @param color 前景色
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder foregroundColor(@ColorInt int color) {
        this.foregroundColor = color;
        return this;
    }

    /**
     * 设置引用线的颜色
     *
     * @param color 引用线的颜色
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder quoteColor(@ColorInt int color) {
        this.quoteColor = color;
        return this;
    }

    /**
     * 设置缩进
     *
     * @param first 首行缩进
     * @param rest  剩余行缩进
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder leadingMargin(int first, int rest) {
        this.firstMargin = first;
        this.restMargin = rest;
        isLeadingMargin = true;
        return this;
    }

    /**
     * 设置列表标记
     *
     * @param gap   列表标记和文字间距离
     * @param color 列表标记的颜色
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder bullet(int gap, int color) {
        gapWidth = gap;
        bulletColor = color;
        isBullet = true;
        return this;
    }

    /**
     * 设置字体比例
     *
     * @param proportion 比例
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder proportion(float proportion) {
        this.proportion = proportion;
        return this;
    }

    /**
     * 设置字体横向比例
     *
     * @param proportion 比例
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder xProportion(float proportion) {
        this.xProportion = proportion;
        return this;
    }

    /**
     * 设置删除线
     *
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder strikeThrough() {
        this.isStrikeThrough = true;
        return this;
    }

    /**
     * 设置下划线
     *
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder underline() {
        this.isUnderline = true;
        return this;
    }

    /**
     * 设置上标
     *
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder superscript() {
        this.isSuperscript = true;
        return this;
    }

    /**
     * 设置下标
     *
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder subscript() {
        this.isSubscript = true;
        return this;
    }

    /**
     * 设置粗体
     *
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder bold() {
        isBold = true;
        return this;
    }

    /**
     * 设置斜体
     *
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder italic() {
        isItalic = true;
        return this;
    }

    /**
     * 设置粗斜体
     *
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder boldItalic() {
        isBoldItalic = true;
        return this;
    }

    /**
     * 文字大小
     *
     * @param size 大小，单位为px
     */
    public TextSpanBuilder sizeInPx(int size) {
        this.size = size;
        return this;
    }

    /**
     * 设置字体
     *
     * @param fontFamily 字体
     *                   <ul>
     *                   <li>monospace</li>
     *                   <li>serif</li>
     *                   <li>sans-serif</li>
     *                   </ul>
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder fontFamily(@Nullable String fontFamily) {
        this.fontFamily = fontFamily;
        return this;
    }

    /**
     * 设置对齐
     *
     * @param align 对其方式
     *              <ul>
     *              <li>{@link Layout.Alignment#ALIGN_NORMAL}正常</li>
     *              <li>{@link Layout.Alignment#ALIGN_OPPOSITE}相反</li>
     *              <li>{@link Layout.Alignment#ALIGN_CENTER}居中</li>
     *              </ul>
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder align(@Nullable Layout.Alignment align) {
        this.align = align;
        return this;
    }

    /**
     * 设置点击事件
     * <p>需添加view.setMovementMethod(LinkMovementMethod.getInstance())</p>
     *
     * @param clickSpan 点击事件
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder click(@NonNull ClickableSpan clickSpan) {
        this.clickSpan = clickSpan;
        return this;
    }

    /**
     * 设置超链接
     * <p>需添加view.setMovementMethod(LinkMovementMethod.getInstance())</p>
     *
     * @param url 超链接
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder url(@NonNull String url) {
        this.url = url;
        return this;
    }

    /**
     * 设置模糊
     * <p>尚存bug，其他地方存在相同的字体的话，相同字体出现在之前的话那么就不会模糊，出现在之后的话那会一起模糊</p>
     * <p>推荐还是把所有字体都模糊这样使用</p>
     *
     * @param radius 模糊半径（需大于0）
     * @param style  模糊样式<ul>
     *               <li>{@link  BlurMaskFilter.Blur#NORMAL}</li>
     *               <li>{@link  BlurMaskFilter.Blur#SOLID}</li>
     *               <li>{@link  BlurMaskFilter.Blur#OUTER}</li>
     *               <li>{@link  BlurMaskFilter.Blur#INNER}</li>
     *               </ul>
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder blur(float radius, BlurMaskFilter.Blur style) {
        this.blurRadius = radius;
        this.blurStyle = style;
        this.isBlur = true;
        return this;
    }

    public TextSpanBuilder span(Object span) {
        spans.add(span);
        return this;
    }

    /**
     * 追加样式字符串
     *
     * @param text 样式字符串文本
     * @return {@link TextSpanBuilder}
     */
    public TextSpanBuilder append(@NonNull CharSequence text) {
        buildSpan();
        this.text = text;
        return this;
    }

    /**
     * 创建样式字符串
     *
     * @return 样式字符串
     */
    public SpannableStringBuilder build() {
        buildSpan();
        return builder;
    }

    /**
     * 设置样式
     */
    private void buildSpan() {
        int start = builder.length();
        builder.append(text);
        int end = builder.length();
        if (backgroundColor != defaultValue) {
            builder.setSpan(new BackgroundColorSpan(backgroundColor), start, end, flag);
            backgroundColor = defaultValue;
        }
        if (foregroundColor != defaultValue) {
            builder.setSpan(new ForegroundColorSpan(foregroundColor), start, end, flag);
            foregroundColor = defaultValue;
        }
        if (isLeadingMargin) {
            builder.setSpan(new LeadingMarginSpan.Standard(firstMargin, restMargin), start, end, flag);
            isLeadingMargin = false;
        }
        if (quoteColor != defaultValue) {
            builder.setSpan(new QuoteSpan(quoteColor), start, end, 0);
            quoteColor = defaultValue;
        }
        if (isBullet) {
            builder.setSpan(new BulletSpan(gapWidth, bulletColor), start, end, 0);
            isBullet = false;
        }
        if (proportion != -1) {
            builder.setSpan(new RelativeSizeSpan(proportion), start, end, flag);
            proportion = -1;
        }
        if (xProportion != -1) {
            builder.setSpan(new ScaleXSpan(xProportion), start, end, flag);
            xProportion = -1;
        }
        if (isStrikeThrough) {
            builder.setSpan(new StrikethroughSpan(), start, end, flag);
            isStrikeThrough = false;
        }
        if (isUnderline) {
            builder.setSpan(new UnderlineSpan(), start, end, flag);
            isUnderline = false;
        }
        if (isSuperscript) {
            builder.setSpan(new SuperscriptSpan(), start, end, flag);
            isSuperscript = false;
        }
        if (isSubscript) {
            builder.setSpan(new SubscriptSpan(), start, end, flag);
            isSubscript = false;
        }
        if (isBold) {
            builder.setSpan(new StyleSpan(Typeface.BOLD), start, end, flag);
            isBold = false;
        }
        if (isItalic) {
            builder.setSpan(new StyleSpan(Typeface.ITALIC), start, end, flag);
            isItalic = false;
        }
        if (isBoldItalic) {
            builder.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), start, end, flag);
            isBoldItalic = false;
        }
        if (size > 0) {
            builder.setSpan(new AbsoluteSizeSpan(size), start, end, flag);
            size = 0;
        }
        if (fontFamily != null) {
            builder.setSpan(new TypefaceSpan(fontFamily), start, end, flag);
            fontFamily = null;
        }
        if (align != null) {
            builder.setSpan(new AlignmentSpan.Standard(align), start, end, flag);
            align = null;
        }
        if (clickSpan != null) {
            builder.setSpan(clickSpan, start, end, flag);
            clickSpan = null;
        }
        if (url != null) {
            builder.setSpan(new URLSpan(url), start, end, flag);
            url = null;
        }
        if (isBlur) {
            builder.setSpan(new MaskFilterSpan(new BlurMaskFilter(blurRadius, blurStyle)), start, end, flag);
            isBlur = false;
        }
        if (spans.size() > 0) {
            for (Object span : spans) {
                builder.setSpan(span, start, end, flag);
            }
            spans.clear();
        }
        flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
    }

}
