package com.xkeshi.cashierdesk.clearableedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;


/**
 * 带清除的EditText View
 * <p>
 * The clearable EditText, you can set on {@link TextWatcherCallback } to handle the text change event.
 * Created by zhangdroid on 2017/8/1.
 * <p>
 * <p>
 * <p>
 * xml使用方法
 * <com.xkeshi.cashierdesk.viewlibrary.view.ClearableEditText
 * android:id="@+id/clearableEditText"
 * android:layout_width="match_parent"
 * android:layout_height="match_parent"
 * app:drawable_clear="@mipmap/ic_launcher" />
 */
public class ClearableEditText extends AppCompatEditText implements TextWatcher, OnFocusChangeListener {
    private Drawable mClearDrawable;
    private TextWatcherCallback mTextWatcherCallback = null;
    private OnClearCallback mOnClearCallback;
    private boolean mIsFocused;
    private int msize = 30;

    private OnFocusChangeListener mOnFocusChangeListener;

    /**
     * 设置删除图标
     *
     * @param resId 删除图标id
     */
    public void setClearImageResource(int resId) {
        if (resId != 0) {
            mClearDrawable = getResources().getDrawable(resId);
        }
    }

    /**
     * 设置监听器
     *
     * @param textWatcherCallback
     */
    public void setTextWatcherCallback(TextWatcherCallback textWatcherCallback) {
        if (textWatcherCallback != null) {
            mTextWatcherCallback = textWatcherCallback;
        }
    }

    public ClearableEditText(Context context) {
        this(context, null);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ClearableEditText, defStyle, 0);
        mClearDrawable = typedArray.getDrawable(R.styleable.ClearableEditText_drawable_clear);
        msize = (int) typedArray.getDimension(R.styleable.ClearableEditText_drawable_size, msize);
        typedArray.recycle();

        if (mClearDrawable == null) {
            mClearDrawable = getResources().getDrawable(R.mipmap.viewlib_icon_delete);
        }
//        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        mClearDrawable.setBounds(0, 0, msize, msize);
        setClearDrawableVisible(false);
        addTextChangedListener(this);
        super.setOnFocusChangeListener(this);
    }

    private void setClearDrawableVisible(boolean isVisible) {
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], isVisible ? mClearDrawable : null,
                getCompoundDrawables()[3]);
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        mOnFocusChangeListener = onFocusChangeListener;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener.onFocusChange(v, hasFocus);
        }
        mIsFocused = hasFocus;
        if (hasFocus) {
            setClearDrawableVisible(getText().length() > 0);
        } else {
            setClearDrawableVisible(false);
        }
    }

    /**
     * 监听Touch事件，通过触摸点的位置，来模拟点击事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                // 判断触摸点是否在删除图标上
                Rect rect = getCompoundDrawables()[2].getBounds();
                int height = rect.height();
                // 删除图标左、右边缘
                int leftBound = getWidth() - getTotalPaddingRight();
                int rightBound = getWidth() - getPaddingRight();
                boolean isInnerWidth = x > leftBound && x < rightBound;
                // 删除图标上、下边缘
                int distance = (getHeight() - height) / 2;
                boolean isInnerHeight = y > distance && y < (distance + height);
                if (isInnerWidth && isInnerHeight) {
                    this.setText("");
                    if (mOnClearCallback != null) {
                        mOnClearCallback.onClear(this);
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (mIsFocused) {
            setClearDrawableVisible(text.length() > 0);
        }
        if (mTextWatcherCallback != null) {
            mTextWatcherCallback.handleTextChanged(text, start, lengthBefore, lengthAfter);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    public interface TextWatcherCallback {

        /**
         * onTextChanged方法的回调
         *
         * @param text
         * @param start
         * @param lengthBefore
         * @param lengthAfter
         */
        void handleTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter);
    }

    /**
     * 清除按钮点击回调
     */
    public interface OnClearCallback {
        /**
         * 当点击清除按钮时候执行刚方法
         *
         * @param view 执行的 {@link ClearableEditText}
         */
        void onClear(ClearableEditText view);
    }

    public void setOnClearCallback(OnClearCallback clearCallback) {
        mOnClearCallback = clearCallback;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        mClearDrawable = null;
    }

}