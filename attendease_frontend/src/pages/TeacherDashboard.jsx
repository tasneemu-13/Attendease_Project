import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import toast, { Toaster } from 'react-hot-toast';

// Base URL for the backend API
const API_BASE_URL = "http://localhost:8080/attendease_backend/api";

export default function TeacherDashboard() {
  const navigate = useNavigate();
  const [mode, setMode] = useState(null);
  const [teachingCourses, setTeachingCourses] = useState([]);
  const [exportData, setExportData] = useState({
    courseCode: "",
    courseName: ""
  });
  
  const teacherId = localStorage.getItem("teacherId");
  const teacherName = localStorage.getItem("teacherName");

  useEffect(() => {
    const fetchTeachingCourses = async () => {
      if (!teacherId) {
        console.warn("Teacher ID not found. Skipping course fetch.");
        return;
      }
      try {
        const response = await fetch(`${API_BASE_URL}/view-courses?teacherId=${teacherId}`);
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        const courses = await response.json();
        setTeachingCourses(courses);
      } catch (error) {
        console.error("Error fetching teaching courses:", error);
        toast.error("Failed to load courses.");
      }
    };
    fetchTeachingCourses();
  }, [teacherId]);

  const handleNavigation = (path, params = {}) => {
    const queryString = new URLSearchParams(params).toString();
    navigate(`${path}?${queryString}`);
  };

  const handleCourseSelectForAttendance = (e) => {
    const selectedCourseCode = e.target.value;
    const selectedCourse = teachingCourses.find(course => course.course_code === selectedCourseCode);

    if (selectedCourse) {
      const className = selectedCourse.class_name;
      const semsterMatch = className.match(/(\d+)/);
      let year = "";
      let semster = "";

      if (semsterMatch && semsterMatch[1]) {
        const parsedSemster = parseInt(semsterMatch[1], 10);
        semster = parsedSemster.toString();
        year = Math.ceil(parsedSemster / 2).toString();
      }

      handleNavigation("/mark-attendance", {
        courseCode: selectedCourseCode,
        className: className,
        courseName: selectedCourse.course_name,
        year: year,
        semster: semster,
      });
    }
  };

  const deleteCourse = async (courseCode) => {
    if (!window.confirm("Are you sure you want to delete this course?")) {
      return;
    }
    try {
      const response = await fetch(
        `${API_BASE_URL}/delete-teacher-course?courseCode=${courseCode}&teacherId=${teacherId}`,
        { method: "DELETE" }
      );
      if (response.ok) {
        toast.success("Course deleted successfully!");
        setTeachingCourses(prev => prev.filter(course => course.course_code !== courseCode));
      } else {
        const errorText = await response.text();
        throw new Error(`Failed to delete course: ${errorText}`);
      }
    } catch (error) {
      console.error("Error deleting course:", error);
      toast.error(error.message);
    }
  };

const handleExportAttendance = async () => {
    if (!exportData.courseCode) {
        toast.error("Please select a course to export attendance.");
        return;
    }
    try {
        const url = `${API_BASE_URL}/export-teacher?courseCode=${exportData.courseCode}&courseName=${exportData.courseName}&teacherName=${teacherName}`;
        const response = await fetch(url);
        
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Server responded with status: ${response.status}. Message: ${errorText}`);
        }

        const blob = await response.blob();
        const filename = `Attendance_Summary_${exportData.courseCode}_${new Date().toISOString().slice(0, 10)}.xlsx`;

        const urlBlob = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = urlBlob;
        a.download = filename;
        document.body.appendChild(a);
        a.click();
        a.remove();
        window.URL.revokeObjectURL(urlBlob);

        toast.success("Attendance exported successfully!");
    } catch (error) {
        console.error("Export failed:", error);
        toast.error(`Export failed: ${error.message}`);
    }
};

const uniqueCourses = new Set(teachingCourses.map(course => course.course_code));

  return (
    <div className="flex flex-col md:flex-row min-h-screen bg-gradient-to-br from-pink-100 to-blue-200">
      <Toaster />
      <aside className="w-full md:w-64 bg-white shadow-lg p-6 space-y-6 md:rounded-r-3xl rounded-b-3xl">
        <h2 className="text-xl font-bold text-center text-blue-900">Teacher Panel</h2>
        <nav className="space-y-12">
          <button className="mt-12 w-full text-left px-4 py-2 rounded-lg bg-pink-100 hover:bg-pink-200 text-blue-900 font-medium" onClick={() => navigate("/add-teacher-course")}>
            ➕ Add Course
          </button>
          <button className="w-full text-left px-4 py-2 rounded-lg bg-pink-100 hover:bg-pink-200 text-blue-900 font-medium" onClick={() => handleNavigation("/view-courses", { teacherId })}>
            📚 View Courses
          </button>
          <button className="w-full text-left px-4 py-2 rounded-lg bg-pink-100 hover:bg-pink-200 text-blue-900 font-medium" onClick={() => setMode("delete")}>
            🗑️ Delete Course
          </button>
          <button className="w-full text-left px-4 py-2 rounded-lg bg-pink-100 hover:bg-pink-200 text-blue-900 font-medium" onClick={() => handleNavigation("/view-teacher-profile", { teacherId })}>
            👤 View Profile
          </button>
          <button className="w-full text-left px-4 py-2 rounded-lg bg-pink-100 hover:bg-pink-200 text-blue-900 font-medium" onClick={() => handleNavigation("/update-teacher-profile", { teacherId })}>
            ✏️ Update Profile
          </button>
        </nav>
      </aside>

      <main className="flex-1 p-10">
        <div className="flex justify-between items-center mb-10">
          <h1 className="text-3xl font-bold text-blue-900">
            Welcome {teacherName || "Teacher"}
          </h1>
          <div className="flex items-center gap-4">
            <button
              onClick={() => {
                localStorage.clear();
                navigate('/');
              }}
              className="bg-pink-200 text-purple-900 px-4 py-2 rounded-full font-semibold hover:bg-pink-300 transition"
            >
              Logout
            </button>
            <span className="text-2xl">👤</span>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-12">
          <div className="bg-white p-6 rounded-2xl shadow-md hover:shadow-lg transition">
            <h2 className="text-xl font-semibold text-pink-700 mb-2">Total Students</h2>
            <p className="text-3xl font-bold text-blue-900">120</p>
          </div>
          <div className="bg-white p-6 rounded-2xl shadow-md hover:shadow-lg transition">
            <h2 className="text-xl font-semibold text-pink-700 mb-2">Courses</h2>
            <p className="text-3xl font-bold text-blue-900">{uniqueCourses.size}</p>
          </div>
          <div className="bg-white p-6 rounded-2xl shadow-md hover:shadow-lg transition">
            <h2 className="text-xl font-semibold text-pink-700 mb-2">Assigned Classes</h2>
            <p className="text-3xl font-bold text-blue-900">{teachingCourses.length}</p>
          </div>
        </div>

        <div className="bg-white p-6 rounded-2xl shadow-md">
          <h2 className="text-xl font-semibold text-blue-900 mb-4">
            Select Course to Mark Attendance
          </h2>
          <select onChange={handleCourseSelectForAttendance} className="w-full p-3 rounded-lg border border-gray-300" defaultValue="">
            <option value="" disabled>
              Select a Course
            </option>
            {teachingCourses.map((course) => (
              <option key={course.course_code} value={course.course_code}>
                {course.course_name} - {course.class_name}
              </option>
            ))}
          </select>
        </div>

        <div className="bg-white p-6 rounded-2xl shadow-md mt-6">
          <h2 className="text-xl font-semibold text-blue-900 mb-4">
            Export Attendance
          </h2>
          <div className="flex gap-4 items-center">
            <select
              onChange={(e) => {
                const selectedCourse = teachingCourses.find(c => c.course_code === e.target.value);
                setExportData({
                  courseCode: selectedCourse ? selectedCourse.course_code : "",
                  courseName: selectedCourse ? selectedCourse.course_name : ""
                });
              }}
              className="w-full p-3 rounded-lg border border-gray-300"
              defaultValue=""
            >
              <option value="" disabled>
                Select a Course to Export
              </option>
              {teachingCourses.map((course) => (
                <option key={course.course_code} value={course.course_code}>
                  {course.course_name} - {course.class_name}
                </option>
              ))}
            </select>
            <button
              onClick={handleExportAttendance}
              className={`bg-green-600 text-white px-6 py-3 rounded-lg font-semibold hover:bg-green-700 transition ${!exportData.courseCode ? 'opacity-50 cursor-not-allowed' : ''}`}
              disabled={!exportData.courseCode}
            >
              Export 📄
            </button>
          </div>
        </div>

        {mode === "delete" && (
          <div className="mt-10">
            <h2 className="text-2xl font-semibold text-red-800 mb-4">Delete a Course</h2>
            {teachingCourses.length === 0 ? (
              <p className="text-gray-600">No courses available for deletion.</p>
            ) : (
              <ul className="space-y-4">
                {teachingCourses.map((course) => (
                  <li key={course.course_code} className="flex justify-between items-center bg-white p-4 rounded-lg shadow">
                    <div>
                      <p className="font-medium text-blue-900">{course.course_name}</p>
                      <p className="text-sm text-gray-500">
                        Course Code: {course.course_code}
                      </p>
                    </div>
                    <button onClick={() => deleteCourse(course.course_code)} className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600">
                      Delete
                    </button>
                  </li>
                ))}
              </ul>
            )}
          </div>
        )}
      </main>
    </div>
  );
}
