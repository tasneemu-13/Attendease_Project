import './App.css';
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import Header from './components/Header';
import HomePage from './pages/HomePage';
import Footer from './components/Footer';
import RegisterForm from './pages/RegisterForm';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import AdminDashboard from './pages/AdminDashboard';
import StudentDashboard from './pages/StudentDashboard';
import Features from './pages/Features';
import BlogPage from './pages/BlogPage';
import SupportPage from './pages/SupportPage';
import LoginPage from './pages/LoginPage';
import TeacherDashboard from './pages/TeacherDashboard';
import MarkAttendance from './pages/MarkAttendance';
import ViewCourses from './pages/ViewCourses';
import ViewStudentProfile from './pages/ViewStudentProfile';
import ViewStudentReport from './pages/ViewStudentReport';
import UpdateTeacherProfile from './pages/UpdateTeacher';
import EditStudentProfile from './pages/EditStudentProfile';
import AddTeacherCourses from './pages/AddTeacherCourses';  
import ViewTeacherProfile from './pages/ViewTeacherProfile';
import ViewTimetable from './pages/ViewTimeTable';
import ExportAttendance from './pages/ExportAttendance';
function App() {
  return (
    <Router>
      <div className="flex flex-col min-h-screen bg-grid">
       
        
        {/* Route-controlled pages */}
        <main className="flex-grow">
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/register" element={<RegisterForm />} />
          <Route path="/login" element={<LoginPage/>}/>
          <Route path="/student" element={<StudentDashboard/>}/>
          <Route path="/features" element={<Features/>}/>
          <Route path="/blogs" element={<BlogPage/>}/>
          <Route path="/support" element={<SupportPage/>} />
          <Route path="/admin" element={<AdminDashboard />} />
          <Route path="/teacher" element={<TeacherDashboard />} />
          <Route path="/mark-attendance" element={<MarkAttendance/>} />
          <Route path="/view-courses" element={<ViewCourses />} />
          <Route path="/view-student-profile" element={<ViewStudentProfile/>} />
          <Route path='/view-attendance-report' element={< ViewStudentReport/>}></Route>
          <Route path="/edit-student-profile" element={<EditStudentProfile />} />
          <Route path="/view-teacher-profile" element={<ViewTeacherProfile />} />
          <Route path="/update-teacher-profile" element={<UpdateTeacherProfile />} />
          <Route path="/add-teacher-course" element={<AddTeacherCourses />} />
          <Route path="/timetable" element={<ViewTimetable />} />
          <Route path="/export-attendance" element={<ExportAttendance/>} />
        </Routes>
        </main>
        <Footer /> {/* Footer stays at the bottom */}
      
      </div>

    </Router>
  );
}

export default App;
