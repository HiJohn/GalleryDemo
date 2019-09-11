package joe.gallerydemo.activities

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SizeUtils
import joe.gallerydemo.R
import joe.gallerydemo.adapters.VideoAdapter
import joe.gallerydemo.adapters.VideoGridAdapter
import joe.gallerydemo.interfaces.OnVideoItemClickListener
import joe.gallerydemo.model.VideoInfo
import joe.gallerydemo.util.RxAsync
import joe.gallerydemo.util.VideoStoreUtil
import joe.gallerydemo.widgets.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.activity_video.*

class VideoGridActivity : AppCompatActivity() ,OnVideoItemClickListener {


    val videoAdapter = VideoGridAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        swipeRefresh.setColorSchemeColors(Color.BLUE,
                Color.GREEN,
                Color.YELLOW,
                Color.RED)
        swipeRefresh.setDistanceToTriggerSync(300)
        swipeRefresh.setProgressBackgroundColorSchemeColor(Color.WHITE)
        swipeRefresh.setOnRefreshListener { loadVideoList() }
        videoRv.layoutManager = GridLayoutManager(this,3)
        videoRv.addItemDecoration(GridSpacingItemDecoration(3,SizeUtils.dp2px(6F),true,0))
        videoRv.adapter = videoAdapter
        videoAdapter.onVideoItemClickListener = this


        loadVideoList()

    }

    fun loadVideoList(){


        RxAsync.async(object:RxAsync.RxCallBack<ArrayList<VideoInfo>>{
            override fun call(): ArrayList<VideoInfo> {
                return  VideoStoreUtil.getVideoInfoList(this@VideoGridActivity)
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
        LogUtils.i("parsifal","videoInfo:${videoInfo.height}")
        var i = Intent(this,ExoPlayerActivity::class.java)
        i.putExtra("uri",uri)
        startActivity(i)
    }

}

