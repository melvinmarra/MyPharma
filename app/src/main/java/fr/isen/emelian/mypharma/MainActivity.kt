package fr.isen.emelian.mypharma

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.isen.emelian.mypharma.Client.ProfileManager.LoginClientActivity
import fr.isen.emelian.mypharma.Pro.ProfileManager.LoginProActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        myPharma_client.setOnClickListener {
            val intent = Intent(this, LoginClientActivity::class.java)
            startActivity(intent)
        }

        myPharma_pro.setOnClickListener {
            val intent = Intent(this, LoginProActivity::class.java)
            startActivity(intent)
        }
    }
}
