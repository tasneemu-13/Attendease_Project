const AddCourseForm = () => (
  <div className="bg-gradient-to-b from-pink-300 to-purple-400 rounded-lg p-6 shadow-lg w-96">
    <h3 className="text-2xl font-bold mb-4">Add Course</h3>
    <input type="text" placeholder="Course Name" className="w-full mb-4 px-4 py-2 rounded-md border focus:outline-none" />
    <input type="text" placeholder="Teacher Name" className="w-full mb-4 px-4 py-2 rounded-md border focus:outline-none" />
    <button className="w-full bg-blue-800 text-white py-2 rounded-md font-semibold">
      Add
    </button>
  </div>
);
export default AddCourseForm;
