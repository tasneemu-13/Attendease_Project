import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { MdOutlineDashboard, MdOutlineClass, MdOutlineSchool, MdOutlinePerson, MdOutlinePeople } from 'react-icons/md';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

// Define a constant for the different views
const VIEWS = {
  FORMS: "forms",
  COURSES: "courses",
  STUDENTS: "students",
  TEACHERS: "teachers",
  USERS: "users",
};

// Custom Confirmation Dialog Component - kept for user interaction
const ConfirmDialog = ({ message, onConfirm, onCancel }) => (
  <div className="fixed inset-0 z-50 flex items-center justify-center bg-gray-900 bg-opacity-50">
    <div className="bg-white p-6 rounded-lg shadow-xl text-center max-w-sm mx-4">
      <p className="text-xl font-semibold mb-4">{message}</p>
      <div className="flex justify-around">
        <button
          onClick={onCancel}
          className="bg-gray-300 text-gray-800 py-2 px-6 rounded-full font-semibold hover:bg-gray-400 transition"
        >
          Cancel
        </button>
        <button
          onClick={onConfirm}
          className="bg-red-600 text-white py-2 px-6 rounded-full font-semibold hover:bg-red-700 transition"
        >
          Confirm
        </button>
      </div>
    </div>
  </div>
);


