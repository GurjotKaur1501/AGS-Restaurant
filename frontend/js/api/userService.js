// User Service API calls (Port 8081)
const USER_SERVICE_URL = "http://localhost:8081/api/users";

export async function getUserByEmail(email) {
  try {
    const response = await fetch(`${USER_SERVICE_URL}/email/${encodeURIComponent(email)}`);
    if (response.ok) {
      return await response.json();
    }
    return null; // User not found
  } catch (error) {
    console.error("Error fetching user by email:", error);
    return null;
  }
}

export async function createUser(userData) {
  try {
    const response = await fetch(USER_SERVICE_URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        firstName: userData.firstName || "Guest",
        lastName: userData.lastName || "User",
        email: userData.email,
        password: userData.password || "password123", // Default password
        phone: userData.phone || "",
        preferredLanguage: "en",
        notificationPreference: "email",
        marketingEmails: false
      })
    });

    if (!response.ok) {
      throw new Error(`Failed to create user: ${response.status}`);
    }

    return await response.json();
  } catch (error) {
    console.error("Error creating user:", error);
    throw error;
  }
}

export async function getUserById(userId) {
  try {
    const response = await fetch(`${USER_SERVICE_URL}/${userId}`);
    if (response.ok) {
      return await response.json();
    }
    return null;
  } catch (error) {
    console.error("Error fetching user by ID:", error);
    return null;
  }
}

// Get or create user by email
export async function getOrCreateUser(email) {
  // First try to get existing user
  let user = await getUserByEmail(email);
  
  // If user doesn't exist, create them
  if (!user) {
    console.log("User not found, creating new user...");
    user = await createUser({ email });
  }
  
  return user;
}