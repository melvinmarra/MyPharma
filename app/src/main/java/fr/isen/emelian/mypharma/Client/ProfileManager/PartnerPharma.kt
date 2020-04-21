package fr.isen.emelian.mypharma.Client.ProfileManager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import fr.isen.emelian.mypharma.Chat.ChatAdapter
import fr.isen.emelian.mypharma.Chat.LatestChatActivity
import fr.isen.emelian.mypharma.Client.Maps.MapsActivity
import fr.isen.emelian.mypharma.Client.PrescriptionManager.SendPrescriptionActivity
import fr.isen.emelian.mypharma.DataClass.Pharmacy
import fr.isen.emelian.mypharma.Item.PharmaItem
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_partner_pharma.*

class PartnerPharma : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partner_pharma)

        supportActionBar?.title = "Our partner pharmacies"

        fetchUsers()

        profile_menu_home_partner.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        location_menu_home_partner.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        send_menu_home_partner.setOnClickListener{
            val intent = Intent(this, SendPrescriptionActivity::class.java)
            startActivity(intent)
        }

        chat_menu_home_partner.setOnClickListener{
            val intent = Intent(this, LatestChatActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchUsers(){
        val ref = FirebaseDatabase.getInstance().getReference("/Pharmacies")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach{
                    val pharma = it.getValue(Pharmacy::class.java)
                    if(pharma != null){
                        adapter.add(
                            PharmaItem(
                                pharma
                            )
                        )
                    }
                }
                RecyclerViewPartnerUser.adapter = adapter
            }

        })
    }
}
