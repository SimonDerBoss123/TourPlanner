import {Card, CardContent} from "../ui/card.tsx";
import { ChevronRight, Clock, MapPin, Mountain, Route as RouteIcon } from 'lucide-react'
import { Badge } from '../ui/badge'
import { Link } from '@tanstack/react-router'
export interface Tour {
    id: number
    name: string
    fromLocation: string
    toLocation: string
    transportType: string
    tourDistance: number
    estimatedTime: number
    tourLogs: number
}
export interface TourCardProps {
    tour: Tour
}

export default function TourCard({ tour }: TourCardProps) {
    return (
        <Link to="/tours/$tourId" params={{tourId: String(tour.id)}}>
        <Card className="border-0 bg-muted/20 hover:bg-muted/40 transition-colors cursor-pointer group">
            <CardContent className="py-4 px-5">
                <div className="flex items-center justify-between">
                    <div className="flex items-center gap-4">
                        <div className="w-9 h-9 rounded-xl bg-primary/10 flex items-center justify-center">
                            <Mountain className="w-4 h-4 text-primary" />
                        </div>
                        <div>
                            <div className="flex items-center gap-2 mb-0.5">
                                <p className="font-medium text-sm">{tour.name}</p>
                                <Badge variant="secondary" className="text-xs px-1.5 py-0 h-4">
                                    {tour.transportType}
                                </Badge>
                            </div>
                            <div className="flex items-center gap-1 text-xs text-muted-foreground">
                                <MapPin className="w-3 h-3" />
                                <span>{tour.fromLocation}</span>
                                <span>→</span>
                                <span>{tour.toLocation}</span>
                            </div>
                        </div>
                    </div>
                    <div className="flex items-center gap-6">
                        <div className="text-right hidden sm:block">
                            <div className="flex items-center gap-1 text-xs text-muted-foreground justify-end">
                                <RouteIcon className="w-3 h-3" />
                                <span>{tour.tourDistance} km</span>
                            </div>
                            <div className="flex items-center gap-1 text-xs text-muted-foreground justify-end mt-0.5">
                                <Clock className="w-3 h-3" />
                                <span>{tour.estimatedTime} min</span>
                            </div>
                        </div>
                        <Badge variant="outline" className="text-xs">
                            {tour.tourLogs} logs
                        </Badge>
                        <ChevronRight className="w-4 h-4 text-muted-foreground group-hover:text-foreground transition-colors" />
                    </div>
                </div>
            </CardContent>
        </Card>
        </Link>
    )
}
