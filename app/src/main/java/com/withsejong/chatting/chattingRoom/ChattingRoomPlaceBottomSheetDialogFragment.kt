package com.withsejong.chatting.chattingRoom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.withsejong.R
import com.withsejong.databinding.FragmentChattingRoomPlaceBottomSheetDialogBinding

class ChattingRoomPlaceBottomSheetDialogFragment(private val onPlaceSelected:(Place)->Unit): BottomSheetDialogFragment() {

    private var _binding : FragmentChattingRoomPlaceBottomSheetDialogBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChattingRoomPlaceBottomSheetDialogBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val placeList = arrayListOf(
            Place("군자관", "# 돌계단을 올라가야 해요\n# 우체국, 안경점, 문구점이 있어요\n# 근처에 집현관, 세종관이 있어요",R.drawable.gunza),
            Place("진관홀", "# 진관키친이 있어요\n# 후문에서 가까워요\n# 근처에 애지헌, 이당관이 있어요",R.drawable.zinkuan),
            Place("광개토관", "# 유동인구가 많아요\n# 가장 높은 건물이에요\n# 근처에 애지헌, 군자관이 있어요",R.drawable.gwanggaeto),
        )
        val placeAdapter = ChattingRoomPlaceAdapter(placeList)
        binding.rcvPlaceList.adapter = placeAdapter
        binding.rcvPlaceList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        placeAdapter.setItemClickListener(object: ChattingRoomPlaceAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                //Toast.makeText(requireActivity(), "${placeList[position].name}", Toast.LENGTH_SHORT).show()
                onPlaceSelected(placeList[position])
                dismiss()
            }

        })




    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}