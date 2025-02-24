package com.pil.docommit.api

import com.pil.docommit.model.Commit
import com.pil.docommit.model.Repository
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// 깃허브 Rest API
interface GitHubApi {
    // 특정 사용자의 모든 저장소 가져오기
    @GET("users/{username}/repos")
        suspend fun getUserRepositories(
            @Path("username") username: String
        ): List<Repository>

    // 특정 저장소의 커밋 가져오기
    @GET("repos/{owner}/{repo}/commits")
    suspend fun getCommits(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("author") author: String,
        @Query("Since") since: String,
        @Query("until") until: String
    ): List<Commit>
}