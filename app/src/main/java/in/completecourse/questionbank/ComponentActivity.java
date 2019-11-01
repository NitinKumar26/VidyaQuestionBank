package in.completecourse.questionbank;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import in.completecourse.questionbank.fragment.ComponentDetailsFragment;
import in.completecourse.questionbank.utils.ListConfig;

public class ComponentActivity extends AppCompatActivity implements View.OnClickListener {
    public static Intent intent;
    private ImageView competitionUpdatesImage;
    public static Spinner classSpinner;

    public static String subjectString, classString, activityName, activityId;
    int cardBackground;

    /**
     *You shouldn't define first page = 0
     * Let's define first page = 'viewpager size' to make endless carousel
    **/

    public static int FIRST_PAGE = 4;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_components);
        ImageView subjectImage = findViewById(R.id.subject_image);
        TextView classDetails = findViewById(R.id.class_details);
        TextView subjectName = findViewById(R.id.subject_name);
        TextView componentDetails = findViewById(R.id.component_details);
        LinearLayout linearLayout = findViewById(R.id.linear2);

        findViewById(R.id.ic_competition_updates).setOnClickListener(this);
        findViewById(R.id.imgBack).setOnClickListener(this);

        intent = getIntent();
        subjectString = getIntent().getStringExtra("subjectkiid");
        classString = getIntent().getStringExtra("uclass");
        activityId = getIntent().getStringExtra("activitykiid");
        activityName = getIntent().getStringExtra("activityname");
        cardBackground = getIntent().getIntExtra("cardColorPosition", -1);


        //Set subject image
        if (classString.equalsIgnoreCase("1") && subjectString.equalsIgnoreCase("2")
                || classString.equalsIgnoreCase("1") && subjectString.equalsIgnoreCase("13"))
            subjectImage.setImageResource(ListConfig.imagesHighSchool[0]);
        else if (classString.equalsIgnoreCase("1") && subjectString.equalsIgnoreCase("3")
                || classString.equalsIgnoreCase("1") && subjectString.equalsIgnoreCase("14"))
            subjectImage.setImageResource(ListConfig.imagesHighSchool[1]);

        else if (classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("8")
                || classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("17"))
            subjectImage.setImageResource(ListConfig.imagesIntermediate[0]);
        else if (classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("10")
                || classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("18"))
            subjectImage.setImageResource(ListConfig.imagesIntermediate[1]);
        else if (classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("6")
                || classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("19"))
            subjectImage.setImageResource(ListConfig.imagesIntermediate[2]);
        else if (classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("12")
                || classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("20"))
            subjectImage.setImageResource(ListConfig.imagesIntermediate[3]);


        //Set Subject Name
        if (classString.equalsIgnoreCase("1") && subjectString.equalsIgnoreCase("2"))
            subjectName.setText(ListConfig.subjectHighSchool[0]);
        else if(classString.equalsIgnoreCase("1") && subjectString.equalsIgnoreCase("3"))
            subjectName.setText(ListConfig.subjectHighSchool[1]);
        else if(classString.equalsIgnoreCase("1") && subjectString.equalsIgnoreCase("13"))
            subjectName.setText(ListConfig.subjectHighSchool[2]);
        else if(classString.equalsIgnoreCase("1") && subjectString.equalsIgnoreCase("14"))
            subjectName.setText(ListConfig.subjectHighSchool[3]);
        else if(classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("8"))
            subjectName.setText(ListConfig.subjectIntermediateHindi[0]);
        else if(classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("10"))
            subjectName.setText(ListConfig.subjectIntermediateHindi[1]);
        else if(classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("6"))
            subjectName.setText(ListConfig.subjectIntermediateHindi[2]);
        else if(classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("12"))
            subjectName.setText(ListConfig.subjectIntermediateHindi[3]);
        else if(classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("17"))
            subjectName.setText(ListConfig.subjectIntermediateEnglsih[0]);
        else if(classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("18"))
            subjectName.setText(ListConfig.subjectIntermediateEnglsih[1]);
        else if(classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("19"))
            subjectName.setText(ListConfig.subjectIntermediateEnglsih[2]);
        else if(classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("20"))
            subjectName.setText(ListConfig.subjectIntermediateEnglsih[3]);

        if (classString.equalsIgnoreCase("1"))
            classDetails.setText(getString(R.string.class_details, "10th"));
        else if (classString.equalsIgnoreCase("2"))
            classDetails.setText(getString(R.string.class_details, "11th"));
        else if (classString.equalsIgnoreCase("3"))
            classDetails.setText(getString(R.string.class_details, "12th"));
        else if (classString.equalsIgnoreCase("4"))
            classDetails.setText(getString(R.string.class_details, "9th"));

        componentDetails.setText(activityName);

        switch (cardBackground%10){
            case 0:
                linearLayout.setBackgroundColor(ComponentActivity.this.getResources().getColor(R.color.color1));
                break;
            case 1:
                linearLayout.setBackgroundColor(ComponentActivity.this.getResources().getColor(R.color.color2));
                break;
            case 2:
                linearLayout.setBackgroundColor(ComponentActivity.this.getResources().getColor(R.color.color3));
                break;
            case 3:
                linearLayout.setBackgroundColor(ComponentActivity.this.getResources().getColor(R.color.color4));
                break;
            case 4:
                linearLayout.setBackgroundColor(ComponentActivity.this.getResources().getColor(R.color.color5));
                break;
            case 5:
                linearLayout.setBackgroundColor(ComponentActivity.this.getResources().getColor(R.color.color6));
                break;
            case 6:
                linearLayout.setBackgroundColor(ComponentActivity.this.getResources().getColor(R.color.color7));
                break;
            case 7:
                linearLayout.setBackgroundColor(ComponentActivity.this.getResources().getColor(R.color.color8));
                break;
            case 8:
                linearLayout.setBackgroundColor(ComponentActivity.this.getResources().getColor(R.color.color9));
                break;
            case 9:
                linearLayout.setBackgroundColor(ComponentActivity.this.getResources().getColor(R.color.color10));
                break;
        }

        loadFragment(new ComponentDetailsFragment());

    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.imgBack:
                finish();
                break;
            case R.id.ic_competition_updates:
                intent = new Intent(ComponentActivity.this, CompetitionUpdatesActivity.class);
                startActivity(intent);
                break;
        }
    }


    /**
     * loading fragment into FrameLayout
     *
     * @param fragment
     */
    @SuppressWarnings("JavaDoc")
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutLogin, fragment);
        transaction.commit();
    }


}

