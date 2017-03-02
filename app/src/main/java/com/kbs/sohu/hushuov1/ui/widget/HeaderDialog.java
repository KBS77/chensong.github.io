package com.kbs.sohu.hushuov1.ui.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kbs.sohu.hushuov1.R;

/**
 * Created by tarena on 2017/02/16.
 */

public class HeaderDialog extends DialogFragment implements View.OnClickListener{

    private Button bt_takephoto;
    private Button bt_openphoto;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog_Alert);
    }

    @Override
    public void onStart() {
        super.onStart();
        int dialogHeight = (int) (getActivity().getResources().getDisplayMetrics().heightPixels * 0.25);
        int dialogWidth = (int) (getActivity().getResources().getDisplayMetrics().widthPixels * 0.75);
        getDialog().getWindow().setLayout(dialogWidth,dialogHeight);
        getDialog().setCanceledOnTouchOutside(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.header_dialog_theme,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bt_takephoto = (Button) view.findViewById(R.id.bt_takephoto);
        bt_openphoto = (Button) view.findViewById(R.id.bt_openphoto);
        bt_takephoto.setOnClickListener(this);
        bt_openphoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_takephoto:
//                startCamera();
                break;
            case R.id.bt_openphoto:
                  startAblum();
                break;
            default:
                break;
        }
    }

    private void startAblum(){
        dismiss();
    }



}
