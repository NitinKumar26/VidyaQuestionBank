package in.completecourse.questionbank.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by belal on 19/4/17.
 */

public class Update {

    @SerializedName("updatekanaam")
    private final String name;

    @SerializedName("updatekaimageurl")
    private String url;

    public Update(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}