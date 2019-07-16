package joe.gallerydemo

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Build
import com.google.android.exoplayer2.ExoPlayerLibraryInfo
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.*
import joe.gallerydemo.util.ExoplayerPool
import leakcanary.LeakSentry
import java.io.File
import kotlin.properties.Delegates

const val DOWNLOAD_CONTENT_DIRECTORY = "downloads"
class GalleryApp :Application() {


    companion object{
        var instance:GalleryApp by Delegates.notNull()
    }

    private lateinit var databaseProvider:DatabaseProvider
    private lateinit var downloadContentDirectory:File
    lateinit var cacheDataSourceFactory: CacheDataSourceFactory
    private lateinit var upstreamFactory: DefaultDataSourceFactory
    private lateinit var httpDataSourceFactory: DefaultHttpDataSourceFactory
    private lateinit var downloadCache: Cache

    private lateinit var userAgent:String

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
        LeakSentry.config = LeakSentry.config.copy(watchFragmentViews = true,watchActivities =
        true,watchFragments = true)
        init()
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
    }

    fun getPlayer():SimpleExoPlayer{
        return ExoplayerPool.obtain(this)
    }

    fun poolPlayer(player: SimpleExoPlayer){
        ExoplayerPool.release(player)
    }

}
