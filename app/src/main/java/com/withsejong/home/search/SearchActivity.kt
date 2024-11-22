package com.withsejong.home.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.withsejong.MainActivity
import com.withsejong.databinding.ActivitySearchBinding
import com.withsejong.home.HomeFragment
import com.withsejong.home.addCommas

class SearchActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //검색어 저장 관련 sharedpreference

        val searchHistorySharedPreferences = getSharedPreferences("searchHistory", MODE_PRIVATE)
        var searchJson = searchHistorySharedPreferences.getString("searchWord", ArrayList<String>().toString())



        //검색어 rcv
        Log.d("SearchActivity_TAG",searchJson.toString())

        val itemType = object : TypeToken<ArrayList<String>>() {}.type
        val searchJsontoList = GsonBuilder().create().fromJson<ArrayList<String>>(searchJson,itemType)

        val searchAdpater = SearchRecentWordAdapter(searchJsontoList)
        binding.rcvRecentSearch.adapter = searchAdpater
        binding.rcvRecentSearch.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)


        val intentBack = Intent(this, MainActivity::class.java)
        val intentSearch = Intent(this,SearchResultActivity::class.java)

        binding.ibtnSearch.setOnClickListener {
            intentSearch.putExtra("searchWord", binding.etSearch.text.toString())

            Log.d("SearchActivity",binding.etSearch.text.toString())
            startActivity(intentSearch)
            finish()


        }
        binding.etSearch.setOnEditorActionListener { v, actionId, event ->
            if(actionId== EditorInfo.IME_ACTION_DONE){

                // 키보드 숨기기
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                intentSearch.putExtra("searchWord", binding.etSearch.text.toString())
                Log.d("SearchActivity",binding.etSearch.text.toString())


                intentSearch.putExtra("searchWord", binding.etSearch.text.toString())


                startActivity(intentSearch)
                finish()

                true
            }
            else{
                false
            }
        }

        searchAdpater.setItemClickListener(object: SearchRecentWordAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val searchWordfromList = searchJsontoList[position]

                intentSearch.putExtra("searchWord", searchWordfromList)
                Log.d("SearchActivity",searchWordfromList)
                startActivity(intentSearch)
                finish()
            }

            //            override fun onClick(v: View, position: Int) {
//                //Toast.makeText(requireContext(),loadData[position].title,Toast.LENGTH_SHORT).show()
//                //TODO 닉네임도 api에 추가되면 넣을 것
//                intentPost.putExtra("nickname", HomeFragment.loadData[position].nickname)
//                intentPost.putExtra("major", HomeFragment.loadData[position].major)
//                intentPost.putExtra("productName", HomeFragment.loadData[position].title)
//                //intentPost.putExtra("img1", loadData[position].image[0].url)
//                val imageUriList = ArrayList<String>()
//                for(i:Int in 0 until HomeFragment.loadData[position].image.size){
//                    imageUriList.add(HomeFragment.loadData[position].image[0].url)
//                }
//
//
//                intentPost.putStringArrayListExtra("imgArray", imageUriList)
//
//
//                val priceAddComma = HomeFragment.loadData[position].price.addCommas()//콤마 붙이는 코드
//                intentPost.putExtra("productPrice", priceAddComma+"원")
//                intentPost.putExtra("productContent", HomeFragment.loadData[position].content)
//                //TODO 태그 배열을 보내야하는데 흠...
//                //intentPost.putStringArrayListExtra("tag", loadData[position].tag)
//
//                val tagArrayList = ArrayList<String>()
//                HomeFragment.loadData[position].tag.forEach{
//                    tagArrayList.add(it.category)
//                }
//
//                val tagListtoJson = GsonBuilder().create().toJson(tagArrayList,itemType)
//
//                Log.d("HomeFragment_TAG",tagListtoJson)
//
//                intentPost.putExtra("tag",tagListtoJson)
//                startActivity(intentPost)
//            }



        })


        //X버튼 누를경우 검색어 다 지우는 코드
        binding.ibtnClear.setOnClickListener {
            binding.etSearch.setText("")
        }
        binding.ibtnBack.setOnClickListener {
            startActivity(intentBack)
            finish()
        }
    }

//    fun saveSearchHistory(searchWord : String){
//        val searchHistorySharedPreferences = getSharedPreferences("searchHistory", MODE_PRIVATE)
//        val editor = searchHistorySharedPreferences.edit()
//
//    }
}