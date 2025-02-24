package com.pil.docommit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pil.docommit.model.Commit
import com.pil.docommit.repository.GitHubRepository
import kotlinx.coroutines.launch

class CommitViewModel : ViewModel() {
    private val repository = GitHubRepository()

    // TextView에 표시할 메시지
    private val _commitStatus = MutableLiveData<String>()
    val commitStatus: LiveData<String> get() = _commitStatus

    // 커밋 리스트
    private val _commits = MutableLiveData<List<Commit>>()
    val commits: LiveData<List<Commit>> get() = _commits

    // 커밋 상태 업데이트 (Repository 호출)
    fun fetchCommitStatus(username: String) {
        viewModelScope.launch {
            try {
                val commits = repository.getTodayCommitsForAllRepos(username)
                if (commits.isNotEmpty()) {
                    _commitStatus.value = "오늘 커밋 있음"
                    _commits.value = commits // 필요 시 커밋 리스트도 업데이트 가능
                } else {
                    _commitStatus.value = "오늘 커밋 없음"
                }
            } catch (e: Exception) {
                _commitStatus.value = "커밋 정보를 가져오는 중 오류가 발생 - ${e.message}"
            }
        }
    }
}