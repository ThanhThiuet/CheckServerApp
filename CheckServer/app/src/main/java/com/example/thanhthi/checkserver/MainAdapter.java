package com.example.thanhthi.checkserver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.thanhthi.checkserver.data.model.ItemCheckServer;
import com.example.thanhthi.checkserver.updateItem.EmptyViewHolder;
import com.example.thanhthi.checkserver.updateItem.FooterViewHolder;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private int TYPE_EMPTY = 0;
    private int TYPE_HEADER = 1;
    private int TYPE_ITEM = 2;
    private int TYPE_FOOTER = 3;

    private List<ItemCheckServer> dataList;

    public MainAdapter(List<ItemCheckServer> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view;
        if (i == TYPE_EMPTY)
        {
            view = inflater.inflate(R.layout.layout_empty, viewGroup, false);
            return new EmptyViewHolder(view);
        }
        else if (i == TYPE_HEADER)
        {
            view = inflater.inflate(R.layout.layout_header_item, viewGroup, false);
            return new HeaderHolder(view);
        }
        else if (i == TYPE_ITEM)
        {
            view = inflater.inflate(R.layout.layout_item, viewGroup, false);
            return new ItemHolder(view);
        }
        else if (i == TYPE_FOOTER)
        {
            view = inflater.inflate(R.layout.layout_footer, viewGroup, false);
            return new FooterViewHolder(view);
        }
        throw new RuntimeException("Không có type nào!");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i)
    {
        if (viewHolder instanceof HeaderHolder)
        {
            int total = dataList.size();
            ((HeaderHolder) viewHolder).bind(total);
        }

        if (viewHolder instanceof ItemHolder)
        {
            ItemCheckServer model = dataList.get(i);
            ((ItemHolder) viewHolder).bind(model);
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null || dataList.size() == 0 ? 1 : 2 + dataList.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        if (dataList == null || dataList.size() == 0)
            return TYPE_EMPTY;
        else if (position == 0)
            return TYPE_HEADER;
        else if (position == 1 + dataList.size())
            return TYPE_FOOTER;
        return TYPE_ITEM;
    }

    class HeaderHolder extends RecyclerView.ViewHolder
    {
        private TextView txtTotal;

        public HeaderHolder(@NonNull View itemView)
        {
            super(itemView);
            txtTotal = itemView.findViewById(R.id.txtTotal);
        }

        @SuppressLint("SetTextI18n")
        public void bind(int number)
        {
            String header;

            if (number <= 0)
                header = "0 item";
            else if (number == 1)
                header = "1 item";
            else
                header = number + " items";

            txtTotal.setText(header);
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private LinearLayout llItem;
        private TextView textNumber, textUrl, textKeyWord, textMessage, textFrequency, textState;
        private ImageView imgOptionMenu, imgSelected;

        public ItemHolder(@NonNull View itemView)
        {
            super(itemView);

            llItem = itemView.findViewById(R.id.llItem);

            textNumber = itemView.findViewById(R.id.textNumber);
            textUrl = itemView.findViewById(R.id.textUrl);
            textKeyWord = itemView.findViewById(R.id.textKeyWord);
            textMessage = itemView.findViewById(R.id.textMessage);
            textFrequency = itemView.findViewById(R.id.textFrequency);
            textState = itemView.findViewById(R.id.textState);

            imgOptionMenu = itemView.findViewById(R.id.imgOptionMenu);
            imgSelected = itemView.findViewById(R.id.imgSelected);

            llItem.setOnClickListener(this);
            imgOptionMenu.setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        public void bind(ItemCheckServer model)
        {
            textNumber.setText(model.getId() + "");
            textUrl.setText(model.getUrl());
            textKeyWord.setText(model.getKeyWord());
            textMessage.setText(model.getMessage());
            textFrequency.setText(model.getFrequency() + "");

            if (model.isChecking())
                textState.setText("Có");
            else
                textState.setText("Không");

            imgSelected.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.llItem:
                {

                    break;
                }
                case R.id.imgOptionMenu:
                {

                    break;
                }
            }
        }
    }
}
