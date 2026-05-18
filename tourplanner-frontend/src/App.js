import { useState, useEffect } from "react";

function App() {
  const [tours, setTours] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8080/api/tours")
        .then(res => res.json())
        .then(data => setTours(data));
  }, []);

  return (
      <div className="p-8 bg-gray-100 min-h-screen">
        <h1 className="text-3xl font-bold text-blue-600 mb-4">Tour Planner</h1>
        <ul>
          {tours.map(tour => (
              <li key={tour.id} className="bg-white p-4 mb-2 rounded shadow">
                {tour.name}
              </li>
          ))}
        </ul>
      </div>
  );
}

export default App;