package `in`.completecourse.questionbank

import `in`.completecourse.questionbank.adapter.SpinAdapterNew
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val userClassArray = arrayOf("Choose Class", "9", "10", "11", "12")
        val spinAdapter = SpinAdapterNew(this, R.layout.spinner_row, userClassArray)
        classSelection.adapter = spinAdapter
        getUserProfile(FirebaseAuth.getInstance().uid)

        btn_done.setOnClickListener{
            done()
        }
    }

    fun done() {
        val username: String
        var userClass: String? = null
        var userEmail: String
        var userSchool: String
        var contact: String

        username = edTv_username.text.toString()
        userEmail = edTv_email.text.toString()
        userSchool = edTv_school.text.toString()
        contact = edTv_contact.text.toString()

        if (userEmail == "Not Provided") userEmail = ""
        if (userSchool == "Not Provided") userSchool = ""
        if (contact == "Not Provided") contact = ""
        if (classSelection.selectedItemPosition == 0)
            userClass = ""

        else if (classSelection.selectedItemPosition == 1)
            userClass = "9th"
        else if (classSelection.selectedItemPosition == 2)
            userClass = "10th"
        else if (classSelection.selectedItemPosition == 3)
            userClass = "11th"
        else if (classSelection.selectedItemPosition == 4)
            userClass = "12th"

        if (userClass != null) {
            if (!username.isEmpty() && !userEmail.isEmpty() && !userClass.isEmpty() && !userSchool.isEmpty() && !contact.isEmpty()) {
                updateProfile(username, userClass, userEmail, userSchool, contact)
            } else Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUserProfile(userId: String?) {
        FirebaseFirestore.getInstance().collection("qb_users").document(userId!!).get().addOnCompleteListener { task: Task<DocumentSnapshot?>? -> progressBar!!.visibility = View.GONE }.addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
            edTv_username.setText(documentSnapshot.getString("name"))
            if (documentSnapshot.getString("email") == null)
                edTv_email.setText("Not Provided")
            else
                edTv_email.setText(documentSnapshot.getString("email"))
            val userClass = documentSnapshot.getString("class")
            if (userClass == null)
                classSelection.setSelection(0)
            else if (userClass == "9th")
                classSelection.setSelection(1)
            else if (userClass == "10th")
                classSelection.setSelection(2)
            else if (userClass == "11th")
                classSelection.setSelection(3)
            else if (userClass == "12th")
                classSelection.setSelection(4)
            if (documentSnapshot.getString("school") == null)
                edTv_school.setText("Not Provided")
            else
                edTv_school.setText(documentSnapshot.getString("school"))
            if (documentSnapshot.getString("phone") == null)
                edTv_contact.setText("--")
            else
                edTv_contact.setText(documentSnapshot.getString("phone"))
        }.addOnFailureListener { e: Exception -> Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show() }
    }

    private fun updateProfile(username: String, userClass: String, userEmail: String, userSchool: String, contact: String) {
        progressBar!!.visibility = View.VISIBLE
        val data: MutableMap<String, Any> = HashMap()
        data["name"] = username
        data["class"] = userClass
        data["email"] = userEmail
        data["school"] = userSchool
        data["phone"] = contact
        if (FirebaseAuth.getInstance().uid != null) {
            FirebaseFirestore.getInstance().collection("qb_users")
                    .document(FirebaseAuth.getInstance().uid!!).set(data)
                    .addOnSuccessListener { aVoid: Void? ->
                        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e: Exception -> Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show() }
                    .addOnCompleteListener { task: Task<Void?>? -> progressBar!!.visibility = View.GONE }
        }
    }
}