package com.psycho;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    @Bind(R.id.btn)
    TextView btn;
    @Bind(R.id.txt)
    TextView txt;
    @Bind(R.id.btn_next)
    TextView btnNext;
    @Bind(R.id.activity_main)
    RelativeLayout activityMain;

    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sp = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        initEvent();
    }

    private void initEvent() {

    }


    @OnClick({R.id.btn_next, R.id.btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                Intent intent = new Intent(MainActivity.this,TwoActivity.class);
                startActivity(intent);
                break;
            case R.id.btn:
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://beadhouse.nbxuanma.com/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ApiService api = retrofit.create(ApiService.class);

                Call<InfoResponse> call = api.info();
                call.enqueue(new Callback<InfoResponse>() {
                    @Override
                    public void onResponse(Call<InfoResponse> call, Response<InfoResponse> response) {
                        final InfoResponse entity = response.body();
                        Logger.json(new Gson().toJson(entity));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txt.setText(entity.getResult().get(0).getName());
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<InfoResponse> call, Throwable t) {
                        Logger.e(t, t.getMessage());
                    }
                });
                break;
        }
    }
}
