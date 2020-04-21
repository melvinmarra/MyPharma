package fr.isen.emelian.mypharma.Chat

import androidx.appcompat.app.AppCompatActivity
import fr.isen.emelian.mypharma.Item.ChatItem
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import fr.isen.emelian.mypharma.DataClass.Pharmacy
import fr.isen.emelian.mypharma.Item.ChatItemTwo
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_chat_adapter.*

class ChatAdapter : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()
    var topharma: Pharmacy? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_adapter)

        chatRecyclerView.adapter = adapter

        topharma = intent.getParcelableExtra<Pharmacy>(NewChatActivity.USER_KEY)
        supportActionBar?.title = topharma?.name

        listenForMessage()
        send_message.setOnClickListener {
            performSendMessage()
        }
    }

    private fun listenForMessage(){
        val fromId = FirebaseAuth.getInstance().uid
        val toId = topharma?.id
        val ref = FirebaseDatabase.getInstance().getReference("/User-messages/$fromId/$toId")

        ref.addChildEventListener(object: ChildEventListener{
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
                        val currentUser = LatestChatActivity.currentUser
                            ?: return
                        adapter.add(ChatItemTwo(chatMessage.text, currentUser))
                    } else {
                        adapter.add(ChatItem(chatMessage.text, topharma!!))
                    }
                }
                chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })
    }

    private fun performSendMessage(){
        val text = et_message.text.toString()
        val fromId= FirebaseAuth.getInstance().uid
        val pharma = intent.getParcelableExtra<Pharmacy>(NewChatActivity.USER_KEY)
        val toId = pharma.id

        if(fromId == null)
            return

        //val reference = FirebaseDatabase.getInstance().getReference("/Messages").push()
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
                et_message.text.clear()
                chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
            }
        toReference.setValue(chatMessage)

        val latestMessageRef = FirebaseDatabase.getInstance().getReference("Latest-messages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)
        val latestMessageRef_two = FirebaseDatabase.getInstance().getReference("Latest-messages/$toId/$fromId")
        latestMessageRef_two.setValue(chatMessage)
    }
}
