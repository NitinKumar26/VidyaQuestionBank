package `in`.completecourse.questionbank

import `in`.completecourse.questionbank.SearchActivity
import `in`.completecourse.questionbank.adapter.SpinAdapter
import `in`.completecourse.questionbank.adapter.SpinAdapterNew
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), View.OnClickListener {
    private var spinnerItemsArray: Array<String>? = null
    var classSelectionSpinner: Spinner? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        title = resources.getString(R.string.app_name)

        //Find the required Views
        //Hindi Subjects
        layout_physics.setOnClickListener(this)
        layout_chemistry.setOnClickListener(this)
        layout_maths.setOnClickListener(this)
        layout_biology.setOnClickListener(this)
        //English Subjects
        layout_physics_english.setOnClickListener(this)
        layout_chemistry_english.setOnClickListener(this)
        layout_maths_english.setOnClickListener(this)
        layout_biology_english.setOnClickListener(this)

        //10th Subjects
        layout_vigyaan.setOnClickListener(this)
        layout_ganit.setOnClickListener(this)
        layout_science.setOnClickListener(this)
        layout_mathematics.setOnClickListener(this)
        classSelectionSpinner = findViewById(R.id.classSelection)
        spinnerItemsArray = arrayOf("10", "12", "12")
        val spinnerAdapter = SpinAdapter(this@SearchActivity, R.layout.spinner_row, spinnerItemsArray!!)
        classSelectionSpinner?.setAdapter(spinnerAdapter)
        classSelectionSpinner?.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                mClassString = spinnerItemsArray!![position]
                if (position == 0) {
                    //show 10th class layout
                    tenth_layout.visibility = View.VISIBLE
                    english_layout.visibility = View.GONE
                    hindi_layout.visibility = View.GONE
                } else if (position == 1) {
                    //Show 12th hindi layout
                    hindi_layout.visibility = View.VISIBLE
                    english_layout.visibility = View.GONE
                    tenth_layout.visibility = View.GONE
                } else {
                    //Show 12th english layout
                    english_layout.visibility = View.VISIBLE
                    hindi_layout.visibility = View.GONE
                    tenth_layout.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }

    override fun onClick(view: View) {
        val intent: Intent
        val classString = classSelectionSpinner!!.selectedItemPosition
        when (view.id) {
            R.id.layout_vigyaan -> {
                intent = Intent(this, SubjectActivity::class.java)
                intent.putExtra("uclass", "1")
                intent.putExtra("subjectkiid", "2")
                startActivity(intent)
            }
            R.id.layout_ganit -> {
                intent = Intent(this, SubjectActivity::class.java)
                intent.putExtra("uclass", "1")
                intent.putExtra("subjectkiid", "3")
                startActivity(intent)
            }
            R.id.layout_science -> {
                intent = Intent(this, SubjectActivity::class.java)
                intent.putExtra("uclass", "1")
                intent.putExtra("subjectkiid", "13")
                startActivity(intent)
            }
            R.id.layout_mathematics -> {
                intent = Intent(this, SubjectActivity::class.java)
                intent.putExtra("uclass", "1")
                intent.putExtra("subjectkiid", "14")
                startActivity(intent)
            }
            R.id.layout_physics_english -> {
                intent = Intent(this, SubjectActivity::class.java)
                intent.putExtra("uclass", "3")
                intent.putExtra("subjectkiid", "17")
                startActivity(intent)
            }
            R.id.layout_chemistry_english -> {
                intent = Intent(this, SubjectActivity::class.java)
                intent.putExtra("uclass", "3")
                intent.putExtra("subjectkiid", "18")
                startActivity(intent)
            }
            R.id.layout_maths_english -> {
                intent = Intent(this, SubjectActivity::class.java)
                intent.putExtra("uclass", "3")
                intent.putExtra("subjectkiid", "19")
                startActivity(intent)
            }
            R.id.layout_biology_english -> {
                intent = Intent(this, SubjectActivity::class.java)
                intent.putExtra("uclass", "3")
                intent.putExtra("subjectkiid", "20")
                startActivity(intent)
            }
            R.id.layout_physics -> {
                intent = Intent(this, SubjectActivity::class.java)
                intent.putExtra("uclass", "3")
                intent.putExtra("subjectkiid", "8")
                startActivity(intent)
            }
            R.id.layout_chemistry -> {
                intent = Intent(this, SubjectActivity::class.java)
                intent.putExtra("uclass", "3")
                intent.putExtra("subjectkiid", "10")
                startActivity(intent)
            }
            R.id.layout_maths -> {
                intent = Intent(this, SubjectActivity::class.java)
                intent.putExtra("uclass", "3")
                intent.putExtra("subjectkiid", "6")
                startActivity(intent)
            }
            R.id.layout_biology -> {
                intent = Intent(this, SubjectActivity::class.java)
                intent.putExtra("uclass", "3")
                intent.putExtra("subjectkiid", "12")
                startActivity(intent)
            }
        }
    }

    companion object {
        var mClassString: String? = null
    }
}