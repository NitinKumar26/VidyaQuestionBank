package `in`.completecourse.questionbank.fragment

import `in`.completecourse.questionbank.MainActivity
import `in`.completecourse.questionbank.R
import `in`.completecourse.questionbank.helper.HelperMethods
import `in`.completecourse.questionbank.helper.PrefManager
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_easy_login.*
import java.util.*

class EasyLoginFragment : Fragment() {
    private var pDialog: ProgressDialog? = null

    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var username: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_easy_login, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pDialog = ProgressDialog(context)
        pDialog!!.setMessage("Please wait...")

        send_verification_code_button.setOnClickListener{
            sendVerificationCode()
        }

        sign_with_google.setOnClickListener{
            signInWithGoogle()
        }
    }

    fun sendVerificationCode() {
        val name = edTv_name.text.toString().trim { it <= ' ' }
        val mobileNumber = edTv_mobile_number.text.toString().trim { it <= ' ' }
        if (HelperMethods.isNetworkAvailable(activity)) {
            if (name.isNotEmpty() && mobileNumber.isNotEmpty()) {
                val bundle = Bundle()
                bundle.putString("username", name)
                bundle.putString("mobileNumber", mobileNumber)
                val otpFragment = OTPFragment()
                otpFragment.arguments = bundle
                HelperMethods.loadFragment(otpFragment, activity as AppCompatActivity?)
            } else {
                Toast.makeText(context, "Please fill the required details", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Please check your Internet connection.", Toast.LENGTH_SHORT).show()
        }
    }

    fun signInWithGoogle() {
        //[START config_sign_in]
        //Configure Google Sign in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        //[END config_sign_in]
        if (context != null) {
            mGoogleSignInClient = GoogleSignIn.getClient(context!!, gso)
            pDialog!!.show()
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //Google sign in was successful, authenticate with Firebase
                Toast.makeText(context, "Google Sign in successful ", Toast.LENGTH_SHORT).show()
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    username = account.displayName
                    //userDocId = account.getId();
                    firebaseAuthWithGoogle(account)
                }
            } catch (e: ApiException) {
                //Google Sign in failed update the UI accordingly
                if (pDialog!!.isShowing) pDialog!!.dismiss()
                Toast.makeText(context, "Sign In Failed", Toast.LENGTH_SHORT).show()
                Log.w("LoginActivity", "Google Sign in Failed", e)
            }
        }
    }

    //[START auth_with_google]
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        if (activity != null) {
            FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener { task: Task<AuthResult?>? -> pDialog!!.hide() }
                    .addOnSuccessListener { authResult: AuthResult ->
                        if (context != null) {
                            if (authResult.user != null) {
                                pDialog!!.show()
                                FirebaseFirestore.getInstance().collection("qb_users")
                                        .document(authResult.user!!.uid).get()
                                        .addOnCompleteListener { pDialog!!.dismiss() }
                                        .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                                            if (documentSnapshot.exists()) {
                                                //User details are already in the database
                                                if (context != null) {
                                                    val prefManager = PrefManager(context)
                                                    prefManager.isFirstTimeLaunch = false
                                                    prefManager.setLogin(true)
                                                    val intent = Intent(context, MainActivity::class.java)
                                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    startActivity(intent)
                                                }
                                            }
                                            else {
                                                pDialog!!.dismiss()
                                                //User details not available in database save them
                                                val userDetails: MutableMap<String, String?> = HashMap()
                                                userDetails["name"] = username
                                                if (FirebaseAuth.getInstance().currentUser != null) {
                                                    userDetails["email"] = FirebaseAuth.getInstance().currentUser!!.email
                                                    userDetails["userid"] = authResult.user!!.uid
                                                    FirebaseFirestore.getInstance().collection("qb_users").document(authResult.user!!.uid).set(userDetails)
                                                            .addOnCompleteListener { pDialog!!.dismiss() }.addOnSuccessListener {
                                                                if (context != null) {
                                                                    val prefManager = PrefManager(context)
                                                                    prefManager.isFirstTimeLaunch = false
                                                                    prefManager.setLogin(true)
                                                                    val intent = Intent(context, MainActivity::class.java)
                                                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                                    startActivity(intent)
                                                                }
                                                            }.addOnFailureListener { e: Exception -> Toast.makeText(context, e.message, Toast.LENGTH_LONG).show() }
                                                }
                                            }
                                        }.addOnFailureListener { e: Exception -> Toast.makeText(context, e.message, Toast.LENGTH_LONG).show() }
                            }
                        }
                    }
        }
    }

    companion object {
        private const val RC_SIGN = 9001
    }
}