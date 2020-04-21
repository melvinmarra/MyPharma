package fr.isen.emelian.mypharma.Client.ProfileManager

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import fr.isen.emelian.mypharma.Client.HomeActivity
import fr.isen.emelian.mypharma.DataClass.User
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_photo_choose.*
import java.util.*


class PhotoChooseActivity: AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference
    lateinit var mStorage: FirebaseStorage
    lateinit var mUser: User

    private val permManager =
        PermissionManager(
            this
        )
    private val picturePermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_choose)

        fun currentUserReference() =
            mDatabase.child("DataUsers").child(mAuth.currentUser!!.uid)

       mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mStorage = FirebaseStorage.getInstance()

        cameraChoose.setOnClickListener{
            pickImageFromGallery()
        }

        confirmChoice.setOnClickListener {
            uploadImageToFirestore()
            Toast.makeText(applicationContext, "Wait until the saved notification", Toast.LENGTH_LONG).show()
        }

        clearChoice.setOnClickListener{

            currentUserReference().addListenerForSingleValueEvent(
                valueListenerAdapter {
                    mUser = it.asUser()!!
                    val ref =
                        mUser.pictureUrl

                    if (ref != "") {
                        val uid = FirebaseAuth.getInstance().uid ?: ""
                        mDatabase.child("DataUsers").child(uid).child("pictureUrl")
                            .setValue("")

                        val intent = Intent(
                            this,
                            PhotoChooseActivity::class.java
                        )
                        startActivity(intent)

                        Toast.makeText(this, "Picture is not save", Toast.LENGTH_SHORT).show()
                    } else {

                        val intent = Intent(
                            this,
                            PhotoChooseActivity::class.java
                        )
                        startActivity(intent)
                    }
                })

        }

        goToHome.setOnClickListener {
            currentUserReference().addListenerForSingleValueEvent(
                valueListenerAdapter {
                    mUser = it.asUser()!!
                    val ref =
                        mUser.pictureUrl

                    if (ref != "") {

                        intent = Intent(
                            this,
                            HomeActivity::class.java
                        )
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    } else {
                        Toast.makeText(applicationContext, "Do not forget to confirm your choice", Toast.LENGTH_LONG).show()
                    }
                })
        }
    }


    private fun pickImageFromGallery() {
        if (permManager.arePermissionsOk(picturePermissions)) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1000)
        }else{
            permManager.requestMultiplePermissions(this, picturePermissions, 20)
        }
    }


    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri  = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            val bitmapDrawable= BitmapDrawable(bitmap)
            cameraChoose.setBackgroundDrawable(bitmapDrawable)
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
        val ref = mDatabase.child("DataUsers").child(uid).child("pictureUrl")
            .setValue(picture_link)

    }


    fun DataSnapshot.asUser(): User? =
        key?.let { getValue(User::class.java)?.copy(id = it) }


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


}

