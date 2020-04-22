package fr.isen.emelian.mypharma.Client.PrescriptionManager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import fr.isen.emelian.mypharma.Chat.LatestChatActivity
import fr.isen.emelian.mypharma.Client.HomeActivity
import fr.isen.emelian.mypharma.Client.Maps.MapsActivity
import fr.isen.emelian.mypharma.Client.ProfileManager.ProfileActivity
import fr.isen.emelian.mypharma.Client.ProfileManager.valueListenerAdapter
import fr.isen.emelian.mypharma.DataClass.Pharmacy
import fr.isen.emelian.mypharma.DataClass.User

import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_overview.*

class OverviewActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference
    lateinit var mStorage: FirebaseStorage
    lateinit var mUser: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        supportActionBar?.title = "Overview"

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mStorage = FirebaseStorage.getInstance()

        pharmaName_overview.text = SendOption.currentPharmacy?.name
        phoneOver.text = SendOption.currentPharmacy?.phone
        emailOver.text = SendOption.currentPharmacy?.email
        streetOver.text = SendOption.currentPharmacy?.address
        postcodeOver.text = SendOption.currentPharmacy?.postcode
        cityOver.text = SendOption.currentPharmacy?.city
        stateOver.text = SendOption.currentPharmacy?.country

        Picasso.get()
            .load(SendOption.currentPharmacy?.profileImageUrl)
            .into(iv_pict_overview)

        fun currentUserReference() =
            mDatabase.child("DataUsers").child(mAuth.currentUser!!.uid)
        currentUserReference().addListenerForSingleValueEvent(
            valueListenerAdapter {
                mUser = it.asUser()!!

                val test_two = mUser.ordoUrl_two
                val builder = androidx.appcompat.app.AlertDialog.Builder(this)

                Picasso.get()
                    .load(mUser.ordoUrl_one)
                    .into(presOverOne)

            })

        showInfo.setOnClickListener {
            val intent = Intent(this, ViewerInfo::class.java)
            startActivity(intent)
        }

        chat_menu_overview.setOnClickListener {
            val intent_menu = Intent(this, LatestChatActivity::class.java)
            startActivity(intent_menu)
        }

        home_menu_overview.setOnClickListener {
            val intent_menu = Intent(this, HomeActivity::class.java)
            startActivity(intent_menu)
        }

        profile_menu_overview.setOnClickListener {
            val intent_menu = Intent(this, ProfileActivity::class.java)
            startActivity(intent_menu)
        }

        location_menu_overview.setOnClickListener {
            val intent_menu = Intent(this, MapsActivity::class.java)
            startActivity(intent_menu)
        }
    }

    fun DataSnapshot.asUser(): User? =
        key?.let { getValue(User::class.java)?.copy(id = it) }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.send_prescription -> {

                val uid = FirebaseAuth.getInstance().uid ?: ""
                val ref = mDatabase.child("DataUsers").child(uid).child("stateRequest")
                    .setValue("send")
                Toast.makeText(applicationContext, "Prescription(s) successfully send to the pharmacy", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?) : Boolean {
        menuInflater.inflate(R.menu.send, menu)
        return super.onCreateOptionsMenu(menu)
    }
}

