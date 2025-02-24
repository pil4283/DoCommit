package com.pil.docommit

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels

import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.Observer
import com.pil.docommit.viewmodel.CommitViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: CommitViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val commitStatusTextView = findViewById<TextView>(R.id.commitStatusTextView)
        enableEdgeToEdge()
        viewModel.commitStatus.observe(this, Observer{status -> commitStatusTextView.text = status})

        // Todo : activity에서 입력부분 추가
        viewModel.fetchCommitStatus("pil4283")
    }

/*    private fun checkTodayCommitsForAllRepos(username: String){
        lifecycleScope.launch(Dispatchers.IO){
            val commitStatus = "오늘 커밋 안함"

            try {
                val commits = repository.getTodayCommitsForAllRepos(username)
                if(commits.isNotEmpty()){
                    Log.d("DoCommit", "Today Commit List :")
                    commits.forEach{commit ->
                        Log.d("DoCommitCheck", commit.sha)
                        Log.d("DoCommitCheck", "Committer: ${commit.commit.committer.date}")
                        Log.d("DoCommit", "Repo: " +
                                "${commit.commit.author.name}, : ${commit.commit.message}")}
                }
                else{
                    Log.d("DoCommit", "오늘의 커밋이 없음")
                }
            }catch(e: Exception){
                Log.e("DoCommit", "error: ${e.message}")
            }


        }
    }*/
}
