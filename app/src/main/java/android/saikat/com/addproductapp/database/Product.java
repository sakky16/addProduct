package android.saikat.com.addproductapp.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by trisys on 22/6/18.
 */
@Entity
public class Product {
    private String productName;
    private String imageUrl1;
    private String imageUrl2;
    @Id
    private String id;
    private int price;
    @Generated(hash = 412872585)
    public Product(String productName, String imageUrl1, String imageUrl2,
            String id, int price) {
        this.productName = productName;
        this.imageUrl1 = imageUrl1;
        this.imageUrl2 = imageUrl2;
        this.id = id;
        this.price = price;
    }
    @Generated(hash = 1890278724)
    public Product() {
    }
    public String getProductName() {
        return this.productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getImageUrl1() {
        return this.imageUrl1;
    }
    public void setImageUrl1(String imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }
    public String getImageUrl2() {
        return this.imageUrl2;
    }
    public void setImageUrl2(String imageUrl2) {
        this.imageUrl2 = imageUrl2;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getPrice() {
        return this.price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

}
