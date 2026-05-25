import { createFileRoute } from '@tanstack/react-router'
import {useEffect, useState} from 'react'
import { Button } from '#components/ui/button'
import {Plus} from 'lucide-react'
import {Navbar} from "../../components/dashboard/Navbar.tsx";
import type {Tour} from "../../components/dashboard/TourCard.tsx";
import {useAuth} from "../../auth.tsx";
import SearchBar from "../../components/dashboard/SearchBar.tsx";
import TourCard from "../../components/dashboard/TourCard.tsx";
export const Route = createFileRoute('/_authenticated/dashboard')({
    component: DashboardComponent,
})



function DashboardComponent() {
    const auth = useAuth()
    const [search, setSearch] = useState('')
    const[tours, setTours] = useState<Tour[]>([])

useEffect(() =>{
    const fetchTours = async () => {
       const token = localStorage.getItem('auth-token');
       const response = await fetch('http://localhost:8080/api/tours',{
           headers: {Authorization: `Bearer ${token}`}
       });
       const data = await response.json();
       setTours(data);
    }
    fetchTours();
},[])

    return (
        <div className="min-h-screen bg-background">
            <Navbar />
            <main className="max-w-6xl mx-auto px-6 py-10">

                {/* Header */}
                <div className="flex items-end justify-between mb-8">
                    <div>
                        <p className="text-sm text-muted-foreground mb-1">Good morning,</p>
                        <h1 className="text-3xl font-semibold tracking-tight">
                            {auth.user?.username}
                        </h1>
                    </div>
                    <Button size="sm" className="gap-1.5">
                        <Plus className="w-4 h-4" />
                        New Tour
                    </Button>
                </div>


                {/* SearchBar */}
                <SearchBar search={search} onSearch={setSearch} />

                {/* Tour Liste */}
                <div className="space-y-2">
                    {tours.map((tour) => (
                        <TourCard key={tour.id} tour={tour} />
                    ))}
                </div>

            </main>
        </div>
    )
}