package fr.isen.emelian.mypharma.NFC

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import java.util.*

class HCEService: HostApduService() {

    // COMMANDS
// RESET
// APDU 00 A4 04 00 07 A0 00 00 00 18 FF FF
// APDU 00 CA 00 00 04
    private val SELECT_AID = byteArrayOf(
        0x00,
        0xA4.toByte(),
        0x04,
        0x00,
        0x07,
        0xA0.toByte(),
        0x00,
        0x00,
        0x00,
        0x18,
        0xFF.toByte(),
        0xFF.toByte()
    )
    private val SELECT_AID_LE = byteArrayOf(
        0x00,
        0xA4.toByte(),
        0x04,
        0x00,
        0x07,
        0xA0.toByte(),
        0x00,
        0x00,
        0x00,
        0x18,
        0xFF.toByte(),
        0xFF.toByte(),
        0x00
    )
    private val GET_DATA =
        byteArrayOf(0x00, 0xCA.toByte(), 0x00, 0x00, 0x04)
    private val SW_OK = byteArrayOf(0x90.toByte(), 0x00)
    private val UID_SW_OK =
        byteArrayOf(0x01, 0x02, 0x03, 0x04, 0x90.toByte(), 0x00)
    private val SW_ERROR = byteArrayOf(0x6F, 0x00)

    override fun processCommandApdu(apdu: ByteArray?, extras: Bundle?): ByteArray? {
        if (Arrays.equals(SELECT_AID, apdu)
            || Arrays.equals(SELECT_AID_LE, apdu)
        ) {
            return SW_OK
        }
        return if (Arrays.equals(GET_DATA, apdu)) {
            UID_SW_OK
        } else SW_ERROR
    }

    override fun onDeactivated(reason: Int) {}
}