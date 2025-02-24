import requests
from datetime import datetime, timedelta
from pytz import timezone

def check_commit_today(username, repo):
    """
    해당 리포지토리의 커밋 여부 확인
    """
    # GitHub API 요청 URL
    commits_url = f"https://api.github.com/repos/{username}/{repo}/commits"
    
    # 요청 파라미터 (author로 사용자 필터링)
    params = {
        "author": username,
        "per_page": 100  # 최대 100개의 커밋 가져오기
    }
    
    # API 요청
    response = requests.get(commits_url, params=params)
    
    if response.status_code != 200:
        print(f"Failed to fetch commits for repository '{repo}': {response.status_code}")
        return False

    # UTC 기준 오늘 날짜 계산
    today_utc = datetime.now()

    # 커밋 데이터 확인
    commits = response.json()
    for commit in commits:
        commit_date_utc = datetime.strptime(commit["commit"]["author"]["date"], "%Y-%m-%dT%H:%M:%SZ").date()
        
        if commit_date_utc == today_utc:  # 오늘 날짜와 비교
            return True

    return False

def get_all_repoositories(username):
    """
    내 모든 공개 리포지토리를 가져옵니다.
    """
    repos = []
    page = 1

    while True:
        url = f"https://api.github.com/users/{username}/repos?per_page=100&page={page}"

        response = requests.get(url)

        if response.status_code != 200:
            raise Exception(f"Failed to fetch repositories: {response.status_code}")
            
        # JSON 응답 데이터 파싱
        data = response.json()
        
        if not data:  # 더 이상 리포지토리가 없으면 종료
            break
        
        # 리포지토리 이름 추가
        repos.extend([repo['name'] for repo in data])
        
        page += 1  # 다음 페이지로 이동

    return repos

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
        username = "pil4283"

        # 내 모든 리포 가져오기
        repositories = get_all_repoositories(username)
        if not repositories:
            print("리포지토리가 빔")
        else:
            committed_today = False

            for repo in repositories:
                if check_commit_today(username, repo):
                    commit_dates = True
                    break
            
            if not committed_today:
                print("오늘 커밋 안함")
            else:
                print("오늘 커밋 함")
    
    except Exception as e:
        print(f"An error occurred: {e}")