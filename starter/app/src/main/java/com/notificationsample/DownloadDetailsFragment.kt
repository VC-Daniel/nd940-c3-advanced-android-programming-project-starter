package com.notificationsample

import android.app.NotificationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.notificationsample.databinding.FragmentDownloadDetailsBinding

/**
 * Display the details from downloading a repository
 */
class DownloadDetailsFragment : Fragment() {

    private lateinit var motionLayout: MotionLayout
    private var downloadSuccessful: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Since we are showing the details of the download cancel all notifications
        val notificationManager = ContextCompat.getSystemService(
            requireContext(),
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelAll()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDownloadDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        motionLayout = binding.motionLayout
        animateDownloadStatusText()

        // retrieve the passed in repository information and display it.
        val detailsFragmentArgs = DownloadDetailsFragmentArgs.fromBundle(requireArguments())
        binding.repositoryDescription = detailsFragmentArgs.fileDescription

        downloadSuccessful = detailsFragmentArgs.downloadSuccessful
        binding.downloadSuccess = downloadSuccessful

        // Go to the main fragment once the user is done looking at the details of this download
        binding.confirmButton.setOnClickListener {
            this.findNavController()
                .navigate(DownloadDetailsFragmentDirections.actionDownloadDetailsFragmentToMainFragment())
        }
        return binding.root
    }

    /**
     * Animate the UI to draw attention when a download has failed. Starting a motion scene
     * transition programmatically when another has completed is based on the logic found at:
     * //https://stackoverflow.com/questions/52437946/start-motion-scene-programmatically
     */
    private fun animateDownloadStatusText() {
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
             * After the initial animation has completed check to see if the download was successful.
             * If the download failed draw attention to the download status by animating the UI
             * with the failedAnimation
             */
            override fun onTransitionCompleted(motionLayout: MotionLayout, i: Int) {
                if (motionLayout.startState == R.id.start && !downloadSuccessful) {
                    motionLayout.setTransition(R.id.failedAnimation)
                    motionLayout.progress = 0f
                    motionLayout.transitionToEnd()
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
    }
}