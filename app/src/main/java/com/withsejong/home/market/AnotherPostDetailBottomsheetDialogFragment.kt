package com.withsejong.home.market

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.JsonParser
import com.withsejong.R
import com.withsejong.chatting.ChatFragment
import com.withsejong.chatting.chattingRoom.ChattingRoomActivity
import com.withsejong.databinding.FragmentAnotherPostDetailBottomsheetdialogBinding
import com.withsejong.home.HomeFragment
import com.withsejong.retrofit.RetrofitClient
import org.json.JSONObject
import retrofit2.Retrofit


class AnotherPostDetailBottomsheetDialogFragment : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = FragmentAnotherPostDetailBottomsheetdialogBinding.inflate(
            layoutInflater,
            container,
            false
        )
        val userReportDialogFragment = UserReportDialogFragment()

        val bottomSheetDialog = dialog as BottomSheetDialog?
        val bottomSheet =
            bottomSheetDialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.setBackgroundResource(R.drawable.design_another_post_detail_clo)
        val tokenSharedPreferences = requireContext().getSharedPreferences("token", MODE_PRIVATE)
        val userInfoSharedPreferences =
            requireContext().getSharedPreferences("userInfo", MODE_PRIVATE)
        val accessToken = tokenSharedPreferences.getString("accessToken", "")
        val studentId = userInfoSharedPreferences.getString("studentId", "")

        val postId = arguments?.getString("postId")
        val boardId = arguments?.getInt("boardId")
        val boardTitle = arguments?.getString("boardTitle")


        //TODO 각 버튼에 해당하는 동작 만들 것
        binding.cloSendChatting.setOnClickListener {

            val jsonObject = JSONObject()
            jsonObject.put("publisher", studentId)
            jsonObject.put("subscriber", postId)
            jsonObject.put("boardId", boardId)

            val makeChattingRoomThread = Thread {
                val response = RetrofitClient.instance.makeChattingRoom(
                    "Bearer $accessToken",
                    JsonParser.parseString(jsonObject.toString())
                ).execute()

                if (response.code() == 403) {

                } else if (response.isSuccessful) {
                    Log.d(
                        "AnotherPostDetailBottomsheetDialogFragment_TAG",
                        response.body().toString()
                    )
                    //TODO roomID에 해당하는 채팅방으로 이동
                    val intentChattingRoom =
                        Intent(requireContext(), ChattingRoomActivity::class.java)
                    intentChattingRoom.putExtra("roomId", response.body()?.id)
                    intentChattingRoom.putExtra("boardTitle", boardTitle)
                    intentChattingRoom.putExtra("publisher", studentId)
                    intentChattingRoom.putExtra("subscriber", postId)

                    startActivity(intentChattingRoom)
                    dismiss()
                }
            }

            makeChattingRoomThread.join()
            makeChattingRoomThread.start()

        }

        binding.cloReport.setOnClickListener {
            dismiss()
            val reportBundle = Bundle()
            reportBundle.putInt("boardId", boardId!!)
            userReportDialogFragment.arguments = reportBundle
            userReportDialogFragment.show(parentFragmentManager, userReportDialogFragment.tag)

        }



        return binding.root
    }

}