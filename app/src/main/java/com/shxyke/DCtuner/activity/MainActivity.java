package com.shxyke.DCtuner.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.shxyke.DCtuner.Contents;
import com.shxyke.DCtuner.R;
import com.shxyke.DCtuner.adapter.ModeAdapter;
import com.shxyke.DCtuner.base.BaseActivity;
import com.shxyke.DCtuner.utils.SPUtils;
import com.shxyke.DCtuner.utils.StatusBarColorUtils;
import com.shxyke.DCtuner.widget.CustomizeDialog;
import com.shxyke.DCtuner.widget.RegionNumberEditText;
import com.shxyke.DCtuner.helper.Utilties;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private Switch setWithBoot;
    private TextView text;
    private FloatingActionButton fab;
    private boolean run = false;
    private boolean isOpen = false;
    private final Handler handler = new Handler();

    @Override
    public int setActivityView() {
        StatusBarColorUtils.setStatusBarDarkIcon(this, true);
        return R.layout.activity_main;
    }

    @Override
    public void initActivity() {
        RecyclerView recyclerView = findViewById(R.id.rv_mode);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> list = new ArrayList<>();
        list.add("SRGB");
        list.add("DCI P3");
        list.add("自适应");
        list.add("OnePlus");
        ModeAdapter adapter = new ModeAdapter(this, list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClick(pos -> {
            switch (pos) {
                case 0:
                    Utilties.set_srgb_status(true);
                    Utilties.update_status();
                    break;
                case 1:
                    Utilties.set_dci_p3_status(true);
                    Utilties.update_status();
                    break;
                case 2:
                    //
                    break;
                case 3:
                    //
                    break;
            }
        });

        findViewById(R.id.btn_set).setOnClickListener(v -> showInput());
        NestedScrollView linearLayout = findViewById(R.id.ll_bottom_sheet);
        findViewById(R.id.card_set_mode).setOnClickListener(v -> {
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
            bottomSheetBehavior.setPeekHeight(1200, true);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });

        setWithBoot = findViewById(R.id.switch_run_boot);
        text = findViewById(R.id.textView2);
        fab= findViewById(R.id.floatingActionButton);
        fab.getDrawable().setTint(Color.WHITE);

        if(Utilties.get_elvss_num()!= (Integer) SPUtils.getData(Contents.SP_ELVSS_MIN_KEY,66)){
            Utilties.set_elvss_num((Integer) SPUtils.getData(Contents.SP_ELVSS_MIN_KEY,66));
        }
        Utilties.update_status();

        isOpen = (boolean)SPUtils.getData(Contents.SP_DC_DIMMING_STATUS_KEY, false);

        setWithBoot.setChecked((boolean)SPUtils.getData(Contents.SP_ON_BOOT_KEY, false));
        fab.setOnClickListener(v -> {
            isOpen = !isOpen;
            Utilties.set_dc_status(isOpen);
            Utilties.update_status();
            int drawable = isOpen ? R.drawable.ic_visibility_24px : R.drawable.ic_visibility_off_24px;
            fab.setImageDrawable(getDrawable(drawable));
            fab.getDrawable().setTint(Color.WHITE);
        });

        setWithBoot.setOnCheckedChangeListener((buttonView, isChecked) -> SPUtils.putData(Contents.SP_ON_BOOT_KEY,isChecked));
        run = true;
        handler.postDelayed(task, 500);
    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            String toShow = "";
            boolean dcStatus;
            boolean srgbStatus;
            boolean dcip3Status;
            boolean setBoot;
            if (run) {
                dcStatus = (boolean)SPUtils.getData(Contents.SP_DC_DIMMING_STATUS_KEY, false);
                dcip3Status = (boolean)SPUtils.getData(Contents.SP_DCIP3_MODE_STATUS_KEY, false);
                srgbStatus = (boolean)SPUtils.getData(Contents.SP_SRGB_MODE_STATUS_KEY, false);
                setBoot = (boolean)SPUtils.getData(Contents.SP_ON_BOOT_KEY, false);
                //mSwitch.setChecked(dcStatus);
                //selDCIP3.setChecked(dcip3Status);
                //selSRGB.setChecked(srgbStatus);
                //selOPMODE.setChecked(sharedPreferences.getBoolean(getString(R.string.oneplus_status), false));
                //selADAPT.setChecked(sharedPreferences.getBoolean(getString(R.string.adaption_status), false));
                setWithBoot.setChecked(setBoot);
                toShow += dcStatus? "DC调光: ON\n":"DC调光: OFF\n";
                if(dcip3Status) {
                    toShow += "颜色模式: DCI P3(广色域模式)\n";
                } else if (srgbStatus) {
                    toShow += "颜色模式: SRGB模式\n";
                } else {
                    toShow += "颜色模式: 未设置\n";
                }
                toShow += setBoot?"开机自启: ON\n":"开机自启: OFF\n";
                toShow += "当前最小DC亮度: " + SPUtils.getData(Contents.SP_ELVSS_MIN_KEY,66);
                text.setText(toShow);
                handler.postDelayed(this, 500);
            }
        }
    };

    @SuppressLint("SetTextI18n")
    private void showInput() {
        @SuppressLint("InflateParams")
        View dialogLayout = getLayoutInflater().inflate(R.layout.layout_input_elvss, null);
        RegionNumberEditText editText = dialogLayout.findViewById(R.id.et_elvss);
        TextView textView = dialogLayout.findViewById(R.id.tv_elvss);
        if("OnePlus5".equals(Build.DEVICE)) {
            editText.setRegion(255, 1);
        } else {
            editText.setRegion(1023, 1);
        }
        editText.setTextWatcher();
        textView.setText("设置机器DC调光最小亮度,当前为:"+SPUtils.getData(Contents.SP_ELVSS_MIN_KEY,66));
        CustomizeDialog.getInstance(this)
                .setTitle("提示")
                .setView(dialogLayout)
                .setPositiveButton("确定", (dialog, which) -> {
                    if(TextUtils.isEmpty(editText.getText())){
                        Snackbar.make(findViewById(R.id.bottom_app_bar), "输入不得为空!", Snackbar.LENGTH_SHORT).show();
                    }else {
                        Utilties.set_elvss_num(Integer.parseInt(editText.getText().toString()));
                        Utilties.update_status();
                    }
                }).create().show();
    }

}
