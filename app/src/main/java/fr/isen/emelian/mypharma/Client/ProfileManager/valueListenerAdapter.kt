package fr.isen.emelian.mypharma.Client.ProfileManager

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class valueListenerAdapter(val handler: (DataSnapshot) -> Unit): ValueEventListener{

    private val TAG = "valueListenerAdapter"
    override fun onCancelled(error: DatabaseError) {
        Log.e("OnCancelled", TAG, error.toException())
    }

    override fun onDataChange(data: DataSnapshot) {
        handler(data)
    }


}

