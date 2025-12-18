import { $ } from "../dom.js";
import { state, formatPrettyDate } from "../state.js";

let calYear, calMonth;

export function initCalendar() {
  const now = new Date();
  calYear = now.getFullYear();
  calMonth = now.getMonth();
  renderCalendar();
}

export function prevMonth() {
  calMonth -= 1;
  if (calMonth < 0) {
    calMonth = 11;
    calYear -= 1;
  }
  renderCalendar();
}

export function nextMonth() {
  calMonth += 1;
  if (calMonth > 11) {
    calMonth = 0;
    calYear += 1;
  }
  renderCalendar();
}

export function renderCalendar() {
  if (!$("calMonthLabel") || !$("calGrid")) return;

  $("calMonthLabel").textContent = new Date(calYear, calMonth, 1).toLocaleDateString(undefined, {
    month: "long",
    year: "numeric"
  });

  const grid = $("calGrid");
  grid.innerHTML = "";

  const dows = ["SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"];
  dows.forEach((d) => {
    const el = document.createElement("div");
    el.className = "dow";
    el.textContent = d;
    grid.appendChild(el);
  });

  const first = new Date(calYear, calMonth, 1);
  const startDow = first.getDay();
  const daysInMonth = new Date(calYear, calMonth + 1, 0).getDate();

  for (let i = 0; i < startDow; i++) {
    const blank = document.createElement("div");
    blank.style.height = "40px";
    grid.appendChild(blank);
  }

  // FIX: Get today's date without timezone issues
  const today = new Date();
  const todayISO = formatDateToISO(today.getFullYear(), today.getMonth(), today.getDate());

  for (let day = 1; day <= daysInMonth; day++) {
    // FIX: Create ISO date string without timezone conversion
    const dateISO = formatDateToISO(calYear, calMonth, day);

    const btn = document.createElement("button");
    btn.type = "button";
    btn.className = "day";
    btn.textContent = String(day);

    if (dateISO < todayISO) btn.classList.add("disabled");
    if (state.dateISO === dateISO) btn.classList.add("selected");

    btn.addEventListener("click", () => {
      if (btn.classList.contains("disabled")) return;

      state.dateISO = dateISO;
      state.time = null;

      console.log(`✅ Date selected: ${dateISO} (Day ${day})`);

      if ($("calPickedLabel")) {
        $("calPickedLabel").textContent = formatPrettyDate(dateISO);
      }

      // Enable "Välj tid" button
      if ($("toTimeFromRestaurant")) {
        $("toTimeFromRestaurant").disabled = false;
      }

      renderCalendar();
    });

    grid.appendChild(btn);
  }

  if ($("calPickedLabel")) {
    $("calPickedLabel").textContent = state.dateISO ? formatPrettyDate(state.dateISO) : "Pick a date";
  }
}

/**
 * Format date to ISO string (YYYY-MM-DD) WITHOUT timezone conversion
 * This ensures the date you click is the date you get!
 */
function formatDateToISO(year, month, day) {
  const y = String(year);
  const m = String(month + 1).padStart(2, '0'); // month is 0-indexed
  const d = String(day).padStart(2, '0');
  return `${y}-${m}-${d}`;
}