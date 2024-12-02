package com.withsejong.home

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.withsejong.R
import com.withsejong.databinding.FragmentHomeBinding
import com.withsejong.home.market.AnotherPostDetailBottomsheetDialogFragment
import com.withsejong.home.market.MyPostDetailBottomsheetDialogFragment
import com.withsejong.home.market.PostDetailActivity
import com.withsejong.home.search.SearchActivity
import com.withsejong.retrofit.BoardFindResponseDtoList
import com.withsejong.retrofit.LoadPostResponse
import com.withsejong.retrofit.RetrofitClient
import com.withsejong.retrofit.Tag
import org.json.JSONArray

class HomeFragment : Fragment(), MyPostDetailBottomsheetDialogFragment.BottomSheetListener {
    private lateinit var binding: FragmentHomeBinding
    lateinit var homeAdapter: HomeAdapter
    lateinit var categoryAdapter: CategoryAdapter
    private var loadedPageCnt = 0
    private var totalPageCnt = 0
    private val TAG = "HomeFragment"

    private var deletePostId: Int = -1


    private var searchTag = "전체"

    /*
    0=전체
    1=전공
    2=교양
    3=균필
    4=공필
    5=기타


    */


    companion object {
        val loadData = ArrayList<BoardFindResponseDtoList>()
        var isLoaded: Boolean = false
        var searchNo = "전체"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        val tokenSharedPreferences =
            requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE)
        val accessToken: String = tokenSharedPreferences.getString("accessToken", "").toString()
        Log.d(TAG, searchNo)
        //동기통신으로 변경
        if (isLoaded == false) {
            val loadPostThread = Thread {
                if(searchNo=="전체"){
                    val response = RetrofitClient.instance.loadPost(
                        accessToken = "Bearer $accessToken",
                        page = loadedPageCnt
                    )
                        .execute()

                    if (response.code() == 403) {
                        //토큰 만료
                        loadedPageCnt++
                        isLoaded = true
//                    totalPageCnt = response.body()?.totalPages ?: -1

                    } else if (response.isSuccessful) {
                        response.body()
                            ?.let { loadData.addAll(response.body()!!.boardFindResponseDtoList) }
                        loadedPageCnt++
                        isLoaded = true
                        totalPageCnt = response.body()?.totalPages ?: -1
                        //TODO 로그인 이후 바로 안뜨는 문제
                        requireActivity().runOnUiThread {
                            homeAdapter.notifyDataSetChanged()

                        }
                    } else {
                        Log.d("HomeFragment", response.toString())
                    }
                }
                else{//카테고리가 전체가 아닌경우
                    var searchTagArray = arrayListOf(searchTag)

                    val response = RetrofitClient.instance.loadSearchByTag(
                            "Bearer $accessToken",
                            searchTagArray,
                            loadedPageCnt
                        ).execute()

                    if (response.code() == 403) {
                        //토큰 만료
                        loadedPageCnt++
                        isLoaded = true
//                    totalPageCnt = response.body()?.totalPages ?: -1

                    } else if (response.isSuccessful) {
                        response.body()
                            ?.let { loadData.addAll(response.body()!!.boardFindResponseDtoList) }
                        loadedPageCnt++
                        isLoaded = true
                        totalPageCnt = response.body()?.totalPages ?: -1
                        //TODO 로그인 이후 바로 안뜨는 문제
                        requireActivity().runOnUiThread {
                            homeAdapter.notifyDataSetChanged()

                        }
                    } else {
                        Log.d("HomeFragment", response.toString())
                    }
                }



            }
            loadPostThread.join()
            loadPostThread.start()
            Log.d("HomeFragment_TAG", loadData.toString())

        }

        //쉐프에서 데이터 받아오기
        val interestingListSharedPreferences =
            requireActivity().getSharedPreferences("interesting", MODE_PRIVATE)
        val interestingListJson = interestingListSharedPreferences.getString("list", "[]")
        val itemType2 =
            object : TypeToken<ArrayList<BoardFindResponseDtoList>>() {}.type //json to list 할때 type

        val jsonToPostList = GsonBuilder().create()
            .fromJson<ArrayList<BoardFindResponseDtoList>>(interestingListJson, itemType2)
        val interestingArrayList = ArrayList<BoardFindResponseDtoList>()
        interestingArrayList.addAll(jsonToPostList)

