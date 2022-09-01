package com.fitpass.libfitpass.home.adapters

import android.R.attr.left
import android.R.attr.right
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.fitpass.libfitpass.base.constants.ConfigConstants
import com.fitpass.libfitpass.base.constants.FontIconConstant
import com.fitpass.libfitpass.base.utilities.FitpassConfig
import com.fitpass.libfitpass.base.utilities.Util
import com.fitpass.libfitpass.databinding.UpcommingImageRowBinding
import com.fitpass.libfitpass.databinding.UpcommingMacrosBinding
import com.fitpass.libfitpass.databinding.UpcommingWorkoutRowBinding
import com.fitpass.libfitpass.home.listeners.FitpassHomeListener
import com.fitpass.libfitpass.home.models.SliderActivity
import com.fitpass.libfitpass.home.viewmodel.HomeViewModel


class UpcomingAdapter(val context: Context,val homeViewModel: HomeViewModel,val fitpassHomeListener: FitpassHomeListener): PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null
    val list=  MutableLiveData<ArrayList<SliderActivity>>()
    override fun getCount(): Int {
        if(list!!.value!=null){
            return list!!.value!!.size
        }else{
            return 0
        }
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun instantiateItem(container: ViewGroup, position: Int): View {
        layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layoutInflater1 = LayoutInflater.from(container.context)
        val vp = container as ViewPager
        var fitpassConfig = FitpassConfig.getInstance()
        var paddingDp: Int=0
        if(fitpassConfig!!.getPadding()>4){
             paddingDp = fitpassConfig!!.getPadding() - 5;
        }

        var density = context.getResources().getDisplayMetrics().density.toFloat()
        var paddingPixel = (paddingDp * density).toInt();
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )


        if(list.value!!.get(position).action.equals(ConfigConstants.WORKOUT_ACTION)){
            var binding = UpcommingWorkoutRowBinding.inflate(layoutInflater1, container, false)
            binding.workoutdata=list.value!!.get(position)
            binding.homeData=homeViewModel
            if(position!=0&&position==list!!.value!!.size-1){
                binding.rlDetail.setPadding(0, 0, paddingPixel, 0);
            }
            if(list.value!!.get(position).data.workout_status.equals("3")){
                binding.viewCircle.visibility=View.GONE
                if(list.value!!.get(position).data.urc_updated_time!=null)
                {
                    if(list.value!!.get(position).data.urc_updated_time>0){
                        binding.tvStatus.setText("Workout attended successfully at: "+Util.convertMiliesToDD_MM_HH_MMDateTime2(list.value!!.get(position).data.urc_updated_time.toString(),true))
                    }
                }else{
                    binding.tvStatus.visibility=View.GONE
                }
            }else if(list.value!!.get(position).data.ongoing_workout.equals("1") && !list.value!!.get(position).data.workout_status.equals("1") ){
                binding.viewCircle.visibility=View.GONE
                binding.tvStatus.setText("Ongoing workout")
            }else{
                binding.viewCircle.visibility=View.GONE
                binding.tvStatus.setText("")
            }

            binding.llScan.setOnClickListener {
                fitpassHomeListener.onScanClick()
            }

            vp.addView(binding.root, 0)
            return binding.root
        }else if(list.value!!.get(position).action.equals(ConfigConstants.NOTICE_ACTION)){
            var binding = UpcommingImageRowBinding.inflate(layoutInflater1, container, false)
            binding.upcommingdata=list.value!!.get(position)
            binding.homeData=homeViewModel
            if(position!=0&&position==list!!.value!!.size-1){
                binding.rlDetail.setPadding(0, 0, paddingPixel, 0);
            }
            binding.rlDetail.setOnClickListener {
                homeViewModel.upCommingActions(ConfigConstants.NOTICE_ACTION,list!!.value!!.get(position).data?.url,list!!.value!!.get(position).data?.show_header)
            }
            vp.addView(binding.root, 0)
            return binding.root
        }else if(list.value!!.get(position).action.equals(ConfigConstants.HRA_COMPLETE_ACTION)){
            var binding = UpcommingImageRowBinding.inflate(layoutInflater1, container, false)
            binding.upcommingdata=list.value!!.get(position)
            binding.homeData=homeViewModel
            if(position!=0&&position==list!!.value!!.size-1){
                binding.rlDetail.setPadding(0, 0, paddingPixel, 0);
            }
            binding.rlDetail.setOnClickListener {
                homeViewModel.upCommingActions(ConfigConstants.HRA_COMPLETE_ACTION,list?.value?.get(position)?.data?.url,list!!.value!!.get(position).data?.show_header)
            }
            vp.addView(binding.root, 0)
            return binding.root
        }
        else{
            var binding = UpcommingMacrosBinding.inflate(layoutInflater1, container, false)
            binding.macrodata=list.value!!.get(position)
            binding.homeData=homeViewModel

            if(position!=0&&position==list!!.value!!.size-1){
                binding.rlDetail.setPadding(0, 0, paddingPixel, 0);
            }
            if(list!!.value!!.get(position).today_calorie_taken.given.isNullOrEmpty()){
                binding.progress.maxProgress=list!!.value!!.get(position).today_calorie_taken.given.toDouble()
            }
            if(list!!.value!!.get(position).today_calorie_taken.taken.isNullOrEmpty()){
                binding.progress.setCurrentProgress(list!!.value!!.get(position).today_calorie_taken.taken.toDouble())
            }
            binding.rlDetail.setOnClickListener {
                homeViewModel.upCommingActions(ConfigConstants.MEAL_LOG_ACTION,list!!.value!!.get(position).url,list!!.value!!.get(position).show_header)
            }

            Util.setimage(binding.tvArrow, FontIconConstant.ARROW_RIGHT)
            Util.setimage(binding.tvCalIcon, FontIconConstant.CALORIES_ICON)
            vp.addView(binding.root, 0)
            return binding.root
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // super.destroyItem(container, position, `object`)
        val vp = container as ViewPager
        val view: View? = `object` as View?
        vp.removeView(view)
    }

    override fun getPageWidth(position: Int): Float {
        return super.getPageWidth(position);
    }
    fun updateItems(items: MutableLiveData<ArrayList<SliderActivity>>?) {
        if (items!!.value!=null&&items!!.value!!.size>0) {
            list.value = (items!!.value ?: emptyList()) as ArrayList<SliderActivity>?
            notifyDataSetChanged()
        }
    }
}