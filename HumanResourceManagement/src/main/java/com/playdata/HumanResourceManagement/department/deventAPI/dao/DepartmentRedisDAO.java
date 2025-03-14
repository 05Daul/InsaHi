package com.playdata.HumanResourceManagement.department.deventAPI.dao;

import com.playdata.HumanResourceManagement.department.business.dto.newDto.FullOrganizationChartDTO;
import com.playdata.HumanResourceManagement.department.business.dto.newDto.OrganizationStructureDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class DepartmentRedisDAO {

    private final RedisTemplate<String, Object> redisTemplate;

    public DepartmentRedisDAO(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 캐시 키 생성
    public String buildOrganizationChartKey(String companyCode) {
        return "organizationChart::" + companyCode;
    }

    /**
     * Redis에서 조직도를 조회하는 메서드
     * @param companyCode 회사 코드
     * @return 조직도 리스트 (캐시된 값)
     */
    public List<FullOrganizationChartDTO> getFullOrganizationChart(String companyCode) {
        String key = buildOrganizationChartKey(companyCode);
        return (List<FullOrganizationChartDTO>) redisTemplate.opsForValue().get(key); // Redis에서 조회
    }

    /**
     * Redis에 조직도를 저장하는 메서드
     * @param companyCode 회사 코드
     * @param chart 조직도 리스트
     */
    public void saveFullOrganizationChart(String companyCode, List<FullOrganizationChartDTO> chart) {
        String key = buildOrganizationChartKey(companyCode);
        redisTemplate.opsForValue().set(key, chart, 24, TimeUnit.HOURS); // 24시간 동안 캐시 저장
    }

    /**
     * Redis에서 조직도를 삭제하는 메서드
     * @param companyCode 회사 코드
     */
    public void deleteFullOrganizationChart(String companyCode) {
        String key = buildOrganizationChartKey(companyCode);
        redisTemplate.delete(key); // Redis에서 삭제
    }

    /**
     * OrganizationStructureDTO를 저장하는 메서드 (예시)
     */
    public void saveOrganizationStructure(String companyCode, List<OrganizationStructureDTO> structure) {
        String key = "organizationStructure::" + companyCode;
        redisTemplate.opsForValue().set(key, structure, 24, TimeUnit.HOURS); // 24시간 동안 캐시 저장
    }

    public List<OrganizationStructureDTO> getOrganizationStructure(String companyCode) {
        String key = "organizationStructure::" + companyCode;
        return (List<OrganizationStructureDTO>) redisTemplate.opsForValue().get(key);
    }
}
