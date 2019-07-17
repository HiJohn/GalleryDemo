package joe.gallerydemo

import androidx.core.util.Pools

object PoolTest {

    private val pool: Pools.SynchronizedPool<String> = Pools.SynchronizedPool(2)


    fun obtain(): String {
        var a = pool.acquire()
        if (a == null) {
            a = "pooltest"
        }
        return a
    }

    fun release(){
        pool.release("pooltest")
    }

}