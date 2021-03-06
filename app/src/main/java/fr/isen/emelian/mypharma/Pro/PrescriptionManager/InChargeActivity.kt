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
import fr.isen.emelian.mypharma.DataClass.User
import fr.isen.emelian.mypharma.Pro.HomeProActivity
import fr.isen.emelian.mypharma.Pro.ProfileManager.ProfilePharmacy
import fr.isen.emelian.mypharma.R
import fr.isen.emelian.mypharma.ble.SelectDeviceActivity
import kotlinx.android.synthetic.main.activity_in_charge.*

class InChargeActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference
    lateinit var mStorage: FirebaseStorage
    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_charge)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mStorage = FirebaseStorage.getInstance()

        user = intent.getParcelableExtra<User>(ListRequest.USER_KEY)
        supportActionBar?.title = "In charge"


        firstname_view_incharge.text = ListRequest.currentUser?.firstName
        lastname_view_incharge.text = ListRequest.currentUser?.lastName

        Picasso.get()
            .load(ListRequest.currentUser?.pictureUrl)
            .into(picture_view_incharge)

        Picasso.get()
            .load(ListRequest.currentUser?.ordoUrl_one)
            .into(prescription_view_incharge)

        prescription_view_incharge.setOnClickListener {
            val intent = Intent(this, ViewerInfoPro::class.java)
            startActivity(intent)
        }

        clientSheet_incharge.setOnClickListener {
            val intent = Intent(this, ClientSheet::class.java)
            startActivity(intent)
        }

        home_pro_viewer_incharge.setOnClickListener{
            val intent = Intent(this, HomeProActivity::class.java)
            startActivity(intent)
        }

        chat_viewer_incharge.setOnClickListener{
            val intent = Intent(this, LatestChatActivityPro::class.java)
            startActivity(intent)
        }

        pharma_info_viewer_incharge.setOnClickListener{
            val intent = Intent(this, ProfilePharmacy::class.java)
            startActivity(intent)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.finishButton -> {
                val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                val intent = Intent(this, InChargeActivity::class.java)
                with(builder) {
                    setTitle("Preparation ended?")
                    setMessage("After this confirmation, the patient will receive a notification to pickout its request/" +
                            "\nDo you want to confirm?")
                    setPositiveButton("Yes") { _, _ ->
                        user?.id?.let {
                            mDatabase.child("DataUsers").child(it).child("stateRequest")
                                .setValue("ready")
                                    startActivity(intent)
                        }
                    }
                    setNegativeButton("No") { dialog, _ ->
                        dialog.cancel()
                    }
                    show()
                }
            }

            R.id.openWithBle -> {
                val intent = Intent(this, SelectDeviceActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?) : Boolean {
        menuInflater.inflate(R.menu.finished, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
