package android.saikat.com.addproductapp.adapter;

import android.content.Context;
import android.saikat.com.addproductapp.R;
import android.saikat.com.addproductapp.database.Product;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trisys on 22/6/18.
 */

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.MyViewHolder> {
    LayoutInflater inflater;
    private Context context;
    ImgaeViewPagerAdapter adapter;
    List<Product> products;


    public ProductRecyclerAdapter(Context context, List<Product> productList){
        this.context=context;
        this.products=productList;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.product_list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        List<String> imageList=new ArrayList<>();
        holder.product_name_tv.setText(products.get(position).getProductName());
        holder.price_tv.setText("Price: \u20B9"+Integer.toString(products.get(position).getPrice()));
        setViewPagerAdapter(products.get(position).getImageUrl1(),products.get(position).getImageUrl2(),holder.viewPager,holder.prodctdotsPanel);
        holder.viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hello="hello";
            }
        });

    }

    public void setViewPagerAdapter(String image1,String imag2,ViewPager viewPager,LinearLayout dotPanel){
        List<String> imageList=new ArrayList<>();
        if(image1!=null && !image1.equalsIgnoreCase("")){
            imageList.add(image1);
        }
        if(imag2!=null && !imag2.equalsIgnoreCase("")) {
            imageList.add(imag2);
        }
        adapter=new ImgaeViewPagerAdapter(context,imageList,viewPager);
        viewPager.setAdapter(adapter);
        final int dotsCount;
        dotsCount=2;
        final ImageView[] dots;
        dots=new ImageView[dotsCount];
        if(imageList.size()>1){
            dotPanel.removeAllViews();
            for(int i=0;i<=1;i++){
                dots[i]=new ImageView(context);
                dots[i].setImageDrawable(ContextCompat.getDrawable(context,R.drawable.page_indicator_unselected));
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(20,20);
                params.setMargins(15,0,15,0);
                dotPanel.addView(dots[i],params);

            }
            dots[0].setImageDrawable(ContextCompat.getDrawable(context,R.drawable.page_indicator_selected));
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i=0;i<dotsCount;i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(context,R.drawable.page_indicator_unselected));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(context,R.drawable.page_indicator_selected));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    public List<Product> getItems() {
        return products;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ViewPager viewPager;
        LinearLayout prodctdotsPanel;
        TextView product_name_tv,price_tv;



        public MyViewHolder(View itemView) {
            super(itemView);
            viewPager=(ViewPager)itemView.findViewById(R.id.product_view_pager);
            prodctdotsPanel=(LinearLayout)itemView.findViewById(R.id.slider_dots);
            product_name_tv=(TextView)itemView.findViewById(R.id.product_name_tv);
            price_tv=(TextView)itemView.findViewById(R.id.price_tv);

        }
    }
}
