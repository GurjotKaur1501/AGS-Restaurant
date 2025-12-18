import { $ } from "../dom.js";
import { state } from "../state.js";
import { getRestaurants } from "../api/restaurantService.js";
import { showStep } from "../router.js";
import { renderRestaurantDetails } from "./restaurantDetails.js";

/**
 * CORRECTED VERSION - Maintains original simple structure with added error handling
 */

let restaurants = [];

export async function renderRestaurantList() {
  const list = $("restaurantList");
  if (!list) return;

  // Show loading state
  list.innerHTML = '<div class="muted" style="padding: 20px; text-align: center;">Loading restaurants...</div>';

  try {
    // Fetch restaurants from API
    console.log("🔍 Fetching restaurants from backend...");
    restaurants = await getRestaurants();
    console.log("✅ Loaded", restaurants.length, "restaurants");

    // Clear loading message
    list.innerHTML = "";

    if (restaurants.length === 0) {
      list.innerHTML = `
        <div class="panel" style="text-align: center; padding: 30px;">
          <p class="muted">No restaurants available.</p>
          <p style="font-size: 13px; margin-top: 10px;">
            Add restaurants via: <code>POST http://localhost:8082/api/restaurants</code>
          </p>
        </div>
      `;
      return;
    }

    // Render each restaurant - KEEP IT SIMPLE like original!
    restaurants.forEach((r) => {
      const card = document.createElement("div");
      card.className = "restaurant-card";
      card.dataset.id = r.id;

      // Handle both API format and demo format
      const imgUrl = r.images ? r.images[0] : (r.img || "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=400");
      const name = r.name || "Unnamed Restaurant";
      const address = r.address || "";
      const cuisine = r.cuisineType || r.cuisine || "International";
      const avgPrice = r.avgPrice || "N/A";

      card.innerHTML = `
        <img class="restaurant-img" src="${imgUrl}" alt="${name}" />
        <div>
          <div class="restaurant-name">${name}</div>
          <div class="restaurant-meta">${address || cuisine + ' • ' + avgPrice}</div>
        </div>
        <button class="btn btn-primary btn-sm" type="button">Select</button>
      `;

      // THIS IS THE KEY - Add click handler AFTER innerHTML is set, BEFORE appending
      card.addEventListener("click", async () => {
        console.log("🎯 Restaurant clicked:", r.name);
        
        state.restaurantId = r.id;
        state.currentRestaurant = r;

        // Remove selection from all cards
        [...list.children].forEach((c) => c.classList.remove("selected"));
        card.classList.add("selected");

        // Navigate to restaurant details page
        showStep("restaurant");

        // Render details for the selected restaurant
        await renderRestaurantDetails();
      });

      list.appendChild(card);
    });

    console.log("✅ Restaurant list rendered successfully");

  } catch (error) {
    console.error("❌ Failed to load restaurants:", error);
    
    // Show error with retry button
    list.innerHTML = `
      <div class="panel" style="padding: 30px; text-align: center;">
        <h3 style="color: #dc2626; margin-bottom: 10px;">⚠️ Cannot Load Restaurants</h3>
        <p class="muted" style="margin-bottom: 20px;">${error.message}</p>
        
        <div style="background: #fef3c7; border: 1px solid #fcd34d; border-radius: 12px; padding: 16px; text-align: left; font-size: 13px;">
          <strong>Check:</strong>
          <ul style="margin: 8px 0 0 20px; padding: 0;">
            <li>Restaurant Service running on port 8082</li>
            <li>Test: <code>curl http://localhost:8082/api/restaurants</code></li>
          </ul>
        </div>
        
        <button class="btn btn-primary" style="margin-top: 20px;" id="retryButton">
          Retry
        </button>
      </div>
    `;
    
    // Add event listener to retry button AFTER it's created
    setTimeout(() => {
      const retryBtn = $("retryButton");
      if (retryBtn) {
        retryBtn.addEventListener("click", () => {
          renderRestaurantList();
        });
      }
    }, 0);
  }
}

export function getRestaurantById(id) {
  return restaurants.find((r) => r.id === id) || null;
}