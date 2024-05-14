package com.withsejong.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.withsejong.R
import com.withsejong.databinding.FragmentHomeBinding
import com.withsejong.home.market.AnotherPostDetailFragment
import com.withsejong.home.search.SearchActivity
import com.withsejong.retrofit.LoadPostResponse
import com.withsejong.retrofit.RetrofitClient
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding

    companion object{
        val loadData = ArrayList<LoadPostResponse>()

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)


        var page :Int = 0

        val tokenSharedPreferences = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE)
        val accessToken:String = tokenSharedPreferences.getString("accessToken","").toString()
        RetrofitClient.instance.loadPost(accessToken = "Bearer $accessToken", page = page).enqueue(
            object : retrofit2.Callback<List<LoadPostResponse>>{
                override fun onResponse(
                    call: retrofit2.Call<List<LoadPostResponse>>,
                    response: Response<List<LoadPostResponse>>
                ) {
//                    for(i:Int in 0 until (response.body()?.size ?: 0)){
//                        loadData.add(response.body())
//                    }
                    Log.d("HomeFragment", response.toString())
                    response.body()?.let { loadData.addAll(it) }


                }

                override fun onFailure(
                    call: retrofit2.Call<List<LoadPostResponse>>,
                    t: Throwable
                ) {
                    Log.d("HomeFragment", t.toString())
                }


            }
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //더미데이터 생성
        //TODO 더미데이터 테스트 코드이므로 추후에 통신을 통해 리스트에 저장하는 것 구현할 것!

//        val mockData = ArrayList<PostData>()
//        mockData.add(PostData("코딩강화 파이썬",
//            arrayListOf("#지기전","#소융대"),
//            7,
//            10000)
//        )
//        mockData.add(PostData("소프트웨어 개념사전",
//            arrayListOf("#컴공","#소융대","#소웨","#전정통"),
//            5,
//            20000)
//        )
//        mockData.add(PostData("돼지책 파이썬",
//            arrayListOf("#컴공","#소융대","#소웨","#전정통"),
//            5,
//            20000)
//        )
//        mockData.add(PostData("코딩강화 파이썬",
//            arrayListOf("#지기전","#소융대"),
//            7,
//            10000)
//        )
//        mockData.add(PostData("소프트웨어 개념사전",
//            arrayListOf("#컴공","#소융대","#소웨","#전정통"),
//            5,
//            20000)
//        )
//        mockData.add(PostData("돼지책 파이썬",
//            arrayListOf("#컴공","#소융대","#소웨","#전정통"),
//            5,
//            20000)
//        )
//        mockData.add(PostData("코딩강화 파이썬",
//            arrayListOf("#지기전","#소융대"),
//            7,
//            10000)
//        )
//        mockData.add(PostData("소프트웨어 개념사전",
//            arrayListOf("#컴공","#소융대","#소웨","#전정통"),
//            5,
//            20000)
//        )
//        mockData.add(PostData("돼지책 파이썬",
//            arrayListOf("#컴공","#소융대","#소웨","#전정통"),
//            5,
//            20000)
//        )

        val homeAdapter = HomeAdapter(loadData)
        binding.rcvSellList.adapter=homeAdapter
        binding.rcvSellList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        //어댑터의 요소들 클릭했을 경우
        homeAdapter.setItemClickListener(object:HomeAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                Toast.makeText(requireContext(),loadData[position].title,Toast.LENGTH_SHORT).show()
            }

        })

        //점3개 클릭했을 때
        val anotherPostDetailFragment = AnotherPostDetailFragment()

        //어댑터의 점3개를 클릭했을 겨우
        //TODO 여기에 분기 추가해서 자기글일때와 아닐때 구분
        homeAdapter.setItemDetailClickListener(object :HomeAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                anotherPostDetailFragment.show(parentFragmentManager,anotherPostDetailFragment.tag)
            }
        }
        )

        //검색버튼 클릭시

        binding.ibtnSearchIcon.setOnClickListener {
            val intent = Intent(requireContext(),SearchActivity::class.java)
            startActivity(intent)
        }

        //카테고리에 들어갈 리스트 작성
        val categoryList = arrayListOf<String>(
            "전체",
            "전공",
            "교양",
            "균필",
            "공필",
            "기타"
        )
        binding.rcvCategory.adapter=CategoryAdapter(categoryList)
        binding.rcvCategory.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)


    }



}