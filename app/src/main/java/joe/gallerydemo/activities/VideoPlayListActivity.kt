package joe.gallerydemo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import joe.gallerydemo.R
import joe.gallerydemo.adapters.VideoPlayAdapter
import joe.gallerydemo.model.VideoInfo
import joe.gallerydemo.util.RxAsync
import joe.gallerydemo.util.VideoStoreUtil
import kotlinx.android.synthetic.main.activity_video_play_list.*

class VideoPlayListActivity : AppCompatActivity() {

    lateinit var adapter: VideoPlayAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play_list)
        adapter = VideoPlayAdapter(this)

        video_list_vp2.adapter = adapter

        initData()
    }


    private fun initData(){
        RxAsync.async(object: RxAsync.RxCallBack<ArrayList<VideoInfo>>{
            override fun call(): ArrayList<VideoInfo> {
                return  VideoStoreUtil.getVideoInfoList(this@VideoPlayListActivity)
            }

            override fun onResult(t: ArrayList<VideoInfo>) {
                adapter.videoList = t
                adapter.notifyDataSetChanged()
            }

            override fun onError(e: Throwable) {}
        })
    }

}
