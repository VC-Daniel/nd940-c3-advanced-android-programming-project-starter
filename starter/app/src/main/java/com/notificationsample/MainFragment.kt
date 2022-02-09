package com.notificationsample

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.notificationsample.databinding.FragmentMainBinding

/**
 * Display a choice of repositories for the user to download. After starting a download the UI is
 * animated to inform the user that the selected repository is being downloaded.
 */
class MainFragment : Fragment() {

    /** Denotes if a repository is currently being downloaded */
    private var downloading: Boolean = false

    private var downloadID: Long = 0
    private lateinit var notificationManager: NotificationManager
    private lateinit var motionLayout: MotionLayout
    private lateinit var loadingButton: LoadingButton

    private val viewModel: MainViewModel by lazy {

        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // Register a receiver so we know when the download is complete
        requireContext().registerReceiver(
            receiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )

        // Update the selected repository when the user has made a selection
        binding.urlSelectorRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.setSelectedRepository(checkedId)
        }

        motionLayout = binding.motionLayout
        loadingButton = binding.loadingButton

        // Start downloading the selected repository when the using touches the download button
        binding.loadingButton.setOnClickListener {
            if (viewModel.isRepositorySelected()) {
                loadingButton.downloadState = DownloadState.Starting
                download(viewModel.selectedRepository.repositoryURL)
                animateLoadingButton()
            } else {
                // Notify the user if no repository has been selected
                Toast.makeText(
                    requireContext(),
                    getString(R.string.select_url_hint),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Calculate the maximum duration for the loading animation as it slows down the
        // longer the download is active
        viewModel.calculateMaxDuration(motionLayout.getTransition(R.id.loadingTransition).duration)

        // Create a channel to post our notifications on when a download finishes
        createChannel(
            getString(R.string.download_notification_channel_id),
            getString(R.string.download_notification_channel_name)
        )

        notificationManager = ContextCompat.getSystemService(
            requireContext(),
            NotificationManager::class.java
        ) as NotificationManager

        return binding.root
    }

    /** Create a notification channel with the supplied [channelId] and [channelName] */
    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                    .apply { setShowBadge(true) }
            notificationChannel.enableLights(false)
            notificationChannel.lightColor = requireContext().getColor(R.color.colorPrimary)
            notificationChannel.enableVibration(true)
            notificationChannel.description =
                getString(R.string.download_notification_channel_Description)

            val notificationManager =
                requireContext().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    /**
     * Animate the UI to indicate a download is in progress. Starting a motion scene
     * programmatically is based on the logic found at:
     * //https://stackoverflow.com/questions/52437946/start-motion-scene-programmatically
     */
    private fun animateLoadingButton() {
        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout, i: Int, i1: Int) {
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout,
                i: Int,
                i1: Int,
                v: Float
            ) {
            }

            /**
             * Continue running the animation until the download has completed. This logic to run
             * the animation continuously is based on the logic found at:
             * https://stackoverflow.com/questions/62348505/how-to-run-animation-defind-in-the-android-motionlayout-motionscene-infinitely
             */
            override fun onTransitionCompleted(motionLayout: MotionLayout, i: Int) {
                if (downloading) {
                    motionLayout.progress = 0f

                    // Each time the animation completes and the download is still ongoing slow down
                    // animation to convey to the user that the download is going to take awhile
                    motionLayout.setTransitionDuration(
                        viewModel.calculateDuration(
                            motionLayout.getTransition(
                                R.id.loadingTransition
                            ).duration
                        )
                    )
                    animateLoadingButton();
                } else {
                    // When the download is no longer ongoing stop the animation
                    motionLayout.setTransitionDuration(viewModel.originalDuration)
                    motionLayout.progress = 0f
                }
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout,
                i: Int,
                b: Boolean,
                v: Float
            ) {
            }
        })
        motionLayout.transitionToEnd()
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Check the id to make sure this is the download we are waiting on to finish
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            var downloadSuccessful = false

            /** Determine if the download was successful or not. This is based on the logic
             * found at https://developer.android.com/reference/android/app/DownloadManager#COLUMN_STATUS
             * and https://code.luasoftware.com/tutorials/android/android-download-file-with-downloadmanager-and-check-status/
             */
            if (id == downloadID) {

                val downloadManager =
                    requireActivity().getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager

                val cursor: Cursor =
                    downloadManager.query(DownloadManager.Query().setFilterById(id));

                if (cursor.moveToNext()) {
                    val columnStatusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    val statusCode: Int = cursor.getInt(columnStatusIndex)
                    cursor.close()
                    downloadSuccessful = statusCode == DownloadManager.STATUS_SUCCESSFUL
                }

                loadingButton.downloadState = DownloadState.Completed

                downloading = false
                motionLayout.progress = 0f

                /**
                 * Pass in the repository information to be displayed if the user checks the
                 * status of the downloa. This logic is partially based off of the tutorial at
                 * https://medium.com/androiddevelopers/navigating-with-deep-links-910a4a6588c
                 */
                val navigationArgs =
                    MainFragmentDirections.actionMainFragmentToDownloadDetailsFragment(
                        viewModel.selectedRepository.repositoryDesc
                    ).setDownloadSuccessful(downloadSuccessful).arguments

                // Post a notification to tell the user the download has finished
                notificationManager.sendNotification(
                    navigationArgs,
                    getString(
                        R.string.notification_description,
                        viewModel.selectedRepository.repositoryName
                    ),
                    requireContext()
                )
            }
        }
    }

    /** Download the selected repository */
    private fun download(url: String) {
        downloading = true
        notificationManager.cancelAll()

        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager =
            requireActivity().getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }
}