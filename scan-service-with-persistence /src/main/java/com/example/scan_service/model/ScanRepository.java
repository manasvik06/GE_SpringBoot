package com.example.scan_service.repository;

import com.example.scan_service.model.ScanRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This is the Repository - the DATA ACCESS LAYER.
 *
 * By extending JpaRepository we get ALL of these for FREE:
 *   save()       → INSERT or UPDATE a row in the database
 *   findById()   → SELECT WHERE id = ?
 *   findAll()    → SELECT * FROM scan_requests
 *   deleteById() → DELETE WHERE id = ?
 *   count()      → SELECT COUNT(*)
 *
 * No SQL needed - Spring Data JPA generates it automatically!
 *
 * JpaRepository<ScanRequest, Long> means:
 *   ScanRequest = the entity type
 *   Long = the type of the primary key
 */
@Repository
public interface ScanRepository extends JpaRepository<ScanRequest, Long> {

  // Custom query methods - Spring generates SQL from the method name

  // SELECT * FROM scan_requests WHERE patient_id = ?
  List<ScanRequest> findByPatientId(String patientId);

  // SELECT * FROM scan_requests WHERE status = ?
  List<ScanRequest> findByStatus(String status);
}