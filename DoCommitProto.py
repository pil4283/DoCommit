import os
import requests
from datetime import datetime, timedelta
from pytz import timezone

#GitHub Personal Access Token (환경 변수에서 읽기)
#GITHUB_TOKEN = os.getenv("GITHUB_TOKEN")
GITHUB_TOKEN = os.environ.get("GITHUB_TOKEN")
print(GITHUB_TOKEN)
if not GITHUB_TOKEN:
    raise Exception("GitHub token not found.")

def get_user_name():
    """
    GitHub API를 호출하여 현재 로그인된 사용자의 정보를 가져옵니다.
    """
    headers = {
        "Authorization": f"Bearer {GITHUB_TOKEN}",
        "Accept": "application/vnd.github+json"
    }
    user_url = "https://api.github.com/user"
    response = requests.get(user_url, headers=headers)

    if response.status_code != 200:
        raise Exception(f"Failed to fetch user info: {response.status_code}")

    user_data = response.json()
    return user_data['login']

def get_commit_dates(username):
    """
    GitHub API를 호출하여 사용자의 최근 커밋 날짜 목록을 가져옵니다.
    """
    headers = {
        "Authorization": f"Bearer {GITHUB_TOKEN}",
        "Accept": "application/vnd.github+json"
    }

    repos_url = f"https://api.github.com/users/{username}/repos"
    repos_response = requests.get(repos_url, headers=headers)
    
    if repos_response.status_code != 200:
        raise Exception(f"Failed to fetch repositories: {repos_response.status_code}")
    
    repos = repos_response.json()
    commit_dates = set()

    for repo in repos:
        repo_name = repo["name"]
        owner = repo["owner"]["login"]

        commits_url = f"https://api.github.com/repos/{owner}/{repo_name}/commits"
        params = {"author": username, "per_page": 100}
        commits_response = requests.get(commits_url, headers=headers, params=params)

        if commits_response.status_code != 200:
            print(f"Failed to fetch commits for {repo_name}: {commits_response.status_code}")
            continue
        
        commits = commits_response.json()
        for commit in commits:
            commit_date = commit["commit"]["author"]["date"]
            commit_dates.add(commit_date[:10])

    return sorted(commit_dates, reverse=True)

def calculate_streak(commit_dates):
    """
    커밋 날짜 목록을 기반으로 연속 커밋 일수를 계산합니다.
    """
    streak = 0
    local_tz = timezone('Asia/Seoul')
    today = datetime.now(local_tz).date()

    for i, date_str in enumerate(commit_dates):
        commit_date = datetime.strptime(date_str, "%Y-%m-%d").date()

        if i == 0 and commit_date != today:  
            break

        if i > 0:
            previous_date = datetime.strptime(commit_dates[i - 1], "%Y-%m-%d").date()
            if (previous_date - commit_date).days > 1:  
                break

        streak += 1

    return streak

if __name__ == "__main__":
    try:
        user_name = get_user_name()
        
        commit_dates = get_commit_dates(user_name)
        
        if not commit_dates:
            print("오늘 커밋 안함")
        else:
            print("오늘 커밋 함")
    
    except Exception as e:
        print(f"An error occurred: {e}")