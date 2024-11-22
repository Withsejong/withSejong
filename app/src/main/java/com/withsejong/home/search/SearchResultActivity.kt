package com.withsejong.home.search

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.withsejong.databinding.ActivityPostSearchResultBinding
import com.withsejong.home.CategoryAdapter
import com.withsejong.home.addCommas
import com.withsejong.home.market.PostDetailActivity
import com.withsejong.retrofit.BoardFindResponseDtoList
import com.withsejong.retrofit.RetrofitClient

class SearchResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostSearchResultBinding
    lateinit var searchResultAdapter: SearchResultAdapter
    lateinit var categoryAdapter: CategoryAdapter


    private var loadedPageCnt = 0
    private var totalPageCnt = 0
    private lateinit var searchWord: String
    private val TAG = "SearchResultActivity_TAG"
    private var searchTag="전체"


    companion object {
        private val loadData = ArrayList<BoardFindResponseDtoList>()
        private var isLoaded: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostSearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isLoaded = false

        val searchHistorySharedPreferences = getSharedPreferences("searchHistory", MODE_PRIVATE)
        var searchJson = searchHistorySharedPreferences.getString("searchWord", ArrayList<String>().toString()) //json

        val itemType = object : TypeToken<ArrayList<String>>() {}.type
        val searchJsontoList = GsonBuilder().create().fromJson<ArrayList<String>>(searchJson,itemType)

        val editor = searchHistorySharedPreferences.edit()

        searchWord = intent.getStringExtra("searchWord").toString()

        searchJsontoList.add(searchJsontoList.size,searchWord)

        if(searchJsontoList.isNotEmpty() && searchJsontoList.size>5){
            searchJsontoList.removeAt(0)
        }

        val resultJson = GsonBuilder().create().toJson(searchJsontoList,itemType)

        editor.putString("searchWord",resultJson)
        editor.apply()

        binding.etSearch.setText(searchWord)

        val tokenSharedPreferences = getSharedPreferences("token", MODE_PRIVATE)
        val accessToken = tokenSharedPreferences.getString("accessToken", "").toString()

        if(isLoaded==false){ //TODO 이거 불필요 한것 같긴함
            loadSearchPost(accessToken)
        }

        Log.d(TAG, loadData.toString())
        searchResultAdapter = SearchResultAdapter(loadData)
        binding.rcvSellList.adapter = searchResultAdapter
        binding.rcvSellList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.ibtnSearch.setOnClickListener {
            loadData.clear()
            searchWord=binding.etSearch.text.toString()
            loadedPageCnt=0
            isLoaded =false
            loadSearchPost(accessToken)
            searchResultAdapter.notifyDataSetChanged()
        }
        //엔터키를 누른경우도 검색어에 해당하는 rcv로 교체
        binding.etSearch.setOnEditorActionListener { v, actionId, event ->
            if(actionId==EditorInfo.IME_ACTION_DONE){
                loadData.clear()
                searchWord=binding.etSearch.text.toString()
                loadedPageCnt=0
                isLoaded =false
                loadSearchPost(accessToken)
                searchResultAdapter.notifyDataSetChanged()
                // 키보드 숨기기
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

                true
            }
            else{
                false
            }
        }

        //페이징
        binding.rcvSellList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val rvPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val totalCount =
                    recyclerView.adapter?.itemCount?.minus(1)

                if (rvPosition == totalCount && loadedPageCnt <= totalPageCnt) {
                    Log.d("SearchResultActivity", "page자료형구현성공")
                    val tokenSharedPreferences = getSharedPreferences("token", Context.MODE_PRIVATE)
                    val accessToken: String =
                        tokenSharedPreferences.getString("accessToken", "").toString()
                    val beforeListCnt = loadData.size
                    val addListCnt = loadMoreData(accessToken = accessToken, searchWord, page = loadedPageCnt)

                    searchResultAdapter.notifyItemRangeInserted(beforeListCnt, addListCnt)
                }
            }
        })
        binding.srlSwiperefresh.setOnRefreshListener {
            loadData.clear()
            loadedPageCnt=0
            isLoaded =false
            loadSearchPost(accessToken)
            searchResultAdapter.notifyDataSetChanged()
            binding.srlSwiperefresh.isRefreshing=false
        }

        //X버튼 누를경우 검색어 다 지우는 코드
        binding.ibtnClear.setOnClickListener {
            binding.etSearch.setText("")
        }

        val categoryList = arrayListOf<String>(
            "전체",
            "전공",
            "전선",
            "전필",
            "공필",
            "교양",
            "교선",
            "교필",
            //"공필",
            "기타"
        )
        categoryAdapter = CategoryAdapter(categoryList)
        binding.rcvCategory.adapter = categoryAdapter
        binding.rcvCategory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        categoryAdapter.setCategoryClickListener(object : CategoryAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                when(position){
                    0->{
                        searchTag = "전체"
                    }
                    1->{
                        searchTag = "전공"
                    }
                    2->{
                        searchTag = "전선"
                    }
                    3->{
                        searchTag = "전필"
                    }
                    4->{
                        searchTag = "공필"
                    }
                    5->{
                        searchTag = "교양"
                    }
                    6->{
                        searchTag = "교선"
                    }
                    7->{
                        searchTag = "교필"
                    }
                    else->{
                        searchTag = "기타"
                    }
                }
                Log.d("HomeFragment_TAG", searchTag)
                loadData.clear()
                var searchTagArray = arrayListOf(searchTag)
                Log.d("HomeFragment_TAG", searchTagArray.toString())
                val tokenSharedPreferences = this@SearchResultActivity.getSharedPreferences("token", Context.MODE_PRIVATE)
                val accessToken:String = tokenSharedPreferences.getString("accessToken","").toString()
                loadedPageCnt=0
                totalPageCnt=0
                if(searchTag=="전체"){
                    val loadPostThread = Thread{
                        loadData.clear()
                        loadedPageCnt=0
                        totalPageCnt=0
                        val response = RetrofitClient.instance.loadPost(accessToken = "Bearer $accessToken", page = loadedPageCnt)
                            .execute()
                        if(response.code()==403){
                            //토큰 만료
                            loadedPageCnt++
                            isLoaded =true
//                    totalPageCnt = response.body()?.totalPages ?: -1
                        }
                        else if(response.isSuccessful){
                            response.body()?.let { loadData.addAll(response.body()!!.boardFindResponseDtoList) }
                            loadedPageCnt++
                            isLoaded =true
                            totalPageCnt = response.body()?.totalPages ?: -1
                        }
                        else{
                            Log.d("HomeFragment",response.toString())
                        }
                        runOnUiThread{
                            //binding.rcvSellList.adapter=homeAdapter(loadDat a)
                            searchResultAdapter.notifyDataSetChanged()
                        }
                    }
                    loadPostThread.join()
                    loadPostThread.start()
                    Log.d("HomeFragment_TAG", loadData.toString())
                }
                else{
                    val searchByTagThread = Thread{
                        val response = RetrofitClient.instance.loadSearchByTag("Bearer $accessToken",searchTagArray,loadedPageCnt).execute()
                        Log.d("HomeFragment_TAG_",response.code().toString())
                        if(response.code()==403){
                            //토큰 만료
                            loadedPageCnt++
                            isLoaded =true
//                    totalPageCnt = response.body()?.totalPages ?: -1
                        }
                        else if(response.body()!!.totalElements==0){
                            Log.d("SearchResultActivity_TAG_", response.body()!!.totalElements.toString())
                            Toast.makeText(this@SearchResultActivity, "검색결과가 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                        else if(response.isSuccessful){
                            loadData.clear()
                            response.body()?.let { loadData.addAll(response.body()!!.boardFindResponseDtoList) }
                            loadedPageCnt++
                            isLoaded =true
                            totalPageCnt = response.body()?.totalPages ?: -1
                            Log.d("HomeFragment_TAG_",response.toString())
                            Log.d("HomeFragment_TAG_",response.body().toString())
                            Log.d("HomeFragment_TAG_", loadData.toString())
                        }
                        else{
                            Log.d("HomeFragment_TAG",response.toString())
                        }
                        Log.d("HomeFragment_TAG", loadData.toString())
                        runOnUiThread{
                            //binding.rcvSellList.adapter=homeAdapter(loadDat a)
                            searchResultAdapter.notifyDataSetChanged()
                        }
                    }
                    searchByTagThread.join()
                    searchByTagThread.start()
                }
            }
        })



        val intentPost = Intent(this, PostDetailActivity::class.java)
        searchResultAdapter.setItemClickListener(object:SearchResultAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                //Toast.makeText(requireContext(),loadData[position].title,Toast.LENGTH_SHORT).show()
                //TODO 닉네임도 api에 추가되면 넣을 것
                intentPost.putExtra("nickname", loadData[position].nickname)
                intentPost.putExtra("major", loadData[position].major)
                intentPost.putExtra("productName", loadData[position].title)
                //intentPost.putExtra("img1", loadData[position].image[0].url)
                //시간 전송
                intentPost.putExtra("createAt", loadData[position].createdAt)
                //tag전송
                val tagArrayList = ArrayList<String>()
                loadData[position].tag.forEach{
                    tagArrayList.add(it.category)
                }

                val tagListtoJson = GsonBuilder().create().toJson(tagArrayList,itemType)

                Log.d("SearchResultActivity_TAG",tagListtoJson)

                intentPost.putExtra("tag",tagListtoJson)

                val imageUriList = ArrayList<String>()
                for(i:Int in 0 until loadData[position].image.size){
                    imageUriList.add(loadData[position].image[0].url)
                }
                intentPost.putStringArrayListExtra("imgArray", imageUriList)
                val priceAddComma = loadData[position].price.addCommas()//콤마 붙이는 코드
                intentPost.putExtra("productPrice", priceAddComma+"원")
                intentPost.putExtra("productContent", loadData[position].content)
                //태그도 인텐트로 넘겨 intent.putExtra("TAG")

                startActivity(intentPost)
            }

        })
    }

    private fun loadMoreData(accessToken: String, keyWord: String, page: Int): Int {

        //일단 무지성으로 동기통신
        val beforesize = loadData.size
        var aftersize = loadData.size
        val loadMoreDataThread = Thread {
            val response = RetrofitClient.instance.loadSearchPost(
                accessToken = "Bearer $accessToken",
                keyWord,
                page
            ).execute()
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

    fun loadSearchPost(accessToken:String){//초기 데이터 로딩용

        val loadSearchPostThread = Thread{
            val response = RetrofitClient.instance.loadSearchPost(
                accessToken = "Bearer $accessToken",
                keyword = searchWord,
                page = 0
            ).execute()

            Log.d(TAG+"after", response.toString())

            if(response.code()==403){

            }
            else if(response.body()?.totalElements==0){
                runOnUiThread {
                    Toast.makeText(this@SearchResultActivity, "검색결과가 없습니다.", Toast.LENGTH_SHORT).show()

                }
            }
            else if(response.isSuccessful){
                loadData.clear()
                response.body()
                    ?.let { loadData.addAll(response.body()!!.boardFindResponseDtoList) }
                loadedPageCnt++
                isLoaded = true
                totalPageCnt = response.body()?.totalPages ?: -1
                Log.d(TAG,totalPageCnt.toString())
            }
            else{
                Log.d(TAG, response.toString())
            }

        }
        loadSearchPostThread.start()
        loadSearchPostThread.join()

    }
}