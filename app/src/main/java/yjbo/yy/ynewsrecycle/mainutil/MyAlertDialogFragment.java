package yjbo.yy.ynewsrecycle.mainutil;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import yjbo.yy.ynewsrecycle.R;
import yjbo.yy.ynewsrecycle.main.MainActivity;

public class MyAlertDialogFragment extends DialogFragment {

    public static MyAlertDialogFragment newInstance(int title) {
        MyAlertDialogFragment frag = new MyAlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @TargetApi(11)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        int title = getArguments().getInt("title");

        
        AlertDialog dialog = new AlertDialog.Builder(getActivity(),android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        .setIcon(R.mipmap.ic_launcher)
        .setTitle("标题")
        .setPositiveButton("确定",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
//                    ((MainActivity)getActivity()).doPositiveClick();
                    Toast.makeText(getActivity(),"确定===",Toast.LENGTH_SHORT).show();
                }
            }
        )
        .setNegativeButton("取消",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
//                    ((MainActivity)getActivity()).doNegativeClick();
                    Toast.makeText(getActivity(),"取消===",Toast.LENGTH_SHORT).show();
                }
            }
        )
        .create();
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        
        return dialog;
    }
}