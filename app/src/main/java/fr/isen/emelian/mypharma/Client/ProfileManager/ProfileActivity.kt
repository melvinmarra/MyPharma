package fr.isen.emelian.mypharma.Client.ProfileManager

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import fr.isen.emelian.mypharma.Chat.LatestChatActivity
import fr.isen.emelian.mypharma.Client.HomeActivity
import fr.isen.emelian.mypharma.Client.Maps.MapsActivity
import fr.isen.emelian.mypharma.Client.PrescriptionManager.SendPrescriptionActivity
import fr.isen.emelian.mypharma.DataClass.User
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*


class ProfileActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference
    lateinit var mStorage: FirebaseStorage
    lateinit var mUser: User



    val PASSWORD_REGEX = """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#${'$'}%!\-_?&])(?=\S+${'$'}).{12,}""".toRegex()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar?.title = "Your profile"

        val user = FirebaseAuth.getInstance().currentUser

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mStorage = FirebaseStorage.getInstance()


        fun currentUserReference() =
            mDatabase.child("DataUsers").child(mAuth.currentUser!!.uid)


        mDatabase.keepSynced(true)
        currentUserReference().addListenerForSingleValueEvent(
            valueListenerAdapter {
                mUser = it.asUser()!!
                firstnameProf.text = mUser.firstName
                lastnameProf.text = mUser.lastName
                dopProf.text = mUser.date_of_birth
                emailProf.text = mUser.email
                phoneProf.text = mUser.phone
                secuProf.text = mUser.secu_number
                passwordProf.text = "***********"

                show_iv.setOnClickListener {
                    if (passwordProf.text == "***********") {
                        passwordProf.text = mUser.password
                    } else if (passwordProf.text == mUser.password) {
                        passwordProf.text = "***********"
                    }
                }

                Picasso.get()
                    .load(mUser.pictureUrl)
                    .into(camera_iv)
            })


        home_menu_profile.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        location_menu_profile.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        send_menu_profile.setOnClickListener{
            val intent = Intent(this, SendPrescriptionActivity::class.java)
            startActivity(intent)
        }

        chat_menu_profile.setOnClickListener{
            val intent = Intent(this, LatestChatActivity::class.java)
            startActivity(intent)
        }

        editEmail_iv.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val input = EditText(this)

            with(builder){
                setTitle("Enter new email :")
                input.inputType = InputType.TYPE_CLASS_TEXT
                setView(input)
                setPositiveButton("OK") { _, _ ->


                    if (!Patterns.EMAIL_ADDRESS.matcher(input.text.toString()).matches()) {
                        Toast.makeText(applicationContext, "Invalid Email ", Toast.LENGTH_LONG)
                            .show()
                    } else {

                        mDatabase.child("DataUsers").child(mUser.id).child("email")
                            .setValue(input.text.toString())

                        user?.updateEmail(input.text.toString())
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {

                                    val mailUser = mAuth.currentUser
                                    mailUser?.sendEmailVerification()
                                        ?.addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Toast.makeText(
                                                    baseContext,
                                                    "Email send",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                }
                            }
                        }
                }
                setNegativeButton("Cancel"){ dialog, _ ->
                    dialog.cancel()
                }

                show()
            }
            currentUserReference().addListenerForSingleValueEvent(
                valueListenerAdapter {
                    mUser = it.asUser()!!
                    emailProf.text = mUser.email
                })
        }

        editPassword_iv.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val input = EditText(this)

            with(builder){
                setTitle("Enter new password :")
                input.inputType = InputType.TYPE_CLASS_TEXT
                setView(input)
                setPositiveButton("OK"){ _, _ ->

                    if (!PASSWORD_REGEX.matches(input.text.toString())) {
                        Toast.makeText(
                            applicationContext,
                            "Password must have:\n1 Uppercase Letter\n1 Lowercase Letter\n1 Special Character\n1 number",
                            Toast.LENGTH_LONG
                        ).show()
                    }else if (input.text.toString().length < 8) {
                        Toast.makeText(applicationContext, "Password should be longer than 8 characters ", Toast.LENGTH_LONG).show()
                    }else{
                        mDatabase.child("DataUsers").child(mUser.id).child("password")
                            .setValue(input.text.toString())

                        val newPassword = input.text.toString()

                        user?.updatePassword(newPassword)
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        baseContext,
                                        "Password registered",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                }
                setNegativeButton("Cancel"){ dialog, _ ->
                    dialog.cancel()
                }
                show()
            }
            currentUserReference().addListenerForSingleValueEvent(
                valueListenerAdapter {
                    mUser = it.asUser()!!
                    passwordProf.text = mUser.password
                })

        }

        editPhone_iv.setOnClickListener {
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
                        mDatabase.child("DataUsers").child(mUser.id).child("phone")
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
                    mUser = it.asUser()!!
                    phoneProf.text = mUser.phone
                })
        }


        camera_iv.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }


    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri  = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            val bitmapDrawable= BitmapDrawable(bitmap)
            camera_iv.setBackgroundDrawable(bitmapDrawable)
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
        val ref = mDatabase.child("DataUsers").child(mUser.id).child("pictureUrl")
            .setValue(picture_link)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.actionConfirm -> {
                uploadImageToFirestore()
                Toast.makeText(applicationContext, "Modification saved", Toast.LENGTH_LONG).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }

            R.id.actionRefresh -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?) : Boolean {
        menuInflater.inflate(R.menu.profile, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun DataSnapshot.asUser(): User? =
        key?.let { getValue(User::class.java)?.copy(id = it) }
}

