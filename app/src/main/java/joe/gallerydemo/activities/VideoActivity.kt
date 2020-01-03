package joe.gallerydemo.activities

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import joe.gallerydemo.R
import joe.gallerydemo.adapters.VideoAdapter
import joe.gallerydemo.interfaces.OnVideoItemClickListener
import joe.gallerydemo.model.VideoInfo
import joe.gallerydemo.util.RxAsync
import joe.gallerydemo.util.VideoStoreUtil
import joe.gallerydemo.viewmodels.VideoListViewModel
import joe.gallerydemo.viewmodels.VideoVMFactory
import kotlinx.android.synthetic.main.activity_video.*

class VideoActivity : AppCompatActivity() ,OnVideoItemClickListener {


    private  val videoAdapter = VideoAdapter()

    private  val viewModel: VideoListViewModel by viewModels{ VideoVMFactory }

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

     private fun loadVideoList(){

        viewModel.mVideoInfoList.observe(this, Observer {
            videoAdapter.videoInfos = it
            videoAdapter.notifyDataSetChanged()
        })

    }

    override fun onVideoItemClick(videoInfo: VideoInfo,uri: Uri) {
        var i = Intent(this,ExoPlayerActivity::class.java)
        i.putExtra("uri",uri)
        startActivity(i)
    }

}

