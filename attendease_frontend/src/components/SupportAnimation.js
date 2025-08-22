// src/components/SupportAnimation.js
import React from 'react';

const SupportAnimation = () => {
  return (
    <video
      src="animation.mp4" // or use import if inside assets
      autoPlay
      loop
      muted
      playsInline
      style={{ width: '700px', height: 'auto' }}
    />
  );
};

export default SupportAnimation;
