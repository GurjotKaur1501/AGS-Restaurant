import { state, resetBookingState } from "./state.js";
import { $, on } from "./dom.js";
import { showStep } from "./router.js";
import { renderRestaurantList } from "./components/restaurantList.js";
import { renderRestaurantDetails } from "./components/restaurantDetails.js";
import { initCalendar, prevMonth, nextMonth } from "./components/calendar.js";
import { renderTimeSlots } from "./components/timeSlots.js";
import { renderGuestsSummary, incrementGuests, decrementGuests } from "./components/guests.js";
import { renderConfirmation, confirmBooking } from "./components/emailConfirm.js";
import { setupModalHandlers } from "./components/modal.js";

// Initialize application
async function init() {
  console.log("🚀 Restaurant Booking System Initializing...");
  
  // Setup modal handlers
  setupModalHandlers();
  
  // Initialize calendar
  initCalendar();

  // Wire up navigation
  setupNavigation();
  
  // Start on home page
  showStep("home");
  
  console.log("✅ Application initialized successfully");
}

function setupNavigation() {
  // Home -> Restaurants
  on("startBooking", "click", async () => {
    showStep("restaurants");
    await renderRestaurantList();
  });

  // Header Home -> Landing
  on("navHome", "click", (e) => {
    e.preventDefault();
    resetBookingState();
    showStep("home");
  });

  // Restaurants -> Restaurant Details (happens in restaurantList component)
  
  // Restaurant Details -> Back to Restaurants
  on("backToRestaurantList", "click", () => {
    showStep("restaurants");
  });

  // Restaurant Details -> Time Selection
  on("toTimeFromRestaurant", "click", async () => {
    if (!state.dateISO) {
      alert("Please select a date first");
      return;
    }
    showStep("time");
    await renderTimeSlots();
  });

  // Calendar navigation
  on("prevMonth", "click", prevMonth);
  on("nextMonth", "click", nextMonth);

  // Time -> Back to Restaurant Details
  on("backToDate", "click", async () => {
    showStep("restaurant");
    await renderRestaurantDetails();
  });

  // Time -> Guests
  on("toGuests", "click", async () => {
    if (!state.time) {
      alert("Please select a time first");
      return;
    }
    showStep("guests");
    await renderGuestsSummary();
  });

  // Guests -> Back to Time
  on("backToTime", "click", async () => {
    showStep("time");
    await renderTimeSlots();
  });

  // Guests +/-
  on("guestMinus", "click", () => {
    decrementGuests();
  });

  on("guestPlus", "click", () => {
    incrementGuests();
  });

  // Guests -> Email
  on("toEmail", "click", async () => {
    showStep("email");
    await renderConfirmation();
    if ($("confirmBooking")) $("confirmBooking").disabled = true;
    if ($("toast")) $("toast").classList.add("hidden");
  });

  // Email -> Back to Guests
  on("backToGuests", "click", async () => {
    showStep("guests");
    await renderGuestsSummary();
  });

  // Email validation
  on("emailInput", "input", (e) => {
    state.email = e.target.value.trim();
    const valid = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(state.email);
    if ($("confirmBooking")) $("confirmBooking").disabled = !valid;
  });

  // Confirm booking
  on("confirmBooking", "click", async () => {
    await confirmBooking();
  });
}

// Start the application when DOM is ready
if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", init);
} else {
  init();
}