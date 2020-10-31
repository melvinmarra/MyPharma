package fr.isen.emelian.mypharma.nfcHce

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import android.view.View
import android.widget.ListView
import fr.isen.emelian.mypharma.R


class NFCMainActivity : Activity(), IsoDepTransceiver.OnMessageReceived, NfcAdapter.ReaderCallback {
    private var nfcAdapter: NfcAdapter? = null
    private var listView: ListView? = null
    private var isoDepAdapter: IsoDepAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_main)

        listView = findViewById<View>(R.id.listView) as ListView
        isoDepAdapter = IsoDepAdapter(layoutInflater)
        listView!!.adapter = isoDepAdapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
    }

    public override fun onResume() {
        super.onResume()
        nfcAdapter?.enableReaderMode(
            this, this, NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null)
    }

    public override fun onPause() {
        super.onPause()
        nfcAdapter!!.disableReaderMode(this)
    }

    override fun onTagDiscovered(tag: Tag) {
        val isoDep = IsoDep.get(tag)
        val transceiver = IsoDepTransceiver(isoDep, this)
        val thread = Thread(transceiver)
        thread.start()
    }

    override fun onMessage(message: ByteArray?) {
        runOnUiThread { isoDepAdapter?.addMessage(String(message!!)) }
    }

    override fun onError(exception: Exception?) {
        onMessage(exception?.message!!.toByteArray())
    }

}