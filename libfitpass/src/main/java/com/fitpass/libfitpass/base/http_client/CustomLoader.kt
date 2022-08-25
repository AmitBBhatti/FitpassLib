package com.fitpass.libfitpass.base.http_client

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.fitpass.libfitpass.R
import kotlinx.coroutines.runBlocking

class CustomLoader {
    companion object {

        var dialog: Dialog? = null


        fun showLoaderDialog(actvity: Activity, context: Context)
        {
            if (dialog==null) {
               // dialog = Dialog(actvity, R.style.cb_dialog)
                dialog = Dialog(actvity)
            }
            if (dialog!=null&&!actvity.isFinishing&& !dialog!!.isShowing) {
                dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog?.setCancelable(false)
                dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog?.setContentView(R.layout.custom_progressbar_layout)

                dialog?.window!!.setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
                val window = dialog?.window
                val wlp = window!!.attributes
                wlp.gravity = Gravity.CENTER
                wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
                window!!.attributes = wlp
                dialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                val image = dialog?.findViewById<ImageView>(R.id.iv_loader)
                Glide.with(actvity)
                    .load(R.drawable.fitpass_loader)
                    .circleCrop()
                    .into(DrawableImageViewTarget(image))
                image?.visibility = View.VISIBLE

                dialog?.window?.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                );
                dialog?.show()
                dialog?.getWindow()?.getDecorView()
                    ?.setSystemUiVisibility(actvity.window.decorView.systemUiVisibility);
                dialog?.getWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            }
            else{
                dialog?.cancel()
                dialog?.dismiss()
                dialog=null
            }
        }
        fun hideLoaderDialog(actvity: Activity)
        {
            actvity.runOnUiThread(Runnable {
                runBlocking {
                    if (dialog!=null&& dialog!!.isShowing)
                    {
                        dialog?.hide()
                        dialog?.dismiss()
                        dialog=null
                    }
                }
            })


        }
        fun showLoaderGif(actvity: Activity, context: Context, imgView: ImageView?): ImageView {
            Glide.with(actvity)
                .load(R.drawable.fitpass_loader)
                .circleCrop()
                .into(DrawableImageViewTarget(imgView))
            imgView?.visibility= View.VISIBLE
            return imgView!!
        }
        fun hideLoaderView(imgView: ImageView?)
        {
            if (imgView!=null)
                imgView?.visibility= View.GONE
        }
    }
}