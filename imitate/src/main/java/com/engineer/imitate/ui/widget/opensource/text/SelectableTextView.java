package com.engineer.imitate.ui.widget.opensource.text;

/**
 * @author Rookie
 * @since 06-30-2020
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Vibrator;
import android.text.Layout;
import android.text.Selection;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.engineer.imitate.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * SelectableTextView ————增强版的TextView，具有以下功能：
 * <p> 1：长按文字弹出ActionMenu菜单；菜单menu可以自定义；实现自定义功能（复制，全选，翻译，分享等；默认实现了全选和复制功能）
 * <p> 2：文本两端对齐功能；适用于中文文本，英文文本 以及中英混合文本
 * Created by zengpu on 2016/11/20.
 */
public class SelectableTextView extends EditText {

    private final int TRIGGER_LONGPRESS_TIME_THRESHOLD = 300;    // 触发长按事件的时间阈值
    private final int TRIGGER_LONGPRESS_DISTANCE_THRESHOLD = 10; // 触发长按事件的位移阈值

    private Context mContext;
    private int mScreenHeight;      // 屏幕高度
    private int mStatusBarHeight;   // 状态栏高度
    private int mActionMenuHeight;  // 弹出菜单高度
    private int mTextHighlightColor;// 选中文字背景高亮颜色

    private float mTouchDownX = 0;
    private float mTouchDownY = 0;
    private float mTouchDownRawY = 0;

    private boolean isLongPress = false;               // 是否发触了长按事件
    private boolean isLongPressTouchActionUp = false;  // 长按事件结束后，标记该次事件
    private boolean isVibrator = false;                // 是否触发过长按震动

    private boolean isTextJustify = true;              // 是否需要两端对齐 ，默认true
    private boolean isForbiddenActionMenu = false;     // 是否需要两端对齐 ，默认false

    private boolean isActionSelectAll = false;         // 是否触发全选事件

    private int mStartLine;             //action_down触摸事件 起始行
    private int mStartTextOffset;       //action_down触摸事件 字符串开始位置的偏移值
    private int mCurrentLine;           // action_move触摸事件 当前行
    private int mCurrentTextOffset;     //action_move触摸事件 字符串当前位置的偏移值

    private int mViewTextWidth;         // SelectableTextView内容的宽度(不包含padding)

    private Vibrator mVibrator;
    private PopupWindow mActionMenuPopupWindow; // 长按弹出菜单
    private ActionMenu mActionMenu = null;

    private OnClickListener mOnClickListener;
    private CustomActionMenuCallBack mCustomActionMenuCallBack;

    public SelectableTextView(Context context) {
        this(context, null);
    }

