package com.bigkoo.pickerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.bigkoo.pickerview.view.BasePickerView;
import com.bigkoo.pickerview.view.WheelTime;
import com.bigkoo.pickerview.view.WheelTime.OnWheelTimeSelectListener;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sai on 15/11/22.
 */
public class TimePickerView extends BasePickerView implements View.OnClickListener, OnWheelTimeSelectListener {

    public interface onLayoutListener
    {
        void onLayoutChange(TimePickerView tpv,int width, int height);
    }

    public enum Type {
        ALL, YEAR_MONTH_DAY, HOURS_MINS, MINS_SEC, MONTH_DAY_HOUR_MIN , YEAR_MONTH
    }// 四种选择模式，年月日时分，年月日，时分，分秒，月日时分，年月

    WheelTime wheelTime;
    private View btnSubmit, btnCancel, timeTopBar, divider, mainLL;
    private TextView tvTitle;
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";
    private OnTimeSelectListener timeSelectListener;
    private onLayoutListener layoutListener;

    public TimePickerView(Context context, Type type) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.pickerview_time, contentContainer);
        mainLL = findViewById(R.id.timeMainLL);
        // -----确定和取消按钮
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setTag(TAG_SUBMIT);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setTag(TAG_CANCEL);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        //顶部标题
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        // ----时间转轮
        final View timepickerview = findViewById(R.id.timepicker);
        wheelTime = new WheelTime(timepickerview, type);
        wheelTime.setOnWheelTimeSelectListener(this);

        divider = findViewById(R.id.timeDivider);
        timeTopBar = findViewById(R.id.timeTopBar);
        timeTopBar.setVisibility(View.GONE);

        //默认选中当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);
        wheelTime.setPicker(year, month, day, hours, minute, sec);

        ViewTreeObserver mainVto = mainLL.getViewTreeObserver();
        mainVto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                int width = mainLL.getMeasuredWidth();
                int height = mainLL.getMeasuredHeight();

                if(layoutListener != null) {
                    if(isShowing()) {
                        layoutListener.onLayoutChange(TimePickerView.this, width, height);
                    }else{
                        layoutListener.onLayoutChange(TimePickerView.this, width, 0);
                    }
                }
            }
        });
    }

    /**
     * Set has time unit label, ex: Year,Month,Day,Hour,Min
     * @param hasLabel If true then will display unit, else not.
     */
    public void setHasLabel(boolean hasLabel) {
        wheelTime.setHasLabel(hasLabel);
    }

    public void setHourMinLabel(String hour, String min) {
        wheelTime.setHourMinLabel(hour, min);
    }

    /**
     * 设置可以选择的时间范围
     *
     * @param startYear
     * @param endYear
     */
    public void setRange(int startYear, int endYear) {
        wheelTime.setStartYear(startYear);
        wheelTime.setEndYear(endYear);
    }

    /**
     * 设置选中时间
     * @param date
     */
    public void setTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null)
            calendar.setTimeInMillis(System.currentTimeMillis());
        else
            calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);
        wheelTime.setPicker(year, month, day, hours, minute, sec);
    }

//    /**
//     * 指定选中的时间，显示选择器
//     *
//     * @param date
//     */
//    public void show(Date date) {
//        Calendar calendar = Calendar.getInstance();
//        if (date == null)
//            calendar.setTimeInMillis(System.currentTimeMillis());
//        else
//            calendar.setTime(date);
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        int hours = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//        wheelTime.setPicker(year, month, day, hours, minute);
//        show();
//    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        wheelTime.setCyclic(cyclic);
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_CANCEL)) {
            if (timeSelectListener != null) {
                timeSelectListener.onClickCancelButton(this);
            }
        } else {
            if (timeSelectListener != null) {
                timeSelectListener.onClickSubmitButton(this);
            }
            /*
            if (timeSelectListener != null) {
                try {
                    Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
                    timeSelectListener.onClickSubmit(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            dismiss();
            return;
            */
        }
    }

    @Override
    public void onSelectDateTime(Date date) {
        if(timeSelectListener != null) {
            timeSelectListener.onTimeSelect(date);
        }
    }

    public interface OnTimeSelectListener {
        //public void onClickSubmit(Date date);
        void onTimeSelect(Date date);
        void onClickSubmitButton(TimePickerView tpv);
        void onClickCancelButton(TimePickerView tpv);
    }

    public void setOnTimeSelectListener(OnTimeSelectListener timeSelectListener) {
        this.timeSelectListener = timeSelectListener;
    }

    @Override
    public void setTitle(String title){
        tvTitle.setText(title);
    }

    @Override
    public void setHasDivider(boolean hasDivider) {
        if(hasDivider) {
            divider.setVisibility(View.VISIBLE);
        }else{
            divider.setVisibility(View.GONE);
        }
    }

    public void setDividerHeight(int height) {
        divider.getLayoutParams().height = height;
    }

    public void setDividerColor(int color) {
        divider.setBackgroundColor(color);
    }

    @Override
    public void setHasTopBar(boolean hasTopBar) {
        if(hasTopBar) {
            timeTopBar.setVisibility(View.VISIBLE);
        }else{
            timeTopBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void setHasCancelButton(boolean hasCancelButton) {
        if(hasCancelButton) {
            btnCancel.setVisibility(View.VISIBLE);
        }else{
            btnCancel.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setHasSubmitButton(boolean hasSubmitButton) {
        if(hasSubmitButton) {
            btnSubmit.setVisibility(View.VISIBLE);
        }else{
            btnSubmit.setVisibility(View.INVISIBLE);
        }
    }

    public Date getSelectedDate() {
        Date date = null;
        try {
            date = WheelTime.dateFormat.parse(wheelTime.getTime());
        } catch (ParseException e) {
        }
        return date;
    }

    public void setOnLayoutListener(onLayoutListener listener) {
        layoutListener = listener;
    }

    public void removeOnLayoutListener() {
        layoutListener = null;
    }

}
