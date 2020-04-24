package com.shxyke.DCtuner.activity;

import android.content.Intent;
import android.os.Build;

import com.shxyke.DCtuner.Contents;
import com.shxyke.DCtuner.R;
import com.shxyke.DCtuner.base.BaseActivity;
import com.shxyke.DCtuner.helper.Utilties;
import com.shxyke.DCtuner.utils.SPUtils;
import com.shxyke.DCtuner.widget.CustomizeDialog;

public class PreStartActivity extends BaseActivity {

    private String text_show = "";

    @Override
    public int setActivityView() {
        return 0;
    }

    @Override
    public void initActivity() {
        if ((boolean) SPUtils.getData(Contents.SP_IS_FIRST_RUN_KEY, true)) {
            text_show += getString(R.string.first_run);
            text_show += "你的机型:" + Build.MODEL + "\n";
            text_show += getString(R.string.grant_root);

            CustomizeDialog.getInstance(this)
                    .setTitle("提示")
                    .setMessage(text_show)
                    .setPositiveButton("获取ROOT", (dialog, which) -> GetRoot())
                    .setNegativeButton("退出", (dialog, which) -> finish())
                    .setCancelable(false)
                    .create().show();
        } else {
            boolean isRoot = Utilties.haveRoot();
            if (!isRoot) {
                text_show += getString(R.string.grant_root);
            } else {
                if (!Utilties.isDCKernel()) {
                    text_show += "你并不是DC内核\n";
                } else {
                    StartMain();
                }
            }
        }
    }

    private void GetRoot() {
        boolean isRoot = Utilties.haveRoot();
        if (isRoot) {
            text_show += "获取Root权限成功！\n";
            boolean isDCKernel = Utilties.isDCKernel();
            if (isDCKernel) {
                if ("OnePlus5".equals(Build.DEVICE)) {
                    Utilties.set_elvss_num(66);
                } else {
                    Utilties.set_elvss_num(302);
                }
                StartMain();
            } else {
                text_show += "但你并不是DC内核\n";
            }
        } else {
            text_show += "获取Root权限失败！\n";
        }
    }

    private void StartMain() {
        Utilties.update_status();
        SPUtils.putData(Contents.SP_IS_FIRST_RUN_KEY, false);
        startActivity(new Intent(PreStartActivity.this, MainActivity.class));
        finish();
    }

}
