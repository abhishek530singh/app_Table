package com.example.tableapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

public class MainActivity extends AppCompatActivity {
    public static int UPDATE_CODE = 22;
    AppUpdateManager appUpdateManager;


    TextView textView2;
    EditText input;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView2=findViewById(R.id.textView2);
        input=findViewById(R.id.input);
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number=input.getText().toString();
                int n=Integer.parseInt(number);
                String result="";
                for(int i=1;i<11;i++)
                {
                    result+= n + "  X  "+i +" = "+" "+n*i+"\n";
                    textView2.setText(result);
                }
            }
        });

      inAppUp();
    }

    private void inAppUp()
    {
        appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> task = appUpdateManager.getAppUpdateInfo();
        task.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability()== UpdateAvailability.UPDATE_AVAILABLE &&
                        appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)){
                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE,
                                MainActivity.this, UPDATE_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                        Log.d("updateerror", "onSuccess: "+e.toString());
                    }
                }
            }
        });
        appUpdateManager.registerListener(listener);
    }
    InstallStateUpdatedListener listener= installState -> {
        if(installState.installStatus() == InstallStatus.DOWNLOADED){
            popUp();
        }
    };

    private void popUp() {
        Snackbar snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                "App Update Almost Done", Snackbar.LENGTH_INDEFINITE
        );
        snackbar.setAction("Reload", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setTextColor(Color.parseColor("#FF0000"));
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == UPDATE_CODE){
            if(resultCode != RESULT_OK){

            }
        }
    }
    }



