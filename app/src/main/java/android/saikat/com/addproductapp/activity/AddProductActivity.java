package android.saikat.com.addproductapp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.saikat.com.addproductapp.R;
import android.saikat.com.addproductapp.Utils.DataBaseManager;
import android.saikat.com.addproductapp.database.Product;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity  {
    private ImageView camera1_iv,camera2_iv;
    private final String[] permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,};
    private static final int ALL_3_PERMISSIONS = 100;
    private boolean isPermissionGiven=false;
    private static final int SELECT_FILE1 = 100;
    private static final int SELECT_FILE2 = 101;
    private TextInputEditText productName_et,price_et;
    private static final int CAMERA_IMAGE_REQUEST1 = 1001;
    private static final int CAMERA_IMAGE_REQUEST2 = 1002;
    private Button add_product_btn;
    private String productImageUrl1 = "", productImageUrl2 = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        getWidgets();
        setListener();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }
    private void getWidgets(){
        camera1_iv=(ImageView)findViewById(R.id.camera1_iv);
        camera2_iv=(ImageView)findViewById(R.id.camera2_iv);
        productName_et=(TextInputEditText)findViewById(R.id.product_name_et);
        price_et=(TextInputEditText)findViewById(R.id.price_et);
        add_product_btn=(Button)findViewById(R.id.add_product_btn);
    }

    private void askPermissions() {
        if ((ActivityCompat.checkSelfPermission(AddProductActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(AddProductActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED)
                ) {
            //requestPermissions(permissions, ALL_5_PERMISSIONS);
            ActivityCompat.requestPermissions(AddProductActivity.this, permissions, ALL_3_PERMISSIONS);
        }
        else {
            isPermissionGiven=true;
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case ALL_3_PERMISSIONS:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //Toast.makeText(RecordAttendanceActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
                    }
                }

        }
    }
    private void captureImageOrSelectImage(final String imageType) {
        final CharSequence[] items = {"Select Camera", "From Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddProductActivity.this);
        builder.setTitle("Choose image option");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Select Camera")) {
                    setIntentForCaptureImage(imageType);

                } else if (items[item].equals("From Gallery")) {
                    if (imageType.equalsIgnoreCase("image1")) {
                        setIntentForSelectImage(imageType, SELECT_FILE1);
                    } else if (imageType.equalsIgnoreCase("image2")) {
                        setIntentForSelectImage(imageType, SELECT_FILE2);
                    }
                }
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    private void setIntentForSelectImage(String imageType, int requestCode) {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent, "Select File"),
                requestCode);
    }
    public void showImage(String imageUrl, ImageView imageView) {
        Glide.with(AddProductActivity.this).load(imageUrl)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

    }
    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_IMAGE_REQUEST1){
            if(resultCode==RESULT_OK){
                showImage(productImageUrl1, camera1_iv);
            }
            else if(resultCode==RESULT_CANCELED){
                productImageUrl1="";
            }

        } else if (requestCode == CAMERA_IMAGE_REQUEST2) {
            if(resultCode==RESULT_OK){
                showImage(productImageUrl2, camera2_iv);
            }
            else if(resultCode==RESULT_CANCELED){
                productImageUrl2="";
            }

        }  else if (requestCode == SELECT_FILE1 && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            productImageUrl1 = getPath(selectedImageUri);
            showImage(productImageUrl1, camera1_iv);
        } else if (requestCode == SELECT_FILE2 && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            productImageUrl2 = getPath(selectedImageUri);
            showImage(productImageUrl2, camera2_iv);
        }
    }

    private void setIntentForCaptureImage(String imageType) {
        String imageFolderPath = Environment.getExternalStorageDirectory().toString()
                + "/productImage";
        File imagesFolder = new File(imageFolderPath);
        imagesFolder.mkdirs();

        // Generating file name
        String imageName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".png";
        File imageFile = new File(imageFolderPath, imageName);
        if (imageType.equalsIgnoreCase("image1")) {
            productImageUrl1 = imageFile.getAbsolutePath();
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            startActivityForResult(takePictureIntent,
                    CAMERA_IMAGE_REQUEST1);
        } else if (imageType.equalsIgnoreCase("image2")) {
            productImageUrl2 = imageFile.getAbsolutePath();
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            startActivityForResult(takePictureIntent,
                    CAMERA_IMAGE_REQUEST2);
        }

    }
    private void setListener(){
        camera1_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productImageUrl1.equalsIgnoreCase("")){
                    askPermissions();
                    if(isPermissionGiven){
                        captureImageOrSelectImage("image1");
                    }
                }

            }
        });
        camera2_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productImageUrl2.equalsIgnoreCase("")){
                    askPermissions();
                    if(isPermissionGiven){
                        captureImageOrSelectImage("image2");
                    }
                }

            }
        });
        add_product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName=productName_et.getText().toString();
                String price=price_et.getText().toString();
                if(productName.equalsIgnoreCase("")){
                    Toast.makeText(AddProductActivity.this,"Please enter product name",Toast.LENGTH_SHORT).show();
                }
                else if(price.equalsIgnoreCase("")){
                    Toast.makeText(AddProductActivity.this,"Please enter some price",Toast.LENGTH_SHORT).show();
                }
                else if(productImageUrl1.equalsIgnoreCase("") && productImageUrl2.equalsIgnoreCase("")){
                    Toast.makeText(AddProductActivity.this,"Please take atleast one picture for this Product",Toast.LENGTH_SHORT).show();
                }
                else {
                    Product product=new Product();
                    product.setId(UUID.randomUUID().toString());
                    product.setProductName(productName);
                    product.setPrice(Integer.parseInt(price));
                    product.setImageUrl1(productImageUrl1);
                    product.setImageUrl2(productImageUrl2);
                    DataBaseManager.getInstance().insertProduct(product);
                    Toast.makeText(AddProductActivity.this,"Product added successfully",Toast.LENGTH_SHORT).show();
                    finish();

                }
            }
        });

    }
}
