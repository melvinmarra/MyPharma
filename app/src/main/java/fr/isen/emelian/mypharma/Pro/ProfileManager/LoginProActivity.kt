package fr.isen.emelian.mypharma.Pro.ProfileManager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import fr.isen.emelian.mypharma.DataClass.Pharmacy
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_login_pro.*

class LoginProActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference
    lateinit var mStorage: FirebaseStorage
    lateinit var mPharma: Pharmacy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_pro)
        mAuth = FirebaseAuth.getInstance()

        validateButtonLoginPro.setOnClickListener {
            signIn(emailLoginPro.text.toString(), passwordLoginPro.text.toString())
        }
    }



    fun signIn(email: String, password: String) {
        if (emailLoginPro.text.toString().isEmpty() || passwordLoginPro.text.toString().isEmpty()) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
        } else {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {

                   // fillRealTimeDatabase("Pharmacie de Quissac", emailLoginPro.text.toString(),"0605040302","30260","Quissac","Rue albert premier","France","Emma","Lilian","Melvin","","")
                                intent = Intent(this, VerificationIdentityPro::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)



                } else {
                    Toast.makeText(this, "Wrong email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }








    private fun fillRealTimeDatabase(name: String, email: String, phone: String, postcode: String, city: String, address: String, country: String,
                                     employee_one: String, employee_two: String,  employee_three: String, currentUser: String, profileImageUrl: String) {


        val pharmaId = mAuth.currentUser?.uid.toString()
        val pharmacies = Pharmacy(
            pharmaId,
            name.toLowerCase(),
            email,
            phone,
            postcode,
            city,
            address,
            country,
            employee_one,
            employee_two,
            employee_three,
            currentUser,
            profileImageUrl
        )



        FirebaseDatabase.getInstance().getReference("Pharmacies").child(pharmaId).setValue(pharmacies).addOnCompleteListener {
            Toast.makeText(applicationContext, "Registered", Toast.LENGTH_LONG).show()
        }
    }

}
