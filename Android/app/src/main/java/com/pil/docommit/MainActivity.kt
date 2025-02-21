package com.pil.docommit

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.pil.docommit.ui.theme.DoCommitTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val repository = GitHubRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()
        // Todo : activity에서 입력부분 추가
        checkTodayCommitsForAllRepos("pil4283")
/*        setContent {
            DoCommitTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }*/
    }

    private fun checkTodayCommitsForAllRepos(username: String){
        lifecycleScope.launch(Dispatchers.IO){
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
    }
}

/*
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DoCommitTheme {
        Greeting("Android")
    }
}*/
