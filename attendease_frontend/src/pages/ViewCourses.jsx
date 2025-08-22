/*import React from "react";

const courses = [
  {
    id: 1,
    name: "Introduction to Data Science",
    code: "DS101",
    coordinator: "Dr. Meena Sharma",
    classes: 12,
  },
  {
    id: 2,
    name: "Web Development Bootcamp",
    code: "WD202",
    coordinator: "Prof. Rajeev Nair",
    classes: 8,
  },
  {
    id: 3,
    name: "Machine Learning Basics",
    code: "ML303",
    coordinator: "Dr. Anita Kapoor",
    classes: 10,
  },
];

export default function ViewCourses() {
  return (
    <div className="p-8 bg-grid min-h-screen">
      <h1 className="text-3xl font-bold text-blue-900 mb-6">View Courses</h1>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 bg-gradient-to-tr from-pink-100 to-blue-200 p-6 rounded-2xl shadow-lg">
        {courses.map((course) => (
          <div
            key={course.id}
            className="bg-white p-6 rounded-2xl shadow-md hover:shadow-lg transition"
          >
            <h2 className="text-xl font-semibold text-pink-700 mb-1">
              {course.name}
            </h2>
            <p className="text-sm text-gray-500 mb-2">Course Code: {course.code}</p>
            <p className="text-blue-900 font-medium mb-1">
              Coordinator: {course.coordinator}
            </p>
            <p className="text-gray-600 mb-4">Total Classes: {course.classes}</p>

            <div className="flex gap-4">
              <button className="bg-blue-100 text-blue-900 px-4 py-1 rounded-full text-sm hover:bg-blue-200 transition">
                Edit
              </button>
              <button className="bg-red-100 text-red-700 px-4 py-1 rounded-full text-sm hover:bg-red-200 transition">
                Delete
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
*/

import React, { useState, useEffect } from "react";
import { useSearchParams, useNavigate } from "react-router-dom"; 

export default function ViewCourses() {
    const [searchParams] = useSearchParams();
    const teacherId = searchParams.get("teacherId");
    const [courses, setCourses] = useState([]);
    const navigate = useNavigate(); // Initialize useNavigate hook

    useEffect(() => {
        const fetchCourses = async () => {
            try {
                if (!teacherId) {
                    console.warn("Teacher ID not found in URL parameters. Cannot fetch courses.");
                    return;
                }

                const res = await fetch(`http://localhost:8080/attendease_backend/api/view-courses?teacherId=${teacherId}`);

                if (!res.ok) {
                    throw new Error(`HTTP error! status: ${res.status}`);
                }
                const data = await res.json();
                setCourses(data);
            } catch (error) {
                console.error("Error fetching courses for ViewCourses:", error);
            }
        };

        fetchCourses();
    }, [teacherId]);

    const handleBackToDashboard = () => {
      navigate('/teacher');
    };

    return (
        <div className="p-8 bg-grid min-h-screen">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-3xl font-bold text-blue-900">View Courses</h1>
                <button
                    onClick={handleBackToDashboard}
                    className="bg-purple-700 text-white py-2 px-4 text-sm rounded-full hover:bg-purple-800 transition"
                >
                    ⬅ Back to Dashboard
                </button>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 bg-gradient-to-tr from-pink-100 to-blue-200 p-6 rounded-2xl shadow-lg">
                {courses.length === 0 ? (
                    <p className="text-gray-700 text-lg">No courses found for this teacher.</p>
                ) : (
                    courses.map((course) => (
                        <div
                            key={course.course_code || course.id}
                            className="bg-white p-6 rounded-2xl shadow-md hover:shadow-lg transition"
                        >
                            <h2 className="text-xl font-semibold text-pink-700 mb-1">
                                {course.course_name}
                            </h2>
                            <p className="text-sm text-gray-500 mb-2">Course Code: {course.course_code}</p>
                            <p className="text-blue-900 font-medium mb-1">
                                Class: {course.class_name}
                            </p>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
}