package android.saikat.com.addproductapp.adapter;

import android.content.Context;
import android.saikat.com.addproductapp.R;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;

import java.util.List;

/**
 * Created by trisys on 22/6/18.
 */

public class ImgaeViewPagerAdapter extends PagerAdapter {
    Context context;
    ViewPager viewPager;
    LayoutInflater layoutInflater;
    List<String> imageList;

    ImgaeViewPagerAdapter(Context context,List<String> imageList,ViewPager viewPager){
        this.context=context;
        this.imageList=imageList;
        this.viewPager=viewPager;
        layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView=layoutInflater.inflate(R.layout.view_pager_item,container,false);
        ImageView product_iv=(ImageView)itemView.findViewById(R.id.imageView);
        final DotProgressBar progressBar=(DotProgressBar) itemView.findViewById(R.id.dot_progress_bar);
        String imagePath=imageList.get(position);
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(context).load(imagePath)
                .thumbnail(0.6f)
                .crossFade()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        if(e!=null){
                            String error=e.toString();
                        }
                        progressBar.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })

                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(product_iv);
        product_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int imageCount=viewPager.getAdapter().getCount();
                int tab=viewPager.getCurrentItem();
                if(tab==0){
                    viewPager.setCurrentItem(1);
                }
                else if(tab==1){
                    viewPager.setCurrentItem(0);
                }
                int tab1=tab;
            }
        });
        container.addView(itemView);
        return itemView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}
