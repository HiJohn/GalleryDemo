package joe.gallerydemo

import android.net.Uri
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.SparseArray
import android.view.ViewGroup
import joe.gallerydemo.fragments.ImageFragment

/**
 * Created by takashi on 2018/2/24.
 */
class GalleryAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {


    private var fragments:SparseArray<ImageFragment> = SparseArray()

    private var mUris: ArrayList<Uri> = ArrayList()

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = fragments.get(position)
        if (fragment == null) {
            val uri = mUris[position]
            fragment = ImageFragment.newInstance(position, uri.path, uri)
        }

        return fragment
    }

    override fun getCount(): Int {
        return mUris.size
    }


    fun setUris(uris:ArrayList<Uri>){
        mUris = uris
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val any =  super.instantiateItem(container, position)
        fragments.put(position,any as ImageFragment?)
        return  any
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        fragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

}