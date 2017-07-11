package home.smart.fly.animations.customview.gallery;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

import home.smart.fly.animations.BuildConfig;
import home.smart.fly.animations.R;

/**
 * @author deadline
 * @time 2016/10/12.
 *
 */
public class ScaleLayout extends FrameLayout {

    private static final String TAG = ScaleLayout.class.getSimpleName();

    /**
     * 可以缩小到的最小比例
     */
    private static final float DEFAULT_MIN_SCALE = 0.7f;

    private static final int DEFAULT_DURATION = 1000;

    public static final int STATE_OPEN = 0;

    public static final int STATE_CLOSE = 1;

    private boolean mSuggestScaleEnable;
    /**
     * 设置是否启用上滑缩小功能
     */
    private boolean mSlideScaleEnable;

    /**
     *  现在有这么几种情况, 默认第二种
     *  1. 只上滑放大下滑缩小  false
     *  2. 只上滑缩小下滑放大  true
     */
    private boolean mSlideUpOrDownEnable;

    /**
     * topView位移的距离，默认是topView的高度
     */
    private int mTopViewMoveDistance;

    /**
     * bottomView位移的距离，默认是bottomView的高度
     */
    private int mBottomViewMoveDistance;

    /**
     * 默认状态关闭
     */
    private int mState = STATE_CLOSE;


    protected View mTopView, mBottomView, mCenterView;


    /**
     * 可滑动到的最小scale
     */
    private float mMinScale = DEFAULT_MIN_SCALE;

    /**
     * 当前的缩放比例
     */
    private float mCurrentScale = 1f;

    /**
     * touchSlop
     */
    private int mTouchSlop = 5;

    /**
     * 根据down up之间滑动的距离计算缩放比例
     */
    private float mSlopLength = 0;

    private float downY;
    private float mInitialMotionX, mInitialMotionY;


    /**
     * 用来ACTION_UP 之后处理变大（scale = 1f）
     * 或变小（scale = mMinScale）的动画
     */
    ValueAnimator animator;

    private OnGetCanScaleListener mCanScaleListener;

    /**
     * scale变化的监听器
     */
    private ArrayList<OnScaleChangedListener> mScaleListenerList;

    /**
     * 状态变化的监听器
     */
    private ArrayList<OnStateChangedListener> mStateListenerList;



    public ScaleLayout(Context context) {
        this(context, null);
    }

