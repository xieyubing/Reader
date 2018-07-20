package com.xb.reader.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xb.reader.R;
import com.xb.reader.bean.Book;

import java.util.List;

/**
 * Created by asus on 2017/7/26.
 */

public class Book_ListAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Book> list;

    public Book_ListAdapter(Context context, List<Book> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_list_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  ViewHolder){
            ((ViewHolder) holder).tvbookname.setText(list.get(position).getBookname());
        }
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvbookname;

        public ViewHolder(View itemView) {
            super(itemView);
            tvbookname = (TextView) itemView.findViewById(R.id.tv_bookname);
        }
    }
}
