package in.completecourse.questionbank.adapter;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import in.completecourse.questionbank.R;
import java.util.ArrayList;
import in.completecourse.questionbank.model.ActivityItem;
import in.completecourse.questionbank.model.Component;


public class ComponentAdapter extends RecyclerView.Adapter<ComponentAdapter.MyViewHolder> {
    private ArrayList<Component> activityItemsList;
    private final Context context;

    public ComponentAdapter(Context context, ArrayList<Component> list){
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
        final Component activityItem = activityItemsList.get(i);
        myViewHolder.textView.setText(activityItem.getmComponentName());
        myViewHolder.activityCard.setCardBackgroundColor(activityItem.getCardBackground());
    }


    @Override
    public int getItemCount() {
        return activityItemsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;
        CardView activityCard;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.activity_name);
            activityCard = itemView.findViewById(R.id.activityCard);
        }
    }

    public interface ClickListener {
        void onClick(int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private final GestureDetector gestureDetector;

        private final ComponentAdapter.ClickListener clickListener;
        public RecyclerTouchListener(Context context, final ClickListener clickListener){
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
            View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());
            if (child!=null && clickListener != null && gestureDetector.onTouchEvent(motionEvent)){
                clickListener.onClick(recyclerView.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {

        }
    }

    public void setItems(ArrayList<Component> activityItems) {
        this.activityItemsList = activityItems;
    }

}
