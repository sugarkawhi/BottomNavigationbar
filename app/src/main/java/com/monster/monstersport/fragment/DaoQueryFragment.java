package com.monster.monstersport.fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.monster.monstersport.R;
import com.monster.monstersport.base.BaseLazyFragment;
import com.monster.monstersport.dao.bean.BookMarkBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * test
 * Created by ZhaoZongyao on 2017/10/16.
 */

public class DaoQueryFragment extends BaseLazyFragment {
    private String TAG = "GreenDaoFragment";


    @BindView(R.id.edt_progress)
    EditText edt_progress;
    @BindView(R.id.listView)
    ListView mListView;

    private DaoAdapter mDaoAdapter;

    public static DaoQueryFragment newInstance() {
        return new DaoQueryFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_daoquery;
    }

    @Override
    protected void init(View view) {
    }


    @Override
    protected void loadData() {
        mDaoAdapter = new DaoAdapter();
        mListView.setAdapter(mDaoAdapter);

    }

    @OnClick(R.id.btn_query)
    public void query() {
        String bookId = "123";
        String chapterId = "123";

        String progress = edt_progress.getText().toString();

        if (!TextUtils.isEmpty(progress)) {
        } else {
        }

    }

    class DaoAdapter extends BaseAdapter {

        List<BookMarkBean> mMarkBeans = new ArrayList<>();

        public void setMarkBeans(List<BookMarkBean> markBeans) {
            mMarkBeans = markBeans;
            notifyDataSetChanged();
        }

        public void setMark(BookMarkBean bookMarkBean) {
            if (mMarkBeans == null) mMarkBeans = new ArrayList<>();
            else mMarkBeans.clear();
            if (bookMarkBean != null)
                mMarkBeans.add(bookMarkBean);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mMarkBeans == null ? 0 : mMarkBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return mMarkBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BookMarkBean bookMarkBean = (BookMarkBean) getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_book_mark, parent, false);
            TextView progress = view.findViewById(R.id.tv_progress);
            progress.setText(String.valueOf(bookMarkBean.getProgress()));
            TextView content = view.findViewById(R.id.tv_content);
            content.setText(bookMarkBean.getContent());
            return view;
        }
    }

}
