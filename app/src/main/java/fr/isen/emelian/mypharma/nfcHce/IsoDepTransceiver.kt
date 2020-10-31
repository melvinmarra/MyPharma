package fr.isen.emelian.mypharma.nfcHce

import android.nfc.tech.IsoDep
import java.io.IOException


class IsoDepTransceiver(
    private val isoDep: IsoDep,
    private val onMessageReceived: OnMessageReceived
) :
    Runnable {
    interface OnMessageReceived {
        fun onMessage(message: ByteArray?)
        fun onError(exception: Exception?)
    }

    private fun createSelectAidApdu(aid: ByteArray): ByteArray {
        val result = ByteArray(6 + aid.size)
        System.arraycopy(
            CLA_INS_P1_P2,
            0,
            result,
            0,
            CLA_INS_P1_P2.size
        )
        result[4] = aid.size.toByte()
        System.arraycopy(aid, 0, result, 5, aid.size)
        result[result.size - 1] = 0
        return result
    }

    override fun run() {
        var messageCounter = 0
        try {
            isoDep.connect()
            var response =
                isoDep.transceive(createSelectAidApdu(AID_ANDROID))
            while (isoDep.isConnected && !Thread.interrupted()) {
                val message = "Message from IsoDep " + messageCounter++
                response = isoDep.transceive(message.toByteArray())
                onMessageReceived.onMessage(response)
            }
            isoDep.close()
        } catch (e: IOException) {
            onMessageReceived.onError(e)
        }
    }

    companion object {
        private val CLA_INS_P1_P2 =
            byteArrayOf(0x00, 0xA4.toByte(), 0x04, 0x00)
        private val AID_ANDROID =
            byteArrayOf(0xF0.toByte(), 0x01, 0x02, 0x03, 0x04, 0x05, 0x06)
    }

}
