package com.fitpass.libfitpass.home

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.fitpass.libfitpass.R
import com.fitpass.libfitpass.base.utilities.FitpassConfig
import com.fitpass.libfitpass.base.utilities.FitpassConfigUtil
import com.fitpass.libfitpass.databinding.FragmentHomeBinding
import com.fitpass.libfitpass.home.adapters.FaqAdapter
import com.fitpass.libfitpass.home.adapters.UpcomingAdapter
import com.fitpass.libfitpass.home.http_client.CommonRepository
import com.fitpass.libfitpass.home.viewmodel.HomeViewModel
import com.fitpass.libfitpass.home.viewmodelfactory.HomeViewModelFactory
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private var homeViewModel: HomeViewModel?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPadding()

        var commonRepository= CommonRepository(requireContext(),requireActivity())
        var viewModelFactories=  HomeViewModelFactory(commonRepository,requireContext(),requireActivity(), binding.vpUpcomming, binding.llDots)
        homeViewModel= ViewModelProvider(this,viewModelFactories!!).get(HomeViewModel::class.java)
        binding.lifecycleOwner=viewLifecycleOwner
        binding.homedata=homeViewModel
        homeViewModel?.let {
            var request= JSONObject()
            request.put("vendor_id","183")
            request.put("policy_number","11111111113111")
            request.put("member_id","15143512435")
            // it.getHomeData(Request("15143512435","11111111113111","183"))
            it.getHomeData(request)
        }

    }


    fun setPadding() {
        var fitpassConfig = FitpassConfig.getInstance()
        var paddingDp: Int = fitpassConfig!!.getPadding();
        var density = getResources().getDisplayMetrics().density.toFloat()
        var paddingPixel = (paddingDp * density).toInt();
        binding.rlFitpassid.setPadding(paddingPixel, 0, paddingPixel, 0);
        binding.rvMenu.setPadding(paddingPixel, 0, paddingPixel, 0);

        var paddingDp1: Int = fitpassConfig!!.getPadding() - 5;
        var paddingPixel1 = (paddingDp1 * density).toInt();
        binding.vpUpcomming.setPadding(paddingPixel1, 0, 0, 0);
        var paddingDp2: Int = fitpassConfig!!.getPadding() - 4;
        var paddingPixel2 = (paddingDp2 * density).toInt();
        binding.rvFaq.setPadding(paddingPixel, 0, paddingPixel, 0);
    }



}