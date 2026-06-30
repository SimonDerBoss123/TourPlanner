import {createFileRoute} from '@tanstack/react-router'
import {Navbar} from "../../../components/dashboard/Navbar.tsx";
import NewTourLogModal from "../../../components/dashboard/NewTourLogModal.tsx";
import EditTourModal from "../../../components/dashboard/EditTourModal.tsx";
import EditTourLogModal from "../../../components/dashboard/EditTourLogModal.tsx";
import { useTourDetail } from '../../../hooks/useTourDetail'
import {MapContainer, Polyline, TileLayer} from "react-leaflet";
import polyline from '@mapbox/polyline';




export const Route = createFileRoute('/_authenticated/tours/$tourId')({
  component: TourDetailPage,
})


function TourDetailPage() {
  const {tourId} = Route.useParams()
  const {
    tour,
    tourLogs,
    isOpen,
    setIsOpen,
    isEditOpen,
    setIsEditOpen,
    isLogEditOpen,
    setIsLogEditOpen,
    selectedLog,
    setSelectedLog,
    fetchTour,
    fetchTourLogs,
    formatTime,
    navigate,
    deleteTourLog,
    deleteTour
  } = useTourDetail(tourId)
  if (!tour) return <div>Loading...</div>

  const positions = tour.geometry
      ? polyline.decode(tour.geometry).map(([lat, lng]) => [lat, lng])
      : [[51.505, -0.09]];

  return (
      <div className="min-h-screen bg-background">
        <Navbar />

        <NewTourLogModal
            tourId={tourId}
            isOpen={isOpen}
            onClose={() => setIsOpen(false)}
            onSuccess={fetchTourLogs}
        />
        <EditTourModal
            isOpen={isEditOpen}
            onClose={() => setIsEditOpen(false)}
            onSuccess={fetchTour}
            tour={tour}
            tourId={tourId}
        />
        {selectedLog && (
            <EditTourLogModal
                isOpen={isLogEditOpen}
                onClose={() => setIsLogEditOpen(false)}
                onSuccess={fetchTourLogs}
                logId={selectedLog?.id}
                tourId={Number(tourId)}
                tourLog={selectedLog}
            />
        )}

        <main className="max-w-5xl mx-auto px-12 py-10">

          <button
              className="text-xs text-muted-foreground mb-8 flex items-center gap-1.5 cursor-pointer hover:text-foreground transition-colors"
              onClick={() => navigate({to: '/dashboard'})}>
            ← Back to Tours
          </button>

          <div className="flex items-start justify-between mb-6">
            <div>
              <h1 className="text-3xl font-semibold tracking-tight">{tour.name}</h1>
              <p className="text-muted-foreground text-sm mt-1">{tour.fromLocation} → {tour.toLocation}</p>
            </div>
            <div className="flex gap-2 mt-1">
              <button
                  className="cursor-pointer text-xs px-3 py-1.5 rounded-lg border bg-background hover:bg-muted transition-colors"
                  onClick={() => setIsEditOpen(true)}>
                Edit
              </button>
              <button
                  className="cursor-pointer text-xs px-3 py-1.5 rounded-lg text-destructive border border-destructive/30 hover:bg-destructive/5 transition-colors"
                  onClick={deleteTour}>
                Delete
              </button>
            </div>
          </div>

          <div className="grid grid-cols-3 gap-3 mb-8">
            <div className="rounded-xl border bg-muted/20 px-4 py-3">
              <p className="text-xs text-muted-foreground uppercase tracking-widest mb-1">Transport</p>
              <p className="text-sm font-medium">{tour.transportType || '—'}</p>
            </div>
            <div className="rounded-xl border bg-muted/20 px-4 py-3">
              <p className="text-xs text-muted-foreground uppercase tracking-widest mb-1">Distance</p>
              <p className="text-sm font-medium">{tour.tourDistance ? `${tour.tourDistance} km` : '—'}</p>
            </div>
            <div className="rounded-xl border bg-muted/20 px-4 py-3">
              <p className="text-xs text-muted-foreground uppercase tracking-widest mb-1">Duration</p>
              <p className="text-sm font-medium">{tour.estimatedTime ? `${tour.estimatedTime} min` : '—'}</p>
            </div>
          </div>

          <div className="grid grid-cols-2 gap-5">

            <div className="rounded-xl overflow-hidden border" style={{ height: '380px', position: 'relative', zIndex: 0 }}>
            <MapContainer center={positions[0]} zoom={13} scrollWheelZoom={false} style={{ height: '100%', width: '100%' }}>
              <TileLayer
                  attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                  url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
              />
              <Polyline positions={positions} color="blue" />
            </MapContainer>
          </div>

            <div className="flex flex-col" style={{ height: '380px' }}>
              <div className="flex items-center justify-between mb-3">
                <h2 className="text-sm font-semibold">Tour Logs</h2>
                <button
                    className="cursor-pointer text-xs px-3 py-1.5 rounded-lg bg-foreground text-background hover:opacity-80 transition-opacity"
                    onClick={() => setIsOpen(true)}>
                  + Add Log
                </button>
              </div>

              <div className="flex flex-col gap-2 overflow-y-auto pr-1">
                {tourLogs.length === 0 && (
                    <p className="text-xs text-muted-foreground text-center mt-10">No logs yet.</p>
                )}
                {tourLogs.map((log) => (
                    <div key={log.id} className="rounded-xl border px-4 py-3 hover:bg-muted/20 transition-colors">
                      <div className="flex items-start justify-between mb-2.5">
                        <div>
                          <p className="text-xs font-medium mb-4">{log.comment}</p>
                          <p className="text-xs text-muted-foreground mt-0.5">{log.dateTime}</p>
                        </div>
                        <div className="flex gap-3">
                          <button
                              className="text-xs text-muted-foreground hover:text-foreground cursor-pointer transition-colors"
                              onClick={() => { setSelectedLog(log); setIsLogEditOpen(true); }}>
                            Edit
                          </button>
                          <button
                              className="text-xs text-destructive hover:opacity-70 cursor-pointer transition-opacity"
                              onClick={() => deleteTourLog(log.id)}>
                            Delete
                          </button>
                        </div>
                      </div>
                      <div className="flex gap-3 text-xs text-muted-foreground">
                        <span>Rating {log.rating}/5</span>
                        <span>Difficulty {log.difficulty}/5</span>
                        <span>{log.totalDistance} km</span>
                        <span>{formatTime(log.totalTime)}</span>
                      </div>
                    </div>
                ))}
              </div>
            </div>

          </div>

        </main>
      </div>
  )
  }