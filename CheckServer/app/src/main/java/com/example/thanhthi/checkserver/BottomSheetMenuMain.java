package com.example.thanhthi.checkserver;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;

import com.example.thanhthi.checkserver.data.model.ItemCheckServer;
import com.example.thanhthi.checkserver.updateItem.UpdateItemFragment;
import com.example.thanhthi.checkserver.utilities.ConfirmDialog;
import com.example.thanhthi.checkserver.utilities.OnConfirmDialogAction;

public class BottomSheetMenuMain extends BottomSheetDialogFragment implements View.OnClickListener
{
    private LinearLayout editOption, deleteOption;

    private ItemCheckServer selectedItem;
    private int position = -1;
    private SendDataToMainActivity sendDataToMainActivity;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style)
    {
        super.setupDialog(dialog, style);
        View view = View.inflate(getContext(), R.layout.layout_menu_option_bottom, null);
        initView(view);
        dialog.setContentView(view);
    }

    private void initView(View view)
    {
        editOption = view.findViewById(R.id.editOption);
        deleteOption = view.findViewById(R.id.deleteOption);

        editOption.setOnClickListener(this);
        deleteOption.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.editOption:
            {
                editSelectedItem();
                break;
            }
            case R.id.deleteOption:
            {
                deleteSelectedItem();
                break;
            }
        }
    }

    private void editSelectedItem()
    {
        UpdateItemFragment updateItemFragment = new UpdateItemFragment();
        updateItemFragment.setFlag(UpdateItemFragment.IS_EDIT);
        updateItemFragment.setSendDataToMainActivity(sendDataToMainActivity);
        updateItemFragment.setSelectedModel(selectedItem);
        updateItemFragment.setPosition(position);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(android.R.id.content, updateItemFragment, "tag");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        dismiss();
    }

    private void deleteSelectedItem()
    {
        String title = "Bạn có muốn xóa item này không?";

        ConfirmDialog.initialize(title, new OnConfirmDialogAction() {
            @Override
            public void onCancel() {}

            @Override
            public void onAccept()
            {
                sendDataToMainActivity.sendDeleteItem(selectedItem, position);
                dismiss();
            }
        }).show(getFragmentManager(), "");
    }

    public void setSelectedItem(ItemCheckServer selectedItem) {
        this.selectedItem = selectedItem;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setSendDataToMainActivity(SendDataToMainActivity sendDataToMainActivity) {
        this.sendDataToMainActivity = sendDataToMainActivity;
    }
}
