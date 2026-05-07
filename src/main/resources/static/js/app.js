const API_BASE = '/api';
const SESSION_ID = 1; // 기본 세션 ID
const USER_ID = 1; // 현재 로그인된 더미 유저 ID

let availableSeats = [];
let selectedSeat = null;

const seatMapEl = document.getElementById('seatMap');
const reserveBtn = document.getElementById('reserveBtn');
const selectedSeatLabel = document.getElementById('selectedSeatLabel');
const selectedSeatPrice = document.getElementById('selectedSeatPrice');
const toastEl = document.getElementById('toast');

// 초기화
async function init() {
    await fetchAvailableSeats();
    renderSeats();
}

// 예약 가능한 좌석 데이터 패치
async function fetchAvailableSeats() {
    try {
        const res = await fetch(`${API_BASE}/sessions/${SESSION_ID}/seats`);
        if(res.ok) {
            availableSeats = await res.json();
        } else {
            showToast('Failed to fetch seats', 'error');
        }
    } catch (e) {
        showToast('Network error', 'error');
    }
}

// 50개 좌석 렌더링
function renderSeats() {
    seatMapEl.innerHTML = '';
    
    // DB의 DataInitializer가 VIP-1 부터 VIP-50 까지 생성함
    // availableSeats 배열에 있는 ID는 AVAILABLE, 없으면 RESERVED 처리
    
    // availableSeats 맵핑 (seatNumber -> seat obj)
    const availableMap = new Map();
    availableSeats.forEach(s => availableMap.set(s.seatNumber, s));

    for (let i = 1; i <= 50; i++) {
        const seatNumStr = `VIP-${i}`;
        const seatEl = document.createElement('div');
        seatEl.classList.add('seat');
        seatEl.innerText = i;

        if (availableMap.has(seatNumStr)) {
            seatEl.classList.add('available');
            const seatData = availableMap.get(seatNumStr);
            seatEl.dataset.id = seatData.id;
            seatEl.dataset.price = seatData.price;
            seatEl.dataset.number = seatData.seatNumber;
            
            seatEl.addEventListener('click', () => selectSeat(seatEl, seatData));
        } else {
            seatEl.classList.add('reserved');
        }

        seatMapEl.appendChild(seatEl);
    }
}

// 좌석 클릭 이벤트
function selectSeat(seatEl, seatData) {
    // 이미 선택된 좌석 취소
    const previouslySelected = document.querySelector('.seat.selected');
    if (previouslySelected) {
        previouslySelected.classList.remove('selected');
        previouslySelected.classList.add('available');
    }

    // 새 좌석 선택
    seatEl.classList.remove('available');
    seatEl.classList.add('selected');
    
    selectedSeat = seatData;
    selectedSeatLabel.innerText = seatData.seatNumber;
    selectedSeatPrice.innerText = `₩ ${seatData.price.toLocaleString()}`;
    
    reserveBtn.disabled = false;
}

// 예매 버튼 클릭 이벤트
reserveBtn.addEventListener('click', async () => {
    if (!selectedSeat) return;

    reserveBtn.disabled = true;
    reserveBtn.innerText = 'PROCESSING...';

    try {
        const res = await fetch(`${API_BASE}/reservations`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                userId: USER_ID,
                seatId: selectedSeat.id
            })
        });

        if (res.ok) {
            showToast(`Success! Reserved ${selectedSeat.seatNumber} 🎉`, 'success');
            // 선택된 좌석 상태를 RESERVED로 변경
            const seatEl = document.querySelector(`.seat[data-id="${selectedSeat.id}"]`);
            if(seatEl) {
                seatEl.classList.remove('selected');
                seatEl.classList.add('reserved');
                // 클릭 이벤트 제거를 위해 노드 복제
                seatEl.replaceWith(seatEl.cloneNode(true));
            }
            resetSelection();
        } else if (res.status === 409) {
            showToast(`Oh no! ${selectedSeat.seatNumber} was already taken by someone else! 😥`, 'error');
            // 실패했으니 RESERVED 처리
            const seatEl = document.querySelector(`.seat[data-id="${selectedSeat.id}"]`);
            if(seatEl) {
                seatEl.classList.remove('selected');
                seatEl.classList.add('reserved');
                seatEl.replaceWith(seatEl.cloneNode(true));
            }
            resetSelection();
        } else {
            showToast('Server Error', 'error');
            resetSelection();
        }
    } catch (e) {
        showToast('Network error', 'error');
        resetSelection();
    }
});

function resetSelection() {
    selectedSeat = null;
    selectedSeatLabel.innerText = 'Select a seat';
    selectedSeatPrice.innerText = '₩ 0';
    reserveBtn.disabled = true;
    reserveBtn.innerText = 'RESERVE TICKET';
}

function showToast(msg, type) {
    toastEl.innerText = msg;
    toastEl.className = `toast show ${type}`;
    setTimeout(() => {
        toastEl.className = 'toast';
    }, 3000);
}

// 시작
init();
