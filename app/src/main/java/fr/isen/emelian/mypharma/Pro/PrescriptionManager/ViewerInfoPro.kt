package fr.isen.emelian.mypharma.Pro.PrescriptionManager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import fr.isen.emelian.mypharma.DataClass.User
import fr.isen.emelian.mypharma.R
import kotlinx.android.synthetic.main.activity_viewer_info_pro.*

class ViewerInfoPro : AppCompatActivity() {

    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewer_info_pro)
        supportActionBar?.title = "Prescription"

        user = intent.getParcelableExtra<User>(ListRequest.USER_KEY)

        Picasso.get()
            .load(ListRequest.currentUser?.ordoUrl_one)
            .into(viewInfoPro_iv)

    }
}
