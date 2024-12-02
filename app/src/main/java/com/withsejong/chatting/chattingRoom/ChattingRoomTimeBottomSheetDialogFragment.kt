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
import com.withsejong.databinding.FragmentChattingRoomTimeBottomSheetDialogBinding
import java.util.Calendar

class ChattingRoomTimeBottomSheetDialogFragment(private val onDateTimeSelected:(String,String)->Unit): BottomSheetDialogFragment() {

    private var _binding : FragmentChattingRoomTimeBottomSheetDialogBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChattingRoomTimeBottomSheetDialogBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // 현재 날짜와 시간 가져오기
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH는 0부터 시작하므로 +1
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        // NumberPicker 설정
        binding.npYear.minValue = currentYear
        binding.npYear.maxValue = currentYear + 1 // 최대 1년 뒤까지 선택 가능
        binding.npYear.value = currentYear // 현재 연도 설정

        binding.npMonth.minValue = 1
        binding.npMonth.maxValue = 12
        binding.npMonth.value = currentMonth // 현재 월 설정

        binding.npDay.minValue = 1
        binding.npDay.maxValue = getMaxDaysInMonth(currentYear, currentMonth) // 현재 월의 최대 일수 반영
        binding.npDay.value = currentDay // 현재 일 설정

        binding.npHour.minValue = 0
        binding.npHour.maxValue = 23
        binding.npHour.value = currentHour // 현재 시간 설정

        binding.npMinute.minValue = 0
        binding.npMinute.maxValue = 59
        binding.npMinute.value = currentMinute // 현재 분 설정

        // 월이 변경될 때 최대 일수 업데이트
        binding.npMonth.setOnValueChangedListener { _, _, newMonth ->
            val maxDay = getMaxDaysInMonth(binding.npYear.value, newMonth)
            binding.npDay.maxValue = maxDay
        }

        // 연도가 변경될 때 (윤년 반영)
        binding.npYear.setOnValueChangedListener { _, _, newYear ->
            val maxDay = getMaxDaysInMonth(newYear, binding.npMonth.value)
            binding.npDay.maxValue = maxDay
        }

        // 전송 버튼 클릭 리스너
        binding.btnSend.setOnClickListener {
            val selectedDate = "${binding.npYear.value}년 ${binding.npMonth.value}월 ${binding.npDay.value}일"
            val selectedTime = "${binding.npHour.value}시 ${binding.npMinute.value}분"

            // 선택된 값을 콜백으로 전달
            onDateTimeSelected(selectedDate, selectedTime)
            dismiss()
        }


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    // 특정 연도와 월의 최대 일수 계산 (윤년 반영)
    private fun getMaxDaysInMonth(year: Int, month: Int): Int {
        return when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31 // 31일까지 있는 월
            4, 6, 9, 11 -> 30 // 30일까지 있는 월
            2 -> if (isLeapYear(year)) 29 else 28 // 2월 (윤년 계산)
            else -> 30 // 기본값 (예외 처리)
        }
    }

    // 윤년 계산 함수
    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

}