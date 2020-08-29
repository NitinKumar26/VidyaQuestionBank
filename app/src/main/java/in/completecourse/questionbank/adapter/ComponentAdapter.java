package in.completecourse.questionbank.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.List;

import in.completecourse.questionbank.R;
import in.completecourse.questionbank.helper.HelperMethods;
import in.completecourse.questionbank.model.Component;


public class ComponentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int MENU_ITEM_VIEW_TYPE = 0;
    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;

    private List<Object> activityItemsList;
    private final Context context;


    public ComponentAdapter(Context context, List<Object> list){
        this.activityItemsList = list;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvSubTitle, tvImage;
        private RelativeLayout relativeLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSubTitle = itemView.findViewById(R.id.tv_subtitle);
            relativeLayout = itemView.findViewById(R.id.img);
            tvImage = itemView.findViewById(R.id.tv_img);
        }
    }

    @Override
    public int getItemCount() {
        return activityItemsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object recyclerViewItem = activityItemsList.get(position);
        if (recyclerViewItem instanceof UnifiedNativeAd) {
            return UNIFIED_NATIVE_AD_VIEW_TYPE;
        }
        return MENU_ITEM_VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i){
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                View unifiedNativeLayoutView = LayoutInflater.from(context).inflate(R.layout.ad_unified_new, viewGroup, false);
                return new UnifiedNativeAdViewHolder(unifiedNativeLayoutView);
            case MENU_ITEM_VIEW_TYPE:
                //Fall Through
            default:
                View view = LayoutInflater.from(context).inflate(R.layout.item_activity, viewGroup, false);
                return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int i) {
        int viewType = getItemViewType(i);
        switch (viewType) {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                UnifiedNativeAd nativeAd = (UnifiedNativeAd) activityItemsList.get(i);
                HelperMethods.populateAdView(nativeAd, ((UnifiedNativeAdViewHolder) holder).getView());
                break;
            case MENU_ITEM_VIEW_TYPE:
                //Fall Through
            default:
                ViewHolder viewHolder = (ViewHolder) holder;

                Component activityItem = (Component) activityItemsList.get(i);
                viewHolder.tvTitle.setText(activityItem.getmComponentName());
                //holder.tvSubTitle.setText(activityItem.getSub_title());
                viewHolder.tvImage.setText(String.valueOf(activityItem.getmComponentName().charAt(0)));

                if (i % 5 == 0) viewHolder.relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.gradient_two));
                else if (i % 5 == 1) viewHolder.relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.gradient_three));
                else if (i % 5 == 2) viewHolder.relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.gradient_four));
                else if (i % 5 == 3) viewHolder.relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.gradient_five));
                else viewHolder.relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.gradient));
        }
    }

    public void setItems(List<Object> activityItemsList){
        this.activityItemsList = activityItemsList;
    }

    static class UnifiedNativeAdViewHolder extends RecyclerView.ViewHolder {
        private UnifiedNativeAdView adView;

        public UnifiedNativeAdView getView() {
            return adView;
        }

        UnifiedNativeAdViewHolder(View view){
            super(view);

            adView = view.findViewById(R.id.unified_ad);

            //The MediaView will display a video asset if one is present in the ad, and the
            //first image asset otherwise.
            adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

            //Register the view used for each individual asset
            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
            //adView.setBodyView(adView.findViewById(R.id.ad_body));
            //adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            adView.setIconView(adView.findViewById(R.id.ad_icon));
            adView.setPriceView(adView.findViewById(R.id.ad_price));
            adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
            //adView.setStoreView(adView.findViewById(R.id.ad_store));
            //adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        }
    }
}
