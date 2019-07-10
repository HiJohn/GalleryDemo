package joe.gallerydemo

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.google.android.exoplayer2.ExoPlayerLibraryInfo
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.*
import leakcanary.LeakSentry
import java.io.File
const val DOWNLOAD_CONTENT_DIRECTORY = "downloads"
class GalleryApp :Application() {


    private lateinit var databaseProvider:DatabaseProvider
    private lateinit var downloadContentDirectory:File
    lateinit var cacheDataSourceFactory: CacheDataSourceFactory
    private lateinit var upstreamFactory: DefaultDataSourceFactory
    private lateinit var httpDataSourceFactory: DefaultHttpDataSourceFactory
    private lateinit var downloadCache: Cache

    lateinit var userAgent:String


    private fun getUserAgent(context: Context, applicationName: String): String {
        val versionName: String
        = try {
            val packageName = context.packageName
            val info = context.packageManager.getPackageInfo(packageName, 0)
            info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
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

}
