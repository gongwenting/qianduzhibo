package com.qiandu.live.ui.list;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qiandu.live.R;


public class ListFootView extends RelativeLayout {

    ProgressBar mProgress;
    TextView mLoadingText;
    TextView mMoreText;
    private Context context;

    public ListFootView(Context context) {
        super(context);
        this.context = context;
    }

    public ListFootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ListFootView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public void initView() {
        View rootView = View.inflate(context, R.layout.list_refresh_footer, this);
        mProgress = (ProgressBar) rootView.findViewById(R.id.pull_to_refresh_progress);
        mLoadingText = (TextView) rootView.findViewById(R.id.pull_to_refresh_text);
        mMoreText = (TextView) rootView.findViewById(R.id.get_more);
    }

    public void setNetError() {
        mLoadingText.setVisibility(View.INVISIBLE);
        mProgress.setVisibility(View.INVISIBLE);
        mMoreText.setVisibility(View.VISIBLE);
        mMoreText.setText(R.string.networkerror_retry);
    }

    public void showErrorMsg(String msg) {
        mLoadingText.setVisibility(View.INVISIBLE);
        mProgress.setVisibility(View.INVISIBLE);
        mMoreText.setVisibility(VISIBLE);
        mMoreText.setGravity(Gravity.CENTER);
        mMoreText.setText(msg);
        mMoreText.setTextColor(Color.RED);
        setBackgroundColor(Color.TRANSPARENT);
    }

    public void setLoading() {
        mLoadingText.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.VISIBLE);
        mLoadingText.setText(R.string.loading_ing);
        mMoreText.setVisibility(View.INVISIBLE);
    }

    public void setLoadDone() {
        mLoadingText.setVisibility(View.INVISIBLE);
        mProgress.setVisibility(View.INVISIBLE);
        mMoreText.setVisibility(View.INVISIBLE);
    }

    public void setLoadDoneClick() {
        mLoadingText.setVisibility(View.GONE);
        mProgress.setVisibility(View.GONE);
        mMoreText.setVisibility(View.VISIBLE);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getContext().getString(R.string.no_more_data_and_click));
        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#4A94D2")), 8, 12, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mMoreText.setText(spannableStringBuilder);
    }
}
