package com.withsejong.home.market

import android.content.Context.MODE_PRIVATE
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.gson.JsonParser
import com.withsejong.R
import com.withsejong.databinding.FragmentAccountDeleteDialogBinding
import com.withsejong.databinding.FragmentUserReportDialogBinding
import com.withsejong.retrofit.RetrofitClient
import org.json.JSONObject


class UserReportDialogFragment : DialogFragment() {

    lateinit var binding : FragmentUserReportDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserReportDialogBinding.inflate(layoutInflater,container,false)
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

        val tokenSharedPreferences = requireActivity().getSharedPreferences("token", MODE_PRIVATE)

        val accessToken = tokenSharedPreferences.getString("accessToken", "")

        val boardId = arguments?.getInt("boardId")


        binding.btnReport.setOnClickListener {
            if(binding.etReportTitle.length()>0 && binding.etReportContent.length()>0){
                val reportTherad = Thread{
                    val jsonObject = JSONObject()
                    jsonObject.put("title", binding.etReportTitle.text.toString())
                    jsonObject.put("content", binding.etReportContent.text.toString())
                    jsonObject.put("boardId", boardId)

                    Log.d("UserReportDialogFragment_TAG",boardId.toString())
                    Log.d("UserReportDialogFragment_TAG",accessToken.toString())

                    Log.d("UserReportDialogFragment_TAG", jsonObject.toString())



                    val response = RetrofitClient.instance.postReport("Bearer ${accessToken}",JsonParser.parseString(jsonObject.toString())).execute()
                    Log.d("UserReportDialogFragment_TAG",response.toString())
                    if(response.code()==403){

                    }
                    else if(response.isSuccessful){
                        requireActivity().runOnUiThread {
                            Toast.makeText(requireContext(), "신고가 완료되었습니다!", Toast.LENGTH_SHORT).show()
                        }
                        dismiss()
                    }
                    else{

                    }
                }
                reportTherad.start()
                reportTherad.join()

            }
        }
    }

}