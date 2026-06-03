import {useAuth} from "../auth.tsx";
import {useEffect, useState} from "react";
import type { Tour } from '../components/dashboard/TourCard';
import {tourService} from "../services/tourService.tsx";

export function useDashboard() {

    const auth = useAuth()
    const [search, setSearch] = useState('')
    const [tours, setTours] = useState<Tour[]>([])
    const [isOpen,setIsOpen] = useState(false)


    const fetchTours = async () => {
        const data = await tourService.getAll();
        setTours(data);
    }

    useEffect(() =>{
        fetchTours();
    },[])


    return { auth, tours, search, setSearch, isOpen, setIsOpen, fetchTours }
}