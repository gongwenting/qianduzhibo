package com.qiandu.live.ui.customviews;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.qiandu.live.R;
import com.qiandu.live.utils.OtherUtils;

/**
 * @description: 结束dialog
 * @author: Andruby
 * @time: 2016/12/17 10:23
 */
public class EndDetailFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog mDetailDialog = new Dialog(getActivity(), R.style.dialog);
        mDetailDialog.setContentView(R.layout.end_detail_fragment);
        mDetailDialog.setCancelable(false);

        mDetailDialog.findViewById(R.id.btn_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDetailDialog.dismiss();
                        getActivity().finish();
                    }
                });

        TextView tvDetailTime = (TextView) mDetailDialog.findViewById(R.id.tv_time);
        TextView tvDetailAdmires = (TextView) mDetailDialog.findViewById(R.id.tv_praise);
        TextView tvDetailWatchCount = (TextView) mDetailDialog.findViewById(R.id.tv_members);

        //确认则显示观看detail
        tvDetailTime.setText(getArguments().getString("time"));
        tvDetailAdmires.setText(getArguments().getString("praiseCount"));
        tvDetailWatchCount.setText(getArguments().getString("totalMemberCount"));

        return mDetailDialog;
    }

    public static void invoke(FragmentManager fragmentManager, long time, int praiseCount, int totalMember) {
        EndDetailFragment endDetailFragment = new EndDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("time", OtherUtils.formattedTime(time));
        bundle.putString("praiseCount", praiseCount + "");
        bundle.putString("totalMemberCount", totalMember + "");
        endDetailFragment.setArguments(bundle);
        endDetailFragment.setCancelable(false);
        endDetailFragment.show(fragmentManager, "endDtail")
        ;
    }
}
