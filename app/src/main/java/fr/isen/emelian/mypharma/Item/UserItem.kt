package fr.isen.emelian.mypharma.Item

import android.content.ClipData
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import fr.isen.emelian.mypharma.DataClass.User
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.user_list_cell.view.*

class UserItem(val user: User): Item<ViewHolder>(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.firstname_display.text = user.firstName
        viewHolder.itemView.lastname_display.text = user.lastName

        Picasso.get().load(user.pictureUrl)
            .into(viewHolder.itemView.picture_user_iv)
    }

    override fun getLayout(): Int {
        return R.layout.user_list_cell
    }
}