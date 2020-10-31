package fr.isen.emelian.mypharma.NFC

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import fr.isen.emelian.mypharma.R
import kotlin.experimental.and


class MainActivityNfc : AppCompatActivity() {
    var context: Context? = null
    var tv0: TextView? = null
    var tv: TextView? = null
    var sv: ScrollView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc);

        context = this
        val ll = LinearLayout(this)
        ll.orientation = LinearLayout.VERTICAL
        ll.setHorizontalGravity(Gravity.FILL)
        tv0 = TextView(this)
        tv0!!.text = "Test HCE"
        ll.addView(tv0)
        val layoutParams: ViewGroup.LayoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.FILL_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        sv = ScrollView(this)
        tv = TextView(this)
        tv!!.text = ""
        tv!!.layoutParams = layoutParams
        sv!!.addView(tv)
        ll.addView(sv)
        setContentView(ll)
    }

    private fun log(message: String) {
        sv!!.post {
            tv!!.append(message)
            tv!!.append("\n")
            sv!!.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    companion object {
        private fun bytesToString(bytes: ByteArray?): String {
            if (bytes == null) return "(empty)"
            val sb = StringBuffer()
            for (b in bytes) {
                sb.append(String.format("%02x ", b and 0xFF.toByte()))
            }
            return sb.toString()
        }
    }
}
