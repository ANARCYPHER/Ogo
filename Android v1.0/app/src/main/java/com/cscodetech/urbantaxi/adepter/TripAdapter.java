package com.cscodetech.urbantaxi.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.urbantaxi.R;
import com.cscodetech.urbantaxi.model.OrderHistoryItem;
import com.cscodetech.urbantaxi.retrofit.APIClient;
import com.cscodetech.urbantaxi.utility.SessionManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolder> {


    private Context mContext;
    private List<OrderHistoryItem> historyItems;
    private RecyclerTouchListener listener;
    SessionManager sessionManager;
    int prSelct;

    public interface RecyclerTouchListener {

        public void onClickWheelerInfo(OrderHistoryItem item, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_icon)
        public ImageView imgIcon;
        @BindView(R.id.txt_timedate)
        public TextView txtTimedate;
        @BindView(R.id.txt_type_orders)
        public TextView txtTypeOrders;
        @BindView(R.id.txt_pickup)
        public TextView txtPickup;
        @BindView(R.id.txt_drop)
        public TextView txtDrop;
        @BindView(R.id.txt_total)
        public TextView txtTotal;
        @BindView(R.id.lvl_click)
        public LinearLayout lvlClick;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public TripAdapter(Context mContext, List<OrderHistoryItem> historyItems, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.historyItems = historyItems;
        this.listener = listener;

        sessionManager = new SessionManager(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tripe, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        OrderHistoryItem item = historyItems.get(position);
        holder.txtTimedate.setText("" + item.getOrderDate());
        holder.txtTypeOrders.setText("" + item.getCarType() + ". " + item.getRideId());
        holder.txtTotal.setText(sessionManager.getStringData(SessionManager.currency) + item.getOrderTotal());
        holder.txtPickup.setText("" + item.getPickAddress());
        holder.txtDrop.setText("" + item.getDropAddress());
        Glide.with(mContext).load(APIClient.baseUrl + "/" + item.getCarImg()).thumbnail(Glide.with(mContext).load(R.drawable.ic_img_dummy)).into(holder.imgIcon);

        holder.lvlClick.setOnClickListener(v -> {


            listener.onClickWheelerInfo(item, position);


        });

    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }
}