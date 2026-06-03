import {useEffect, useState} from "react";
import {tourLogService} from "../../services/tourLogService.tsx";
import {Dialog, DialogContent, DialogHeader, DialogTitle} from "../ui/dialog"
import {Input} from "../ui/input"
import {Button} from "../ui/button"

export interface EditTourLogModalProps{
    isOpen: boolean,
    onClose: ()=> void,
    onSuccess: () => void
    logId: number,
    tourId: number,
    tourLog: {
        comment: string,
        difficulty: string,
        rating: string,
        totalTime: string,
        dateTime: string,
        totalDistance: string
    }
}

export default function EditTourLogModal({onSuccess,isOpen,onClose,logId,tourId,tourLog}: EditTourLogModalProps){
    const [comment,setComment] = useState(tourLog.comment);
    const [difficulty,setDifficulty] = useState(tourLog.difficulty);
    const [rating,setRating] = useState(tourLog.rating);
    const [totalTime,setTotalTime] = useState(tourLog.totalTime);
    const [dateTime, setDateTime] = useState(tourLog.dateTime);
    const [totalDistance,setTotalDistance] = useState(tourLog.totalDistance);


    useEffect(() => {
        setComment(tourLog.comment)
        setDifficulty(tourLog.difficulty)
        setRating(tourLog.rating)
        setTotalTime(tourLog.totalTime)
        setDateTime(tourLog.dateTime)
        setTotalDistance(tourLog.totalDistance)
    }, [tourLog])

    const handleSubmit = async () => {
        const response = await tourLogService.update(Number(logId),Number(tourId),
            {comment,difficulty,rating,totalDistance,totalTime,dateTime}
        );
        if(response){
            onClose()
            onSuccess()
        }
    }

    return (
        <Dialog open={isOpen} onOpenChange={onClose}>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle>Edit Tour Log</DialogTitle>
                </DialogHeader>
                {/* Formular hier */}
                <div className="flex flex-col gap-4">
                    <Input
                        placeholder="Comment"
                        value={comment}
                        onChange={e => setComment(e.target.value)}
                    />
                    <Input
                        placeholder="Difficulty"
                        value={difficulty}
                        onChange={e => setDifficulty(e.target.value)}
                    />
                    <Input
                        placeholder="Rating"
                        value={rating}
                        onChange={e => setRating(e.target.value)}
                    />
                    <Input
                        placeholder="Total distance in km"
                        value={totalDistance}
                        onChange={e => setTotalDistance(e.target.value)}
                    />
                    <Input
                        placeholder="Total time in minutes"
                        value={totalTime}
                        onChange={e => setTotalTime(e.target.value)}
                    />
                    <Input
                        placeholder="Date"
                        type="datetime-local"
                        value={dateTime}
                        onChange={e => setDateTime(e.target.value)}
                    />

                    <Button type="submit"
                            onClick={handleSubmit}>
                        Edit
                    </Button>
                </div>
            </DialogContent>
        </Dialog>
    )

}