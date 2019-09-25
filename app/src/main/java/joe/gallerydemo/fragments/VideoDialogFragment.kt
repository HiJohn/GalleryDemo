package joe.gallerydemo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import joe.gallerydemo.R

class VideoDialogFragment : DialogFragment() {


    fun newInstance(title: String): VideoDialogFragment {
//        val dialogFragment = VideoDialogFragment()
//        val args = Bundle()
//        args.putString("title", title)
//        dialogFragment.setArguments(args)
        return VideoDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_NoActionBar_FullScreenDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_fragment_video,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}