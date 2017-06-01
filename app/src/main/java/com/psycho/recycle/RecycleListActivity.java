package com.psycho.recycle;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.psycho.ApiService;
import com.psycho.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static me.xiaopan.android.widget.ToastUtils.toastS;

public class RecycleListActivity extends AppCompatActivity implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.rv_list)
    RecyclerView rvList;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @Bind(R.id.activity_recycle_list)
    RelativeLayout activityRecycleList;

    private int pageIndex =1;

    private int delayMillis = 1000;

    DateEntity entity= new DateEntity();

    RecycleAdapter adapter;

    private static final int pageSize = 10;
    private int listSize;

    private boolean isError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_list);
        ButterKnife.bind(this);

        initEvent();
        ApiGetDate();

    }

    private void initEvent() {
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        rvList.setLayoutManager(new LinearLayoutManager(this));

    }


    private void ApiGetDate(){

        /**
         * 增加header
         */
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Authorization", "218930B9BF4137DEC7A32643525D0DE62D7C2818E08FF3FC4B1831056C28D39D52BC953098ABC0E554013AB265B00F838CA3B9396303B123F2C60764D57EC1C1")
//                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        }).addNetworkInterceptor(new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.HEADERS));


        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://LawPa3.nbxuanma.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        ApiService api = retrofit.create(ApiService.class);
        Map<String,String> params = new HashMap<>();
        params.put("pageIndex", pageIndex+"");
        params.put("status", 0+"");
        params.put("region", "-1");
        params.put("coTypeID", "-1");

        Call<DateEntity> call = api.getList(params);
        call.enqueue(new Callback<DateEntity>() {
            @Override
            public void onResponse(Call<DateEntity> call, Response<DateEntity> response) {
                Logger.json(new Gson().toJson(response.body()));
                entity = response.body();
                if (entity.getStatus()==1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setUi();
                        }
                    });
                }else {
//                    toastS(RecycleListActivity.this,entity.getResult());
                }
            }

            @Override
            public void onFailure(Call<DateEntity> call, Throwable t) {
                isError = true;
            }
        });

    }

    private void setUi() {
        listSize = entity.getResult().size();
        if (null== adapter){
            adapter = new RecycleAdapter(R.layout.item_item,entity.getResult());
            adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
            adapter.setOnLoadMoreListener(this);
            rvList.setAdapter(adapter);
        }else {
            if (pageIndex==1){
                adapter.setNewData(entity.getResult());
                swipeLayout.setRefreshing(false);
                adapter.setEnableLoadMore(true);
            }else {
                adapter.setEnableLoadMore(true);
                if (isError){
                    isError = false;
                    adapter.loadMoreFail();
                }else {
                    Log.e("TAG", "listSize==" + listSize);
                    if (listSize==0){
                        adapter.loadMoreComplete();
                    }else {
                        adapter.addData(entity.getResult());
                    }
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        adapter.setEnableLoadMore(false);
        pageIndex=1;
        ApiGetDate();
    }

    @Override
    public void onLoadMoreRequested() {
        rvList.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (adapter.getData().size()<pageSize){
                    adapter.loadMoreEnd(true);
                }else {
                    pageIndex++;
                    ApiGetDate();
                }
            }
        },delayMillis);

    }
}
