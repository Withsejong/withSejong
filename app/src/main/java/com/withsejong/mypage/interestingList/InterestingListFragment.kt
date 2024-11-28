package com.withsejong.mypage.interestingList

import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.withsejong.R
import com.withsejong.databinding.FragmentInterestingListBinding
import com.withsejong.mypage.MypageMainFragment
import com.withsejong.retrofit.BoardFindResponseDtoList

class InterestingListFragment : Fragment() {
    private lateinit var binding:FragmentInterestingListBinding
    lateinit var interestingListAdapter: InterestingListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInterestingListBinding.inflate(layoutInflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mypageMainFragment = MypageMainFragment()
        binding.ibtnBack.setOnClickListener {
            val fragmentManager = parentFragmentManager.beginTransaction()
            fragmentManager.replace(R.id.fcv_all_fragment,mypageMainFragment).commit()
        }
        //휴대폰 뒤로가기를 누른 경우 이전 fragment로 돌아가는 행동 정의
        val backActionCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val fragmentManager = parentFragmentManager.beginTransaction()
                fragmentManager.replace(R.id.fcv_all_fragment,mypageMainFragment).commit()
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(),backActionCallback)

        //쉐프에서 데이터 받아오기
        val interestingListSharedPreferences = requireActivity().getSharedPreferences("interesting",MODE_PRIVATE)
        val interestingListJson=interestingListSharedPreferences.getString("list","[]")
        val itemType2 = object : TypeToken<ArrayList<BoardFindResponseDtoList>>() {}.type //json to list 할때 type

        val jsonToPostList = GsonBuilder().create().fromJson<ArrayList<BoardFindResponseDtoList>>(interestingListJson,itemType2)
        val interestingArrayList = ArrayList<BoardFindResponseDtoList>()
        interestingArrayList.addAll(jsonToPostList)


        interestingListAdapter = InterestingListAdapter(interestingArrayList)
        binding.rcvInterestingList.adapter = interestingListAdapter
        binding.rcvInterestingList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)


        interestingListAdapter.setItemPickClickListener(object : InterestingListAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {

                AlertDialog.Builder(requireActivity())
                    .setTitle("관심목록 제거")
                    .setMessage("관심목록에서 제거하시겠습니까?")
                    .setPositiveButton("제거",object :DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            interestingArrayList.removeAt(position)
                            interestingListAdapter.notifyItemRemoved(position)

                            val postListtoJson = GsonBuilder().create().toJson(interestingArrayList,itemType2)


                            val editor = interestingListSharedPreferences.edit()
                            editor.putString("list",postListtoJson)
                            editor.apply()

                        }

                    })
                    .setNegativeButton("취소"
                    ) { dialog, which ->

                        dialog.dismiss()
                    }.show()


            }

        })

    }




}