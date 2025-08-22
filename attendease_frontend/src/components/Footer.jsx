import React from "react";
import { Mail, Instagram, Linkedin } from "lucide-react";


// The App component to render the footer
export default function Footer() {
  return (
    <>
      {/*
         CSS for the heartbeat animation.
        
      */}
      <style>
        {`
          @keyframes heartbeat {
            0%, 100% {
              transform: scale(1);
            }
            50% {
              transform: scale(1.1);
            }
          }
          .animate-heartbeat {
            animation: heartbeat 1.5s ease-in-out infinite;
          }
        `}
      </style>
      <footer className="bg-gradient-to-r from-indigo-200 to-purple-500 text-blue-900 px-8 py-6">
        <div className="grid grid-cols-1 md:grid-cols-3 items-center gap-4 text-center">
         
          <div className="text-xl font-semibold bg-clip-text text-transparent bg-gradient-to-r from-blue-700 to-indigo-500 drop-shadow-md transition-all duration-300 hover:scale-105 hover:from-pink-500 hover:to-purple-500">
            © 2025 Attendease
          </div>


          <div className="flex justify-center gap-6">
            <a
              href="mailto:workoholictd@gmail.com"
              target="_blank"
              rel="noopener noreferrer"
              className="hover:text-purple-700 transition"
            >
              <Mail />
            </a>
            <a
              href="https://www.instagram.com/vanshh_gurjar/"
              target="_blank"
              rel="noopener noreferrer"
              className="hover:text-purple-700 transition"
            >
              <Instagram />
            </a>
            <a
              href="https://www.linkedin.com/in/tasneem-dewas"
              target="_blank"
              rel="noopener noreferrer"
              className="hover:text-purple-700 transition"
            >
              <Linkedin />
            </a>
            <a
              href="https://x.com/vanshh_gurjar"
              target="_blank"
              rel="noopener noreferrer"
              className="hover:text-purple-700 transition"
            >
              
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="24"
                height="24"
                viewBox="0 0 24 24"
                fill="currentColor"
                className="h-6 w-6"
              >
                <path d="M18.901 1.144h3.68l-8.629 9.942 9.07 11.236h-7.29l-6.027-7.614-6.49 7.614h-3.68l8.911-10.31-9.157-11.236h7.324l5.35 6.786z" />
              </svg>
            </a>
          </div>


          {/* Added gradient and drop shadow for a more stylish look */}
          <div className="text-lg animate-heartbeat font-semibold bg-clip-text text-transparent bg-pink-900 drop-shadow-md">
            Crafted with ❤️ by T & V
          </div>
        </div>
      </footer>
    </>
  );
}
