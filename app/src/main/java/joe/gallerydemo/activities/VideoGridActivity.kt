package joe.gallerydemo.activities

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
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
import joe.gallerydemo.viewmodels.VideoListViewModel
import joe.gallerydemo.viewmodels.VideoVMFactory
import joe.gallerydemo.widgets.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.activity_video.*

class VideoGridActivity : AppCompatActivity() ,OnVideoItemClickListener {


    private val videoAdapter = VideoGridAdapter()

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
        videoRv.layoutManager = GridLayoutManager(this,3)
        videoRv.addItemDecoration(GridSpacingItemDecoration(3,SizeUtils.dp2px(6F),true,0))
        videoRv.adapter = videoAdapter
        videoAdapter.onVideoItemClickListener = this


        loadVideoList()

    }

    private fun loadVideoList(){
        viewModel.mVideoInfoList.observe(this, Observer {
            videoAdapter.videoInfos = it
            videoAdapter.notifyDataSetChanged()
        })

    }

    override fun onVideoItemClick(videoInfo: VideoInfo,uri: Uri) {
        LogUtils.i("parsifal","videoInfo:${videoInfo.height}")
        var i = Intent(this,ExoPlayerActivity::class.java)
        i.putExtra("uri",uri)
        startActivity(i)
    }

}

