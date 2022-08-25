package com.fitpass.libfitpass.home.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.fitpass.libfitpass.databinding.MacroRowBinding
import com.fitpass.libfitpass.home.models.MacrosDetail
import com.fitpass.libfitpass.home.viewmodel.HomeViewModel

class Macro_Adapter (val homeViewModel: HomeViewModel): RecyclerView.Adapter<Macro_Adapter.ViewHolder>() {

   private var list= MutableLiveData<ArrayList<MacrosDetail>>()
    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: MacroRowBinding = MacroRowBinding.inflate(layoutInflater, parent, false)
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
        holder.binding.macrodata=list!!.value!!.get(position)

    }


    class ViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
        var binding:   MacroRowBinding

        init {
            this.binding = binding as MacroRowBinding
        }
    }
    fun updateItems(items: MutableLiveData<ArrayList<MacrosDetail>>?) {
        if (items!!.value!=null&&items!!.value!!.size>0) {
            list.value = (items!!.value ?: emptyList()) as ArrayList<MacrosDetail>?
            notifyDataSetChanged()
        }
    }
}