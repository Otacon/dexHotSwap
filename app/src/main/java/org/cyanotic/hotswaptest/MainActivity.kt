package org.cyanotic.hotswaptest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import dalvik.system.DexClassLoader
import kotlinx.android.synthetic.main.activity_main.*
import org.cyanotic.common.FragmentFactory
import java.io.File


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        container.offscreenPageLimit = 1
        val dexPath = intent.getStringExtra("DEX_LOCATION")
        if (intent != null && dexPath != null) {
            System.gc()
            val fragmentFactory = loadFragmentFactory(File(dexPath))
            container.adapter = SectionsPagerAdapter(supportFragmentManager, fragmentFactory)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val dexPath = data?.data.toString()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("DEX_LOCATION", dexPath)
            finish()
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_reload) {
            container.adapter = null
            System.gc()
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "file/*"
            startActivityForResult(intent, 101)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun loadFragmentFactory(dex: File): FragmentFactory {
        Log.i("MainActivity", "Loading dex from $dex")
        return ModuleLoader(codeCacheDir).load(dex)!!
    }
}


class SectionsPagerAdapter(
        fm: FragmentManager,
        val fragmentFactory: FragmentFactory
) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return fragmentFactory.getFragment(position)
    }

    override fun getCount(): Int {
        return fragmentFactory.getFragmentCount()
    }
}

class ModuleLoader(
        private val cacheDir: File
) {

    fun load(dex: File): FragmentFactory? {
        val cls = "org.cyanotic.fragmentfactory.FragmentFactoryDefault"
        val classLoader = DexClassLoader(dex.absolutePath, cacheDir.absolutePath, null, this.javaClass.classLoader)

        val moduleClass = classLoader.loadClass(cls)
        return moduleClass.newInstance() as FragmentFactory
    }
}
