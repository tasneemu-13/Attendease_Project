import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const EditStudentProfile = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const enrollmentId = queryParams.get("enrollmentId");

  const [profileData, setProfileData] = useState({
    studentName: "",
    email: "",
    className: "",
    year: "",
    // The key is "semster" to match the backend
    semster: "",
    program: "",
    branch: "",
  });

  const [hasChanges, setHasChanges] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProfile = async () => {
      if (!enrollmentId) {
        toast.error("Enrollment ID not found in URL.");
        setLoading(false);
        return;
      }

      try {
        const response = await fetch(
          `http://localhost:8080/attendease_backend/api/view-student-profile?enrollmentId=${enrollmentId}`
        );

        if (!response.ok) {
          throw new Error("Failed to fetch student profile.");
        }

        const data = await response.json();
        // The API response may use "semester", so we need to map it to "semster"
        // to match the state key.
        const mappedData = {
          ...data,
          semster: data.semster || data.semester, // Use 'semster' from response, or fall back to 'semester'
        };
        setProfileData(mappedData);
      } catch (error) {
        console.error("Error fetching profile:", error);
        toast.error("Error loading profile data.");
      } finally {
        setLoading(false);
      }
    };
    fetchProfile();
  }, [enrollmentId]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setProfileData((prevData) => ({
      ...prevData,
      // Ensure numeric fields are stored as numbers
      [name]: name === "year" || name === "semster" ? parseInt(value, 10) || "" : value,
    }));
    setHasChanges(true);
  };

  const handleSave = async () => {
    if (!hasChanges) {
      toast.info("No changes to save.");
      return;
    }

    try {
      const response = await fetch(
        `http://localhost:8080/attendease_backend/api/edit-student-profile`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            "Access-Control-Allow-Credentials": "true",
          },
          body: JSON.stringify(profileData),
        }
      );

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Failed to save profile changes.");
      }

      toast.success("Profile updated successfully! ✅");
      setHasChanges(false);
    } catch (error) {
      console.error("Error saving profile:", error);
      toast.error(error.message);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-grid flex items-center justify-center font-sans">
        <p className="text-purple-600 text-2xl font-semibold">Loading student profile... ⏳</p>
      </div>
    );
  }

  return (
    <div className="min-h-screen flex flex-col bg-grid font-sans">
      <main className="flex-grow px-4 py-10 flex justify-center">
        <div className="bg-white shadow-xl rounded-2xl p-6 w-full max-w-xl border border-gray-200">
          <div className="space-y-4">
            <h2 className="text-2xl font-semibold text-purple-800 mb-4 text-center">
              Update Student Profile
            </h2>

            {/* Static fields */}
            <div>
              <span className="text-gray-500">Name</span>
              <p className="font-medium text-gray-700">{profileData.studentName || "N/A"}</p>
            </div>
            <div>
              <span className="text-gray-500">Email</span>
              <p className="font-medium text-gray-700">{profileData.email || "N/A"}</p>
            </div>
            <div>
              <span className="text-gray-500">Enrollment ID</span>
              <p className="font-medium text-gray-700">{profileData.enrollmentId || "N/A"}</p>
            </div>
            <div>
              <span className="text-gray-500">Program / Branch</span>
              <p className="font-medium text-gray-700">
                {profileData.program || "N/A"} / {profileData.branch || "N/A"}
              </p>
            </div>

            <hr className="my-4" />

            {/* Editable fields */}
            <div>
              <label htmlFor="className" className="text-gray-500">Class Name</label>
              <input
                type="text"
                id="className"
                name="className"
                value={profileData.className}
                onChange={handleInputChange}
                placeholder="e.g. CSE-B"
                className="mt-1 p-2 border rounded-lg w-full text-gray-700 focus:outline-none focus:ring-2 focus:ring-purple-500 transition-all duration-200"
              />
            </div>
            <div>
              <label htmlFor="year" className="text-gray-500">Year</label>
              <input
                type="number"
                id="year"
                name="year"
                value={profileData.year}
                onChange={handleInputChange}
                placeholder="e.g. 3"
                className="mt-1 p-2 border rounded-lg w-full text-gray-700 focus:outline-none focus:ring-2 focus:ring-purple-500 transition-all duration-200"
              />
            </div>
            <div>
              <label htmlFor="semster" className="text-gray-500">Semester</label>
              <input
                type="number"
                id="semster"
                name="semster"
                value={profileData.semster}
                onChange={handleInputChange}
                placeholder="e.g. 6"
                className="mt-1 p-2 border rounded-lg w-full text-gray-700 focus:outline-none focus:ring-2 focus:ring-purple-500 transition-all duration-200"
              />
            </div>

            <button
              onClick={handleSave}
              className="w-full mt-4 px-4 py-2 bg-purple-700 text-white rounded-lg hover:bg-purple-800 transition-all duration-300 transform hover:scale-105"
            >
              Save Changes
            </button>
            <button
              onClick={() => navigate(-1)}
              className="w-full mt-2 px-4 py-2 bg-gray-400 text-white rounded-lg hover:bg-gray-500 transition-all duration-300 transform hover:scale-105"
            >
              ⬅ Back
            </button>
          </div>
        </div>
      </main>
      <ToastContainer position="bottom-right" autoClose={3000} />
    </div>
  );
};

export default EditStudentProfile;
