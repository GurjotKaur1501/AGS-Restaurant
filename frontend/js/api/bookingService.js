// Booking Service API calls (Port 8083)
const BOOKING_SERVICE_URL = "http://localhost:8083/api/bookings";

/**
 * CORRECTED VERSION - Real availability checking from backend
 */

/**
 * Check if a table is available at a specific time
 * This queries the booking service for existing bookings
 */
export async function checkTableAvailability(tableId, bookingTime) {
  try {
    // Query existing bookings for this table around the requested time
    const timeWindow = 2; // hours
    const startTime = new Date(bookingTime);
    startTime.setHours(startTime.getHours() - timeWindow);
    const endTime = new Date(bookingTime);
    endTime.setHours(endTime.getHours() + timeWindow);

    const response = await fetch(`${BOOKING_SERVICE_URL}`);
    if (!response.ok) {
      throw new Error(`Failed to check availability: ${response.status}`);
    }

    const allBookings = await response.json();
    
    // Filter bookings for this table within time window
    const conflicts = allBookings.filter(booking => {
      if (booking.tableId !== tableId) return false;
      if (booking.status === "CANCELLED") return false;
      
      const bookingDate = new Date(booking.bookingTime);
      return bookingDate >= startTime && bookingDate <= endTime;
    });

    const available = conflicts.length === 0;
    console.log(`✅ Table ${tableId} availability at ${bookingTime}:`, available);
    return { available, conflicts };
  } catch (error) {
    console.error("❌ Error checking availability:", error);
    throw error;
  }
}

/**
 * Get availability for a restaurant on a specific date
 * Returns which time slots are unavailable
 */
export async function getRestaurantAvailability(restaurantId, dateISO, partySize) {
  try {
    // Fetch all bookings
    const response = await fetch(`${BOOKING_SERVICE_URL}`);
    if (!response.ok) {
      throw new Error(`Failed to fetch bookings: ${response.status}`);
    }

    const allBookings = await response.json();
    
    // Filter bookings for this date
    const dateBookings = allBookings.filter(booking => {
      if (booking.status === "CANCELLED") return false;
      const bookingDate = new Date(booking.bookingTime);
      const targetDate = new Date(dateISO);
      return bookingDate.toDateString() === targetDate.toDateString();
    });

    // Extract booked time slots
    const bookedSlots = dateBookings.map(booking => {
      const time = new Date(booking.bookingTime);
      return `${String(time.getHours()).padStart(2, '0')}:${String(time.getMinutes()).padStart(2, '0')}`;
    });

    console.log(`✅ Restaurant ${restaurantId} bookings on ${dateISO}:`, bookedSlots);
    
    return {
      unavailableTimes: [...new Set(bookedSlots)], // Remove duplicates
      bookedCount: dateBookings.length
    };
  } catch (error) {
    console.error("❌ Error fetching restaurant availability:", error);
    // Return empty unavailable times instead of throwing
    // This allows booking to proceed if availability check fails
    return { unavailableTimes: [], bookedCount: 0 };
  }
}

/**
 * Create a new booking
 */
export async function createBooking(bookingData) {
  try {
    console.log("📝 Creating booking with data:", bookingData);
    
    const response = await fetch(BOOKING_SERVICE_URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        userId: bookingData.userId,
        tableId: bookingData.tableId,
        bookingTime: bookingData.bookingTime, // ISO 8601 format: YYYY-MM-DDTHH:mm:ss
        numberOfGuests: bookingData.numberOfGuests,
        specialRequests: bookingData.specialRequests || ""
      })
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({ message: "Unknown error" }));
      throw new Error(errorData.message || `Failed to create booking: ${response.status}`);
    }

    const result = await response.json();
    console.log("✅ Booking created successfully:", result);
    return result;
  } catch (error) {
    console.error("❌ Error creating booking:", error);
    throw error;
  }
}

/**
 * Get a booking by ID
 */
export async function getBookingById(bookingId) {
  try {
    const response = await fetch(`${BOOKING_SERVICE_URL}/${bookingId}`);
    if (!response.ok) {
      throw new Error(`Booking not found: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    console.error("❌ Error fetching booking:", error);
    throw error;
  }
}

/**
 * Get all bookings for a user
 */
export async function getBookingsByUserId(userId) {
  try {
    const response = await fetch(`${BOOKING_SERVICE_URL}/user/${userId}`);
    if (!response.ok) {
      throw new Error(`Failed to fetch user bookings: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    console.error("❌ Error fetching user bookings:", error);
    return [];
  }
}

/**
 * Cancel a booking
 */
export async function cancelBooking(bookingId) {
  try {
    const response = await fetch(`${BOOKING_SERVICE_URL}/${bookingId}/cancel`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      }
    });

    if (!response.ok) {
      throw new Error(`Failed to cancel booking: ${response.status}`);
    }

    return await response.json();
  } catch (error) {
    console.error("❌ Error canceling booking:", error);
    throw error;
  }
}

/**
 * Get all bookings (admin function)
 */
export async function getAllBookings() {
  try {
    const response = await fetch(BOOKING_SERVICE_URL);
    if (!response.ok) {
      throw new Error(`Failed to fetch bookings: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    console.error("❌ Error fetching all bookings:", error);
    return [];
  }
}