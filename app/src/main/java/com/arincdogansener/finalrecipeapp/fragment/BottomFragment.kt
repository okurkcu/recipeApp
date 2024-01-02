package com.arincdogansener.finalrecipeapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arincdogansener.finalrecipeapp.databinding.FragmentBottomBinding

class BottomFragment : Fragment() {
    private lateinit var binding: FragmentBottomBinding

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBottomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvResultBottomFragment.text="Bottom Fragment"
    }
}