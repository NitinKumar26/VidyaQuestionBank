package in.completecourse.questionbank.model;

public class NotificationModel {
    String mHeading, mSubHeading, url, serial;

    public String getmHeading() {
        return mHeading;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getmSubHeading() {
        return mSubHeading;
    }

    public void setmHeading(String mHeading) {
        this.mHeading = mHeading;
    }

    public void setmSubHeading(String mSubHeading) {
        this.mSubHeading = mSubHeading;
    }

    public NotificationModel(){

    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public NotificationModel(String heading, String subheading, String url){
        this.mHeading = heading;
        this.mSubHeading = subheading;
        this.url = url;
    }
}