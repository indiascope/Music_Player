package com.encora.musicplayer.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityOptionsCompat
import com.encora.musicplayer.R
import com.encora.musicplayer.data.local.entity.SongEntity
import com.encora.musicplayer.databinding.ActivityMainBinding
import com.encora.musicplayer.domain.State
import com.encora.musicplayer.ui.base.BaseActivity
import com.encora.musicplayer.ui.details.SongDetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import com.encora.musicplayer.ui.main.adapter.SongListAdapter
import com.encora.musicplayer.utils.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val mViewModel: MainViewModel by viewModels()

    private val mAdapter = SongListAdapter(this::onItemClicked)

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme) // Set AppTheme before setting content view.

        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)

        initView()
    }

    override fun onStart() {
        super.onStart()
        observeSongs()
        if (mViewModel.songsLiveData.value is State.Error || mAdapter.itemCount == 0) {
            getSongs(false)
        }
        handleNetworkChanges()
    }

    fun initView() {
        mViewBinding.run {
            songsRecyclerView.adapter = mAdapter
            songsRecyclerView.setEmptyView(emptyView)

            swipeRefreshLayout.setOnRefreshListener { getSongs(true) }
        }

        // If Current State isn't `Success` then reload posts.
        mViewModel.songsLiveData.value?.let { currentState ->
            if (!currentState.isSuccessful()) {
                getSongs(false)
            }
        }
    }

    private fun observeSongs() {
        mViewModel.songsLiveData.observe(this) { state ->
            when (state) {
                is State.Loading -> showLoading(true)
                is State.Success -> {
                    if (state.data.isNotEmpty()) {
                        mAdapter.submitList(state.data.toMutableList())
                    }else{
                        mAdapter.notifyDataSetChanged()
                    }
                    showLoading(false)
                }
                is State.Error -> {
                    showToast(state.message)
                    mAdapter.notifyDataSetChanged()
                    showLoading(false)
                }
            }
        }
    }

    private fun getSongs(fetchLatestSong:Boolean) = mViewModel.getSongs(fetchLatestSong)

    private fun showLoading(isLoading: Boolean) {
        mViewBinding.swipeRefreshLayout.isRefreshing = isLoading
    }

    /**
     * Observe network changes i.e. Internet Connectivity
     */
    private fun handleNetworkChanges() {
        NetworkUtils.getNetworkLiveData(applicationContext).observe(this) { isConnected ->
            if (!isConnected) {
                mViewBinding.textViewNetworkStatus.text =
                    getString(R.string.text_no_connectivity)
                mViewBinding.networkStatusLayout.apply {
                    show()
                    setBackgroundColor(getColorRes(R.color.colorStatusNotConnected))
                }
            } else {
                if (mViewModel.songsLiveData.value is State.Error || mAdapter.itemCount == 0) {
                    getSongs(false)
                }
                mViewBinding.textViewNetworkStatus.text = getString(R.string.text_connectivity)
                mViewBinding.networkStatusLayout.apply {
                    setBackgroundColor(getColorRes(R.color.colorStatusConnected))

                    animate()
                        .alpha(1f)
                        .setStartDelay(ANIMATION_DURATION)
                        .setDuration(ANIMATION_DURATION)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                hide()
                            }
                        })
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_theme -> {
                // Get new mode.
                val mode =
                    if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                        Configuration.UI_MODE_NIGHT_NO
                    ) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                    }

                // Change UI Mode
                AppCompatDelegate.setDefaultNightMode(mode)
                true
            }

            else -> true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    private fun onItemClicked(songEntity: SongEntity, imageView: ImageView) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            imageView,
            imageView.transitionName
        )
        val songId = songEntity.id ?: run {
            showToast("Unable to launch details")
            return
        }
        val intent = SongDetailsActivity.getStartIntent(this, songId)
        startActivity(intent, options.toBundle())
    }

    companion object {
        const val ANIMATION_DURATION = 1000L
    }
}
