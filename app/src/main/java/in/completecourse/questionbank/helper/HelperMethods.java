package in.completecourse.questionbank.helper;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import java.util.Calendar;
import java.util.TimeZone;

public class HelperMethods {
    /**
     * Making notification bar transparent
     */
    public static void changeStatusBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

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
}
