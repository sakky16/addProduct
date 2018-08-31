package android.saikat.com.addproductapp.activity;

import android.os.AsyncTask;
import android.saikat.com.addproductapp.R;
import android.saikat.com.addproductapp.Utils.DataBaseManager;
import android.saikat.com.addproductapp.adapter.ProductRecyclerAdapter;
import android.saikat.com.addproductapp.database.Product;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;

import java.util.LinkedList;
import java.util.List;

public class ViewProductActivity extends AppCompatActivity {
    RecyclerView prod_rv;
    ImageView back_iv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Product> products;
    private TextView no_product_tv;
    public ProgressBar progressBar;
    private static final int MAX_ITEMS_PER_REQUEST = 20;
    private static final int NUMBER_OF_ITEMS = 100;
    private static final int SIMULATED_LOADING_TIME_IN_MS = 1500;
    ProductRecyclerAdapter adapter;
    private int page;
    LinearLayoutManager linearLayoutManagaer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        getWidgets();
        getList();
        setAdapter();
        setListener();
    }

    private void getWidgets(){
        prod_rv=(RecyclerView)findViewById(R.id.prod_rv);
        back_iv=(ImageView)findViewById(R.id.back_iv);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        no_product_tv=(TextView)findViewById(R.id.no_product_tv);
    }

    private void getList(){
        products= DataBaseManager.getInstance().getAllProductList();

    }
    private void setListener(){
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void setAdapter(){
        if(products!=null && products.size()>0){
            prod_rv.setVisibility(View.VISIBLE);
            no_product_tv.setVisibility(View.GONE);
            linearLayoutManagaer=new LinearLayoutManager(this);
            adapter=new ProductRecyclerAdapter(ViewProductActivity.this,products);
            prod_rv.setHasFixedSize(true);
            prod_rv.setLayoutManager(linearLayoutManagaer);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(prod_rv.getContext(),
                    DividerItemDecoration.VERTICAL);
            prod_rv.addItemDecoration(dividerItemDecoration);
            prod_rv.setAdapter(adapter);
            prod_rv.addOnScrollListener(createInfiniteScrollListener());
        }
        else {
            prod_rv.setVisibility(View.GONE);
            no_product_tv.setVisibility(View.VISIBLE);
        }

    }

    @NonNull
    private InfiniteScrollListener createInfiniteScrollListener() {
        return new InfiniteScrollListener(MAX_ITEMS_PER_REQUEST, linearLayoutManagaer) {
            @Override public void onScrolledToEnd(final int firstVisibleItemPosition) {
                simulateLoading();
                int start = ++page * MAX_ITEMS_PER_REQUEST;
                final boolean allItemsLoaded = start >= products.size();
                if (allItemsLoaded) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    int end = start + MAX_ITEMS_PER_REQUEST;
                    if(end>products.size()){
                        end=products.size()-1;
                    }

                    final List<Product> itemsLocal = getItemsToBeLoaded(start, end);
                    refreshView(prod_rv, new ProductRecyclerAdapter(ViewProductActivity.this,itemsLocal), firstVisibleItemPosition);
                }
            }
        };
    }

    @NonNull private List<Product> getItemsToBeLoaded(int start, int end) {
        List<Product> newItems = products.subList(start, end);
        final List<Product> oldItems = ((ProductRecyclerAdapter) prod_rv.getAdapter()).getItems();
        final List<Product> itemsLocal = new LinkedList<>();
        itemsLocal.addAll(oldItems);
        for(int i=0;i<newItems.size();i++){
            if(!itemsLocal.contains(newItems.get(i))){
                itemsLocal.add(itemsLocal.get(i));
            }
        }
        //itemsLocal.addAll(newItems);
        return itemsLocal;
    }
    private void simulateLoading() {
        new AsyncTask<Void, Void, Void>() {
            @Override protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(SIMULATED_LOADING_TIME_IN_MS);
                } catch (InterruptedException e) {
                    Log.e("MainActivity", e.getMessage());
                }
                return null;
            }

            @Override protected void onPostExecute(Void param) {
                progressBar.setVisibility(View.GONE);
            }
        }.execute();
    }


}

