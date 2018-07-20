package com.xb.reader.views.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xb.reader.R;
import com.xb.reader.bean.Book;
import com.xb.reader.views.activity.PageActivity;
import com.xb.reader.views.adapter.Book_ListAdapter;
import com.xb.reader.views.view.RecyclerViewClickListener;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by asus on 2017/7/18.
 */

public class Fragment_List extends Fragment {

    @BindView(R.id.recyler_view_list_book)
    RecyclerView recylerViewListBook;
    Unbinder unbinder;
    @BindView(R.id.layout_swipe_refresh)
    SwipeRefreshLayout layoutSwipeRefresh;

    private List<Book> lists = new ArrayList<>();
    private Book_ListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pager_book_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        /*layoutSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { //下拉刷新
            @Override
            public void onRefresh() {

            }
        });*/
        lists = DataSupport.findAll(Book.class);
        adapter = new Book_ListAdapter(getActivity(),lists);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recylerViewListBook.setLayoutManager(layoutManager);
        recylerViewListBook.setAdapter(adapter);

        recylerViewListBook.addOnItemTouchListener(new RecyclerViewClickListener(getActivity(),recylerViewListBook, new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i("点击item",position+"");
                Intent intent = new Intent(getActivity(), PageActivity.class);
                intent.putExtra("Id",lists.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(getActivity(),"长按的是"+position,Toast.LENGTH_LONG).show();
            }
        }));

        return view;
    }

    public void upBook(List<Book> books){
        lists.addAll(books);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
