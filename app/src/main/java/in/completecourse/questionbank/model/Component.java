package in.completecourse.questionbank.model;

import android.graphics.drawable.Drawable;

public class Component {
    private String mComponentName, mComponentKiId, mComponentURL;
    private Drawable cardBackground;

    public Drawable getCardBackground() {
        return cardBackground;
    }

    public void setCardBackground(Drawable cardBackground) {
        this.cardBackground = cardBackground;
    }

    public String getmComponentName() {
        return mComponentName;
    }

    public void setmComponentName(String mComponentName) {
        this.mComponentName = mComponentName;
    }

// --Commented out by Inspection START (2/11/19 12:17 AM):
//    public String getmComponentKiId() {
//        return mComponentKiId;
//    }
// --Commented out by Inspection STOP (2/11/19 12:17 AM)

    public void setmComponentKiId(String mComponentKiId) {
        this.mComponentKiId = mComponentKiId;
    }

    public String getmComponentURL() {
        return mComponentURL;
    }

    public void setmComponentURL(String mComponentURL) {
        this.mComponentURL = mComponentURL;
    }
}
