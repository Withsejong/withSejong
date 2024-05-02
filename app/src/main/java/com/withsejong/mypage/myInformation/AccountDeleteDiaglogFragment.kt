package com.withsejong.mypage.myInformation

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.withsejong.R
import com.withsejong.databinding.FragmentAccountDeleteDiaglogBinding


class AccountDeleteDiaglogFragment : DialogFragment() {

    lateinit var binding : FragmentAccountDeleteDiaglogBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment

        binding = FragmentAccountDeleteDiaglogBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    //TODO fragment연속으로 못 만들게 막는 코드 추가할 것

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val displayMetrics = Resources.getSystem().displayMetrics
        val width = displayMetrics.widthPixels*.8
        val height = displayMetrics.heightPixels*0.43
        val window = dialog?.window
        window?.setLayout(width.toInt(),height.toInt())
        dialog?.window?.setBackgroundDrawableResource(R.drawable.design_delete_account_dialog)//다이얼로그 모양 둥글게 설정


        binding.btnNoLeave.setOnClickListener {
            dismiss()
        }
        binding.btnLeave.setOnClickListener {
            //TODO 계정탈퇴 api연결 후 메인 페이지로 이동
            //TODO request launcher써야 할 수도
        }




    }

}