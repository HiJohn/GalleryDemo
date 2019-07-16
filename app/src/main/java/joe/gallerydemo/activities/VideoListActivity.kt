package joe.gallerydemo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import joe.gallerydemo.R
import joe.gallerydemo.adapters.VideoPlayAdapter2
import joe.gallerydemo.animator.OnViewPagerListener
import joe.gallerydemo.animator.ViewPagerLayoutManager
import joe.gallerydemo.model.VideoInfo
import joe.gallerydemo.util.RxAsync
import joe.gallerydemo.util.VideoStoreUtil
import kotlinx.android.synthetic.main.activity_video_list.*

class VideoListActivity : AppCompatActivity() {


    private lateinit var adapter: VideoPlayAdapter2

    private lateinit var layoutManager: ViewPagerLayoutManager

    private val pagerListener = object:OnViewPagerListener{
        override fun onPageRelease(itemView: View, isNext: Boolean, position: Int) {

            val videoHolder = videoListRv.findViewHolderForLayoutPosition(position) as
                    VideoPlayAdapter2.VideoHolder
            videoHolder.detachPlayer()

        }

        override fun onPageSelected(itemView: View, position: Int, isBottom: Boolean) {
            val videoHolder = videoListRv.findViewHolderForLayoutPosition(position) as VideoPlayAdapter2.VideoHolder
            videoHolder.preparePlay()

        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list)
        adapter = VideoPlayAdapter2()
        layoutManager = ViewPagerLayoutManager(this,LinearLayoutManager.VERTICAL)
        layoutManager.setOnViewPagerListener(pagerListener)
        videoListRv.layoutManager = layoutManager
        videoListRv.adapter = adapter
        initData()
    }



    private fun initData(){
        RxAsync.async(object: RxAsync.RxCallBack<ArrayList<VideoInfo>>{
            override fun call(): ArrayList<VideoInfo> {
                return  VideoStoreUtil.getVideoInfoList(this@VideoListActivity)
            }

            override fun onResult(t: ArrayList<VideoInfo>) {
                adapter.videos = t
                adapter.notifyDataSetChanged()
            }

            override fun onError(e: Throwable) {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
