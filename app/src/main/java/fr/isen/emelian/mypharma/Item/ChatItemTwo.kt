package fr.isen.emelian.mypharma.Item

import fr.isen.emelian.mypharma.DataClass.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.chat_cell.view.*
import kotlinx.android.synthetic.main.chat_cell_two.view.*

class ChatItemTwo(val text: String, val user: User): Item<ViewHolder>(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.message_tv_two.text = text

        val uri = user.pictureUrl
        val target = viewHolder.itemView.profileChat_two
        Picasso.get()
            .load(uri)
            .into(target)
    }

    override fun getLayout(): Int {
        return R.layout.chat_cell_two
    }
}