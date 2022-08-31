package com.fitpass.libfitpass.home.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.fitpass.libfitpass.base.constants.ConfigConstants
import com.fitpass.libfitpass.base.utilities.Util
import com.fitpass.libfitpass.databinding.MenuRowBinding
import com.fitpass.libfitpass.home.listeners.FitpassHomeListener
import com.fitpass.libfitpass.home.models.Product
import com.fitpass.libfitpass.home.viewmodel.HomeViewModel

class MenuAdapter (val homeViewModel: HomeViewModel,val fitpassHomeListener: FitpassHomeListener): RecyclerView.Adapter<MenuAdapter.ViewHolder>() {
    val list=  MutableLiveData<ArrayList<Product>>()
    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: MenuRowBinding = MenuRowBinding.inflate(layoutInflater, parent, false)
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
        holder.binding.product=list.value!!.get(position)
        holder.binding.homeData=homeViewModel
        holder.binding.llicon.background= Util.drawCircle("#"+list!!.value!!.get(position).bgcolor)
        if(position==list!!.value!!.size-1){
            holder.binding.view.visibility=View.GONE
        }else{
            holder.binding.view.visibility=View.VISIBLE
        }
        holder.binding.llDetail.setOnClickListener {
            if(!list!!.value!!.get(position).redircet_url.isNullOrEmpty()){
                //homeViewModel.menuActions(list!!.value!!.get(position).redircet_url)
                fitpassHomeListener.onMenuClick(list!!.value!!.get(position))
            }

        }

    }
    class ViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
        var binding:   MenuRowBinding

        init {
            this.binding = binding as MenuRowBinding
        }
    }
    fun updateItems(items: MutableLiveData<ArrayList<Product>>?) {
        if (items!!.value!=null&&items!!.value!!.size>0) {
            list.value = (items!!.value ?: emptyList()) as ArrayList<Product>?
            notifyDataSetChanged()
        }
    }
}