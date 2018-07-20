package com.xb.reader.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xb.reader.R;
import com.xb.reader.bean.Book;
import com.xb.reader.util.ConvertDemo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by asus on 2017/8/10.
 */

public class RecordsListBaseAdapter extends BaseAdapter {

    private Context context;
    private List<Book> books;
    private LayoutInflater inflater;

    public RecordsListBaseAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return books == null?0:books.size();
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            convertView = inflater.inflate(R.layout.book_list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvBookname.setText(books.get(position).getBookname());
        viewHolder.tvBokauthor.setText(books.get(position).getBokauthor());
        viewHolder.tvBookupdate.setText(ConvertDemo.DateToStr(books.get(position).getBookupdate()));
        viewHolder.tvUpchapter.setText(books.get(position).getUpchapter());
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.iv_written)
        ImageView ivWritten;
        @BindView(R.id.tv_bookname)
        TextView tvBookname;
        @BindView(R.id.tv_bokauthor)
        TextView tvBokauthor;
        @BindView(R.id.tv_bookupdate)
        TextView tvBookupdate;
        @BindView(R.id.tv_upchapter)
        TextView tvUpchapter;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void Updata(List<Book> books){
        this.books = books;
        notifyDataSetChanged();
    }
}
