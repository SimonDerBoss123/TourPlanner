import {useState} from "react";
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '../ui/dialog'
import {Input} from "../ui/input.tsx";
import {Button} from "../ui/button.tsx";
import {tourService} from "../../services/tourService.tsx";



export interface NewTourModalProps{
    isOpen: boolean,
    onClose: () => void
    onSuccess: () => void
}


export default function NewTourModal({isOpen,onClose,onSuccess} : NewTourModalProps){
    const [name,setName] = useState('')
    const [description,setDescription] = useState('')
    const [fromLocation, setFromLocation] = useState('')
    const [to, setTo] = useState('')
    const [transportType,setTransportType] = useState('')

    const handleSubmit = async () => {
        //validate input

        if(!name || !description || !fromLocation || !to || !transportType) return

        const response = await tourService.create(
            {name, description, fromLocation, toLocation: to, transportType}
        )
        if(response){
            setName('')
            setDescription('')
            setFromLocation('')
            setTo('')
            setTransportType('')
            onClose();
            onSuccess();
        }
    }

    return (
        <Dialog open={isOpen} onOpenChange={onClose}>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle>New Tour</DialogTitle>
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
                    <Input
                        placeholder="transport type"
                        value={transportType}
                        onChange={e => setTransportType(e.target.value)}
                    />

                    <Button
                        type="submit"
                        onClick={handleSubmit}
                        disabled={!name || !description || !fromLocation || !to || !transportType}
                    >
                        Submit
                    </Button>
                </div>
            </DialogContent>
        </Dialog>

    )
}