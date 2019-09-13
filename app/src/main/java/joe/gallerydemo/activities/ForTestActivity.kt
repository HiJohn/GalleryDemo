package joe.gallerydemo.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import joe.gallerydemo.R
import kotlinx.android.synthetic.main.activity_for_test.*
import kotlinx.android.synthetic.main.rectangle_item.*

class ForTestActivity : AppCompatActivity() {


    private val videoUrl = "https://youtu.be/DZx91yQqpjY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_for_test)
        openexo.setOnClickListener { openExoPlayer() }
    }


    private fun openExoPlayer(){
        var intent = Intent(this,ExoPlayerActivity::class.java)
        intent.putExtra("uri",Uri.parse(videoUrl))
        startActivity(intent)
    }
}
