package com.playdata.Common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Service
public class CompanyService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${k8s.config-map-url}")
    private String configMapUrl;

    @Value("${k8s.token}")
    private String k8sToken;       // Kubernetes 인증 토큰

    // 1. 회사 등록 메서드
    public String registerCompany(String companyName) throws Exception {
        synchronized (this) {  // 동시성 제어 - 참조변수(this) lock 사용
            int retryCount = 0;
            while (retryCount < 3) {  // 최대 3번 재시도
                try {
                    // 1.1 현재 ConfigMap 상태 조회
                    Map<String, Object> configMap = getConfigMap();
                    int lastBrokerId = Integer.parseInt((String) configMap.get("last_broker_id"));
                    String brokerUsage = (String) configMap.get("broker_usage");

                    // 1.2 회사 코드 생성
                    int newBrokerId = assignBrokerId(brokerUsage, lastBrokerId);
                    String companyCode = generateCompanyCode(newBrokerId);

                    // ️ 1.3 ConfigMap 업데이트 (낙관적 잠금)
                    updateBrokerUsage(newBrokerId, companyCode, (String) configMap.get("resourceVersion"));

                    return "[회사 등록 완료] 회사 코드: " + companyCode + ", 브로커 ID: " + newBrokerId;
                    // ️ 1.4 예외처리
                } catch (RuntimeException e) {
                    if (e.getMessage().contains("동시성 충돌")) {
                        retryCount++;
                        Thread.sleep(100);
                    } else {
                        throw e;
                    }
                }
            }
            throw new RuntimeException("[실패] 최대 재시도 횟수 초과");
        }
    }

    //  ConfigMap 조회
//  ConfigMap 조회
    private Map<String, Object> getConfigMap() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(k8sToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        ResponseEntity<String> response = restTemplate.exchange(
                configMapUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("ConfigMap 조회 실패: " + response.getBody());
        }

        // JSON 파싱을 위한 ObjectMapper 생성
        ObjectMapper mapper = new ObjectMapper();
        // 응답 데이터를 JSON 트리로 변환
        JsonNode root = mapper.readTree(response.getBody());

        /*
        lastBorkerId = 없으면 "1"로 초기화
        borkerUsage = 없으면 빈 문자열
        resourceVersion = 없으면 "0"으로 초기화
         */

        String lastBrokerId = root.path("data").path("last_broker_id").asText("1");
        String brokerUsage = root.path("data").path("broker_usage").asText("");
        String resourceVersion = root.path("metadata").path("resourceVersion").asText("0");

        return Map.of(
                "last_broker_id", lastBrokerId,
                "broker_usage", brokerUsage,
                "resourceVersion", resourceVersion
        );
    }


    // 브로커 ID 할당 로직 (3개 이상이면 새로운 브로커 할당)
    private int assignBrokerId(String brokerUsage, int lastBrokerId) {
        String[] usageLines = brokerUsage.split("\n");
        for (String line : usageLines) {
            String[] parts = line.split(":");
            int brokerId = Integer.parseInt(parts[0].trim());
            String[] companies = parts[1].split(",");
            if (companies.length < 3) {
                return brokerId;
            }
        }
        // 3개 이상인 경우 → 새로운 브로커 할당
        return lastBrokerId + 1;
    }

    // 회사 코드 생성
    private String generateCompanyCode(int brokerId) {
        return String.format("CMP%03d-%s", brokerId, UUID.randomUUID().toString().substring(0, 8));
    }

    // ConfigMap에 브로커 사용 정보 업데이트
    private void updateBrokerUsage(int brokerId, String companyCode, String resourceVersion) throws Exception {
        // JSON 형식의 Patch 데이터 생성
        String jsonPatch = String.format(
                "{ \"metadata\": { \"resourceVersion\": \"%s\" }, \"data\": { \"broker_usage\": \"%d: %s\" } }",
                resourceVersion, brokerId, companyCode
        );
        // 통신
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/merge-patch+json"));
        headers.setBearerAuth(k8sToken);

        HttpEntity<String> request = new HttpEntity<>(jsonPatch, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                configMapUrl, HttpMethod.PATCH, request, String.class
        );

        if (response.getStatusCode() == HttpStatus.CONFLICT) {
            throw new RuntimeException("동시성 충돌");
        } else if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("ConfigMap 업데이트 실패: " + response.getBody());
        }
    }

    // RestTemplate 빈 등록
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
