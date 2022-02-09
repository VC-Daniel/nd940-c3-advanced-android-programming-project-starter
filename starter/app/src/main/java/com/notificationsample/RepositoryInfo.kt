package com.notificationsample

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Information about a repository. [repositoryName] is the a short alias to use when denoting the
 * repository while [repositoryDesc] describes the repository in more detail. [repositoryURL]
 * defines where the repository can be downloaded from
 */
@Parcelize
data class RepositoryInfo(
    val repositoryName: String,
    val repositoryDesc: String,
    val repositoryURL: String
) : Parcelable