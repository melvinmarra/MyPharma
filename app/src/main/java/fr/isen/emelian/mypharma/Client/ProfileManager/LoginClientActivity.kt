package fr.isen.emelian.mypharma.Client.ProfileManager

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import fr.isen.emelian.mypharma.Client.HomeActivity
import fr.isen.emelian.mypharma.DataClass.User
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_login_client.*

class LoginClientActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference
    lateinit var mStorage: FirebaseStorage
    lateinit var mUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_client)
        auth = FirebaseAuth.getInstance()

        mDatabase = FirebaseDatabase.getInstance().reference
        mStorage = FirebaseStorage.getInstance()


        signupButton.setOnClickListener {
            intent = Intent(this, SignupClientActivity::class.java)
            startActivity(intent)
        }

        validateButtonLogin.setOnClickListener{
            signIn(emailLogin.text.toString(), passwordLogin.text.toString())
        }

        buttonForgot.setOnClickListener{

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Enter your email (an email will be send)")
            val view = layoutInflater.inflate(R.layout.activity_forgot_password, null)
            val username = view.findViewById<EditText>(R.id.et_username)
            builder.setView(view)
            builder.setPositiveButton("Reset", DialogInterface.OnClickListener { _, _ ->
                forgotPassword(username)
            })
            builder.setNegativeButton("Close", DialogInterface.OnClickListener { _, _ -> })
            builder.show()
        }
    }

    private fun forgotPassword(username: EditText){
        if (username.text.toString().isEmpty()){
            Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()){
            Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show()
            return
        }


        auth.sendPasswordResetEmail(username.text.toString())
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "Email sent", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Error, try again later", Toast.LENGTH_SHORT).show()
                }
            }
    }


    fun signIn(email: String, password: String){
        if (emailLogin.text.toString().isEmpty() || passwordLogin.text.toString().isEmpty()){
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
        }

        else {

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {

                    fun currentUserReference() =
                        mDatabase.child("DataUsers").child(auth.currentUser!!.uid)

                    currentUserReference().addListenerForSingleValueEvent(
                        valueListenerAdapter {
                            mUser = it.asUser()!!
                            val ref =
                                mUser.pictureUrl

                            if (ref != "") {


                                intent = Intent(
                                    this,
                                    HomeActivity::class.java
                                )
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                Toast.makeText(
                                    this,
                                    "Welcome " + emailLogin.text.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {

                                intent = Intent(
                                    this,
                                    PhotoChooseActivity::class.java
                                )
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)

                            }
                        })

                } else {
                    Toast.makeText(this, "Wrong email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun DataSnapshot.asUser(): User? =
        key?.let { getValue(User::class.java)?.copy(id = it) }
}