    public SelectableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.SelectableTextView);
        isTextJustify = mTypedArray.getBoolean(R.styleable.SelectableTextView_textJustify, true);
        isForbiddenActionMenu = mTypedArray.getBoolean(R.styleable.SelectableTextView_forbiddenActionMenu, false);
        mTextHighlightColor = mTypedArray.getColor(R.styleable.SelectableTextView_textHeightColor, 0x60ffeb3b);
        mTypedArray.recycle();

        init();
    }

    private void init() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mScreenHeight = wm.getDefaultDisplay().getHeight();
        mStatusBarHeight = Utils.getStatusBarHeight(mContext);
        mActionMenuHeight = Utils.dp2px(mContext, 45);

        mVibrator = (Vibrator) mContext.getSystemService(VIBRATOR_SERVICE);

        if (isTextJustify)
            setGravity(Gravity.TOP);

        setTextIsSelectable(true);
        setCursorVisible(false);

        setTextHighlightColor(mTextHighlightColor);
    }

    @Override
    public boolean getDefaultEditable() {
        // 返回false，屏蔽掉系统自带的ActionMenu
        return false;
    }

    public void setTextJustify(boolean textJustify) {
        isTextJustify = textJustify;
    }

    public void setForbiddenActionMenu(boolean forbiddenActionMenu) {
        isForbiddenActionMenu = forbiddenActionMenu;
    }

    public void setTextHighlightColor(int color) {
        this.mTextHighlightColor = color;
        String color_hex = String.format("%08X", color);
        color_hex = "#40" + color_hex.substring(2);
        setHighlightColor(Color.parseColor(color_hex));
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        if (null != l) {
            mOnClickListener = l;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Layout layout = getLayout();
        int currentLine; // 当前所在行

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.d("SelectableTextView", "ACTION_DOWN");

                // 每次按下时，创建ActionMenu菜单，创建不成功，屏蔽长按事件
                if (null == mActionMenu) {
                    mActionMenu = createActionMenu();
                }
                mTouchDownX = event.getX();
                mTouchDownY = event.getY();
                mTouchDownRawY = event.getRawY();
                isLongPress = false;
                isVibrator = false;
                isLongPressTouchActionUp = false;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("SelectableTextView", "ACTION_MOVE");
                // 先判断是否禁用了ActionMenu功能，以及ActionMenu是否创建失败，
                // 二者只要满足了一个条件，退出长按事件
                if (!isForbiddenActionMenu || mActionMenu.getChildCount() == 0) {
                    // 手指移动过程中的字符偏移
                    currentLine = layout.getLineForVertical(getScrollY() + (int) event.getY());
                    int mWordOffset_move = layout.getOffsetForHorizontal(currentLine, (int) event.getX());
                    // 判断是否触发长按事件
                    if (event.getEventTime() - event.getDownTime() >= TRIGGER_LONGPRESS_TIME_THRESHOLD
                            && Math.abs(event.getX() - mTouchDownX) < TRIGGER_LONGPRESS_DISTANCE_THRESHOLD
                            && Math.abs(event.getY() - mTouchDownY) < TRIGGER_LONGPRESS_DISTANCE_THRESHOLD) {

                        Log.d("SelectableTextView", "ACTION_MOVE 长按");
                        isLongPress = true;
                        isLongPressTouchActionUp = false;
                        mStartLine = currentLine;
                        mStartTextOffset = mWordOffset_move;

                        // 每次触发长按时，震动提示一次
                        if (!isVibrator) {
                            mVibrator.vibrate(30);
                            isVibrator = true;
                        }
                    }
                    if (isLongPress) {

                        if (!isTextJustify)
                            requestFocus();
                        mCurrentLine = currentLine;
                        mCurrentTextOffset = mWordOffset_move;
                        // 通知父布局不要拦截触摸事件
                        getParent().requestDisallowInterceptTouchEvent(true);
                        // 选择字符
                        Selection.setSelection(getEditableText(), Math.min(mStartTextOffset, mWordOffset_move),
                                Math.max(mStartTextOffset, mWordOffset_move));
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d("SelectableTextView", "ACTION_UP");
                // 处理长按事件
                if (isLongPress) {
                    currentLine = layout.getLineForVertical(getScrollY() + (int) event.getY());
                    int mWordOffsetEnd = layout.getOffsetForHorizontal(currentLine, (int) event.getX());
                    // 至少选中一个字符
                    mCurrentLine = currentLine;
                    mCurrentTextOffset = mWordOffsetEnd;
                    int maxOffset = getEditableText().length() - 1;
                    if (mStartTextOffset > maxOffset)
                        mStartTextOffset = maxOffset;
                    if (mCurrentTextOffset > maxOffset)
                        mCurrentTextOffset = maxOffset;
                    if (mCurrentTextOffset == mStartTextOffset) {
                        if (mCurrentTextOffset == layout.getLineEnd(currentLine) - 1)
                            mStartTextOffset -= 1;
                        else
                            mCurrentTextOffset += 1;
                    }


                    Selection.setSelection(getEditableText(), Math.min(mStartTextOffset, mCurrentTextOffset),
                            Math.max(mStartTextOffset, mCurrentTextOffset));
                    // 计算菜单显示位置
                    int mPopWindowOffsetY = calculatorActionMenuYPosition((int) mTouchDownRawY, (int) event.getRawY());
                    // 弹出菜单
                    showActionMenu(mPopWindowOffsetY, mActionMenu);
                    isLongPressTouchActionUp = true;
                    isLongPress = false;

                } else if (event.getEventTime() - event.getDownTime() < TRIGGER_LONGPRESS_TIME_THRESHOLD) {
                    // 由于onTouchEvent最终返回了true,onClick事件会被屏蔽掉，因此在这里处理onClick事件
                    if (null != mOnClickListener)
                        mOnClickListener.onClick(this);
                }
                // 通知父布局继续拦截触摸事件
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return true;
    }

    /* ***************************************************************************************** */
    // 创建ActionMenu部分

    /**
     * 创建ActionMenu菜单
     *
     * @return
     */
    private ActionMenu createActionMenu() {
        // 创建菜单
        ActionMenu actionMenu = new ActionMenu(mContext);
        // 是否需要移除默认item
        boolean isRemoveDefaultItem = false;
        if (null != mCustomActionMenuCallBack) {
            isRemoveDefaultItem = mCustomActionMenuCallBack.onCreateCustomActionMenu(actionMenu);
        }
        if (!isRemoveDefaultItem)
            actionMenu.addDefaultMenuItem(); // 添加默认item

        actionMenu.addCustomItem();  // 添加自定义item
        actionMenu.setFocusable(true); // 获取焦点
        actionMenu.setFocusableInTouchMode(true);

        if (actionMenu.getChildCount() != 0) {
            // item监听
            for (int i = 0; i < actionMenu.getChildCount(); i++) {
                actionMenu.getChildAt(i).setOnClickListener(mMenuClickListener);
            }
        }
        return actionMenu;
    }

    /**
     * 长按弹出菜单
     *
     * @param offsetY
     * @param actionMenu
     * @return 菜单创建成功，返回true
     */
    private void showActionMenu(int offsetY, ActionMenu actionMenu) {

        mActionMenuPopupWindow = new PopupWindow(actionMenu, WindowManager.LayoutParams.WRAP_CONTENT,
                mActionMenuHeight, true);
        mActionMenuPopupWindow.setFocusable(true);
        mActionMenuPopupWindow.setOutsideTouchable(false);
        mActionMenuPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mActionMenuPopupWindow.showAtLocation(this, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, offsetY);

        mActionMenuPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Selection.removeSelection(getEditableText());
                // 如果设置了分散对齐，ActionMenu销毁后，强制刷新一次，防止出现文字背景未消失的情况
                if (isTextJustify)
                    SelectableTextView.this.postInvalidate();
            }
        });
    }

    /**
     * 隐藏菜单
     */
    private void hideActionMenu() {
        if (null != mActionMenuPopupWindow) {
            mActionMenuPopupWindow.dismiss();
            mActionMenuPopupWindow = null;
        }
    }

    /**
     * 菜单点击事件监听
     */
    private OnClickListener mMenuClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            String menuItemTitle = (String) v.getTag();

            // 选中的字符的开始和结束位置
            int start = getSelectionStart();
            int end = getSelectionEnd();
            // 获得选中的字符
            String selected_str;
            if (start < 0 || end < 0 || end <= start) {
                selected_str = "";
            } else
                selected_str = getText().toString().substring(start, end);

            if (menuItemTitle.equals(ActionMenu.DEFAULT_MENU_ITEM_TITLE_SELECT_ALL)) {
                //全选事件
                if (isTextJustify) {
                    mStartLine = 0;
                    mCurrentLine = getLayout().getLineCount() - 1;
                    mStartTextOffset = 0;
                    mCurrentTextOffset = getLayout().getLineEnd(mCurrentLine);
                    isActionSelectAll = true;
                    SelectableTextView.this.invalidate();
                }
                Selection.selectAll(getEditableText());

            } else if (menuItemTitle.equals(ActionMenu.DEFAULT_MENU_ITEM_TITLE_COPY)) {
                // 复制事件
                Utils.copyText(mContext, selected_str);
                Toast.makeText(mContext, "复制成功！", Toast.LENGTH_SHORT).show();
                hideActionMenu();

            } else {
                // 自定义事件
                if (null != mCustomActionMenuCallBack) {
                    mCustomActionMenuCallBack.onCustomActionItemClicked(menuItemTitle, selected_str);
                }
                hideActionMenu();
            }
        }
    };

    /**
     * 计算弹出菜单相对于父布局的Y向偏移
     *
     * @param yOffsetStart 所选字符的起始位置相对屏幕的Y向偏移
     * @param yOffsetEnd   所选字符的结束位置相对屏幕的Y向偏移
     * @return
     */
    private int calculatorActionMenuYPosition(int yOffsetStart, int yOffsetEnd) {
        if (yOffsetStart > yOffsetEnd) {
            int temp = yOffsetStart;
            yOffsetStart = yOffsetEnd;
            yOffsetEnd = temp;
        }
        int actionMenuOffsetY;

        if (yOffsetStart < mActionMenuHeight * 3 / 2 + mStatusBarHeight) {
            if (yOffsetEnd > mScreenHeight - mActionMenuHeight * 3 / 2) {
                // 菜单显示在屏幕中间
                actionMenuOffsetY = mScreenHeight / 2 - mActionMenuHeight / 2;
            } else {
                // 菜单显示所选文字下方
                actionMenuOffsetY = yOffsetEnd + mActionMenuHeight / 2;
            }
        } else {
            // 菜单显示所选文字上方
            actionMenuOffsetY = yOffsetStart - mActionMenuHeight * 3 / 2;
        }
        return actionMenuOffsetY;
    }

    /* ***************************************************************************************** */
    // 两端对齐部分

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("SelectableTextView", "onDraw");
        if (!isTextJustify) {
            // 不需要两端对齐
            super.onDraw(canvas);

        } else {
            //textview内容的实际宽度
            mViewTextWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
            // 重绘文字，两端对齐
            drawTextWithJustify(canvas);
            // 绘制选中文字的背景，触发以下事件时需要绘制背景：
            // 1.长按事件 2.全选事件 3.手指滑动过快时，进入ACTION_UP事件后，
            // 可能会出现背景未绘制的情况
            if (isLongPress | isActionSelectAll | isLongPressTouchActionUp) {
                drawSelectedTextBackground(canvas);
                isActionSelectAll = false;
                isLongPressTouchActionUp = false;
            }
        }
    }

    /**
     * 重绘文字，两端对齐
     *
     * @param canvas
     */
    private void drawTextWithJustify(Canvas canvas) {
        // 文字画笔
        TextPaint textPaint = getPaint();
        textPaint.setColor(getCurrentTextColor());
        textPaint.drawableState = getDrawableState();

        String text_str = getText().toString();
        // 当前所在行的Y向偏移
        int currentLineOffsetY = getPaddingTop();
        currentLineOffsetY += getTextSize();

        Layout layout = getLayout();

        //循环每一行,绘制文字
        for (int i = 0; i < layout.getLineCount(); i++) {
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
            //获取到TextView每行中的内容
            String line_str = text_str.substring(lineStart, lineEnd);
            // 获取每行字符串的宽度(不包括字符间距)
            float desiredWidth = StaticLayout.getDesiredWidth(text_str, lineStart, lineEnd, getPaint());

            if (isLineNeedJustify(line_str)) {
                //最后一行不需要重绘
                if (i == layout.getLineCount() - 1) {
                    canvas.drawText(line_str, getPaddingLeft(), currentLineOffsetY, textPaint);
                } else {
                    drawJustifyTextForLine(canvas, line_str, desiredWidth, currentLineOffsetY);
                }
            } else {
                canvas.drawText(line_str, getPaddingLeft(), currentLineOffsetY, textPaint);
            }
            //更新行Y向偏移
            currentLineOffsetY += getLineHeight();
        }
    }

    /**
     * 绘制选中的文字的背景
     *
     * @param canvas
     */
    private void drawSelectedTextBackground(Canvas canvas) {
        if (mStartTextOffset == mCurrentTextOffset)
            return;

        // 文字背景高亮画笔
        Paint highlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlightPaint.setStyle(Paint.Style.FILL);
        highlightPaint.setColor(mTextHighlightColor);
        highlightPaint.setAlpha(60);

        // 计算开始位置和结束位置的字符相对view最左侧的x偏移
        float startToLeftPosition = calculatorCharPositionToLeft(mStartLine, mStartTextOffset);
        float currentToLeftPosition = calculatorCharPositionToLeft(mCurrentLine, mCurrentTextOffset);

        // 行高
        int h = getLineHeight();
        int paddingTop = getPaddingTop();
        int paddingLeft = getPaddingLeft();

        // 创建三个矩形，分别对应：
        // 所有选中的行对应的矩形，起始行左侧未选中文字的对应的矩形，结束行右侧未选中的文字对应的矩形
        RectF rect_all, rect_lt, rect_rb;
        // sdk版本控制
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mStartTextOffset < mCurrentTextOffset) {
                rect_all = new RectF(paddingLeft, mStartLine * h + paddingTop,
                        mViewTextWidth + paddingLeft, (mCurrentLine + 1) * h + paddingTop);
                rect_lt = new RectF(paddingLeft, mStartLine * h + paddingTop,
                        startToLeftPosition, (mStartLine + 1) * h + paddingTop);
                rect_rb = new RectF(currentToLeftPosition, mCurrentLine * h + paddingTop,
                        mViewTextWidth + paddingLeft, (mCurrentLine + 1) * h + paddingTop);
            } else {
                rect_all = new RectF(paddingLeft, mCurrentLine * h + paddingTop,
                        mViewTextWidth + paddingLeft, (mStartLine + 1) * h + paddingTop);
                rect_lt = new RectF(paddingLeft, mCurrentLine * h + paddingTop,
                        currentToLeftPosition, (mCurrentLine + 1) * h + paddingTop);
                rect_rb = new RectF(startToLeftPosition, mStartLine * h + paddingTop,
                        mViewTextWidth + paddingLeft, (mStartLine + 1) * h + paddingTop);
            }

            // 创建三个路径，分别对应上面三个矩形
            Path path_all = new Path();
            Path path_lt = new Path();
            Path path_rb = new Path();
            path_all.addRect(rect_all, Path.Direction.CCW);
            path_lt.addRect(rect_lt, Path.Direction.CCW);
            path_rb.addRect(rect_rb, Path.Direction.CCW);
            // 将左上角和右下角的矩形从path_all中减去
            path_all.addRect(rect_all, Path.Direction.CCW);
            path_all.op(path_lt, Path.Op.DIFFERENCE);
            path_all.op(path_rb, Path.Op.DIFFERENCE);

            canvas.drawPath(path_all, highlightPaint);

        } else {
            Path path_all = new Path();
            path_all.moveTo(startToLeftPosition, (mStartLine + 1) * h + paddingTop);
            path_all.lineTo(startToLeftPosition, mStartLine * h + paddingTop);
            path_all.lineTo(mViewTextWidth + paddingLeft, mStartLine * h + paddingTop);
            path_all.lineTo(mViewTextWidth + paddingLeft, mCurrentLine * h + paddingTop);
            path_all.lineTo(currentToLeftPosition, mCurrentLine * h + paddingTop);
            path_all.lineTo(currentToLeftPosition, (mCurrentLine + 1) * h + paddingTop);
            path_all.lineTo(paddingLeft, (mCurrentLine + 1) * h + paddingTop);
            path_all.lineTo(paddingLeft, (mStartLine + 1) * h + paddingTop);
            path_all.lineTo(startToLeftPosition, (mStartLine + 1) * h + paddingTop);

            canvas.drawPath(path_all, highlightPaint);
        }
