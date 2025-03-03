#!/bin/bash

BACKUP_FILE=$1

if [ -z "$BACKUP_FILE" ]; then
  echo "❌ 백업 파일 경로를 입력하세요."
  exit 1
fi

echo "🔄 Redis 복구 시작..."
kubectl cp $BACKUP_FILE redis-0:/data/dump.rdb
kubectl exec -it redis-0 -- redis-cli shutdown save
kubectl delete pod redis-0  # 🌀 재시작을 통한 복구
echo "✅ 복구 완료!"
