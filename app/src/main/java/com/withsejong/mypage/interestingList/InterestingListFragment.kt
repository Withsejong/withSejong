package com.withsejong.mypage.interestingList

import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.withsejong.home.addCommas
import com.withsejong.home.market.PostDetailActivity
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

        if(interestingArrayList.size==0){
            binding.cloEmptyItemNotification.visibility = View.VISIBLE
        }


        interestingListAdapter = InterestingListAdapter(interestingArrayList)
        binding.rcvInterestingList.adapter = interestingListAdapter
        binding.rcvInterestingList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

        val intentPost = Intent(requireContext(), PostDetailActivity::class.java)

        interestingListAdapter.setItemClickListener(object :InterestingListAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                //Toast.makeText(requireContext(),loadData[position].title,Toast.LENGTH_SHORT).show()
                //TODO 닉네임도 api에 추가되면 넣을 것
                intentPost.putExtra("nickname", interestingArrayList[position].nickname)
                intentPost.putExtra("major", interestingArrayList[position].major)
                intentPost.putExtra("productName", interestingArrayList[position].title)
                //시간 전송
                intentPost.putExtra("createAt", interestingArrayList[position].createdAt)
                intentPost.putExtra("boardId", interestingArrayList[position].id)
                intentPost.putExtra("postId", interestingArrayList[position].studentId)
                intentPost.putExtra("boardTitle", interestingArrayList[position].title)


                //intentPost.putExtra("img1", loadData[position].image[0].url)
                val imageUriList = ArrayList<String>()
                for(i:Int in 0 until interestingArrayList[position].image.size){
                    imageUriList.add(interestingArrayList[position].image[0].url)
                }


                intentPost.putStringArrayListExtra("imgArray", imageUriList)


                val priceAddComma = interestingArrayList[position].price.addCommas()//콤마 붙이는 코드
                intentPost.putExtra("productPrice", priceAddComma+"원")
                intentPost.putExtra("productContent", interestingArrayList[position].content)
                //TODO 태그 배열을 보내야하는데 흠...
                //intentPost.putStringArrayListExtra("tag", loadData[position].tag)

                val tagArrayList = ArrayList<String>()
                interestingArrayList[position].tag.forEach{
                    tagArrayList.add(it.category)
                }
                val itemType = object : TypeToken<ArrayList<String>>() {}.type //json to list 할때 type

                val tagListtoJson = GsonBuilder().create().toJson(tagArrayList,itemType)

                Log.d("HomeFragment_TAG",tagListtoJson)

                intentPost.putExtra("tag",tagListtoJson)
                startActivity(intentPost)
            }

        })


        //관심목록 하트 아이콘 클릭
        interestingListAdapter.setItemPickClickListener(object : InterestingListAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {

                AlertDialog.Builder(requireActivity())
                    .setTitle("관심목록 제거")
                    .setMessage("관심목록에서 제거하시겠습니까?")
                    .setPositiveButton("제거",object :DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            interestingArrayList.removeAt(position)
                            interestingListAdapter.notifyItemRemoved(position)
                            if(interestingArrayList.size==0){
                                binding.cloEmptyItemNotification.visibility = View.VISIBLE
                            }


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