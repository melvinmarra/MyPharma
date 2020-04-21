package fr.isen.emelian.mypharma.Item

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import fr.isen.emelian.mypharma.DataClass.Pharmacy
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.chat_cell.view.*

class ChatItem(val text: String, val pharmacy: Pharmacy): Item<ViewHolder>(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.message_tv.text = text

        val uri = pharmacy.profileImageUrl
        val target = viewHolder.itemView.profileChat
        Picasso.get()
            .load(uri)
            .into(target)
    }

    override fun getLayout(): Int {
        return R.layout.chat_cell
    }
}