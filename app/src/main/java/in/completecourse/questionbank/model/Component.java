package in.completecourse.questionbank.model;

public class Component {
    private String mComponentName, mComponentKiId, mComponentURL;
    private int cardBackground;

    public int getCardBackground() {
        return cardBackground;
    }

    public void setCardBackground(int cardBackground) {
        this.cardBackground = cardBackground;
    }

    public String getmComponentName() {
        return mComponentName;
    }

    public void setmComponentName(String mComponentName) {
        this.mComponentName = mComponentName;
    }

    public String getmComponentKiId() {
        return mComponentKiId;
    }

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
