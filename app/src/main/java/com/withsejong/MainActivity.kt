package com.withsejong

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.AlertDialog
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

//        androidx.appcompat.app.AlertDialog.Builder(this)
//            .setTitle("종료")
//            .setMessage("세종끼리를 종료하시겠습니까?")
//            .setPositiveButton("네",object:DialogInterface.OnClickListener{
//                override fun onClick(dialog: DialogInterface?, which: Int) {
//                    finish()
//                }
//
//            })
//            .setNegativeButton("아니오",object : DialogInterface.OnClickListener {
//                override fun onClick(dialog: DialogInterface?, which: Int) {
//                    dialog?.dismiss()
//                }
//
//            }
//
//
//            )
//            .show()

        //뒤로 가기 2번 눌러서 앱 종료 로직 추가

        var backPressedTime = 0L
        val onBackPressed = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(System.currentTimeMillis() - backPressedTime<=2000){
                    finish() //앱 종료
                }
                else{
                    backPressedTime=System.currentTimeMillis()
                    Toast.makeText(this@MainActivity, "한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show()
                }
            }

        }
        onBackPressedDispatcher.addCallback(this@MainActivity,onBackPressed)


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







