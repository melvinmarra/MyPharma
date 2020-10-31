package fr.isen.emelian.mypharma.Client


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.content.SharedPreferences
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import fr.isen.emelian.mypharma.*
import fr.isen.emelian.mypharma.Chat.LatestChatActivity
import fr.isen.emelian.mypharma.Client.Maps.MapsActivity
import fr.isen.emelian.mypharma.Client.PrescriptionManager.SendPrescriptionActivity
import fr.isen.emelian.mypharma.Client.PrescriptionManager.ViewerInfo
import fr.isen.emelian.mypharma.Client.ProfileManager.PartnerPharma
import fr.isen.emelian.mypharma.Client.ProfileManager.ProfileActivity
import fr.isen.emelian.mypharma.Client.ProfileManager.valueListenerAdapter
import fr.isen.emelian.mypharma.DataClass.User
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference
    lateinit var mStorage: FirebaseStorage
    lateinit var mUser: User

    private val USER_PREFS = "user_prefs"
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.title = "Welcome on MyPharma"

        val user = FirebaseAuth.getInstance().currentUser
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        val refresh = Intent(this, HomeActivity::class.java)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mStorage = FirebaseStorage.getInstance()

        sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)

        fun currentUserReference() =
            mDatabase.child("DataUsers").child(mAuth.currentUser!!.uid)


        mDatabase.keepSynced(true)
        currentUserReference().addListenerForSingleValueEvent(
            valueListenerAdapter {
                mUser = it.asUser()!!
                val state = mUser.stateRequest
                firstnameTop.text = mUser.firstName
                lastnameTop.text = mUser.lastName
                dobTop.text = mUser.date_of_birth

                Picasso.get()
                    .load(mUser.pictureUrl)
                    .into(camera_top)

                if(state != ""){

                    noRequest.visibility = View.INVISIBLE
                    cancel.visibility = View.VISIBLE


                    myLastPharmacy.visibility = View.VISIBLE
                    myLastPharmacy.text = mUser.latestPharmacy

                    myLastPrescription.visibility = View.VISIBLE

                    queryState.visibility = View.VISIBLE

                    queryState_tv.visibility = View.VISIBLE
                    queryState_tv.text = mUser.stateRequest

                    pictureLatestPharma.visibility = View.VISIBLE
                    Picasso.get()
                        .load(mUser.urlLatest)
                        .into(pictureLatestPharma)

                    firstLastPres.visibility = View.VISIBLE
                    firstLastPres.setOnClickListener {
                        val intent = Intent(this, ViewerInfo::class.java)
                        startActivity(intent)
                    }

                    if(queryState_tv.text == "send"){
                        queryState_tv.setTextColor(resources.getColor(R.color.blue))
                    }else if(queryState_tv.text == "take in charge"){
                        queryState_tv.setTextColor(resources.getColor(R.color.yellow))
                    }else if(queryState_tv.text == "ready"){
                        queryState_tv.setTextColor(resources.getColor(R.color.green))
                    }

                    cancel.setOnClickListener {
                        with(builder) {
                            setTitle("Are you sure?")
                            setMessage("Do you really want to cancel this request to the pharmacy ?")
                            setPositiveButton("Yes") { dialog, _ ->
                                val uid = FirebaseAuth.getInstance().uid ?: ""
                                mDatabase.child("DataUsers").child(uid).child("ordoUrl_one")
                                    .setValue("")
                                mDatabase.child("DataUsers").child(uid).child("stateRequest")
                                    .setValue("")
                                mDatabase.child("DataUsers").child(uid).child("latestPharmacy")
                                    .setValue("")
                                startActivity(refresh)
                            }
                            setNegativeButton("No") { dialog, _ ->
                            }
                            show()
                        }
                    }

                }
            })

        partner_iv.setOnClickListener{
            val intent = Intent(this, PartnerPharma::class.java)
            startActivity(intent)
        }

        aboutUs.setOnClickListener {
            with(builder) {
                setTitle("About us")
                setMessage("Welcome on MyPharma app. \n\nWe have created this app to make you have better services and help" +
                        " pharmacies.\nIf you any questions about the app and its utilisation, please do not hesitate to contact us by phone or email." +
                        "\n\nPhone : 0612345678\nEmail : mypharma.app@outlook.fr")
                setPositiveButton("Ok") { dialog, _ ->
                }
                show()
            }
        }

        profile_menu_home.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        location_menu_home.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        send_menu_home.setOnClickListener{
            val intent = Intent(this, SendPrescriptionActivity::class.java)
            startActivity(intent)
        }

        chat_menu_home.setOnClickListener{
            val intent = Intent(this, LatestChatActivity::class.java)
            startActivity(intent)
        }


        logout.setOnClickListener{
            Toast.makeText(applicationContext, "Déconnexion réussi !", Toast.LENGTH_SHORT).show()
            this.finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
        }

        logout_iv.setOnClickListener{
            Toast.makeText(applicationContext, "Déconnexion réussi !", Toast.LENGTH_SHORT).show()
            this.finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
        }

    }

    fun DataSnapshot.asUser(): User? =
        key?.let { getValue(User::class.java)?.copy(id = it) }
}

/* if(queryState_tv.text == "send"){
                       queryState_tv.setTextColor(resources.getColor(R.color.blue))
                   }else if(queryState_tv.text == "take in charge"){
                       queryState_tv.setTextColor(resources.getColor(R.color.yellow))
                   }else if(queryState_tv.text == "ready"){
                       queryState_tv.setTextColor(resources.getColor(R.color.green))
                   }*/