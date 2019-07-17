package joe.gallerydemo.util

import android.content.Context
import androidx.core.util.Pools
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player.REPEAT_MODE_ALL
import com.google.android.exoplayer2.SimpleExoPlayer

object ExoplayerPool {


    private val pool: Pools.SynchronizedPool<SimpleExoPlayer> = Pools.SynchronizedPool(2)

    fun obtain(context: Context): SimpleExoPlayer{
        var player = pool.acquire()
        if (player==null){
            player = newInstancePlayer(context)
        }
        return player
    }

    private fun newInstancePlayer(context: Context): SimpleExoPlayer {
        val player = ExoPlayerFactory.newSimpleInstance(context)
        player.repeatMode = REPEAT_MODE_ALL
        return player
    }

    fun release(player: SimpleExoPlayer) {
        //clear state if needed
        pool.release(player)
    }


    fun releasePlayer(player: SimpleExoPlayer){
        player.release()
    }

    fun clear(){
        do {
            var player = pool.acquire()
            player?.release()
        } while (player!=null)
    }
}