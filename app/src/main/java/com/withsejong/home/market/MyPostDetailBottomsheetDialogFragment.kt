package com.withsejong.home.market

import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.material3.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.GsonBuilder
import com.withsejong.R
import com.withsejong.databinding.FragmentMyPostDetailBottomsheetdialogBinding
import com.withsejong.retrofit.PostPullUpResponse
import com.withsejong.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyPostDetailBottomsheetDialogFragment : BottomSheetDialogFragment() {
    private var listener: BottomSheetListener? = null

    interface BottomSheetListener {
        fun onBottomSheetResult(success: Boolean, position: Int)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            FragmentMyPostDetailBottomsheetdialogBinding.inflate(layoutInflater, container, false)
        val tokenSharedPreferences = requireContext().getSharedPreferences("token", MODE_PRIVATE)
        listener = parentFragment as? BottomSheetListener
        Log.d("MyPostDetailBottomsheetDialogFragment_TAG", listener.toString())


        val userInfoSharedPreferences =
            requireContext().getSharedPreferences("userInfo", MODE_PRIVATE)
        val accessToken = tokenSharedPreferences.getString("accessToken", "")
        val studentId = userInfoSharedPreferences.getString("studentId","")
        val boardId = arguments?.getString("boardId")?.toIntOrNull() ?: -1

        val position = arguments?.getString("position")?.toIntOrNull() ?: -1


        Log.d("MyPostDetailBottomsheetDialogFragment_TAG", boardId.toString())
        Log.d("MyPostDetailBottomsheetDialogFragment_TAG", position.toString())


        binding.cloEditPost.setOnClickListener {
            AlertDialog.Builder(requireActivity())
                .setTitle("알림")
                .setMessage("게시글 수정은 추후 지원 예정입니다. 게시글 수정 대신 글을 삭제했다가 다시 업로드 해주시면 감사하겠습니다.")
                .setPositiveButton("확인", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        dismiss()
                    }

                })
                .show()
        }

        binding.cloPullUpPost.setOnClickListener{
            RetrofitClient.instance.pullUpPost("Bearer ${accessToken}", boardId,studentId.toString()).enqueue(object:Callback<PostPullUpResponse>{
                override fun onResponse(call: Call<PostPullUpResponse>, response: Response<PostPullUpResponse>) {

                    Toast.makeText(requireActivity(), "게시글 올리기 성공", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.setFragmentResult("myPostFunc",
                        Bundle().apply { putBoolean("isPullUp", true)

                        putString("createdAt", response.body()?.createdAt)
                        }


                    )
                    dismiss()


                }

                override fun onFailure(call: Call<PostPullUpResponse>, t: Throwable) {
                    Toast.makeText(requireActivity(), "게시글 올리기 실패", Toast.LENGTH_SHORT).show()
                }

            })


        }

        binding.cloDelete.setOnClickListener {

            AlertDialog.Builder(requireActivity())
                .setTitle("게시글 삭제")
                .setMessage("게시글을 삭제하시겠습니까?")
                .setPositiveButton("제거", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val deleteThread = Thread {
                            val response =
                                RetrofitClient.instance.deletePost("Bearer ${accessToken}", boardId)
                                    .execute()
                            if (response.code() == 403) {

                            } else if (response.isSuccessful) {
                                Log.d(
                                    "MyPostDetailBottomsheetDialogFragment_TAG",
                                    response.body().toString()
                                )
                                requireActivity().runOnUiThread {
                                    Toast.makeText(
                                        requireContext(),
                                        "게시글이 삭제되었습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                listener?.onBottomSheetResult(true, position - 1)

                                parentFragmentManager.setFragmentResult("myPostFunc",
                                    Bundle().apply { putBoolean("isDeleted", true) }


                                )
                                dismiss()
                            }

                        }
                        deleteThread.join()
                        deleteThread.start()

                    }

                })
                .setNegativeButton(
                    "취소"
                ) { dialog, which ->

                    dialog.dismiss()
                }.show()

        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetDialog = dialog as BottomSheetDialog?
        val bottomSheet =
            bottomSheetDialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.setBackgroundResource(R.drawable.design_another_post_detail_clo)
        //TODO 각 버튼에 해당하는 동작 만들 것
    }

}