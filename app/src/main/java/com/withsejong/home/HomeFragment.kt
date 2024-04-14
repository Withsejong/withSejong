package com.withsejong.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onStart() {
        super.onStart()

        //더미데이터 생성
        //TODO 더미데이터 테스트 코드이므로 추후에 통신을 통해 리스트에 저장하는 것 구현할 것!

        val mockData = ArrayList<PostData>()
        mockData.add(PostData("코딩강화 파이썬",
            arrayListOf("#지기전","소융대"),
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
            arrayListOf("#지기전","소융대"),
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
            arrayListOf("#지기전","소융대"),
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
            arrayListOf("#지기전","소융대"),
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

    }


}