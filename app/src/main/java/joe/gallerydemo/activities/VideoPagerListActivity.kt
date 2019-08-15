package joe.gallerydemo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player.*
import com.google.android.exoplayer2.SimpleExoPlayer
import joe.gallerydemo.R
import joe.gallerydemo.adapters.VideoPlayAdapter
import joe.gallerydemo.model.VideoInfo
import joe.gallerydemo.util.RxAsync
import joe.gallerydemo.util.VideoStoreUtil
import kotlinx.android.synthetic.main.activity_video_play_list.*

class VideoPagerListActivity : AppCompatActivity() {

    lateinit var adapter: VideoPlayAdapter

    lateinit var player:SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play_list)
        adapter = VideoPlayAdapter(this)

        video_list_vp2.adapter = adapter
//        video_list_vp2.offscreenPageLimit = 5
//        video_list_vp2.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//                player.playWhenReady = false
//            }
//
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                player.playWhenReady = true
//            }
//        })
        initPlayer()

        initData()
    }


    private fun initPlayer(){
        player = ExoPlayerFactory.newSimpleInstance(this)
        player.repeatMode = REPEAT_MODE_ALL

    }

    private fun initData(){
        RxAsync.async(object: RxAsync.RxCallBack<ArrayList<VideoInfo>>{
            override fun call(): ArrayList<VideoInfo> {
                return  VideoStoreUtil.getVideoInfoList(this@VideoPagerListActivity)
            }

            override fun onResult(t: ArrayList<VideoInfo>) {
                adapter.videoList = t
                adapter.notifyDataSetChanged()
            }

            override fun onError(e: Throwable) {}
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}
