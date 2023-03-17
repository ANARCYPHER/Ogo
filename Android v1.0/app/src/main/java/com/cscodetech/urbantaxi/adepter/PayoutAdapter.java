package com.cscodetech.urbantaxi.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.urbantaxi.R;
import com.cscodetech.urbantaxi.model.PayoutlistItem;
import com.cscodetech.urbantaxi.retrofit.APIClient;
import com.cscodetech.urbantaxi.utility.SessionManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PayoutAdapter extends RecyclerView.Adapter<PayoutAdapter.MyViewHolder> {



    private Context mContext;
    private List<PayoutlistItem> payoutlistItems;

    SessionManager sessionManager;


    public interface RecyclerTouchListener {
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_proof)
        public ImageView imgProof;
        @BindView(R.id.txt_status)
        public TextView txtStatus;
        @BindView(R.id.txt_requst)
        public TextView txtRequst;
        @BindView(R.id.txt_amt)
        public TextView txtAmt;
        @BindView(R.id.txt_payby)
        public TextView txtPayby;
        @BindView(R.id.txt_r_date)
        public TextView txtRDate;



        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public PayoutAdapter(Context mContext, List<PayoutlistItem> payoutlistItems) {
        this.mContext = mContext;
        this.payoutlistItems = payoutlistItems;


        sessionManager = new SessionManager(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custome_payoutitem, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        PayoutlistItem  payout=payoutlistItems.get(position);
        Glide.with(mContext).load(APIClient.baseUrl + "/" + payout.getProof()).into(holder.imgProof);

       holder.txtStatus.setText("" + payout.getStatus());
       holder.txtRequst.setText("" + payout.getPayoutId());
       holder.txtAmt.setText(sessionManager.getStringData(SessionManager.currency) + payout.getAmt());
       holder.txtPayby.setText("" + payout.getRType());
       holder.txtRDate.setText("" + payout.getRDate());



    }

    @Override
    public int getItemCount() {
        return payoutlistItems.size();
    }
}