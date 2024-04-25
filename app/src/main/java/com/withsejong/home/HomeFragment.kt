package com.withsejong.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.withsejong.R
import com.withsejong.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //더미데이터 생성
        //TODO 더미데이터 테스트 코드이므로 추후에 통신을 통해 리스트에 저장하는 것 구현할 것!

        val mockData = ArrayList<PostData>()
        mockData.add(PostData("코딩강화 파이썬",
            arrayListOf("#지기전","#소융대"),
            7,
            10000)
        )
        mockData.add(PostData("소프트웨어 개념사전",
            arrayListOf("#컴공","#소융대","#소웨","#전정통"),
            5,
            20000)
        )
        mockData.add(PostData("돼지책 파이썬",
            arrayListOf("#컴공","#소융대","#소웨","#전정통"),
            5,
            20000)
        )
        mockData.add(PostData("코딩강화 파이썬",
            arrayListOf("#지기전","#소융대"),
            7,
            10000)
        )
        mockData.add(PostData("소프트웨어 개념사전",
            arrayListOf("#컴공","#소융대","#소웨","#전정통"),
            5,
            20000)
        )
        mockData.add(PostData("돼지책 파이썬",
            arrayListOf("#컴공","#소융대","#소웨","#전정통"),
            5,
            20000)
        )
        mockData.add(PostData("코딩강화 파이썬",
            arrayListOf("#지기전","#소융대"),
            7,
            10000)
        )
        mockData.add(PostData("소프트웨어 개념사전",
            arrayListOf("#컴공","#소융대","#소웨","#전정통"),
            5,
            20000)
        )
        mockData.add(PostData("돼지책 파이썬",
            arrayListOf("#컴공","#소융대","#소웨","#전정통"),
            5,
            20000)
        )

        val homeAdapter = HomeAdapter(mockData)
        binding.rcvSellList.adapter=homeAdapter
        binding.rcvSellList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        //어댑터의 요소들 클릭했을 경우

        homeAdapter.setItemClickListener(object:HomeAdapter.OnitemClickListener{
            override fun onClick(v: View, position: Int) {
                Toast.makeText(requireContext(),mockData[position].name,Toast.LENGTH_SHORT).show()
            }

        })

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