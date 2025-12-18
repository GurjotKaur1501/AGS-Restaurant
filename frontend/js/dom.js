export const $ = (id) => document.getElementById(id);

export function on(id, event, handler) {
  const el = $(id);
  if (!el) return;
  el.addEventListener(event, handler);
}

export function setHidden(id, hidden) {
  const el = $(id);
  if (!el) return;
  el.classList.toggle("hidden", hidden);
}

export function setText(id, text) {
  const el = $(id);
  if (el) el.textContent = text;
}

export function setValue(id, value) {
  const el = $(id);
  if (el) el.value = value;
}

export function enable(id, enabled = true) {
  const el = $(id);
  if (el) el.disabled = !enabled;
}