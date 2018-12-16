package com.yueban.androidkotlindemo.navigation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.transition.TransitionInflater
import com.yueban.androidkotlindemo.R
import kotlinx.android.synthetic.main.fragment_navigation_2.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NavigationFragment2.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NavigationFragment2.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class NavigationFragment2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        Log.d("element", sharedElementEnterTransition.toString())
//        Log.d("element", sharedElementReturnTransition.toString())
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.transition_common).apply {
                duration = 400
            }
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.transition_common).apply {
                duration = 400
            }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation_2, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction1(uri)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        goto_next_3.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigationFragment2_to_navigationFragment32))
        goto_next_4.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigationFragment22_to_navigationFragment43))

        /********** demo 6: pass params between destinations **********/
        arguments?.let {
            val userId = NavigationFragment2Args.fromBundle(it).userId
            tv_userId.text = userId
        }

        /********** demo: global action **********/
//        goto_next_3.setOnClickListener {
//            findNavController().navigate(R.id.action_global_navigationFragment1)
//        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction1(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NavigationFragment2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NavigationFragment2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
