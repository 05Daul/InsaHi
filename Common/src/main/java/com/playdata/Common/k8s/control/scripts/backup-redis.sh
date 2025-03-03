#!/bin/bash

TIMESTAMP=$(date +%Y%m%d%H%M%S)
BACKUP_FILE="/mnt/data/backup/redis-backup-$TIMESTAMP.rdb"

echo "🔄 Redis 백업 시작..."
kubectl exec -it redis-0 -- redis-cli save
kubectl cp redis-0:/data/dump.rdb $BACKUP_FILE
echo "✅ 백업 완료: $BACKUP_FILE"
