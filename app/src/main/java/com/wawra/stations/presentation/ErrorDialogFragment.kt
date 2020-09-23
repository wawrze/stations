package com.wawra.stations.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.wawra.stations.R
import com.wawra.stations.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_error.*

class ErrorDialogFragment : BaseDialog() {

    private val args by navArgs<ErrorDialogFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_error, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog_error_message.text = args.message
        dialog_error_button.setOnClickListener { super.dismiss() }
    }
}