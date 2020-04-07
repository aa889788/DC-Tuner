package com.shxyke.DCtuner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ScrollView;

public class PreStartActivity extends AppCompatActivity {
    private TextView text1;
    private ScrollView scv;
    private String text_show = "";
    private String haveRootPermission = "haveRootPermission";
    private String isFirstRun = "isFirstRun";
    private Button next_step;
    private Button grant_root;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_activity);
        sharedPreferences = getSharedPreferences("share", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        boolean isfirstrun = sharedPreferences.getBoolean(isFirstRun, true);
        boolean have_root;
        editor.putBoolean(isFirstRun, isfirstrun);
        editor.apply();
        Intent intent = new Intent(PreStartActivity.this,MainActivity.class);
        if(isfirstrun){
            text_show += getString(R.string.first_run);
            text_show += getString(R.string.grant_root);
        }
        else {
            have_root = Utilties.haveRoot();
            if(!have_root){
                text_show += getString(R.string.grant_root);
            }
            else {
                Utilties.update_status(this);
                startActivity(intent);
                finish();
            }
        }
        grant_root = findViewById(R.id.button);
        next_step = findViewById(R.id.button2);
        text1 = (TextView) findViewById(R.id.textView);
        scv = (ScrollView) findViewById(R.id.scrollView);
        next_step.setEnabled(false);
        text1.setText(text_show);
    }

    public void GetRoot(View view){
        boolean ifroot = Utilties.haveRoot();
        editor=sharedPreferences.edit();
        if(ifroot){
            text_show += "获取Root权限成功！\n";
            boolean isDCKernel = Utilties.isDCKernel();
            if(isDCKernel){
                editor.putBoolean(isFirstRun, false);
                editor.apply();
                next_step.setEnabled(true);
            }
            else{
                text_show += "但你并不是DC内核\n";
                grant_root.setEnabled(false);
            }
        }else{
            text_show += "获取Root权限失败！\n";
        }
        text1.setText(text_show);
        int offset = text1.getMeasuredHeight() - scv.getMeasuredHeight();
        if (offset < 0) {
            offset = 0;
        }
        scv.scrollTo(0, offset);
    }

    public void NextStep(View view){
        Intent intent = new Intent(PreStartActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
