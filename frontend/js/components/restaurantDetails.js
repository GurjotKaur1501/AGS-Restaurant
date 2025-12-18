import { $ } from "../dom.js";
import { state } from "../state.js";
import { getRestaurantById } from "../api/restaurantService.js";
import { openImageModal } from "./modal.js";
import { renderCalendar } from "./calendar.js";

export async function renderRestaurantDetails() {
  const r = await getRestaurantById(state.restaurantId);
  if (!r) {
    console.error("Restaurant not found");
    return;
  }

  // Store full restaurant object
  state.currentRestaurant = r;

  $("restName").textContent = r.name;
  $("restAddress").textContent = r.address || "Address not available";
  
  // Handle different data formats (API vs demo)
  const cuisine = r.cuisineType || r.cuisine || "International";
  const avgPrice = r.avgPrice || "195 kr";
  const rating = r.rating || 8.0;
  const reviews = r.reviews || 100;
  
  $("restCuisine").textContent = `${cuisine} • Genomsnittspris ${avgPrice}`;
  $("restRating").textContent = `★ ${rating} (${reviews} omdömen)`;
  $("restAbout").textContent = r.about || "A wonderful dining experience awaits you at this restaurant.";

  // Info cards
  const info = $("restInfoCards");
  info.innerHTML = `
    <div class="info-card"><div class="k">Typ av mat</div><div class="v">${cuisine}</div></div>
    <div class="info-card"><div class="k">Genomsnittspris</div><div class="v">${avgPrice}</div></div>
    <div class="info-card"><div class="k">Öppettider</div><div class="v">Lunch 11:00–14:00, Middag 17:00–22:00</div></div>
    <div class="info-card"><div class="k">Kostalternativ</div><div class="v">Glutenfritt, Vegetariska rätter</div></div>
  `;

  // Menu
  const menu = $("menuList");
  if (r.menu && r.menu.length > 0) {
    menu.innerHTML = r.menu
      .map(
        (item) => `
        <div class="menu-item">
          <div>
            <div class="name">${item.name}</div>
            <div class="desc">${item.desc}</div>
          </div>
          <div class="price">${item.price}</div>
        </div>
      `
      )
      .join("");
  } else {
    menu.innerHTML = '<p class="muted">Menu information not available.</p>';
  }

  // Gallery layout
  const gallery = $("gallery");
  gallery.innerHTML = "";

  const images = r.images || [
    r.img || "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=800",
    "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=800",
    "https://images.unsplash.com/photo-1424847651672-bf20a4b0982b?w=800",
    "https://images.unsplash.com/photo-1551218808-94e220e084d2?w=800",
    "https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?w=800"
  ];

  const big = makeGalleryItem(images[0]);
  big.style.minHeight = "220px";

  const mid = makeGalleryItem(images[1]);
  mid.style.minHeight = "220px";

  const right = document.createElement("div");
  right.className = "gallery-right";

  const topRight = makeGalleryItem(images[2]);
  topRight.style.minHeight = "104px";

  const bottomGrid = document.createElement("div");
  bottomGrid.className = "gallery-small";

  const b1 = makeGalleryItem(images[3]);
  b1.style.minHeight = "104px";

  const b2 = makeGalleryItem(images[4]);
  b2.style.minHeight = "104px";

  const overlay = document.createElement("div");
  overlay.className = "gallery-overlay";
  overlay.textContent = "Visa fler bilder";
  b2.appendChild(overlay);

  bottomGrid.appendChild(b1);
  bottomGrid.appendChild(b2);

  right.appendChild(topRight);
  right.appendChild(bottomGrid);

  gallery.appendChild(big);
  gallery.appendChild(mid);
  gallery.appendChild(right);

  // Render calendar inside booking widget
  renderCalendar();
  
  // Setup tabs
  setupTabs();
}

function makeGalleryItem(url) {
  const div = document.createElement("div");
  div.className = "gallery-item";
  div.innerHTML = `<img src="${url}" alt="Restaurant image" />`;
  div.addEventListener("click", () => openImageModal(url));
  return div;
}

function setupTabs() {
  const tabs = document.querySelectorAll(".tab");
  const panels = document.querySelectorAll(".tab-panel");
  
  tabs.forEach(tab => {
    tab.addEventListener("click", () => {
      const targetTab = tab.dataset.tab;
      
      // Remove active from all tabs and hide all panels
      tabs.forEach(t => t.classList.remove("active"));
      panels.forEach(p => p.classList.add("hidden"));
      
      // Activate clicked tab and show corresponding panel
      tab.classList.add("active");
      $(`tab-${targetTab}`).classList.remove("hidden");
    });
  });
}