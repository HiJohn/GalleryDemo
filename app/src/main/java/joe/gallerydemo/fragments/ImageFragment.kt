package joe.gallerydemo.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import joe.gallerydemo.R
import joe.gallerydemo.util.loadImage
import kotlinx.android.synthetic.main.fragment_image.*

class ImageFragment : Fragment() {

    private lateinit var mPath: String
    private var mPosition: Int = 0
    private var mUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        mPath = arguments?.getString(ARG_PATH,"").toString()
        if (arguments != null) {
            mPath = arguments!!.getString(ARG_PATH,"")
            mUri = arguments!!.getParcelable(ARG_URI)
            mPosition = arguments!!.getInt(ARG_POSITION)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        image_see.loadImage(mUri,this@ImageFragment)

    }


    companion object {

        private val ARG_PATH = "path"
        private val ARG_URI = "uri"
        private val ARG_POSITION = "position"


        fun newInstance(position:Int,path: String?, uri: Uri): ImageFragment {
            val fragment = ImageFragment()
            val args = Bundle()
            args.putInt(ARG_POSITION,position)
            args.putString(ARG_PATH, path)
            args.putParcelable(ARG_URI, uri)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor

