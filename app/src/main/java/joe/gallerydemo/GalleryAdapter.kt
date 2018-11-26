package joe.gallerydemo

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import android.util.SparseArray
import android.view.ViewGroup
import joe.gallerydemo.fragments.ImageFragment

/**
 * Created by takashi on 2018/2/24.
 */
class GalleryAdapter(fm: androidx.fragment.app.FragmentManager?) : androidx.fragment.app.FragmentStatePagerAdapter(fm) {

    init {

    }

    private var fragments:SparseArray<ImageFragment> = SparseArray()

    private var mUris: ArrayList<Uri> = ArrayList()

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        var fragment: androidx.fragment.app.Fragment? = fragments.get(position)
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