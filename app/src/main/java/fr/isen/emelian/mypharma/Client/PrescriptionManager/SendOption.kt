package fr.isen.emelian.mypharma.Client.PrescriptionManager


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import fr.isen.emelian.mypharma.Chat.LatestChatActivity
import fr.isen.emelian.mypharma.Client.HomeActivity
import fr.isen.emelian.mypharma.Client.Maps.MapsActivity
import fr.isen.emelian.mypharma.Client.ProfileManager.ProfileActivity
import fr.isen.emelian.mypharma.Item.PharmaItem
import fr.isen.emelian.mypharma.DataClass.Pharmacy
//import fr.isen.emelian.mypharma.Client.Fragment.SearchFragment
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_send_option.*

class SendOption : AppCompatActivity() {

    companion object{
        val USER_KEY = "USER_KEY"
        var currentPharmacy: Pharmacy? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_option)

        supportActionBar?.title = "Select your pharmacy"


        home_menu_send_send.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        location_menu_send_send.setOnClickListener{
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        profile_menu_send_send.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        chat_menu_send_send.setOnClickListener{
            val intent = Intent(this, LatestChatActivity::class.java)
            startActivity(intent)
        }

        fetchUsers()
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
                    val intent = Intent(view.context, ChoosePicturePrescription::class.java)
                    intent.putExtra(USER_KEY, pharmaItem.pharma)
                    currentPharmacy = pharmaItem.pharma
                    startActivity(intent)
                    finish()

                }
                pharmaRecyclerView.adapter = adapter
            }

        })
    }

}
