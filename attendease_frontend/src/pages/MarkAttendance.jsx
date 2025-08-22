import React, { useEffect, useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { FaArrowLeft } from 'react-icons/fa'; // Import an icon for the back button

const MarkAttendance = () => {
  const [params] = useSearchParams();
  const navigate = useNavigate();

  const courseCode = params.get("courseCode");
  const className = params.get("className");
  const courseName = params.get("courseName");
  const year = params.get("year");
  const semster = params.get("semster");

  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);

  const teacherName = localStorage.getItem("teacherName");

  const today = new Date();
  const formattedDate = today.toLocaleDateString("en-US", {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
  const formattedTime = today.toLocaleTimeString("en-US", {
    hour: '2-digit',
    minute: '2-digit'
  });

  const toggleAttendance = (index) => {
    const updated = [...students];
    updated[index].present = !updated[index].present;
    setStudents(updated);
  };

  const handleBackToDashboard = () => {
    navigate("/teacher"); // Assumes the teacher dashboard route is /teacher-dashboard
  };

  const handleSubmit = async () => {
    const attendanceEntries = students.map((s) => ({
      enrollmentId: s.enrollmentId,
      present: s.present,
    }));

    const payload = {
      courseCode,
      className,
      courseName,
      markedBy: teacherName,
      date: new Date().toISOString().split("T")[0],
      attendanceEntries,
    };

    try {
      const res = await fetch("http://localhost:8080/attendease-backend/api/mark-attendance", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      const text = await res.text();
      let data;

      try {
        data = JSON.parse(text);
      } catch (e) {
        throw new Error("Invalid JSON: " + text);
      }

      if (data.success) {
        toast.success("Attendance marked successfully!");
      } else {
        toast.error("Failed: " + data.message);
      }
    } catch (err) {
      toast.error("Error: " + err.message);
    }
  };

  useEffect(() => {
    if (!className || !year || !semster) {
      setLoading(false);
      toast.error("Missing class information. Please go back and select a course again.");
      return;
    }

    const fetchData = async () => {
      try {
        const res = await fetch(`http://localhost:8080/attendease_backend/api/students?className=${className}&year=${year}&semster=${semster}`);

        const text = await res.text();
        let studentData;

        try {
          studentData = JSON.parse(text);
        } catch (e) {
          throw new Error("Invalid JSON: " + text);
        }

        setStudents(studentData.map((s) => ({ ...s, present: false })));
      } catch (err) {
        toast.error("Failed to fetch data: " + err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [className, year, semster]);

  if (loading) {
    return <div className="p-6 flex justify-center items-center h-screen bg-gray-50">Loading...</div>;
  }

  const total = students.length;
  const present = students.filter((s) => s.present).length;
  const absent = total - present;

  return (
    <div className="p-6 bg-gray-50 min-h-screen font-sans">
      <ToastContainer position="top-center" />

      <div className="bg-white p-6 rounded-3xl shadow-md mb-6 border border-gray-200">
        <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center">
          <div className="mb-4 sm:mb-0">
            <h2 className="text-3xl font-bold text-blue-900 mb-2">Mark Attendance</h2>
            <p><span className="font-semibold">Course:</span> {courseName}</p>
            <p><span className="font-semibold">Class:</span> {className}</p>
            <p><span className="font-semibold">Coordinator:</span> {teacherName}</p>
          </div>
          <div className="text-right">
            <p className="text-lg text-gray-700">{formattedDate}</p>
            <p className="text-2xl font-bold text-blue-900">{formattedTime}</p>
          </div>
        </div>
      </div>

      <div className="bg-white p-6 rounded-3xl shadow-md border border-gray-200">
        <table className="w-full table-auto rounded-xl overflow-hidden">
          <thead>
            <tr className="bg-pink-100 text-left text-pink-800">
              <th className="p-4">Enrollment ID</th>
              <th className="p-4">Name</th>
              <th className="p-4">Status</th>
            </tr>
          </thead>
          <tbody>
            {students.map((student, index) => (
              <tr key={student.enrollmentId} className="border-b last:border-b-0 hover:bg-gray-50 transition-colors">
                <td className="p-4">{student.enrollmentId}</td>
                <td className="p-4">{student.studentName}</td>
                <td className="p-4">
                  <label className="inline-flex items-center cursor-pointer">
                    <input
                      type="checkbox"
                      className="sr-only peer"
                      checked={student.present}
                      onChange={() => toggleAttendance(index)}
                    />
                    <div className="w-12 h-6 bg-gray-300 rounded-full peer-checked:bg-green-500 relative transition-all duration-300 shadow-inner">
                      <div className="absolute left-1 top-1 w-4 h-4 bg-white rounded-full shadow-md peer-checked:translate-x-6 transition-transform duration-300"></div>
                    </div>
                    <span className="ml-3 text-base font-medium text-gray-700">
                      {student.present ? "Present" : "Absent"}
                    </span>
                  </label>
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        <div className="mt-8 flex justify-between items-center flex-wrap gap-4">
          <button
            onClick={handleBackToDashboard}
            className="flex items-center px-6 py-3 bg-gray-200 text-gray-800 font-semibold rounded-2xl shadow-md hover:bg-gray-300 transition-colors duration-200"
          >
            <FaArrowLeft className="mr-2" /> Back to Dashboard
          </button>
          <div className="flex-grow text-center">
            <p className="text-xl font-bold text-gray-800">Present: <span className="text-green-600">{present}</span> / <span className="text-gray-500">{total}</span></p>
          </div>
          <button
            onClick={handleSubmit}
            className="bg-pink-600 hover:bg-pink-700 text-white font-semibold px-8 py-3 rounded-2xl shadow-md transition-colors duration-200"
          >
            Submit Attendance
          </button>
        </div>
      </div>
    </div>
  );
};

export default MarkAttendance;

/*
import React, { useEffect, useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { FaArrowLeft } from 'react-icons/fa'; // Import an icon for the back button

const MarkAttendance = () => {
  const [params] = useSearchParams();
  const navigate = useNavigate();

  const courseCode = params.get("courseCode");
  const className = params.get("className");
  const courseName = params.get("courseName");
  const year = params.get("year");
  const semster = params.get("semster");

  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [attendanceAlreadyMarked, setAttendanceAlreadyMarked] = useState(false);

  const teacherName = localStorage.getItem("teacherName");

  const today = new Date();
  const formattedDate = today.toLocaleDateString("en-US", {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
  const formattedTime = today.toLocaleTimeString("en-US", {
    hour: '2-digit',
    minute: '2-digit'
  });

  const toggleAttendance = (index) => {
    const updated = [...students];
    updated[index].present = !updated[index].present;
    setStudents(updated);
  };

  const handleBackToDashboard = () => {
    navigate("/teacher-dashboard"); // Assumes the teacher dashboard route is /teacher-dashboard
  };

  const handleSubmit = async () => {
    // Prevent submission if attendance is already marked
    if (attendanceAlreadyMarked) {
      toast.error("Attendance for this class has already been marked today.");
      return;
    }

    const attendanceEntries = students.map((s) => ({
      enrollmentId: s.enrollmentId,
      present: s.present,
    }));

    const payload = {
      courseCode,
      className,
      courseName,
      markedBy: teacherName,
      date: new Date().toISOString().split("T")[0],
      attendanceEntries,
    };

    try {
      const res = await fetch("http://localhost:8080/attendease-backend/api/mark-attendance", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      const text = await res.text();
      let data;

      try {
        data = JSON.parse(text);
      } catch (e) {
        throw new Error("Invalid JSON: " + text);
      }

      if (data.success) {
        toast.success("Attendance marked successfully!");
        setAttendanceAlreadyMarked(true); // Set state to prevent re-submission
      } else {
        toast.error("Failed: " + data.message);
      }
    } catch (err) {
      toast.error("Error: " + err.message);
    }
  };

  useEffect(() => {
    if (!className || !year || !semster || !courseCode) {
      setLoading(false);
      toast.error("Missing class information. Please go back and select a course again.");
      return;
    }

    const fetchData = async () => {
      try {
        // First, check if attendance has already been marked for today.
        const todayDate = new Date().toISOString().split("T")[0];
        const statusRes = await fetch(`http://localhost:8080/attendease-backend/api/attendance-status?courseCode=${courseCode}&className=${className}&date=${todayDate}`);
        const statusData = await statusRes.json();
        
        if (statusData.isMarked) {
          setAttendanceAlreadyMarked(true);
          setLoading(false);
          toast.info("Attendance has already been marked for this class today.");
          return;
        }

        // If not marked, fetch the student list.
        const res = await fetch(`http://localhost:8080/attendease-backend/api/students?className=${className}&year=${year}&semster=${semster}`);
        
        const text = await res.text();
        let studentData;

        try {
          studentData = JSON.parse(text);
        } catch (e) {
          throw new Error("Invalid JSON: " + text);
        }

        setStudents(studentData.map((s) => ({ ...s, present: false })));
      } catch (err) {
        toast.error("Failed to fetch data: " + err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [className, year, semster, courseCode]);

  if (loading) {
    return <div className="p-6 flex justify-center items-center h-screen bg-gray-50">Loading...</div>;
  }

  const total = students.length;
  const present = students.filter((s) => s.present).length;
  const absent = total - present;

  return (
    <div className="p-6 bg-gray-50 min-h-screen font-sans">
      <ToastContainer position="top-center" />

      <div className="bg-white p-6 rounded-3xl shadow-md mb-6 border border-gray-200">
        <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center">
          <div className="mb-4 sm:mb-0">
            <h2 className="text-3xl font-bold text-blue-900 mb-2">Mark Attendance</h2>
            <p><span className="font-semibold">Course:</span> {courseName}</p>
            <p><span className="font-semibold">Class:</span> {className}</p>
            <p><span className="font-semibold">Coordinator:</span> {teacherName}</p>
          </div>
          <div className="text-right">
            <p className="text-lg text-gray-700">{formattedDate}</p>
            <p className="text-2xl font-bold text-blue-900">{formattedTime}</p>
          </div>
        </div>
      </div>
      
      {attendanceAlreadyMarked ? (
        <div className="bg-white p-6 rounded-3xl shadow-md border-2 border-green-500 text-center text-green-700 text-lg font-semibold">
          Attendance for this class has already been marked today.
        </div>
      ) : (
        <div className="bg-white p-6 rounded-3xl shadow-md border border-gray-200">
          <table className="w-full table-auto rounded-xl overflow-hidden">
            <thead>
              <tr className="bg-pink-100 text-left text-pink-800">
                <th className="p-4">Enrollment ID</th>
                <th className="p-4">Name</th>
                <th className="p-4">Status</th>
              </tr>
            </thead>
            <tbody>
              {students.map((student, index) => (
                <tr key={student.enrollmentId} className="border-b last:border-b-0 hover:bg-gray-50 transition-colors">
                  <td className="p-4">{student.enrollmentId}</td>
                  <td className="p-4">{student.studentName}</td>
                  <td className="p-4">
                    <label className="inline-flex items-center cursor-pointer">
                      <input
                        type="checkbox"
                        className="sr-only peer"
                        checked={student.present}
                        onChange={() => toggleAttendance(index)}
                      />
                      <div className="w-12 h-6 bg-gray-300 rounded-full peer-checked:bg-green-500 relative transition-all duration-300 shadow-inner">
                        <div className="absolute left-1 top-1 w-4 h-4 bg-white rounded-full shadow-md peer-checked:translate-x-6 transition-transform duration-300"></div>
                      </div>
                      <span className="ml-3 text-base font-medium text-gray-700">
                        {student.present ? "Present" : "Absent"}
                      </span>
                    </label>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          <div className="mt-8 flex justify-between items-center flex-wrap gap-4">
            <button
              onClick={handleBackToDashboard}
              className="flex items-center px-6 py-3 bg-gray-200 text-gray-800 font-semibold rounded-2xl shadow-md hover:bg-gray-300 transition-colors duration-200"
            >
              <FaArrowLeft className="mr-2" /> Back to Dashboard
            </button>
            <div className="flex-grow text-center">
              <p className="text-xl font-bold text-gray-800">Present: <span className="text-green-600">{present}</span> / <span className="text-gray-500">{total}</span></p>
            </div>
            <button
              onClick={handleSubmit}
              className="bg-pink-600 hover:bg-pink-700 text-white font-semibold px-8 py-3 rounded-2xl shadow-md transition-colors duration-200"
            >
              Submit Attendance
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default MarkAttendance;
*/