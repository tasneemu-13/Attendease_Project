import React from "react";
import { motion } from "framer-motion";

export default function AboutUs() {
  return (
    <div className="flex min-h-screen bg-white px-4 py-6 sm:px-6 sm:py-8 md:px-10 md:py-10 text-gray-800">
      {/* Vertical Heading for Desktop */}
      <div className="hidden md:flex items-center pr-10">
        <div className="text-purple-800 font-extrabold text-4xl tracking-widest rotate-[-90deg] whitespace-nowrap">
          ABOUT US
        </div>
      </div>

      {/* Content Sections */}
      <div className="space-y-8 sm:space-y-10 flex-1 w-full">
        {/* Origin Story */}
        <motion.div
          className="bg-purple-100 p-4 sm:p-6 rounded-2xl shadow-md"
          initial={{ opacity: 0, x: -30 }}
          whileInView={{ opacity: 1, x: 0 }}
          transition={{ duration: 0.6 }}
          viewport={{ once: true }}
        >
          <h2 className="text-xl sm:text-2xl font-bold mb-3 sm:mb-4 text-purple-800">
            How the Idea Originated
          </h2>
          <p className="text-sm sm:text-base leading-relaxed">
            As a B.Tech Computer Science student, I often noticed the stress and confusion that surrounded attendance updates — especially towards the end of each month. Attendance data was typically shared via Excel sheets on messaging groups, making it hard for students to track and interpret their records.
          </p>
          <div className="italic text-xs sm:text-sm text-purple-700 bg-purple-200 mt-3 sm:mt-4 p-2 sm:p-3 rounded-xl w-fit">
            “Why not simplify the process for everyone?”
          </div>
        </motion.div>

        {/* Vision Statement */}
        <motion.div
          className="bg-yellow-50 p-4 sm:p-6 rounded-2xl shadow-md"
          initial={{ opacity: 0, x: -30 }}
          whileInView={{ opacity: 1, x: 0 }}
          transition={{ duration: 0.6, delay: 0.2 }}
          viewport={{ once: true }}
        >
          <h2 className="text-xl sm:text-2xl font-bold mb-3 sm:mb-4 text-yellow-700">
            Vision Behind the Project
          </h2>
          <p className="text-sm sm:text-base leading-relaxed">
            What began as a personal thought turned into a team-driven mission — to build a smart, user-friendly attendance system that benefits everyone involved: students, teachers, and administrators. The vision was to make attendance tracking simple, transparent, and accessible.
          </p>
        </motion.div>

        {/* Team Collaboration Section */}
        <motion.div
          className="bg-green-50 p-4 sm:p-6 rounded-2xl shadow-md"
          initial={{ opacity: 0, x: -30 }}
          whileInView={{ opacity: 1, x: 0 }}
          transition={{ duration: 0.6, delay: 0.4 }}
          viewport={{ once: true }}
        >
          <h2 className="text-xl sm:text-2xl font-bold mb-3 sm:mb-4 text-green-700">
            Built with Passion & Purpose
          </h2>
          <p className="text-sm sm:text-base leading-relaxed">
            When Tasneem introduced the idea, Vansh immediately saw it as a perfect opportunity to put his skills to use on something impactful. We both shared the same vision and enthusiasm — to deliver a clean and modern experience that supports role-specific features for admin, teacher, and student.
          </p>
          <div className="italic text-xs sm:text-sm text-green-700 bg-green-100 mt-3 sm:mt-4 p-2 sm:p-3 rounded-xl w-fit">
            “Let’s make something that’s useful, scalable, and easy to use.”
          </div>
        </motion.div>
      </div>
    </div>
  );
}
