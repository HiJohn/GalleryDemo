package joe.gallerydemo.adapters

import android.net.Uri
import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import joe.gallerydemo.fragments.ImageFragment

/**
 * Created by takashi on 2018/2/24.
 */
class GalleryAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun createFragment(position: Int): Fragment {
        val uri = mUris[position]
        return ImageFragment.newInstance(position, uri.path, uri)
    }

    override fun getItemCount(): Int {
        return mUris.size
    }

    var mUris: ArrayList<Uri> = ArrayList()


}