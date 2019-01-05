package com.example.thanhthi.checkserver.updateItem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.thanhthi.checkserver.R;
import com.example.thanhthi.checkserver.SendDataToMainActivity;
import com.example.thanhthi.checkserver.data.model.ItemCheckServer;

public class UpdateItemFragment extends Fragment implements View.OnClickListener
{
    public static final String FLAG = "flag";
    public static final String IS_CREATE = "create";
    public static final String IS_EDIT = "edit";

    public static final String SELECTED_MODEL = "model";
    public static final String SELECTED_POSITION = "position";

    private String flag;

    private TextView title;
    private TextInputEditText edtUrl, edtKeyWord, edtMessage, edtFrequency;
    private Switch switchCheck;
    private Button btnDone;

    private ItemCheckServer selectedModel;
    private int position;
    private SendDataToMainActivity sendDataToMainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_update_item, container, false);
        initView(view);
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void initView(View view)
    {
        title = view.findViewById(R.id.title);
        edtUrl = view.findViewById(R.id.edtUrl);
        edtKeyWord = view.findViewById(R.id.edtKeyWord);
        edtMessage = view.findViewById(R.id.edtMessage);
        edtFrequency = view.findViewById(R.id.edtFrequency);
        switchCheck = view.findViewById(R.id.switchCheck);
        btnDone = view.findViewById(R.id.btnDone);

        switchCheck.setChecked(false);

        switch (flag)
        {
            case IS_CREATE:
            {
                title.setText("Tạo mới");
                break;
            }
            case IS_EDIT:
            {
                title.setText("Chỉnh sửa");
                break;
            }
        }

        btnDone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnDone:
            {
                String url = edtUrl.getText().toString();
                String keyWord = edtKeyWord.getText().toString();
                String message = edtMessage.getText().toString();
                Float frequency = Float.parseFloat(edtFrequency.getText().toString());

                selectedModel = new ItemCheckServer(url, keyWord, message, frequency, switchCheck.isChecked());

                checkSwitch();
                updateItemListActivity();
                break;
            }
        }
    }

    private void checkSwitch()
    {
        if (switchCheck.isChecked())
            startCheckServer();
        else
            stopCheckServer();
    }

    private void startCheckServer()
    {
        // start service
        //...
    }

    private void stopCheckServer()
    {

    }

    private void updateItemListActivity()
    {
        switch(flag)
        {
            case IS_CREATE:
            {
                sendDataToMainActivity.sendNewItem(selectedModel);
                break;
            }
            case IS_EDIT:
            {

                break;
            }
        }
        getActivity().onBackPressed();
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setSelectedModel(ItemCheckServer selectedModel) {
        this.selectedModel = selectedModel;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setSendDataToMainActivity(SendDataToMainActivity sendDataToMainActivity) {
        this.sendDataToMainActivity = sendDataToMainActivity;
    }
}
