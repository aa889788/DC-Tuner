package com.shxyke.DCtuner.widget;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

public class RegionNumberEditText extends androidx.appcompat.widget.AppCompatEditText {

    private Context context;
    private int max;
    private int min;

    public RegionNumberEditText(Context context) {
        super(context);
        this.context = context;
    }

    public RegionNumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public RegionNumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    /**
     * 设置输入数字的范围
     * @param maxNum 最大数
     * @param minNum 最小数
     */
    public void setRegion(int maxNum, int minNum) {
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        this.max = maxNum;
        this.min = minNum;
    }

    public void setTextWatcher() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start >= 0) {
                    if (min != -1 && max != -1) {
                        try {
                            int num = Integer.parseInt(s.toString());
                            if (num > max) {
                                s = String.valueOf(max);
                                setText(s);
                                Toast.makeText(context.getApplicationContext(), "输入范围为" + min + "~" + max, Toast.LENGTH_LONG).show();
                            } else if (num < min) {
                                s = String.valueOf(min);
                                setText(s);
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}