package com.bigkoo.pickerview.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.bigkoo.pickerview.utils.PickerViewAnimateUtil;
import com.bigkoo.pickerview.R;
import com.bigkoo.pickerview.listener.OnDismissListener;

/**
 * Created by Sai on 15/11/22.
 * 精仿iOSPickerViewController控件
 */
public abstract class BasePickerView {
    private final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM
    );

    public abstract void setTitle(String title);
    public abstract void setHasTopBar(boolean hasTopBar);
    public abstract void setHasCancelButton(boolean hasCancelButton);
    public abstract void setHasSubmitButton(boolean hasSubmitButton);

    private Context context;
    protected ViewGroup contentContainer;
    private ViewGroup decorView;//activity的根View
    private ViewGroup rootView;//附加View 的 根View

    private OnDismissListener onDismissListener;
    private boolean isDismissing;

    private Animation outAnim;
    private Animation inAnim;
    private int gravity = Gravity.BOTTOM;
    private boolean hasAnimation = false;

    private static BasePickerView currentPickerView = null;

    public BasePickerView(Context context){
        this.context = context;
        initViews();
        init();
        initEvents();
    }

    protected void initViews(){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        decorView = (ViewGroup) ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        rootView = (ViewGroup) layoutInflater.inflate(R.layout.layout_basepickerview, decorView, false);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));
        contentContainer = (ViewGroup) rootView.findViewById(R.id.content_container);
        contentContainer.setLayoutParams(params);
    }

    protected void init() {
        inAnim = getInAnimation();
        outAnim = getOutAnimation();
    }
    protected void initEvents() {
    }
    /**
     * show的时候调用
     *
     * @param view 这个View
     */
    private void onAttached(View view) {
        decorView.addView(view);

        if (hasAnimation) {
            contentContainer.startAnimation(inAnim);
        }

        currentPickerView = this;
    }
    /**
     * 添加这个View到Activity的根视图
     */
    public void show(boolean hasAnimation) {
        if (isShowing()) {
            return;
        }

        if(currentPickerView != null) {
            currentPickerView.dismiss();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(currentPickerView.isDismissing) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }).start();
        }

        this.setAnimationEnable(hasAnimation);

        onAttached(rootView);
    }

    public void show() {
        this.show(true);
    }
    /**
     * 检测该View是不是已经添加到根视图
     *
     * @return 如果视图已经存在该View返回true
     */
    public boolean isShowing() {
        View view = decorView.findViewById(R.id.outmost_container);
        return view != null;
    }
    public void dismiss() {
        if (isDismissing) {
            return;
        }

        //消失动画
        outAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                decorView.post(new Runnable() {
                    @Override
                    public void run() {
                        //从activity根视图移除
                        decorView.removeView(rootView);
                        isDismissing = false;
                        currentPickerView = null;
                        if (onDismissListener != null) {
                            onDismissListener.onDismiss(BasePickerView.this);
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if (hasAnimation) {
            contentContainer.startAnimation(outAnim);
            isDismissing = true;
        }else{
            decorView.removeView(rootView);
            isDismissing = false;
            currentPickerView = null;
        }
    }
    public Animation getInAnimation() {
        int res = PickerViewAnimateUtil.getAnimationResource(this.gravity, true);
        return AnimationUtils.loadAnimation(context, res);
    }

    public Animation getOutAnimation() {
        int res = PickerViewAnimateUtil.getAnimationResource(this.gravity, false);
        return AnimationUtils.loadAnimation(context, res);
    }

    public BasePickerView setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    public BasePickerView setCancelable(boolean isCancelable) {
        View view = rootView.findViewById(R.id.outmost_container);

        if (isCancelable) {
            view.setOnTouchListener(onCancelableTouchListener);
        }else{
            view.setOnTouchListener(null);
        }
        return this;
    }
    /**
     * Called when the user touch on black overlay in order to dismiss the dialog
     */
    private final View.OnTouchListener onCancelableTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dismiss();
            }
            return false;
        }
    };

    public View findViewById(int id){
        return contentContainer.findViewById(id);
    }

    /**
     * Set if pickerView is show,then mask other UI touch event.
     * @param isMask If true will set masked, else not.
     */
    public void setMasked(boolean isMask) {
        FrameLayout maskView = (FrameLayout)rootView.findViewById(R.id.outmost_container);
        maskView.setClickable(isMask);

        if(isMask) {
            maskView.setBackgroundColor(context.getResources().getColor(R.color.bgColor_overlay));
        }else {
            maskView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    /**
     * Set pickerView animation enable when appear and disappear.
     * @param enable If true will enable, else disable.
     */
    private void setAnimationEnable(boolean enable) {
        hasAnimation = enable;
    }
}
