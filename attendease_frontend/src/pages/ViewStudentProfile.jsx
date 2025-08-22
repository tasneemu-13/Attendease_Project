import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const ViewStudentProfile = () => {
  const [studentData, setStudentData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const location = useLocation();
  const navigate = useNavigate();
  const queryParams = new URLSearchParams(location.search);
  const enrollmentId = queryParams.get("enrollmentId");

  const fetchStudentProfile = async () => {
    if (!enrollmentId) {
      setError("Enrollment ID is missing in the URL. Please navigate from the dashboard.");
      setLoading(false);
      return;
    }

    try {
      // NOTE: The backend URL should be dynamically configured, not hardcoded.
      const response = await fetch(`http://localhost:8080/attendease_backend/api/view-student-profile?enrollmentId=${enrollmentId}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          // The following header is typically set by the browser in a cross-origin request
          // "Access-Control-Allow-Credentials": "true",
        },
      });

      if (!response.ok) {
        if (response.status === 404) {
          throw new Error("Student profile not found for this Enrollment ID.");
        }
        const errorText = await response.text();
        throw new Error(`Failed to fetch student profile: ${errorText}`);
      }

      const data = await response.json();
      setStudentData(data);
    } catch (err) {
      console.error("Error fetching student profile:", err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStudentProfile();
  }, [enrollmentId]);

  const handleEditProfile = () => {
    // Corrected to use the camelCase key from the backend
    if (studentData && studentData.enrollmentId) {
      navigate(`/edit-student-profile?enrollmentId=${studentData.enrollmentId}`);
    } else {
      toast.error("Student data not available for editing.");
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-grid flex items-center justify-center">
        <p className="text-purple-600 text-2xl font-semibold">Loading student profile... ⏳</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-grid flex flex-col items-center justify-center">
        <p className="text-red-600 text-2xl font-semibold">Error: {error} ❌</p>
        <button
          onClick={() => window.history.back()}
          className="mt-8 bg-purple-700 text-white py-3 px-6 text-lg rounded-2xl hover:bg-purple-800 transition"
        >
          ⬅ Back to Dashboard
        </button>
      </div>
    );
  }

  if (!studentData) {
    return (
      <div className="min-h-screen bg-grid flex flex-col items-center justify-center">
        <p className="text-gray-600 text-2xl font-semibold">No student data found. Please check the Enrollment ID. 🤷‍♂️</p>
        <button
          onClick={() => window.history.back()}
          className="mt-8 bg-purple-700 text-white py-3 px-6 text-lg rounded-2xl hover:bg-purple-800 transition"
        >
          ⬅ Back to Dashboard
        </button>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-grid flex flex-col">
      <main className="flex-grow flex items-center justify-center px-4 py-8">
        <div className="bg-white p-14 rounded-[2rem] shadow-2xl w-full max-w-xl border-4 border-purple-500">
          <h1 className="text-4xl font-bold text-center text-purple-600 mb-10">My Profile</h1>
          <div className="flex flex-col items-center space-y-6">
            <div className="w-full mt-6 text-xl space-y-3">
              {/* Corrected keys to match backend */}
              <p><span className="font-semibold">Name:</span> {studentData.studentName || "N/A"}</p>
              <p><span className="font-semibold">Enrollment ID:</span> {studentData.enrollmentId || "N/A"}</p>
              <p><span className="font-semibold">Class Name:</span> {studentData.className || "N/A"}</p>
              <p><span className="font-semibold">Year:</span> {studentData.year || "N/A"}</p>
              <p><span className="font-semibold">Semester:</span> {studentData.semster || "N/A"}</p>
              <p><span className="font-semibold">Program:</span> {studentData.program || "N/A"}</p>
              <p><span className="font-semibold">Branch:</span> {studentData.branch || "N/A"}</p>
              <p><span className="font-semibold">Email:</span> {studentData.email || "N/A"}</p>
            </div>
            <div className="flex flex-col sm:flex-row gap-4 mt-8 w-full justify-center">
              <button
                onClick={handleEditProfile}
                className="text-purple-700 bg-white border-2 border-purple-700 py-3 px-6 text-lg rounded-2xl hover:bg-purple-700 hover:text-white transition-colors duration-200"
              >
                ✏️ Edit Profile
              </button>
                <button
                onClick={() => window.history.back()}
                className="text-purple-700 bg-white border-2 border-purple-700 py-3 px-6 text-lg rounded-2xl hover:bg-purple-700 hover:text-white transition-colors duration-200"
              >
                ⬅ Back to Dashboard
              </button>
            </div>
          </div>
        </div>
      </main>
      <ToastContainer position="bottom-right" autoClose={3000} />
    </div>
  );
};

export default ViewStudentProfile;
