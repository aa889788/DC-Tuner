package com.shxyke.DCtuner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class start_activity extends AppCompatActivity {
    private TextView text1;
    private String text_show = "";
    private String haveRootPermission = "haveRootPermission";
    private String isFirstRun = "isFirstRun";
    private Button next_step;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_activity);
        sharedPreferences = getSharedPreferences("share", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        boolean isfirstrun = sharedPreferences.getBoolean(isFirstRun, true);
        boolean have_rootpermission = sharedPreferences.getBoolean(haveRootPermission, false);
        editor.putBoolean(isFirstRun, true);
        editor.apply();
        Intent intent = new Intent(start_activity.this,MainActivity.class);
        if(have_rootpermission){
            basic_com.update_status(this);
            startActivity(intent);
            finish();
        }else{
            if(isfirstrun){
                text_show += getString(R.string.FirstRun);
            }
            text_show += getString(R.string.GiveRoot);
        }
        next_step = findViewById(R.id.button2);
        text1 = (TextView) findViewById(R.id.textView2);
        next_step.setEnabled(false);
        text1.setText(text_show);
    }

    public void GetRoot(View view){
        boolean ifroot = basic_com.haveRoot();
        editor=sharedPreferences.edit();
        if(ifroot){
            editor.putBoolean(haveRootPermission, true);
            editor.apply();
            text_show += "\n获取Root权限成功！";
            text1.setText(text_show);
            next_step.setEnabled(true);
        }else{
            text_show += "\n获取Root权限失败！";
            text1.setText(text_show);
        }
    }

    public void NextStep(View view){

        Intent intent = new Intent(start_activity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
