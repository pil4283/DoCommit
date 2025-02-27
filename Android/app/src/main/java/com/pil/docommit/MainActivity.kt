package com.pil.docommit

import android.health.connect.datatypes.units.Length
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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

        val gitEditText = findViewById<EditText>(R.id.githubIdEditText)
        val gitID = gitEditText.text
        val setAlramButton = findViewById<Button>(R.id.setAlramButton)

        setAlramButton.setOnClickListener{
            viewModel.fetchCommitStatus(gitID.toString())
            Toast.makeText(this, gitID,Toast.LENGTH_LONG).show()
        }
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
