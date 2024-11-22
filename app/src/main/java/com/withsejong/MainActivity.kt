package com.withsejong

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.withsejong.chatting.ChatFragment
import com.withsejong.databinding.MainActivityBinding
import com.withsejong.home.HomeFragment
import com.withsejong.mypage.MypageMainFragment
import com.withsejong.post.PostActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding:MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        //val searchFragment = SearchFragment()
        val mypageMainFragment = MypageMainFragment()
        val chatFragment = ChatFragment()
        val postActivity = PostActivity()
        val intentPost = Intent(this,postActivity::class.java)

        var fragmentManager = supportFragmentManager.beginTransaction()
        fragmentManager.add(R.id.fcv_all_fragment,homeFragment).commit()


        //bottomnavigation 각 메뉴별로 클릭했을 때 fragment가 바뀌는 기능
        binding.botnavMenu.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home->{
                    fragmentManager = supportFragmentManager.beginTransaction()
                    fragmentManager.replace(R.id.fcv_all_fragment,homeFragment).commit()


                }
                //R.id.search -> supportFragmentManager.beginTransaction().replace(R.id.fcv_all_fragment,searchFragment).commit()
                //R.id.post -> supportFragmentManager.beginTransaction().replace(R.id.fcv_all_fragment,postFragment).commit()
                R.id.post->startActivity(intentPost)
                R.id.chat -> {
                    fragmentManager = supportFragmentManager.beginTransaction()
                    fragmentManager.replace(R.id.fcv_all_fragment,chatFragment).commit()

                }

                R.id.mypage->{
                    fragmentManager = supportFragmentManager.beginTransaction()
                    fragmentManager.replace(R.id.fcv_all_fragment,mypageMainFragment).commit()

                }
            }
        true
        }
    }
}







