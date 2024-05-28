package com.withsejong.mypage.sellList

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.withsejong.R
import com.withsejong.databinding.FragmentSellListBinding
import com.withsejong.mypage.MypageMainFragment
import com.withsejong.retrofit.BoardFindResponseDtoList
import com.withsejong.retrofit.LoadPostResponse
import com.withsejong.retrofit.RetrofitClient


class SellListFragment : Fragment() {
    private lateinit var binding : FragmentSellListBinding
    private var loadedPageCnt = 0
    private var totalPageCnt = 0
    private val TAG = "SellListFragment"
    private val loadData = ArrayList<BoardFindResponseDtoList>()
    private var isLoaded:Boolean =false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentSellListBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //상단 뒤로가기 버튼 동작 정의
        val mypageMainFragment = MypageMainFragment()

        val tokenSharedPreferences = requireContext().getSharedPreferences("token",MODE_PRIVATE)
        val userInfoSharedPreferences = requireContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE)

        val accessToken = tokenSharedPreferences.getString("accessToken", "").toString()
        val studentId = userInfoSharedPreferences.getString("studentId","").toString()

        binding.ibtnBack.setOnClickListener {
            val fragmentManager = parentFragmentManager.beginTransaction()
            fragmentManager.replace(R.id.fcv_all_fragment,mypageMainFragment).commit()
        }
        //휴대폰 뒤로가기를 누른 경우 이전 fragment로 돌아가는 행동 정의
        //TODO 뒤로가기 코드에서 뒤로가기 광클릭시 에러 발생
        val backActionCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val fragmentManager = parentFragmentManager.beginTransaction()
                fragmentManager.replace(R.id.fcv_all_fragment,mypageMainFragment).commit()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(),backActionCallback)

    if(isLoaded==false){
        val loadListThread = Thread{
            val response = RetrofitClient.instance.loadSellList("Bearer $accessToken", studentId,loadedPageCnt).execute()
            Log.d(TAG,response.toString())

            if(response.code()==403){
                Log.d(TAG,response.body().toString())

            }
            else if(response.isSuccessful){

                response.body()?.let { loadData.addAll(response.body()!!.boardFindResponseDtoList) }
                loadedPageCnt++
                isLoaded =true
                totalPageCnt = response.body()?.totalPages ?: -1
                Log.d(TAG,response.body().toString())

            }
            else{
                Log.d(TAG,response.toString())

            }

        }
        loadListThread.start()
        loadListThread.join()
    }



        val sellListAdapter = SellListAdapter(loadData)
        Log.d(TAG, loadData.toString())
        binding.rcvSellList.adapter = sellListAdapter
        binding.rcvSellList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)


        //페이징을 위한
        binding.rcvSellList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val rvPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val totalCount =
                    recyclerView.adapter?.itemCount?.minus(1)

                if(rvPosition==totalCount && loadedPageCnt<=totalPageCnt){
                    Log.d(TAG, "page자료형구현성공")
                    val tokenSharedPreferences = requireActivity().getSharedPreferences("token", MODE_PRIVATE)
                    val accessToken:String = tokenSharedPreferences.getString("accessToken","").toString()
                    val beforeListCnt = loadData.size
                    val addListCnt = loadMoreData(accessToken = accessToken, studentId, page = loadedPageCnt)

                    sellListAdapter.notifyItemRangeInserted(beforeListCnt,addListCnt)
                }
            }

        })
    }
    private fun loadMoreData(accessToken:String, studentId:String, page:Int):Int {
        //일단 무지성으로 동기통신
        val beforesize = loadData.size
        var aftersize = loadData.size
        val loadMoreDataThread = Thread{
            val response = RetrofitClient.instance.loadSellList(accessToken = "Bearer $accessToken",studentId, page).execute()
            if(response.code()==403){
                //TODO 리프레시 후 재 로드, return으로 추가한 개수
            }
            else if(response.isSuccessful){

                response.body()?.let { loadData.addAll(response.body()!!.boardFindResponseDtoList) }
                aftersize = loadData.size
                loadedPageCnt++


            }

        }
        loadMoreDataThread.start()
        loadMoreDataThread.join()
        return aftersize-beforesize


    }



}