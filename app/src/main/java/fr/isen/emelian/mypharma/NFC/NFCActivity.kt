package fr.isen.emelian.mypharma.NFC

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
import fr.isen.emelian.mypharma.DataClass.User
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_nfc.*

class NFCActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference
    lateinit var mStorage: FirebaseStorage
    lateinit var mUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)

        supportActionBar?.title = "My virtual NFC card"

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mStorage = FirebaseStorage.getInstance()

        fun currentUserReference() =
            mDatabase.child("DataUsers").child(mAuth.currentUser!!.uid)


        mDatabase.keepSynced(true)
        currentUserReference().addListenerForSingleValueEvent(
            valueListenerAdapter {
                mUser = it.asUser()!!
                card_firstname.text = mUser.firstName
                card_lastname.text = mUser.lastName
                card_id_tv.text = "XXXXXXXXXXXXXXXXXX"

                card_viewId.setOnClickListener {
                    if(card_id_tv.text == "XXXXXXXXXXXXXXXXXX"){
                        card_id_tv.text = mUser.id
                    }else{
                        card_id_tv.text = "XXXXXXXXXXXXXXXXXX"
                    }
                }

                Picasso.get()
                    .load(mUser.pictureUrl)
                    .into(card_picture)
            })

        home_menu_nfc.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        location_menu_nfc.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        profile_menu_nfc.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        chat_menu_nfc.setOnClickListener{
            val intent = Intent(this, LatestChatActivity::class.java)
            startActivity(intent)
        }

    }

    fun DataSnapshot.asUser(): User? =
        key?.let { getValue(User::class.java)?.copy(id = it) }
}