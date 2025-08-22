import React from "react";
import { Linkedin } from "lucide-react";

const contributors = [
  {
    id: 1,
    name: "Tasneem Dewaswala",
    title: "Project Lead",
    image: "/images/whatsapp-1.jpeg",
    thought:
      "In my second year as a BTech CSE student, I realized a significant flaw in our college's traditional attendance system. Attendance information was shared as static Excel files at the end of each month, often through messaging platforms. This made it difficult for both students and faculty to track attendance in real time.This observation inspired me to think of a better solution: a system that would allow students and faculty to track attendance easily and transparently in real time. I also saw this as a perfect opportunity to apply the concepts we had been learning all year, including Java, DBMS, and Object-Oriented Programming (OOP), to build a practical and impactful project.",
    align: "left",
    linkedin: "https://www.linkedin.com/in/tasneem-dewas",
  },
  {
    id: 2,
    name: "Vansh Patil",
    title: "Project Coordinator",
    image: "/images/whatsapp-2.jpeg",
    thought:
      "For me, this project was a chance to turn my passion for purposeful tech into something impactful. When Tasneem shared the idea, it immediately resonated with my own experiences as a student. I knew how frustrating outdated attendance systems could be. That’s why I was excited to help build a solution that’s simple, accurate, and genuinely helpful—for both students and educators. It’s more than just code; it’s a step toward smarter education.",
    align: "right",
    linkedin: "https://www.linkedin.com/in/vanshhpatil/",
  },
];

export default function ContributorsStory() {
  return (
    <section className="relative py-24 px-6 md:px-12 bg-white">
      {/* Subtle Background Gradient */}
      <div className="absolute inset-0 bg-gradient-to-br from-yellow-50 to-pink-50 opacity-50 z-0"></div>

      {/* Main Content Container */}
      <div className="relative z-10 w-full max-w-6xl mx-auto">
        {/* Title */}
        <h2 className="text-4xl font-extrabold text-center text-purple-800 mb-16">
          Contributor's Story
        </h2>

        {/* Contributors */}
        <div className="space-y-24">
          {contributors.map((c) => (
            <div
              key={c.id}
              className={`flex flex-col md:flex-row ${
                c.align === "right" ? "md:flex-row-reverse" : ""
              } items-center justify-center gap-12 p-8 rounded-3xl shadow-lg bg-white bg-opacity-80 backdrop-filter backdrop-blur-sm`}
            >
              {/* Image */}
              <div className="w-44 h-44 md:w-56 md:h-56 rounded-full border-4 border-purple-200 shadow-md overflow-hidden transform transition-transform duration-300 hover:scale-105">
                <img
                  src={c.image}
                  alt={c.name}
                  className="w-full h-full object-cover"
                />
              </div>

              {/* Card */}
              <div className="flex flex-col items-center md:items-start text-center md:text-left max-w-xl">
                <p className="text-lg text-gray-700 italic mb-6 leading-relaxed">
                  “{c.thought}”
                </p>
                <div className="flex items-center gap-4">
                  <div>
                    <h3 className="text-2xl font-bold text-purple-700">
                      {c.name}
                    </h3>
                    <p className="text-md text-gray-500">{c.title}</p>
                  </div>
                  {c.linkedin && (
                    <a
                      href={c.linkedin}
                      target="_blank"
                      rel="noopener noreferrer"
                      aria-label={`LinkedIn profile of ${c.name}`}
                      className="text-gray-400 hover:text-purple-600 transition-colors duration-300 transform hover:scale-110"
                    >
                      <Linkedin className="w-8 h-8" />
                    </a>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>
        
        {/* Tech Stack Section */}
        <div className="mt-20 text-center">
          <h3 className="text-3xl font-bold text-purple-700 mb-6">
            Tech Stack
          </h3>
          <div className="flex flex-wrap justify-center gap-6 text-xl text-gray-700 font-semibold">
            <p className="flex items-center gap-2 bg-purple-100 py-2 px-4 rounded-full shadow-sm cursor-pointer transition-colors duration-300 hover:bg-purple-200 hover:text-purple-800">
              <span className="text-pink-500">⚛️</span> React (Frontend)
            </p>
            <p className="flex items-center gap-2 bg-purple-100 py-2 px-4 rounded-full shadow-sm cursor-pointer transition-colors duration-300 hover:bg-purple-200 hover:text-purple-800">
              <span className="text-pink-500">☕</span> Java (Backend)
            </p>
            <p className="flex items-center gap-2 bg-purple-100 py-2 px-4 rounded-full shadow-sm cursor-pointer transition-colors duration-300 hover:bg-purple-200 hover:text-purple-800">
              <span className="text-pink-500">💾</span> MySQL (Database)
            </p>
          </div>
        </div>
      </div>
    </section>
  );
}
