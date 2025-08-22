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
    // Wil Add more entries 
  ];

  return (
    <div>
      <LoginHeader/>
    <div className="min-h-screen px-6 py-12 bg-stone-100 font-playfair">
      <div className="max-w-5xl mx-auto">
        <h1 className="text-5xl font-bold text-center text-purple-900 mb-12 underline decoration-pink-400 underline-offset-8">
          Attendease Journal
        </h1>

        <div className="space-y-16">
          {blogs.map((blog, index) => (
            <div key={index} className={`flex flex-col md:flex-row ${index % 2 !== 0 ? 'md:flex-row-reverse' : ''} bg-white shadow-lg rounded-xl overflow-hidden`}>
              <img src={blog.image} alt={blog.title} className="md:w-1/2 h-64 object-cover w-full" />

              <div className="md:w-1/2 p-8 flex flex-col justify-between">
                <div>
                  <p className="text-sm text-gray-500 mb-2">{blog.date}</p>
                  <h2 className="text-3xl font-semibold text-indigo-700 mb-4">{blog.title}</h2>
                  <p className="text-lg text-gray-700 leading-relaxed">{blog.content}</p>
                </div>
                <div className="mt-6">
                  <a href="#" className="text-indigo-600 font-medium hover:underline">
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
