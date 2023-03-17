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
import com.cscodetech.urbantaxi.model.Identy;
import com.cscodetech.urbantaxi.retrofit.APIClient;
import com.cscodetech.urbantaxi.utility.SessionManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerifyListAdapter extends RecyclerView.Adapter<VerifyListAdapter.MyViewHolder> {


    private Context mContext;
    private List<Identy> identyList;
    private RecyclerTouchListener listener;
    SessionManager sessionManager;
    int prSelct;

    public interface RecyclerTouchListener {
        public void onClickVeryfyItem(Identy item, int position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_frount)
        public ImageView imgFrount;
        @BindView(R.id.txt_frount)
        public TextView txtFrount;
        @BindView(R.id.txt_backend)
        public TextView txtBackend;
        @BindView(R.id.img_backend)
        public ImageView imgBackend;
        @BindView(R.id.txt_uploadfrount)
        public TextView txtUploadfrount;
        @BindView(R.id.txt_tile)
        public TextView txtTile;
        @BindView(R.id.lvl_click)
        public LinearLayout lvlClick;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public VerifyListAdapter(Context mContext, List<Identy> wheeleritemList, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.identyList = wheeleritemList;
        this.listener = listener;

        sessionManager = new SessionManager(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_verufylist, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Glide.with(mContext).load(APIClient.baseUrl + "/" + identyList.get(position).getFront()).thumbnail(Glide.with(mContext).load(R.drawable.ic_img_dummy)).into(holder.imgFrount);
        Glide.with(mContext).load(APIClient.baseUrl + "/" + identyList.get(position).getBack()).thumbnail(Glide.with(mContext).load(R.drawable.ic_img_dummy)).into(holder.imgBackend);
        holder.txtTile.setText("" + identyList.get(position).getTitle());
        holder.txtUploadfrount.setText(""+identyList.get(position).getStatus());
        holder.lvlClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(identyList.get(position).getStatus().equalsIgnoreCase("Reject")){
                    listener.onClickVeryfyItem(identyList.get(position),position);
                }
            }
        });
        if(identyList.get(position).getStatus().equalsIgnoreCase("Reject")){
            holder.txtFrount.setVisibility(View.VISIBLE);
            holder.txtBackend.setVisibility(View.VISIBLE);
        }else {
            holder.txtFrount.setVisibility(View.GONE);
            holder.txtBackend.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return identyList.size();
    }
}