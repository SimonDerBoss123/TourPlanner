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
    const [fromSuggestions, setFromSuggestions] = useState([])
    const [toSuggestions, setToSuggestions] = useState([])

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

    const searchFromLocation = async (query: string) => {

        //wenn weniger als 2 zeichen eingetippt aufhören - wenn mehr dann api call machen
        if (query.length < 2) {
            setFromSuggestions([]);
            return;
        }
        const response = await fetch(`https://api.openrouteservice.org/geocode/search?api_key=${import.meta.env.VITE_ORS_API_KEY}&text=${query}&size=5`);
        //gibt listen von orten zurück
        const data = await response.json();

        //von jedem ort das label rausziehen und im state speichern
        const labels = data.features.map((feature: any) => feature.properties.label);
        setFromSuggestions(labels);
    }

    const searchToLocation = async (query: string) => {
        if (query.length < 2) {
            setToSuggestions([]);
            return;
        }
        const response = await fetch(`https://api.openrouteservice.org/geocode/search?api_key=${import.meta.env.VITE_ORS_API_KEY}&text=${query}&size=5`);
        const data = await response.json();

        const labels = data.features.map((feature: any) => feature.properties.label);
        setToSuggestions(labels);
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

                    <div className="relative">
                        <Input
                            placeholder="From"
                            value={fromLocation}
                            onChange={e => {
                                setFromLocation(e.target.value);
                                searchFromLocation(e.target.value);
                            }}
                        />
                        {fromSuggestions.length > 0 && (
                            <div className="absolute z-50 w-full mt-1 bg-background border rounded-xl shadow-lg overflow-hidden">
                                {fromSuggestions.map((suggestion, i) => (
                                    <div
                                        key={i}
                                        className="px-3 py-2 text-sm hover:bg-muted cursor-pointer"
                                        onClick={() => {
                                            setFromLocation(suggestion);
                                            setFromSuggestions([]);
                                        }}>
                                        {suggestion}
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>

                    <div className="relative">
                        <Input
                            placeholder="To"
                            value={to}
                            onChange={e => {
                                setTo(e.target.value);
                                searchToLocation(e.target.value);
                            }}
                        />
                        {toSuggestions.length > 0 && (
                            <div className="absolute z-50 w-full mt-1 bg-background border rounded-xl shadow-lg overflow-hidden">
                                {toSuggestions.map((suggestion, i) => (
                                    <div
                                        key={i}
                                        className="px-3 py-2 text-sm hover:bg-muted cursor-pointer"
                                        onClick={() => {
                                            setTo(suggestion);
                                            setToSuggestions([]);
                                        }}>
                                        {suggestion}
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>

                    <select
                        value={transportType}
                        onChange={e => setTransportType(e.target.value)}
                        className="border rounded-lg px-1 py-2 w-full bg-background"
                    >
                        <option value="">Select transport type</option>
                        <option value="driving-car">Car</option>
                        <option value="cycling-regular">Bike</option>
                        <option value="foot-walking">Walking</option>
                        <option value="hiking">Hiking</option>
                    </select>

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