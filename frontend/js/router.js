import { state } from "./state.js";
import { $ } from "./dom.js";

const steps = ["home", "restaurants", "restaurant", "time", "guests", "email"];

const titles = {
  home: ["Table Hub", "Fast and seamless restaurant booking experience."],
  restaurants: ["Select a Restaurant", "Choose a restaurant to continue your booking."],
  restaurant: ["Restaurant Details", "Explore details and book a table."],
  time: ["Select a Time", "Choose an available time slot (24-hour format)."],
  guests: ["How many guests?", "Select party size (1–10)."],
  email: ["Enter your email to receive confirmation", "We will send the confirmation to your email."]
};

export function showStep(stepId) {
  // Hide all steps
  steps.forEach((s) => {
    const el = $("step-" + s);
    if (el) el.classList.add("hidden");
  });

  // Show target step
  const target = $("step-" + stepId);
  if (target) target.classList.remove("hidden");

  state.step = stepId;

  // Update header (only for non-restaurant-details pages)
  const wizardHeader = $("wizardHeader");
  if (wizardHeader && stepId !== "restaurant") {
    wizardHeader.classList.remove("hidden");
  } else if (wizardHeader && stepId === "restaurant") {
    wizardHeader.classList.add("hidden");
  }

  // Update title and subtitle
  if ($("screenTitle") && $("screenSubtitle") && titles[stepId] && stepId !== "restaurant") {
    $("screenTitle").textContent = titles[stepId][0];
    $("screenSubtitle").textContent = titles[stepId][1];
  }
}