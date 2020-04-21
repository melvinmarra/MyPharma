package fr.isen.emelian.mypharma.Pro.ProfileManager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import fr.isen.emelian.mypharma.Client.ProfileManager.valueListenerAdapter
import fr.isen.emelian.mypharma.DataClass.Pharmacy
import fr.isen.emelian.mypharma.Pro.HomeProActivity
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_verification_identity_pro.*

class VerificationIdentityPro : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference
    lateinit var mStorage: FirebaseStorage
    lateinit var mPharma: Pharmacy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_identity_pro)

        fun currentUserReference() =
            mDatabase.child("Pharmacies").child(mAuth.currentUser!!.uid)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mStorage = FirebaseStorage.getInstance()

        buttonConfirmVerif.setOnClickListener {
            currentUserReference().addListenerForSingleValueEvent(
                valueListenerAdapter {
                    mPharma = it.asUser()!!
                    val refname = mPharma.name
                    val refemployee_one = mPharma.employee_one
                    val refemployee_two = mPharma.employee_two
                    val refemployee_three = mPharma.employee_three


                    if (refemployee_one.toLowerCase() != employeeConfirm.text.toString().toLowerCase() && refemployee_two.toLowerCase() != employeeConfirm.text.toString().toLowerCase() && refemployee_three.toLowerCase() != employeeConfirm.text.toString().toLowerCase()) {
                        Toast.makeText(this, "Access not allowed", Toast.LENGTH_SHORT).show()
                    } else if (refname.toLowerCase() != pharmaConfirm.text.toString().toLowerCase()) {
                        Toast.makeText(this, "Access not allowed", Toast.LENGTH_SHORT).show()
                    } else {

                        val current =
                            mDatabase.child("Pharmacies").child(mPharma.id).child("currentUser")
                                .setValue(employeeConfirm.text.toString().toLowerCase())
                        val intent = Intent(
                            this,
                            HomeProActivity::class.java
                        )
                        startActivity(intent)
                    }
                })
        }



    }


    fun DataSnapshot.asUser(): Pharmacy? =
        key?.let { getValue(Pharmacy::class.java)?.copy(id = it) }

}
