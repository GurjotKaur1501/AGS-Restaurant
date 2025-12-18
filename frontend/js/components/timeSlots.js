import { $ } from "../dom.js";
import { state } from "../state.js";
import { getRestaurantAvailability } from "../api/bookingService.js";
import { getRestaurantById } from "../api/restaurantService.js";

/**
 * CORRECTED VERSION v2 - Bug fixes applied
 */

const ALL_LUNCH_SLOTS = ["11:00", "11:30", "12:00", "12:30", "13:00", "13:30"];
const ALL_DINNER_SLOTS = ["17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00"];

export async function renderTimeSlots() {
  if (!$("lunchSlots") || !$("dinnerSlots")) return;

  // Clear previous slots
  $("lunchSlots").innerHTML = "";
  $("dinnerSlots").innerHTML = "";

  try {
    // Fetch real availability from booking service
    const availability = await getRestaurantAvailability(
      state.restaurantId,
      state.dateISO,
      state.guests
    );

    console.log("✅ Availability loaded:", availability);
    
    const unavailableSet = new Set(availability?.unavailableTimes || []);

    // Render lunch slots
    ALL_LUNCH_SLOTS.forEach((time) => {
      const slot = createTimeSlot(time, unavailableSet.has(time));
      $("lunchSlots").appendChild(slot);
    });

    // Render dinner slots
    ALL_DINNER_SLOTS.forEach((time) => {
      const slot = createTimeSlot(time, unavailableSet.has(time));
      $("dinnerSlots").appendChild(slot);
    });

  } catch (error) {
    console.error("❌ Error fetching availability:", error);
    
    // Show error message
    const errorDiv = document.createElement("div");
    errorDiv.className = "muted";
    errorDiv.style.padding = "12px";
    errorDiv.style.marginBottom = "12px";
    errorDiv.textContent = `Cannot load availability: ${error.message}. Showing all time slots.`;
    $("lunchSlots").appendChild(errorDiv);
    
    // Fallback: show all slots as available
    // FIXED BUG: Previously was appending lunch to dinner container!
    ALL_LUNCH_SLOTS.forEach((time) => {
      const slot = createTimeSlot(time, false);
      $("lunchSlots").appendChild(slot);  // ✅ FIXED: was $("dinnerSlots")
    });
    
    ALL_DINNER_SLOTS.forEach((time) => {
      const slot = createTimeSlot(time, false);
      $("dinnerSlots").appendChild(slot);
    });
  }

  // Update summary
  await updateSummary();
}

function createTimeSlot(time, isUnavailable) {
  const button = document.createElement("button");
  button.type = "button";
  button.className = "slot";
  button.textContent = time;

  if (isUnavailable) {
    button.classList.add("unavailable");
    button.disabled = true;
    button.title = "This time slot is already booked";
  }

  if (state.time === time) {
    button.classList.add("selected");
  }

  button.addEventListener("click", () => {
    if (button.disabled) return;
    
    state.time = time;
    console.log("✅ Selected time:", time);
    
    // Enable continue button
    if ($("toGuests")) {
      $("toGuests").disabled = false;
    }
    
    // Re-render to update selection
    renderTimeSlots();
  });

  return button;
}

async function updateSummary() {
  try {
    const restaurant = await getRestaurantById(state.restaurantId);
    
    if ($("summaryTimeTop")) {
      $("summaryTimeTop").innerHTML = `
        <div>
          <strong>${restaurant?.name ?? "Restaurant"}</strong>
          <div class="muted" style="font-size: 12px; margin-top: 2px;">
            ${state.guests} guest(s)
          </div>
        </div>
        <div class="muted" style="text-align: right;">
          ${formatDate(state.dateISO)}
        </div>
      `;
    }
  } catch (error) {
    console.error("❌ Error updating summary:", error);
    // Don't throw - just log it
  }
}

function formatDate(isoDate) {
  if (!isoDate) return "";
  const date = new Date(isoDate + "T00:00:00");
  return date.toLocaleDateString(undefined, {
    weekday: 'short',
    month: 'short',
    day: 'numeric'
  });
}