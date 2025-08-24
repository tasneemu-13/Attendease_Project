import AboutUs from '../components/AboutUs';
import Header from '../components/Header';
import SupportAnimation from '../components/SupportAnimation';
import TypedText from '../components/TypedText';
import Button from '../components/Button';
import { useNavigate } from 'react-router-dom';
import ContributorsStory from '../components/ContributorStory';

const HomePage = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-grid">
      <Header />
      {/* Main Content Section */}
      <div className="flex flex-col items-center justify-center px-4 py-8 sm:px-6 sm:py-10 md:px-8 md:py-12">
        {/* Text and Animation Container */}
        <div className="flex flex-col md:flex-row items-center justify-center w-full max-w-5xl space-y-6 md:space-y-0 md:space-x-8">
          <div className="text-center md:text-left w-full md:w-auto">
            <h1 className="font-dancing text-3xl sm:text-4xl md:text-6xl text-purple-900">Welcome to</h1>
            <div
              className="text-2xl sm:text-3xl md:text-5xl inline-block px-2 py-1 rounded-lg font-bold bg-gradient-to-r from-sky-400 via-purple-500 to-pink-300 text-transparent bg-clip-text"
              style={{ fontFamily: "'Doodle Shadow', cursive" }}
            >
              <TypedText />
            </div>
          </div>
          <div className="w-full md:w-auto flex justify-center">
            <SupportAnimation />
          </div>
        </div>
        {/* Buttons Section */}
        <div className="mt-6 sm:mt-8 flex flex-col sm:flex-row space-y-4 sm:space-y-0 sm:space-x-4 w-full max-w-xs sm:max-w-md">
          <Button
            label="About Us"
            onClick={() => window.scrollTo(0, document.body.scrollHeight)}
          />
          <Button
            label="Get Started"
            onClick={() => navigate('/register')}
          />
        </div>
      </div>
      {/* Contributors */}
      <ContributorsStory />
      {/* About Us */}
      <AboutUs />
    </div>
  );
};

export default HomePage;
