package com.withsejong

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.withsejong.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val mapFragment = MapFragment()
        val chatFragment = ChatFragment()
        val mypageFragment = MypageFragment()
        //bottomnavigation 각 메뉴별로 클릭했을 때 fragment가 바뀌는 기능
        binding.botnavMenu.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home->supportFragmentManager.beginTransaction().replace(R.id.fcv_all_fragment,homeFragment).commit()
                R.id.map -> supportFragmentManager.beginTransaction().replace(R.id.fcv_all_fragment,mapFragment).commit()
                R.id.chat -> supportFragmentManager.beginTransaction().replace(R.id.fcv_all_fragment,chatFragment).commit()
                R.id.mypage->supportFragmentManager.beginTransaction().replace(R.id.fcv_all_fragment,mypageFragment).commit()
            }
        true
        }

    }
}







