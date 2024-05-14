package com.withsejong

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telecom.Call
import androidx.appcompat.app.AppCompatActivity
import com.withsejong.databinding.MainActivityBinding
import com.withsejong.home.HomeFragment
import com.withsejong.home.PostData
import com.withsejong.mypage.MypageMainFragment
import com.withsejong.post.PostActivity
import com.withsejong.post.PostFragment
import com.withsejong.retrofit.LoadPostResponse
import com.withsejong.retrofit.RetrofitClient
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding:MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)






        val homeFragment = HomeFragment()
        //val searchFragment = SearchFragment()
        val postFragment = PostFragment()
        val mypageMainFragment = MypageMainFragment()
        val chatFragment = ChatFragment()
        val postActivity = PostActivity()
        val intentPost = Intent(this,postActivity::class.java)

        supportFragmentManager.beginTransaction().add(R.id.fcv_all_fragment,homeFragment).commit()

        //bottomnavigation 각 메뉴별로 클릭했을 때 fragment가 바뀌는 기능
        binding.botnavMenu.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home->supportFragmentManager.beginTransaction().replace(R.id.fcv_all_fragment,homeFragment).commit()
                //R.id.search -> supportFragmentManager.beginTransaction().replace(R.id.fcv_all_fragment,searchFragment).commit()
                //R.id.post -> supportFragmentManager.beginTransaction().replace(R.id.fcv_all_fragment,postFragment).commit()
                R.id.post->startActivity(intentPost)

                R.id.chat -> supportFragmentManager.beginTransaction().replace(R.id.fcv_all_fragment,chatFragment).commit()
                R.id.mypage->supportFragmentManager.beginTransaction().replace(R.id.fcv_all_fragment,mypageMainFragment).commit()
            }
        true
        }
    }
}







