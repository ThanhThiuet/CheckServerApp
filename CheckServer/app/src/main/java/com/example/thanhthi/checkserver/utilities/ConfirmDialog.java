package com.example.thanhthi.checkserver.utilities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thanhthi.checkserver.R;

/**
 * Created by hdh lee ltronghau@gmail.com +8498 345 7505 on 7/11/2017.
 */

public class ConfirmDialog extends DialogFragment
{
    private OnConfirmDialogAction listener;

    public static ConfirmDialog initialize(String message, OnConfirmDialogAction listener)
    {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setOnConfirmDialogAction(listener);
        Bundle mBundle = new Bundle();
        mBundle.putString("message", message);
        dialog.setArguments(mBundle);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final String message = getArguments().getString("message");
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.notification))
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt)
                    {
                        if (listener != null)
                        {
                            listener.onAccept();
                        }

                        if (getDialog() != null)
                        {
                            getDialog().dismiss();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt)
                    {
                        if (listener != null)
                        {
                            listener.onCancel();
                        }
                    }
                }).create();

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        setCancelable(true);
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null)
        {
            listener.onCancel();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setOnConfirmDialogAction(OnConfirmDialogAction listener)
    {
        this.listener = listener;
    }
}
