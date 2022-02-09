package com.notificationsample

import android.app.Application
import androidx.lifecycle.AndroidViewModel

/** The logic for the main fragment */
class MainViewModel(app: Application) : AndroidViewModel(app) {

    /** Internal duration of the first iteration of the downloading animation */
    private var _originalDuration: Int =
        app.resources.getInteger(R.integer.quick_animation_duration)

    /** Duration of the first iteration of the downloading animation */
    val originalDuration: Int
        get() = _originalDuration

    /** The maximum duration of the downloading animation after it has slowed down to indicate the
     * download is going to take awhile */
    private var _maxDuration: Int =
        app.resources.getInteger(R.integer.quick_animation_duration)

    val maxDuration: Int
        get() = _maxDuration

    /** The repository the user selected to download */
    private lateinit var _selectedRepository: RepositoryInfo

    val selectedRepository: RepositoryInfo
        get() = _selectedRepository

    // populate all the repository objects with information about their respective repositories
    /** Contains the Udacity repository information */
    private val udacityRepositoryInfo = RepositoryInfo(
        UDACITY_NAME, app.getString(R.string.project_radio_button_prompt),
        UDACITY_URL
    )

    /** Contains the Glide repository information */
    private val glideRepositoryInfo = RepositoryInfo(
        GLIDE_NAME, app.getString(R.string.glide_radio_button_prompt), GLIDE_URL
    )

    /** Contains the Retrofit repository information */
    private val retrofitRepositoryInfo = RepositoryInfo(
        RETROFIT_NAME, app.getString(R.string.retrofit_radio_button_prompt),
        RETROFIT_URL
    )

    init {

    }

    /**
     *
     */
    fun setSelectedRepository(id: Int) {
        _selectedRepository = when (id) {
            R.id.glideRadioButton -> glideRepositoryInfo
            R.id.udacityRadioButton -> udacityRepositoryInfo
            R.id.retrofitRadioButton -> retrofitRepositoryInfo
            else -> udacityRepositoryInfo
        }
    }

    /**
     *
     */
    fun calculateMaxDuration(duration: Int) {
        _originalDuration = duration
        _maxDuration = _originalDuration * 10
    }

    /**
     *
     */
    fun calculateDuration(currentDuration: Int): Int {
        var duration: Int = currentDuration

        if (duration >= maxDuration) {
            duration = maxDuration
        } else {
            duration *= 2
            if (duration >= maxDuration) {
                duration = maxDuration
            }
        }

        return (duration)
    }

    /**
     *
     */
    fun isRepositorySelected(): Boolean {
        //https://stackoverflow.com/questions/37618738/how-to-check-if-a-lateinit-variable-has-been-initialized
        return (this::_selectedRepository.isInitialized)
    }

    //
    companion object {
        private const val UDACITY_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val UDACITY_NAME = "Udacity"
        private const val GLIDE_URL =
            "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
        private const val GLIDE_NAME = "Glide"
        private const val RETROFIT_URL =
            "https://github.com/square/retrofit/archive/refs/heads/master.zip"
        private const val RETROFIT_NAME = "Retrofit"

        private const val CHANNEL_ID = "channelId"
    }
}