package com.example.scan_service.service;

import com.example.scan_service.model.ScanRequest;
import com.example.scan_service.repository.ScanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * This is the Service layer - contains our BUSINESS LOGIC.
 *
 * Compare this carefully to Stage 2 ScanService.
 * The methods are IDENTICAL - same names, same logic.
 * The ONLY difference is where data comes from:
 *
 *   Stage 2: private List<ScanRequest> scanRequests = new ArrayList<>()
 *   Stage 3: private ScanRepository scanRepository (talks to database!)
 *
 * This shows the power of clean layered architecture -
 * we swapped the entire data layer without touching the Controller!
 *
 * Also, no more AtomicLong! The database generates IDs
 * automatically and handles concurrency safely through
 * ACID transactions.
 */
@Service
public class ScanService {

  // Instead of an ArrayList, we now have a Repository
  // that talks directly to our H2 database
  @Autowired
  private ScanRepository scanRepository;

  /**
   * Creates and PERSISTS a new scan request to the database.
   *
   * Stage 2: scanRequests.add(request) → saved to ArrayList (lost on restart)
   * Stage 3: scanRepository.save(request) → saved to DB (survives restarts!)
   *
   * No more AtomicLong - the database auto-generates the ID!
   */
  public ScanRequest createScan(ScanRequest request) {
    if (request.getStatus() == null) {
      request.setStatus("PENDING");
    }
    // save() does INSERT if new, UPDATE if ID already exists
    // Returns the saved entity with the database-generated ID
    return scanRepository.save(request);
  }

  /**
   * Retrieves ALL scan requests from the database.
   *
   * Stage 2: return scanRequests (the ArrayList)
   * Stage 3: return scanRepository.findAll() (SELECT * from database)
   *
   * Same method signature - Controller doesn't need to change!
   */
  public List<ScanRequest> getAllScans() {
    return scanRepository.findAll();
  }

  /**
   * Finds a scan by ID from the database.
   *
   * Stage 2: manual stream().filter() loop through ArrayList
   * Stage 3: scanRepository.findById() (SELECT WHERE id = ?)
   *
   * Still returns Optional - same API, cleaner implementation!
   */
  public Optional<ScanRequest> getScanById(Long id) {
    return scanRepository.findById(id);
  }

  /**
   * Updates a scan status and saves it back to the database.
   *
   * Stage 2: mutated the object in the ArrayList
   * Stage 3: mutate the object then call save() to persist the change
   */
  public Optional<ScanRequest> updateScanStatus(Long id, String newStatus) {
    Optional<ScanRequest> found = scanRepository.findById(id);
    found.ifPresent(scan -> {
      scan.setStatus(newStatus);
      scanRepository.save(scan); // persist the update to database!
    });
    return found;
  }

  /**
   * Deletes a scan from the database by ID.
   */
  public void deleteScan(Long id) {
    scanRepository.deleteById(id);
  }

  // BONUS - new methods only possible with persistence!
  // These would have been complex manual loops in Stage 2

  /**
   * Find all scans for a specific patient.
   * Critical for patient history in a medical system!
   */
  public List<ScanRequest> getScansByPatient(String patientId) {
    return scanRepository.findByPatientId(patientId);
  }

  /**
   * Find all scans with a given status
   */
  public List<ScanRequest> getScansByStatus(String status) {
    return scanRepository.findByStatus(status);
  }
}