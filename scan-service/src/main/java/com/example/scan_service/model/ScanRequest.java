package com.example.scan_service.model;

/**
 * This is our POJO - Plain Old Java Object.
 *
 * A POJO is a simple Java class that represents our data.
 * Compare this to Stage 1 where we stored scan data as
 * raw String arrays like: String[] scan = {id, patientId, scanType...}
 *
 * Problems with the Stage 1 approach:
 *  - scan[0] means nothing, you have to remember what index is what
 *  - No type safety - everything was a String, even the ID
 *  - Easy to make mistakes - wrong index = wrong data
 *
 * With a POJO:
 *  - Each field has a name - scan.getPatientId() is clear and readable
 *  - Each field has a proper type - id is a Long, not a String
 *  - Private fields means nobody can accidentally change the data
 *    without going through our getters and setters (encapsulation!)
 *
 * This class also gets automatically converted to JSON by Spring Boot
 * when we return it from our controller - that's serialization!
 * When JSON comes IN from a request, it gets converted back to this
 * object automatically - that's deserialization!
 */
public class ScanRequest {

  // Private fields - only accessible through getters and setters (Encapsulation)
  private Long id;              // Unique identifier for each scan
  private String patientId;     // The patient this scan belongs to
  private String scanType;      // What kind of scan - Chest, Head etc
  private String scheduledDate; // When the scan is scheduled
  private String status;        // PENDING, COMPLETED, or CANCELLED

  // No-arg constructor
  // Jackson (Spring's JSON library) REQUIRES this to convert
  // incoming JSON into a ScanRequest object (deserialization)
  public ScanRequest() {}

  // Full constructor - convenient way to create a scan with all fields
  public ScanRequest(Long id, String patientId, String scanType,
                     String scheduledDate, String status) {
    this.id = id;
    this.patientId = patientId;
    this.scanType = scanType;
    this.scheduledDate = scheduledDate;
    this.status = status;
  }

  // Getters and Setters
  // Instead of scan[0], scan[1] we now use
  // scan.getId(), scan.getPatientId() - much more readable!
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  // PUBLIC getter - controlled READ access to the private attribute
  public String getPatientId() {
    return patientId;
  }
  // PUBLIC setter - controlled WRITE access to the private attribute
  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  public String getScanType() { return scanType; }
  public void setScanType(String scanType) { this.scanType = scanType; }

  public String getScheduledDate() { return scheduledDate; }
  public void setScheduledDate(String scheduledDate) { this.scheduledDate = scheduledDate; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  // toString - useful for logging and debugging
  // When you print a ScanRequest object this is what shows up
  @Override
  public String toString() {
    return "ScanRequest{" +
            "id=" + id +
            ", patientId='" + patientId + '\'' +
            ", scanType='" + scanType + '\'' +
            ", scheduledDate='" + scheduledDate + '\'' +
            ", status='" + status + '\'' +
            '}';
  }
}