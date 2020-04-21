package fr.isen.emelian.mypharma.NFC

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import java.util.*
import kotlin.experimental.and

class HostCardEmulatorService: HostApduService() {


    val SELECT_OK_SW = HexStringToByteArray("9000")
    val TAG = "Host Card Emulator"
    val AID = "A0000002471001"
    val UNKNOWN_CMD_SW = HexStringToByteArray("0000")
    val SELECT_APDU = BuildSelectApdu(AID)

    val STATUS_SUCCESS = "9000"
    val STATUS_FAILED = "6F00"
    val CLA_NOT_SUPPORTED = "6E00"
    val INS_NOT_SUPPORTED = "6D00"
    val SELECT_APDU_HEADER = "00A40400"


    val SELECT_INS = "A4"
    val DEFAULT_CLA = "00"
    val MIN_APDU_LENGTH = 12


    override fun onDeactivated(reason: Int) {
        Log.d(TAG, "Deactivated: " + reason)
    }

    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle): ByteArray {
        val account: String = "1234"
        val accountBytes = account.toByteArray()
        return ConcatArrays(accountBytes, SELECT_OK_SW)
    }

    fun BuildSelectApdu(aid: String): ByteArray { // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
        return HexStringToByteArray(
            SELECT_APDU_HEADER + String.format(
                "%02X",
                aid.length / 2
            ) + aid
        )
    }

    fun ByteArrayToHexString(bytes: ByteArray): String {
        val hexArray = charArrayOf(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        )
        val hexChars =
            CharArray(bytes.size * 2) // Each byte has two hex characters (nibbles)
        var v: Int
        for (j in bytes.indices) {
            v = (bytes[j] and 0xFF.toByte()).toInt() // Cast bytes[j] to int, treating as unsigned value
            hexChars[j * 2] =
                hexArray[v ushr 4] // Select hex character from upper nibble
            hexChars[j * 2 + 1] =
                hexArray[v and 0x0F] // Select hex character from lower nibble
        }
        return String(hexChars)
    }

    fun HexStringToByteArray(s: String): ByteArray {
        val len = s.length
        require(len % 2 != 1) { "Hex string must have even number of characters" }
        val data =
            ByteArray(len / 2) // Allocate 1 byte per 2 hex characters
        var i = 0
        while (i < len) {
            // Convert each character into a integer (base-16), then bit-shift into place
            data[i / 2] = ((Character.digit(s[i], 16) shl 4)
                    + Character.digit(s[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }

    fun ConcatArrays(first: ByteArray, vararg rest: ByteArray): ByteArray {
        var totalLength = first.size
        for (array in rest) {
            totalLength += array.size
        }
        val result = Arrays.copyOf(first, totalLength)
        var offset = first.size
        for (array in rest) {
            System.arraycopy(array, 0, result, offset, array.size)
            offset += array.size
        }
        return result
    }

}


