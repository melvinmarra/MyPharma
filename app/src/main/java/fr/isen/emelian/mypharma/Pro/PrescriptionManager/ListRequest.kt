package fr.isen.emelian.mypharma.Pro.PrescriptionManager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import fr.isen.emelian.mypharma.Chat.ChatAdapter
import fr.isen.emelian.mypharma.Chat.LatestChatActivityPro
import fr.isen.emelian.mypharma.Client.ProfileManager.valueListenerAdapter
import fr.isen.emelian.mypharma.DataClass.Pharmacy
import fr.isen.emelian.mypharma.DataClass.User
import fr.isen.emelian.mypharma.Item.PharmaItem
import fr.isen.emelian.mypharma.Item.UserItem
import fr.isen.emelian.mypharma.Pro.HomeProActivity
import fr.isen.emelian.mypharma.Pro.ProfileManager.ProfilePharmacy
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_list_request.*

class ListRequest : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var mDatabase: DatabaseReference
    lateinit var mStorage: FirebaseStorage
    lateinit var mPharma: Pharmacy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_request)
        supportActionBar?.title = "Requests to do"


        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mStorage = FirebaseStorage.getInstance()

        fetchPrescriptionToDo()

        home_pro_request.setOnClickListener{
            val intent = Intent(this, HomeProActivity::class.java)
            startActivity(intent)
        }

        chat_request.setOnClickListener{
            val intent = Intent(this, LatestChatActivityPro::class.java)
            startActivity(intent)
        }

        pharma_info_request.setOnClickListener{
            val intent = Intent(this, ProfilePharmacy::class.java)
            startActivity(intent)
        }

    }

    private fun fetchPrescriptionToDo(){
        val ref = FirebaseDatabase.getInstance().getReference("/DataUsers")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    val user = it.getValue(User::class.java)
                    fun currentUserReference() =
                        mDatabase.child("Pharmacies").child(mAuth.currentUser!!.uid)
                    currentUserReference().addListenerForSingleValueEvent(
                        valueListenerAdapter {
                            mPharma = it.asUser()!!
                            if (user != null && user.stateRequest == "send" && user.latestPharmacy == mPharma.name) {
                                adapter.add(
                                    UserItem(user)
                                )
                            }

                        })
                }

                adapter.setOnItemClickListener{ item, view ->
                    val userItem = item as UserItem
                    val intent = Intent(view.context, PrescriptionViewer::class.java)
                    currentUser = userItem.user
                    intent.putExtra(USER_KEY, userItem.user)
                    startActivity(intent)

                    finish()
                }
                prescriptionRecyclerView.adapter = adapter

            }

        })
    }

    private fun fetchPrescriptionInCharge(){
        val ref = FirebaseDatabase.getInstance().getReference("/DataUsers")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    val user = it.getValue(User::class.java)
                    fun currentUserReference() =
                        mDatabase.child("Pharmacies").child(mAuth.currentUser!!.uid)
                    currentUserReference().addListenerForSingleValueEvent(
                        valueListenerAdapter {
                            mPharma = it.asUser()!!
                            if (user != null && user.stateRequest == "take in charge" && user.latestPharmacy == mPharma.name) {
                                adapter.add(
                                    UserItem(user)
                                )
                            }

                        })
                }

                adapter.setOnItemClickListener{ item, view ->
                    val userItem = item as UserItem
                    val intent = Intent(view.context, InChargeActivity::class.java)
                    currentUser = userItem.user
                    intent.putExtra(USER_KEY, userItem.user)
                    startActivity(intent)

                    finish()
                }
                prescriptionRecyclerView.adapter = adapter

            }

        })
    }

    companion object{
        val USER_KEY = "USER_KEY"
        var currentUser: User? = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.toDo -> {
                supportActionBar?.title = "Waiting Requests"
                fetchPrescriptionToDo()
            }

            R.id.inCharge -> {
                supportActionBar?.title = "In process"
                fetchPrescriptionInCharge()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?) : Boolean {
        menuInflater.inflate(R.menu.prescription_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun DataSnapshot.asUser(): Pharmacy? =
        key?.let { getValue(Pharmacy::class.java)?.copy(id = it) }
}
