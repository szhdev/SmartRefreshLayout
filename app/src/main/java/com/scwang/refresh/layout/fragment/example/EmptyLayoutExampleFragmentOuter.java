package com.scwang.refresh.layout.fragment.example;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.refresh.layout.R;
import com.scwang.refresh.layout.activity.FragmentActivity;
import com.scwang.refresh.layout.adapter.BaseRecyclerAdapter;
import com.scwang.refresh.layout.adapter.SmartViewHolder;
import com.scwang.refresh.layout.fragment.example.EmptyLayoutExampleFragment.Item;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.Arrays;

import static android.R.layout.simple_list_item_2;
import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;
import static com.scwang.refresh.layout.R.id.recyclerView;
import static com.scwang.refresh.layout.R.id.refreshLayout;

/**
 * 使用示例-空布页面
 * A simple {@link Fragment} subclass.
 */
public class EmptyLayoutExampleFragmentOuter extends Fragment implements AdapterView.OnItemClickListener, OnRefreshListener {

    private View mEmptyLayout;
    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_example_emptylayout_outer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        final Toolbar toolbar = root.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> requireActivity().finish());

        mRefreshLayout = root.findViewById(refreshLayout);
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(getContext()).setSpinnerStyle(SpinnerStyle.FixedBehind).setPrimaryColorId(R.color.colorPrimary).setAccentColorId(android.R.color.white));
        mRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = root.findViewById(recyclerView);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(root.getContext(), VERTICAL));

        mEmptyLayout = root.findViewById(R.id.empty);

        ImageView image = root.findViewById(R.id.empty_image);
        image.setImageResource(R.drawable.ic_empty);

        TextView empty = root.findViewById(R.id.empty_text);
        empty.setText("暂无数据点击刷新");

        ((View)empty.getParent()).setOnClickListener(v -> doRefresh(0));

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        doRefresh(2000);
    }

    protected void doRefresh(int delayed) {
        mRefreshLayout.getLayout().postDelayed(() -> {
            mRecyclerView.setAdapter(new BaseRecyclerAdapter<Item>(Arrays.asList(Item.values()), simple_list_item_2, EmptyLayoutExampleFragmentOuter.this) {
                @Override
                protected void onBindViewHolder(SmartViewHolder holder, Item model, int position) {
                    holder.text(android.R.id.text1, model.name());
                    holder.text(android.R.id.text2, model.name);
                    holder.textColorId(android.R.id.text2, R.color.colorTextAssistant);
                }
            });
            mRefreshLayout.finishRefresh();
            mEmptyLayout.setVisibility(View.GONE);
        }, delayed);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Item item = Item.values()[position];
        if (Activity.class.isAssignableFrom(item.clazz)) {
            startActivity(new Intent(getContext(), item.clazz));
        } else if (Fragment.class.isAssignableFrom(item.clazz)) {
            FragmentActivity.start(this, item.clazz);
        }
    }
}
