import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { FaGoogle } from "react-icons/fa";
import LoginHeader from "../components/LoginHeader";
import { AUTH_BASE_URL } from "../config";
// This component presents a multi-step registration form with Google Sign-in option,
// password confirmation, and a dynamic, responsive UI.
export default function RegisterForm() {
  // useNavigate hook to programmatically navigate to different routes
  const navigate = useNavigate();

  // State variables for the form's data and UI state
  const [step, setStep] = useState("credentials");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [role, setRole] = useState("");
  const [accessCode, setAccessCode] = useState("");
  const [teacherName, setTeacherName] = useState("");
  // New state to manage a custom message box instead of native 'alert'
  const [message, setMessage] = useState({ text: "", type: "" });
  const [studentData, setStudentData] = useState({
    enrollment: "",
    name: "",
    class: "",
    year: "",
    program: "",
    branch: "",
    semester: "",
  });

  // Function to display a temporary, styled message to the user
  const showMessage = (text, type) => {
    setMessage({ text, type });
    // Hide the message after 3 seconds
    setTimeout(() => setMessage({ text: "", type: "" }), 3000);
  };

  // Handles the submission of email and password with confirmation check
  const handleCredentialSubmit = (e) => {
    e.preventDefault();
    if (!email || !password || !confirmPassword) {
      showMessage("Please fill in all fields", "error");
      return;
    }
    if (password !== confirmPassword) {
      showMessage("Passwords do not match!", "error");
      return;
    }
    setStep("role");
  };

  // Handles the selection of a role (Teacher or Student)
  const handleRoleSelect = (selectedRole) => {
    setRole(selectedRole);
    if (selectedRole === "Teacher") {
      setStep("teacher-auth");
    } else {
      setStep("student-info");
    }
  };

  // Handles the submission of the teacher access code
  const handleAccessSubmit = (e) => {
    e.preventDefault();
    // Validate the access code. In a real app, this would be an API call.
    if (accessCode === "TEACH2025") {
      setStep("teacher-info");
    } else {
      showMessage("Invalid Teacher Access Code!", "error");
    }
  };

  // Handles the final registration for a teacher
  const handleTeacherSubmit = async (e) => {
    e.preventDefault();
    console.log("Attempting to register teacher...");
    try {
      // Using a relative path so the React proxy redirects to the backend.
    const res = await fetch(`${AUTH_BASE_URL}/register`, {
         method: "POST",
         headers: {
          "Content-Type": "application/json",
         },
        body: JSON.stringify({
        email,
        password,
        role: "teacher",
        tac: accessCode,
        name: teacherName,
        }),
     });
      // Log the response status to help with debugging
      console.log("Server response status:", res.status);

      const data = await res.json();
      if (data.status === "success") {
        showMessage("Successfully Registered as Teacher!", "success");
        // Redirect to the login page after a delay
        setTimeout(() => navigate("/login"), 2000);
      } else {
        showMessage("Registration failed.", "error");
      }
    } catch (err) {
      console.error("Fetch error:", err);
      showMessage("Error connecting to server.", "error");
    }
  };

  // Handles the final registration for a student
  const handleStudentSubmit = async (e) => {
    e.preventDefault();
    console.log("Attempting to register student...");
    try {
      // Using a relative path so the React proxy redirects to the backend.
        const res = await fetch(`${AUTH_BASE_URL}/register` ,{
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          email,
          password,
          role: "student",
          enrollmentId: studentData.enrollment,
          name: studentData.name,
          className: studentData.class,
          year: studentData.year,
          program: studentData.program,
          branch: studentData.branch,
          // CHANGED: Renamed 'semester' to 'semster' to match the backend.
          semster: studentData.semester,
        }),
      });
      // Log the response status to help with debugging
      console.log("Server response status:", res.status);

      const data = await res.json();
      if (data.status === "success") {
        showMessage("Successfully Registered as Student!", "success");
        // Redirect to the login page after a delay
        setTimeout(() => navigate("/login"), 2000);
      } else {
        showMessage("Registration failed.", "error");
      }
    } catch (err) {
      console.error("Fetch error:", err);
      showMessage("Error connecting to server.", "error");
    }
  };

  // This function would contain the real Google Sign-in logic
  const handleGoogleSignIn = () => {
    showMessage("Simulating Google Sign-in...", "success");
    // In a real application, you would implement the Firebase/Google Auth flow here.
    // For now, this is a placeholder.
  };

  return (
    <div>
      <LoginHeader/>
      <div className="relative min-h-screen bg-gradient-to-br from-indigo-100 via-purple-100 to-pink-100 flex items-center justify-center p-4 overflow-hidden">
        {/* Dynamic Background with Animated Gradients */}
        <div className="absolute inset-0 z-0 opacity-80">
          <div className="absolute inset-0 bg-gradient-to-r from-blue-300 to-purple-400 mix-blend-multiply filter blur-3xl opacity-70 animate-morph" style={{ borderRadius: '60% 40% 30% 70% / 60% 30% 70% 40%' }}></div>
          <div className="absolute inset-0 bg-gradient-to-r from-green-300 to-yellow-300 mix-blend-multiply filter blur-3xl opacity-70 animate-morph-reverse" style={{ borderRadius: '30% 70% 50% 50% / 50% 50% 50% 50%' }}></div>
          <div className="absolute inset-0 bg-gradient-to-r from-pink-300 to-indigo-400 mix-blend-multiply filter blur-3xl opacity-70 animate-morph-slow" style={{ borderRadius: '70% 30% 60% 40% / 40% 60% 40% 60%' }}></div>
        </div>

        {/* Message Box for success and error messages */}
        {message.text && (
          <div className={`absolute top-6 z-20 px-6 py-3 rounded-xl shadow-lg transform transition-all duration-300 ease-in-out ${message.type === "success" ? "bg-green-600 text-white" : "bg-red-600 text-white"}`}>
            <p className="font-semibold">{message.text}</p>
          </div>
        )}

        {/* Main Form Container */}
        <div className="relative z-10 bg-white/80 border-2 border-purple-400 rounded-3xl p-6 sm:p-10 shadow-2xl backdrop-blur-md text-center text-gray-800 space-y-6 w-full max-w-sm sm:max-w-md transform transition-all duration-500 ease-in-out">
          <h2 className="text-3xl sm:text-4xl font-extrabold text-indigo-800">Register</h2>

          {/* Step 1: Credentials */}
          {step === "credentials" && (
            <div className="space-y-6">
              <form onSubmit={handleCredentialSubmit} className="space-y-4 text-left">
                <input
                  type="email"
                  placeholder="Email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="w-full p-3 rounded-xl border-2 border-purple-300 focus:outline-none focus:ring-2 focus:ring-purple-600 transition-all duration-200"
                  required
                />
                <input
                  type="password"
                  placeholder="Password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="w-full p-3 rounded-xl border-2 border-purple-300 focus:outline-none focus:ring-2 focus:ring-purple-600 transition-all duration-200"
                  required
                />
                <input
                  type="password"
                  placeholder="Confirm Password"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  className="w-full p-3 rounded-xl border-2 border-purple-300 focus:outline-none focus:ring-2 focus:ring-purple-600 transition-all duration-200"
                  required
                />
                <button type="submit" className="w-full bg-purple-600 text-white py-3 rounded-xl font-bold hover:bg-purple-700 transition-colors duration-200 shadow-md transform hover:scale-105">
                  Next
                </button>
              </form>
              <div className="relative flex items-center py-2">
                <div className="flex-grow border-t border-gray-400"></div>
                <span className="flex-shrink mx-4 text-gray-500 font-medium">OR</span>
                <div className="flex-grow border-t border-gray-400"></div>
              </div>
              {/* Google Sign-in Button */}
              <button
                onClick={handleGoogleSignIn}
                className="w-full flex items-center justify-center p-3 rounded-xl border-2 border-gray-300 bg-white text-gray-700 font-semibold shadow-md hover:bg-gray-100 transition-all duration-200 transform hover:scale-105"
              >
                <svg className="w-5 h-5 mr-2" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <path fill="#FFC107" d="M43.611 20.083H42V20H24v8h11.303c-1.649 4.657-6.083 8-11.303 8-6.627 0-12-5.373-12-12s5.373-12 12-12c3.059 0 5.842 1.154 8.026 3.066l5.748-5.748C34.209 4.884 29.13 3 24 3 12.955 3 3 12.955 3 24s9.955 21 21 21 21-9.955 21-21c0-1.341-.138-2.651-.389-3.917z" />
                  <path fill="#FF3D00" d="M6.306 14.691L4.767 9.875A20.942 20.942 0 003 24c0 4.292 1.18 8.358 3.253 11.192l.09-.09.28-.28c2.074-2.834 3.254-6.899 3.254-11.192 0-3.376-.844-6.551-2.31-9.287z" />
                  <path fill="#4CAF50" d="M24 45c5.13 0 10.158-1.884 13.913-5.266l-5.748-5.748C29.842 41.154 27.059 42 24 42c-5.22 0-9.654-3.343-11.303-8L8.69 38.835a20.942 20.942 0 0015.31 6.165z" />
                  <path fill="#1976D2" d="M43.611 20.083c-.251 1.266-.389 2.576-.389 3.917 0 5.145-2.029 9.894-5.337 13.313l-4.992-4.992c2.684-2.851 4.329-6.608 4.329-10.961 0-1.897-.376-3.708-1.076-5.421h-.431z" />
                </svg>
                Sign in with Google
              </button>
            </div>
          )}

          {/* Step 2: Role Selection */}
          {step === "role" && (
            <div className="space-y-4">
              <p className="text-lg font-semibold">Select your role:</p>
              <div className="flex flex-col sm:flex-row justify-center gap-4">
                <button onClick={() => handleRoleSelect("Teacher")} className="px-6 py-3 rounded-xl border-2 border-pink-400 bg-white shadow-lg hover:bg-purple-100 font-semibold transition-colors duration-200 transform hover:scale-105">Teacher</button>
                <button onClick={() => handleRoleSelect("Student")} className="px-6 py-3 rounded-xl border-2 border-purple-400 bg-white shadow-lg hover:bg-purple-100 font-semibold transition-colors duration-200 transform hover:scale-105">Student</button>
              </div>
            </div>
          )}

          {/* Step 3: Teacher Access Code */}
          {step === "teacher-auth" && (
            <form onSubmit={handleAccessSubmit} className="space-y-6 text-left">
              <input
                type="text"
                placeholder="Access Code"
                value={accessCode}
                onChange={(e) => setAccessCode(e.target.value)}
                className="w-full p-3 rounded-xl border-2 border-purple-300 focus:outline-none focus:ring-2 focus:ring-purple-600 transition-all duration-200"
                required
              />
              <button type="submit" className="w-full bg-purple-600 text-white py-3 rounded-xl font-bold hover:bg-purple-700 transition-colors duration-200 shadow-md transform hover:scale-105">
                Next
              </button>
            </form>
          )}

          {/* Step 4: Teacher Info */}
          {step === "teacher-info" && (
            <form onSubmit={handleTeacherSubmit} className="space-y-6 text-left">
              <input
                type="text"
                placeholder="Full Name"
                value={teacherName}
                onChange={(e) => setTeacherName(e.target.value)}
                className="w-full p-3 rounded-xl border-2 border-purple-300 focus:outline-none focus:ring-2 focus:ring-purple-600 transition-all duration-200"
                required
              />
              <button type="submit" className="w-full bg-purple-600 text-white py-3 rounded-xl font-bold hover:bg-purple-700 transition-colors duration-200 shadow-md transform hover:scale-105">
                Register as Teacher
              </button>
            </form>
          )}

          {/* Step 5: Student Info */}
          {step === "student-info" && (
            <form onSubmit={handleStudentSubmit} className="space-y-6 text-left">
              <input
                type="text"
                placeholder="Enrollment ID"
                value={studentData.enrollment}
                onChange={(e) => setStudentData({ ...studentData, enrollment: e.target.value })}
                className="w-full p-3 rounded-xl border-2 border-purple-300 focus:outline-none focus:ring-2 focus:ring-purple-600 transition-all duration-200"
                required
              />
              <input
                type="text"
                placeholder="Full Name"
                value={studentData.name}
                onChange={(e) => setStudentData({ ...studentData, name: e.target.value })}
                className="w-full p-3 rounded-xl border-2 border-purple-300 focus:outline-none focus:ring-2 focus:ring-purple-600 transition-all duration-200"
                required
              />
              <input
                type="text"
                placeholder="Class"
                value={studentData.class}
                onChange={(e) => setStudentData({ ...studentData, class: e.target.value })}
                className="w-full p-3 rounded-xl border-2 border-purple-300 focus:outline-none focus:ring-2 focus:ring-purple-600 transition-all duration-200"
                required
              />
              <input
                type="text"
                placeholder="Year"
                value={studentData.year}
                onChange={(e) => setStudentData({ ...studentData, year: e.target.value })}
                className="w-full p-3 rounded-xl border-2 border-purple-300 focus:outline-none focus:ring-2 focus:ring-purple-600 transition-all duration-200"
                required
              />
              <input
                type="text"
                placeholder="Program"
                value={studentData.program}
                onChange={(e) => setStudentData({ ...studentData, program: e.target.value })}
                className="w-full p-3 rounded-xl border-2 border-purple-300 focus:outline-none focus:ring-2 focus:ring-purple-600 transition-all duration-200"
                required
              />
              <input
                type="text"
                placeholder="Branch"
                value={studentData.branch}
                onChange={(e) => setStudentData({ ...studentData, branch: e.target.value })}
                className="w-full p-3 rounded-xl border-2 border-purple-300 focus:outline-none focus:ring-2 focus:ring-purple-600 transition-all duration-200"
                required
              />
              {/* Using a select dropdown for semesters for correct HTML semantics and user experience */}
              <select
                value={studentData.semester}
                onChange={(e) => setStudentData({ ...studentData, semester: e.target.value })}
                className="w-full p-3 rounded-xl border-2 border-purple-300 bg-white focus:outline-none focus:ring-2 focus:ring-purple-600 transition-all duration-200"
                required
              >
                <option value="" disabled>Select Semester</option>
                <option value="1">Semester 1</option>
                <option value="2">Semester 2</option>
                <option value="3">Semester 3</option>
                <option value="4">Semester 4</option>
                <option value="5">Semester 5</option>
                <option value="6">Semester 6</option>
                <option value="7">Semester 7</option>
                <option value="8">Semester 8</option>
              </select>

              <button type="submit" className="w-full bg-purple-600 text-white py-3 rounded-xl font-bold hover:bg-purple-700 transition-colors duration-200 shadow-md transform hover:scale-105">
                Register as Student
              </button>
            </form>
          )}
        </div>

        {/* Tailwind CSS keyframes for background animations */}
        <style>{`
          @keyframes morph {
            0% { border-radius: 60% 40% 30% 70% / 60% 30% 70% 40%; transform: rotate(0deg); }
            50% { border-radius: 30% 70% 50% 50% / 50% 50% 50% 50%; transform: rotate(180deg); }
            100% { border-radius: 60% 40% 30% 70% / 60% 30% 70% 40%; transform: rotate(360deg); }
          }
          @keyframes morph-reverse {
            0% { border-radius: 30% 70% 50% 50% / 50% 50% 50% 50%; transform: rotate(0deg); }
            50% { border-radius: 60% 40% 30% 70% / 60% 30% 70% 40%; transform: rotate(-180deg); }
            100% { border-radius: 30% 70% 50% 50% / 50% 50% 50% 50%; transform: rotate(-360deg); }
          }
          @keyframes morph-slow {
            0% { border-radius: 70% 30% 60% 40% / 40% 60% 40% 60%; transform: rotate(0deg); }
            50% { border-radius: 40% 60% 70% 30% / 70% 30% 40% 60%; transform: rotate(200deg); }
            100% { border-radius: 70% 30% 60% 40% / 40% 60% 40% 60%; transform: rotate(360deg); }
          }
          .animate-morph { animation: morph 20s ease-in-out infinite; }
          .animate-morph-reverse { animation: morph-reverse 22s ease-in-out infinite; }
          .animate-morph-slow { animation: morph-slow 25s ease-in-out infinite; }
        `}</style>
      </div>
    </div>
  );
}
