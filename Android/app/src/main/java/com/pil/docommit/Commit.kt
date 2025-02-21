package com.pil.docommit.model

data class Commit(
    val sha: String,
    val commit: CommitDetail
)

data class CommitDetail(
    val message: String,
    val author: Author,
    val committer: Committer
)

data class Author(
    val name: String,
    val date: String
)

data class Committer(
    val name: String,
    val date: String
)


