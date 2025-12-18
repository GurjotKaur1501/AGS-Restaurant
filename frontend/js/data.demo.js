export const restaurants = [
  {
    id: 1,
    name: "La Petite Bistro",
    cuisine: "French",
    address: "Östra Hamngatan 5, 411 10, Göteborg",
    rating: 8.5,
    reviews: 761,
    avgPrice: "219 kr",
    about: "A minimalist French bistro with seasonal tasting options and classic dishes.",
    images: [
      "https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?auto=format&fit=crop&w=1200&q=60",
      "https://images.unsplash.com/photo-1551218808-94e220e084d2?auto=format&fit=crop&w=1200&q=60",
      "https://images.unsplash.com/photo-1523986371872-9d3ba2e2f642?auto=format&fit=crop&w=1200&q=60",
      "https://images.unsplash.com/photo-1553621042-f6e147245754?auto=format&fit=crop&w=1200&q=60",
      "https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=1200&q=60"
    ],
    menu: [
      { name: "Steak Frites", desc: "Grilled steak, fries, pepper sauce", price: "219 kr" },
      { name: "French Onion Soup", desc: "Caramelized onion, beef broth, gratiné", price: "99 kr" },
      { name: "Crème Brûlée", desc: "Vanilla custard, caramel crust", price: "79 kr" }
    ]
  },
  {
    id: 2,
    name: "Oceanview Grill",
    cuisine: "Seafood",
    address: "Avenyn 12, 411 36, Göteborg",
    rating: 8.2,
    reviews: 412,
    avgPrice: "249 kr",
    about: "Fresh seafood and grilled classics in a calm, modern setting.",
    images: [
      "https://images.unsplash.com/photo-1551218808-94e220e084d2?auto=format&fit=crop&w=1200&q=60",
      "https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?auto=format&fit=crop&w=1200&q=60",
      "https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=1200&q=60",
      "https://images.unsplash.com/photo-1523986371872-9d3ba2e2f642?auto=format&fit=crop&w=1200&q=60",
      "https://images.unsplash.com/photo-1553621042-f6e147245754?auto=format&fit=crop&w=1200&q=60"
    ],
    menu: [
      { name: "Grilled Salmon", desc: "Lemon butter, seasonal greens", price: "259 kr" },
      { name: "Shrimp Toast", desc: "Nordic shrimp, dill mayo", price: "119 kr" },
      { name: "Citrus Sorbet", desc: "Light dessert, fresh citrus", price: "69 kr" }
    ]
  },
  {
    id: 3,
    name: "Sakura Sushi",
    cuisine: "Japanese",
    address: "Vasagatan 22, 411 24, Göteborg",
    rating: 8.7,
    reviews: 892,
    avgPrice: "285 kr",
    about: "Authentic Japanese cuisine with fresh sushi and traditional dishes.",
    images: [
      "https://images.unsplash.com/photo-1553621042-f6e147245754?auto=format&fit=crop&w=1200&q=60",
      "https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?auto=format&fit=crop&w=1200&q=60",
      "https://images.unsplash.com/photo-1551218808-94e220e084d2?auto=format&fit=crop&w=1200&q=60",
      "https://images.unsplash.com/photo-1523986371872-9d3ba2e2f642?auto=format&fit=crop&w=1200&q=60",
      "https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=1200&q=60"
    ],
    menu: [
      { name: "Sushi Platter", desc: "Chef's selection of 12 pieces", price: "295 kr" },
      { name: "Ramen Bowl", desc: "Rich broth, noodles, pork belly", price: "189 kr" },
      { name: "Mochi Ice Cream", desc: "Green tea flavor", price: "69 kr" }
    ]
  }
];

export const lunchSlots = ["11:00", "11:30", "12:00", "12:30", "13:00", "13:30"];
export const dinnerSlots = ["17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00"];