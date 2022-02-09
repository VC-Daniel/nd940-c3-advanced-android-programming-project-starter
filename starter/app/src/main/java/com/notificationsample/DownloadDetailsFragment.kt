package com.notificationsample

import android.app.NotificationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.notificationsample.databinding.FragmentDownloadDetailsBinding

/**
 * Display the details from downloading a repository
 */
class DownloadDetailsFragment : Fragment() {

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

        // retrieve the passed in repository information and display it.
        val detailsFragmentArgs = DownloadDetailsFragmentArgs.fromBundle(requireArguments())
        binding.repositoryDescription = detailsFragmentArgs.fileDescription
        binding.downloadSuccess = detailsFragmentArgs.downloadSuccessful

        // Go to the main fragment once the user is done looking at the details of this download
        binding.confirmButton.setOnClickListener {
            this.findNavController()
                .navigate(DownloadDetailsFragmentDirections.actionDownloadDetailsFragmentToMainFragment())
        }

        return binding.root
    }
}