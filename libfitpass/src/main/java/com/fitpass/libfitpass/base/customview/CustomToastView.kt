package com.fitpass.libfitpass.base.customview


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import com.fitpass.libfitpass.R
import com.fitpass.libfitpass.base.constants.FontIconConstant
import com.fitpass.libfitpass.base.utilities.Util
import com.fitpass.libfitpass.fontcomponent.FontAwesome

class CustomToastView {
    companion object
    {
        var dialog: Dialog? = null
        fun successToasMessage(activity: Activity, context: Context?, message: String?): Dialog? {

            if (dialog == null || dialog != null && !dialog!!.isShowing() && !activity.isFinishing) {
                dialog = Dialog(activity/*, R.style.cb_dialog*/)
                dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog!!.setCancelable(true)
                dialog!!.setContentView(R.layout.success_footer_layout)
                dialog!!.getWindow()!!.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
                val window: Window? = dialog!!.getWindow()
                val wlp = window!!.attributes
                wlp.gravity = Gravity.BOTTOM
                wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
                window.attributes = wlp
                dialog!!.getWindow()!!.setBackgroundDrawableResource(R.color.transparent)
                val image: FontAwesome =
                    dialog!!.findViewById<View>(R.id.fa_info) as FontAwesome
                val fa_error_cross: FontAwesome =
                    dialog!!.findViewById<View>(R.id.fa_error_cross) as FontAwesome
                Util.setimage(fa_error_cross, FontIconConstant.CLOSE_ICON)
                Util.setimage(image, FontIconConstant.CIRCULER_OK_ICON)
                val text =
                    dialog!!.findViewById<View>(R.id.tv_server_error_message) as TextView
                text.text = message
                fa_error_cross.setOnClickListener(View.OnClickListener { if (!activity.isFinishing && dialog != null) dialog!!.dismiss() })
                dialog!!.show()
                val handler = Handler()
                handler.postDelayed(
                    { if (!activity.isFinishing && dialog != null) dialog!!.dismiss() },
                    10000
                )
            }
            return dialog
        }


        fun errorToasMessage(activity: Activity, context: Context?, message: String?): Dialog? {
            activity.runOnUiThread(Runnable {
                if (dialog == null || dialog != null && !dialog!!.isShowing() && !activity.isFinishing) {
                    dialog = Dialog(activity/*, R.style.cb_dialog*/)
                    dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog!!.setCancelable(true)
                    dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog!!.setCancelable(true)
                    dialog!!.setContentView(R.layout.error_footer_layout)
                    dialog!!.getWindow()?.setLayout(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT
                    )
                    val window: Window? = dialog!!.getWindow()
                    val wlp = window?.attributes
                    wlp!!.gravity = Gravity.BOTTOM
                    wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
                    window!!.attributes = wlp
                    dialog!!.getWindow()?.setBackgroundDrawableResource(R.color.transparent)
                    val image: FontAwesome =
                        dialog!!.findViewById<View>(R.id.fa_info) as FontAwesome
                    val ll_error_base =
                        dialog!!.findViewById<View>(R.id.ll_error_base) as LinearLayout
                    val fa_error_cross: FontAwesome =
                        dialog!!.findViewById<View>(R.id.fa_error_cross) as FontAwesome
                    Util.setimage(fa_error_cross, FontIconConstant.CLOSE_ICON)
                    Util.setimage(image, FontIconConstant.ERROR_ICON)
                    val text =
                        dialog!!.findViewById<View>(R.id.tv_server_error_message) as TextView
                    text.text = message
                    dialog!!.show()
                    fa_error_cross.setOnClickListener(View.OnClickListener { dialog!!.dismiss() })
                    val handler = Handler()
                    handler.postDelayed({ dialog!!.dismiss() }, 10000)
                }

            })
            return dialog
        }

        fun errorToastMessage(
            activity: Activity,
            context: Context?,
            message: String?,
            showTryAgaiView: Boolean,
            retryListener: RetryListener
        ): Dialog? {
            activity.runOnUiThread(Runnable {
                if (dialog == null || dialog != null && !dialog!!.isShowing() && !activity.isFinishing) {
                    dialog =
                        Dialog(activity)
                    dialog!!.requestWindowFeature(
                        Window.FEATURE_NO_TITLE
                    )
                    dialog!!.setCancelable(
                        true
                    )
                    dialog!!.requestWindowFeature(
                        Window.FEATURE_NO_TITLE
                    )
                    dialog!!.setCancelable(
                        true
                    )
                    dialog!!.setContentView(R.layout.error_footer_layout)
                    dialog!!.getWindow()
                        ?.setLayout(
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.WRAP_CONTENT
                        )
                    val window: Window? =
                        dialog!!.getWindow()
                    val wlp = window!!.attributes
                    wlp.gravity = Gravity.BOTTOM
                    wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
                    window.attributes = wlp
                    dialog!!.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)
                    val image: FontAwesome =
                        dialog!!.findViewById<View>(
                            R.id.fa_info
                        ) as FontAwesome
                    val ll_error_base =
                        dialog!!.findViewById<View>(
                            R.id.ll_error_base
                        ) as LinearLayout
                    val fa_error_cross: FontAwesome =
                        dialog!!.findViewById<View>(
                            R.id.fa_error_cross
                        ) as FontAwesome
                    //CommonMethods.setimage(fa_error_cross, Constants.Companion.getIcon_close());
                    Util.setimage(image, FontIconConstant.ERROR_ICON)
                    val text =
                        dialog!!.findViewById<View>(
                            R.id.tv_server_error_message
                        ) as TextView
                    val tvRetry =
                        dialog!!.findViewById<View>(
                            R.id.tv_retry
                        ) as TextView
                    text.text = message
                    if (showTryAgaiView) {
                        tvRetry.visibility = View.VISIBLE
                    }
                    dialog!!.show()
                    tvRetry.setOnClickListener {
                        dialog!!.cancel()
                        dialog!!.dismiss()
                        retryListener.onRetryClick("")
                    }

                }
            })
            return dialog
        }

    }
}