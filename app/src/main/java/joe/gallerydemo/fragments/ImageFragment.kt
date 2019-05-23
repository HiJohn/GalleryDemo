package joe.gallerydemo.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import joe.gallerydemo.R
import joe.gallerydemo.glide.GlideApp
import joe.gallerydemo.util.loadImage
import kotlinx.android.synthetic.main.fragment_image.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ImageFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageFragment : Fragment() {

    private lateinit var mPath: String
    private var mPosition: Int = 0
    private var mUri: Uri? = null

    private var mListener: OnFragmentInteractionListener? = null



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


//        GlideApp.with(this@ImageFragment)
//                .load(mUri)
//                .into(image_see)
    }


    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {

        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        private val ARG_PATH = "path"
        private val ARG_URI = "uri"
        private val ARG_POSITION = "position"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param path
         * @param uri uri of this picture
         * @param position
         * @return A new instance of fragment ImageFragment.
         */

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

