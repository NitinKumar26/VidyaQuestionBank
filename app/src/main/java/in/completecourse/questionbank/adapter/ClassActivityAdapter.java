package in.completecourse.questionbank.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import in.completecourse.questionbank.R;
import java.util.ArrayList;
import in.completecourse.questionbank.model.ActivityItem;


public class ClassActivityAdapter extends RecyclerView.Adapter<ClassActivityAdapter.MyViewHolder> {
    private ArrayList<ActivityItem> activityItemsList;
    private final Context context;

    public ClassActivityAdapter(Context context, ArrayList<ActivityItem> list){
        this.activityItemsList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_activity_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        final ActivityItem activityItem = activityItemsList.get(i);
        myViewHolder.textView.setText(activityItem.getActivityKaName());
        myViewHolder.activityCard.setBackground(activityItem.getCardBackground());
    }


    @Override
    public int getItemCount() {
        return activityItemsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;
        final CardView activityCard;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.activity_name);
            activityCard = itemView.findViewById(R.id.activityCard);
        }
    }



// --Commented out by Inspection START (2/11/19 12:17 AM):
//    public void setItems(ArrayList<ActivityItem> activityItems) {
//        this.activityItemsList = activityItems;
//    }
// --Commented out by Inspection STOP (2/11/19 12:17 AM)

}
