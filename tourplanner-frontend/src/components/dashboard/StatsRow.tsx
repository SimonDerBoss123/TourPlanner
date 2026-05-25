import {Card, CardContent} from "../ui/card.tsx";


export interface StatsRowProps{
    totalTours: number
    totalDistance: number
    totalLogs: number
}
export default function StatsRow({totalTours,totalDistance,totalLogs} : StatsRowProps){
    return (
        <Card className="border-0 bg-muted/40">
            <CardContent className="pt-5 pb-4">
                <p className="text-xs text-muted-foreground uppercase tracking-wider mb-1">Tour Logs</p>
                <p className="text-2xl font-semibold">
                    {totalDistance}
                </p>
            </CardContent>
        </Card>
    )

}