package fr.isen.emelian.mypharma.Pro.ProfileManager

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import fr.isen.emelian.mypharma.Chat.LatestChatActivityPro
import fr.isen.emelian.mypharma.Client.HomeActivity
import fr.isen.emelian.mypharma.Client.ProfileManager.ProfileActivity
import fr.isen.emelian.mypharma.Client.ProfileManager.valueListenerAdapter
import fr.isen.emelian.mypharma.DataClass.Pharmacy
import fr.isen.emelian.mypharma.Pro.HomeProActivity
import fr.isen.emelian.mypharma.Pro.PrescriptionManager.ListRequest
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_profile_pharmacy.*
import java.util.*

class ProfilePharmacy : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference
    lateinit var mStorage: FirebaseStorage
    lateinit var mPharma: Pharmacy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_pharmacy)

        supportActionBar?.title = "Your pharmacy"

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

                Picasso.get()
                    .load(mPharma.profileImageUrl)
                    .into(picture_pharma)
            })

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

        home_pro.setOnClickListener {
            val intent = Intent(this, HomeProActivity::class.java)
            startActivity(intent)
        }

        newProProfile.setOnClickListener {
            val intent = Intent(this, ListRequest::class.java)
            startActivity(intent)
        }

        pharma_info_chat.setOnClickListener {
            val intent = Intent(this, LatestChatActivityPro::class.java)
            startActivity(intent)
        }

        picture_pharma.setOnClickListener{
            pickImageFromGallery(1000)
        }
    }

    private fun pickImageFromGallery(requestCode: Int) {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, requestCode)
    }

    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri  = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            val bitmapDrawable= BitmapDrawable(bitmap)
            picture_pharma.setBackgroundDrawable(bitmapDrawable)
        }
    }

    fun uploadImageToFirestore(){
        if(selectedPhotoUri == null){
            return
        }

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Successful image upload", Toast.LENGTH_LONG).show()

                ref.downloadUrl.addOnSuccessListener {
                    saveUserToDatabase(it.toString())
                }
            }
            .addOnFailureListener{

            }
    }

    private fun saveUserToDatabase(picture_link: String ) {
        val uid = FirebaseAuth.getInstance().uid ?:""
        val ref = mDatabase.child("Pharmacies").child(uid).child("profileImageUrl")
            .setValue(picture_link)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.actionConfirm -> {
                uploadImageToFirestore()
                Toast.makeText(applicationContext, "Modification saved", Toast.LENGTH_LONG).show()
                val intent = Intent(this, HomeProActivity::class.java)
                startActivity(intent)
            }

            R.id.actionRefresh -> {
                val intent = Intent(this, ProfilePharmacy::class.java)
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?) : Boolean {
        menuInflater.inflate(R.menu.profile, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun DataSnapshot.asUser(): Pharmacy? =
        key?.let { getValue(Pharmacy::class.java)?.copy(id = it) }

}
