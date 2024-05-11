package com.withsejong

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.withsejong.databinding.MainActivityBinding
import com.withsejong.home.HomeFragment
import com.withsejong.home.PostData
import com.withsejong.mypage.MypageMainFragment
import com.withsejong.post.PostActivity
import com.withsejong.post.PostFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding:MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)




        //더미데이터 생성
        //TODO 더미데이터 테스트 코드이므로 추후에 통신을 통해 리스트에 저장하는 것 구현할 것!

        val mockData = ArrayList<PostData>()

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
            arrayListOf("#지기전","#소융대1"),
            7,
            10000)
        )

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







