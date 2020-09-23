package com.wawra.stations.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wawra.stations.R
import com.wawra.stations.base.BaseDialog
import com.wawra.stations.presentation.MainActivity
import kotlinx.android.synthetic.main.dialog_data_out_of_date.*

class DataOutOfDateDialogFragment : BaseDialog() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_data_out_of_date, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog_data_out_of_date_cancel_button.setOnClickListener { super.dismiss() }
        dialog_data_out_of_date_retry_button.setOnClickListener {
            super.dismiss()
            (activity as? MainActivity)?.updateDataIfNeeded()
        }
    }
}