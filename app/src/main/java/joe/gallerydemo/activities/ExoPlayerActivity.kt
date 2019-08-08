package joe.gallerydemo.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.drm.*
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil
import com.google.android.exoplayer2.source.BehindLiveWindowException
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.trackselection.*
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.ErrorMessageProvider
import com.google.android.exoplayer2.util.Util
import joe.gallerydemo.GalleryApp
import joe.gallerydemo.R
import kotlinx.android.synthetic.main.activity_exoplayer.*
import leakcanary.LeakSentry
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import kotlin.math.max

class ExoPlayerActivity : AppCompatActivity(), PlaybackPreparer, PlayerControlView.VisibilityListener {


    override fun preparePlayback() {
        player.retry()
    }


    override fun onVisibilityChange(visibility: Int) {


    }


    private val cookieManager:CookieManager = CookieManager()
    init {
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)
    }

    private val TAG = "ExoPlayerActivity"

    // Saved instance state keys.
    private val KEY_TRACK_SELECTOR_PARAMETERS = "track_selector_parameters"
    private val KEY_WINDOW = "window"
    private val KEY_POSITION = "position"

    private var startWindow: Int = 0
    private var startPosition: Long = 0
    lateinit var player: SimpleExoPlayer
    lateinit var uri: Uri
    lateinit var dataSourceFactory: CacheDataSourceFactory
    lateinit var mediaSource: MediaSource
    lateinit var trackSelector: DefaultTrackSelector
    lateinit var trackSelectorParameters: DefaultTrackSelector.Parameters
    lateinit var renderersFactory: RenderersFactory
    lateinit var trackSelectionFactory: TrackSelection.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exoplayer)

        initIntent()

        initPlayerView()
