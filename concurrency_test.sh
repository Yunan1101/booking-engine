#!/bin/bash

# 설정
API_URL="http://localhost:8081/api/reservations"
USER_ID=1
SEAT_ID=4
REQUESTS=20

echo "🚀 좌석 ID ${SEAT_ID}번에 대해 ${REQUESTS}명의 동시 접속 예매 요청을 시작합니다..."
echo "---------------------------------------------------"

# 결과를 저장할 임시 파일
TEMP_DIR=$(mktemp -d)

# 100번의 비동기 POST 요청 발송
for i in $(seq 1 $REQUESTS); do
  curl -s -o /dev/null -w "%{http_code}\n" -X POST "$API_URL" \
       -H "Content-Type: application/json" \
       -d "{\"userId\": $USER_ID, \"seatId\": $SEAT_ID}" > "${TEMP_DIR}/result_${i}.txt" &
done

# 모든 백그라운드 프로세스가 끝날 때까지 대기
wait

# 결과 집계
SUCCESS_COUNT=0
CONFLICT_COUNT=0

for i in $(seq 1 $REQUESTS); do
  HTTP_CODE=$(cat "${TEMP_DIR}/result_${i}.txt")
  if [ "$HTTP_CODE" == "200" ]; then
    SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
  elif [ "$HTTP_CODE" == "409" ]; then
    CONFLICT_COUNT=$((CONFLICT_COUNT + 1))
  fi
done

# 결과 출력
echo "✅ 예매 성공(200 OK): ${SUCCESS_COUNT}명"
echo "❌ 이미 예약된 좌석(409 Conflict): ${CONFLICT_COUNT}명"
echo "---------------------------------------------------"

if [ "$SUCCESS_COUNT" -eq 1 ] && [ "$CONFLICT_COUNT" -eq $(($REQUESTS - 1)) ]; then
  echo "🎉 동시성 테스트 통과! 비관적 락(Pessimistic Lock)이 완벽하게 작동하여 초과 예매(Overbooking)를 방지했습니다."
else
  echo "⚠️ 동시성 제어 실패. 결과값을 다시 확인해주세요."
fi

# 임시 폴더 삭제
rm -rf "$TEMP_DIR"
