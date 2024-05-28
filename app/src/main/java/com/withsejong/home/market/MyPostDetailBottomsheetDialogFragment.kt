package com.withsejong.home.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.withsejong.R
import com.withsejong.databinding.FragmentMyPostDetailBottomsheetdialogBinding


class MyPostDetailBottomsheetDialogFragment : BottomSheetDialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val binding = FragmentMyPostDetailBottomsheetdialogBinding.inflate(layoutInflater,container,false)
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