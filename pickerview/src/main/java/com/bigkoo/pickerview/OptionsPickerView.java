package com.bigkoo.pickerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.view.BasePickerView;
import com.bigkoo.pickerview.view.WheelOptions;
import com.bigkoo.pickerview.view.WheelOptions.OnWheelOptionsSelectListener;
import java.util.ArrayList;

/**
 * Created by Sai on 15/11/22.
 */
public class OptionsPickerView<T> extends BasePickerView implements View.OnClickListener, OnWheelOptionsSelectListener {
    WheelOptions wheelOptions;
    private View btnSubmit, btnCancel , optionsTopBar;
    private TextView tvTitle;
    private OnOptionsSelectListener optionsSelectListener;
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";

    public OptionsPickerView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.pickerview_options, contentContainer);
        // -----确定和取消按钮
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setTag(TAG_SUBMIT);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setTag(TAG_CANCEL);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        //顶部标题
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        // ----转轮
        final View optionsPicker = findViewById(R.id.optionspicker);
        wheelOptions = new WheelOptions(optionsPicker);
        wheelOptions.setOnWheelOptionsSelectListener(this);

        optionsTopBar = findViewById(R.id.optionsTopBar);
    }
    public void setPicker(ArrayList<T> optionsItems) {
        wheelOptions.setPicker(optionsItems, null, null, false);
    }

    public void setPicker(ArrayList<T> options1Items,
                          ArrayList<ArrayList<T>> options2Items, boolean linkage) {
        wheelOptions.setPicker(options1Items, options2Items, null, linkage);
    }

    public void setPicker(ArrayList<T> options1Items,
                          ArrayList<ArrayList<T>> options2Items,
                          ArrayList<ArrayList<ArrayList<T>>> options3Items,
                          boolean linkage) {
        wheelOptions.setPicker(options1Items, options2Items, options3Items,
                linkage);
    }
    /**
     * 设置选中的item位置
     * @param option1
     */
    public void setSelectOptions(int option1){
        wheelOptions.setCurrentItems(option1, 0, 0);
    }
    /**
     * 设置选中的item位置
     * @param option1
     * @param option2
     */
    public void setSelectOptions(int option1, int option2){
        wheelOptions.setCurrentItems(option1, option2, 0);
    }
    /**
     * 设置选中的item位置
     * @param option1
     * @param option2
     * @param option3
     */
    public void setSelectOptions(int option1, int option2, int option3){
        wheelOptions.setCurrentItems(option1, option2, option3);
    }
    /**
     * 设置选项的单位
     * @param label1
     */
    public void setLabels(String label1){
        wheelOptions.setLabels(label1, null, null);
    }
    /**
     * 设置选项的单位
     * @param label1
     * @param label2
     */
    public void setLabels(String label1,String label2){
        wheelOptions.setLabels(label1, label2, null);
    }
    /**
     * 设置选项的单位
     * @param label1
     * @param label2
     * @param label3
     */
    public void setLabels(String label1,String label2,String label3){
        wheelOptions.setLabels(label1, label2, label3);
    }
    /**
     * 设置是否循环滚动
     * @param cyclic
     */
    public void setCyclic(boolean cyclic){
        wheelOptions.setCyclic(cyclic);
    }
    public void setCyclic(boolean cyclic1,boolean cyclic2,boolean cyclic3) {
        wheelOptions.setCyclic(cyclic1, cyclic2, cyclic3);
    }

    @Override
    public void onClick(View v)
    {
        String tag=(String) v.getTag();
        if(tag.equals(TAG_CANCEL))
        {
            if(optionsSelectListener != null) {
                optionsSelectListener.onClickCancelButton();
            }
        }
        else
        {
            if(optionsSelectListener!=null) {
                optionsSelectListener.onClickSubmitButton();
                /*
                int[] optionsCurrentItems=wheelOptions.getCurrentItems();
                optionsSelectListener.onOptionsSelect(optionsCurrentItems[0], optionsCurrentItems[1], optionsCurrentItems[2]);
                */
            }
        }
    }

    @Override
    public void onRowSelected(int optionIndex, int rowIndex) {
        if(optionsSelectListener != null) {
            optionsSelectListener.onRowSelected(optionIndex, rowIndex);
        }
    }

    public interface OnOptionsSelectListener {
        //public void onOptionsSelect(int options1, int option2, int options3);
        void onRowSelected(int optionIndex, int rowIndex);
        void onClickSubmitButton();
        void onClickCancelButton();
    }

    public void setOnOptionsSelectListener(
            OnOptionsSelectListener optionsSelectListener) {
        this.optionsSelectListener = optionsSelectListener;
    }

    @Override
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void setHasTopBar(boolean hasTopBar) {
        if(hasTopBar) {
            optionsTopBar.setVisibility(View.VISIBLE);
        }else{
            optionsTopBar.setVisibility(View.GONE);
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

    public int getRowIndex(int optionIndex) {
        int[] optionsCurrentItems = wheelOptions.getCurrentItems();
        return optionsCurrentItems[optionIndex];
    }
}
