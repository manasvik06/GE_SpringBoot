import java.util.ArrayList;
import java.util.List;

/**
 * STAGE 1 : A monolithic Java Application 
 *
 * A list of problems
 *   - Everything is in ONE class (no separation of concerns)
 *   - Data is stored in a plain ArrayList (lost when app restarts)
 *   - No REST API (you can still talk to other services, but you will have to manually set up a HTTPServer etc, etc)
 *   - Hardcoded logic mixed with data — hard to maintain or scale
 *
 **/
 
public class ScanSystem {

    //This is how we are storing the data, in memory! When our program stops/restarts all of 
    //our data is lost (Problem)
    private static List<String[]> scanRequests = new ArrayList<>();

    //Simple counter to simulate IDs which will reset every run, so can never really store a record (Problem)
    private static int idCounter = 1;

    public static void main(String[] args) {

        //dummy data for scan requests
        createScanRequest("P001", "Chest CT", "2024-06-01");
        createScanRequest("P002", "Head CT",  "2024-06-02");
        createScanRequest("P003", "Abdomen CT", "2024-06-03");

        //some methods being implemented
        //Getting all scans
        System.out.println("=== All Scan Requests ===");
        getAllScans();

        //Getting a particular scan by an ID
        System.out.println("\n=== Get Scan by ID: 2 ===");
        getScanById(2);

        //Updating a particular scan
        System.out.println("\n=== Updating Scan ID 1 to COMPLETED ===");
        updateScanStatus(1, "COMPLETED");
        getScanById(1);
    }

    /**
     * Creates a new scan request and adds it to the in-memory list.
     *
     * PROBLEM: Data is stored as a raw String array — no type safety,
     * no validation, easy to make mistakes (e.g. wrong index).
     * Everything is bundled here with no separation of logic.
     */
    public static void createScanRequest(String patientId, String scanType, String scheduledDate) {
        // Store as a raw String array: [id, patientId, scanType, scheduledDate, status]
        // This is fragile — the developer has to remember what every address means
        String[] scan = {
            String.valueOf(idCounter++),  // index 0: ID
            patientId,                    // index 1: Patient ID
            scanType,                     // index 2: Scan Type
            scheduledDate,                // index 3: Scheduled Date
            "PENDING"                     // index 4: Status (hardcoded default)
        };

        scanRequests.add(scan);
        System.out.println("Created scan request for patient: " + patientId);
    }

    /**
     * Prints all scan requests to the console.
     *
     * PROBLEM: No API — output just goes to System.out.
     * Other services or frontends can't consume this data.
     */
    public static void getAllScans() {
        if (scanRequests.isEmpty()) {
            System.out.println("No scan requests found.");
            return;
        }

        // Loop through the list and print each scan
        for (String[] scan : scanRequests) {
            printScan(scan);
        }
    }

    /**
     * Finds a scan by its ID.
     *
     * PROBLEM: Manual loop through a list — no query language,
     * no indexing. Slow and error-prone for large datasets.
     */
    public static void getScanById(int id) {
        for (String[] scan : scanRequests) {
            // Remember: index 0 is the ID
            if (Integer.parseInt(scan[0]) == id) {
                printScan(scan);
                return;
            }
        }
        System.out.println("Scan not found for ID: " + id);
    }

    /**
     * Updates the status of a scan request.
     *
     * PROBLEM: Direct array mutation — no validation,
     * no audit trail, no error handling.
     */
    public static void updateScanStatus(int id, String newStatus) {
        for (String[] scan : scanRequests) {
            if (Integer.parseInt(scan[0]) == id) {
                scan[4] = newStatus; // index 4 is status — easy to get wrong!
                System.out.println("Updated scan ID " + id + " to status: " + newStatus);
                return;
            }
        }
        System.out.println("Scan not found for ID: " + id);
    }

    /**
     * Helper to print a scan array.
     *
     * PROBLEM: Using raw array indices everywhere is fragile.
     * A proper POJO with named fields would be much cleaner.
     */
    private static void printScan(String[] scan) {
        System.out.println(
            "ID: "       + scan[0] +
            " | Patient: " + scan[1] +
            " | Type: "    + scan[2] +
            " | Date: "    + scan[3] +
            " | Status: "  + scan[4]
        );
    }
}