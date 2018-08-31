package android.saikat.com.addproductapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.saikat.com.addproductapp.R;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {
    ImageView splash_iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        splash_iv=(ImageView)findViewById(R.id.splash_iv);
        showScreen();
    }

    private void showScreen() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        };
        Timer t = new Timer();
        t.schedule(task, 2000);
    }

}
