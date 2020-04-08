package com.shxyke.DCtuner;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {


    private Switch mSwitch;
    private CheckBox selSRGB;
    private CheckBox selDCIP3;
    private CheckBox selADAPT;
    private CheckBox selOPMODE;
    private CheckBox setWithBoot;
    private RecyclerView rtext;
    private FloatingActionButton fab;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private AlertDialog.Builder builder;
    private boolean run = false;
    private boolean isColorModKernel = false;
    private final Handler handler = new Handler();

    final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("share", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        rtext = findViewById(R.id.recyclerView);
        mSwitch = findViewById(R.id.switch1);
        selSRGB = findViewById(R.id.checkBox);
        selDCIP3 = findViewById(R.id.checkBox2);
        selADAPT = findViewById(R.id.checkBox3);
        selOPMODE = findViewById(R.id.checkBox4);
        setWithBoot = findViewById(R.id.checkBox5);
        rtext = findViewById(R.id.recyclerView);
        fab= findViewById(R.id.floatingActionButton);
        Utilties.update_status(getApplicationContext());

        isColorModKernel = Utilties.isColorModKernel();
        if(!isColorModKernel){
            selSRGB.setEnabled(false);
            selDCIP3.setEnabled(false);
            selADAPT.setEnabled(false);
            selOPMODE.setEnabled(false);
        }
        else {
            selSRGB.setChecked(sharedPreferences.getBoolean(getString(R.string.srgb_status), false));
            selDCIP3.setChecked(sharedPreferences.getBoolean(getString(R.string.dci_p3_status), false));
            selADAPT.setChecked(sharedPreferences.getBoolean(getString(R.string.adaption_status), false));
            selOPMODE.setChecked(sharedPreferences.getBoolean(getString(R.string.oneplus_status), false));
        }
        mSwitch.setChecked(sharedPreferences.getBoolean(getString(R.string.dc_status), false));
        setWithBoot.setChecked(sharedPreferences.getBoolean(getString(R.string.auto_run_status), false));
        fab.setOnClickListener(v ->  showInput());
        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // 开启switch，设置提示信息
                Utilties.set_dc_status(true);
                Utilties.update_status(getApplicationContext());
                mSwitch.setChecked(sharedPreferences.getBoolean(getString(R.string.dc_status), false));
                //mViewShow.setText(sharedPreferences.getBoolean(getString(R.string.DcModeStatus), false)?getString(R.string.text_on):getString(R.string.text_off));
            } else {
                // 关闭swtich，设置提示信息
                Utilties.set_dc_status(false);
                Utilties.update_status(getApplicationContext());
                mSwitch.setChecked(sharedPreferences.getBoolean(getString(R.string.dc_status), false));
                //mViewShow.setText(sharedPreferences.getBoolean(getString(R.string.DcModeStatus), false)?getString(R.string.text_on):getString(R.string.text_off));
            }
        });

        selSRGB.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Utilties.set_srgb_status(true);
                Utilties.update_status(getApplicationContext());
                selSRGB.setChecked(sharedPreferences.getBoolean(getString(R.string.srgb_status), false));
                //mViewShow3.setText(sharedPreferences.getBoolean(getString(R.string.srgb_status), false)?"SRGB On":"SRGB Off");
                selDCIP3.setChecked(sharedPreferences.getBoolean(getString(R.string.dci_p3_status), false));
                selADAPT.setChecked(sharedPreferences.getBoolean(getString(R.string.adaption_status), false));
                selOPMODE.setChecked(sharedPreferences.getBoolean(getString(R.string.oneplus_status), false));
                //mViewShow2.setText(sharedPreferences.getBoolean(getString(R.string.dci_p3_status), false)?"DCI P3 On":"DCI P3 Off");
            } else {
                if(Utilties.get_srgb_status()) {
                    Utilties.set_srgb_status(false);
                    Utilties.update_status(getApplicationContext());
                    selSRGB.setChecked(sharedPreferences.getBoolean(getString(R.string.srgb_status), false));
                    //selSRGB.setText(sharedPreferences.getBoolean(getString(R.string.srgb_status), false)?"SRGB On":"SRGB Off");
                }
            }
        });

        selDCIP3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Utilties.set_dci_p3_status(true);
                Utilties.update_status(getApplicationContext());
                selDCIP3.setChecked(sharedPreferences.getBoolean(getString(R.string.dci_p3_status), false));
                //mViewShow3.setText(sharedPreferences.getBoolean(getString(R.string.srgb_status), false)?"SRGB On":"SRGB Off");
                selSRGB.setChecked(sharedPreferences.getBoolean(getString(R.string.srgb_status), false));
                selADAPT.setChecked(sharedPreferences.getBoolean(getString(R.string.adaption_status), false));
                selOPMODE.setChecked(sharedPreferences.getBoolean(getString(R.string.oneplus_status), false));
                //mViewShow2.setText(sharedPreferences.getBoolean(getString(R.string.dci_p3_status), false)?"DCI P3 On":"DCI P3 Off");
            } else {
                if(Utilties.get_dc_status()) {
                    Utilties.set_dci_p3_status(false);
                    Utilties.update_status(getApplicationContext());
                    selDCIP3.setChecked(sharedPreferences.getBoolean(getString(R.string.dci_p3_status), false));
                    //selSRGB.setText(sharedPreferences.getBoolean(getString(R.string.srgb_status), false)?"SRGB On":"SRGB Off");
                }
            }
        });

        selADAPT.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Utilties.set_adaption_status(true);
                Utilties.update_status(getApplicationContext());
                selADAPT.setChecked(sharedPreferences.getBoolean(getString(R.string.adaption_status), false));
                //mViewShow3.setText(sharedPreferences.getBoolean(getString(R.string.srgb_status), false)?"SRGB On":"SRGB Off");
                selDCIP3.setChecked(sharedPreferences.getBoolean(getString(R.string.dci_p3_status), false));
                selSRGB.setChecked(sharedPreferences.getBoolean(getString(R.string.srgb_status), false));
                selOPMODE.setChecked(sharedPreferences.getBoolean(getString(R.string.oneplus_status), false));
                //mViewShow2.setText(sharedPreferences.getBoolean(getString(R.string.dci_p3_status), false)?"DCI P3 On":"DCI P3 Off");
            } else {
                if(Utilties.get_adaption_status()) {
                    Utilties.set_srgb_status(false);
                    Utilties.update_status(getApplicationContext());
                    selADAPT.setChecked(sharedPreferences.getBoolean(getString(R.string.adaption_status), false));
                    //selSRGB.setText(sharedPreferences.getBoolean(getString(R.string.srgb_status), false)?"SRGB On":"SRGB Off");
                }
            }
        });

        selOPMODE.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Utilties.set_opmode_status(true);
                Utilties.update_status(getApplicationContext());
                selOPMODE.setChecked(sharedPreferences.getBoolean(getString(R.string.oneplus_status), false));
                //mViewShow3.setText(sharedPreferences.getBoolean(getString(R.string.srgb_status), false)?"SRGB On":"SRGB Off");
                selDCIP3.setChecked(sharedPreferences.getBoolean(getString(R.string.dci_p3_status), false));
                selSRGB.setChecked(sharedPreferences.getBoolean(getString(R.string.srgb_status), false));
                selADAPT.setChecked(sharedPreferences.getBoolean(getString(R.string.adaption_status), false));
                //mViewShow2.setText(sharedPreferences.getBoolean(getString(R.string.dci_p3_status), false)?"DCI P3 On":"DCI P3 Off");
            } else {
                if(Utilties.get_opmode_status()) {
                    Utilties.set_srgb_status(false);
                    Utilties.update_status(getApplicationContext());
                    selOPMODE.setChecked(sharedPreferences.getBoolean(getString(R.string.oneplus_status), false));
                    //selSRGB.setText(sharedPreferences.getBoolean(getString(R.string.srgb_status), false)?"SRGB On":"SRGB Off");
                }
            }
        });
        setWithBoot.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                editor.putBoolean(getString(R.string.auto_run_status),true);
            }else{
                editor.putBoolean(getString(R.string.auto_run_status),false);
            }
            editor.apply();
        });
        run = true;
        handler.postDelayed(task, 500);
    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (run) {
                selOPMODE.setChecked(sharedPreferences.getBoolean(getString(R.string.oneplus_status), false));
                //mViewShow3.setText(sharedPreferences.getBoolean(getString(R.string.srgb_status), false)?"SRGB On":"SRGB Off");
                selDCIP3.setChecked(sharedPreferences.getBoolean(getString(R.string.dci_p3_status), false));
                selSRGB.setChecked(sharedPreferences.getBoolean(getString(R.string.srgb_status), false));
                selADAPT.setChecked(sharedPreferences.getBoolean(getString(R.string.adaption_status), false));
                //mViewShow2.setText(sharedPreferences.getBoolean(getString(R.string.dci_p3_status), false)?"DCI P3 On":"DCI P3 Off");
                setWithBoot.setChecked(sharedPreferences.getBoolean(getString(R.string.auto_run_status), false));
                mSwitch.setChecked(sharedPreferences.getBoolean(getString(R.string.dc_status), false));
                handler.postDelayed(this, 500);
            }
        }
    };

    private void showInput() {
        final RegionNumberEditText editText = new RegionNumberEditText(this);
        Log.d(LOG_TAG, Build.DEVICE);
        if("OnePlus5".equals(Build.DEVICE)) {
            editText.setRegion(255, 1);
        }else {
            editText.setRegion(1023, 1);
        }
        editText.setTextWatcher();
        builder = new AlertDialog.Builder(this).setTitle("设置机器DC调光最小亮度,当前为:"+sharedPreferences.getInt(getString(R.string.elvss_min),66)).setView(editText)
                .setPositiveButton("Set", (dialogInterface, i) -> {
                    if(TextUtils.isEmpty(editText.getText())){
                        Toast.makeText(getApplicationContext(), "输入不得为空", Toast.LENGTH_LONG).show();
                    }else {
                        Utilties.set_elvss_num(Integer.valueOf(editText.getText().toString()));
                        Utilties.update_status(getApplicationContext());
                    }
                });
        builder.create().show();
    }


}
