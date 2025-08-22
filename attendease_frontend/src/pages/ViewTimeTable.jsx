import React, { useState } from "react";
import { useNavigate } from "react-router-dom"; // Import useNavigate for redirection

const ViewTimetable = () => {
  const [batch, setBatch] = useState("");
  const [isMessageVisible, setIsMessageVisible] = useState(false); // State to control message visibility
  const navigate = useNavigate(); // Hook for navigation

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!batch) {
      // You can add a local visual cue or just prevent the action without a pop-up
      console.log("Please select a batch.");
      return;
    }

    // Show the "service coming soon" message
    setIsMessageVisible(true);

    // After 3 seconds, hide the message and redirect to the student dashboard
    setTimeout(() => {
      setIsMessageVisible(false);
      navigate("/student"); // Redirect to the student dashboard
    }, 3000); // 3-second delay
  };

  return (
    <div className="relative min-h-screen flex items-center justify-center bg-dot-grid">
      {/* Conditional message box */}
      {isMessageVisible && (
        <div className="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white p-8 rounded-lg shadow-xl border border-purple-400 text-center w-full max-w-sm">
            <h2 className="text-xl font-bold text-purple-700 mb-4">
              Service Coming Soon!
            </h2>
            <p className="text-gray-600">
              We're working on making your timetable available. Please check back later.
            </p>
          </div>
        </div>
      )}

      <div className="z-10 bg-white p-10 rounded-2xl shadow-2xl border border-purple-400 w-[90%] max-w-md text-center space-y-6">
        <h1 className="text-2xl font-bold text-purple-700">Get Your Timetable</h1>
        <form onSubmit={handleSubmit} className="space-y-4">
          <select
            value={batch}
            onChange={(e) => setBatch(e.target.value)}
            className="w-full p-3 border border-gray-300 rounded-md focus:outline-none"
          >
            <option value="">Select Batch</option>
            <option value="BATCH-1">BATCH-1</option>
            <option value="BATCH-2">BATCH-2</option>
          </select>

          <button
            type="submit"
            className="w-full bg-purple-600 hover:bg-purple-700 text-white font-semibold py-2 rounded-lg transition"
            disabled={isMessageVisible} // Disable button while message is visible
          >
            View Timetable 📅
          </button>
        </form>
      </div>
    </div>
  );
};

export default ViewTimetable;