        homeAdapter = HomeAdapter(loadData, interestingArrayList)
        binding.rcvSellList.adapter = homeAdapter
        binding.rcvSellList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        Log.d("HomeFragment_TAG", loadData.toString())



        binding.srlSwiperefresh.setOnRefreshListener {
            // 데이터 초기화
            loadData.clear()
            loadedPageCnt = 0
            totalPageCnt = 0
            isLoaded = false
            homeAdapter.notifyDataSetChanged()

            // 액세스 토큰 가져오기
            val tokenSharedPreferences =
                requireActivity().getSharedPreferences("token", MODE_PRIVATE)
            val accessToken: String = tokenSharedPreferences.getString("accessToken", "").toString()

            // `searchTag` 값에 따라 데이터를 로드
            val refreshThread = Thread {
                if (searchTag == "전체") {
                    // 전체 데이터 로드
                    val response = RetrofitClient.instance.loadPost(
                        accessToken = "Bearer $accessToken",
                        page = loadedPageCnt
                    ).execute()

                    if (response.isSuccessful) {
                        response.body()?.boardFindResponseDtoList?.let {
                            loadData.addAll(it)
                        }
                        loadedPageCnt++
                        totalPageCnt = response.body()?.totalPages ?: 0
                    } else {
                        Log.e(TAG, "전체 데이터 로드 실패: ${response.code()}")
                    }
                } else {
                    // 선택된 카테고리 데이터 로드
                    val searchTagArray = arrayListOf(searchTag)
                    val response = RetrofitClient.instance.loadSearchByTag(
                        "Bearer $accessToken",
                        searchTagArray,
                        loadedPageCnt
                    ).execute()

                    if (response.isSuccessful) {
                        response.body()?.boardFindResponseDtoList?.let {
                            loadData.addAll(it)
                        }
                        loadedPageCnt++
                        totalPageCnt = response.body()?.totalPages ?: 0
                    } else {
                        Log.e(TAG, "카테고리 데이터 로드 실패: ${response.code()}")
                    }
                }

                // UI 업데이트
                requireActivity().runOnUiThread {
                    homeAdapter.notifyDataSetChanged()
                    binding.srlSwiperefresh.isRefreshing = false
                }
            }

            refreshThread.start()
        }



