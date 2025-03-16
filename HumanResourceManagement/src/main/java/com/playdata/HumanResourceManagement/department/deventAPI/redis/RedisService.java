//package com.playdata.HumanResourceManagement.department.deventAPI.redis;
//
//import com.playdata.HumanResourceManagement.department.business.dto.newDto.ActionBasedOrganizationChartDTO;
//import com.playdata.HumanResourceManagement.department.business.dto.newDto.FullOrganizationChartDTO;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class RedisService {
//
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    /**
//     * 조직도를 업데이트 (ActionBasedOrganizationChartDTO)
//     */
//    public void updateOrganizationChart(String companyCode, ActionBasedOrganizationChartDTO department) {
//        redisTemplate.opsForValue().set("organizationChart:" + companyCode, department);
//    }
//
//    /**
//     * 조직도 조회 (FullOrganizationChartDTO 리스트 반환)
//     */
//    public List<FullOrganizationChartDTO> getOrganizationChart(String companyCode) {
//        return (List<FullOrganizationChartDTO>) redisTemplate.opsForValue().get("organizationChart:" + companyCode);
//    }
//
//    /**
//     * 조직도 저장 (FullOrganizationChartDTO 리스트 저장)
//     */
//    public void saveOrganizationChart(String companyCode, List<FullOrganizationChartDTO> organizationChart) {
//        redisTemplate.opsForValue().set("organizationChart:" + companyCode, organizationChart);
//    }
//
//    /**
//     * 조직도 삭제
//     */
//    public void deleteOrganizationChart(String companyCode) {
//        redisTemplate.delete("organizationChart:" + companyCode);
//    }
//
//    /**
//     * ActionBasedOrganizationChartDTO 업데이트 (Redis 저장)
//     */
//    public void updateActionBasedOrganizationChart(String companyCode, ActionBasedOrganizationChartDTO actionDTO) {
//        String key = "actionBasedOrganizationChart:" + companyCode;
//        redisTemplate.opsForValue().set(key, actionDTO);
//    }
//}
