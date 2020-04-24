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
import fr.isen.emelian.mypharma.DataClass.User
import fr.isen.emelian.mypharma.Item.UserItem
import fr.isen.emelian.mypharma.Pro.HomeProActivity
import fr.isen.emelian.mypharma.Pro.PrescriptionManager.ListRequest
import fr.isen.emelian.mypharma.Pro.ProfileManager.ProfilePharmacy
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_new_chat_pro.*

class NewChatActivityPro : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chat_pro)

        supportActionBar?.title = "Select User"

        fetchUsers()

        newPro_chat_chat.setOnClickListener{
            val intent = Intent(this, ListRequest::class.java)
            startActivity(intent)
        }

        home_pro_chat_chat.setOnClickListener{
            val intent = Intent(this, HomeProActivity::class.java)
            startActivity(intent)
        }

        pharma_info_chat_chat.setOnClickListener{
            val intent = Intent(this, ProfilePharmacy::class.java)
            startActivity(intent)
        }
    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/DataUsers")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach{
                    val user = it.getValue(User::class.java)
                    if(user != null){
                        adapter.add(
                            UserItem(
                                user
                            )
                        )
                    }
                }

                adapter.setOnItemClickListener{ item, view ->
                    val userItem = item as UserItem
                    val intent = Intent(view.context, ChatAdapterPro::class.java)
                    intent.putExtra(USER_KEY, userItem.user)
                    startActivity(intent)
                    finish()
                }
                RecyclerViewChatUser_pro.adapter = adapter
            }
        })
    }


    companion object{
        val USER_KEY = "USER_KEY"
    }
}
