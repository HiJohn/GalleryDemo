package joe.gallerydemo

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Build
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.ExoPlayerLibraryInfo
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.FileDataSourceFactory
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.*
import leakcanary.LeakSentry
import java.io.File
import kotlin.properties.Delegates


const val DOWNLOAD_CONTENT_DIRECTORY = "downloads"
class GalleryApp :Application() {


    companion object{
        var instance:GalleryApp by Delegates.notNull()

        // For Singleton instantiation
//        @Volatile private var instance: GalleryApp? = null
//
//        fun getInstance() =
//                instance ?: synchronized(this) {
//                    instance ?: GalleryApp().also { instance = it }
//                }
    }

    private lateinit var databaseProvider:DatabaseProvider
    private lateinit var downloadContentDirectory:File
    lateinit var cacheDataSourceFactory: CacheDataSourceFactory
    private lateinit var upstreamFactory: DefaultDataSourceFactory
    private lateinit var httpDataSourceFactory: DefaultHttpDataSourceFactory
    private lateinit var downloadCache: Cache

    private lateinit var userAgent:String
    lateinit var player: SimpleExoPlayer

    private fun getUserAgent(context: Context, applicationName: String): String {
        val versionName: String
        = try {
            val packageName = context.packageName
            val info = context.packageManager.getPackageInfo(packageName, 0)
            info.versionName
        } catch (e: NameNotFoundException) {
            "?"
        }

        return (applicationName + "/" + versionName + " (Linux;Android " + Build.VERSION.RELEASE
                + ") " + ExoPlayerLibraryInfo.VERSION_SLASHY)
    }

    private fun buildHttpDataSourceFactory(): HttpDataSource.Factory {
        return DefaultHttpDataSourceFactory(userAgent)
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        initLeakCanary()
        init()
        initPrDownloader()
    }

    private fun initLeakCanary(){
        if (BuildConfig.DEBUG) {
            LeakSentry.config = LeakSentry.config.copy(watchFragmentViews = true, watchActivities =
            true, watchFragments = true)
        }
    }

    private fun initPrDownloader(){
        // Enabling database for resume support even after the application is killed:
//        val config = PRDownloaderConfig.newBuilder()
//                .setDatabaseEnabled(true)
//                .build()
//        PRDownloader.initialize(this, config)

        // Setting timeout globally for the download network requests:
//        val config1 = PRDownloaderConfig.newBuilder()
//                .setReadTimeout(30000)
//                .setConnectTimeout(30000)
//                .build()
//        PRDownloader.initialize(this, config1)
        PRDownloader.initialize(this)
    }

    private fun init(){
        databaseProvider =  ExoDatabaseProvider(this)
        downloadContentDirectory = File(filesDir, DOWNLOAD_CONTENT_DIRECTORY)
        downloadCache = SimpleCache(downloadContentDirectory, NoOpCacheEvictor(), databaseProvider)
        userAgent = getUserAgent(this,"exoPlayerDemo")
        httpDataSourceFactory = DefaultHttpDataSourceFactory(userAgent)
        upstreamFactory = DefaultDataSourceFactory(this, buildHttpDataSourceFactory())
        cacheDataSourceFactory = CacheDataSourceFactory(
                downloadCache,
                upstreamFactory,
                FileDataSourceFactory(),
                /* eventListener= */ null,
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null)
        player = newInstancePlayer(this)
    }

    private fun newInstancePlayer(context: Context): SimpleExoPlayer {
        val player = ExoPlayerFactory.newSimpleInstance(context)
        player.repeatMode = Player.REPEAT_MODE_ALL
        player.setAudioAttributes(AudioAttributes.DEFAULT,true)
        return player
    }
}
