package com.yueban.androidkotlindemo.navigation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.yueban.androidkotlindemo.R
import kotlinx.android.synthetic.main.fragment_navigation_1.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NavigationFragment1.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NavigationFragment1.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class NavigationFragment1 : Fragment() {
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation_1, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction2(uri)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /********** demo 3: navigate to a destination by an action **********/
//        goto_next.setOnClickListener {
//            goto_next.findNavController().navigate(R.id.action_navigationFragment1_to_navigationFragment2)
//        }

        /********** demo 3.1 **********/
//        goto_next.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigationFragment1_to_navigationFragment2))

        /********** demo 4: navigate to a nested graph **********/
        goto_nested_graph.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigationFragment1_to_nested_navigation))

        /********** demo 4.1: navigate to a include graph **********/
        goto_include_graph.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigationFragment1_to_nav_include_fragments))

        /********** demo 5: shared element transition **********/
        val extras = FragmentNavigatorExtras(
            message to message.transitionName,
            goto_next to goto_next.transitionName
        )
        goto_next.setOnClickListener {
            it.findNavController().navigate(R.id.action_navigationFragment1_to_navigationFragment2, null, null, extras)
        }

        /********** demo 6: pass params between destinations **********/
//        goto_next.setOnClickListener {
//            val action = NavigationFragment1Directions
//                .actionNavigationFragment1ToNavigationFragment2()
////                .setUserId("another id")
//            it.findNavController().navigate(action)
//
////            it.findNavController().navigate(
////                R.id.action_navigationFragment1_to_navigationFragment2,
////                bundleOf("userId" to "another id")
////            )
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
        fun onFragmentInteraction2(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NavigationFragment1.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NavigationFragment1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
