package com.example.scan_service.model;

import jakarta.persistence.*;

/**
 * This is our POJO - Plain Old Java Object.
 *
 * To add persistence we have addede JPA annotations to map
 * to a database table.
 *
 * The annotations used are:
 *  @Entity tells JPA this class maps to a database table
 *  @Table sets the table name in the database
 *  @Id marks the primary key
 *  @GeneratedValue tells the database to auto-generate IDs
 *    no more manual AtomicLong counter needed!
 *  @Column maps each field to a database column
 */

@Entity
@Table(name = "scan_requests")
public class ScanRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;              // Unique identifier for each scan
  @Column(name = "patient_id", nullable = false)
  private String patientId;     // The patient this scan belongs to
  @Column(name = "scan_type", nullable = false)
  private String scanType;      // What kind of scan - Chest, Head etc
  @Column(name = "scheduled_date")
  private String scheduledDate; // When the scan is scheduled
  @Column(name = "status")
  private String status = "PENDING";        // PENDING (default), COMPLETED, or CANCELLED

 //Our constructors, setters and getters stay the same
  public ScanRequest() {}


  public ScanRequest(Long id, String patientId, String scanType,
                     String scheduledDate, String status) {
    this.id = id;
    this.patientId = patientId;
    this.scanType = scanType;
    this.scheduledDate = scheduledDate;
    this.status = status;
  }


  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }


  public String getPatientId() {
    return patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  public String getScanType() { return scanType; }
  public void setScanType(String scanType) { this.scanType = scanType; }

  public String getScheduledDate() { return scheduledDate; }
  public void setScheduledDate(String scheduledDate) { this.scheduledDate = scheduledDate; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

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