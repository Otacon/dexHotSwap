package org.cyanotic.fragmentfactory

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.TextView
import org.cyanotic.common.FragmentFactory

class FragmentFactoryDefault : FragmentFactory {

    override fun getFragment(position: Int): Fragment {
        return PlaceholderFragment.newInstance(position)
    }

    override fun getFragmentCount(): Int {
        return 3
    }
}

class PlaceholderFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val textView = TextView(container?.context)
        textView.layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER)
        textView.text = "Goodbye world from fragment ${arguments?.getInt(ARG_SECTION_NUMBER)}"

        val rootView = FrameLayout(container?.context)
        rootView.addView(textView)

        return rootView
    }

    companion object {

        private val ARG_SECTION_NUMBER = "section_number"

        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            val fragment = PlaceholderFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }
}