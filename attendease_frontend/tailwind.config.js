/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
    "./public/index.html"
  ],
  theme: {
    extend: {
      keyframes: {
        fadeIn: {
          '0%': { opacity: 0 },
          '100%': { opacity: 1 },
        float: {
          '0%, 100%': { transform: 'translateY(0px)' },
          '50%': { transform: 'translateY(-20px)' },
        },
        blob: {
          '0%': { transform: 'translate(0px, 0px) scale(1)' },
          '33%': { transform: 'translate(30px, -50px) scale(1.1)' },
          '66%': { transform: 'translate(-20px, 20px) scale(0.9)' },
          '100%': { transform: 'translate(0px, 0px) scale(1)' },
        },

        },
        gradientX: {
          '0%, 100%': {
            backgroundPosition: '0% 50%',
          },
          '50%': {
            backgroundPosition: '100% 50%',
          },
        },
      },
      animation: {
        'fade-in': 'fadeIn 1s ease-out',
        'spin-slow': 'spin 5s linear infinite',
        'gradient-x': 'gradientX 5s ease infinite',
         float: 'float 6s ease-in-out infinite',
         blob: 'blob 20s infinite',

      },
      fontFamily: {
        dancing: ["'Dancing Script'", 'cursive'],
        bubbles: ["'Bubblegum Sans'", 'cursive'],
        shadow: ["'Doodle Shadow'", 'cursive'],
        playfair: ['"Playfair Display"', 'serif'],
      },
    },
  },
  plugins: [],
}
