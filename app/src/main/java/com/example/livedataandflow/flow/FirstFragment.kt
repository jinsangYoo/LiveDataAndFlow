package com.example.livedataandflow.flow

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.livedataandflow.databinding.FragmentFirstBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private val TAG = javaClass.simpleName
    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val sharedFlow = MutableSharedFlow<Int>(replay = 1)
//    private val sharedFlow2 = MutableSharedFlow<Int>()
    private val sharedFlow2 = MutableSharedFlow<Int>(extraBufferCapacity = 3)
    private val sharedFlow3 = MutableSharedFlow<Int>(replay = 0)
    private val sharedFlow4 = MutableSharedFlow<Int>(extraBufferCapacity = 2)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false).apply {
            btnSharedflow1.setOnClickListener {
                lifecycleScope.launch {
                    for (i in 1..5) {
                        sharedFlow.emit(i)
                        Log.d(TAG, "sharedFlow emit $i")
                    }
                }
                lifecycleScope.launch {
                    sharedFlow.collect {
                        Log.d(TAG, "sharedFlow collect 1: $it")
                    }
                }

                lifecycleScope.launch {
                    sharedFlow.collect {
                        Log.d(TAG, "sharedFlow collect 2: $it")
                    }
                }
            }
            btnSharedflow2.setOnClickListener {
                lifecycleScope.launch {
                    for (i in 1..5) {
                        delay(200)
                        sharedFlow2.emit(i)
                        Log.d(TAG, "sharedFlow2 emit $i")
                    }
                }
                lifecycleScope.launch {
                    sharedFlow2.collect {
                        delay(500)
                        Log.d(TAG, "sharedFlow2 collect 1: $it")
                    }
                }
            }
            btnSharedflow3.setOnClickListener {
                lifecycleScope.launch {
                    for (i in 1..3) {
                        sharedFlow3.emit(i)
                        Log.d(TAG, "sharedFlow3 emit $i")
                        delay(300)
                    }
                }
                lifecycleScope.launch {
                    delay(1000)
                    sharedFlow3.collect {
                        Log.d(TAG, "sharedFlow3 collect: $it")
                    }
                }
            }
            btnSharedflow4.setOnClickListener {
                lifecycleScope.launch {
                    for (i in 1..5) {
                        val result = sharedFlow4.tryEmit(i)
                        if (result) {
                            Log.d(TAG, "sharedFlow4 성공: $i")
                        }
                        else {
                            Log.d(TAG, "sharedFlow4 실패: $i")
                        }
                        delay(200)
                    }
                }
                lifecycleScope.launch {
                    sharedFlow4.collect {
                        delay(1000)
                        Log.d(TAG, "sharedFlow4 collect: $it")
                    }
                }
            }
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}