package com.example.scan_service.controller;

import com.example.scan_service.model.ScanRequest;
import com.example.scan_service.service.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
// This class is the exact same - eventhough we have added persistence to our microservice!
/**
 * This is the Controller - the ENTRY POINT of our microservice.
 * It exposes HTTP endpoints that anyone can call over the network.
 * This is what makes it a microservice - it's accessible via REST API!
 *
 * In our monolothic application there was no API at all. Data was just printed to
 * System.out and no one else could access it.
 *
 * Key annotations:
 *
 * @RestController - marks this as a REST API controller.
 *   Automatically converts our Java objects to JSON (serialization)
 *   and incoming JSON to Java objects (deserialization).
 *   This uses Jackson under the hood, no manual JSON writing and handling (this makes it very easy for the user!)
 *
 * @RequestMapping("/api/scans") - sets the base URL for all
 *   endpoints in this controller. Every endpoint starts with /api/scans. The user can customise this :)
 *
 * @Autowired - Spring automatically injects the ScanService here.
 *   This is dependency injection - we don't create ScanService
 *   ourselves, Spring handles it. The controller doesn't need to
 *   know HOW ScanService works, just that it can use it.
 */
@RestController
@RequestMapping("/api/scans")
public class ScanController {

  // Spring automatically provides us a ScanService instance
  // No need to write: ScanService scanService = new ScanService()
  @Autowired
  private ScanService scanService;

  /**
   * POST /api/scans
   * Creates a new scan request.
   *
   * @RequestBody tells Spring to take the incoming JSON and
   * automatically convert it into a ScanRequest object for us. Then we can use
   * all the object methods (again deserialization is being taken care for us!)
   *
   * Example JSON to send:
   * {
   *   "patientId": "P001",
   *   "scanType": "Chest CT",
   *   "scheduledDate": "2024-06-01"
   * }
   *
   * HTTP Status Codes:
   *  201 CREATED     → scan was successfully created
   *  400 BAD REQUEST → missing required fields (patientId or scanType)
   *  500 SERVER ERROR → something unexpected went wrong on our end
   *
   * NOTE: We use 201 CREATED instead of 200 OK because 201 is the
   * technically correct REST response when a new resource is created.
   */
  @PostMapping
  public ResponseEntity<ScanRequest> createScan(@RequestBody ScanRequest request) {
    try {
      // Validate required fields before doing anything, these are my own validation checks I have
      // created, of course in an actual application this could be very different
      if (request.getPatientId() == null || request.getPatientId().isEmpty()) {
        return ResponseEntity.badRequest().build(); // 400
      }
      // scanType is also required - we need to know what kind of scan this is
      if (request.getScanType() == null || request.getScanType().isEmpty()) {
        return ResponseEntity.badRequest().build(); // 400
      }

      ScanRequest created = scanService.createScan(request);

      // ResponseEntity.status(201) sends back HTTP 201 CREATED with our
      // object as JSON - that JSON conversion is SERIALIZATION happening!
      return ResponseEntity.status(201).body(created);

    } catch (Exception e) {
      // Something unexpected went wrong on our end
      // We return 500 so the client knows it's a server side problem
      return ResponseEntity.status(500).build(); // 500
    }
  }

  /**
   * GET /api/scans
   * Returns all scan requests as a JSON array.
   *
   * Try this in your browser: http://localhost:8080/api/scans
   * You'll see all scans returned as JSON - no manual formatting needed!
   *
   * NOTE: An empty list is still 200 OK - not 404.
   * 404 means the ENDPOINT doesn't exist, not that the list is empty.
   * This is an important REST API design distinction!
   *
   * HTTP Status Codes:
   *  200 OK           → returns list successfully (even if empty)
   *  500 SERVER ERROR → something unexpected went wrong
   */
  @GetMapping
  public ResponseEntity<List<ScanRequest>> getAllScans() {
    try {
      // Spring automatically serializes our List of ScanRequest
      // objects into a JSON array on the way out
      return ResponseEntity.ok(scanService.getAllScans()); // 200
    } catch (Exception e) {
      return ResponseEntity.status(500).build(); // 500
    }
  }

