import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";

const ViewTeacherProfile = () => {
    // State to hold teacher profile data fetched from the backend
    const [teacherData, setTeacherData] = useState(null);
    // States for loading and error handling
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // useLocation hook to access the URL's query parameters
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    // Extract the teacherId from the URL query string
    // e.g., if URL is /viewteacherprofile?teacherId=24
    const teacherId = queryParams.get("teacherId");

    useEffect(() => {
        // Fetch teacher data from the backend
        const fetchTeacherProfile = async () => {
            // Basic validation: Check if teacherId is present in the URL
            if (!teacherId) {
                setError("Teacher ID is missing in the URL. Please navigate from the dashboard.");
                setLoading(false);
                return;
            }

            try {
                // Construct the URL for your servlet
                // You'll need to create a new servlet endpoint for this
                const response = await fetch(`http://localhost:8080/attendease_backend/api/view-teacher-profile?teacherId=${teacherId}`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Access-Control-Allow-Credentials": "true",
                    },
                });

                // Checking if the HTTP response was successful
                if (!response.ok) {
                    if (response.status === 404) {
                        throw new Error("Teacher profile not found for this ID.");
                    }
                    const errorText = await response.text(); // Get more detailed error from backend
                    throw new Error(`Failed to fetch teacher profile: ${errorText}`);
                }

                // Parsing the JSON response
                const data = await response.json();
                setTeacherData(data); // Updating the teacherData state with fetched data
            } catch (err) {
                // Catching any network errors or errors thrown above
                console.error("Error fetching teacher profile:", err);
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchTeacherProfile();
    }, [teacherId]); // Dependency array: Re-run effect if teacherId changes

    if (loading) {
        return (
            <div className="min-h-screen bg-grid flex items-center justify-center">
                <p className="text-purple-600 text-2xl font-semibold">Loading teacher profile... ⏳</p>
            </div>
        );
    }

    // Display error state
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

    // if no data is found after loading (e.g., teacherData is null from backend)
    if (!teacherData) {
        return (
            <div className="min-h-screen bg-grid flex flex-col items-center justify-center">
                <p className="text-gray-600 text-2xl font-semibold">No teacher data found. Please check the Teacher ID. 🤷‍♂️</p>
                <button
                    onClick={() => window.history.back()}
                    className="mt-8 bg-purple-700 text-white py-3 px-6 text-lg rounded-2xl hover:bg-purple-800 transition"
                >
                    ⬅ Back to Dashboard
                </button>
            </div>
        );
    }

    // Rendering the profile once data is loaded
    return (
        <div className="min-h-screen bg-grid flex flex-col">
            <main className="flex-grow flex items-center justify-center px-4 py-8">
                <div className="bg-white p-14 rounded-[2rem] shadow-2xl w-full max-w-xl border-4 border-purple-500">
                    <h1 className="text-4xl font-bold text-center text-purple-600 mb-10">My Profile</h1>

                    <div className="flex flex-col items-center space-y-6">
                        <div className="w-full mt-6 text-xl space-y-3">
                            <p><span className="font-semibold">Name:</span> {teacherData.teacherName || "N/A"}</p>
                            <p><span className="font-semibold">Teacher ID:</span> {teacherData.teacherId || "N/A"}</p>
                            <p><span className="font-semibold">Department:</span> {teacherData.department || "N/A"}</p>
                            <p><span className="font-semibold">Email:</span> {teacherData.email || "N/A"}</p>
                            <p><span className="font-semibold">Phone:</span> {teacherData.phone || "N/A"}</p>
                            <p><span className="font-semibold">Experience:</span> {teacherData.experience || "N/A"}</p>
                            <p><span className="font-semibold">Highest Qualification:</span> {teacherData.qualification || "N/A"}</p>
                        </div>

                        <button
                            onClick={() => window.history.back()}
                            className="mt-8 bg-purple-700 text-white py-3 px-6 text-lg rounded-2xl hover:bg-purple-800 transition"
                        >
                            ⬅ Back to Dashboard
                        </button>
                    </div>
                </div>
            </main>
        </div>
    );
};

export default ViewTeacherProfile;