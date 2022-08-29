package com.fitpass.libfitpass.scanqrcode.Adapers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.fitpass.libfitpass.R
import com.fitpass.libfitpass.base.constants.FontIconConstant
import com.fitpass.libfitpass.base.utilities.Util
import com.fitpass.libfitpass.databinding.WorkoutscanRowBinding
import com.fitpass.libfitpass.scanqrcode.models.Workout
import com.fitpass.libfitpass.scanqrcode.viewmodel.ScanViewModel

import com.fitpass.libfitpass.scanqrcode.listeners.FitpassScanListener


class FitpassScanListAdapter(val homeViewModel: ScanViewModel,val fitpassScanListener: FitpassScanListener): RecyclerView.Adapter<FitpassScanListAdapter.ViewHolder>() {
    lateinit var context:Context
    private var list= MutableLiveData<ArrayList<Workout>>()
    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        this.context=parent.context
        val binding: WorkoutscanRowBinding = WorkoutscanRowBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int {
        if(list!!.value!=null){
            return list!!.value!!.size
        }else{
            return 0
        }
    }

    override  fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.scandata=list!!.value!!.get(position)
        if(list.value!!.get(position).workout_status.equals("3")){
            holder.binding.rlIcon.background= Util.drawRectRadious("#51d071")
            holder.binding.tvWorkoutSuccess.visibility= View.VISIBLE
            holder.binding.tvWorkoutSuccess.setText("Workout attended successfully at: "+Util.convertMiliesToDate(list!!.value!!.get(position).urc_updated_time.toString(),true,"hh:mm aa"))
            holder.binding.llScanHelp.visibility= View.GONE
            Util.setFantIcon(holder.binding.faIcon,FontIconConstant.ACTIVE_ICON)
        }else{
            holder.binding.rlIcon.background= Util.drawRectRadious("#e60d61")
            Util.setFantIcon(holder.binding.faIcon,FontIconConstant.GYM_WORKOUT_ICON)
            holder.binding.tvWorkoutSuccess.visibility= View.GONE
            holder.binding.llScanHelp.visibility= View.VISIBLE
        }
        holder.binding.llScan.setOnClickListener {
            holder.binding.tvWorkoutSuccess.visibility= View.VISIBLE
            holder.binding.tvWorkoutSuccess.setText("Workout scan in progress")
            fitpassScanListener.onScanClick(list.value!!.get(position))
        }
    }


    class ViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
        var binding: WorkoutscanRowBinding

        init {
            this.binding = binding as WorkoutscanRowBinding
        }
    }
    fun updateItems(items: MutableLiveData<ArrayList<Workout>>?) {
        if (items!!.value!=null&&items!!.value!!.size>0) {
            list.value = (items!!.value ?: emptyList()) as ArrayList<Workout>?
            notifyDataSetChanged()
        }
    }
}