        //카테고리에 들어갈 리스트 작성
        val categoryList = arrayListOf<String>(
            "전체",
            "도서",
            "의류",
            "가구",
            "전자제품",
            "기타"


//            "전공",
//            "전선",
//            "전필",
//            "공필",
//            "교양",
//            "교선",
//            "교필",
//            //"공필",
//            "기타"
        )
        categoryAdapter = CategoryAdapter(categoryList)
        binding.rcvCategory.adapter = categoryAdapter
        binding.rcvCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemType = object : TypeToken<ArrayList<String>>() {}.type //json to list 할때 type
        categoryAdapter.setCategoryClickListener(object : CategoryAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                var selectedPosition: Int? = null



                when (position) {
                    0 -> {
                        searchTag = "전체"
                        searchNo = "전체"
                        v.setBackgroundColor(Color.parseColor("#000000"))
                    }

                    1 -> {
                        searchTag = "도서"
                        searchNo = "도서"

                        v.setBackgroundColor(Color.parseColor("#000000"))

                    }

                    2 -> {
                        searchTag = "의류"
                        searchNo = "의류"

                        v.setBackgroundColor(Color.parseColor("#000000"))

                    }

                    3 -> {
                        searchTag = "가구"
                        searchNo = "가구"

                        v.setBackgroundColor(Color.parseColor("#000000"))

                    }

                    4 -> {
                        searchTag = "전자제품"
                        searchNo = "전자제품"

                        v.setBackgroundColor(Color.parseColor("#000000"))

                    }
//                    5->{
//                        searchTag = "교양"
//                    }
//                    6->{
//                        searchTag = "교선"
//                    }
//                    7->{
//                        searchTag = "교필"
//                    }
                    else -> {
                        searchTag = "기타"
                        searchNo = "기타"

                        v.setBackgroundColor(Color.parseColor("#000000"))

                    }
                }
                Log.d("HomeFragment_TAG", searchTag)
                loadData.clear()
                var searchTagArray = arrayListOf(searchTag)
                Log.d("HomeFragment_TAG", searchTagArray.toString())
                val tokenSharedPreferences =
                    requireActivity().getSharedPreferences("token", MODE_PRIVATE)
                val accessToken: String =
                    tokenSharedPreferences.getString("accessToken", "").toString()
                loadedPageCnt = 0
                totalPageCnt = 0
                if (searchTag == "전체") {
                    val loadPostThread = Thread {
                        loadData.clear()
                        loadedPageCnt = 0
                        totalPageCnt = 0
                        val response = RetrofitClient.instance.loadPost(
                            accessToken = "Bearer $accessToken",
                            page = loadedPageCnt
                        )
                            .execute()
                        if (response.code() == 403) {
                            //토큰 만료
                            loadedPageCnt++
                            isLoaded = true
//                    totalPageCnt = response.body()?.totalPages ?: -1
                        } else if (response.isSuccessful) {
                            response.body()
                                ?.let { loadData.addAll(response.body()!!.boardFindResponseDtoList) }
                            loadedPageCnt++
                            isLoaded = true
                            totalPageCnt = response.body()?.totalPages ?: -1
                        } else {
                            Log.d("HomeFragment", response.toString())
                        }
                        requireActivity().runOnUiThread {
                            //binding.rcvSellList.adapter=homeAdapter(loadDat a)
                            homeAdapter.notifyDataSetChanged()
                        }
                    }
                    loadPostThread.join()
                    loadPostThread.start()
                    Log.d("HomeFragment_TAG", loadData.toString())
                } else {
                    val searchByTagThread = Thread {
                        val response = RetrofitClient.instance.loadSearchByTag(
                            "Bearer $accessToken",
                            searchTagArray,
                            loadedPageCnt
                        ).execute()
                        Log.d("HomeFragment_TAG_", response.code().toString())
                        if (response.code() == 403) {
                            //토큰 만료
                            loadedPageCnt++
                            isLoaded = true
//                    totalPageCnt = response.body()?.totalPages ?: -1
                        } else if (response.isSuccessful) {
                            loadData.clear()
                            response.body()
                                ?.let { loadData.addAll(response.body()!!.boardFindResponseDtoList) }
                            loadedPageCnt++
                            isLoaded = true
                            totalPageCnt = response.body()?.totalPages ?: -1
                            Log.d("HomeFragment_TAG_", response.toString())
                            Log.d("HomeFragment_TAG_", response.body().toString())
                            Log.d("HomeFragment_TAG_", loadData.toString())
                        } else {
                            Log.d("HomeFragment_TAG", response.toString())
                        }
                        Log.d("HomeFragment_TAG", loadData.toString())
                        requireActivity().runOnUiThread {
                            //binding.rcvSellList.adapter=homeAdapter(loadDat a)
                            homeAdapter.notifyDataSetChanged()
                        }
                    }
                    searchByTagThread.join()
                    searchByTagThread.start()
                }
            }
        })

        //어댑터의 요소들 클릭했을 경우
        val intentPost = Intent(requireContext(), PostDetailActivity::class.java)
        homeAdapter.setItemClickListener(object : HomeAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                //Toast.makeText(requireContext(),loadData[position].title,Toast.LENGTH_SHORT).show()
                //TODO 닉네임도 api에 추가되면 넣을 것
                intentPost.putExtra("nickname", loadData[position].nickname)
                intentPost.putExtra("major", loadData[position].major)
                intentPost.putExtra("productName", loadData[position].title)
                //시간 전송
                intentPost.putExtra("createAt", loadData[position].createdAt)
                intentPost.putExtra("boardId", loadData[position].id)
                intentPost.putExtra("postId", loadData[position].studentId)
                intentPost.putExtra("boardTitle", loadData[position].title)


                //intentPost.putExtra("img1", loadData[position].image[0].url)
                val imageUriList = ArrayList<String>()
                for (i: Int in 0 until loadData[position].image.size) {
                    imageUriList.add(loadData[position].image[0].url)
                }


                intentPost.putStringArrayListExtra("imgArray", imageUriList)


                val priceAddComma = loadData[position].price.addCommas()//콤마 붙이는 코드
                intentPost.putExtra("productPrice", priceAddComma + "원")
                intentPost.putExtra("productContent", loadData[position].content)
                //TODO 태그 배열을 보내야하는데 흠...
                //intentPost.putStringArrayListExtra("tag", loadData[position].tag)

                val tagArrayList = ArrayList<String>()
                loadData[position].tag.forEach {
                    tagArrayList.add(it.category)
                }

                val tagListtoJson = GsonBuilder().create().toJson(tagArrayList, itemType)

                Log.d("HomeFragment_TAG", tagListtoJson)

                intentPost.putExtra("tag", tagListtoJson)
                startActivity(intentPost)
            }

        })
        val itemType2 =
            object : TypeToken<ArrayList<BoardFindResponseDtoList>>() {}.type //json to list 할때 type

        homeAdapter.setItemPickClickListener(object : HomeAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val interestingListSharedPreferences = requireActivity().getSharedPreferences(
                    "interesting",
                    MODE_PRIVATE
                )
                var interstingPostJson = interestingListSharedPreferences.getString("list", "[]")

                var jsonToPostList = GsonBuilder().create()
                    .fromJson<ArrayList<BoardFindResponseDtoList>>(interstingPostJson, itemType2)
                val interestingArrayList = ArrayList<BoardFindResponseDtoList>()
                interestingArrayList.addAll(jsonToPostList)
                if (v is ImageButton) {
                    if (v.tag.equals("unpicked")) {
                        v.setImageResource(R.drawable.icon_post_picked)
                        v.tag = "picked"
                        interestingArrayList.add(loadData[position])
                        Log.d("HomeFragment_TAG_interestingList", interestingArrayList.toString())

                        val postListtoJson =
                            GsonBuilder().create().toJson(interestingArrayList, itemType2)


                        val editor = interestingListSharedPreferences.edit()
                        editor.putString("list", postListtoJson)
                        editor.apply()
                    } else {
                        v.tag = "unpicked"
                        v.setImageResource(R.drawable.icon_post_unpick)
                        Log.d("HomeFragment_TAG_interestingList_b", interestingArrayList.toString())

                        interestingArrayList.remove(loadData[position])
                        Log.d("HomeFragment_TAG_interestingList_a", interestingArrayList.toString())

                        val postListtoJson =
                            GsonBuilder().create().toJson(interestingArrayList, itemType2)
                        val editor = interestingListSharedPreferences.edit()
                        editor.putString("list", postListtoJson)
                        editor.apply()


                        interstingPostJson =
                            interestingListSharedPreferences.getString("list", "[]")

                        jsonToPostList = GsonBuilder().create()
                            .fromJson<ArrayList<BoardFindResponseDtoList>>(
                                interstingPostJson,
                                itemType2
                            )
                        interestingArrayList.clear()
                        interestingArrayList.addAll(jsonToPostList)
                    }
//                    if (currentImageId == R.drawable.icon_post_unpick) {
//                        v.setImageResource(R.drawable.icon_post_picked)
//                        v.tag = R.drawable.icon_post_picked
//                    }
//                    //TODO 잠깐 주석처리 pick일때 클릭시 unpick으로 변경
//                    else {
//                        v.setImageResource(R.drawable.icon_post_unpick)
//                        v.tag = R.drawable.icon_post_unpick
//                    }
                }
            }
        })

        //페이징을 위한
        binding.rcvSellList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val rvPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val totalCount =
                    recyclerView.adapter?.itemCount?.minus(1)

                if (rvPosition == totalCount && loadedPageCnt < totalPageCnt) {
                    Log.d("HomeFragment_TAG", "page자료형구현성공")

                    val tokenSharedPreferences =
                        requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE)
                    val accessToken: String =
                        tokenSharedPreferences.getString("accessToken", "").toString()

                    val beforeListCnt = loadData.size
                    val addListCnt = loadMoreData(accessToken = accessToken, page = loadedPageCnt)

                    // RecyclerView.post를 사용해 notifyItemRangeInserted 호출을 안전하게 처리
                    recyclerView.post {
                        homeAdapter.notifyItemRangeInserted(beforeListCnt, addListCnt)
                    }
                }
            }
        })


        //점3개 클릭했을 때
        val anotherPostDetailBottomsheetDialogFragment =
            AnotherPostDetailBottomsheetDialogFragment()
        val myPostDetailBottomsheetDialogFragment = MyPostDetailBottomsheetDialogFragment()

        val userInfoSharedPreferences =
            requireActivity().getSharedPreferences("userInfo", MODE_PRIVATE)
        val studentId = userInfoSharedPreferences.getString("studentId", "")
        //어댑터의 점3개를 클릭했을 겨우

        homeAdapter.setItemDetailClickListener(object : HomeAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {

                if (loadData[position].studentId == studentId) {//내가 작성한 글인경우

                    val myPostBundle = Bundle()
                    myPostBundle.putString("boardId", loadData[position].id.toString())
                    myPostBundle.putString("position", position.toString())

                    myPostDetailBottomsheetDialogFragment.arguments = myPostBundle

                    myPostDetailBottomsheetDialogFragment.show(
                        parentFragmentManager,
                        myPostDetailBottomsheetDialogFragment.tag
                    )

                    parentFragmentManager.setFragmentResultListener(
                        "myPostFunc",
                        this@HomeFragment,
                        object : FragmentResultListener {
                            override fun onFragmentResult(requestKey: String, result: Bundle) {
                                val deleteCode = result.getBoolean("isDeleted", false)
                                val pullUpCode = result.getBoolean("isPullUp", false)
                                if (deleteCode) {
                                    requireActivity().runOnUiThread {
                                        Log.d(TAG, position.toString())

                                        loadData.removeAt(position)
                                        homeAdapter.notifyItemRemoved(position)
                                        homeAdapter.notifyItemRangeChanged(position, loadData.size)
                                    }

                                } else if (pullUpCode) {
                                    requireActivity().runOnUiThread {
                                        val createdAt = result.getString("createdAt", "")
                                        val tmpBoard =
                                            loadData[position].copy(createdAt = createdAt)
                                        loadData.removeAt(position)
                                        loadData.add(0, tmpBoard)
                                        homeAdapter.notifyItemMoved(position, 0)
                                        homeAdapter.notifyItemRangeChanged(0, position + 1)
                                        Log.d(TAG, loadData.toString())
                                    }
                                }
                            }
                        })
                } else {
                    val anotherBundle = Bundle()
                    anotherBundle.putString("postId", loadData[position].studentId)
                    anotherBundle.putInt("boardId", loadData[position].id)
                    anotherBundle.putString("boardTitle", loadData[position].title)
                    anotherPostDetailBottomsheetDialogFragment.arguments = anotherBundle
                    anotherPostDetailBottomsheetDialogFragment.show(
                        parentFragmentManager,
                        anotherPostDetailBottomsheetDialogFragment.tag
                    )

                }


            }
        }
        )

        //검색버튼 클릭시
        val intentSearch = Intent(requireContext(), SearchActivity::class.java)
        binding.ibtnSearchIcon.setOnClickListener {
            startActivity(intentSearch)
        }
    }

    private fun loadMoreData(accessToken: String, page: Int): Int {

        val moreData = ArrayList<LoadPostResponse>()
        //일단 무지성으로 동기통신
        val beforesize = loadData.size
        var aftersize = loadData.size
        val loadMoreDataThread = Thread {
            val response =
                RetrofitClient.instance.loadPost(accessToken = "Bearer $accessToken", page)
                    .execute()
            if (response.code() == 403) {
                //TODO 리프레시 후 재 로드, return으로 추가한 개수
            } else if (response.isSuccessful) {

                response.body()?.let { loadData.addAll(response.body()!!.boardFindResponseDtoList) }
                aftersize = loadData.size
                loadedPageCnt++
            }

        }
        loadMoreDataThread.start()
        loadMoreDataThread.join()
        return aftersize - beforesize


    }

    override fun onBottomSheetResult(success: Boolean, position: Int) {
        Log.d("HomeFragment_TAG", "onBottomSheetResult함수 진입")

        if (success) {
            Log.d("HomeFragment_TAG", "onBottomSheetResult진입")
            homeAdapter.notifyItemRemoved(position)
        }
    }


}