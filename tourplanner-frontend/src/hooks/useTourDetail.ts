import {useEffect, useState} from "react";
import {tourService} from "../services/tourService.tsx";
import {tourLogService} from "../services/tourLogService.tsx";
import {useNavigate} from "@tanstack/react-router";

export interface Tour {
    id: number
    name: string
    fromLocation: string
    toLocation: string
    transportType: string
    tourDistance: number
    estimatedTime: number
    geometry: string
    popularity: number
    childFriendliness: boolean
    description: string
}

export interface TourLog {
    id: number
    comment: string
    dateTime: string
    difficulty: number
    rating: number
    totalDistance: number
    totalTime: number
}

export function useTourDetail(tourId:string) {

    const [tour, setTour] = useState<Tour | null>(null);
    const [tourLogs, setTourLogs] = useState<TourLog[]>([]);
    const [isOpen, setIsOpen] = useState(false);
    const [isEditOpen, setIsEditOpen] = useState(false)
    const [selectedLog, setSelectedLog] = useState<TourLog | null>(null)
    const [isLogEditOpen, setIsLogEditOpen] = useState(false)

    useEffect(() => {
        fetchTour();
        fetchTourLogs();
    }, [tourId])

    const fetchTourLogs = async () => {
        const response = await tourLogService.getById(Number(tourId));
        setTourLogs(response);
    }


    const fetchTour = async () => {
        const response = await tourService.getById(Number(tourId));
        setTour(response);
    }

    const formatTime = (totalMinutes: number) => {
        const hours = Math.floor(totalMinutes / 60)
        const minutes = totalMinutes % 60
        if (hours === 0) return `${minutes}m`
        if (minutes === 0) return `${hours}h`
        return `${hours}h ${minutes}m`
    }


    const navigate = useNavigate();

    const deleteTour = async () => {
        await tourService.delete(Number(tourId))
        navigate({ to: '/dashboard' })
    }

    const deleteTourLog = async (logId: number) => {
        await tourLogService.delete(logId, Number(tourId))
        fetchTourLogs()
        fetchTour()
    }


    return {
        tour,
        tourLogs,
        isOpen, setIsOpen,
        isEditOpen, setIsEditOpen,
        isLogEditOpen, setIsLogEditOpen,
        selectedLog, setSelectedLog,
        fetchTour,
        fetchTourLogs,
        formatTime,
        navigate,
        deleteTour,
        deleteTourLog
    }
}