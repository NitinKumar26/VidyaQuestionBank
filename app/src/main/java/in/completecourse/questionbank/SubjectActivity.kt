package `in`.completecourse.questionbank

import `in`.completecourse.questionbank.SubjectActivity
import `in`.completecourse.questionbank.fragment.ClassDetailsFragment
import `in`.completecourse.questionbank.helper.HelperMethods
import `in`.completecourse.questionbank.utils.ListConfig
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_subject.*

class SubjectActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject)

        ic_competition_updates.setOnClickListener(this)

        Companion.intent = intent
        subjectString = intent.getStringExtra("subjectkiid")
        classString = intent.getStringExtra("uclass")
        imgBack.setOnClickListener(this)

        //Set subject image
        if (classString.equals("1", ignoreCase = true) && subjectString.equals("2", ignoreCase = true)
                || classString.equals("1", ignoreCase = true) && subjectString.equals("13", ignoreCase = true))
                    subject_image.setImageResource(ListConfig.imagesHighSchool.get(0))
        else if (classString.equals("1", ignoreCase = true) && subjectString.equals("3", ignoreCase = true)
                || classString.equals("1", ignoreCase = true) && subjectString.equals("14", ignoreCase = true))
            subject_image.setImageResource(ListConfig.imagesHighSchool.get(1))
        else if (classString.equals("3", ignoreCase = true) && subjectString.equals("8", ignoreCase = true)
                || classString.equals("3", ignoreCase = true) && subjectString.equals("17", ignoreCase = true))
            subject_image.setImageResource(ListConfig.imagesIntermediate.get(0))
        else if (classString.equals("3", ignoreCase = true) && subjectString.equals("10", ignoreCase = true)
                || classString.equals("3", ignoreCase = true) && subjectString.equals("18", ignoreCase = true))
            subject_image.setImageResource(ListConfig.imagesIntermediate.get(1))
        else if (classString.equals("3", ignoreCase = true) && subjectString.equals("6", ignoreCase = true)
                || classString.equals("3", ignoreCase = true) && subjectString.equals("19", ignoreCase = true))
            subject_image.setImageResource(ListConfig.imagesIntermediate.get(2))
        else if (classString.equals("3", ignoreCase = true) && subjectString.equals("12", ignoreCase = true)
                || classString.equals("3", ignoreCase = true) && subjectString.equals("20", ignoreCase = true))
                    subject_image.setImageResource(ListConfig.imagesIntermediate.get(3))


        //Set Subject Name
        if (classString.equals("1", ignoreCase = true) && subjectString.equals("2", ignoreCase = true))
            subject_name.setText(ListConfig.subjectHighSchool.get(0))
        else if (classString.equals("1", ignoreCase = true) && subjectString.equals("3", ignoreCase = true))
            subject_name.setText(ListConfig.subjectHighSchool.get(1))
        else if (classString.equals("1", ignoreCase = true) && subjectString.equals("13", ignoreCase = true))
            subject_name.setText(ListConfig.subjectHighSchool.get(2))
        else if (classString.equals("1", ignoreCase = true) && subjectString.equals("14", ignoreCase = true))
            subject_name.setText(ListConfig.subjectHighSchool.get(3))
        else if (classString.equals("3", ignoreCase = true) && subjectString.equals("8", ignoreCase = true))
            subject_name.setText(ListConfig.subjectIntermediateHindi.get(0))
        else if (classString.equals("3", ignoreCase = true) && subjectString.equals("10", ignoreCase = true))
            subject_name.setText(ListConfig.subjectIntermediateHindi.get(1))
        else if (classString.equals("3", ignoreCase = true) && subjectString.equals("6", ignoreCase = true))
            subject_name.setText(ListConfig.subjectIntermediateHindi.get(2))
        else if (classString.equals("3", ignoreCase = true) && subjectString.equals("12", ignoreCase = true))
            subject_name.setText(ListConfig.subjectIntermediateHindi.get(3))
        else if (classString.equals("3", ignoreCase = true) && subjectString.equals("17", ignoreCase = true))
            subject_name.setText(ListConfig.subjectIntermediateEnglsih.get(0))
        else if (classString.equals("3", ignoreCase = true) && subjectString.equals("18", ignoreCase = true))
            subject_name.setText(ListConfig.subjectIntermediateEnglsih.get(1))
        else if (classString.equals("3", ignoreCase = true) && subjectString.equals("19", ignoreCase = true))
            subject_name.setText(ListConfig.subjectIntermediateEnglsih.get(2))
        else if (classString.equals("3", ignoreCase = true) && subjectString.equals("20", ignoreCase = true))
            subject_name.setText(ListConfig.subjectIntermediateEnglsih.get(3))

        if (classString.equals("1", ignoreCase = true))
            class_details.text = getString(R.string.class_details, "10th")
        else if (classString.equals("2", ignoreCase = true))
            class_details.text = getString(R.string.class_details, "11th")
        else if (classString.equals("3", ignoreCase = true))
            class_details.text = getString(R.string.class_details, "12th")
        else if (classString.equals("4", ignoreCase = true))
            class_details.text = getString(R.string.class_details, "9th")
        HelperMethods.loadFragment(ClassDetailsFragment(), this@SubjectActivity)
    }

    override fun onClick(view: View) {
        val intent: Intent
        when (view.id) {
            R.id.imgBack -> finish()
            R.id.ic_competition_updates -> {
                intent = Intent(this@SubjectActivity, CompetitionUpdatesActivity::class.java)
                intent.putExtra("class", classString)
                startActivity(intent)
            }
        }
    }

    companion object {
        var intent: Intent? = null
        var subjectString: String? = null
        var classString: String? = null
    }
}