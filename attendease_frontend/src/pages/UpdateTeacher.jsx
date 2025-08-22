import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const UpdateTeacherProfile = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const teacherId = queryParams.get("teacherId");

  // State to store the full profile data
  const [profileData, setProfileData] = useState({
    teacherName: "",
    email: "",
    department: "",
    experience: "",
    qualification: "",
    phone: "",
  });

  const [hasChanges, setHasChanges] = useState(false);

  // useEffect to fetch the existing profile data when the component loads
  useEffect(() => {
    const fetchProfile = async () => {
      if (!teacherId) {
        toast.error("Teacher ID not found in URL.");
        return;
      }

      try {
        // Fetching existing data to pre-populate the form
        const response = await fetch(
          `http://localhost:8080/attendease_backend/api/view-teacher-profile?teacherId=${teacherId}`
        );
        if (!response.ok) {
          throw new Error("Failed to fetch teacher profile.");
        }
        const data = await response.json();
        setProfileData(data);
      } catch (error) {
        console.error("Error fetching profile:", error);
        toast.error("Error loading profile data.");
      }
    };

    fetchProfile();
  }, [teacherId]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setProfileData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
    setHasChanges(true);
  };

  const handleSave = async () => {
    if (!hasChanges) {
      toast.info("No changes to save.");
      return;
    }

    try {
      // Convert teacherId to an integer before sending
      const numericTeacherId = parseInt(teacherId, 10);

      // Check if the conversion was successful
      if (isNaN(numericTeacherId)) {
        throw new Error("Invalid teacher ID format.");
      }

      const response = await fetch(
        `http://localhost:8080/attendease_backend/api/update-teacher-profile`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          // Send the numeric ID in the body
          body: JSON.stringify({ teacherId: numericTeacherId, ...profileData }),
        }
      );

      if (!response.ok) {
        // Attempt to parse a more specific error message from the backend response
        const errorData = await response.json();
        throw new Error(errorData.error || "Failed to save profile changes.");
      }

      toast.success("Profile updated successfully!");
      setHasChanges(false);
    } catch (error) {
      console.error("Error saving profile:", error);
      toast.error(error.message);
    }
  };

  return (
    <div className="min-h-screen flex flex-col bg-grid font-sans">
      <main className="flex-grow px-4 py-10 flex justify-center">
        <div className="bg-white shadow-xl rounded-2xl p-6 w-full max-w-xl border border-gray-200">
          {/* Right side fields */}
          <div className="space-y-4">
            <h2 className="text-2xl font-semibold text-purple-800 mb-4 text-center">
              Teacher Profile
            </h2>

            <div>
              <span className="text-gray-500">Name</span>
              <p className="font-medium">{profileData.teacherName}</p>
            </div>

            <div>
              <span className="text-gray-500">Email</span>
              <p className="font-medium">{profileData.email}</p>
            </div>

            <div>
              <label className="text-gray-500">Department</label>
              <input
                type="text"
                name="department"
                value={profileData.department}
                onChange={handleInputChange}
                placeholder="Enter department"
                className="mt-1 p-2 border rounded-lg w-full text-gray-700"
              />
            </div>

            <div>
              <label className="text-gray-500">Experience</label>
              <input
                type="number" // Use type="number" for experience
                name="experience"
                value={profileData.experience}
                onChange={handleInputChange}
                placeholder="e.g. 8"
                className="mt-1 p-2 border rounded-lg w-full text-gray-700"
              />
            </div>

            <div>
              <label className="text-gray-500">Qualification</label>
              <input
                type="text"
                name="qualification"
                value={profileData.qualification}
                onChange={handleInputChange}
                placeholder="e.g. Ph.D. in AI"
                className="mt-1 p-2 border rounded-lg w-full text-gray-700"
              />
            </div>

            <div>
              <label className="text-gray-500">Contact</label>
              <input
                type="text"
                name="phone"
                value={profileData.phone}
                onChange={handleInputChange}
                placeholder="e.g. +91 9876543210"
                className="mt-1 p-2 border rounded-lg w-full text-gray-700"
              />
            </div>

            <button
              onClick={handleSave}
              className="w-full mt-4 px-4 py-2 bg-purple-700 text-white rounded-lg hover:bg-purple-800 transition-all"
            >
              Save Changes
            </button>
            <button
              onClick={() => window.history.back()}
              className="w-full mt-2 px-4 py-2 bg-gray-400 text-white rounded-lg hover:bg-gray-500 transition-all"
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

export default UpdateTeacherProfile;