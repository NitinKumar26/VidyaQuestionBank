package `in`.completecourse.questionbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import `in`.completecourse.questionbank.R
import android.widget.TextView
import android.widget.LinearLayout
import `in`.completecourse.questionbank.utils.ListConfig
import `in`.completecourse.questionbank.fragment.ComponentDetailsFragment
import `in`.completecourse.questionbank.CompetitionUpdatesActivity
import android.widget.Spinner
import androidx.fragment.app.Fragment

class ComponentActivity : AppCompatActivity(), View.OnClickListener {
    private val competitionUpdatesImage: ImageView? = null
    var cardBackground = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_components)

        val subjectImage = findViewById<ImageView>(R.id.subject_image)
        val classDetails = findViewById<TextView>(R.id.class_details)
        val subjectName = findViewById<TextView>(R.id.subject_name)
        val componentDetails = findViewById<TextView>(R.id.component_details)
        val linearLayout = findViewById<LinearLayout>(R.id.linear2)

        findViewById<View>(R.id.ic_competition_updates).setOnClickListener(this)
        findViewById<View>(R.id.imgBack).setOnClickListener(this)

        Companion.intent = intent
        subjectString = intent.getStringExtra("subjectkiid")
        classString = intent.getStringExtra("uclass")
        activityId = intent.getStringExtra("activitykiid")
        activityName = intent.getStringExtra("activityname")
        cardBackground = intent.getIntExtra("cardColorPosition", -1)

        //Set subject image
        if (classString.equals("1", ignoreCase = true) && subjectString.equals("2", ignoreCase = true)
                || classString.equals("1", ignoreCase = true) && subjectString.equals("13", ignoreCase = true)) subjectImage.setImageResource(ListConfig.imagesHighSchool.get(0)) else if (classString.equals("1", ignoreCase = true) && subjectString.equals("3", ignoreCase = true)
                || classString.equals("1", ignoreCase = true) && subjectString.equals("14", ignoreCase = true)) subjectImage.setImageResource(ListConfig.imagesHighSchool.get(1)) else if (classString.equals("3", ignoreCase = true) && subjectString.equals("8", ignoreCase = true)
                || classString.equals("3", ignoreCase = true) && subjectString.equals("17", ignoreCase = true)) subjectImage.setImageResource(ListConfig.imagesIntermediate.get(0)) else if (classString.equals("3", ignoreCase = true) && subjectString.equals("10", ignoreCase = true)
                || classString.equals("3", ignoreCase = true) && subjectString.equals("18", ignoreCase = true)) subjectImage.setImageResource(ListConfig.imagesIntermediate.get(1)) else if (classString.equals("3", ignoreCase = true) && subjectString.equals("6", ignoreCase = true)
                || classString.equals("3", ignoreCase = true) && subjectString.equals("19", ignoreCase = true)) subjectImage.setImageResource(ListConfig.imagesIntermediate.get(2)) else if (classString.equals("3", ignoreCase = true) && subjectString.equals("12", ignoreCase = true)
                || classString.equals("3", ignoreCase = true) && subjectString.equals("20", ignoreCase = true)) subjectImage.setImageResource(ListConfig.imagesIntermediate.get(3))


        //Set Subject Name
        if (classString.equals("1", ignoreCase = true) && subjectString.equals("2", ignoreCase = true)) subjectName.setText(ListConfig.subjectHighSchool.get(0)) else if (classString.equals("1", ignoreCase = true) && subjectString.equals("3", ignoreCase = true)) subjectName.setText(ListConfig.subjectHighSchool.get(1)) else if (classString.equals("1", ignoreCase = true) && subjectString.equals("13", ignoreCase = true)) subjectName.setText(ListConfig.subjectHighSchool.get(2)) else if (classString.equals("1", ignoreCase = true) && subjectString.equals("14", ignoreCase = true)) subjectName.setText(ListConfig.subjectHighSchool.get(3)) else if (classString.equals("3", ignoreCase = true) && subjectString.equals("8", ignoreCase = true)) subjectName.setText(ListConfig.subjectIntermediateHindi.get(0)) else if (classString.equals("3", ignoreCase = true) && subjectString.equals("10", ignoreCase = true)) subjectName.setText(ListConfig.subjectIntermediateHindi.get(1)) else if (classString.equals("3", ignoreCase = true) && subjectString.equals("6", ignoreCase = true)) subjectName.setText(ListConfig.subjectIntermediateHindi.get(2)) else if (classString.equals("3", ignoreCase = true) && subjectString.equals("12", ignoreCase = true)) subjectName.setText(ListConfig.subjectIntermediateHindi.get(3)) else if (classString.equals("3", ignoreCase = true) && subjectString.equals("17", ignoreCase = true)) subjectName.setText(ListConfig.subjectIntermediateEnglsih.get(0)) else if (classString.equals("3", ignoreCase = true) && subjectString.equals("18", ignoreCase = true)) subjectName.setText(ListConfig.subjectIntermediateEnglsih.get(1)) else if (classString.equals("3", ignoreCase = true) && subjectString.equals("19", ignoreCase = true)) subjectName.setText(ListConfig.subjectIntermediateEnglsih.get(2)) else if (classString.equals("3", ignoreCase = true) && subjectString.equals("20", ignoreCase = true)) subjectName.setText(ListConfig.subjectIntermediateEnglsih.get(3))
        if (classString.equals("1", ignoreCase = true)) classDetails.text = getString(R.string.class_details, "10th") else if (classString.equals("2", ignoreCase = true)) classDetails.text = getString(R.string.class_details, "11th") else if (classString.equals("3", ignoreCase = true)) classDetails.text = getString(R.string.class_details, "12th") else if (classString.equals("4", ignoreCase = true)) classDetails.text = getString(R.string.class_details, "9th")
        componentDetails.text = activityName
        when (cardBackground % 10) {
            0 -> linearLayout.setBackgroundColor(this@ComponentActivity.resources.getColor(R.color.color1))
            1 -> linearLayout.setBackgroundColor(this@ComponentActivity.resources.getColor(R.color.color2))
            2 -> linearLayout.setBackgroundColor(this@ComponentActivity.resources.getColor(R.color.color3))
            3 -> linearLayout.setBackgroundColor(this@ComponentActivity.resources.getColor(R.color.color4))
            4 -> linearLayout.setBackgroundColor(this@ComponentActivity.resources.getColor(R.color.color5))
            5 -> linearLayout.setBackgroundColor(this@ComponentActivity.resources.getColor(R.color.color6))
            6 -> linearLayout.setBackgroundColor(this@ComponentActivity.resources.getColor(R.color.color7))
            7 -> linearLayout.setBackgroundColor(this@ComponentActivity.resources.getColor(R.color.color8))
            8 -> linearLayout.setBackgroundColor(this@ComponentActivity.resources.getColor(R.color.color9))
            9 -> linearLayout.setBackgroundColor(this@ComponentActivity.resources.getColor(R.color.color10))
        }
        loadFragment(ComponentDetailsFragment())
    }

    override fun onClick(view: View) {
        val intent: Intent
        when (view.id) {
            R.id.imgBack -> finish()
            R.id.ic_competition_updates -> {
                intent = Intent(this@ComponentActivity, CompetitionUpdatesActivity::class.java)
                startActivity(intent)
            }
        }
    }

    /**
     * loading fragment into FrameLayout
     *
     * @param fragment
     */
    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayoutLogin, fragment)
        transaction.commit()
    }

    companion object {
        var intent: Intent? = null
        var classSpinner: Spinner? = null
        var subjectString: String? = null
        var classString: String? = null
        var activityName: String? = null
        var activityId: String? = null

        /**
         * You shouldn't define first page = 0
         * Let's define first page = 'viewpager size' to make endless carousel
         */
        var FIRST_PAGE = 4
    }
}