import React, { useState, useEffect } from "react";
import {
  FaChartBar,
  FaUser,
  FaEdit,
  FaCalendarAlt,
  FaSignOutAlt // Added FaSignOutAlt icon
} from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const StudentDashboard = () => {
  const navigate = useNavigate();
  const [studentProfile, setStudentProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const enrollmentId = localStorage.getItem("enrollmentId");
  const studentName = localStorage.getItem("studentName");

  useEffect(() => {
    const fetchStudentProfile = async () => {
      if (!enrollmentId) {
        toast.error("Error: Enrollment ID not found. Please log in again.");
        setLoading(false);
        return;
      }

      try {
        const response = await fetch(`http://localhost:8080/attendease_backend/api/view-student-profile?enrollmentId=${enrollmentId}`);
        if (!response.ok) {
          throw new Error("Failed to fetch student profile.");
        }
        const data = await response.json();
        setStudentProfile(data);
      } catch (error) {
        console.error("Error fetching student profile:", error);
        toast.error("Error fetching student profile data.");
      } finally {
        setLoading(false);
      }
    };

    fetchStudentProfile();
  }, [enrollmentId]);

  const handleViewProfile = () => {
    if (enrollmentId) {
      navigate(`/view-student-profile?enrollmentId=${enrollmentId}`);
    } else {
      toast.error("Error: Enrollment ID not found. Please log in again.");
    }
  };

  const handleEditProfile = () => {
    if (enrollmentId) {
      navigate(`/edit-student-profile?enrollmentId=${enrollmentId}`);
    } else {
      toast.error("Error: Enrollment ID not found. Please log in again.");
    }
  };

  const handleViewAttendance = () => {
    if (studentProfile) {
      navigate(`/view-attendance-report?enrollmentId=${studentProfile.enrollmentId}&year=${studentProfile.year}&semster=${studentProfile.semster}`);
    } else {
      toast.error("Error: Student profile not loaded. Please try again.");
    }
  };

  // New logout handler
  const handleLogout = () => {
    localStorage.removeItem("enrollmentId");
    localStorage.removeItem("studentName");
    navigate("/");
    toast.success("You have been logged out.");
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-grid flex justify-center items-center">
        <p className="text-2xl text-purple-700 font-semibold">Loading your dashboard...</p>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-grid font-sans">
      <ToastContainer position="top-right" autoClose={3000} hideProgressBar={false} newestOnTop={false} closeOnClick rtl={false} pauseOnFocusLoss draggable pauseOnHover />
      
      {/* Header with Logout Button */}
      <header className="bg-purple-700 text-white py-4 px-6 flex justify-between items-center shadow-md">
        <h1 className="text-3xl font-bold">AttendEase</h1>
        <div className="flex items-center text-lg">
          <span className="mr-4">Welcome, {studentName || "Student"} 👋</span>
          <button
            onClick={handleLogout}
            className="flex items-center px-4 py-2 bg-purple-500 rounded-full text-white text-base font-semibold hover:bg-purple-600 transition-colors duration-200 shadow-md"
          >
            <FaSignOutAlt className="mr-2" />
            Logout
          </button>
        </div>
      </header>

      <main className="max-w-2xl mx-auto mt-10 p-6 rounded-3xl shadow-2xl bg-gradient-to-br from-pink-200 via-purple-200 to-blue-100 mb-10 border border-purple-300">
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-8">
          <div
            className="aspect-square bg-white p-6 rounded-xl shadow-lg hover:shadow-2xl hover:scale-105 transition duration-300 flex flex-col items-center justify-center border border-gray-200 cursor-pointer"
            onClick={handleViewAttendance}
          >
            <FaChartBar className="text-5xl text-purple-700 mb-4" />
            <p className="text-xl font-semibold text-center">View Your Attendance</p>
          </div>

          <div
            className="aspect-square bg-white p-6 rounded-xl shadow-lg hover:shadow-2xl hover:scale-105 transition duration-300 flex flex-col items-center justify-center border border-gray-200 cursor-pointer"
            onClick={handleViewProfile}
          >
            <FaUser className="text-5xl text-purple-700 mb-4" />
            <p className="text-xl font-semibold text-center">My Profile</p>
          </div>

          <div
            className="aspect-square bg-white p-6 rounded-xl shadow-lg hover:shadow-2xl hover:scale-105 transition duration-300 flex flex-col items-center justify-center border border-gray-200 cursor-pointer"
            onClick={handleEditProfile}
          >
            <FaEdit className="text-5xl text-purple-700 mb-4" />
            <p className="text-xl font-semibold text-center">Edit Profile</p>
          </div>

          <div
            className="aspect-square bg-white p-6 rounded-xl shadow-lg hover:shadow-2xl hover:scale-105 transition duration-300 flex flex-col items-center justify-center border border-gray-200 cursor-pointer"
            onClick={() => navigate(`/timetable?enrollmentId=${enrollmentId}`)}
          >
            <FaCalendarAlt className="text-5xl text-purple-700 mb-4" />
            <p className="text-xl font-semibold text-center">View Your Timetable 🗓️</p>
          </div>
        </div>
      </main>
    </div>
  );
};

export default StudentDashboard;
