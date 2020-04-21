package fr.isen.emelian.mypharma.Pro.ProfileManager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import fr.isen.emelian.mypharma.Client.ProfileManager.valueListenerAdapter
import fr.isen.emelian.mypharma.DataClass.Pharmacy
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_profile_pharmacy.*

class ProfilePharmacy : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference
    lateinit var mStorage: FirebaseStorage
    lateinit var mPharma: Pharmacy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_pharmacy)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mStorage = FirebaseStorage.getInstance()

        fun currentUserReference() =
            mDatabase.child("Pharmacies").child(mAuth.currentUser!!.uid)


        mDatabase.keepSynced(true)
        currentUserReference().addListenerForSingleValueEvent(
            valueListenerAdapter {
                mPharma = it.asUser()!!
                nameTV.text = mPharma.name
                emailTV.text = mPharma.email
                phoneTV.text = mPharma.phone
                addressTV.text = mPharma.address
                countryTV.text = mPharma.country
                city_tv.text = mPharma.city
                postcode.text = mPharma.postcode
                firstuser_tv.text = mPharma.employee_one
                seconduser_tv.text = mPharma.employee_two
                thirduser_tv.text = mPharma.employee_three
            })

        seeUserAllowed.setOnClickListener {
            if (allowedUser.visibility == View.VISIBLE) {
                allowedUser.visibility = View.INVISIBLE
                firstuser_tv.visibility = View.VISIBLE
                seconduser_tv.visibility = View.VISIBLE
                thirduser_tv.visibility = View.VISIBLE
            }else{
                allowedUser.visibility = View.VISIBLE
                firstuser_tv.visibility = View.INVISIBLE
                seconduser_tv.visibility = View.INVISIBLE
                thirduser_tv.visibility = View.INVISIBLE
            }
        }

        edit_iv_phone.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val input = EditText(this)

            with(builder){
                setTitle("Enter new phone number :")
                input.inputType = InputType.TYPE_CLASS_TEXT
                setView(input)
                setPositiveButton("OK"){ _, _ ->
                    if (!Patterns.PHONE.matcher(input.text.toString()).matches() || input.text.toString().length < 10 || input.text.toString().length > 10) {
                        Toast.makeText(applicationContext, "Invalid phone number ", Toast.LENGTH_LONG).show()
                    }else {
                        mDatabase.child("Pharmacies").child(mPharma.id).child("phone")
                            .setValue(input.text.toString())
                    }
                }
                setNegativeButton("Cancel"){ dialog, _ ->
                    dialog.cancel()
                }
                show()
            }
            currentUserReference().addListenerForSingleValueEvent(
                valueListenerAdapter {
                    mPharma = it.asUser()!!
                    phoneTV.text = mPharma.phone
                })
        }

        edit_iv_address.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val input = EditText(this)

            with(builder){
                setTitle("Enter new street :")
                input.inputType = InputType.TYPE_CLASS_TEXT
                setView(input)
                setPositiveButton("OK"){ _, _ ->

                    mDatabase.child("Pharmacies").child(mPharma.id).child("address")
                            .setValue(input.text.toString())

                }
                setNegativeButton("Cancel"){ dialog, _ ->
                    dialog.cancel()
                }
                show()
            }
            currentUserReference().addListenerForSingleValueEvent(
                valueListenerAdapter {
                    mPharma = it.asUser()!!
                    addressTV.text = mPharma.address
                })
        }

        edit_iv_postcode.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val input = EditText(this)

            with(builder){
                setTitle("Enter new postcode :")
                input.inputType = InputType.TYPE_CLASS_TEXT
                setView(input)
                setPositiveButton("OK"){ _, _ ->

                    mDatabase.child("Pharmacies").child(mPharma.id).child("postcode")
                            .setValue(input.text.toString())

                }
                setNegativeButton("Cancel"){ dialog, _ ->
                    dialog.cancel()
                }
                show()
            }
            currentUserReference().addListenerForSingleValueEvent(
                valueListenerAdapter {
                    mPharma = it.asUser()!!
                    postcode.text = mPharma.postcode
                })
        }

        edit_iv_city.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val input = EditText(this)

            with(builder){
                setTitle("Enter new city :")
                input.inputType = InputType.TYPE_CLASS_TEXT
                setView(input)
                setPositiveButton("OK"){ _, _ ->

                    mDatabase.child("Pharmacies").child(mPharma.id).child("city")
                        .setValue(input.text.toString())

                }
                setNegativeButton("Cancel"){ dialog, _ ->
                    dialog.cancel()
                }
                show()
            }
            currentUserReference().addListenerForSingleValueEvent(
                valueListenerAdapter {
                    mPharma = it.asUser()!!
                    city_tv.text = mPharma.city
                })
        }

        edit_iv_country.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val input = EditText(this)

            with(builder){
                setTitle("Enter new country :")
                input.inputType = InputType.TYPE_CLASS_TEXT
                setView(input)
                setPositiveButton("OK"){ _, _ ->

                    mDatabase.child("Pharmacies").child(mPharma.id).child("country")
                        .setValue(input.text.toString())

                }
                setNegativeButton("Cancel"){ dialog, _ ->
                    dialog.cancel()
                }
                show()
            }
            currentUserReference().addListenerForSingleValueEvent(
                valueListenerAdapter {
                    mPharma = it.asUser()!!
                    countryTV.text = mPharma.country
                })
        }

        refreshProPharma.setOnClickListener {
            val intent = Intent(this, ProfilePharmacy::class.java)
            startActivity(intent)
        }

    }

    fun DataSnapshot.asUser(): Pharmacy? =
        key?.let { getValue(Pharmacy::class.java)?.copy(id = it) }

}
