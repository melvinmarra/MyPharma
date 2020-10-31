package fr.isen.emelian.mypharma.Client.PrescriptionManager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import kotlinx.android.synthetic.main.activity_choose_picture_prescription.*
import java.util.*

class ChoosePicturePrescription : AppCompatActivity() {

    var topharma: Pharmacy? = null

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference
    lateinit var mStorage: FirebaseStorage
    lateinit var mUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_picture_prescription)

        topharma = intent.getParcelableExtra<Pharmacy>(SendOption.USER_KEY)
        supportActionBar?.title = topharma?.name

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mStorage = FirebaseStorage.getInstance()

        val intent = Intent(this, SendOption::class.java)
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        val refresh = Intent(this, ChoosePicturePrescription::class.java)
        //val filename = UUID.randomUUID().toString()

        fun currentUserReference() =
            mDatabase.child("DataUsers").child(mAuth.currentUser!!.uid)
        currentUserReference().addListenerForSingleValueEvent(
            valueListenerAdapter {
                mUser = it.asUser()!!
                val test = mUser.ordoUrl_one
                val state = mUser.stateRequest

               //mUser.latestPharmacy = SendOption.currentPharmacy!!.name


                if (test != "" && state == "send") {
                    with(builder) {
                        setTitle("Be careful : \nA prescription was send")
                        setMessage("Load other prescriptions will cancel your request to the pharmacy")
                        setPositiveButton("Ok") { _, _ ->
                        }
                        show()
                    }
                }else if(test != "" && state == "take in charge") {
                    with(builder) {
                        setTitle("Your last prescription is taking in charge")
                        setMessage("Wait until the end of your prescription preparation")
                        setPositiveButton("Ok") { dialog, _ ->
                            startActivity(intent)
                        }
                        show()
                    }
                }
                if(test != ""){
                    Picasso.get()
                        .load(test)
                        .into(firstOrdo_iv)

                    nextStep_txt.visibility = View.VISIBLE
                    nextStep_arrow.visibility = View.VISIBLE


                }else{

                }

                buttonURL.setOnClickListener {
                    with(builder) {
                        setTitle("Prescription URL :")
                        setMessage(test)
                        setPositiveButton("Ok") { _, _ ->
                        }
                        show()
                    }
                }
            })

        firstOrdo_iv.setOnClickListener{
            pickImageFromGallery(1000)
        }

        validordo_one.setOnClickListener {
            val uid = FirebaseAuth.getInstance().uid ?: ""
            mDatabase.child("DataUsers").child(uid).child("stateRequest")
                .setValue("")
            if(mUser.latestPharmacy == "") {
                val latest = mDatabase.child("DataUsers").child(uid).child("latestPharmacy")
                    .setValue(topharma?.name.toString())
                val latestUrl = mDatabase.child("DataUsers").child(uid).child("urlLatest")
                    .setValue(topharma?.profileImageUrl.toString())
            }else if(mUser.latestPharmacy != "" && mUser.latestPharmacy != topharma?.name && topharma?.name != ""){
                val latest = mDatabase.child("DataUsers").child(uid).child("latestPharmacy")
                    .setValue(topharma?.name.toString())
                val latestUrl = mDatabase.child("DataUsers").child(uid).child("urlLatest")
                    .setValue(topharma?.profileImageUrl.toString())
            }

            uploadImageToFirestore()
        }

        clearordo_one.setOnClickListener{
            val uid = FirebaseAuth.getInstance().uid ?: ""
            deletefromFirestore()
            mDatabase.child("DataUsers").child(uid).child("ordoUrl_one")
                .setValue("")
            mDatabase.child("DataUsers").child(uid).child("stateRequest")
                .setValue("")
            startActivity(refresh)
            Toast.makeText(this, "You can now load a new one", Toast.LENGTH_SHORT).show()
        }


        nextStep_txt.setOnClickListener {
            val intentNext = Intent(this, OverviewActivity::class.java)
            startActivity(intentNext)
        }

        nextStep_arrow.setOnClickListener {
            val intentNext = Intent(this, OverviewActivity::class.java)
            startActivity(intentNext)
        }

        chat_menu_send_pict.setOnClickListener {
            val intent_menu = Intent(this, LatestChatActivity::class.java)
            startActivity(intent_menu)
        }

        home_menu_send_pict.setOnClickListener {
            val intent_menu = Intent(this, HomeActivity::class.java)
            startActivity(intent_menu)
        }

        profile_menu_send_pict.setOnClickListener {
            val intent_menu = Intent(this, ProfileActivity::class.java)
            startActivity(intent_menu)
        }

        location_menu_send_pict.setOnClickListener {
            val intent_menu = Intent(this, MapsActivity::class.java)
            startActivity(intent_menu)
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
            firstOrdo_iv.setBackgroundDrawable(bitmapDrawable)
        }
    }

    fun uploadImageToFirestore() {

        val pharmaMelvin = "pharmacie de melvin"
        val pharmaLilian = "lilian pharmacy"
        val pharmaEmma = "pharmacy emma"
        val pharmaEmelian = "emelian pharma"
        val pharmaPraden = "praden pharmacy"
        val pharmaQuissac = "pharmacie de quissac"

        val folderName = mUser.id
        val filename = mUser.firstName


        if (selectedPhotoUri == null) {
            return
        }

        if ((topharma?.name) == pharmaLilian || mUser.latestPharmacy == pharmaLilian){
            val ref = FirebaseStorage.getInstance().getReference("/pharma_lilian/").child("$folderName/$filename")
            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Successful image upload", Toast.LENGTH_LONG)
                        .show()
                    ref.downloadUrl.addOnSuccessListener {
                        saveUserToDatabase(it.toString())
                    }
                }
                .addOnFailureListener {
                }

        }else if ((topharma?.name) == pharmaEmma || mUser.latestPharmacy == pharmaEmma){
            val ref = FirebaseStorage.getInstance().getReference("/pharma_emma/").child("$folderName/$filename")
            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Successful image upload", Toast.LENGTH_LONG)
                        .show()
                    ref.downloadUrl.addOnSuccessListener {
                        saveUserToDatabase(it.toString())
                    }
                }
                .addOnFailureListener {
                }

        }else if ((topharma?.name) == pharmaMelvin || mUser.latestPharmacy == pharmaMelvin){
            val ref = FirebaseStorage.getInstance().getReference("/pharma_melvin/").child("$folderName/$filename")
            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Successful image upload", Toast.LENGTH_LONG)
                        .show()
                    ref.downloadUrl.addOnSuccessListener {
                        saveUserToDatabase(it.toString())
                    }
                }
                .addOnFailureListener {
                }

        }else if ((topharma?.name) == pharmaPraden || mUser.latestPharmacy == pharmaMelvin){
            val ref = FirebaseStorage.getInstance().getReference("/pharma_praden/").child("$folderName/$filename")
            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Successful image upload", Toast.LENGTH_LONG)
                        .show()
                    ref.downloadUrl.addOnSuccessListener {
                        saveUserToDatabase(it.toString())
                    }
                }
                .addOnFailureListener {
                }

        }else if ((topharma?.name) == pharmaQuissac || mUser.latestPharmacy == pharmaQuissac){
            val ref = FirebaseStorage.getInstance().getReference("/pharma_quissac/").child("$folderName/$filename")
            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Successful image upload", Toast.LENGTH_LONG)
                        .show()
                    ref.downloadUrl.addOnSuccessListener {
                        saveUserToDatabase(it.toString())
                    }
                }
                .addOnFailureListener {
                }

        }else if ((topharma?.name) == pharmaEmelian || mUser.latestPharmacy == pharmaEmelian) {
            val ref = FirebaseStorage.getInstance().getReference("/pharma_emelian/").child("$folderName/$filename")
            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Successful image upload", Toast.LENGTH_LONG)
                        .show()
                    ref.downloadUrl.addOnSuccessListener {
                        saveUserToDatabase(it.toString())
                    }
                }
                .addOnFailureListener {
                }
        }
    }


    fun deletefromFirestore() {

        val pharmaMelvin = "pharmacie de melvin"
        val pharmaLilian = "lilian pharmacy"
        val pharmaEmma = "pharmacy emma"
        val pharmaEmelian = "emelian pharma"
        val pharmaPraden = "praden pharmacy"
        val pharmaQuissac = "pharmacie de quissac"

        if ((topharma?.name) == pharmaLilian || mUser.latestPharmacy == pharmaLilian){
            val ref = FirebaseStorage.getInstance().getReference("/pharma_lilian/${mUser.id}/").child("/${mUser.firstName}")
            ref.delete()
                .addOnSuccessListener {
                }
                .addOnFailureListener {
                }

        }
        else if ((topharma?.name) == pharmaEmma || mUser.latestPharmacy == pharmaEmma){
            val ref = FirebaseStorage.getInstance().getReference("/pharma_emma/${mUser.id}/").child("/${mUser.firstName}")
            ref.delete()
                .addOnSuccessListener {
                }
                .addOnFailureListener {
                }

        }else if ((topharma?.name) == pharmaMelvin|| mUser.latestPharmacy == pharmaMelvin){
            val ref = FirebaseStorage.getInstance().getReference("/pharma_melvin/${mUser.id}/").child("/${mUser.firstName}")
            ref.delete()
                .addOnSuccessListener {
                }
                .addOnFailureListener {
                }

        }else if ((topharma?.name) == pharmaPraden|| mUser.latestPharmacy == pharmaPraden){
            val ref = FirebaseStorage.getInstance().getReference("/pharma_praden/${mUser.id}/").child("/${mUser.firstName}")
            ref.delete()
                .addOnSuccessListener {
                }
                .addOnFailureListener {
                }

        }else if ((topharma?.name) == pharmaQuissac|| mUser.latestPharmacy == pharmaQuissac){
            val ref = FirebaseStorage.getInstance().getReference("/pharma_quissac/${mUser.id}/").child("/${mUser.firstName}")
            ref.delete()
                .addOnSuccessListener {
                }
                .addOnFailureListener {
                }

        }else if ((topharma?.name) == pharmaEmelian|| mUser.latestPharmacy == pharmaEmelian) {
            val ref = FirebaseStorage.getInstance().getReference("/pharma_emelian/${mUser.id}/").child("/${mUser.firstName}")
            ref.delete()
                .addOnSuccessListener {
                }
                .addOnFailureListener {
                }
        }
    }


    private fun saveUserToDatabase(picture_link: String ) {

        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = mDatabase.child("DataUsers").child(uid).child("ordoUrl_one")
                .setValue(picture_link)

        val refresh = Intent(this, ChoosePicturePrescription::class.java)
        startActivity(refresh)
    }

    open class PermissionManager(val context: Context){


        fun isPermissionOk(perm: String): Boolean {
            val result = ContextCompat.checkSelfPermission(context, perm)
            return result == PackageManager.PERMISSION_GRANTED
        }

        fun requestMultiplePermissions(activity: Activity, perms: Array<String>, code: Int) {
            ActivityCompat.requestPermissions(activity, perms, code)
        }

        fun arePermissionsOk(perms: Array<String>): Boolean {
            for (p in perms) {
                if (isPermissionOk(p))
                    continue
                else
                    return false
            }
            return true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item?.itemId) {
            R.id.change_pharma_id -> {

        val intent = Intent(this, SendOption::class.java)
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        val uid = FirebaseAuth.getInstance().uid ?: ""

        fun currentUserReference() =
            mDatabase.child("DataUsers").child(mAuth.currentUser!!.uid)

        currentUserReference().addListenerForSingleValueEvent(
            valueListenerAdapter {
                mUser = it.asUser()!!
                val test =
                    mUser.ordoUrl_one
                val state =
                    mUser.stateRequest

                        if (test == "") {
                            startActivity(intent)
                        } else if (test != "" && state == "") {
                            with(builder) {
                                setTitle("Prescription(s) already saved")
                                setMessage("Delete it ?")
                                setPositiveButton("Yes") { _, _ ->
                                    val urlOne =
                                        mDatabase.child("DataUsers").child(uid).child("ordoUrl_one")
                                            .setValue("")
                                    startActivity(intent)
                                }
                                setNegativeButton("No") { dialog, _ ->
                                    dialog.cancel()
                                }
                                show()
                            }
                        } else if (test != "" && state == "send") {
                            with(builder) {
                                setTitle("A request was send to a pharmacy")
                                setMessage("Cancel it and select an other pharmacy?")
                                setPositiveButton("Yes") { _, _ ->
                                    val urlOne =
                                        mDatabase.child("DataUsers").child(uid).child("ordoUrl_one")
                                            .setValue("")
                                    val stateP = mDatabase.child("DataUsers").child(uid)
                                        .child("stateRequest")
                                        .setValue("")
                                    startActivity(intent)
                                }
                                setNegativeButton("No") { dialog, _ ->
                                    dialog.cancel()
                                }
                                show()
                            }
                        } else if (test != "" && state == "take in charge") {
                            with(builder) {
                                setTitle("A request was send take in charge by a  pharmacy")
                                setMessage("You can not cancel it")
                                setPositiveButton("ok") { _, _ -> }
                                show()
                            }
                        }
                })
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?) : Boolean {
        menuInflater.inflate(R.menu.change_pharma, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun DataSnapshot.asUser(): User? =
        key?.let { getValue(User::class.java)?.copy(id = it) }


}
