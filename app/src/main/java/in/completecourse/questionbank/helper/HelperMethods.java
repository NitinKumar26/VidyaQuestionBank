package in.completecourse.questionbank.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.TimeZone;

public class HelperMethods {
// --Commented out by Inspection START (2/11/19 12:17 AM):
//    /**
//     * Making notification bar transparent
//     */
//    public static void changeStatusBarColor(Activity activity) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = activity.getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
//    }
// --Commented out by Inspection STOP (2/11/19 12:17 AM)

    public static String generateChecksum(){
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);

        //Months are indexed from 0
        int currentMonth = month + 1;
        int sum = year + currentMonth + date;
        int mul = year * currentMonth* date;
        int sum5 = sum * 5;
        return (mul + "31" + sum5);
    }

    public static int pix(Context activity, int dp){
    DisplayMetrics metrics = new DisplayMetrics();
    AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
    appCompatActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    float logincalDensity = metrics.density;
        return (int) Math.ceil(dp * logincalDensity);
    }


    public static void loadFragment(Fragment fragment, FragmentActivity activity, int frameLayoutId, boolean addToBackStack, String tag) {
        // load fragment
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.replace(frameLayoutId, fragment, tag);
            if (addToBackStack)
                transaction.addToBackStack(null);
            transaction.commit();
    }

    /**
     * Checks if there is Internet accessible.
     * @return True if there is Internet. False if not.
     */
    public static boolean isNetworkAvailable(Activity activity) {
        NetworkInfo activeNetworkInfo = null;
            ConnectivityManager connectivityManager = (ConnectivityManager) ((activity)).getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null){
                activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public interface ClickListener {
        void onClick(int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private final GestureDetector gestureDetector;

        private final HelperMethods.ClickListener clickListener;
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
}
