import React from 'react';
import LoginHeader from '../components/LoginHeader';

export default function BlogPage() {
  const blogs = [
    {
      title: "The Rise of Hybrid Events",
      content: "Hybrid events combine physical and virtual experiences, reshaping the future of engagement. Learn how this impacts event planning today.",
      image: "/images/image1.png",
      date: "July 20, 2025"
    },
    {
      title: "Designing Attendee-Centered Experiences",
      content: "Putting attendees first is the future of event design. Explore how user experience transforms participation.",
      image: "/images/image2.png",
      date: "July 14, 2025"
    },
    // Will add more entries
  ];

  return (
    <div>
      <LoginHeader />
      <div className="min-h-screen px-4 py-8 sm:px-6 sm:py-10 bg-stone-100 font-playfair">
        <div className="max-w-5xl mx-auto">
          <h1 className="text-3xl sm:text-4xl md:text-5xl font-bold text-center text-purple-900 mb-8 sm:mb-12 underline decoration-pink-400 underline-offset-4 sm:underline-offset-8">
            Attendease Journal
          </h1>

          <div className="space-y-10 sm:space-y-16">
            {blogs.map((blog, index) => (
              <div key={index} className={`flex flex-col bg-white shadow-lg rounded-xl overflow-hidden ${index % 2 !== 0 ? 'md:flex-row-reverse' : 'md:flex-row'}`}>
                <img src={blog.image} alt={blog.title} className="w-full h-48 sm:h-64 object-cover" />
                <div className="p-4 sm:p-6 flex flex-col justify-between">
                  <div>
                    <p className="text-xs sm:text-sm text-gray-500 mb-1 sm:mb-2">{blog.date}</p>
                    <h2 className="text-xl sm:text-2xl md:text-3xl font-semibold text-indigo-700 mb-2 sm:mb-4">{blog.title}</h2>
                    <p className="text-sm sm:text-lg text-gray-700 leading-relaxed">{blog.content}</p>
                  </div>
                  <div className="mt-4 sm:mt-6">
                    <a href="#" className="text-indigo-600 font-medium hover:underline text-sm sm:text-base">
                      Read More →
                    </a>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
