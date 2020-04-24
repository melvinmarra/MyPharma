package fr.isen.emelian.mypharma.Chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import fr.isen.emelian.mypharma.DataClass.Pharmacy
import fr.isen.emelian.mypharma.DataClass.User
import fr.isen.emelian.mypharma.Pro.HomeProActivity
import fr.isen.emelian.mypharma.Pro.PrescriptionManager.ListRequest
import fr.isen.emelian.mypharma.Pro.ProfileManager.ProfilePharmacy
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_latest_chat_pro.*
import kotlinx.android.synthetic.main.latest_message_cell.view.*

class LatestChatActivityPro : AppCompatActivity() {


    val adapter = GroupAdapter<ViewHolder>()

    companion object{
        var currentPharma: Pharmacy?= null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_chat_pro)

        supportActionBar?.title = "Contact users"
        recyclerViewLatestMessagePro.adapter = adapter

        listenForLatestMessage()
        fetchCurrentUser()

        adapter.setOnItemClickListener{ item, view ->
            val intent = Intent(this, ChatAdapterPro::class.java)
            val cell = item as LatestMessageCell
            intent.putExtra(NewChatActivityPro.USER_KEY, cell.chatPartnerUser)
            startActivity(intent)
        }

        newPro_chat.setOnClickListener{
            val intent = Intent(this, ListRequest::class.java)
            startActivity(intent)
        }

        home_pro_chat.setOnClickListener{
            val intent = Intent(this, HomeProActivity::class.java)
            startActivity(intent)
        }

        pharma_info_chat.setOnClickListener{
            val intent = Intent(this, ProfilePharmacy::class.java)
            startActivity(intent)
        }

    }

    class LatestMessageCell(val chatMessage: ChatMessage): Item<ViewHolder>(){
        override fun getLayout(): Int {
            return R.layout.latest_message_cell
        }

        var chatPartnerUser: User? = null

        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.latestMessageView.text = chatMessage.text

            val chatPartnerId: String
            if(chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                chatPartnerId = chatMessage.toId
            }else{
                chatPartnerId = chatMessage.fromId
            }

            val ref = FirebaseDatabase.getInstance().getReference("/DataUsers/$chatPartnerId")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(p0: DataSnapshot) {
                    chatPartnerUser = p0.getValue(User::class.java)
                    val target = viewHolder.itemView.profilePictureLatest
                    viewHolder.itemView.nameLatest.text = chatPartnerUser?.firstName
                    Picasso.get()
                        .load(chatPartnerUser?.pictureUrl)
                        .into(target)
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })
        }
    }

    val latestMessagesMap = HashMap<String,ChatMessage>()
    private fun refreshRecyclerView(){
        adapter.clear()
        latestMessagesMap.values.forEach{
            adapter.add(LatestMessageCell(it))
        }
    }

    private fun listenForLatestMessage() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("Latest-messages/$fromId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage= p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerView()
            }
            override fun onChildRemoved(p0: DataSnapshot) {
            }
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage= p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerView()
            }

        })
    }

    private fun fetchCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/Pharmacies/$uid")

        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                currentPharma = p0.getValue(Pharmacy::class.java)
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.menu_new_message -> {
                val intent = Intent(this, NewChatActivityPro::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?) : Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
