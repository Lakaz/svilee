package com.mobiwarez.laki.sville.ui.starter


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.analytics.FirebaseAnalytics
import com.mobiwarez.data.account.db.model.AccountModel

import com.mobiwarez.laki.sville.R
import com.mobiwarez.laki.sville.base.BaseFragment
import com.mobiwarez.laki.sville.base.ScopedPresenter
import com.mobiwarez.laki.sville.dagger.fragment.FragmentComponent
import com.mobiwarez.laki.sville.data.accountManager.AccountsManager
import com.mobiwarez.laki.sville.fragments.RxBaseFragment
import com.mobiwarez.laki.sville.job.UpdateCreditsJob
import com.mobiwarez.laki.sville.ui.toys.create.ItemCategoryDialogFragment
import com.mobiwarez.laki.sville.util.showToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
//@Inject
//DashContract.Presenter presenter;

class DashboardFragment : RxBaseFragment(), DashContract.View {

    private var receieveButton: Button? = null
    private var giveButton: Button? = null

    private var mListener: Navigator? = null

    lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = FirebaseAnalytics.getInstance(activity)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater!!.inflate(R.layout.fragment_dashboard, container, false)
        receieveButton = view.findViewById(R.id.receive_button)
        giveButton = view.findViewById(R.id.give_button)

        receieveButton!!.setOnClickListener {
            v ->
                firebaseAnalytics.logEvent("receive_pressed", null)
                mListener!!.startReceive()
        }

        giveButton!!.setOnClickListener {
            v ->
                firebaseAnalytics.logEvent("give_pressed", null)
                mListener!!.startGive()
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        fetchAccount()
    }

    interface Navigator {
        fun startGive()

        fun startReceive()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DashboardFragment.Navigator) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


    private fun createAccount(){
        val manager = AccountsManager(context)
        val account = AccountModel()
        account.credits = "10"
        account.rewardPoints = "0"

        val subscription = manager.addNewAccount(account)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {

                        },
                        {

                        }
                )

        subscriptions.add(subscription)
    }

    private fun fetchAccount(){

        val manager = AccountsManager(context)
        val subscription = manager.getAccounts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            accounts -> if (accounts.isEmpty()){
                            activity.showToast("no accounts")
                            createAccount()
                            UpdateCreditsJob.buildUpdateCreditsJob()
                        }else {
                            //showAccount(accounts[0])
                        }
                        },
                        {
                            error ->
                            Timber.d(error)
                        }
                )

        subscriptions.add(subscription)

    }


    companion object {


        val TAG = DashboardFragment::class.java.simpleName

        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }


    /*
    @Override
    protected void inject(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public ScopedPresenter getPresenter() {
        return presenter;
    }
*/
}// Required empty public constructor
