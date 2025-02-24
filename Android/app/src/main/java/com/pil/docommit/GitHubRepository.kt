package com.pil.docommit.repository

import android.os.Build
import android.util.Log
import com.pil.docommit.api.GitHubApi
import com.pil.docommit.model.Commit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

class GitHubRepository {

    private val api: GitHubApi
    init{
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(GitHubApi::class.java)
    }

    suspend fun getTodayCommitsForAllRepos(username: String): List<Commit>{
        val now = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val todayStart = now.atStartOfDay(ZoneOffset.UTC).toString()
        val todayEnd = now.plusDays(1).atStartOfDay(ZoneOffset.UTC).minusSeconds(1).toString()

//        val since = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            now.toLocalDate().atStartOfDay(ZoneOffset.UTC).toString()
//        } else {
//            TODO("VERSION.SDK_INT < O")
//        }
//        val until = now.plusDays(1).toLocalDate().atStartOfDay(ZoneOffset.UTC).minusSeconds(1).toString()

        Log.d("since : ","since : $todayStart")
        Log.d("until : ", "until :${todayEnd}")

        val repositories = api.getUserRepositories(username)

        val allCommits = mutableListOf<Commit>()

        for( repo in repositories)
        {
            try{
                val commits = api.getCommits(
                    owner = repo.owner.login,
                    repo = repo.name,
                    author = username,
                    since = todayStart,
                    until = todayEnd
                )
                val filteredCommits = commits.filter{ commit ->
                    val authorDate = OffsetDateTime.parse(commit.commit.author.date)
                    authorDate.isAfter(OffsetDateTime.parse(todayStart)) &&
                            authorDate.isBefore(OffsetDateTime.parse(todayEnd))
                }
                allCommits.addAll(filteredCommits)
            }catch(e: Exception){
                println("Error fetching commits for repo: ${repo.name}, error: ${e.message}")
            }
        }

        return allCommits
    }
}