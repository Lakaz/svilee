package com.mobiwarez.laki.sville.ui.toys.create


import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mobiwarez.laki.sville.R
import org.greenrobot.eventbus.EventBus


/**
 * A simple [Fragment] subclass.
 */
class AgeGroupDialogFragment : DialogFragment() {

    private var newToyViewModel: NewToyViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        newToyViewModel = ViewModelProviders.of(activity).get(NewToyViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Select Item Age Group")
                .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                .setItems(R.array.age_group_array) { dialog, which ->
                    val category = (dialog as AlertDialog).listView.adapter.getItem(which).toString()
                    newToyViewModel!!.ageGroup = category
                    EventBus.getDefault().post(DoneSettingAgeMessage())
                }
        return builder.create()

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_blank, container, false)
    }

    companion object {

        val TAG = AgeGroupDialogFragment::class.java.simpleName

        fun newInstance(): AgeGroupDialogFragment {
            return AgeGroupDialogFragment()
        }
    }


}// Required empty public constructor
