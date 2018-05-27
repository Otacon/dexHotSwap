package org.cyanotic.common

import android.support.v4.app.Fragment

interface FragmentFactory {

    fun getFragment(position: Int) : Fragment

    fun getFragmentCount(): Int

}