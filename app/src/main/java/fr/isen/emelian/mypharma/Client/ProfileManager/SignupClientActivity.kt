package fr.isen.emelian.mypharma.Client.ProfileManager

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import fr.isen.emelian.mypharma.DataClass.User
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_signup_client.*
import kotlinx.android.synthetic.main.activity_signup_client.firstname
import java.text.SimpleDateFormat
import java.util.*


class SignupClientActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val PASSWORD_REGEX = """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#${'$'}%!\-_?&])(?=\S+${'$'}).{12,}""".toRegex()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_client)

        supportActionBar?.title = "Sign Up"

        auth = FirebaseAuth.getInstance()

        dateOfBirth.setOnClickListener {
            this.showDatePicker(dateSetListener)
        }

    }


    private fun checkField() {


        if (email.text.toString().isEmpty() || mdp.text.toString().isEmpty() || mdpConfirm.text.toString().isEmpty() || firstname.text.toString().isEmpty()
            || lastname.text.toString().isEmpty() || phoneNumber.text.toString().isEmpty()
        ) {
            Toast.makeText(applicationContext, "Every Field must be fill ! ", Toast.LENGTH_LONG).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            Toast.makeText(applicationContext, "Invalid Email ", Toast.LENGTH_LONG).show()
        } else if (mdp.text.toString().length < 8) {
            Toast.makeText(applicationContext, "Password should be longer than 8 characters ", Toast.LENGTH_LONG).show()
        } else if (!Patterns.PHONE.matcher(phoneNumber.text.toString()).matches() || phoneNumber.text.toString().length < 10 || phoneNumber.text.toString().length > 10) {
            Toast.makeText(applicationContext, "Invalid phone number ", Toast.LENGTH_LONG).show()
        } else if (mdp.text.toString() != mdpConfirm.text.toString()) {
            Toast.makeText(applicationContext, "Password and Confirm Password fields must be equals ", Toast.LENGTH_LONG).show()
        } else if (secu.text.toString().length < 13 || secu.text.toString().length > 13) {
            Toast.makeText(applicationContext, "Invalid security social number ", Toast.LENGTH_LONG).show()
        } else if (!PASSWORD_REGEX.matches(mdp.text.toString())) {
            Toast.makeText(
                applicationContext,
                "Password must have:\n1 Uppercase Letter\n1 Lowercase Letter\n1 Special Character\n1 number",
                Toast.LENGTH_LONG
            ).show()
        } else {
            signUp()

        }
    }

    private fun signUp(){

        auth.createUserWithEmailAndPassword(email.text.toString(), mdp.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    sendEmailVerification()

                    fillRealTimeDatabase(firstname.text.toString(), lastname.text.toString(), dateOfBirth.text.toString(), phoneNumber.text.toString(), email.text.toString(), mdp.text.toString(), secu.text.toString(),"", "","","")

                    startActivity(Intent(this, LoginClientActivity::class.java))
                    Toast.makeText(baseContext, "Authentication worked.",
                        Toast.LENGTH_SHORT).show()

                    finish()

                } else {
                    Toast.makeText(baseContext, "Email already used.",
                        Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun fillRealTimeDatabase(firstName: String, lastName: String, dateOfBirth: String, secu: String, email: String, phone: String, password: String, ordo_one: String, latestPharmacy: String, stateRequest: String, urlLatest: String) {


        val userId = auth.currentUser?.uid.toString()
        val users = User(
            userId,
            firstName,
            lastName,
            dateOfBirth,
            phone,
            email,
            password,
            secu,
            ordo_one,
            latestPharmacy,
            stateRequest,
            urlLatest
        )



        FirebaseDatabase.getInstance().getReference("DataUsers").child(userId).setValue(users).addOnCompleteListener {
            Toast.makeText(applicationContext, "Registered", Toast.LENGTH_LONG).show()
        }
    }




    private fun sendEmailVerification() {
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Verification email sent to ${user.email} ", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(baseContext, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                }
            }
    }


    val cal = Calendar.getInstance()
    val dateSetListener =
        DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
            dateOfBirth.text = sdf.format(cal.time)
        }


    private fun showDatePicker(dateSetListener: DatePickerDialog.OnDateSetListener){
        val cal = Calendar.getInstance()
        DatePickerDialog(
            this@SignupClientActivity, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.confirmationButton -> {
                checkField()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?) : Boolean {
        menuInflater.inflate(R.menu.confirm, menu)
        return super.onCreateOptionsMenu(menu)
    }
    
}




/*Complémentaire (mutuelle) OBLIGATOIRE
Origanisme de rattachement sociale / Caisse
rattachement sociale = mieux

(médecin traitant)


photo carte id + identité + complémentaire
*/