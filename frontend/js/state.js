// Global application state
export const state = {
  step: "home",
  restaurantId: null,
  dateISO: null, // YYYY-MM-DD
  time: null, // HH:mm
  guests: 2,
  email: "",
  userId: null, // Will be set after user lookup/creation
  tableId: null, // Will be set after availability check
  currentRestaurant: null, // Store full restaurant object
  bookingId: null, // Store booking ID after creation
  confirmationCode: null // Store confirmation code
};

// Helper to reset booking state
export function resetBookingState() {
  state.restaurantId = null;
  state.dateISO = null;
  state.time = null;
  state.guests = 2;
  state.email = "";
  state.userId = null;
  state.tableId = null;
  state.currentRestaurant = null;
  state.bookingId = null;
  state.confirmationCode = null;
}

// Helper to format date for display
export function formatPrettyDate(iso) {
  if (!iso) return "";
  const d = new Date(iso + "T00:00:00");
  return d.toLocaleDateString(undefined, { year: "numeric", month: "long", day: "numeric" });
}