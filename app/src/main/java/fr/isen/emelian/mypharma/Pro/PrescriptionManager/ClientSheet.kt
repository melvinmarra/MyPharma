package fr.isen.emelian.mypharma.Pro.PrescriptionManager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import fr.isen.emelian.mypharma.DataClass.User
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_client_sheet.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ClientSheet : AppCompatActivity() {

    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_sheet)

        user = intent.getParcelableExtra<User>(ListRequest.USER_KEY)
        supportActionBar?.title = "Patient cheet"

        val age = calculAge(ListRequest.currentUser?.date_of_birth.toString())

        sheet_firstname.text = ListRequest.currentUser?.firstName
        sheet_lastname.text = ListRequest.currentUser?.lastName
        sheet_dob.text = ListRequest.currentUser?.date_of_birth
        sheet_age.text = age.toString()
        sheet_email.text = ListRequest.currentUser?.email
        sheet_phone.text = ListRequest.currentUser?.phone
        sheet_secu.text = ListRequest.currentUser?.secu_number

        Picasso.get()
            .load(ListRequest.currentUser?.pictureUrl)
            .into(sheet_picture)

    }

    private fun calculAge(date: String): Int {

        var age = 0

        try {
            val dates = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).parse(date)
            val today = Calendar.getInstance();
            val birth = Calendar.getInstance();

            birth.time = dates

            val thisYear = today.get(Calendar.YEAR)
            val yearBirth = birth.get(Calendar.YEAR)

            age = thisYear - yearBirth

            val thisMonth = today.get(Calendar.MONTH)
            val birthMonth = birth.get(Calendar.MONTH)

            if(birthMonth > thisMonth){
                age--
            }else if (birthMonth == thisMonth){
                val thisDay = today.get(Calendar.DAY_OF_MONTH)
                val birthDay = birth.get(Calendar.DAY_OF_MONTH)

                if(birthDay > thisDay){
                    age--
                }
            }
        }catch (e: ParseException){
            e.printStackTrace()
        }
        return age
    }
}

