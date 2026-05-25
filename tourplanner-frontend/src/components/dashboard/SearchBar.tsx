import {Search} from "lucide-react";
import {Input} from "../ui/input.tsx";

export interface SearchBarProps{
    search: string,
    onSearch: (value:string) => void
}

export default function SearchBar({search,onSearch} : SearchBarProps){
    return (
        <div className="relative mb-6">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
        <Input
            placeholder="Search tours..."
            value={search}
            onChange={(e) => onSearch(e.target.value)}
            className="pl-9 bg-muted/40 border-0 focus-visible:ring-1"
        />
    </div>
    )
}