//        canvas.restore();
    }

    /**
     * 重绘此行,两端对齐
     *
     * @param canvas
     * @param line_str           该行所有的文字
     * @param desiredWidth       该行每个文字的宽度的总和
     * @param currentLineOffsetY 该行的Y向偏移
     */
    private void drawJustifyTextForLine(Canvas canvas, String line_str, float desiredWidth, int currentLineOffsetY) {

        // 画笔X方向的偏移
        float lineTextOffsetX = getPaddingLeft();
        // 判断是否是首行
        if (isFirstLineOfParagraph(line_str)) {
            String blanks = "  ";
            // 画出缩进空格
            canvas.drawText(blanks, lineTextOffsetX, currentLineOffsetY, getPaint());
            // 空格需要的宽度
            float blank_width = StaticLayout.getDesiredWidth(blanks, getPaint());
            // 更新画笔X方向的偏移
            lineTextOffsetX += blank_width;
            line_str = line_str.substring(3);
        }

        // 计算相邻字符(或单词)之间需要填充的宽度,英文按单词处理，中文按字符处理
        // (TextView内容的实际宽度 - 该行字符串的宽度)/（字符或单词个数-1）
        if (isContentABC(line_str)) {
            // 该行包含英文,以空格分割单词
            String[] line_words = line_str.split(" ");
            // 计算相邻单词间需要插入的空白
            float insert_blank = mViewTextWidth - desiredWidth;
            if (line_words.length > 1)
                insert_blank = (mViewTextWidth - desiredWidth) / (line_words.length - 1);
            // 遍历单词
            for (int i = 0; i < line_words.length; i++) {
                // 判断分割后的每一个单词；如果是纯英文，按照纯英文单词处理，直接在画布上画出单词；
                // 如果包括汉字，则按照汉字字符处理，逐个字符绘画
                // 如果只有一个单词，按中文处理
                // 最后一个单词按照纯英文单词处理
                String word_i = line_words[i] + " ";
                if (line_words.length == 1 || (isContentHanZi(word_i) && i < line_words.length - 1)) {
                    // 单词按照汉字字符处理
                    // 计算单词中相邻字符间需要插入的空白
                    float insert_blank_word_i = insert_blank;
                    if (word_i.length() > 1)
                        insert_blank_word_i = insert_blank / (word_i.length() - 1);
                    // 遍历单词中字符，依次绘画
                    for (int j = 0; j < word_i.length(); j++) {
                        String word_i_char_j = String.valueOf(word_i.charAt(j));
                        float word_i_char_j_width = StaticLayout.getDesiredWidth(word_i_char_j, getPaint());
                        canvas.drawText(word_i_char_j, lineTextOffsetX, currentLineOffsetY, getPaint());
                        // 更新画笔X方向的偏移
                        lineTextOffsetX += word_i_char_j_width + insert_blank_word_i;
                    }
                } else {
                    //单词按照纯英文处理
                    float word_i_width = StaticLayout.getDesiredWidth(word_i, getPaint());
                    canvas.drawText(word_i, lineTextOffsetX, currentLineOffsetY, getPaint());
                    // 更新画笔X方向的偏移
                    lineTextOffsetX += word_i_width + insert_blank;
                }
            }
        } else {
            // 该行按照中文处理
            float insert_blank = (mViewTextWidth - desiredWidth) / (line_str.length() - 1);
            for (int i = 0; i < line_str.length(); i++) {
                String char_i = String.valueOf(line_str.charAt(i));
                float char_i_width = StaticLayout.getDesiredWidth(char_i, getPaint());
                canvas.drawText(char_i, lineTextOffsetX, currentLineOffsetY, getPaint());
                // 更新画笔X方向的偏移
                lineTextOffsetX += char_i_width + insert_blank;
            }
        }
    }

    /**
     * 计算字符距离控件左侧的位移
     *
     * @param line       字符所在行
     * @param charOffset 字符偏移量
     */
    private float calculatorCharPositionToLeft(int line, int charOffset) {

        String text_str = getText().toString();


        Layout layout = getLayout();
        int lineStart = layout.getLineStart(line);
        int lineEnd = layout.getLineEnd(line);

        String line_str = text_str.substring(lineStart, lineEnd);

        if (line_str.equals("\n"))
            return getPaddingLeft();
        // 最左侧
        if (lineStart == charOffset)
            return getPaddingLeft();
        // 最右侧
        if (charOffset == lineEnd - 1)
            return mViewTextWidth + getPaddingLeft();

        float desiredWidth = StaticLayout.getDesiredWidth(text_str, lineStart, lineEnd, getPaint());

        // 中间位置
        // 计算相邻字符之间需要填充的宽度
        // (TextView内容的实际宽度 - 该行字符串的宽度)/（字符个数-1）
        float insert_blank = (mViewTextWidth - desiredWidth) / (line_str.length() - 1);
        // 计算当前字符左侧所有字符的宽度
        float allLeftCharWidth = StaticLayout.getDesiredWidth(text_str.substring(lineStart, charOffset), getPaint());

        // 相邻字符之间需要填充的宽度 + 当前字符左侧所有字符的宽度
        return insert_blank * (charOffset - lineStart) + allLeftCharWidth + getPaddingLeft();

    }

    /**
     * 判断是不是段落的第一行。一个汉字相当于一个字符，此处判断是否为第一行的依据是：
     * 字符长度大于3且前两个字符为空格
     *
     * @param line
     * @return
     */
    private boolean isFirstLineOfParagraph(String line) {
        return line.length() > 3 && line.charAt(0) == ' ' && line.charAt(1) == ' ';
    }

    /**
     * 判断该行需不需要缩放；该行最后一个字符不是换行符的时候返回true，
     * 该行最后一个字符是换行符的时候返回false
     *
     * @param line_str 该行的文字
     * @return
     */
    private boolean isLineNeedJustify(String line_str) {
        if (line_str.length() == 0) {
            return false;
        } else {
            return line_str.charAt(line_str.length() - 1) != '\n';
        }
    }

    /**
     * 判断是否包含英文
     *
     * @param line_str
     * @return
     */
    private boolean isContentABC(String line_str) {
        String regex = ".*[a-zA-Z]+.*";
        Matcher m = Pattern.compile(regex).matcher(line_str);
        return m.matches();
    }

    /**
     * 判断是否包含中文
     *
     * @param word_str
     * @return
     */
    private boolean isContentHanZi(String word_str) {
//        String E1 = "[\u4e00-\u9fa5]";// 中文
        String regex = ".*[\\u4e00-\\u9fa5]+.*";
        Matcher m = Pattern.compile(regex).matcher(word_str);
        return m.matches();
    }

    /**
     * 判断是否是中文标点符号
     *
     * @param str
     * @return
     */
    private boolean isUnicodeSymbol(String str) {
        String regex = ".*[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]$+.*";
        Matcher m = Pattern.compile(regex).matcher(str);
        return m.matches();
    }

    public void setCustomActionMenuCallBack(CustomActionMenuCallBack callBack) {
        this.mCustomActionMenuCallBack = callBack;
    }
}