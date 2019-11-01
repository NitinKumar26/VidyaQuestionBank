package in.completecourse.questionbank.model;

import android.graphics.drawable.Drawable;

public class ActivityItem {

    private String activityKaName, activityKiId, conceptKaFlipURL, chapterKaVideoID, chapterSerial, otherImportantQues, desc;
    private Drawable cardBackground;

    public ActivityItem(){

    }

    public String getActivityKaName() {
        return activityKaName;
    }

    public void setActivityKaName(String activityKaName) {
        this.activityKaName = activityKaName;
    }

    public String getActivityKiId() {
        return activityKiId;
    }

    public void setActivityKiId(String activityKiId) {
        this.activityKiId = activityKiId;
    }

    public String getConceptKaFlipURL() {
        return conceptKaFlipURL;
    }

    public void setConceptKaFlipURL(String conceptKaFlipURL) {
        this.conceptKaFlipURL = conceptKaFlipURL;
    }

    public String getChapterKaVideoID() {
        return chapterKaVideoID;
    }

    public void setChapterKaVideoID(String chapterKaVideoID) {
        this.chapterKaVideoID = chapterKaVideoID;
    }

    public String getChapterSerial() {
        return chapterSerial;
    }

    public void setChapterSerial(String chapterSerial) {
        this.chapterSerial = chapterSerial;
    }

    public String getOtherImportantQues() {
        return otherImportantQues;
    }

    public void setOtherImportantQues(String otherImportantQues) {
        this.otherImportantQues = otherImportantQues;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Drawable getCardBackground() {
        return cardBackground;
    }

    public void setCardBackground(Drawable cardBackground) {
        this.cardBackground = cardBackground;
    }
}
