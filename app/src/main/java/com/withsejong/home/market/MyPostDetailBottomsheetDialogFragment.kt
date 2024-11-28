package com.withsejong.home.market

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.withsejong.R
import com.withsejong.databinding.FragmentMyPostDetailBottomsheetdialogBinding
import com.withsejong.retrofit.RetrofitClient


class MyPostDetailBottomsheetDialogFragment : BottomSheetDialogFragment() {
    private var listener: BottomSheetListener? = null

    interface BottomSheetListener {
        fun onBottomSheetResult(success: Boolean,position:Int)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val binding = FragmentMyPostDetailBottomsheetdialogBinding.inflate(layoutInflater,container,false)
        val tokenSharedPreferences = requireContext().getSharedPreferences("token", MODE_PRIVATE)
        listener = parentFragment as? BottomSheetListener
        Log.d("MyPostDetailBottomsheetDialogFragment_TAG", listener.toString())



        val userInfoSharedPreferences = requireContext().getSharedPreferences("userInfo", MODE_PRIVATE)
        val accessToken = tokenSharedPreferences.getString("accessToken","" )
        val boardId = arguments?.getString("boardId")?.toIntOrNull() ?: -1

        val position = arguments?.getString("position")?.toIntOrNull() ?: -1


        Log.d("MyPostDetailBottomsheetDialogFragment_TAG", boardId.toString())
        Log.d("MyPostDetailBottomsheetDialogFragment_TAG", position.toString())


        binding.cloEditPost.setOnClickListener {

        }

        binding.cloDelete.setOnClickListener {
            val deleteThread = Thread{
                val response = RetrofitClient.instance.deletePost("Bearer ${accessToken}",boardId).execute()
                if(response.code()==403){

                }
                else if(response.isSuccessful){
                    Log.d("MyPostDetailBottomsheetDialogFragment_TAG", response.body().toString())
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    listener?.onBottomSheetResult(true,position-1)

                    parentFragmentManager.setFragmentResult("isDeleted",
                        Bundle().apply { putBoolean("isDeleted", true) }



                    )
                    dismiss()
                }

            }
            deleteThread.join()
            deleteThread.start()
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetDialog = dialog as BottomSheetDialog?
        val bottomSheet = bottomSheetDialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.setBackgroundResource(R.drawable.design_another_post_detail_clo)
        //TODO 각 버튼에 해당하는 동작 만들 것
    }

}