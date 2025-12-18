import { $ } from "../dom.js";
import { state } from "../state.js";
import { getRestaurantById } from "../api/restaurantService.js";

export async function renderGuestsSummary() {
  const r = await getRestaurantById(state.restaurantId);

  if ($("summaryGuestsTop")) {
    $("summaryGuestsTop").innerHTML = `
      <div><strong>${r?.name ?? "Restaurant"}</strong></div>
      <div class="muted">${state.dateISO} at ${state.time}</div>
    `;
  }

  if ($("guestValue")) $("guestValue").textContent = String(state.guests);
}

export function incrementGuests() {
  state.guests = Math.min(10, state.guests + 1);
  if ($("guestValue")) $("guestValue").textContent = String(state.guests);
}

export function decrementGuests() {
  state.guests = Math.max(1, state.guests - 1);
  if ($("guestValue")) $("guestValue").textContent = String(state.guests);
}