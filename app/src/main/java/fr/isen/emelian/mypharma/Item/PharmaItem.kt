package fr.isen.emelian.mypharma.Item

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import fr.isen.emelian.mypharma.DataClass.Pharmacy
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.pharmacy_list_cell.view.*

class PharmaItem(val pharma: Pharmacy): Item<ViewHolder>(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.namePharma_send.text = pharma.name
        viewHolder.itemView.addressPharma_send.text = pharma.address
        viewHolder.itemView.postcodePharma_send.text = pharma.postcode
        viewHolder.itemView.cityPharma_send.text = pharma.city

        Picasso.get().load(pharma.profileImageUrl)
            .into(viewHolder.itemView.picture_pharma_iv)
    }

    override fun getLayout(): Int {
        return R.layout.pharmacy_list_cell
    }
}
