package com.example.scan_service.service;

import com.example.scan_service.model.ScanRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This is the Service layer - it contains our BUSINESS LOGIC.
 *
 * In Stage 1, everything was in one class - data storage, logic,
 * and output all mixed together. That's what made it hard to
 * maintain and scale.
 *
 * Now we have SEPARATION OF CONCERNS:
 *  - Controller  → handles HTTP requests and responses
 *  - Service     → handles business logic (THIS FILE)
 *  - Model/POJO  → represents the data structure
 *
 * Each class has ONE job and does it well.
 *
 * @Service tells Spring Boot to manage this class automatically.
 * Spring creates one instance and shares it across the application.
 * You never need to write "new ScanService()" anywhere!
 *
 * IMPORTANT: We are still using an ArrayList to store data.
 * This means all data is lost when the app restarts.
 * This is exactly the problem Stage 3 (persistence) will fix!
 */
@Service
public class ScanService {

  // In-memory storage using a proper List of ScanRequest POJOs
  // Compare to Stage 1: List<String[]> - raw arrays with no structure
  // Now we have: List<ScanRequest> - proper typed objects
  // Still lost on restart though - that's Stage 3's job to fix!
  private List<ScanRequest> scanRequests = new ArrayList<>();

  // AtomicLong is a thread-safe counter
  // If two requests come in at the same time (concurrency!),
  // a regular int counter could give the same ID to both.
  // AtomicLong handles this safely - one of the concurrency
  // concepts from our Advanced Java section!
  private AtomicLong idCounter = new AtomicLong(1);

  /**
   * Creates a new scan request and adds it to our list.
   * Returns the saved object so the caller can see the assigned ID.
   *
   * In Stage 1 this was: scanRequests.add(new String[]{id, patientId...})
   * Now we work with proper ScanRequest objects!
   */
  public ScanRequest createScan(ScanRequest request) {
    // Assign a unique ID using our thread-safe counter
    request.setId(idCounter.getAndIncrement());

    // Set default status if none was provided in the request
    if (request.getStatus() == null) {
      request.setStatus("PENDING");
    }

    scanRequests.add(request);
    return request;
  }

  /**
   * Returns all scan requests in our list.
   *
   * In Stage 1 this was a void method that just printed to console.
   * Now we RETURN the data so the Controller can send it as JSON.
   * Other services can actually consume this data now!
   */
  public List<ScanRequest> getAllScans() {
    return scanRequests;
  }

  /**
   * Finds a single scan by its ID.
   *
   * Returns Optional<ScanRequest> instead of ScanRequest directly.
   * Optional is a cleaner way to handle "not found" situations
   * without returning null or throwing exceptions everywhere.
   * Think of it as a box that either contains a ScanRequest or is empty.
   *
   * In Stage 1 this was a manual for loop through the array list.
   * Here we use Java Streams - cleaner and more readable.
   */
  public Optional<ScanRequest> getScanById(Long id) {
    return scanRequests.stream()
            .filter(scan -> scan.getId().equals(id))
            .findFirst();
  }

  /**
   * Updates the status of an existing scan request.
   * Returns the updated scan so the caller can see the change,
   * or empty Optional if the scan wasn't found.
   */
  public Optional<ScanRequest> updateScanStatus(Long id, String newStatus) {
    Optional<ScanRequest> found = getScanById(id);
    // ifPresent - only runs if the scan was actually found
    found.ifPresent(scan -> scan.setStatus(newStatus));
    return found;
  }

  /**
   * Deletes a scan request by ID.
   * In a real medical system you might archive instead of delete
   * for compliance and audit trail purposes.
   */
  public void deleteScan(Long id) {
    // Remove the scan from our list where the ID matches
    scanRequests.removeIf(scan -> scan.getId().equals(id));
  }


  public void main(){
    ScanRequest scan = new ScanRequest();
    scan.setPatientId("P101");
    scan.setScanType("CT");
    scan.setStatus("PENDING");
  }}