package com.example.thanhthi.checkserver.updateItem;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thanhthi.checkserver.R;
import com.example.thanhthi.checkserver.SendDataToMainActivity;
import com.example.thanhthi.checkserver.data.model.ItemCheckServer;
import com.example.thanhthi.checkserver.services.CheckServerService;
import com.example.thanhthi.checkserver.services.NotificationHelper;

import java.util.Calendar;

public class UpdateItemFragment extends Fragment implements View.OnClickListener
{
    public static final String IS_CREATE = "create";
    public static final String IS_EDIT = "edit";
    private String flag;

    private TextView title;
    private TextInputEditText edtUrl, edtKeyWord, edtMessage, edtFrequency;
    private Switch switchCheck;
    private Button btnDone;

    private ItemCheckServer selectedModel;
    private int position;
    private SendDataToMainActivity sendDataToMainActivity;

    @Override
    public void onStop() {
        super.onStop();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

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
                title.setText("Create new item");
                break;
            }
            case IS_EDIT:
            {
                title.setText("Edit item");
                edtUrl.setText(selectedModel.getUrl());
                edtKeyWord.setText(selectedModel.getKeyWord());
                edtMessage.setText(selectedModel.getMessage());
                edtFrequency.setText(selectedModel.getFrequency() + "");
                switchCheck.setChecked(selectedModel.isChecking());
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
                updateItemListActivity();
                break;
            }
        }
    }

    private void updateItemListActivity()
    {
        String url = edtUrl.getText().toString().trim();
        String keyWord = edtKeyWord.getText().toString().trim();
        String message = edtMessage.getText().toString().trim();
        Double frequency = Double.parseDouble(edtFrequency.getText().toString().trim());

        if (url.isEmpty())
        {
            Toast.makeText(getContext(), "Please input url!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (keyWord.isEmpty())
        {
            Toast.makeText(getContext(), "Please input key word", Toast.LENGTH_SHORT).show();
            return;
        }
        if (message.isEmpty())
        {
            Toast.makeText(getContext(), "Please input message", Toast.LENGTH_SHORT).show();
            return;
        }
        if (frequency == 0.0d)
        {
            Toast.makeText(getContext(), "Please input frequency", Toast.LENGTH_SHORT).show();
            return;
        }

        switch(flag)
        {
            case IS_CREATE:
            {
                selectedModel = new ItemCheckServer(url, keyWord, message, frequency, switchCheck.isChecked());

                sendDataToMainActivity.sendNewItem(selectedModel);
                break;
            }
            case IS_EDIT:
            {
                selectedModel.setUrl(url);
                selectedModel.setKeyWord(keyWord);
                selectedModel.setMessage(message);
                selectedModel.setFrequency(frequency);
                selectedModel.setChecking(switchCheck.isChecked());

                sendDataToMainActivity.sendEditItem(selectedModel, position);
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