//        initPlayer()


        restoreInstanceState(savedInstanceState)

    }


    private fun restoreInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            trackSelectorParameters = (savedInstanceState.getParcelable
            (KEY_TRACK_SELECTOR_PARAMETERS)) ?: DefaultTrackSelector.ParametersBuilder().build()
            startWindow = savedInstanceState.getInt(KEY_WINDOW)
            startPosition = savedInstanceState.getLong(KEY_POSITION)
        } else {
            trackSelectorParameters = DefaultTrackSelector.ParametersBuilder().build()
            clearStartPosition()
        }
    }


    private fun initIntent() {
        uri = intent.getParcelableExtra("uri")
        LogUtils.i(TAG,"uri:$uri")
//        if (uri == null) {
//            ToastUtils.showShort("uri null")
//            finish()
//        }else{
//
//        }
        if (CookieHandler.getDefault() !== cookieManager) {
            CookieHandler.setDefault(cookieManager)
        }


        dataSourceFactory = GalleryApp.instance.cacheDataSourceFactory
        mediaSource = buildMediaSource(uri, null)
    }

    private fun initPlayerView() {
        player_view.setControllerVisibilityListener(this)
        player_view.setErrorMessageProvider(PlayerErrorMessageProvider())
        player_view.requestFocus()
    }


    fun initPlayer() {
        //renderersFactory,
        // trackSelectionFactory ，
        // trackSelector,
        // drmSessionManager
        trackSelectionFactory = RandomTrackSelection.Factory()
//      if (isAbr){AdaptiveTrackSelection.Factory()}else{RandomTrackSelection.Factory()}

        renderersFactory = buildRenderersFactory()
        trackSelector = DefaultTrackSelector(trackSelectionFactory)
//        trackSelectorParameters = DefaultTrackSelector.ParametersBuilder().build()


        //暂不需要 DRM
        val drmSessionManager: DefaultDrmSessionManager<FrameworkMediaCrypto>? = null


        player = ExoPlayerFactory.newSimpleInstance(
                this, renderersFactory, trackSelector, drmSessionManager)
        player.addListener(PlayerEventListener())
//        player.addAnalyticsListener(EventLogger(trackSelector))
        player.playWhenReady = true
        player.repeatMode = Player.REPEAT_MODE_ONE
        player_view.player = player
        player_view.setPlaybackPreparer(this)

        val haveStartPosition = startWindow != C.INDEX_UNSET
        if (haveStartPosition) {
            player.seekTo(startWindow, startPosition)
        }
        player.prepare(mediaSource, !haveStartPosition, false)

    }


    private fun buildRenderersFactory(): RenderersFactory {

        val isUseExtensionRenderers = false
        val preferExtensionRenderer = false

        @DefaultRenderersFactory.ExtensionRendererMode
        val extensionRendererMode = if (isUseExtensionRenderers)
            if (preferExtensionRenderer)
                DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
            else
                DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON
        else
            DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF
        return DefaultRenderersFactory(/* context= */this)
                .setExtensionRendererMode(extensionRendererMode)
    }

    private fun buildMediaSource(uri: Uri, overrideExtension: String?): MediaSource {
        return when (@C.ContentType
        val type = Util.inferContentType(uri, overrideExtension)) {
            C.TYPE_DASH -> DashMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
            C.TYPE_SS -> SsMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
            C.TYPE_HLS -> HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
            C.TYPE_OTHER -> {ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri)}
            else -> throw IllegalStateException("Unsupported type: $type")
        }
    }


    //======================================================================================

    public override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        releasePlayer()
        player_view.overlayFrameLayout?.removeAllViews()
        clearStartPosition()
        setIntent(intent)
    }


    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initPlayer()
            player_view.onResume()
        }
    }
    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23) {
            initPlayer()
            player_view.onResume()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            player_view.onPause()
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            player_view.onPause()
            releasePlayer()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        player_view.overlayFrameLayout?.removeAllViews()
        LeakSentry.refWatcher.watch(this)
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        updateTrackSelectorParameters()
        updateStartPosition()
        outState.putParcelable(KEY_TRACK_SELECTOR_PARAMETERS, trackSelectorParameters)
        outState.putInt(KEY_WINDOW, startWindow)
        outState.putLong(KEY_POSITION, startPosition)
    }


    //=============================================================================================


    private fun clearStartPosition() {
        startWindow = C.INDEX_UNSET
        startPosition = C.TIME_UNSET
    }

    private fun updateStartPosition() {
        if (player != null) {
            startWindow = player.currentWindowIndex
            startPosition = max(0, player.contentPosition)
        }
    }

    private fun updateTrackSelectorParameters() {
        if (trackSelector != null) {
            trackSelectorParameters = trackSelector.parameters
        }
    }

    fun releasePlayer() {
        if (player != null) {
            updateTrackSelectorParameters()
            updateStartPosition()
            player.clearVideoSurface()
            player.release()
        }
    }


    private fun isBehindLiveWindow(e: ExoPlaybackException): Boolean {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false
        }
        var cause: Throwable? = e.sourceException
        while (cause != null) {
            if (cause is BehindLiveWindowException) {
                return true
            }
            cause = cause.cause
        }
        return false
    }

    private inner class PlayerEventListener : Player.EventListener {

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playbackState == Player.STATE_ENDED) {

            }
        }

        override fun onPlayerError(e: ExoPlaybackException?) {
            if (isBehindLiveWindow(e!!)) {
                clearStartPosition()
                initPlayer()
            } else {

            }
        }

        override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {

        }
    }

    private inner class PlayerErrorMessageProvider : ErrorMessageProvider<ExoPlaybackException> {

        override fun getErrorMessage(e: ExoPlaybackException): Pair<Int, String> {
            var errorString = getString(R.string.error_generic)
            if (e.type == ExoPlaybackException.TYPE_RENDERER) {
                val cause = e.rendererException
                if (cause is MediaCodecRenderer.DecoderInitializationException) {
                    // Special case for decoder initialization failures.
                    errorString = if (cause.decoderName == null) {
                        when {
                            cause.cause is MediaCodecUtil.DecoderQueryException -> getString(R.string.error_querying_decoders)
                            cause.secureDecoderRequired -> getString(
                                    R.string.error_no_secure_decoder, cause.mimeType)
                            else -> getString(R.string.error_no_decoder, cause.mimeType)
                        }
                    } else {
                        getString(
                                R.string.error_instantiating_decoder,
                                cause.decoderName)
                    }
                }
            }
            return Pair.create(0, errorString)
        }
    }



}
