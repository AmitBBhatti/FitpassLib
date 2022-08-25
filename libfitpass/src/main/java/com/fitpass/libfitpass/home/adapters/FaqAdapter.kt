package com.fitpass.libfitpass.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.fitpass.libfitpass.base.constants.FontIconConstant
import com.fitpass.libfitpass.base.utilities.Util
import com.fitpass.libfitpass.databinding.FaqRowBinding
import com.fitpass.libfitpass.home.models.List
import com.fitpass.libfitpass.home.viewmodel.HomeViewModel

class FaqAdapter (val homeViewModel: HomeViewModel): RecyclerView.Adapter<FaqAdapter.ViewHolder>() {

    private var list= MutableLiveData<ArrayList<List>>()
    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: FaqRowBinding = FaqRowBinding.inflate(layoutInflater, parent, false)
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
        holder.binding.faqdata=list.value!!.get(position)
        holder.binding.homeData=homeViewModel
        Util.setimage(holder.binding.tvArrow,FontIconConstant.ARROW_DOWN)
        holder.binding.rlHeader.setOnClickListener {
            if(holder.binding.tvDes.visibility== View.GONE){
                holder.binding.tvDes.visibility=View.VISIBLE
                Util.setimage(holder.binding.tvArrow,FontIconConstant.ARROW_UP)
            }else{
                holder.binding.tvDes.visibility=View.GONE
                Util.setimage(holder.binding.tvArrow,FontIconConstant.ARROW_DOWN)
            }
        }

    }
    class ViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
        var binding:   FaqRowBinding

        init {
            this.binding = binding as FaqRowBinding
        }
    }
    fun updateItems(items: MutableLiveData<ArrayList<List>>?) {
        if (items!!.value!=null&&items!!.value!!.size>0) {
            list.value = (items!!.value ?: emptyList()) as ArrayList<List>?
            notifyDataSetChanged()
        }
    }
}