import {createFileRoute} from '@tanstack/react-router'
import {useState} from 'react'
import { Button } from '#components/ui/button'
import { Input } from '#components/ui/input'
import { Card, CardContent, CardHeader, CardTitle } from '#components/ui/card'
import { Label } from '#components/ui/label'
export const Route = createFileRoute('/login')({
    component: LoginPage,
})


function LoginPage() {
    const [username,setUsername] = useState('')
    const [password,setPassword] = useState('')

    const handleLogin = async () => {
        const response = await fetch('http://localhost:8080/api/users/login', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({username,password}),
        })
        const data = await response.json();
        console.log(data);
    }

    return (
        <div className="min-h-screen flex items-center justify-center">
            <Card className="w-full max-w-sm">
                <CardHeader>
                    <CardTitle>Login</CardTitle>
                </CardHeader>
                <CardContent>
                    {/* Input Felder */}
                    <Input className = "mb-2"
                        value = {username}
                        onChange = {e => setUsername(e.target.value)}
                        placeholder = "Username"
                    />
                    <Input className = "mb-4"
                        value = {password}
                        onChange = {e => setPassword(e.target.value)}
                        placeholder = "Passwort"
                    />

                    {/* Login Button */}
                    <Button onClick = {handleLogin}>Login</Button>


                </CardContent>
            </Card>
        </div>
    )
}



