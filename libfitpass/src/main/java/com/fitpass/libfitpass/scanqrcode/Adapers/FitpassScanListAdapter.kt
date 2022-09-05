package com.fitpass.libfitpass.scanqrcode.Adapers

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.fitpass.libfitpass.R
import com.fitpass.libfitpass.base.constants.FontIconConstant
import com.fitpass.libfitpass.base.utilities.Util
import com.fitpass.libfitpass.databinding.WorkoutscanRowBinding
import com.fitpass.libfitpass.scanqrcode.FitpassScanQrCodeActivity
import com.fitpass.libfitpass.scanqrcode.FitpassShowQrCodeActivity
import com.fitpass.libfitpass.scanqrcode.activitymodels.ActivityConfig
import com.fitpass.libfitpass.scanqrcode.models.Workout
import com.fitpass.libfitpass.scanqrcode.viewmodel.ScanViewModel

import com.fitpass.libfitpass.scanqrcode.listeners.FitpassScanListener


class FitpassScanListAdapter(
    val scanViewModel: ScanViewModel,
    val fitpassScanListener: FitpassScanListener
) : RecyclerView.Adapter<FitpassScanListAdapter.ViewHolder>() {
    lateinit var context: Context
    private var list = MutableLiveData<ArrayList<Workout>>()
    var tvStatusList=ArrayList<TextView>()
    var tvDefaultList=ArrayList<TextView>()
    var llScanList=ArrayList<LinearLayout>()
    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        this.context = parent.context
        val binding: WorkoutscanRowBinding =
            WorkoutscanRowBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        if (list!!.value != null) {
            return list!!.value!!.size
        } else {
            return 0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.scandata = list!!.value!!.get(position)
        tvStatusList.add(holder.binding.tvWorkoutStatus)
        llScanList.add(holder.binding.llScanHelp)
        tvDefaultList.add(holder.binding.tvDefault)
        if (list.value!!.get(position).workout_status.equals("3")) {
            holder.binding.rlIcon.background = Util.drawRectRadious("#51d071")
            holder.binding.tvWorkoutStatus.visibility = View.VISIBLE
            holder.binding.tvDefault.visibility = View.GONE
            holder.binding.tvWorkoutStatus.setText(
                "Workout attended successfully at: " + Util.convertMiliesToDate(
                    list!!.value!!.get(position).urc_updated_time.toString(),
                    true,
                    "hh:mm aa"
                )
            )
            holder.binding.llScanHelp.visibility = View.GONE
            Util.setFantIcon(holder.binding.faIcon, FontIconConstant.ACTIVE_ICON)
        } else {
            // holder.binding.rlIcon.background = Util.drawRectRadious("#e60d61")

            holder.binding.tvDefault.visibility = View.VISIBLE
            holder.binding.tvWorkoutStatus.visibility = View.GONE
            holder.binding.llScanHelp.visibility = View.VISIBLE
            var activitlist:ArrayList<ActivityConfig>
            activitlist= Util.getActivityConfigList(context)!!
            for(data in activitlist){
                if(data.activity_id.equals(list.value!!.get(position).activity_id)) {
                    holder.binding.rlIcon.background = Util.drawGradient(data.start_color, data.end_color)
                }
            }
            if (!list.value!!.get(position).activity_id.isNullOrEmpty()) {
                holder.binding.faIcon.setText(Util.getWorkoutImage(list.value!!.get(position).activity_id!!.toInt()).toInt(16)
                    .toChar().toString()
                )
            }
        }
        holder.binding.llScan.setOnClickListener {
            var pos:Int=0
            for (tv in tvStatusList){
                if(llScanList.get(pos).visibility==View.VISIBLE){
                    Log.d("llScanList","VISIBLE")
                    tv.visibility=View.GONE
                    tvDefaultList.get(pos).visibility=View.VISIBLE
                }else{
                    Log.d("llScanList","GONE")
                    /*  tv.visibility=View.VISIBLE
                      tvDefaultList.get(pos).visibility=View.GONE*/
                }
                pos=pos+1
            }
            holder.binding.tvWorkoutStatus.visibility = View.VISIBLE
            holder.binding.tvDefault.visibility = View.GONE
            holder.binding.tvWorkoutStatus.setText("Workout scanning in progress")
            FitpassScanQrCodeActivity.tvStatus= holder.binding.tvWorkoutStatus
            FitpassScanQrCodeActivity.rlIcon= holder.binding.rlIcon
            FitpassScanQrCodeActivity.llScanHelp= holder.binding.llScanHelp
            FitpassScanQrCodeActivity.faIcon= holder.binding.faIcon
            fitpassScanListener.onScanClick(
                list.value!!.get(position),
                scanViewModel.scanWorkOutResults.value!!.studio_name,
                scanViewModel!!.scanWorkOutResults.value!!.studio_logo,
                scanViewModel!!.scanWorkOutResults.value!!.address,position)
        }

    }


    class ViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
        var binding: WorkoutscanRowBinding

        init {
            this.binding = binding as WorkoutscanRowBinding
        }
    }

    fun updateItems(items: MutableLiveData<ArrayList<Workout>>?) {
        if (items!!.value != null && items!!.value!!.size > 0) {
            list.value = (items!!.value ?: emptyList()) as ArrayList<Workout>?
            notifyDataSetChanged()
        }
    }
}