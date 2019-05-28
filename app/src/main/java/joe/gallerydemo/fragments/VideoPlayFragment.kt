package joe.gallerydemo.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.PlaybackPreparer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.RandomTrackSelection
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import joe.gallerydemo.GalleryApp
import joe.gallerydemo.R

import joe.gallerydemo.model.VideoInfo
import kotlinx.android.synthetic.main.item_exoplayer.*
import java.io.File

class VideoPlayFragment : Fragment() ,PlaybackPreparer{
    override fun preparePlayback() {
        player.retry()
    }

    private lateinit var videoInfo :VideoInfo
    private lateinit var mediaSource: ProgressiveMediaSource
    lateinit var player: SimpleExoPlayer
    lateinit var app: GalleryApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = this.context?.applicationContext as GalleryApp

        arguments?.let {
            videoInfo = it.getParcelable(ARG_VIDEO_INFO)?: VideoInfo()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.item_exoplayer, container, false)

        return view
    }

    fun initPlayer(){
        val renderersFactory = DefaultRenderersFactory(this.context)
        val trackSelectionFactory = RandomTrackSelection.Factory()
        val trackSelector = DefaultTrackSelector(trackSelectionFactory)
        player = ExoPlayerFactory.newSimpleInstance(
                this.context, renderersFactory, trackSelector)
        player.playWhenReady  = true

        val uri = Uri.fromFile(File(videoInfo.path))
        val dataSourceFactory = DefaultDataSourceFactory(this.context,app.userAgent)
        mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
        exoplayer_view.player = player
        exoplayer_view.requestFocus()

        player.prepare(mediaSource)
    }


    //===================================================================================

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initPlayer()
            exoplayer_view.onResume()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <=23||player==null) {
            initPlayer()
            exoplayer_view.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            exoplayer_view.onPause()
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            exoplayer_view.onPause()
            releasePlayer()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


    fun releasePlayer() {
        if (player != null) {
            player.release()
        }
    }

    //===================================================================================

    companion object {

        const val ARG_VIDEO_INFO  = "video_info"

        @JvmStatic
        fun newInstance(videoInfo: VideoInfo) =
                VideoPlayFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_VIDEO_INFO, videoInfo)
                    }
                }
    }
}
