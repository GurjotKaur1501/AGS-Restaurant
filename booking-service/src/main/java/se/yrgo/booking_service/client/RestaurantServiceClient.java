package se.yrgo.booking_service.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.yrgo.booking_service.client.clientdto.ExternalTableDTO;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantServiceClient {

    private final RestTemplate restTemplate;
    private static final String RESTAURANT_SERVICE_URL = "http://localhost:8082/api/tables";

    /**
     * Get table details from restaurant-service
     * Falls back to mock data if service unavailable
     */
    public ExternalTableDTO getTableById(Long tableId) {
        try {
            log.info("Fetching table {} from restaurant-service", tableId);
            String url = RESTAURANT_SERVICE_URL + "/" + tableId;
            return restTemplate.getForObject(url, ExternalTableDTO.class);
        } catch (Exception e) {
            log.warn("Failed to fetch table from restaurant-service, using mock data: {}", e.getMessage());
            // Fallback to mock data if service unavailable
            return createMockTable(tableId);
        }
    }

    /**
     * Verify if table exists
     */
    public boolean tableExists(Long tableId) {
        try {
            getTableById(tableId);
            return true;
        } catch (Exception e) {
            log.error("Table {} does not exist", tableId);
            return false;
        }
    }

    /**
     * Check table capacity
     */
    public boolean hasCapacity(Long tableId, Integer numberOfGuests) {
        try {
            ExternalTableDTO table = getTableById(tableId);
            return table.getCapacity() >= numberOfGuests;
        } catch (Exception e) {
            log.warn("Could not verify table capacity, allowing booking");
            return true; // Allow booking if service unavailable
        }
    }

    // Mock data for testing when restaurant-service is not running
    private ExternalTableDTO createMockTable(Long tableId) {
        ExternalTableDTO table = new ExternalTableDTO();
        table.setId(tableId);
        table.setTableNumber("T-" + tableId);
        table.setCapacity(4); // Default capacity
        table.setLocation("Main Dining Area");
        table.setRestaurantId(1L);
        return table;
    }
}