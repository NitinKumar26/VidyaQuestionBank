package in.completecourse.questionbank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import in.completecourse.questionbank.adapter.SpinAdapter;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private String[] spinnerItemsArray;
    public static String mClassString;
    public Spinner classSelectionSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle(getResources().getString(R.string.app_name));

        //Find the required Views
        //Hindi Subjects
        findViewById(R.id.layout_physics).setOnClickListener(this);
        findViewById(R.id.layout_chemistry).setOnClickListener(this);
        findViewById(R.id.layout_maths).setOnClickListener(this);
        findViewById(R.id.layout_biology).setOnClickListener(this);
        //English Subjects
        findViewById(R.id.layout_physics_english).setOnClickListener(this);
        findViewById(R.id.layout_chemistry_english).setOnClickListener(this);
        findViewById(R.id.layout_maths_english).setOnClickListener(this);
        findViewById(R.id.layout_biology_english).setOnClickListener(this);

        //10th Subjects
        findViewById(R.id.layout_vigyaan).setOnClickListener(this);
        findViewById(R.id.layout_ganit).setOnClickListener(this);
        findViewById(R.id.layout_science).setOnClickListener(this);
        findViewById(R.id.layout_mathematics).setOnClickListener(this);

        classSelectionSpinner = findViewById(R.id.classSelection);
        spinnerItemsArray = new String[]{"10", "12", "12"};
        SpinAdapter spinnerAdapter = new SpinAdapter(SearchActivity.this, R.layout.spinner_row , spinnerItemsArray);
        classSelectionSpinner.setAdapter(spinnerAdapter);
        classSelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mClassString = spinnerItemsArray[position];

                if (position == 0){
                    //show 10th class layout
                    findViewById(R.id.tenth_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.english_layout).setVisibility(View.GONE);
                    findViewById(R.id.hindi_layout).setVisibility(View.GONE);
                }else if (position == 1){
                    //Show 12th hindi layout
                    findViewById(R.id.hindi_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.english_layout).setVisibility(View.GONE);
                    findViewById(R.id.tenth_layout).setVisibility(View.GONE);
                }else{
                    //Show 12th english layout
                    findViewById(R.id.english_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.hindi_layout).setVisibility(View.GONE);
                    findViewById(R.id.tenth_layout).setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        int classString = classSelectionSpinner.getSelectedItemPosition();
        switch (view.getId()){
            case R.id.layout_vigyaan:
                intent = new Intent(this, SubjectActivity.class);
                intent.putExtra("uclass", "1");
                intent.putExtra("subjectkiid", "2");
                startActivity(intent);
                break;
            case R.id.layout_ganit:
                intent = new Intent(this, SubjectActivity.class);
                intent.putExtra("uclass", "1");
                intent.putExtra("subjectkiid", "3");
                startActivity(intent);
                break;
            case R.id.layout_science:
                intent = new Intent(this, SubjectActivity.class);
                intent.putExtra("uclass", "1");
                intent.putExtra("subjectkiid", "13");
                startActivity(intent);
                break;
            case R.id.layout_mathematics:
                intent = new Intent(this, SubjectActivity.class);
                intent.putExtra("uclass", "1");
                intent.putExtra("subjectkiid", "14");
                startActivity(intent);
                break;
            case R.id.layout_physics_english:
                intent = new Intent(this, SubjectActivity.class);
                intent.putExtra("uclass", "3");
                intent.putExtra("subjectkiid", "17");
                startActivity(intent);
                break;
            case R.id.layout_chemistry_english:
                intent = new Intent(this, SubjectActivity.class);
                intent.putExtra("uclass", "3");
                intent.putExtra("subjectkiid", "18");
                startActivity(intent);
                break;
            case R.id.layout_maths_english:
                intent = new Intent(this, SubjectActivity.class);
                intent.putExtra("uclass", "3");
                intent.putExtra("subjectkiid", "19");
                startActivity(intent);
                break;
            case R.id.layout_biology_english:
                intent = new Intent(this, SubjectActivity.class);
                intent.putExtra("uclass", "3");
                intent.putExtra("subjectkiid", "20");
                startActivity(intent);
                break;
            case R.id.layout_physics:
                intent = new Intent(this, SubjectActivity.class);
                intent.putExtra("uclass", "3");
                intent.putExtra("subjectkiid", "8");
                startActivity(intent);
                break;
            case R.id.layout_chemistry:
                intent = new Intent(this, SubjectActivity.class);
                intent.putExtra("uclass", "3");
                intent.putExtra("subjectkiid", "10");
                startActivity(intent);
                break;
            case R.id.layout_maths:
                intent = new Intent(this, SubjectActivity.class);
                intent.putExtra("uclass", "3");
                intent.putExtra("subjectkiid", "6");
                startActivity(intent);
                break;
            case R.id.layout_biology:
                intent = new Intent(this, SubjectActivity.class);
                intent.putExtra("uclass", "3");
                intent.putExtra("subjectkiid", "12");
                startActivity(intent);
                break;
        }
    }
}
