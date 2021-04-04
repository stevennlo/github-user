package com.example.githubuser.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Repository(
    @SerialName("stargazers_count")
    val stargazersCount: Int? = null,
    @SerialName("is_template")
    val isTemplate: Boolean? = null,
    @SerialName("pushed_at")
    val pushedAt: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("visibility")
    val visibility: String? = null,
    @SerialName("full_name")
    val fullName: String? = null,
    @SerialName("size")
    val size: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("default_branch")
    val defaultBranch: String? = null,
    @SerialName("private")
    val jsonMemberPrivate: Boolean? = null,
    @SerialName("contributors_url")
    val contributorsUrl: String? = null,
    @SerialName("has_downloads")
    val hasDownloads: Boolean? = null,
    @SerialName("open_issues_count")
    val openIssuesCount: Int? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("has_projects")
    val hasProjects: Boolean? = null,
    @SerialName("archived")
    val archived: Boolean? = null,
    @SerialName("has_wiki")
    val hasWiki: Boolean? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null,
    @SerialName("comments_url")
    val commentsUrl: String? = null,
    @SerialName("stargazers_url")
    val stargazersUrl: String? = null,
    @SerialName("disabled")
    val disabled: Boolean? = null,
    @SerialName("git_url")
    val gitUrl: String? = null,
    @SerialName("has_pages")
    val hasPages: Boolean? = null,
    @SerialName("owner")
    val owner: User? = null,
    @SerialName("commits_url")
    val commitsUrl: String? = null,
    @SerialName("compare_url")
    val compareUrl: String? = null,
    @SerialName("git_commits_url")
    val gitCommitsUrl: String? = null,
    @SerialName("topics")
    val topics: List<String>? = null,
    @SerialName("has_issues")
    val hasIssues: Boolean? = null,
    @SerialName("fork")
    val fork: Boolean? = null,
    @SerialName("watchers_count")
    val watchersCount: Int? = null,
    @SerialName("node_id")
    val nodeId: String? = null,
    @SerialName("homepage")
    val homepage: String? = null,
    @SerialName("forks_count")
    val forksCount: Int? = null
)
