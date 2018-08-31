package android.saikat.com.addproductapp;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

/**
 * Created by trisys on 22/6/18.
 */

public class ProductApplication extends MultiDexApplication {

    private static Context  applicationContext;
    public static Context getContext(){
        return applicationContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (applicationContext == null) {
            applicationContext = this;
        }
    }
}
