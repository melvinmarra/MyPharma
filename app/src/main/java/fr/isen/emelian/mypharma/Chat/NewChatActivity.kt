package fr.isen.emelian.mypharma.Chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import fr.isen.emelian.mypharma.Client.HomeActivity
import fr.isen.emelian.mypharma.Client.Maps.MapsActivity
import fr.isen.emelian.mypharma.Client.ProfileManager.ProfileActivity
import fr.isen.emelian.mypharma.Client.PrescriptionManager.SendPrescriptionActivity
import fr.isen.emelian.mypharma.Item.PharmaItem
import fr.isen.emelian.mypharma.DataClass.Pharmacy
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_chat.*

class NewChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.title = "Select Pharmacy"

        fetchUsers()

        profile_menu_chat.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        location_menu_chat.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        send_menu_chat.setOnClickListener{
            val intent = Intent(this, SendPrescriptionActivity::class.java)
            startActivity(intent)
        }

        home_menu_chat.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
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
                adapter.setOnItemClickListener{ item, view ->
                    val pharmaItem = item as PharmaItem
                    val intent = Intent(view.context, ChatAdapter::class.java)
                    intent.putExtra(USER_KEY, pharmaItem.pharma)
                    startActivity(intent)

                    finish()
                }
                RecyclerViewChatUser.adapter = adapter
            }

        })
    }

    companion object{
        val USER_KEY = "USER_KEY"
    }
}
