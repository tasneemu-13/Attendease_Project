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
      <div className="flex flex-row items-center justify-center flex-wrap px-4 md:px-8">
        <div className="text-3xl md:text-5xl text-purple-900 mb-4 w-full md:w-auto text-center md:text-left">
          <div className="w-full md:max-w-3xl">
            <h1 className="font-dancing text-3xl md:text-8xl">Welcome to</h1>
            <div
              className="text-2xl sm:text-3xl md:text-6xl inline-block px-2 py-1 rounded-lg font-bold bg-gradient-to-r from-sky-400 via-purple-500 to-pink-300 text-transparent bg-clip-text"
              style={{ fontFamily: "'Doodle Shadow', cursive" }}
            >
              <TypedText />
            </div>
            <div className="flex flex-col items-center md:items-start space-y-4 mt-6">
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
        </div>
        {/* Animation */}
        <div className="w-full md:w-auto flex justify-center md:justify-start mt-4 md:mt-0">
          <SupportAnimation />
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
