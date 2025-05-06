let selectedInterests = [];

// 관심사 선택
function selectInterest(interest) {
    const button = event.target;

    if (selectedInterests.length < 6 || button.classList.contains('selected')) {
        if (selectedInterests.includes(interest)) {
            // 선택된 항목 해제
            selectedInterests = selectedInterests.filter(item => item !== interest);
            button.classList.remove('selected');
        } else {
            // 최대 6개까지 선택
            if (selectedInterests.length < 6) {
                selectedInterests.push(interest);
                button.classList.add('selected');
            } else {
                alert("관심사는 최대 6개까지 선택할 수 있습니다.");
            }
        }
    }

    updateInterestList();
}

// 선택된 관심사 목록 업데이트
function updateInterestList() {
    const interestList = document.getElementById('interestList');
    interestList.innerHTML = '';
    selectedInterests.forEach(interest => {
        const span = document.createElement('span');
        span.textContent = interest;
        interestList.appendChild(span);
    });
}

// 관심사 팝업 열기
document.getElementById('selectInterestsBtn').addEventListener('click', () => {
    // 팝업을 열기 전에 버튼들을 무채색으로 설정
    const buttons = document.querySelectorAll('.interest-selector button');
    buttons.forEach(button => {
        button.classList.remove('selected');
    });

    document.getElementById('interestModal').style.display = 'flex';
});

// 관심사 팝업 닫기
function closeInterestModal() {
    document.getElementById('interestModal').style.display = 'none';
}