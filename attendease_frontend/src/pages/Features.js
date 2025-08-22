import React from "react";
import { useNavigate } from 'react-router-dom';
import { ClipboardList, CloudUpload, School, Users, Rocket, BookOpen, Star, MessageSquare } from "lucide-react";

const mainFeatures = [
  {
    icon: <ClipboardList className="w-10 h-10 text-purple-600" />,
    title: "No More Registers",
    desc: "Say goodbye to physical attendance registers. Professors can now manage everything digitally with ease.",
  },
  {
    icon: <Rocket className="w-10 h-10 text-purple-600" />,
    title: "Scalable Platform",
    desc: "Built to grow — Attendease supports institutions of all sizes and adapts to your expanding needs.",
  },
  {
    icon: <School className="w-10 h-10 text-purple-600" />,
    title: "Customizable for Any Institute",
    desc: "Attendease is a versatile platform, easily customized to meet the specific needs and functionalities of any educational institution.",
  },
  {
    icon: <Users className="w-10 h-10 text-purple-600" />,
    title: "User Friendly",
    desc: "An intuitive interface ensures that teachers and students can navigate and use the platform effortlessly.",
  },
  {
    icon: <BookOpen className="w-10 h-10 text-purple-600" />,
    title: "All-in-One Tool",
    desc: "From taking attendance to managing records — everything a teacher or student expects is already here.",
  },
  {
    icon: <CloudUpload className="w-10 h-10 text-purple-600" />,
    title: "Excel Integration",
    desc: "Professors can upload Excel files directly — no manual copying needed like before.",
  },
];

const upcomingFeatures = [
  {
    icon: <Star className="w-10 h-10 text-yellow-500" />,
    title: "Coming Soon: Semester Registration",
    desc: "We're building more functionality to allow for semester-wise course registration, making administration even simpler.",
  },
  {
    icon: <MessageSquare className="w-10 h-10 text-blue-500" />,
    title: "Share Your Ideas",
    desc: "Have a great idea for a new feature? We're listening! Help us make Attendease even better for you.",
    link: "/support", // Placeholder for a future support/feedback page
  },
];

const Features = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-gradient-to-br from-indigo-100 via-purple-100 to-pink-100 px-6 py-12 flex flex-col items-center">
      <h1 className="text-4xl md:text-5xl font-bold text-purple-800 mb-10 font-playfair text-center">
        What Makes <span className="text-pink-500">Attendease</span> Special?
      </h1>

      <div className="grid gap-8 md:grid-cols-2 lg:grid-cols-3 w-full max-w-6xl">
        {mainFeatures.map((feature, idx) => (
          <div
            key={idx}
            className="bg-white rounded-2xl p-6 shadow-xl transition-all duration-500 transform hover:-translate-y-1
              bg-gradient-to-r from-white via-white to-white hover:from-pink-200 hover:via-purple-200 hover:to-indigo-200 
              bg-[length:200%_200%] hover:animate-gradient-x"
          >
            <div className="flex items-center justify-center mb-4">
              {feature.icon}
            </div>
            <h2 className="text-xl font-semibold text-purple-700 mb-2 text-center">{feature.title}</h2>
            <p className="text-gray-600 text-sm text-center">{feature.desc}</p>
          </div>
        ))}
      </div>

      <h2 className="text-3xl font-bold text-purple-800 mt-16 mb-8 text-center">
        Exciting Features Are Coming!
      </h2>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-2 gap-8 w-full max-w-2xl">
        {upcomingFeatures.map((feature, idx) => (
          <div
            key={idx}
            className="bg-white rounded-2xl p-6 shadow-xl transition-all duration-500 transform hover:-translate-y-1
              bg-gradient-to-r from-white via-white to-white hover:from-pink-200 hover:via-purple-200 hover:to-indigo-200 
              bg-[length:200%_200%] hover:animate-gradient-x flex flex-col items-center"
          >
            <div className="flex items-center justify-center mb-4">
              {feature.icon}
            </div>
            <h2 className="text-xl font-semibold text-purple-700 mb-2 text-center">{feature.title}</h2>
            <p className="text-gray-600 text-sm text-center">{feature.desc}</p>
            {feature.link && (
              <button
                onClick={() => navigate(feature.link)}
                className="mt-4 px-4 py-2 bg-purple-500 text-white rounded-full shadow hover:bg-purple-600 transition-all"
              >
                Learn More
              </button>
            )}
          </div>
        ))}
      </div>

      <button
        onClick={() => navigate("/")}
        className="mt-12 px-6 py-3 bg-purple-500 text-white rounded-full shadow hover:bg-purple-600 transition-all"
      >
        Back to Home
      </button>
    </div>
  );
};

export default Features;
