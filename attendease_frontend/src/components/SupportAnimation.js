import React from 'react';

const SupportAnimation = () => {
  return (
    <video
      src="animation.mp4"
      autoPlay
      loop
      muted
      playsInline
      style={{ maxWidth: '100%', height: 'auto' }}
      className="w-full md:w-auto md:max-w-[400px]"
    />
  );
};

export default SupportAnimation;
