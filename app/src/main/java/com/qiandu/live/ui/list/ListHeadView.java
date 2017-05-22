package com.qiandu.live.ui.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.qiandu.live.R;
import com.qiandu.live.http.AsyncHttp;
import com.qiandu.live.http.request.BanerRequest;
import com.qiandu.live.http.request.RequestComm;
import com.qiandu.live.http.response.BanerResp;
import com.qiandu.live.http.response.ResList;
import com.qiandu.live.http.response.Response;
import com.qiandu.live.model.GiftInfo;
import com.qiandu.live.utils.AsimpleCache.ACache;

import java.util.ArrayList;

/**
 * Created by admin on 2017/5/11.
 */
public class ListHeadView extends RelativeLayout {
    private Context context;
    private SliderLayout sliderPager;

    public ListHeadView(Context context) {
        super(context);
        this.context = context;
    }

    public ListHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ListHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void initView() {
        View rootView = View.inflate(context, R.layout.list_refresh_header, this);
        sliderPager = (SliderLayout) rootView.findViewById(R.id.slider);

        initData();
    }

    public void initData() {
        baner("app","android");

    }

    private void baner(String app, String android) {
        BanerRequest ban=new BanerRequest(app,android);
        AsyncHttp.instance().post(ban, new AsyncHttp.IHttpListener() {
            @Override
            public void onStart(int requestId) {

            }

            @Override
            public void onSuccess(int requestId, Response response) {

                if (response.code == RequestComm.SUCCESS) {
                    ResList<BanerResp> resList = (ResList<BanerResp>) response.data;
                    if (resList != null) {
                        ArrayList<BanerResp> result = (ArrayList<BanerResp>) resList.datas;
//                        ACache.get()
////                        for (BanerResp ba : result) {
////                            ba.getImage();
////                        }

                        for (int i=0; i<result.size(); i++){
                            TextSliderView textSliderView = new TextSliderView(getContext());
                            textSliderView.description("").image(result.get(i).getImage());

                            sliderPager.addSlider(textSliderView);
                        }
                    }
                }
            }

            @Override
            public void onFailure(int requestId, int httpStatus, Throwable error) {

            }
        });


    }
}
