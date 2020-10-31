package fr.isen.emelian.mypharma.nfcHce

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.util.*


class IsoDepAdapter(private val layoutInflater: LayoutInflater) : BaseAdapter() {
    private val messages: MutableList<String>? =
        ArrayList(100)
    private var messageCounter = 0
    fun addMessage(message: String) {
        messageCounter++
        messages!!.add("Message [$messageCounter]: $message")
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return messages!!.size ?: 0
    }

    override fun getItem(position: Int): Any {
        return messages!![position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(
        position: Int,
        convertView: View,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.simple_list_item_1, parent, false)
        }
        val view =
            convertView.findViewById<View>(R.id.text1) as TextView
        view.text = getItem(position) as CharSequence
        return convertView
    }

}
