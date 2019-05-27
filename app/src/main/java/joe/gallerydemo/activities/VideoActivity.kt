package joe.gallerydemo.activities

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import joe.gallerydemo.R
import joe.gallerydemo.adapters.VideoAdapter
import joe.gallerydemo.interfaces.OnVideoItemClickListener
import joe.gallerydemo.model.VideoInfo
import joe.gallerydemo.util.RxAsync
import joe.gallerydemo.util.VideoStoreUtil
import kotlinx.android.synthetic.main.activity_video.*

class VideoActivity : AppCompatActivity() ,OnVideoItemClickListener {


    val videoAdapter = VideoAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        swipeRefresh.setColorSchemeColors(Color.BLUE,
                Color.GREEN,
                Color.YELLOW,
                Color.RED)
        swipeRefresh.setDistanceToTriggerSync(300)
        swipeRefresh.setProgressBackgroundColorSchemeColor(Color.WHITE)
        swipeRefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                loadVideoList()
            }
        })
        videoRv.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        videoRv.adapter = videoAdapter
        videoAdapter.onVideoItemClickListener = this
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            videoRv.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//
//            }
//
//
//        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            videoRv.setOnScrollChangeListener(object: View.OnScrollChangeListener {
//                override fun onScrollChange(v: View?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
//                }
//            })
//        }

        loadVideoList()

    }

    fun loadVideoList(){


        RxAsync.async(object:RxAsync.RxCallBack<ArrayList<VideoInfo>>{
            override fun call(): ArrayList<VideoInfo> {
                return  VideoStoreUtil.getVideoInfoList(this@VideoActivity)
            }

            override fun onResult(t: ArrayList<VideoInfo>) {
                videoAdapter.videoInfos = t
                videoAdapter.notifyDataSetChanged()
                swipeRefresh.isRefreshing = false
            }

            override fun onError(e: Throwable) {

            }
        })

//        GlobalScope.launch {
//            val data:ArrayList<VideoInfo>? = VideoStoreUtil.getVideoInfoList(this@VideoActivity)
//
//        }

//        launch{
//
//        }
    }

    override fun onVideoItemClick(videoInfo: VideoInfo,uri: Uri) {
        var i = Intent(this,ExoPlayerActivity::class.java)
        i.putExtra("uri",uri)
        startActivity(i)
    }

}

