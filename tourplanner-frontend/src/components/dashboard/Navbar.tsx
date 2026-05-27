import * as React from "react"
import {LogOut, Mountain} from "lucide-react";
import {Avatar, AvatarFallback} from "../ui/avatar.tsx";
import {Separator} from "../ui/separator.tsx";
import {Button} from "../ui/button.tsx";
import {useAuth} from "../../auth.tsx";
import {useNavigate} from "@tanstack/react-router";


export function Navbar(){
    const auth =useAuth();
    const navigate = useNavigate();

    const handleLogOut = () => {
        auth.logout();
        navigate({to: '/login'});
    }

    return (
        <nav className="border-b bg-background/80 backdrop-blur-sm sticky top-0 z-10">
            <div className="max-w-6xl mx-auto px-6 h-14 flex items-center justify-between">
                <div className="flex items-center gap-2">
                    <Mountain className="w-5 h-5" />
                    <span className="font-semibold text-sm tracking-tight">TourPlanner</span>
                </div>
                <div className="flex items-center gap-3">
                    <Avatar className="w-7 h-7">
                        <AvatarFallback className="text-xs bg-primary text-primary-foreground">
                            {auth.user?.username?.charAt(0).toUpperCase()}
                        </AvatarFallback>
                    </Avatar>
                    <span className="text-sm text-muted-foreground">{auth.user?.username}</span>
                    <Separator orientation="vertical" className="h-4" />
                    <Button
                        variant="ghost"
                        size="sm"
                        onClick={handleLogOut}
                        className="text-muted-foreground hover:text-foreground gap-1.5"
                    >
                        <LogOut className="w-3.5 h-3.5" />
                        <span className="text-xs">Sign out</span>
                    </Button>
                </div>
            </div>
        </nav>
    )
}