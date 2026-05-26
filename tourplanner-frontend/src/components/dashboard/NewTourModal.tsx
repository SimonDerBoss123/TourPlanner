import {useState} from "react";
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '../ui/dialog'
import {Input} from "../ui/input.tsx";
import {Button} from "../ui/button.tsx";



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
        const token = localStorage.getItem('auth-token');
        const response = await fetch('http://localhost:8080/api/tours', {
            method: 'POST',
            headers: {
                Authorization: `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: name, description: description, toLocation: to, fromLocation: fromLocation,
                transportType: transportType
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

                    <Button type="submit"
                    onClick={handleSubmit}>
                        Submit
                    </Button>
                </div>
            </DialogContent>
        </Dialog>

    )
}