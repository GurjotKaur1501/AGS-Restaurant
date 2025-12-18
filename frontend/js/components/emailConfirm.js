import { $ } from "../dom.js";
import { state } from "../state.js";
import { getRestaurantById } from "../api/restaurantService.js";
import { createBooking } from "../api/bookingService.js";
import { getOrCreateUser } from "../api/userService.js";
import { getAvailableTable } from "../api/restaurantService.js";

/**
 * CORRECTED VERSION v2 - Resets button state on render
 */

export async function renderConfirmation() {
  try {
    const r = await getRestaurantById(state.restaurantId);

    if ($("confirmImg")) {
      const imgUrl = r?.images?.[0] || "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=400";
      $("confirmImg").src = imgUrl;
    }
    
    if ($("confirmName")) $("confirmName").textContent = r?.name ?? "Restaurant";
    
    if ($("confirmDetail")) {
      $("confirmDetail").textContent = `${state.dateISO} at ${state.time} • ${state.guests} guest(s)`;
    }

    // FIX: Reset button to default state when rendering
    // This ensures the button is fresh for each new booking
    if ($("confirmBooking")) {
      $("confirmBooking").textContent = "Confirm Booking";
      $("confirmBooking").disabled = true; // Will be enabled when email is valid
    }

    // Hide any previous toast messages
    if ($("toast")) {
      $("toast").classList.add("hidden");
    }

    // Clear email input
    if ($("emailInput")) {
      $("emailInput").value = state.email || "";
    }

  } catch (error) {
    console.error("❌ Error rendering confirmation:", error);
    setToast(`Failed to load restaurant details: ${error.message}`, "error");
  }
}

export async function confirmBooking() {
  try {
    setToast("🔄 Creating your booking...", "info");
    
    // Step 1: Get or create user
    console.log("Step 1: Getting/creating user...");
    const user = await getOrCreateUser(state.email);
    if (!user || !user.id) {
      throw new Error("Failed to create user account");
    }
    state.userId = user.id;
    console.log("✅ User ready:", user);

    // Step 2: Use backend to allocate appropriate table
    console.log("Step 2: Allocating table using backend strategy...");
    try {
      const allocatedTable = await getAvailableTable(
        state.restaurantId,
        state.guests,
        "bestFit"
      );
      
      if (!allocatedTable || !allocatedTable.id) {
        throw new Error("No suitable table available for your party size");
      }
      
      state.tableId = allocatedTable.id;
      console.log("✅ Backend allocated table:", allocatedTable);
      
    } catch (tableError) {
      throw new Error(`Table allocation failed: ${tableError.message}. The restaurant may be fully booked at this time.`);
    }

    // Step 3: Create booking time in ISO 8601 format
    const bookingDateTime = `${state.dateISO}T${state.time}:00`;
    console.log("Step 3: Booking time:", bookingDateTime);

    // Validate booking time is in the future
    const bookingDate = new Date(bookingDateTime);
    if (bookingDate <= new Date()) {
      throw new Error("Booking time must be in the future");
    }

    // Step 4: Create booking via booking service
    console.log("Step 4: Creating booking...");
    const bookingData = {
      userId: state.userId,
      tableId: state.tableId,
      bookingTime: bookingDateTime,
      numberOfGuests: state.guests,
      specialRequests: ""
    };

    const result = await createBooking(bookingData);
    console.log("✅ Booking created:", result);

    // Store booking details (handle both response formats)
    if (result.booking) {
      state.bookingId = result.booking.id;
      state.confirmationCode = result.booking.confirmationCode;
    } else {
      state.bookingId = result.id;
      state.confirmationCode = result.confirmationCode;
    }

    // Show success message with confirmation code
    const confirmationCode = state.confirmationCode || "PENDING";
    setToast(
      `✅ Booking confirmed! Your confirmation code is: ${confirmationCode}. A confirmation email will be sent to ${state.email}.`,
      "success"
    );

    // Update button after successful booking
    if ($("confirmBooking")) {
      $("confirmBooking").disabled = true;
      $("confirmBooking").textContent = "✓ Booking Confirmed!";
    }

    // Log full booking details
    console.log("📋 Booking Summary:", {
      bookingId: state.bookingId,
      confirmationCode: state.confirmationCode,
      userId: state.userId,
      tableId: state.tableId,
      restaurantId: state.restaurantId,
      dateTime: bookingDateTime,
      guests: state.guests
    });

  } catch (error) {
    console.error("❌ Booking failed:", error);
    
    let errorMessage = error.message;
    
    if (error.message.includes("Failed to fetch") || error.message.includes("NetworkError")) {
      errorMessage = "Cannot connect to booking service. Please ensure all services are running.";
    } else if (error.message.includes("409")) {
      errorMessage = "This time slot is no longer available. Please select a different time.";
    } else if (error.message.includes("400")) {
      errorMessage = "Invalid booking data. Please check your selection and try again.";
    }
    
    setToast(`❌ Booking failed: ${errorMessage}`, "error");
    
    // Re-enable button so user can try again
    if ($("confirmBooking")) {
      $("confirmBooking").disabled = false;
      $("confirmBooking").textContent = "Confirm Booking";
    }
  }
}

export function setToast(msg, type = "info") {
  const t = $("toast");
  if (!t) return;
  
  t.textContent = msg;
  t.classList.remove("hidden");
  
  const styles = {
    error: {
      background: "rgba(220, 38, 38, 0.14)",
      borderColor: "rgba(220, 38, 38, 0.30)"
    },
    success: {
      background: "rgba(34, 197, 94, 0.14)",
      borderColor: "rgba(34, 197, 94, 0.30)"
    },
    info: {
      background: "rgba(59, 130, 246, 0.14)",
      borderColor: "rgba(59, 130, 246, 0.30)"
    }
  };
  
  const style = styles[type] || styles.info;
  t.style.background = style.background;
  t.style.borderColor = style.borderColor;
  
  // Auto-hide success messages after 5 seconds
  if (type === "success") {
    setTimeout(() => {
      t.classList.add("hidden");
    }, 5000);
  }
}