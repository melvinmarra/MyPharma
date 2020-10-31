package fr.isen.emelian.mypharma.Notification

import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class FcmPush {
    var JSON = MediaType.parse("application/json; charset=utf-8")
    var url = "https://fcm.googleapis.com/fcm/send"
    var serverKey = "AAAA-3rEj-M:APA91bEy0dGlBVPU9rqShS7hDbmUg-SW0cSarPQ_7OsdA2vj5T0EYZGkrKGxxcLCdlvaD" +
            "fub3cQOnpTYmqCUqn_zIg7cpOgpRInv76qctbBF1zT1DicUqHLisSubbz07o-s4U5re8xAN"
    var gson: Gson? = null
    var okHttpClient: OkHttpClient? = null

    companion object{
        var instance = FcmPush()
    }

    init{
        gson = Gson()
        okHttpClient = OkHttpClient()
    }

    fun sendMessage(destinationUid: String, title: String, message: String){
        FirebaseFirestore.getInstance().collection("pushtokens").document(destinationUid).get().addOnCompleteListener {
            task ->
            if(task.isSuccessful){
                var token = task?.result?.get("pushToken").toString()

                var pushDTO = PushDTO()
                pushDTO.to = token
                pushDTO.notification?.title = title
                pushDTO.notification?.body = message

                var body = RequestBody.create(JSON,gson?.toJson(pushDTO))
                var request = Request.Builder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "key=$serverKey")
                    .url(url)
                    .post(body)
                    .build()

                okHttpClient?.newCall(request)?.enqueue(object: Callback{
                    override fun onFailure(call: Call?, e: IOException?) {

                    }

                    override fun onResponse(call: Call, response: Response) {
                        println(response?.body()?.string())
                    }

                })

            }
        }
    }
}