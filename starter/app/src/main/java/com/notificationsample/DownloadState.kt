package com.notificationsample

/**
 * The current state of downloading the selected repository.
 */
sealed class DownloadState {
    object Starting : DownloadState()
    object Loading : DownloadState()
    object Completed : DownloadState()
}