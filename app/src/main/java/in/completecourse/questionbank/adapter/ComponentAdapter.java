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

import in.completecourse.questionbank.model.Component;


public class ComponentAdapter extends RecyclerView.Adapter<ComponentAdapter.MyViewHolder> {
    private final ArrayList<Component> activityItemsList;
    private final Context context;

    public ComponentAdapter(Context context, ArrayList<Component> list){
        this.activityItemsList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.component_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        final Component activityItem = activityItemsList.get(i);
        myViewHolder.textView.setText(activityItem.getmComponentName());
        myViewHolder.activityCard.setBackground(activityItem.getCardBackground());

    }


    @Override
    public int getItemCount() {
        return activityItemsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;
        final CardView activityCard;
        //ImageView check;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.activity_name);
            activityCard = itemView.findViewById(R.id.activityCard);
            //check = itemView.findViewById(R.id.check);
        }
    }
}
