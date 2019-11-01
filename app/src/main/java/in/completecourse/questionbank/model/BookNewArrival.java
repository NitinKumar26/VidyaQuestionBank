package in.completecourse.questionbank.model;

/**
 * Created by ravi on 21/12/17.
 */

public class BookNewArrival {
    private String title;
    private String url;
    private String rate;
    private String code;
    private String siteUrl;

    public String getTitle() {
        return title;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public BookNewArrival(){}


    public String getUrl() {
        return url;
    }

    public String getCode() {
        return code;
    }

// --Commented out by Inspection START (2/11/19 12:17 AM):
//    public void setCode(String code) {
//        this.code = code;
//    }
// --Commented out by Inspection STOP (2/11/19 12:17 AM)

    public void setUrl(String url) {
        this.url = url;
    }


    public String getRate() {
        return rate;
    }

    public void setRate(String price) {
        this.rate = price;
    }

}