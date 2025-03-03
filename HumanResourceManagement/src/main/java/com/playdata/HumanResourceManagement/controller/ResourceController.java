package com.playdata.HumanResourceManagement.controller;

import com.playdata.HumanResourceManagement.entity.AddressBook;
import com.playdata.HumanResourceManagement.service.resource.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {

    private final ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    // 회사코드로 연락처 목록 조회
    @GetMapping("/contact/{companyCode}")
    public ResponseEntity<List<AddressBook>> searchContactByCompanyCode(@PathVariable String companyCode) {
        List<AddressBook> contacts = resourceService.getContactsByCompanyCode(companyCode);
        if (contacts.isEmpty()) {
            return ResponseEntity.status(404).body(null);  // 연락처가 없을 경우 404 반환
        }
        return ResponseEntity.ok(contacts);  // 연락처 목록 반환
    }

    // 연락처 업로드
    @PostMapping("/contact")
    public ResponseEntity<String> uploadContact(@RequestBody AddressBook addressBook, @RequestParam String companyCode) {
        resourceService.saveContact(addressBook, companyCode);  // 회사코드와 함께 저장
        return ResponseEntity.ok("Contact uploaded successfully.");
    }

    // 연락처 삭제
    @DeleteMapping("/contact/{contactId}")
    public ResponseEntity<String> deleteContact(@PathVariable Long contactId) {
        resourceService.deleteContact(contactId);
        return ResponseEntity.ok("Contact with ID " + contactId + " deleted successfully.");
    }

    // 연락처 수정
    @PutMapping("/contact/{contactId}")
    public ResponseEntity<String> updateContact(@PathVariable Long contactId, @RequestBody AddressBook addressBook) {
        resourceService.updateContact(contactId, addressBook);
        return ResponseEntity.ok("Contact with ID " + contactId + " updated successfully.");
    }

    // 연락처 상세 정보 조회
    @GetMapping("/contact/details/{contactId}")
    public ResponseEntity<AddressBook> contactDetails(@PathVariable Long contactId) {
        AddressBook contact = resourceService.getContactDetails(contactId);
        return ResponseEntity.ok(contact);
    }
}
