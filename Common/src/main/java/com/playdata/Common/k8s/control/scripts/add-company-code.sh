#!/bin/bash

COMPANY_CODE=$1  # 🔄 첫 번째 인자로 회사 코드 받기
API_SERVER="https://kubernetes.default.svc"
NAMESPACE="default"
CONFIGMAP_NAME="kafka-config"
TOKEN=$(cat /var/run/secrets/kubernetes.io/serviceaccount/token)

# 📋 현재 ConfigMap 가져오기
curl -s -H "Authorization: Bearer $TOKEN" \
     -H "Accept: application/json" \
     $API_SERVER/api/v1/namespaces/$NAMESPACE/configmaps/$CONFIGMAP_NAME \
     -k > temp.json

# 🛠 ConfigMap에 회사 코드 추가
jq --arg code "$COMPANY_CODE" '.data.COMPANY_CODE = $code' temp.json > updated.json

# 🔄 ConfigMap 업데이트
curl -s -X PUT -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     --data-binary @updated.json \
     $API_SERVER/api/v1/namespaces/$NAMESPACE/configmaps/$CONFIGMAP_NAME \
     -k

echo "✅ 회사 코드가 성공적으로 등록되었습니다! 🚀"