    public ScaleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScaleLayout, 0, 0);
        mState = a.getInteger(R.styleable.ScaleLayout_state, STATE_CLOSE);
        mSlideScaleEnable = a.getBoolean(R.styleable.ScaleLayout_slideScaleEnable, true);
        mSlideUpOrDownEnable = a.getBoolean(R.styleable.ScaleLayout_slideUpOrDownEnable, true);
        mSuggestScaleEnable = a.getBoolean(R.styleable.ScaleLayout_suggestScaleEnable, false);

        a.recycle();
        setupScaleLayout();
    }

    private void setupScaleLayout() {

        setWillNotDraw(false);
        mScaleListenerList = new ArrayList<>();
        mStateListenerList  = new ArrayList<>();
    }


    /**
     * 设置最小scale
     * {@link #DEFAULT_MIN_SCALE}
     * @param minScale
     */
    public void setMinScale(float minScale){

        if(minScale > 0f && minScale < 1f){
            if(mMinScale != minScale){
                if(isOpen()){
                    if(animator != null){
                        animator.cancel();
                        animator = null;
                    }
                    animator = getAnimator(mMinScale, minScale);
                    animator.start();
                }
                mMinScale = minScale;
            }
        }
    }


    public float getMinScale(){
        return mMinScale;
    }

    public float getCurrentScale(){
        return mCurrentScale;
    }


    public void setSuggestScaleEnable(boolean enable){
        if(mSuggestScaleEnable != enable){
            mSuggestScaleEnable = enable;
            requestLayout();
        }
    }

    /**
     * 设置的scale不得当的话，有可能topView / bottomView被覆盖
     * 通过设置{@link #setSuggestScaleEnable(boolean)}启用
     * @return
     */
    private float getSuggestScale(){

        int height = 0;

        if(mTopView != null){
            height += mTopView.getMeasuredHeight();
        }

        if(mBottomView != null){
            height += mBottomView.getMeasuredHeight();
        }
        return 1 - height * 1f / (getMeasuredHeight() - getPaddingTop() - getPaddingBottom());
    }


    /**
     * 设置是否启用滑动缩小功能
     * @param enable
     */
    public void setSlideScaleEnable(boolean enable){
        this.mSlideScaleEnable = enable;
    }

    /**
     *   现在有这么几种情况, 默认第二种, 两者都可以的话，感觉好奇怪，
     *   比如一直下滑会由大变小后又变大，操作感觉不是很好
     *   1. 只上滑放大下滑缩小  false
     *   2. 只上滑缩小下滑放大  true
     */
    public void setSlideUpOrDownEnable(boolean enable){
        this.mSlideUpOrDownEnable = enable;
    }

    /**
     * add OnScaleChangedListener
     * @param listener
     */
    public void addOnScaleChangedListener(OnScaleChangedListener listener){
        if(listener != null){
            mScaleListenerList.add(listener);
        }
    }

    /**
     * add OnStateChangedListener
     * @param listener
     */
    public void addOnStateChangedListener(OnStateChangedListener listener){
        if(listener != null){
            mStateListenerList.add(listener);
        }
    }

    public void setOnGetCanScaleListener(OnGetCanScaleListener listener){
        mCanScaleListener = listener;
    }

    /**
     *  {@link #setState(int state, boolean animationEnable)}
     * @param state
     */
    public void setState(int state){
        setState(state, true);
    }

    /**
     * 设置状态变化
     * @param state open or close
     * @param animationEnable change state with or without animation
     */
    public void setState(final int state, boolean animationEnable) {

        if(!animationEnable)
        {
            if(state == STATE_CLOSE){
                mSlopLength = 0;
                mCurrentScale = 1;
            }else{
                if(mSlideUpOrDownEnable) {
                    mSlopLength = -getMeasuredHeight() * (1 - mMinScale) * 1.25f;
                }else{
                    mSlopLength = getMeasuredHeight() * (1 - mMinScale) * 1.25f;
                }
                mCurrentScale = mMinScale;
            }
            doSetScale();
            mState = state;

        }else{
            if(animator != null){
                animator.cancel();
                animator = null;
            }

            if(state == STATE_CLOSE && mCurrentScale != 1){

                mSlopLength = 0;
                animator = getAnimator(mCurrentScale, 1f);

            }else if(state == STATE_OPEN && mCurrentScale != mMinScale){

                if(mSlideUpOrDownEnable) {
                    mSlopLength = -getMeasuredHeight() * (1 - mMinScale) * 1.25f;
                }else{
                    mSlopLength = getMeasuredHeight() * (1 - mMinScale) * 1.25f;
                }
                animator = getAnimator(mCurrentScale, mMinScale);
            }

            if(animator != null) {
                animator.addListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mState = state;
                    }

                });
                animator.start();
            }
        }
    }

    /**
     * 获取当前状态开启或者关闭
     * @return
     */
    public boolean isOpen(){

        return mState == STATE_OPEN;
    }

    /**
     * @param from scale
     * @param to  scale
     * @return
     */
    private ValueAnimator getAnimator(float from, float to){

        ValueAnimator animator = ValueAnimator.ofFloat(from, to);

        animator.setDuration((long)(DEFAULT_DURATION * Math.abs(to - from)));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (Float) animation.getAnimatedValue();
                if(mCurrentScale != scale){
                    mCurrentScale = scale;
                    doSetScale();
                }
            }
        });
        return animator;
    }

    /**
     * 1.触发监听事件
     * 2.计算scale的pivotX, pivotY(因为topView 和bottomView 的高度可能不一样，所以不能固定设置在中心点)
     * 3.设置 mCenterView的scale
     * 4.设置topView and BottomView 的动画（渐变和位移）
     */
    private void doSetScale() {

        int scaleListenerCount = mScaleListenerList.size();

        OnScaleChangedListener mScaleChangedListener;
        for (int i = 0; i < scaleListenerCount; i++) {
            mScaleChangedListener = mScaleListenerList.get(i);
            if(mScaleChangedListener != null){
                mScaleChangedListener.onScaleChanged(mCurrentScale);
            }
        }

        if(mCurrentScale == mMinScale || mCurrentScale == 1f){
            int stateListenerCount = mStateListenerList.size();

            OnStateChangedListener mStateChangedListener;
            for (int i = 0; i < stateListenerCount; i++) {
                mStateChangedListener = mStateListenerList.get(i);
                if(mStateChangedListener != null){
                    mStateChangedListener.onStateChanged(mCurrentScale == mMinScale);
                }
            }
        }

        doSetCenterView(mCurrentScale);
        doSetTopAndBottomView(mCurrentScale);
    }

    /**
     * 当scale发生变化时，centerView设置scale
     * @param scale
     */
    public void doSetCenterView(float scale){

        mCenterView.setPivotX(getCenterViewPivotX());
        mCenterView.setPivotY(getCenterViewPivotY());

        mCenterView.setScaleX(scale);
        mCenterView.setScaleY(scale);

    }

    public float getCenterViewPivotX(){
        return (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / 2f;
    }

    public float getCenterViewPivotY(){
        float pivotY = 0;
        if(mTopView == null && mBottomView != null) {

            pivotY = 0;

        }else if(mBottomView == null && mTopView != null){

            pivotY = getMeasuredHeight() - getPaddingBottom() - getPaddingTop();

        }else if(mTopView == null && mBottomView == null){

            pivotY = (getMeasuredHeight() - getPaddingTop() - getPaddingBottom()) / 2f;

        }else{
            int totalDistance = mTopViewMoveDistance + mBottomViewMoveDistance;
            int temp = getMeasuredHeight() - getPaddingBottom() - getPaddingTop();
            if(totalDistance != 0) {
                pivotY = temp * mTopViewMoveDistance / totalDistance;
            }
        }

        if(BuildConfig.DEBUG) {
            Log.d(TAG, "pivotY : " + pivotY);
        }
        return pivotY;
    }

    /**
     * 当scale发生变化时，topView bottomView设置渐变和位移
     * @param scale
     */
    public void doSetTopAndBottomView(float scale){

        //这里把mMinScale(0.7f) ~ 1 区间的值映射到 0 ~ 1
        float value = (scale - mMinScale) / (1 - mMinScale);
        float alpha = 1 - value;

        int top = 0;
        if(mTopView != null){
            top = getPaddingTop() + (int)(mTopViewMoveDistance * value);
            mTopView.setAlpha(alpha);
            mTopView.setTop(top);
            mTopView.setBottom(top + mTopView.getMeasuredHeight());
        }

        if(mBottomView != null){
            top = getMeasuredHeight() - getPaddingBottom()
                    -mBottomViewMoveDistance  - (int)(mBottomViewMoveDistance * value);
            mBottomView.setAlpha(alpha);
            mBottomView.setTop(top);
            mBottomView.setBottom(top + mBottomView.getMeasuredHeight());
        }
    }

    /**
     * xml解析完的回调,检测如果子view的数量少于1个抛出异常
     * 获取并设置top center bottom view
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int childCount = getChildCount();
        if(childCount < 1){
            throw new IllegalStateException("ScaleLayout should have one direct child at least !");
        }

        mTopView = findViewById(R.id.scaleLayout_top);
        mBottomView = findViewById(R.id.scaleLayout_bottom);
        mCenterView = findViewById(R.id.scaleLayout_center);

        // if centerView does not exist
        // it make no sense
        if(mCenterView == null){
            throw new IllegalStateException("ScaleLayout should have one direct child at least !");
        }

        LayoutParams lp = (FrameLayout.LayoutParams)mCenterView.getLayoutParams();
        lp.gravity &= Gravity.CENTER;
        mCenterView.setLayoutParams(lp);

        //hide topView and bottomView
        //set the topView on the top of ScaleLayout
        if(mTopView != null){
            lp = (FrameLayout.LayoutParams)mTopView.getLayoutParams();
            lp.gravity &= Gravity.TOP;
            mTopView.setLayoutParams(lp);
            mTopView.setAlpha(0);
        }

        //set the bottomView on the bottom of ScaleLayout
        if(mBottomView != null){
            lp = (FrameLayout.LayoutParams)mBottomView.getLayoutParams();
            lp.gravity &= Gravity.BOTTOM;
            mBottomView.setLayoutParams(lp);
            mBottomView.setAlpha(0);
        }

        setState(mState, false);
    }

    /**
     * 使得centerView 大小等同ScaleLayout的大小
     * 如果不想这样处理，也可以在触摸事件中使用TouchDelegate
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int layoutHeight = heightSize - getPaddingTop() - getPaddingBottom();
        int layoutWidth = widthSize - getPaddingLeft() - getPaddingRight();

        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(layoutWidth, MeasureSpec.EXACTLY);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(layoutHeight, MeasureSpec.EXACTLY);

        mCenterView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if(mBottomView != null){
            mBottomViewMoveDistance = mBottomView.getMeasuredHeight();
        }

        if(mTopView != null){
            mTopViewMoveDistance = mTopView.getMeasuredHeight();
        }

        if(mSuggestScaleEnable){
            setMinScale(getSuggestScale());
        }
    }

    /**
     * 所有的down事件都不拦截，因此接下来的move, up事件，
     * 都会先执行onInterceptTouchEvent的（move, up）
     * 继而分发给子view的dispatchTouchEvent(move, up)，
     * 然后在onInterceptTouchEvent（move）事件中判断是否满足滑动条件
     * 满足就拦截，拦截了之后move up事件就会都分发给自身的OnTouchEvent,
     * 否则如上继续分发给子View
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        boolean intercept = false;

        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:

                onTouchEvent(ev);
                mInitialMotionX = ev.getX();
                mInitialMotionY = ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                final float deltaX = Math.abs(ev.getX() - mInitialMotionX);
                final float deltaY = Math.abs(ev.getY() - mInitialMotionY);

                if(mCanScaleListener != null
                        && !mCanScaleListener.onGetCanScale(ev.getX() - mInitialMotionX > 0)){
                    intercept = false;
                }else {
                    intercept = deltaY > deltaX && deltaY > mTouchSlop;
                }
                break;
        }
        return intercept;
    }

    /**
     * 该方法中实现了
     * 上滑缩小下滑放大功能
     * 也可设置为 上滑放大下滑缩小
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (!isEnabled() || !mSlideScaleEnable) {
            return super.onTouchEvent(ev);
        }



        switch (ev.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                return true;

            case MotionEvent.ACTION_MOVE:
                if(mCanScaleListener != null && !mCanScaleListener.onGetCanScale(ev.getY() - downY > 0)){
                    return super.onTouchEvent(ev);
                }
                if (Math.abs(ev.getY() - downY) > mTouchSlop) {

                    mSlopLength += (ev.getY() - downY);

                    float scale;
                    if (mSlideUpOrDownEnable) {

                        scale = 1 + (0.8f * mSlopLength / getMeasuredHeight());
                    } else {
                        scale = 1 - (0.8f * mSlopLength / getMeasuredHeight());
                    }

                    scale = Math.min(scale, 1f);

                    mCurrentScale = Math.max(mMinScale, scale);

                    doSetScale();

                    downY = ev.getY();
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mCurrentScale > mMinScale && mCurrentScale < 1f) {

                    float half = (1 - mMinScale) / 2;

                    if (mCurrentScale >= mMinScale + half) {

                        setState(STATE_CLOSE, true);
                    } else {

                        setState(STATE_OPEN, true);
                    }
                }
                break;
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 存储当前状态
     * @return
     */
    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putSerializable(TAG, mState);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mState = (int) bundle.getSerializable(TAG);
            state = bundle.getParcelable("superState");
            setState(mState, true);
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if(animator != null){
            animator.cancel();
            animator = null;
        }
    }

    /**
     * 当centerView 的scale变化的时候，通过这个
     * 接口外部的View可以做一些同步的事情，
     * 比如，你有一个其他的view要根据centerView的变化而变化
     */
    public interface OnScaleChangedListener{

        void onScaleChanged(float currentScale);
    }

    /**
     * state == false 当完全关闭（scale == 1f）
     * state == true  或当完全开启的时候(scale = mMinScale)
     */
    public interface OnStateChangedListener{

        void onStateChanged(boolean state);
    }

    /**
     * 返回是否可以scale,主要为了适配部分有滑动冲突的view
     * 如TouchImageView, 甚至webView等
     * isScrollSown = true  代表向下，
     * isScrollSown = false 代表向上
     */
    public interface OnGetCanScaleListener{

        boolean onGetCanScale(boolean isScrollSown);
    }

}
