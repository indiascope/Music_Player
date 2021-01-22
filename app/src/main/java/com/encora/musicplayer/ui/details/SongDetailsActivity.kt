package com.encora.musicplayer.ui.details

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import coil.load
import com.encora.musicplayer.R
import com.encora.musicplayer.databinding.ActivitySongDetailsBinding
import com.encora.musicplayer.ui.base.BaseActivity
import com.encora.musicplayer.utils.hide
import com.encora.musicplayer.utils.show
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SongDetailsActivity : BaseActivity<SongDetailsViewModel, ActivitySongDetailsBinding>() {

    @Inject
    lateinit var viewModelFactory: SongDetailsViewModel.SongDetailsViewModelFactory

    // ExoPlayer
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var mediaSource: MediaSource
    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(this, getString(R.string.app_name))
    }

    var isAudioPlaying = false
    private var playbackPosition: Long = 0


    override val mViewModel: SongDetailsViewModel by viewModels {
        val songId = intent.extras?.getInt(KEY_SONG_ID)
            ?: throw IllegalArgumentException("`songId` must be non-null")

        SongDetailsViewModel.provideFactory(viewModelFactory, songId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)

        setSupportActionBar(mViewBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setUpWebView()
    }

    private fun setUpWebView() {
        mViewBinding.songContent.songBody.apply {
            settings.javaScriptEnabled=true
            setUpWebViewClient()
            settings.defaultTextEncodingName = "utf-8"
        }
    }

    private fun WebView.setUpWebViewClient() {
        val webViewLoadingClient: WebViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(request?.url.toString())
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                mViewBinding.progressBar.show()
                super.onPageStarted(view, url, favicon)

            }


            override fun onPageFinished(view: WebView, url: String) {
                mViewBinding.progressBar.hide()
                super.onPageFinished(view, url)

            }

            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {

            }
        }
        webViewClient = webViewLoadingClient
    }

    override fun onStart() {
        super.onStart()

        initSong()
    }

    private fun initSong() {
        mViewModel.song.observe(this) { song ->
            mViewBinding.songContent.apply {
                songTitle.text = song.title?:"N/A"
                songAuthor.text = song.artistName?:"N/A"
                song.content?.let {
                    mViewBinding.songContent.songBody.loadData(it, "text/html", "UTF-8")
                }
                song.audioLink?.let { link->
                    initializeExoPlayer(link)
                }?:mViewBinding.fab.hide()
            }
            mViewBinding.imageView.load(song.imageUrl)
        }
    }


    private fun initializeExoPlayer(audioLink: String) {
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this)
        mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(audioLink))
        simpleExoPlayer.prepare(mediaSource)
        with(simpleExoPlayer) {
            mViewBinding.fab.setOnClickListener {
                if (isAudioPlaying) {
                    pauseMedia()
                    playWhenReady = false
                } else {
                    playWhenReady = true
                    simpleExoPlayer.seekTo(playbackPosition)
                    playMedia()
                }
            }
        }
    }

    override fun getViewBinding(): ActivitySongDetailsBinding =
        ActivitySongDetailsBinding.inflate(layoutInflater)



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }

        }

        return super.onOptionsItemSelected(item)
    }


    private fun playMedia() {
        isAudioPlaying = true
        mViewBinding.fab.setImageResource(R.drawable.ic_baseline_stop_24)
    }

    private fun pauseMedia() {
       isAudioPlaying = false
        mViewBinding.fab.setImageResource(R.drawable.ic_baseline_play_arrow_24)
    }


    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            simpleExoPlayer.playWhenReady = false
            releasePlayer()
        }
    }

    override fun onDestroy() {
        simpleExoPlayer.playWhenReady = false
        releasePlayer()
        super.onDestroy()

    }

    private fun releasePlayer() {
        playbackPosition = simpleExoPlayer.currentPosition
        simpleExoPlayer.stop()
        simpleExoPlayer.release()
    }

    companion object {
        private const val KEY_SONG_ID = "songId"

        fun getStartIntent(
            context: Context,
            songId: Int
        ) = Intent(context, SongDetailsActivity::class.java).apply { putExtra(KEY_SONG_ID, songId) }
    }
}