const AdminDashboard = () => {
  const navigate = useNavigate();

  // --- State for Forms ---
  const [addCourseCode, setAddCourseCode] = useState("");
  const [addCourseName, setAddCourseName] = useState("");
  const [addYear, setAddYear] = useState("");
  const [deleteCourseCode, setDeleteCourseCode] = useState("");
  const [deleteCourseNameDisplay, setDeleteCourseNameDisplay] = useState("");
  const [deleteStudentEnrollmentId, setDeleteStudentEnrollmentId] = useState("");
  const [deleteStudentClassNameDisplay, setDeleteStudentClassNameDisplay] = useState("");
  const [deleteTeacherUserId, setDeleteTeacherUserId] = useState("");

  // --- State for View Management ---
  const [currentView, setCurrentView] = useState(VIEWS.FORMS);
  const [courses, setCourses] = useState([]);
  const [students, setStudents] = useState([]);
  const [teachers, setTeachers] = useState([]);
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // --- State for custom confirmation dialog ---
  const [showConfirmDialog, setShowConfirmDialog] = useState(false);
  const [confirmDialogMessage, setConfirmDialogMessage] = useState("");
  const [confirmAction, setConfirmAction] = useState(null);

  // Function to show custom confirmation dialog
  const showConfirm = (message, action) => {
    setConfirmDialogMessage(message);
    setConfirmAction(() => action); // Use a function to set the state
    setShowConfirmDialog(true);
  };

  // --- Event Handlers ---
  const handleAddCourse = async () => {
    if (!addCourseCode.trim() || !addCourseName.trim() || !addYear.trim()) {
      toast.error("Please fill in all fields for Add Course (Code, Name, Year).");
      return;
    }
    if (isNaN(addYear)) {
      toast.error("Year must be a valid number.");
      return;
    }

    try {
      const response = await fetch("http://localhost:8080/attendease_backend/api/add-courses", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Credentials": "true",
        },
        body: JSON.stringify({
          courseCode: addCourseCode,
          courseName: addCourseName,
          year: parseInt(addYear),
        }),
      });

      const data = await response.json();

      if (response.ok) {
        toast.success(data.message); // Using toast for success
        setAddCourseCode("");
        setAddCourseName("");
        setAddYear("");
      } else {
        toast.error(`Error: ${data.message || "Failed to add course."}`); // Using toast for errors
      }
    } catch (error) {
      console.error("Error adding course:", error);
      toast.error("An unexpected error occurred while adding the course. Please check your network or server logs.");
    }
  };

  const handleDeleteCourse = async () => {
    if (!deleteCourseCode.trim()) {
      toast.error("Please enter the Course ID (Code) to delete a course.");
      return;
    }
    showConfirm(
      `Are you sure you want to delete course with ID: ${deleteCourseCode}?`,
      async () => {
        try {
          const response = await fetch("http://localhost:8080/attendease_backend/api/delete-courses", {
            method: "DELETE",
            headers: {
              "Content-Type": "application/json",
              "Access-Control-Allow-Credentials": "true",
            },
            body: JSON.stringify({
              courseCode: deleteCourseCode,
            }),
          });
    
          let data;
          try {
            data = await response.json();
          } catch (jsonError) {
            console.error("Failed to parse JSON response:", jsonError);
            toast.error("Failed to process server response. It may not be a JSON response.");
            return;
          }

          if (response.ok) {
            toast.success(data.message);
            setDeleteCourseCode("");
            setDeleteCourseNameDisplay("");
          } else {
            // FIX: Check if the message indicates success even with a non-OK status
            if (data.message && data.message.toLowerCase().includes("deleted successfully")) {
              toast.success(data.message);
            } else {
              toast.error(`Error: ${data.message || "Failed to delete course."}`);
            }
          }
        } catch (error) {
          console.error("Error deleting course:", error);
          toast.error("An unexpected error occurred while deleting the course. Please check your network or server logs.");
        }
      }
    );
  };

  const handleDeleteStudent = async () => {
    if (!deleteStudentEnrollmentId.trim()) {
      toast.error("Please enter the Enrollment ID to delete a student.");
      return;
    }
    showConfirm(
      `Are you sure you want to delete student with Enrollment ID: ${deleteStudentEnrollmentId}?`,
      async () => {
        try {
          const response = await fetch("http://localhost:8080/attendease_backend/api/delete-student", {
            method: "DELETE",
            headers: {
              "Content-Type": "application/json",
              "Access-Control-Allow-Credentials": "true",
            },
            body: JSON.stringify({
              enrollmentId: deleteStudentEnrollmentId,
            }),
          });
    
          let data;
          try {
            data = await response.json();
          } catch (jsonError) {
            console.error("Failed to parse JSON response:", jsonError);
            toast.error("Failed to process server response. It may not be a JSON response.");
            return;
          }

          if (response.ok) {
            toast.success(data.message);
            setDeleteStudentEnrollmentId("");
            setDeleteStudentClassNameDisplay("");
          } else {
            // FIX: Check if the message indicates success even with a non-OK status
            if (data.message && data.message.toLowerCase().includes("deleted successfully")) {
              toast.success(data.message);
            } else {
              toast.error(`Error: ${data.message || "Failed to delete student."}`);
            }
          }
        } catch (error) {
          console.error("Error deleting student:", error);
          toast.error("An unexpected error occurred while deleting the student. Please check your network or server logs.");
        }
      }
    );
  };

  const handleDeleteTeacher = async () => {
    if (!deleteTeacherUserId.trim()) {
      toast.error("Please enter the User ID to delete a teacher.");
      return;
    }
    if (isNaN(deleteTeacherUserId)) {
      toast.error("User ID must be a valid number.");
      return;
    }
    showConfirm(
      `Are you sure you want to delete teacher with User ID: ${deleteTeacherUserId}?`,
      async () => {
        try {
          const response = await fetch("http://localhost:8080/attendease_backend/api/delete-teacher", {
            method: "DELETE",
            headers: {
              "Content-Type": "application/json",
              "Access-Control-Allow-Credentials": "true",
            },
            body: JSON.stringify({
              userId:deleteTeacherUserId,
            }),
          });
    
          let data;
          try {
            data = await response.json();
          } catch (jsonError) {
            console.error("Failed to parse JSON response:", jsonError);
            toast.error("Failed to process server response. It may not be a JSON response.");
            return;
          }

          if (response.ok) {
            toast.success(data.message);
            setDeleteTeacherUserId("");
          } else {
            // FIX: Check if the message indicates success even with a non-OK status
            if (data.message && data.message.toLowerCase().includes("deleted successfully")) {
              toast.success(data.message);
            } else {
              toast.error(`Error: ${data.message || "Failed to delete teacher."}`);
            }
          }
        } catch (error) {
          console.error("Error deleting teacher:", error);
          toast.error("An unexpected error occurred while deleting the teacher. Please check your network or server logs.");
        }
      }
    );
  };

  const handleViewAllCourses = async () => {
    setCurrentView(VIEWS.COURSES);
    setLoading(true);
    setError(null);
    setCourses([]);

    try {
      const response = await fetch("http://localhost:8080/attendease_backend/api/view-all-courses", {
        method: "GET",
        headers: {
          "Access-Control-Allow-Credentials": "true",
        },
      });

      if (response.status === 204) {
        setCourses([]);
        toast.info("No courses available."); // Using toast for informational message
        setError("No courses available.");
      } else if (response.ok) {
        const data = await response.json();
        setCourses(data);
        setError(null);
      } else {
        const data = await response.json();
        setError(data.message || "Failed to fetch courses.");
        toast.error(`Error: ${data.message || "Failed to fetch courses."}`);
      }
    } catch (err) {
      console.error("Error fetching courses:", err);
      setError("An unexpected error occurred. Please check the server logs.");
      toast.error("An unexpected error occurred. Please check the server logs.");
    } finally {
      setLoading(false);
    }
  };

  const handleViewAllStudents = async () => {
    setCurrentView(VIEWS.STUDENTS);
    setLoading(true);
    setError(null);
    setStudents([]);

    try {
      const response = await fetch("http://localhost:8080/attendease_backend/api/view-all-students", {
        method: "GET",
        headers: {
          "Access-Control-Allow-Credentials": "true",
        },
      });

      if (response.status === 204) {
        setStudents([]);
        toast.info("No students available.");
        setError("No students available.");
      } else if (response.ok) {
        const data = await response.json();
        setStudents(data);
        setError(null);
      } else {
        const data = await response.json();
        setError(data.message || "Failed to fetch students.");
        toast.error(`Error: ${data.message || "Failed to fetch students."}`);
      }
    } catch (err) {
      console.error("Error fetching students:", err);
      setError("An unexpected error occurred. Please check the server logs.");
      toast.error("An unexpected error occurred. Please check the server logs.");
    } finally {
      setLoading(false);
    }
  };

  const handleViewAllTeachers = async () => {
    setCurrentView(VIEWS.TEACHERS);
    setLoading(true);
    setError(null);
    setTeachers([]);

    try {
      const response = await fetch("http://localhost:8080/attendease_backend/api/view-all-teachers", {
        method: "GET",
        headers: {
          "Access-Control-Allow-Credentials": "true",
        },
      });

      if (response.status === 204) {
        setTeachers([]);
        toast.info("No teachers available.");
        setError("No teachers available.");
      } else if (response.ok) {
        const data = await response.json();
        setTeachers(data);
        setError(null);
      } else {
        const data = await response.json();
        setError(data.message || "Failed to fetch teachers.");
        toast.error(`Error: ${data.message || "Failed to fetch teachers."}`);
      }
    } catch (err) {
      console.error("Error fetching teachers:", err);
      setError("An unexpected error occurred. Please check the server logs.");
      toast.error("An unexpected error occurred. Please check the server logs.");
    } finally {
      setLoading(false);
    }
  };

  const handleViewAllUsers = async () => {
    setCurrentView(VIEWS.USERS);
    setLoading(true);
    setError(null);
    setUsers([]);

    try {
      const response = await fetch("http://localhost:8080/attendease_backend/api/view-all-users", {
        method: "GET",
        headers: {
          "Access-Control-Allow-Credentials": "true",
        },
      });

      if (response.status === 204) {
        setUsers([]);
        toast.info("No users available.");
        setError("No users available.");
      } else if (response.ok) {
        const data = await response.json();
        setUsers(data);
        setError(null);
      } else {
        const data = await response.json();
        setError(data.message || "Failed to fetch users.");
        toast.error(`Error: ${data.message || "Failed to fetch users."}`);
      }
    } catch (err) {
      console.error("Error fetching users:", err);
      setError("An unexpected error occurred. Please check the server logs.");
      toast.error("An unexpected error occurred. Please check the server logs.");
    } finally {
      setLoading(false);
    }
  };

  const handleSidebarClick = (view) => {
    if (view === VIEWS.COURSES) {
      handleViewAllCourses();
    } else if (view === VIEWS.STUDENTS) {
      handleViewAllStudents();
    } else if (view === VIEWS.TEACHERS) {
      handleViewAllTeachers();
    } else if (view === VIEWS.USERS) {
      handleViewAllUsers();
    } else {
      setCurrentView(view);
    }
  };

  // Class for styling sidebar buttons, changing based on the current view
  const sidebarButtonClass = (view) => 
    `flex items-center gap-4 w-full px-4 py-3 rounded-md transition-colors duration-200 ${
      currentView === view ? 'bg-white bg-opacity-20 text-white' : 'text-purple-100 hover:bg-white hover:bg-opacity-10'
    }`;

  // --- Rendering Functions ---
  const renderForms = () => (
    <>
      <div className="flex gap-10 flex-wrap">
        {/* Add Course */}
        <div className="bg-gradient-to-b from-pink-300 to-purple-400 rounded-lg p-6 shadow-lg w-96">
          <h3 className="text-2xl font-bold mb-4">Add Course</h3>
          <input
            type="text"
            placeholder="Course Code"
            className="w-full mb-4 px-4 py-2 rounded-md border focus:outline-none"
            value={addCourseCode}
            onChange={(e) => setAddCourseCode(e.target.value)}
          />
          <input
            type="text"
            placeholder="Course Name"
            className="w-full mb-4 px-4 py-2 rounded-md border focus:outline-none"
            value={addCourseName}
            onChange={(e) => setAddCourseName(e.target.value)}
          />
          <input
            type="number"
            placeholder="Year"
            className="w-full mb-4 px-4 py-2 rounded-md border focus:outline-none"
            value={addYear}
            onChange={(e) => setAddYear(e.target.value)}
          />
          <button
            onClick={handleAddCourse}
            className="w-full bg-blue-800 text-white py-2 rounded-md font-semibold"
          >
            Add
          </button>
        </div>

        {/* Delete Course */}
        <div className="bg-gradient-to-b from-pink-300 to-purple-400 rounded-lg p-6 shadow-lg w-96">
          <h3 className="text-2xl font-bold mb-4">Delete Course</h3>
          <input
            type="text"
            placeholder="Course ID (Code)"
            className="w-full mb-4 px-4 py-2 rounded-md border focus:outline-none"
            value={deleteCourseCode}
            onChange={(e) => setDeleteCourseCode(e.target.value)}
          />
          <input
            type="text"
            placeholder="Course Name (Optional)"
            className="w-full mb-4 px-4 py-2 rounded-md border focus:outline-none"
            value={deleteCourseNameDisplay}
            onChange={(e) => setDeleteCourseNameDisplay(e.target.value)}
            disabled
          />
          <button
            onClick={handleDeleteCourse}
            className="w-full bg-blue-800 text-white py-2 rounded-md font-semibold"
          >
            Delete
          </button>
        </div>
      </div>

      <div className="flex gap-10 mt-10 flex-wrap">
        {/* Delete Student */}
        <div className="bg-gradient-to-b from-pink-300 to-purple-400 rounded-lg p-6 shadow-lg w-96">
          <h3 className="text-2xl font-bold mb-4">Delete Student</h3>
          <input
            type="text"
            placeholder="Enrollment ID"
            className="w-full mb-4 px-4 py-2 rounded-md border focus:outline-none"
            value={deleteStudentEnrollmentId}
            onChange={(e) => setDeleteStudentEnrollmentId(e.target.value)}
          />
          <input
            type="text"
            placeholder="Class (Optional)"
            className="w-full mb-4 px-4 py-2 rounded-md border focus:outline-none"
            value={deleteStudentClassNameDisplay}
            onChange={(e) => setDeleteStudentClassNameDisplay(e.target.value)}
            disabled
          />
          <button
            onClick={handleDeleteStudent}
            className="w-full bg-blue-800 text-white py-2 rounded-md font-semibold"
          >
            Delete
          </button>
        </div>

        {/* Delete Teacher */}
        <div className="bg-gradient-to-b from-pink-300 to-purple-400 rounded-lg p-6 shadow-lg w-96">
          <h3 className="text-2xl font-bold mb-4">Delete Teacher</h3>
          <input
            type="number"
            placeholder="Teacher User ID"
            className="w-full mb-4 px-4 py-2 rounded-md border focus:outline-none"
            value={deleteTeacherUserId}
            onChange={(e) => setDeleteTeacherUserId(e.target.value)}
          />
          <button
            onClick={handleDeleteTeacher}
            className="w-full bg-blue-800 text-white py-2 rounded-md font-semibold"
          >
            Delete
          </button>
        </div>
      </div>
    </>
  );

  const renderCourses = () => (
    <div className="bg-white rounded-lg p-6 shadow-lg">
      <h3 className="text-2xl font-bold mb-4">All Courses</h3>
      {loading && <p className="animate-pulse">Loading courses...</p>}
      {error && <p className="text-red-500">{error}</p>}
      {!loading && !error && courses.length > 0 && (
        <table className="min-w-full bg-white border border-gray-200">
          <thead>
            <tr className="bg-gray-100">
              <th className="py-2 px-4 border-b text-left">Course Code</th>
              <th className="py-2 px-4 border-b text-left">Course Name</th>
              <th className="py-2 px-4 border-b text-left">Year</th>
            </tr>
          </thead>
          <tbody>
            {courses.map((course, index) => (
              <tr key={index} className="hover:bg-gray-50">
                <td className="py-2 px-4 border-b">{course.course_code}</td>
                <td className="py-2 px-4 border-b">{course.course_name}</td>
                <td className="py-2 px-4 border-b">{course.year}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
      {!loading && !error && courses.length === 0 && (
        <p>No courses available.</p>
      )}
    </div>
  );

  const renderStudents = () => (
    <div className="bg-white rounded-lg p-6 shadow-lg">
      <h3 className="text-2xl font-bold mb-4">All Students</h3>
      {loading && <p className="animate-pulse">Loading students...</p>}
      {error && <p className="text-red-500">{error}</p>}
      {!loading && !error && students.length > 0 && (
        <table className="min-w-full bg-white border border-gray-200">
          <thead>
            <tr className="bg-gray-100">
              <th className="py-2 px-4 border-b text-left">Enrollment ID</th>
              <th className="py-2 px-4 border-b text-left">User ID</th>
              <th className="py-2 px-4 border-b text-left">Name</th>
              <th className="py-2 px-4 border-b text-left">Class</th>
              <th className="py-2 px-4 border-b text-left">Year</th>
              <th className="py-2 px-4 border-b text-left">Program</th>
              <th className="py-2 px-4 border-b text-left">Branch</th>
              <th className="py-2 px-4 border-b text-left">Semester</th>
            </tr>
          </thead>
          <tbody>
            {students.map((student, index) => (
              <tr key={index} className="hover:bg-gray-50">
                <td className="py-2 px-4 border-b">{student.enrollment_id}</td>
                <td className="py-2 px-4 border-b">{student.user_id}</td>
                <td className="py-2 px-4 border-b">{student.stu_name}</td>
                <td className="py-2 px-4 border-b">{student.class_name}</td>
                <td className="py-2 px-4 border-b">{student.year}</td>
                <td className="py-2 px-4 border-b">{student.program}</td>
                <td className="py-2 px-4 border-b">{student.branch}</td>
                <td className="py-2 px-4 border-b">{student.semster}</td> 
              </tr>
            ))}
          </tbody>
        </table>
      )}
      {!loading && !error && students.length === 0 && (
        <p>No students available.</p>
      )}
    </div>
  );

  const renderTeachers = () => (
    <div className="bg-white rounded-lg p-6 shadow-lg">
      <h3 className="text-2xl font-bold mb-4">All Teachers</h3>
      {loading && <p className="animate-pulse">Loading teachers...</p>}
      {error && <p className="text-red-500">{error}</p>}
      {!loading && !error && teachers.length > 0 && (
        <table className="min-w-full bg-white border border-gray-200">
          <thead>
            <tr className="bg-gray-100">
              <th className="py-2 px-4 border-b text-left">Teacher ID</th>
              <th className="py-2 px-4 border-b text-left">TAC</th>
              <th className="py-2 px-4 border-b text-left">User ID</th>
              <th className="py-2 px-4 border-b text-left">NAME</th>
            </tr>
          </thead>
          <tbody>
            {teachers.map((teacher, index) => (
              <tr key={index} className="hover:bg-gray-50">
                <td className="py-2 px-4 border-b">{teacher.teacher_id}</td>
                <td className="py-2 px-4 border-b">{teacher.TAC}</td>
                <td className="py-2 px-4 border-b">{teacher.user_id}</td>
                <td className="py-2 px-4 border-b">{teacher.name}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
      {!loading && !error && teachers.length === 0 && (
        <p>No teachers available.</p>
      )}
    </div>
  );

  const renderUsers = () => (
    <div className="bg-white rounded-lg p-6 shadow-lg">
      <h3 className="text-2xl font-bold mb-4">All Users</h3>
      {loading && <p className="animate-pulse">Loading users...</p>}
      {error && <p className="text-red-500">{error}</p>}
      {!loading && !error && users.length > 0 && (
        <table className="min-w-full bg-white border border-gray-200">
          <thead>
            <tr className="bg-gray-100">
              <th className="py-2 px-4 border-b text-left">User ID</th>
              <th className="py-2 px-4 border-b text-left">Email</th>
              <th className="py-2 px-4 border-b text-left">Role</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user, index) => (
              <tr key={index} className="hover:bg-gray-50">
                <td className="py-2 px-4 border-b">{user.user_id}</td>
                <td className="py-2 px-4 border-b">{user.email_id}</td>
                <td className="py-2 px-4 border-b">{user.role}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
      {!loading && !error && users.length === 0 && (
        <p>No users available.</p>
      )}
    </div>
  );

  return (
    <div className="flex bg-grid min-h-screen font-sans text-gray-800">
      {/* ToastContainer to display notifications */}
      <ToastContainer position="top-right" autoClose={3000} hideProgressBar={false} newestOnTop={false} closeOnClick rtl={false} pauseOnFocusLoss draggable pauseOnHover />
      
      {/* Custom Confirmation Dialog - still used for blocking user confirmation */}
      {showConfirmDialog && (
        <ConfirmDialog
          message={confirmDialogMessage}
          onConfirm={() => {
            confirmAction();
            setShowConfirmDialog(false);
          }}
          onCancel={() => setShowConfirmDialog(false)}
        />
      )}

      {/* Sidebar with a more vibrant gradient */}
      <aside className="w-72 bg-gradient-to-b from-purple-900 to-pink-600 text-white p-6 space-y-6">
        <h1 className="text-4xl font-bold mb-4">AttendEase</h1>

        <div className="space-y-4">
          <button 
            className={sidebarButtonClass(VIEWS.FORMS)}
            onClick={() => handleSidebarClick(VIEWS.FORMS)}
          >
            <MdOutlineDashboard className="text-2xl" />
            <span className="text-lg">Dashboard</span>
          </button>

          <div className="space-y-4">
            <button className={sidebarButtonClass(VIEWS.COURSES)} onClick={() => handleSidebarClick(VIEWS.COURSES)}>
              <MdOutlineClass className="text-3xl" />
              <span className="text-lg">View Courses</span>
            </button>
            <button className={sidebarButtonClass(VIEWS.STUDENTS)} onClick={() => handleSidebarClick(VIEWS.STUDENTS)}>
              <MdOutlineSchool className="text-3xl" />
              <span className="text-lg">View Student</span>
            </button>
            <button className={sidebarButtonClass(VIEWS.TEACHERS)} onClick={() => handleSidebarClick(VIEWS.TEACHERS)}>
              <MdOutlinePerson className="text-3xl" />
              <span className="text-lg">View Teacher</span>
            </button>
            <button className={sidebarButtonClass(VIEWS.USERS)} onClick={() => handleSidebarClick(VIEWS.USERS)}>
              <MdOutlinePeople className="text-3xl" />
              <span className="text-lg">View Users</span>
            </button>
          </div>
        </div>
      </aside>

      {/* Main Content */}
      <main className="flex-1 p-8">
        <div className="flex justify-between items-center mb-10">
          <div className="text-3xl font-bold bg-purple-900 text-white px-4 py-2 rounded-tl-xl rounded-br-xl shadow-md">
            Welcome, Admin
          </div>
          <div className="flex items-center gap-4">
            <button 
              onClick={() => navigate('/')} 
              className="bg-pink-200 text-purple-900 px-4 py-2 rounded-full font-semibold hover:bg-pink-300 transition"
            >
              Logout
            </button>
            <span className="text-2xl">👤</span>
          </div>
        </div>
        
        {/* Conditionally render content based on the currentView state */}
        {currentView === VIEWS.FORMS && renderForms()}
        {currentView === VIEWS.COURSES && renderCourses()}
        {currentView === VIEWS.STUDENTS && renderStudents()}
        {currentView === VIEWS.TEACHERS && renderTeachers()}
        {currentView === VIEWS.USERS && renderUsers()}
      </main>
    </div>
  );
};

export default AdminDashboard;
