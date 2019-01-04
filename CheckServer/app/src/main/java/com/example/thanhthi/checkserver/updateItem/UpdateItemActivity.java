package com.example.thanhthi.checkserver.updateItem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.thanhthi.checkserver.R;
import com.example.thanhthi.checkserver.data.model.ItemCheckServer;

public class UpdateItemActivity extends AppCompatActivity implements View.OnClickListener
{
    public static final String FLAG = "flag";
    public static final String IS_CREATE = "create";
    public static final String IS_EDIT = "edit";

    private String flag;

    private TextView title;
    private TextInputEditText edtUrl, edtKeyWord, edtMessage, edtFrequency;
    private Switch switchCheck;
    private Button btnDone;

    private ItemCheckServer editItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);

        initView();
    }

    @SuppressLint("SetTextI18n")
    private void initView()
    {
        edtUrl = findViewById(R.id.edtUrl);
        edtKeyWord = findViewById(R.id.edtKeyWord);
        edtMessage = findViewById(R.id.edtMessage);
        edtFrequency = findViewById(R.id.edtFrequency);
        switchCheck = findViewById(R.id.switchCheck);
        btnDone = findViewById(R.id.btnDone);

        switchCheck.setChecked(false);

        Bundle bundle = getIntent().getExtras();
        flag = bundle.getString(FLAG, IS_CREATE);
        if (flag.equals(IS_CREATE))
            title.setText("Tạo mới");
        else
        {
            title.setText("Chỉnh sửa");
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
        String url = edtUrl.getText().toString();
        String keyWord = edtKeyWord.getText().toString();
        String message = edtMessage.getText().toString();
        Float frequency = Float.parseFloat(edtFrequency.getText().toString());

        // start service
        //...
    }

    private void stopCheckServer()
    {

    }

    private void updateItemListActivity()
    {

    }
}
