import { $ } from "../dom.js";

export function openImageModal(url) {
  const modal = $("imgModal");
  const img = $("modalImg");
  if (!modal || !img) return;

  img.src = url;
  modal.classList.remove("hidden");
}

export function closeImageModal() {
  const modal = $("imgModal");
  const img = $("modalImg");
  if (!modal || !img) return;

  modal.classList.add("hidden");
  img.src = "";
}

export function setupModalHandlers() {
  const closeBtn = $("modalClose");
  const backdrop = $("modalBackdrop");
  
  if (closeBtn) {
    closeBtn.addEventListener("click", closeImageModal);
  }
  
  if (backdrop) {
    backdrop.addEventListener("click", closeImageModal);
  }
  
  // Close modal on Escape key
  document.addEventListener("keydown", (e) => {
    if (e.key === "Escape") {
      closeImageModal();
    }
  });
}