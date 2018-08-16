package com.example.xrecyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;

public class LinearActivity extends AppCompatActivity {
    private int times = 0;
    private ArrayList<String> list = new ArrayList<>();
    private CommonAdapter<String> adapter;
    private XRecyclerView xRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        xRecyclerView = findViewById(R.id.xRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        xRecyclerView.setLayoutManager(layoutManager);

        initData(list, "init");

        View header = LayoutInflater.from(this).inflate(R.layout.header, (ViewGroup) findViewById(android.R.id.content), false);
        xRecyclerView.addHeaderView(header);

        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.getDefaultFootView().setLoadingHint("加载中...");
        xRecyclerView.getDefaultFootView().setLoadingDoneHint("加载完成");
        xRecyclerView.getDefaultFootView().setNoMoreHint("没有更多");

        xRecyclerView.setLimitNumberToCallLoadMore(2);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {

            @Override
            public void onLoadMore() {
                if (times < 2) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            initData(list, "more");
                            xRecyclerView.loadMoreComplete();
                            adapter.notifyDataSetChanged();
                        }
                    }, 1000);

                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            initData(list, "nomore");
                            xRecyclerView.setNoMore(true);
                            adapter.notifyDataSetChanged();

                        }
                    }, 1000);
                }
                times++;

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
            }
        });

        adapter = new CommonAdapter<String>(this, R.layout.item, list) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.tv, s);
            }
        };
        xRecyclerView.setAdapter(adapter);

    }

    private void initData(ArrayList<String> list, String tag) {
        int itemLimit = 8;
        for (int i = 0; i < itemLimit; i++) {
            list.add("item-" + tag + i);
        }
    }

    @Override
    protected void onDestroy() {
        if (xRecyclerView != null) {
            xRecyclerView.destroy();
            xRecyclerView = null;
        }
        super.onDestroy();
    }
}
