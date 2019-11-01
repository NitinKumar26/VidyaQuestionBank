package in.completecourse.questionbank.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by belal on 19/4/17.
 */

public class Updates {


// --Commented out by Inspection START (2/11/19 12:17 AM):
// --Commented out by Inspection START (2/11/19 12:17 AM):
        @SerializedName("data")
        private ArrayList<Update> heros;
// --Commented out by Inspection STOP (2/11/19 12:17 AM)
// --Commented out by Inspection STOP (2/11/19 12:17 AM)

    public Updates(){

    }

    public ArrayList<Update> getHeros(){
        return heros;
    }
}
