import React, { useState } from 'react';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

// The main application component for the attendance export feature.
const App = () => {
  // State variables for the input fields.
  const [courseCode, setCourseCode] = useState('');
  const [courseName, setCourseName] = useState('');
  // State for managing the loading state of the button.
  const [isLoading, setIsLoading] = useState(false);

  // Function to handle the form submission and API call.
  const handleExport = async (e) => {
    e.preventDefault();

    // Basic validation to ensure both fields are filled.
    if (!courseCode || !courseName) {
      toast.error('Please enter both course code and course name.');
      return;
    }

    setIsLoading(true);
    const loadingToastId = toast.info('Exporting attendance...', { autoClose: false });

    try {
      // Construct the URL for the servlet, ensuring parameters are properly encoded.
      const url = new URL('/api/export-attendance', window.location.origin);
      url.searchParams.append('courseCode', courseCode);
      url.searchParams.append('courseName', courseName);

      // Make the fetch call to the backend servlet.
      const response = await fetch(url.toString());

      // Check if the response was successful.
      if (!response.ok) {
        // If not successful, get the error message from the response body.
        let errorMessage = 'An unexpected error occurred.';
        try {
          // Attempt to read the error message as plain text.
          errorMessage = await response.text();
        } catch (error) {
          // Fallback if the response body is not readable as text.
          console.error("Failed to read error message:", error);
        }
        throw new Error(errorMessage);
      }

      // If the response is successful, trigger the file download.
      const blob = await response.blob();
      const filename = response.headers.get('Content-Disposition')?.split('filename=')[1]?.replace(/"/g, '') || `Attendance_${courseCode}.xlsx`;
      const urlBlob = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = urlBlob;
      a.download = filename;
      document.body.appendChild(a);
      a.click();
      a.remove();
      window.URL.revokeObjectURL(urlBlob);

      toast.update(loadingToastId, {
        render: 'Attendance exported successfully!',
        type: 'success',
        autoClose: 5000,
        isLoading: false,
      });

    } catch (error) {
      console.error('Export failed:', error);
      toast.update(loadingToastId, {
        render: `Export failed: ${error.message}`,
        type: 'error',
        autoClose: 5000,
        isLoading: false,
      });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center p-4 font-sans">
      <div className="bg-white p-8 rounded-xl shadow-lg w-full max-w-md">
        <h1 className="text-3xl font-bold text-gray-800 mb-6 text-center">Export Attendance</h1>
        <p className="text-gray-600 mb-6 text-center">
          Enter the course details to download the attendance record.
        </p>
        <form onSubmit={handleExport} className="space-y-6">
          <div>
            <label htmlFor="courseCode" className="block text-sm font-medium text-gray-700">
              Course Code
            </label>
            <input
              type="text"
              id="courseCode"
              value={courseCode}
              onChange={(e) => setCourseCode(e.target.value)}
              className="mt-1 block w-full px-4 py-2 bg-gray-50 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
              placeholder="e.g., CS101"
              disabled={isLoading}
            />
          </div>
          <div>
            <label htmlFor="courseName" className="block text-sm font-medium text-gray-700">
              Course Name
            </label>
            <input
              type="text"
              id="courseName"
              value={courseName}
              onChange={(e) => setCourseName(e.target.value)}
              className="mt-1 block w-full px-4 py-2 bg-gray-50 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
              placeholder="e.g., Introduction to Computer Science"
              disabled={isLoading}
            />
          </div>
          <button
            type="submit"
            className={`w-full flex justify-center py-2 px-4 border border-transparent rounded-lg shadow-sm text-sm font-medium text-white ${
              isLoading ? 'bg-blue-400 cursor-not-allowed' : 'bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500'
            }`}
            disabled={isLoading}
          >
            {isLoading ? (
              <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
            ) : 'Export'}
          </button>
        </form>
      </div>
      <ToastContainer position="bottom-right" />
    </div>
  );
};

export default App;
