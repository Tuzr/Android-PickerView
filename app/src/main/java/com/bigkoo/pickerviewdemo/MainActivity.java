package com.bigkoo.pickerviewdemo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerviewdemo.bean.ProvinceBean;

public class MainActivity extends Activity implements TimePickerView.onLayoutListener, OptionsPickerView.onLayoutListener {

    private ArrayList<ProvinceBean> options1Items = new ArrayList<ProvinceBean>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<ArrayList<ArrayList<String>>>();
    private TextView tvTime, tvOptions;
    TimePickerView pvTime;
    OptionsPickerView pvOptions;
    View vMasker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vMasker=findViewById(R.id.vMasker);
        tvTime=(TextView) findViewById(R.id.tvTime);
        tvOptions=(TextView) findViewById(R.id.tvOptions);
        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.MINS_SEC);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();

        pvTime.setHasLabel(true);
        //pvTime.setHourMinLabel("小時","分鐘");

        //pvTime.setHasDivider(true);

        //pvTime.setDividerColor(Color.BLUE);
        //pvTime.setDividerHeight(50);

        //pvTime.setHasTopBar(false);
        //pvTime.setHasCancelButton(true);
        //pvTime.setHasSubmitButton(true);

        pvTime.setTitle("選擇時間");

        //pvTime.setMasked(false);

        pvTime.setRange(calendar.get(Calendar.YEAR) - 5, calendar.get(Calendar.YEAR));
        pvTime.setTime(new Date());
        pvTime.setCyclic(true);
        pvTime.setCancelable(false);
        //pvTime.show(false);
        pvTime.setOnLayoutListener(this);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        /*
        Date date = null;
        try {
            date = format.parse("2015-02-18 11:22");
        } catch (ParseException e) {
        }
        pvTime.setTime(date);
        */

        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                tvTime.setText(getTime(date));
            }

            @Override
            public void onClickSubmitButton(TimePickerView tpv) {
                pvTime.dismiss();

                String result = getTime(pvTime.getSelectedDate());
                Log.d("debug","Select date is: " + result);
            }

            @Override
            public void onClickCancelButton(TimePickerView tpv) {
                pvTime.dismiss();
            }
        });
        //弹出时间选择器
        tvTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if(pvTime.isShowing()) {
                    //pvTime.removeOnLayoutListener();
                    pvTime.dismiss();
                }else{
                    //pvOptions.removeOnLayoutListener();
                    pvTime.setOnLayoutListener(MainActivity.this);
                    pvTime.show(false);
                }
            }
        });

        //选项选择器
        pvOptions = new OptionsPickerView(this);

        //选项1
        options1Items.add(new ProvinceBean(0,"广东","广东省，以岭南东道、广南东路得名","其他数据"));
        options1Items.add(new ProvinceBean(1,"湖南","湖南省地处中国中部、长江中游，因大部分区域处于洞庭湖以南而得名湖南","芒果TV"));
        options1Items.add(new ProvinceBean(3,"广西","嗯～～",""));

        //选项2
        ArrayList<String> options2Items_01=new ArrayList<String>();
        options2Items_01.add("广州");
        options2Items_01.add("佛山");
        options2Items_01.add("东莞");
        options2Items_01.add("阳江");
        options2Items_01.add("珠海");
        ArrayList<String> options2Items_02=new ArrayList<String>();
        options2Items_02.add("长沙");
        options2Items_02.add("岳阳");
        ArrayList<String> options2Items_03=new ArrayList<String>();
        options2Items_03.add("桂林");
        options2Items.add(options2Items_01);
        options2Items.add(options2Items_02);
        options2Items.add(options2Items_03);

        //选项3
        ArrayList<ArrayList<String>> options3Items_01 = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> options3Items_02 = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> options3Items_03 = new ArrayList<ArrayList<String>>();
        ArrayList<String> options3Items_01_01=new ArrayList<String>();
        options3Items_01_01.add("白云1");
        options3Items_01_01.add("天河2");
        options3Items_01_01.add("海珠3");
        options3Items_01_01.add("越秀4");
        options3Items_01.add(options3Items_01_01);
        ArrayList<String> options3Items_01_02=new ArrayList<String>();
        options3Items_01_02.add("南海1");
        options3Items_01_02.add("高明2");
        options3Items_01_02.add("顺德3");
        options3Items_01_02.add("禅城4");
        options3Items_01.add(options3Items_01_02);
        ArrayList<String> options3Items_01_03=new ArrayList<String>();
        options3Items_01_03.add("其他1");
        options3Items_01_03.add("常平2");
        options3Items_01_03.add("虎门3");
        options3Items_01.add(options3Items_01_03);
        ArrayList<String> options3Items_01_04=new ArrayList<String>();
        options3Items_01_04.add("其他1");
        options3Items_01_04.add("其他2");
        options3Items_01_04.add("其他3");
        options3Items_01.add(options3Items_01_04);
        ArrayList<String> options3Items_01_05=new ArrayList<String>();
        options3Items_01_05.add("其他1");
        options3Items_01_05.add("其他2");
        options3Items_01_05.add("其他3");
        options3Items_01.add(options3Items_01_05);

        ArrayList<String> options3Items_02_01=new ArrayList<String>();
        options3Items_02_01.add("长沙1");
        options3Items_02_01.add("长沙2");
        options3Items_02_01.add("长沙3");
        options3Items_02_01.add("长沙4");
        options3Items_02_01.add("长沙5");
        options3Items_02_01.add("长沙6");
        options3Items_02_01.add("长沙7");
        options3Items_02_01.add("长沙8");
        options3Items_02.add(options3Items_02_01);
        ArrayList<String> options3Items_02_02=new ArrayList<String>();
        options3Items_02_02.add("岳1");
        options3Items_02_02.add("岳2");
        options3Items_02_02.add("岳3");
        options3Items_02_02.add("岳4");
        options3Items_02_02.add("岳5");
        options3Items_02_02.add("岳6");
        options3Items_02_02.add("岳7");
        options3Items_02_02.add("岳8");
        options3Items_02_02.add("岳9");
        options3Items_02.add(options3Items_02_02);
        ArrayList<String> options3Items_03_01=new ArrayList<String>();
        options3Items_03_01.add("好山水");
        options3Items_03.add(options3Items_03_01);

        options3Items.add(options3Items_01);
        options3Items.add(options3Items_02);
        options3Items.add(options3Items_03);

        //三级联动效果
        //pvOptions.setPicker(options1Items, options2Items, options3Items, true);
        //pvOptions.setMasked(false);

        ArrayList<String> conditionItems = new ArrayList<String>();
        conditionItems.add("低於");
        conditionItems.add("高於");

        options2Items.clear();
        options2Items_01.clear();
        options2Items_01.add("0");
        options2Items_01.add("5");
        options2Items_01.add("10");
        options2Items_01.add("15");

        options2Items.add(options2Items_01);
        //options2Items.add(options2Items_01);

        pvOptions.setPicker(conditionItems, options2Items, false);
        pvOptions.setLabels(null, "dB");
        //pvOptions.setMasked(false);

                //pvOptions.setPicker(options1Items);
                //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
                //pvOptions.setTitle("选择城市");
        //pvOptions.setHasTopBar(false);

        pvOptions.setCyclic(false, true, false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(0, 3);
        pvOptions.show(false);

        final ArrayList<String> item0 = conditionItems;
        final ArrayList<String> item1 = options2Items_01;

        pvOptions.setOnOptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            /*
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText();
                //+ options2Items.get(options1).get(option2)
                //+ options3Items.get(options1).get(option2).get(options3);
                tvOptions.setText(tx);
                //vMasker.setVisibility(View.GONE);
            }
            */

            @Override
            public void onRowSelected(int optionIndex, int rowIndex) {
                /*
                if(optionIndex == 0) {
                    String tx = options1Items.get(rowIndex).getPickerViewText();
                    tvOptions.setText(tx);
                }
                */
                String condition = item0.get(pvOptions.getRowIndex(0));
                String value = item1.get(pvOptions.getRowIndex(1));

                if(optionIndex == 0) {
                    condition = item0.get(rowIndex);
                }

                if(optionIndex == 1) {
                    value = item1.get(rowIndex);
                }

                tvOptions.setText(condition + value + "db");

                Log.d("debug", String.format("Option: %d, Row: %d", optionIndex, rowIndex));
            }

            @Override
            public void onClickSubmitButton(OptionsPickerView opv) {

            }

            @Override
            public void onClickCancelButton(OptionsPickerView opv) {

            }
        });
        //点击弹出选项选择器
        tvOptions.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(pvOptions.isShowing()) {
                    //pvOptions.removeOnLayoutListener();
                    pvOptions.dismiss();
                }else{
                    //pvTime.removeOnLayoutListener();
                    pvOptions.setOnLayoutListener(MainActivity.this);
                    pvOptions.show(false);
                }
            }
        });
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    @Override
    public void onLayoutChange(TimePickerView tpv, int width, int height) {
        Log.e("TimePickerView",String.format("width:%d height:%d", width, height));
    }

    @Override
    public void onLayoutChange(OptionsPickerView tpv, int width, int height) {
        Log.e("OptionsPickerView",String.format("width:%d height:%d", width, height));
    }
}
