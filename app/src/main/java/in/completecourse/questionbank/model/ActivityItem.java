package in.completecourse.questionbank.model;

public class ActivityItem {

    private String activityKaName, activityKiId, conceptKaFlipURL, chapterKaVideoID, chapterSerial, otherImportantQues, desc;
    private int cardBackground;

    public ActivityItem(){

    }

    public String getChapterKaVideoID() {
        return chapterKaVideoID;
    }

    public void setChapterKaVideoID(String chapterKaVideoID) {
        this.chapterKaVideoID = chapterKaVideoID;
    }

    public void setConceptKaFlipURL(String conceptKaFlipURL) {
        this.conceptKaFlipURL = conceptKaFlipURL;
    }

    public int getCardBackground() {
        return cardBackground;
    }

    public void setCardBackground(int cardBackground) {
        this.cardBackground = cardBackground;
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

    public String getChapterSerial() {
        return chapterSerial;
    }

    public void setChapterSerial(String chapterSerial) {
        this.chapterSerial = chapterSerial;
    }

    public void setActivityKiId(String activityKiId) {
        this.activityKiId = activityKiId;
    }

    public void setActivityKaName(String activityKaName) {
        this.activityKaName = activityKaName;
    }

    public String getActivityKiId() {
        return activityKiId;
    }

    public String getActivityKaName() {
        return activityKaName;
    }

    public String getConceptKaFlipURL() {
        return conceptKaFlipURL;
    }
}
