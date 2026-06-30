import {useState} from "react";
import {tourService} from "../../services/tourService.tsx";
import {Dialog, DialogContent, DialogHeader, DialogTitle} from "#components/ui/dialog";
import {Input} from "#components/ui/input";
import {Button} from "#components/ui/button";


export interface EditTourModalProps{
    isOpen: boolean,
    onClose: () => void,
    onSuccess: () => void
    tour: {
        name: string
        description: string
        fromLocation: string
        toLocation: string
        transportType: string
    },
    tourId: string
}

export default function EditTourModal({isOpen,onClose,onSuccess, tourId, tour} : EditTourModalProps){
    const [name,setName] = useState(tour.name)
    const [description,setDescription] = useState(tour.description)
    const [fromLocation, setFromLocation] = useState(tour.fromLocation)
    const [to, setTo] = useState(tour.toLocation)
    const [transportType,setTransportType] = useState(tour.transportType)


    const handleSubmit = async() => {
        const response = await tourService.update(
            {name, description, fromLocation, toLocation: to, transportType}, Number(tourId)
        )
        if(response){
            onClose()
            onSuccess()
        }
    }

    return (
        <Dialog open={isOpen} onOpenChange={onClose}>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle>Edit Tour</DialogTitle>
                </DialogHeader>
                {/* Formular hier */}
                <div className="flex flex-col gap-4">
                    <Input
                        placeholder="Name"
                        value={name}
                        onChange={e => setName(e.target.value)}
                    />
                    <Input
                        placeholder="Description"
                        value={description}
                        onChange={e => setDescription(e.target.value)}
                    />
                    <Input
                        placeholder="From"
                        value={fromLocation}
                        onChange={e => setFromLocation(e.target.value)}
                    />
                    <Input
                        placeholder="to"
                        value={to}
                        onChange={e => setTo(e.target.value)}
                    />
                    <select
                        value={transportType}
                        onChange={e => setTransportType(e.target.value)}
                        className="border rounded-lg px-3 py-2 text-sm w-full bg-background"
                    >
                        <option value="">Select transport type</option>
                        <option value="driving-car">Car</option>
                        <option value="cycling-regular">Bike</option>
                        <option value="foot-walking">Walking</option>
                        <option value="hiking">Hiking</option>
                    </select>

                    <Button type="submit"
                            onClick={handleSubmit}>
                        Edit
                    </Button>
                </div>
            </DialogContent>
        </Dialog>
    )

}