  /**
   * GET /api/scans/{id}
   * Returns a single scan request by its ID.
   *
   * @PathVariable extracts the {id} from the URL.
   * e.g. GET /api/scans/1 → id = 1
   *
   * Returns 200 OK with the scan if found.
   * Returns 404 Not Found if no scan has that ID.
   *
   * HTTP Status Codes:
   *  200 OK           → scan found and returned as JSON
   *  404 NOT FOUND    → no scan exists with that ID
   *  500 SERVER ERROR → something unexpected went wrong
   */
  @GetMapping("/{id}")
  public ResponseEntity<ScanRequest> getScanById(@PathVariable Long id) {
    try {
      Optional<ScanRequest> result = scanService.getScanById(id);

      if (result.isPresent()) {
        return ResponseEntity.ok(result.get()); // 200 OK with body
      } else {
        return ResponseEntity.notFound().build(); // 404
      }

    } catch (Exception e) {
      return ResponseEntity.status(500).build(); // something went wrong on our end : server error
    }
  }

  /**
   * PUT /api/scans/{id}/status
   * Updates the status of a scan request.
   *
   * @RequestParam extracts a value from the URL query string.
   * e.g. PUT /api/scans/1/status?newStatus=COMPLETED
   *
   * We use PUT here because we are UPDATING an existing resource.
   * Using the right HTTP method is part of good REST API design!
   *
   * HTTP Status Codes:
   *  200 OK           → status updated successfully
   *  400 BAD REQUEST  → invalid status value provided
   *  404 NOT FOUND    → no scan exists with that ID
   *  500 SERVER ERROR → something unexpected went wrong
   */
  @PutMapping("/{id}/status")
  public ResponseEntity<ScanRequest> updateStatus(
          @PathVariable Long id,
          @RequestParam String newStatus) {
    try {
      // Validate that the status is one of our three allowed values
      // We don't want someone setting a scan status to an invalid value!
      if (!newStatus.equals("PENDING") &&
              !newStatus.equals("COMPLETED") &&
              !newStatus.equals("CANCELLED")) {
        return ResponseEntity.badRequest().build(); // 400
      }

      Optional<ScanRequest> result = scanService.updateScanStatus(id, newStatus);

      if (result.isPresent()) {
        return ResponseEntity.ok(result.get());
      } else {
        return ResponseEntity.notFound().build();
      }

    } catch (Exception e) {
      // Something unexpected went wrong on our end
      return ResponseEntity.status(500).build(); // 500
    }
  }



    /**
     * DELETE /api/scans/{id}
     * Deletes a scan request by its ID.
     *
     * We use DELETE because we are REMOVING an existing resource.
     * This completes our full REST API - GET, POST, PUT, DELETE.
     *
     * We can also just set the status to CANCELLED but here I have included
     * DELETE just to complete all the REST processes
     *
     * HTTP Status Codes:
     *  204 NO CONTENT   → scan deleted successfully (no body returned)
     *  404 NOT FOUND    → no scan exists with that ID
     *  500 SERVER ERROR → something unexpected went wrong
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScan(@PathVariable Long id) {
      try {
        // Check if the scan exists first
        // If it doesn't exist there's nothing to delete → 404
        if (scanService.getScanById(id).isEmpty()) {
          return ResponseEntity.notFound().build(); // 404
        }

        scanService.deleteScan(id);

        // 204 NO CONTENT is the correct response for DELETE
        // We deleted it successfully - there's nothing to return!
        return ResponseEntity.noContent().build(); // 204

      } catch (Exception e) {
        return ResponseEntity.status(500).build(); // 500
      }
  }
}