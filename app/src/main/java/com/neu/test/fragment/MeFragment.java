package com.neu.test.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.allen.library.SuperTextView;
import com.neu.test.R;
import com.neu.test.activity.MeAboutActivity;
import com.neu.test.activity.MeAccountAndSafeActivity;
import com.neu.test.activity.MeFeedbackActivity;
import com.neu.test.activity.MeInformActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment implements View.OnClickListener {
    public static final int REQUEST_CODE_MEINFORM = 0;
    public static final int REQUEST_CODE_ACCOUNTANDSAFE = 1;
    public static final int REQUEST_CODE_FEEDBACK = 2;
    public static final int REQUEST_CODE_ABOUT = 3;

    private SuperTextView STVinform;
    private SuperTextView STVaccount;
    private SuperTextView STVfeedback;
    private SuperTextView STVabout;

    int me;


    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        init(view);

        return view;
    }

    private void init(View view) {
        STVinform = view.findViewById(R.id.STV_inform);
        STVaccount = view.findViewById(R.id.STV_account);
        STVfeedback = view.findViewById(R.id.STV_feedback);
        STVabout = view.findViewById(R.id.STV_about);


        STVinform.setOnClickListener(this);
        STVaccount.setOnClickListener(this);
        STVfeedback.setOnClickListener(this);
        STVabout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.STV_inform:
                intent = new Intent(getActivity(), MeInformActivity.class);
                getActivity().startActivityForResult(intent,REQUEST_CODE_MEINFORM);
                //startActivity(intent);
                break;
            case R.id.STV_account:
                intent = new Intent(getActivity(), MeAccountAndSafeActivity.class);
                getActivity().startActivityForResult(intent,REQUEST_CODE_ACCOUNTANDSAFE);
                break;
            case R.id.STV_feedback:
                intent = new Intent(getActivity(), MeFeedbackActivity.class);
                getActivity().startActivityForResult(intent,REQUEST_CODE_FEEDBACK);
                break;
            case R.id.STV_about:
                intent = new Intent(getActivity(), MeAboutActivity.class);
                getActivity().startActivityForResult(intent,REQUEST_CODE_ABOUT);
                break;
        }
    }
}
