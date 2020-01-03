package joe.gallerydemo.fragments

import android.animation.ValueAnimator
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.DialogFragment
import joe.gallerydemo.R
import joe.gallerydemo.interfaces.OnDialogDownloadListener
import kotlinx.android.synthetic.main.dialog_fragment_demo.*

class MeDialogFragment : DialogFragment() {


    private var content: String = ""
    private var force: Boolean = false
    private var anim:ValueAnimator = ValueAnimator.ofInt(0,100)

    private lateinit var mListener: OnDialogDownloadListener

    companion object {
        private const val ARG_CONTENT = "content"
        private const val ARG_FORCE = "force"

        @JvmStatic
        fun newInstance(title: String, force: Boolean) =
                MeDialogFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_CONTENT, title)
                        putBoolean(ARG_FORCE, force)
                    }
                }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme_NoActionBar_FullScreenDialog)
        content = arguments?.getString(ARG_CONTENT)?:""
        force = arguments?.getBoolean(ARG_FORCE)?:true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_fragment_demo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        upgrade_content.text =  content
        initClick()
        setForce(force)
    }

    private fun initClick(){
        upgrade_under_right.setOnClickListener {
            upgrade_progress.visibility = View.VISIBLE
            upgrade_under_right.isClickable = false
            upgrade_under_left.isClickable = false
            if (mListener!= null){
                mListener.onDownload()
            }
        }
        upgrade_under_left.setOnClickListener { dismiss() }
        upgrade_force.setOnClickListener {
            upgrade_progress.visibility = View.VISIBLE
            upgrade_force.isClickable = false
            if (mListener!= null){
                mListener.onForce()
            }
        }

    }


    fun onDownloadComplete(){
        upgrade_force.isClickable = true
        upgrade_under_right.isClickable = false
        upgrade_under_left.isClickable = false

    }

    private fun startDownload(){
        upgrade_progress.visibility = View.VISIBLE
        anim.repeatCount = 0
        anim.interpolator = LinearInterpolator()
        anim.duration = 2000L
        anim.addUpdateListener {
            val progress = it.animatedValue as Int
            upgrade_progress.progress = progress
            if (progress == 100){
                dismiss()
            }
        }
        anim.start()
    }

    fun setDialogClickListener(listener: OnDialogDownloadListener){
        mListener = listener
    }

    fun updateProgress(progress:Int){
        if (upgrade_progress!=null) {
            upgrade_progress.progress = progress
        }
    }

    private fun setForce(force: Boolean) {
        if (force) {
            upgrade_force.visibility = View.VISIBLE
            upgrade_under_left.visibility = View.GONE
            upgrade_under_right.visibility = View.GONE
        } else {
            upgrade_force.visibility = View.GONE
            upgrade_under_left.visibility = View.VISIBLE
            upgrade_under_right.visibility = View.VISIBLE
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}