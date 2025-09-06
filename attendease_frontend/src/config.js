// src/config.js
const BASE_URL = process.env.REACT_APP_API_BASE_URL || "http://localhost:8080";
// For login/register (no /api)
export const AUTH_BASE_URL = BASE_URL;

// For all other requests (with /api)
export const API_BASE_URL = `${BASE_URL}/api`;
