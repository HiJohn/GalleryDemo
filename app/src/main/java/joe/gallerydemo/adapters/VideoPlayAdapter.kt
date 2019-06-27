package joe.gallerydemo.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import joe.gallerydemo.fragments.VideoPlayFragment
import joe.gallerydemo.model.VideoInfo

class VideoPlayAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun createFragment(position: Int): Fragment {
        val videoInfo = videoList[position]
//        var fragment = map[position]
//        if (fragment==null){
//            fragment = VideoPlayFragment.newInstance(videoInfo,position)
//            map.put(position,fragment)
//        }
//        return fragment

        return VideoPlayFragment.newInstance(videoInfo, position)
    }


    var videoList: ArrayList<VideoInfo> = ArrayList()

//    var map:SparseArray<VideoPlayFragment> = SparseArray()


    override fun getItemCount(): Int {
        return videoList.size
    }


}