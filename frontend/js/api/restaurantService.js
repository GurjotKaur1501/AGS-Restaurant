// Restaurant Service API calls (Port 8082)
const RESTAURANT_SERVICE_URL = "http://localhost:8082/api/restaurants";
const TABLE_SERVICE_URL = "http://localhost:8082/api/tables";

/**
 * CORRECTED VERSION - No demo data fallback
 * This ensures we're testing real microservice integration
 */

export async function getRestaurants() {
  try {
    const response = await fetch(RESTAURANT_SERVICE_URL);
    if (!response.ok) {
      throw new Error(`Failed to fetch restaurants: ${response.status} ${response.statusText}`);
    }
    const data = await response.json();
    console.log("✅ Fetched restaurants from API:", data);
    return data;
  } catch (error) {
    console.error("❌ Error fetching restaurants:", error);
    // Instead of falling back to demo data, throw error
    throw new Error(`Restaurant service unavailable: ${error.message}. Please ensure the restaurant service is running on port 8082.`);
  }
}

export async function getRestaurantById(id) {
  try {
    const response = await fetch(`${RESTAURANT_SERVICE_URL}/${id}`);
    if (!response.ok) {
      throw new Error(`Failed to fetch restaurant ${id}: ${response.status}`);
    }
    const data = await response.json();
    console.log("✅ Fetched restaurant from API:", data);
    return data;
  } catch (error) {
    console.error("❌ Error fetching restaurant:", error);
    throw new Error(`Failed to load restaurant details: ${error.message}`);
  }
}

export async function getTablesByRestaurantId(restaurantId) {
  try {
    const response = await fetch(`${TABLE_SERVICE_URL}/restaurant/${restaurantId}`);
    if (!response.ok) {
      throw new Error(`Failed to fetch tables: ${response.status}`);
    }
    const tables = await response.json();
    console.log("✅ Fetched tables from API:", tables);
    return tables;
  } catch (error) {
    console.error("❌ Error fetching tables:", error);
    throw new Error(`Failed to load tables: ${error.message}`);
  }
}

/**
 * Get available tables for a specific time and party size
 * This uses the backend allocation strategy
 */
export async function getAvailableTable(restaurantId, partySize, strategy = "firstFit") {
  try {
    const response = await fetch(
      `${TABLE_SERVICE_URL}/allocate?restaurantId=${restaurantId}&partySize=${partySize}&strategy=${strategy}`,
      { method: "POST" }
    );
    
    if (!response.ok) {
      const error = await response.json().catch(() => ({}));
      throw new Error(error.message || `No available tables for party size ${partySize}`);
    }
    
    const table = await response.json();
    console.log("✅ Allocated table from backend:", table);
    return table;
  } catch (error) {
    console.error("❌ Error allocating table:", error);
    throw error;
  }
}

export async function createRestaurant(restaurantData) {
  try {
    const response = await fetch(RESTAURANT_SERVICE_URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(restaurantData)
    });

    if (!response.ok) {
      throw new Error(`Failed to create restaurant: ${response.status}`);
    }

    return await response.json();
  } catch (error) {
    console.error("❌ Error creating restaurant:", error);
    throw error;
  }
}

/**
 * Create a table for a restaurant
 */
export async function createTable(tableData) {
  try {
    const response = await fetch(TABLE_SERVICE_URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(tableData)
    });

    if (!response.ok) {
      throw new Error(`Failed to create table: ${response.status}`);
    }

    return await response.json();
  } catch (error) {
    console.error("❌ Error creating table:", error);
    throw error;
  }
}