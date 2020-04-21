package fr.isen.emelian.mypharma.Chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import fr.isen.emelian.mypharma.DataClass.User
import fr.isen.emelian.mypharma.Item.ChatItemPro
import fr.isen.emelian.mypharma.Item.ChatItemTwoPro
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_chat_adapter.*
import kotlinx.android.synthetic.main.activity_chat_adapter_pro.*
import kotlinx.android.synthetic.main.activity_latest_chat_pro.*

class ChatAdapterPro : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()
    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_adapter_pro)

        chatRecyclerView_pro.adapter = adapter
        toUser = intent.getParcelableExtra<User>(NewChatActivityPro.USER_KEY)
        supportActionBar?.title = toUser?.firstName

        listenForMessage()
        send_message_pro.setOnClickListener {
            performSendMessage()
        }
    }

    private fun listenForMessage() {

        val fromId = FirebaseAuth.getInstance().uid //d'ou ca vient
        val toId = toUser?.id
        val ref = FirebaseDatabase.getInstance().getReference("/User-messages/$fromId/$toId")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)
                if(chatMessage != null) {
                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        val currentPharma = LatestChatActivityPro.currentPharma
                            ?: return
                        adapter.add(ChatItemTwoPro(chatMessage.text, currentPharma))
                    } else {
                        adapter.add(ChatItemPro(chatMessage.text, toUser!!))
                    }
                }
                chatRecyclerView_pro.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }


    private fun performSendMessage(){

        val text = et_message_pro.text.toString()
        val fromId= FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewChatActivityPro.USER_KEY)
        val toId = user.id

        if(fromId == null)
            return

        val reference = FirebaseDatabase.getInstance().getReference("/User-messages/$fromId/$toId").push()
        val toReference = FirebaseDatabase.getInstance().getReference("/User-messages/$toId/$fromId").push()
        val chatMessage = ChatMessage(
            reference.key!!,
            text,
            fromId,
            toId,
            System.currentTimeMillis() / 1000
        )

        reference.setValue(chatMessage)
            .addOnSuccessListener {
                et_message_pro.text.clear()
                chatRecyclerView_pro.scrollToPosition(adapter.itemCount - 1)
            }
        toReference.setValue(chatMessage)

        val latestMessageRef = FirebaseDatabase.getInstance().getReference("Latest-messages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)
        val latestMessageRef_two = FirebaseDatabase.getInstance().getReference("Latest-messages/$toId/$fromId")
        latestMessageRef_two.setValue(chatMessage)
    }
}
