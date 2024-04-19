package com.withsejong.mypage.faq

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.withsejong.R
import com.withsejong.databinding.FragmentFaqBinding
import com.withsejong.retrofit.RetrofitClient
import com.withsejong.retrofit.loadFaqResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class FaqFragment : Fragment() {

    private lateinit var binding:FragmentFaqBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentFaqBinding.inflate(layoutInflater,container,false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tokenSharedPreferences = requireContext().getSharedPreferences("token",
            Context.MODE_PRIVATE
        )

        RetrofitClient.instance.loadFaq(accessToken = "Bearer ${tokenSharedPreferences.getString("accessToken","error")}").enqueue(object : Callback<ArrayList<loadFaqResponse>>{
            override fun onResponse(
                call: Call<ArrayList<loadFaqResponse>>,
                response: Response<ArrayList<loadFaqResponse>>
            ) {
                if(response.isSuccessful){//FAQ를 잘 받아 온 경우

                    Log.d("FaqFragment_TAG", response.toString())
                    val responseList : ArrayList<loadFaqResponse>? = response.body()

                    binding.rcvNoticyList.adapter = FaqAdapter(responseList)
                    binding.rcvNoticyList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

                }
                else{
                    Log.d("FaqFragment_TAG", response.toString())

                }
            }

            override fun onFailure(call: Call<ArrayList<loadFaqResponse>>, t: Throwable) {
                Log.d("FaqFragment_TAG", t.toString())
            }

        })


    }
}