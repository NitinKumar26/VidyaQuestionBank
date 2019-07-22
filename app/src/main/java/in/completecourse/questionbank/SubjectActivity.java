package in.completecourse.questionbank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import in.completecourse.questionbank.fragment.ClassDetailsFragment;
import in.completecourse.questionbank.utils.ListConfig;

public class SubjectActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int count = 4;
    public static Intent intent;
    private ImageView competitionUpdatesImage;
    //public static Spinner classSpinner;
    public static String subjectString, classString;

    /**
     * You shouldn't define first page = 0
     * Let's define first page = 'viewpager size' to make endless carousel
     */
    public static int FIRST_PAGE = 4;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        ImageView subjectImage = findViewById(R.id.subject_image);
        TextView classDetails = findViewById(R.id.class_details);
        TextView subjectName = findViewById(R.id.subject_name);
        ImageView competitionUpdates = findViewById(R.id.ic_competition_updates);
        competitionUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SubjectActivity.this, CompetitionUpdatesActivity.class));

            }
        });



        intent = getIntent();
        subjectString = getIntent().getStringExtra("subjectkiid");
        classString = getIntent().getStringExtra("uclass");

        Log.e("subjectkiid_subA", subjectString);
        Log.e("class_subA", classString);


        if (classString.equalsIgnoreCase("4") && subjectString.equalsIgnoreCase("1")
                || classString.equalsIgnoreCase("1") && subjectString.equalsIgnoreCase("2")
                || classString.equalsIgnoreCase("4") && subjectString.equalsIgnoreCase("16")
                || classString.equalsIgnoreCase("1") && subjectString.equalsIgnoreCase("13")){
            subjectImage.setImageResource(ListConfig.imagesHighSchool[0]);
        }else if (classString.equalsIgnoreCase("4") && subjectString.equalsIgnoreCase("15")
                || classString.equalsIgnoreCase("4") && subjectString.equalsIgnoreCase("4")
                || classString.equalsIgnoreCase("1") && subjectString.equalsIgnoreCase("3")
                || classString.equalsIgnoreCase("1") && subjectString.equalsIgnoreCase("14")){
            subjectImage.setImageResource(ListConfig.imagesHighSchool[2]);
        }else if (classString.equalsIgnoreCase("2") && subjectString.equalsIgnoreCase("5")
                || classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("6")){
            subjectImage.setImageResource(ListConfig.imagesIntermediate[2]);
        }else if (classString.equalsIgnoreCase("2") && subjectString.equalsIgnoreCase("7")
                || classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("8")){
            subjectImage.setImageResource(ListConfig.imagesIntermediate[0]);
        }else if (classString.equalsIgnoreCase("2") && subjectString.equalsIgnoreCase("9")
                || classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("10")){
            subjectImage.setImageResource(ListConfig.imagesIntermediate[1]);
        }else if (classString.equalsIgnoreCase("2") && subjectString.equalsIgnoreCase("11")
                || classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("12")){
            subjectImage.setImageResource(ListConfig.imagesIntermediate[3]);
        }

        if (classString.equalsIgnoreCase("4") && subjectString.equalsIgnoreCase("1")
                || classString.equalsIgnoreCase("1") && subjectString.equalsIgnoreCase("2")){
            subjectName.setText(ListConfig.subjectHighSchool[0]);
        }else if (classString.equalsIgnoreCase("4") && subjectString.equalsIgnoreCase("16")
                || classString.equalsIgnoreCase("1") && subjectString.equalsIgnoreCase("13")){
            subjectName.setText(ListConfig.subjectHighSchool[1]);
        }else if (classString.equalsIgnoreCase("4") && subjectString.equalsIgnoreCase("4")
                || classString.equalsIgnoreCase("1") && subjectString.equalsIgnoreCase("3")){
            subjectName.setText(ListConfig.subjectHighSchool[2]);
        }else if (classString.equalsIgnoreCase("4") && subjectString.equalsIgnoreCase("15")
                || classString.equalsIgnoreCase("1") && subjectString.equalsIgnoreCase("14")){
            subjectName.setText(ListConfig.subjectHighSchool[3]);
        }else if (classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("6")
                || classString.equalsIgnoreCase("2") && subjectString.equalsIgnoreCase("5")){
            subjectName.setText(ListConfig.subjectIntermediate[2]);
        }else if (classString.equalsIgnoreCase("3") && subjectString.equalsIgnoreCase("8")
                || classString.equalsIgnoreCase("2") && subjectString.equalsIgnoreCase("7")){
            subjectImage.setImageResource(ListConfig.imagesIntermediate[3]);
        }

        if (classString.equalsIgnoreCase("1"))
            classDetails.setText(getString(R.string.class_details, "10th"));
        else if (classString.equalsIgnoreCase("2"))
            classDetails.setText(getString(R.string.class_details, "11th"));
        else if (classString.equalsIgnoreCase("3"))
            classDetails.setText(getString(R.string.class_details, "12th"));
        else if (classString.equalsIgnoreCase("4"))
            classDetails.setText(getString(R.string.class_details, "9th"));

        loadFragment(new ClassDetailsFragment());

    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.imgBack:
                finish();
                break;
            case R.id.ic_competition_updates:
                intent = new Intent(SubjectActivity.this, CompetitionUpdatesActivity.class);
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
