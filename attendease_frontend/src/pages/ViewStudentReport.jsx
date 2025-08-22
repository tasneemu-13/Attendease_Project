import React, { useState, useEffect } from "react";
import {
  PieChart,
  Pie,
  Cell,
  Legend,
  Tooltip,
  ResponsiveContainer,
} from "recharts";
import { useNavigate, useSearchParams } from "react-router-dom";
import toast, { Toaster } from 'react-hot-toast';

const ViewStudentReport = () => {
  const navigate = useNavigate();
  const currentDate = new Date();
  const [searchParams] = useSearchParams();

  // State to hold dynamic data from the backend
  const [reportData, setReportData] = useState({
    student: null,
    attendanceReport: []
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const formattedDate = currentDate.toLocaleDateString("en-IN", {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
  });

  const formattedTime = currentDate.toLocaleTimeString("en-IN", {
    hour: "2-digit",
    minute: "2-digit",
    hour12: true,
  });

  // A constant array for pie chart colors
  const COLORS = ["#4ade80", "#facc15", "#f87171", "#60a5fa", "#a78bfa", "#fb923c"];

  // Use useEffect to fetch data when the component mounts
  useEffect(() => {
    const fetchAttendanceData = async () => {
      setLoading(true);

      // Get dynamic values from URL search parameters
      const enrollmentId = searchParams.get("enrollmentId");
      const year = searchParams.get("year");
      const semster = searchParams.get("semster");

      // Validate that all required parameters are present
      if (!enrollmentId || !year || !semster) {
          setError("Missing required URL parameters: enrollmentId, year, or semester.");
          setLoading(false);
          return;
      }

      try {
        // Corrected API URL for fetching the report data
        const response = await fetch(`http://localhost:8080/attendease_backend/api/view-attendance-report?enrollmentId=${enrollmentId}&year=${year}&semster=${semster}`);
        
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        setReportData(data); // Set the entire report data object to state
      } catch (e) {
        console.error("Error fetching attendance data:", e);
        setError("Failed to load attendance report.");
      } finally {
        setLoading(false);
      }
    };

    fetchAttendanceData();
  }, [searchParams]); // The dependency array ensures this effect runs whenever URL parameters change

  // Function to handle the export logic for a single course
const handleExport = async (courseCode) => {
  const { student } = reportData;

  try {
    const params = new URLSearchParams({
      enrollmentId: student.enrollmentId,
      courseCode: courseCode,
      studentName: student.studentName,
      className: student.className,
    });

    const url = `http://localhost:8080/attendease_backend/api/export-student?${params.toString()}`;
    const response = await fetch(url);
    
    // The rest of your existing logic for handling the response, blob, etc.
    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Server responded with status: ${response.status}. Message: ${errorText}`);
    }

    const blob = await response.blob();
    const filename = `Student_Report_${student.enrollmentId}_${courseCode}_${new Date().toISOString().slice(0, 10)}.xlsx`;

    const urlBlob = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = urlBlob;
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    a.remove();
    window.URL.revokeObjectURL(urlBlob);

    toast.success("Report exported successfully!");

  } catch (error) {
    console.error("Export failed:", error);
    toast.error(`Export failed: ${error.message}`);
  }
};


  // Render loading state or error message
  if (loading) {
    return (
      <div className="flex justify-center items-center h-screen">
        <div className="text-xl font-semibold text-gray-700">
          Loading report...
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex justify-center items-center h-screen">
        <div className="text-xl font-semibold text-red-600">
          Error: {error}
        </div>
      </div>
    );
  }

  // Use optional chaining for safe access to student details
  const { student, attendanceReport } = reportData;

  // Prepare data for the Pie Chart from the fetched data
  const attendanceData = attendanceReport.map((course) => ({
    name: course.course,
    value: parseFloat(((course.present / course.total) * 100).toFixed(1)),
  }));

  // Custom label rendering for the pie chart
  const renderCustomLabel = ({ cx, cy, midAngle, outerRadius, percent, index }) => {
    const RADIAN = Math.PI / 180;
    const radius = outerRadius + 20;
    const x = cx + radius * Math.cos(-midAngle * RADIAN);
    const y = cy + radius * Math.sin(-midAngle * RADIAN);
    const { name, value } = attendanceData[index];

    return (
      <text
        x={x}
        y={y}
        fill="#333"
        textAnchor={x > cx ? "start" : "end"}
        dominantBaseline="central"
        fontSize={12}
      >
        {`${name}: ${value}%`}
      </text>
    );
  };
  
  return (
    <div className="max-w-6xl mx-auto mt-8 p-6 bg-white shadow-lg rounded-xl mb-7">
      <Toaster /> {/* Toaster for notifications */}
      
      {/* Back to Dashboard Button */}
      <div className="mb-4">
        <button
          onClick={() => navigate("/student")}
          className="bg-pink-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition"
        >
          ⬅️ Back to Dashboard
        </button>
      </div>

     
      <div className="mb-6 bg-gray-100 p-4 rounded-lg shadow-sm">
        <div className="flex justify-between text-sm text-gray-700">
          <div>
 
            <p><strong>👤 Student Name:</strong> {student?.studentName}</p>
            <p><strong>🆔 Enrollment ID:</strong> {student?.enrollmentId}</p>
            <p><strong>📝 Class:</strong> {student?.className}</p>
          </div>
          <div className="text-right">
            <p>{formattedDate}</p>
            <p>{formattedTime}</p>
          </div>
        </div>
      </div>

      
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 items-center">
     
        <div className="bg-yellow-50 p-6 rounded-lg shadow flex justify-center items-center h-[420px]">
          <div className="w-full h-full">
            <h2 className="text-lg font-semibold mb-6 text-yellow-700 text-center">
              📊 Attendance Percentage by Course
            </h2>
            <ResponsiveContainer width="100%" height="90%">
              <PieChart>
                <Pie
                  data={attendanceData}
                  cx="50%"
                  cy="50%"
                  outerRadius={120}
                  dataKey="value"
                  label={renderCustomLabel}
                  labelLine={false}
                >
                  {attendanceData.map((_, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip />
                <Legend verticalAlign="bottom" />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Summary Section */}
        <div className="bg-gradient-to-br from-pink-200 via-purple-100 to-blue-200 p-6 rounded-lg shadow-md">
          <h2 className="text-lg font-semibold mb-4 text-blue-900 text-center">
            📝 Course-wise Attendance Summary
          </h2>
          <ul className="text-sm text-gray-800 divide-y divide-gray-700">
            {attendanceReport.map((course, idx) => (
              <li key={idx} className="pt-2 pb-3 flex justify-between items-center">
                <div>
                  <strong className="text-base text-purple-900">📘 {course.course}</strong>
                  <div className="ml-2 mt-1">
                    <p>🧑‍🏫 Teacher: {course.teacherName}</p> 
                    <p>✅ Present: {course.present}</p>
                    <p>❌ Absent: {course.absent}</p>
                    <p>📅 Total Classes: {course.total}</p>
                    <p>
                      📈 Attendance:{" "}
                      <strong>
                        {((course.present / course.total) * 100).toFixed(1)}%
                      </strong>
                    </p>
                  </div>
                </div>
                {/* Export button for each course */}
              <button
  onClick={() => handleExport(course.course)}
  className="bg-green-600 text-white px-4 py-2 rounded-lg font-semibold hover:bg-green-700 transition"
  disabled={loading || !student}
>
  Export 📄
</button>
                 
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default ViewStudentReport;
