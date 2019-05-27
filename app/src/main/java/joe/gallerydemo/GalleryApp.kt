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
import java.io.File

class GalleryApp :Application() {



    private val DOWNLOAD_CONTENT_DIRECTORY = "downloads"

    private lateinit var databaseProvider:DatabaseProvider
    private lateinit var downloadContentDirectory:File

    private lateinit var downloadCache: Cache

    lateinit var userAgent:String

    fun buildDataSourceFactory(): DataSource.Factory {
        val upstreamFactory = DefaultDataSourceFactory(this, buildHttpDataSourceFactory())
        return buildReadOnlyCacheDataSource(upstreamFactory, downloadCache)
    }

    fun buildReadOnlyCacheDataSource(
            upstreamFactory: DataSource.Factory, cache: Cache): CacheDataSourceFactory {
        return CacheDataSourceFactory(
                cache,
                upstreamFactory,
                FileDataSourceFactory(),
                /* eventListener= */ null,
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null)/* cacheWriteDataSinkFactory= */
    }

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
        init()
    }


    private fun init(){
        databaseProvider =  ExoDatabaseProvider(this)
        downloadContentDirectory = File(filesDir, DOWNLOAD_CONTENT_DIRECTORY)
        downloadCache = SimpleCache(downloadContentDirectory, NoOpCacheEvictor(), databaseProvider)
        userAgent = getUserAgent(this,"exoPlayerDemo")
    }

}
