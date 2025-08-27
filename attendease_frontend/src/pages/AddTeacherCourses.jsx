import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import API_BASE_URL from "../config";   // import config.js

export default function AddTeacherCourses() {
  const navigate = useNavigate();
  const teacherId = localStorage.getItem("teacherId");

  const [courses, setCourses] = useState([]);
  const [selectedCourse, setSelectedCourse] = useState("");
  const [selectedYear, setSelectedYear] = useState(""); // State to store the selected course's year
  const [className, setClassName] = useState("");
  const [message, setMessage] = useState("");
  const [isSuccess, setIsSuccess] = useState(false);
  const [loading, setLoading] = useState(false);

  // Define valid section names based on the course's year
  const classNameOptions = {
    "1": ["C11", "P11"]
    "2": ["3S", "4S"],
    "3": ["5S", "6S"],
    "4": ["7S", "8S"]
  };

  useEffect(() => {
    if (!teacherId) {
      setMessage("Session expired. Please log in again.");
      setIsSuccess(false);
      setTimeout(() => navigate("/login"), 2000);
    }
  }, [teacherId, navigate]);

  useEffect(() => {
    const fetchCourses = async () => {
      try {
        const res = await fetch(`${API_BASE_URL}/view-all-courses`);  // ⬅️ replaced
        const data = await res.json();
        setCourses(data);
      } catch (error) {
        console.error("Error fetching courses:", error);
        setMessage("Failed to load courses.");
        setIsSuccess(false);
      }
    };
    fetchCourses();
  }, []);

  const handleCourseChange = (e) => {
    const courseCode = e.target.value;
    setSelectedCourse(courseCode);

    const courseObject = courses.find(course => course.course_code === courseCode);
    if (courseObject) {
      setSelectedYear(String(courseObject.year));
      setClassName("");
    } else {
      setSelectedYear("");
      setClassName("");
    }
  };

  const handleAddCourse = async (e) => {
    e.preventDefault();

    if (!selectedCourse || !className) {
      setMessage("Please select a course and enter a valid class name.");
      setIsSuccess(false);
      return;
    }

    const semesterRegex = /(\d+)\s*S/;
    const match = className.match(semesterRegex);
    if (match && parseInt(match[1]) > 8) {
      setMessage("Invalid class name. The semester number cannot be greater than 8.");
      setIsSuccess(false);
      return;
    }

    setLoading(true);
    try {
      const response = await fetch(`${API_BASE_URL}/add-teacher-course`, {   // ⬅️ replaced
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          teacherId,
          courseCode: selectedCourse,
          className,
        }),
      });

      const result = await response.json();
      setMessage(result.message);
      setIsSuccess(response.ok);

      if (response.ok) {
        setTimeout(() => navigate("/teacher"), 1500);
      }
    } catch (error) {
      console.error("Error adding course:", error);
      setMessage("Failed to add course.");
      setIsSuccess(false);
    } finally {
      setLoading(false);
    }
  };

  return (
  
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-100 to-pink-100">
      <div className="bg-white shadow-lg rounded-xl p-8 w-full max-w-md">
        <h1 className="text-2xl font-bold text-blue-900 mb-6">Add Teaching Course</h1>

        {message && (
          <p className={`mb-4 text-center ${isSuccess ? "text-green-600" : "text-red-500"}`}>
            {message}
          </p>
        )}

        <form onSubmit={handleAddCourse} className="space-y-4">
          <div>
            <label className="block text-blue-900 font-semibold mb-1">Select Course</label>
            <select
              value={selectedCourse}
              onChange={handleCourseChange}
              className="w-full p-3 border rounded-lg"
            >
              <option value="">-- Select a Course --</option>
              {courses.map((course, index) => (
                <option key={index} value={course.course_code}>
                  {course.course_name} ({course.year})
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-blue-900 font-semibold mb-1">Class Name</label>
            <input
              type="text"
              value={className}
              onChange={(e) => setClassName(e.target.value)}
              placeholder={selectedYear ? `e.g., BRANCH-${classNameOptions[selectedYear]?.[0]}` : "Select a course first"}
              className={`w-full p-3 border rounded-lg ${!selectedYear ? "bg-gray-100 text-gray-500 cursor-not-allowed" : ""}`}
              disabled={!selectedYear}
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-blue-900 text-white py-3 rounded-lg hover:bg-blue-800 transition"
          >
            {loading ? "Adding..." : "➕ Add Course"}
          </button>
        </form>

        <button
          onClick={() => navigate("/teacher")}
          className="mt-4 w-full text-blue-900 font-semibold"
        >
          ⬅ Back to Dashboard
        </button>
      </div>
    </div>
  );
}
