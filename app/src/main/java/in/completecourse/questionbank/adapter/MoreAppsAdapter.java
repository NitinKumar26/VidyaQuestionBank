package in.completecourse.questionbank.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.completecourse.questionbank.R;
import in.completecourse.questionbank.model.MoreAppsItem;

public class MoreAppsAdapter extends RecyclerView.Adapter<MoreAppsAdapter.ViewHolder> {
    private ArrayList<MoreAppsItem>  moreAppsItems;
    private Context mContext;

    public MoreAppsAdapter(Context mContext, ArrayList<MoreAppsItem> moreAppsItems) {
        this.moreAppsItems = moreAppsItems;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MoreAppsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_more_apps, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreAppsAdapter.ViewHolder holder, int position) {
        MoreAppsItem item = moreAppsItems.get(position);

        Glide.with(mContext).load(item.getIcon_url()).into(holder.imgAppIcon);

        holder.tvAppName.setText(item.getApp_name());
        holder.tvAppDesc.setText(item.getApp_desc());
    }

    @Override
    public int getItemCount() {
        return moreAppsItems.size();
    }

     static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAppName, tvAppDesc;
        private ImageView imgAppIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAppIcon = itemView.findViewById(R.id.img);
            tvAppName = itemView.findViewById(R.id.tv_title);
            tvAppDesc = itemView.findViewById(R.id.tv_subtitle);
        }
    }

    public void setItems(ArrayList<MoreAppsItem> items){
        this.moreAppsItems = items;
    }

}
