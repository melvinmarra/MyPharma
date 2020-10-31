package fr.isen.emelian.mypharma.nfcHce

import android.annotation.SuppressLint
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log


@SuppressLint("Registered")
class MyHostApduService : HostApduService() {
    private var messageCounter = 0
    override fun processCommandApdu(
        apdu: ByteArray,
        extras: Bundle
    ): ByteArray {
        return if (selectAidApdu(apdu)) {
            Log.i("HCEDEMO", "Application selected")
            welcomeMessage
        } else {
            Log.i("HCEDEMO", "Received: " + String(apdu))
            nextMessage
        }
    }

    private val welcomeMessage: ByteArray
        get() = "Hello Desktop!".toByteArray()

    private val nextMessage: ByteArray
        get() = ("Message from android: " + messageCounter++).toByteArray()

    private fun selectAidApdu(apdu: ByteArray): Boolean {
        return apdu.size >= 2 && apdu[0] == 0.toByte() && apdu[1] == 0xa4.toByte()
    }

    override fun onDeactivated(reason: Int) {
        Log.i("HCEDEMO", "Deactivated: $reason")
    }
}