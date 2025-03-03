package com.playdata.HumanResourceManagement.service.resource;

import com.playdata.HumanResourceManagement.entity.AddressBook;
import com.playdata.HumanResourceManagement.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;

    @Autowired
    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    // 회사코드로 연락처 조회
    public List<AddressBook> getContactsByCompanyCode(String companyCode) {
        return resourceRepository.findByCompanyCode(companyCode);
    }

    // 연락처 저장 (회사코드 테이블이 존재하는 AddressBook 객체)
    public void saveContact(AddressBook addressBook, String companyCode) {
        addressBook.setCompanyCode(companyCode);
        addressBook.setCreatedAt(java.time.LocalDateTime.now());
        resourceRepository.save(addressBook);
    }

    // 연락처 삭제
    public void deleteContact(Long contactId) {
        resourceRepository.deleteById(contactId);
    }

    // 연락처 수정
    public void updateContact(Long contactId, AddressBook addressBook) {
        if (resourceRepository.existsById(contactId)) {
            addressBook.setAddressBookId(contactId);
            resourceRepository.save(addressBook);
        }
    }

    // 연락처 상세 조회
    public AddressBook getContactDetails(Long contactId) {
        Optional<AddressBook> contact = resourceRepository.findById(contactId);
        return contact.orElseThrow(() -> new RuntimeException("Contact not found"));
    }
}
