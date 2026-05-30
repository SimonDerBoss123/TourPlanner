import {useState} from "react";
import {Dialog, DialogContent, DialogHeader, DialogTitle} from "../ui/dialog.tsx";
import {Input} from "../ui/input.tsx";
import {Button} from "../ui/button.tsx";


export interface NewTourLogModalProps{
    isOpen: boolean,
    onClose: () => void,
    onSuccess: () => void,
    tourId: string
}


export default function NewTourLogModal({isOpen,onClose,onSuccess,tourId}: NewTourLogModalProps){
    const [comment,setComment] = useState('');
    const [difficulty,setDifficulty] = useState('');
    const [rating,setRating] = useState('');
    const [totalTime,setTotalTime] = useState('');
    const [dateTime, setDateTime] = useState('');
    const [totalDistance,setTotalDistance] = useState('');

    const handleSubmit = async () => {
        const token = localStorage.getItem('auth-token');
        const response = await fetch(`http://localhost:8080/api/tours/${tourId}/tourlogs`,{
            method: 'POST',
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                    comment: comment, difficulty: parseInt(difficulty), rating: parseInt(rating), totalTime: parseInt(totalTime), dateTime: dateTime, totalDistance: parseInt(totalDistance)
                })
        })
        if(response.ok){
            onClose();
            onSuccess();
        }
    }


    return (
        <Dialog open={isOpen} onOpenChange={onClose}>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle>New Tour Log</DialogTitle>
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
                        Add
                    </Button>
                </div>
            </DialogContent>
        </Dialog>
    )


}