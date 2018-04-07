package home.smart.fly.animations.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import home.smart.fly.animations.R;

/**
 * author : engineer
 * e-mail : yingkongshi11@gmail.com
 * time   : 2018/2/4
 * desc   :
 * version: 1.0
 */
public class CircleMenu extends ViewGroup {

    private int mRadius;

    private static final float RADIO_DEFAULT_CHILD_DIMENSION = 1 / 4F;
    private static final float RADIO_PADDING_LAYOUT = 1 / 12F;

    private float mPadding;

    private double mStartAngle = 0;

    private String[] mItemTexts;
    private int[] mItemImgs;

    private int mMenuItemCount;

    private int mMenuItemLayoutId = R.layout.circle_menu_item;

    private OnItemClickListener mOnItemClickListener;


    public void setMenuItemIconsAndTexts(int[] images, String[] texts) {
        if (null == images && null == texts) {
            throw new IllegalArgumentException("menu can not be null");
        }

        mItemTexts = texts;
        mItemImgs = images;
        mMenuItemCount = Math.min(images.length, texts.length);
        buildMenuItems();
    }

    private void buildMenuItems() {
        for (int i = 0; i < mMenuItemCount; i++) {
            View itemView = inflateMenuView(i);
            initMenuItem(itemView, i);
            addView(itemView);
        }
    }

    private View inflateMenuView(final int childIndex) {
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        View itemView = mInflater.inflate(mMenuItemLayoutId, this, false);
        return itemView;
    }

    private void initMenuItem(View itemView, int childIndex) {

    }

    public CircleMenu(Context context) {
        super(context);
    }

    public CircleMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureContainer(widthMeasureSpec, heightMeasureSpec);
        measureChildViews();
    }

    private void measureChildViews() {
        mRadius = Math.max(getMeasuredHeight(), getMeasuredWidth());

        final int count = getChildCount();

        int childSize = (int) (mRadius * RADIO_DEFAULT_CHILD_DIMENSION);

        int childMode = MeasureSpec.EXACTLY;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

            int makeMeasureSpaec = -1;
            makeMeasureSpaec = MeasureSpec.makeMeasureSpec(childSize, childMode);
            child.measure(makeMeasureSpaec, makeMeasureSpaec);
        }

        mPadding = RADIO_PADDING_LAYOUT * mPadding;
    }

    private void measureContainer(int widthMeasureSpec, int heightMeasureSpec) {
        int resultWidth = 0;
        int resultHeight = 0;

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
            resultWidth = getSuggestedMinimumWidth() == 0 ? getWidth() : getSuggestedMinimumWidth();
            resultHeight = getSuggestedMinimumHeight() == 0 ? getHeight() : getSuggestedMinimumHeight();
        } else {
            resultWidth = resultHeight = Math.min(widthSize, heightSize);
        }

        setMeasuredDimension(resultWidth, resultHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childCount = getChildCount();
        int left, top;
        int itemWidth = (int) (mRadius * RADIO_DEFAULT_CHILD_DIMENSION);
        float angleDelay = 360 / childCount;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

            mStartAngle %= 360;
            float distanceFromCenter = mRadius / 2f - itemWidth / 2 - mPadding;
            left = (int) (mRadius / 2 + Math.round(distanceFromCenter *
                    Math.cos(Math.toRadians(mStartAngle)) - 1 / 2f * itemWidth));
            top = (int) (mRadius / 2 + Math.round(distanceFromCenter *
                    Math.sin(Math.toRadians(mStartAngle)) - 1 / 2f * itemWidth));
            child.layout(left,top,left+itemWidth,top+itemWidth);

            mStartAngle+=angleDelay;
        }
    }

    public interface OnItemClickListener {
        void OnClick(View v, int index);
    }
}
