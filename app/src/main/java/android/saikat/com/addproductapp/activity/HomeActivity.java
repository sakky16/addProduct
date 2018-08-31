package android.saikat.com.addproductapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.saikat.com.addproductapp.R;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;


public class HomeActivity extends AppCompatActivity {
    Button add_product_btn, view_product_btn;
    private final String[] permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,};
    private static final int ALL_3_PERMISSIONS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWidgets();
        askPermissions();
        setListener();

    }
    private void askPermissions() {
        if ((ActivityCompat.checkSelfPermission(HomeActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(HomeActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED)
                ) {
            //requestPermissions(permissions, ALL_5_PERMISSIONS);
            ActivityCompat.requestPermissions(HomeActivity.this, permissions, ALL_3_PERMISSIONS);
        }

    }

    private void getWidgets() {
        add_product_btn = (Button) findViewById(R.id.add_product_btn);
        view_product_btn = (Button) findViewById(R.id.view_product_btn);

    }




    private void setListener() {
        add_product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddProductActivity();

            }
        });
        view_product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToViewProductActivity();
            }
        });
    }

    private void goToAddProductActivity() {
        startActivity(new Intent(HomeActivity.this, AddProductActivity.class));

    }

    private void goToViewProductActivity() {
        startActivity(new Intent(HomeActivity.this, ViewProductActivity.class));

    }
}
