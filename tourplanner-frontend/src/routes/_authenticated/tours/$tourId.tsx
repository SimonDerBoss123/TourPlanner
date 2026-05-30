import {createFileRoute, useNavigate, Link} from '@tanstack/react-router'
import React, {useEffect, useState} from "react";
import {Navbar} from "../../../components/dashboard/Navbar.tsx";
import NewTourLogModal from "../../../components/dashboard/NewTourLogModal.tsx";

export const Route = createFileRoute('/_authenticated/tours/$tourId')({
  component: TourDetailPage,
})


function TourDetailPage() {
  const { tourId } = Route.useParams();
  const [tour,setTour] = useState(null);
  const [tourLogs,setTourLogs] = useState([]);
  const [isOpen, setIsOpen] = useState(false);

   useEffect(() => {
     fetchTour()
     fetchTourLogs()
   },[])

  const fetchTourLogs = async () => {
    const token = localStorage.getItem('auth-token');
    const response = await fetch(`http://localhost:8080/api/tours/${tourId}/tourlogs`,{
      headers:{ Authorization: `Bearer ${token}`}
    });

    const data = await response.json();
    setTourLogs(data);
  }

  const fetchTour = async () =>{
    const token = localStorage.getItem('auth-token')
    const response = await fetch(`http://localhost:8080/api/tours/${tourId}`,{
      headers:{ Authorization: `Bearer ${token}`}
    });

    const data = await response.json();
    setTour(data);
  }

  const formatTime = (totalMinutes: number) => {
    const hours = Math.floor(totalMinutes / 60)
    const minutes = totalMinutes % 60
    if (hours === 0) return `${minutes}m`
    if (minutes === 0) return `${hours}h`
    return `${hours}h ${minutes}m`
  }

  const navigate = useNavigate();


  if(!tour) return <div>Loading...</div>

  return (
      <div className="min-h-screen bg-background">
        <Navbar />

        <NewTourLogModal
            tourId={tourId}
            isOpen={isOpen}
            onClose = {() => setIsOpen(false)}
            onSuccess = {fetchTourLogs}

        />

        <main className="max-w-6xl mx-auto px-6 py-10">

          {/* Back Button */}
          <button className="text-sm text-muted-foreground mb-6 flex items-center gap-1 cursor-pointer"
          onClick={() => navigate({to: '/dashboard'})}>
            ← Back
          </button>

          {/* Tour Header */}
          <div className="flex items-start justify-between mb-8">
            <div>
              <h1 className="text-3xl font-semibold tracking-tight mb-1">{tour.name}</h1>
              <p className="text-muted-foreground text-sm">{tour.fromLocation} → {tour.toLocation}</p>
            </div>
            <div className="flex gap-2">
              <button className="text-sm border rounded px-3 py-1">Edit</button>
              <button className="text-sm border rounded px-3 py-1 text-destructive">Delete</button>
            </div>
          </div>

          {/* Tour Info */}
          <div className="grid grid-cols-3 gap-4 mb-10">
            <div className="bg-muted/40 rounded-xl p-4">
              <p className="text-xs text-muted-foreground uppercase tracking-wider mb-1">Transport</p>
              <p className="font-medium">{tour.transportType}</p>
            </div>
            <div className="bg-muted/40 rounded-xl p-4">
              <p className="text-xs text-muted-foreground uppercase tracking-wider mb-1">Distance</p>
              <p className="font-medium">{tour.totalDistance}</p>
            </div>
            <div className="bg-muted/40 rounded-xl p-4">
              <p className="text-xs text-muted-foreground uppercase tracking-wider mb-1">Duration</p>
              <p className="font-medium">{tour.estimatedTime}</p>
            </div>
          </div>

          {/* TourLogs Header */}
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-xl font-semibold">Tour Logs</h2>
            <button className="text-sm bg-primary text-primary-foreground rounded px-3 py-1"
              onClick={() => setIsOpen(true)}>
              + Add Log
            </button>
          </div>

          {/* TourLogs Liste */}
          {tourLogs.map((log) => (
              <div key={log.id} className="bg-muted/20 rounded-xl p-4">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="font-medium text-sm">{log.comment}</p>
                    <p className="text-xs text-muted-foreground">{log.dateTime}</p>
                  </div>
                  <div className="flex gap-4 text-xs text-muted-foreground">
                    <span>⭐ {log.rating}/5</span>
                    <span>💪 {log.difficulty}/5</span>
                    <span>{log.totalDistance} km</span>
                    <span>{formatTime(log.totalTime)} min</span>
                  </div>
                </div>
              </div>
          ))}

        </main>
      </div>
  )
}


