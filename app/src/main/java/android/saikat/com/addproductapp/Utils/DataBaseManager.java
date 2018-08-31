package android.saikat.com.addproductapp.Utils;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.saikat.com.addproductapp.ProductApplication;
import android.saikat.com.addproductapp.database.DaoMaster;
import android.saikat.com.addproductapp.database.DaoSession;
import android.saikat.com.addproductapp.database.Product;
import android.saikat.com.addproductapp.database.ProductDao;
import android.widget.LinearLayout;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trisys on 22/6/18.
 */

public class DataBaseManager {
    private static DataBaseManager instance;
    /**
     * The Android activity reference for access to DatabaseManager.
     */

    private DaoMaster.OpenHelper mHelper;
    private SQLiteDatabase database;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    private DataBaseManager() {
        mHelper = new DaoMaster.DevOpenHelper(ProductApplication.getContext(), "product-tb", null);
    }


    public static DataBaseManager getInstance() {
        if (instance == null) {
            instance = new DataBaseManager();
        }
        return instance;
    }


    public void closeDbConnections() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
        if (database != null && database.isOpen()) {
            database.close();
        }
    }


    public void openReadableDb() throws SQLiteException {
        database = mHelper.getReadableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }


    public void openWritableDb() throws SQLiteException {
        database = mHelper.getWritableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();

    }
    public synchronized void insertProduct(Product product) {
        if (product != null) {


        try {
            openWritableDb();
            ProductDao productDao = daoSession.getProductDao();
            productDao.insertOrReplaceInTx(product);
            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }
    public synchronized List<Product> getAllProductList(){
        List<Product> products=new ArrayList<>();
        try {
            openReadableDb();
            ProductDao dao=daoSession.getProductDao();
            WhereCondition condition=ProductDao.Properties.Id.isNotNull();
            QueryBuilder<Product> queryBuilder=dao.queryBuilder().where(condition);
            products=queryBuilder.list();

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return products;
    }

}
