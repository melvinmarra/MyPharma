package fr.isen.emelian.mypharma.Client.PrescriptionManager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.isen.emelian.mypharma.Chat.LatestChatActivity
import fr.isen.emelian.mypharma.Client.HomeActivity
import fr.isen.emelian.mypharma.Client.Maps.MapsActivity
import fr.isen.emelian.mypharma.Client.ProfileManager.ProfileActivity
import fr.isen.emelian.mypharma.NFC.NFCActivity
//import fr.isen.emelian.mypharma.Client.Fragment.SearchFragment
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_send_prescription.*

class SendPrescriptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_prescription)

        supportActionBar?.title = "Prescription Manager"

        profile_menu_send.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        location_menu_send.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        home_menu_send.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        send_prescription.setOnClickListener {
            val intent = Intent(this, SendOption::class.java)
            startActivity(intent)
        }

        chat_menu_send.setOnClickListener{
            val intent = Intent(this, LatestChatActivity::class.java)
            startActivity(intent)
        }

        pickout_iv.setOnClickListener {
            val intent = Intent(this, NFCActivity::class.java)
            startActivity(intent)
        }

    }
}
