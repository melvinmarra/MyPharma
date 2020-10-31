package fr.isen.emelian.mypharma.Pro

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import fr.isen.emelian.mypharma.Chat.LatestChatActivityPro
import fr.isen.emelian.mypharma.Client.ProfileManager.valueListenerAdapter
import fr.isen.emelian.mypharma.DataClass.Pharmacy
import fr.isen.emelian.mypharma.MainActivity
import fr.isen.emelian.mypharma.Pro.PrescriptionManager.ListRequest
import fr.isen.emelian.mypharma.Pro.ProfileManager.ProfilePharmacy
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_home_pro.*

class HomeProActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference
    lateinit var mStorage: FirebaseStorage
    lateinit var mPharma: Pharmacy

    lateinit var sharedPreferences: SharedPreferences
    private val USER_PREFS = "user_prefs"

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_pro)

        supportActionBar?.title = "Welcome on MyPharma Professional"

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mStorage = FirebaseStorage.getInstance()


        sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)

        fun currentUserReference() =
            mDatabase.child("Pharmacies").child(mAuth.currentUser!!.uid)


        mDatabase.keepSynced(true)
        currentUserReference().addListenerForSingleValueEvent(
            valueListenerAdapter {
                mPharma = it.asUser()!!
                current_user_tv.text = mPharma.currentUser
                place_name_current.text = mPharma.name
                textView5.text = mPharma.box_two

                if(textView5.text == "fill"){
                    textView5.setTextColor(R.color.red)
                }else if(textView5.text == "empty"){
                    textView5.setTextColor(R.color.green)
                }

            })


        logout_iv_pro.setOnClickListener{
            Toast.makeText(applicationContext, "Déconnexion réussi !", Toast.LENGTH_SHORT).show()
            this.finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
        }

        logout_iv_pro.setOnClickListener{
            Toast.makeText(applicationContext, "Déconnexion réussi !", Toast.LENGTH_SHORT).show()
            this.finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
        }

        pharma_info.setOnClickListener{
            val intent = Intent(this, ProfilePharmacy::class.java)
            startActivity(intent)
        }

        chat_home_pro.setOnClickListener{
            val intent = Intent(this, LatestChatActivityPro::class.java)
            startActivity(intent)
        }

        newPro.setOnClickListener{
            val intent = Intent(this, ListRequest::class.java)
            startActivity(intent)
        }

    }

    fun DataSnapshot.asUser(): Pharmacy? =
        key?.let { getValue(Pharmacy::class.java)?.copy(id = it) }


}
