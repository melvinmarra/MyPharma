package fr.isen.emelian.mypharma.Pro.PrescriptionManager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import fr.isen.emelian.mypharma.Chat.LatestChatActivityPro
import fr.isen.emelian.mypharma.Client.PrescriptionManager.ViewerInfo
import fr.isen.emelian.mypharma.DataClass.User
import fr.isen.emelian.mypharma.Pro.HomeProActivity
import fr.isen.emelian.mypharma.Pro.ProfileManager.ProfilePharmacy
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_prescription_viewer.*

class PrescriptionViewer : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference
    lateinit var mStorage: FirebaseStorage
    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prescription_viewer)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mStorage = FirebaseStorage.getInstance()

        user = intent.getParcelableExtra<User>(ListRequest.USER_KEY)
        supportActionBar?.title = "To do"

        firstname_view.text = ListRequest.currentUser?.firstName
        lastname_view.text = ListRequest.currentUser?.lastName

        Picasso.get()
            .load(ListRequest.currentUser?.pictureUrl)
            .into(picture_view)

        Picasso.get()
            .load(ListRequest.currentUser?.ordoUrl_one)
            .into(prescription_view)

        prescription_view.setOnClickListener {
            val intent = Intent(this, ViewerInfoPro::class.java)
            startActivity(intent)
        }

        clientSheet.setOnClickListener {
            val intent = Intent(this, ClientSheet::class.java)
            startActivity(intent)
        }

        home_pro_viewer.setOnClickListener{
            val intent = Intent(this, HomeProActivity::class.java)
            startActivity(intent)
        }

        chat_viewer.setOnClickListener{
            val intent = Intent(this, LatestChatActivityPro::class.java)
            startActivity(intent)
        }

        pharma_info_viewer.setOnClickListener{
            val intent = Intent(this, ProfilePharmacy::class.java)
            startActivity(intent)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.treatButton -> {
                val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                val intent = Intent(this, ListRequest::class.java)
                with(builder) {
                    setTitle("Treat this pharmacy?")
                    setMessage("You will not be able to cancel after this confirmation")
                    setPositiveButton("Yes") { _, _ ->
                        user?.id?.let {
                            mDatabase.child("DataUsers").child(it).child("stateRequest")
                                .setValue("take in charge")
                                startActivity(intent)
                        }
                    }
                    setNegativeButton("No") { dialog, _ ->
                        dialog.cancel()
                    }
                    show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?) : Boolean {
        menuInflater.inflate(R.menu.treat, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
