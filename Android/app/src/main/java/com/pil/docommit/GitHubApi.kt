package com.pil.docommit.api

import com.pil.docommit.model.Commit
import com.pil.docommit.model.Repository
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {
    @GET("users/{username}/repos")
        suspend fun getUserRepositories(
            @Path("username") username: String
        ): List<Repository>


    @GET("repos/{owner}/{repo}/commits")
    suspend fun getCommits(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("author") author: String,
        @Query("Since") since: String,
        @Query("until") until: String
    ): List<Commit>
}