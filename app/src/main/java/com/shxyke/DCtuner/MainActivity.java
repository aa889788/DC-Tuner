package com.shxyke.DCtuner;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {


    private Switch mSwitch;
    private TextView mViewShow;
    private FloatingActionButton fab;
    private SharedPreferences sharedPreferences;
    private AlertDialog.Builder builder;

    private boolean run = false;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("share", MODE_PRIVATE);
        mViewShow = findViewById(R.id.textView);
        mViewShow.setText(sharedPreferences.getBoolean(getString(R.string.DcModeStatus), false)?getString(R.string.text_on):getString(R.string.text_off));
        mSwitch = findViewById(R.id.switch1);
        fab= findViewById(R.id.floatingActionButton);
        mSwitch.setChecked(sharedPreferences.getBoolean(getString(R.string.DcModeStatus), false));
        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // 开启switch，设置提示信息
                basic_com.set_status(true);
                basic_com.update_status(getApplicationContext());
                mSwitch.setChecked(sharedPreferences.getBoolean(getString(R.string.DcModeStatus), false));
                mViewShow.setText(sharedPreferences.getBoolean(getString(R.string.DcModeStatus), false)?getString(R.string.text_on):getString(R.string.text_off));
            } else {
                // 关闭swtich，设置提示信息
                basic_com.set_status(false);
                basic_com.update_status(getApplicationContext());
                mSwitch.setChecked(sharedPreferences.getBoolean(getString(R.string.DcModeStatus), false));
                mViewShow.setText(sharedPreferences.getBoolean(getString(R.string.DcModeStatus), false)?getString(R.string.text_on):getString(R.string.text_off));
            }
        });

        fab.setOnClickListener(v ->  showInput());

        run = true;
        handler.postDelayed(task, 1000);

    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (run) {
                mSwitch.setChecked(sharedPreferences.getBoolean(getString(R.string.DcModeStatus), false));
                mViewShow.setText(sharedPreferences.getBoolean(getString(R.string.DcModeStatus), false)?getString(R.string.text_on):getString(R.string.text_off));
                handler.postDelayed(this, 1000);
            }
        }
    };

    private void showInput() {
        final RegionNumberEditText editText = new RegionNumberEditText(this);
        editText.setRegion(255,1);
        editText.setTextWatcher();
        builder = new AlertDialog.Builder(this).setTitle("Set ELVSS Min, Current:"+sharedPreferences.getInt(getString(R.string.ElvssMin),66)).setView(editText)
                .setPositiveButton("Set", (dialogInterface, i) -> {
                    if(TextUtils.isEmpty(editText.getText())){
                        Toast.makeText(getApplicationContext(), "输入不得为空", Toast.LENGTH_LONG).show();
                    }else {
                        basic_com.set_elvss_num(Integer.valueOf(editText.getText().toString()));
                        basic_com.update_status(getApplicationContext());
                    }
                });
        builder.create().show();
    }


}
