package com.xb.reader.views.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xb.reader.R;
import com.xb.reader.views.adapter.Book_FindAdapter;
import com.xb.reader.views.view.RecyclerViewClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by asus on 2017/7/18.
 */

public class Fragment_Find extends Fragment {

    @BindView(R.id.recyler_view_find_book)
    RecyclerView recylerViewFindBook;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pager_book_find, container, false);
        unbinder = ButterKnife.bind(this, view);
        List<String> list = new ArrayList<>();
        list.add("玄幻");
        list.add("武侠");
        list.add("仙侠");
        list.add("都市");
        list.add("科幻");
        list.add("言情");
        Book_FindAdapter adapter = new Book_FindAdapter(getActivity(),list);
        recylerViewFindBook.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recylerViewFindBook.setAdapter(adapter);

        recylerViewFindBook.addOnItemTouchListener(new RecyclerViewClickListener(getActivity(), recylerViewFindBook, new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(),"点击的是"+position,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(getActivity(),"长按的是"+position,Toast.LENGTH_LONG).show();
            }
        }));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
