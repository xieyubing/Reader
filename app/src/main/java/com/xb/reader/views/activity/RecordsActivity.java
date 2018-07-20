package com.xb.reader.views.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.xb.reader.MainActivity;
import com.xb.reader.R;
import com.xb.reader.bean.Book;
import com.xb.reader.bean.Records;
import com.xb.reader.util.HttpJsoup;
import com.xb.reader.util.NetWorkUtils;
import com.xb.reader.util.ThreadPoolManager;
import com.xb.reader.util.UrlEncoder;
import com.xb.reader.views.adapter.RecordsListBaseAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by asus on 2017/8/8.
 */

public class RecordsActivity extends BaseActivity {
    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.search_et_input)
    EditText searchEtInput;
    @BindView(R.id.search_iv_delete)
    ImageView searchIvDelete;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.lv_records)
    ListView lvRecords;
    RecordsListBaseAdapter adapter;

    private String name;
    List<Book> books;
    ProgressDialog progressDialog;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1) {
                progressDialog.dismiss();
                books = (List<Book>) msg.obj;
                adapter.Updata(books);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        adapter = new RecordsListBaseAdapter(RecordsActivity.this, books);
        lvRecords.setAdapter(adapter);
        lvRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RecordsActivity.this, PageActivity.class);
                intent.putExtra("URl", books.get(position).getUrl());
                startActivity(intent);

            }
        });

        searchEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = searchEtInput.getText().toString().trim();
                if (input.isEmpty()) {
                    searchIvDelete.setVisibility(GONE);
                } else {
                    searchIvDelete.setVisibility(VISIBLE);
                }
            }
        });
    }

    @OnClick({R.id.iv_return, R.id.search_iv_delete, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                startActivity(new Intent(RecordsActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.search_iv_delete:
                searchEtInput.setText("");
                searchIvDelete.setVisibility(GONE);
                break;
            case R.id.iv_search:
                name = searchEtInput.getText().toString().trim();//辰东
                if (name == null || name.length() < 1) {
                    Toast.makeText(RecordsActivity.this, "不能为空哦", Toast.LENGTH_LONG).show();
                } else {
                    if (NetWorkUtils.isNetworkConnected(RecordsActivity.this)) {
                        Records records = new Records();
                        records.setName(name);
                        records.save();
                        progressDialog = ProgressDialog.show(RecordsActivity.this, "请稍等...", "获取数据中...", true);
                        ThreadPoolManager.getInstance().execute(new Runnable() {
                            @Override
                            public void run() {
                                List<Book> books = HttpJsoup.Records(UrlEncoder.encode(name));
                                Message message = new Message();
                                message.arg1 = 1;
                                message.obj = books;
                                handler.sendMessage(message);
                            }
                        });
                    } else {
                        Toast.makeText(RecordsActivity.this, "没有网络哦", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

}
