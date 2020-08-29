package in.completecourse.questionbank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import in.completecourse.questionbank.fragment.ClassDetailsFragment;
import in.completecourse.questionbank.helper.HelperMethods;
import in.completecourse.questionbank.utils.ListConfig;

public class SubjectActivity extends AppCompatActivity implements View.OnClickListener {
    public static Intent intent;
    public static String subjectString, classString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        ImageView subjectImage = findViewById(R.id.subject_image);
        TextView classDetails = findViewById(R.id.class_details);
        TextView subjectName = findViewById(R.id.subject_name);

        findViewById(R.id.ic_competition_updates).setOnClickListener(this);

        intent = getIntent();
        subjectString = getIntent().getStringExtra("subjectkiid");
        classString = getIntent().getStringExtra("uclass");

        findViewById(R.id.imgBack).setOnClickListener(this);

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

        HelperMethods.INSTANCE.loadFragment(new ClassDetailsFragment(), SubjectActivity.this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.imgBack:
                SubjectActivity.this.finish();
                break;
            case R.id.ic_competition_updates:
                intent = new Intent(SubjectActivity.this, CompetitionUpdatesActivity.class);
                intent.putExtra("class", classString);
                startActivity(intent);
                break;
        }
    }

}
