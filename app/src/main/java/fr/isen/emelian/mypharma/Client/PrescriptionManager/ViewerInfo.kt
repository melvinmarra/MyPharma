package fr.isen.emelian.mypharma.Client.PrescriptionManager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import fr.isen.emelian.mypharma.Client.ProfileManager.valueListenerAdapter
import fr.isen.emelian.mypharma.DataClass.User
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_overview.*
import kotlinx.android.synthetic.main.activity_viewer_info.*

class ViewerInfo : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference
    lateinit var mStorage: FirebaseStorage
    lateinit var mUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewer_info)


        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mStorage = FirebaseStorage.getInstance()

        fun currentUserReference() =
            mDatabase.child("DataUsers").child(mAuth.currentUser!!.uid)
        currentUserReference().addListenerForSingleValueEvent(
            valueListenerAdapter {
                mUser = it.asUser()!!

                Picasso.get()
                    .load(mUser.ordoUrl_one)
                    .into(bigPicture)

                UrlOvertxt.text = mUser.ordoUrl_one

            })
    }

    fun DataSnapshot.asUser(): User? =
        key?.let { getValue(User::class.java)?.copy(id = it) }
}
