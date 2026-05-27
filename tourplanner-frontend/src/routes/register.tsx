import React, { useState } from 'react'
import { Button } from '#components/ui/button'
import { Input } from '#components/ui/input'
import { Card, CardContent, CardHeader, CardTitle } from '#components/ui/card'
import { createFileRoute, redirect } from '@tanstack/react-router'
import {useAuth} from "../auth.tsx";

export const Route = createFileRoute('/register')({
    validateSearch: (search) => ({
        redirect: (search.redirect as string) || '/',
    }),
    beforeLoad: ({ context, search }) => {
        // Redirect if already authenticated
        if (context.auth.isAuthenticated) {
            throw redirect({ to: search.redirect })
        }
    },
    component: RegisterComponent,
})
function RegisterComponent() {
    const navigate = Route.useNavigate()
    const { auth } = Route.useRouteContext()
    const { redirect } = Route.useSearch()
    const [username,setUsername] = useState('')
    const [password,setPassword] = useState('')
    const [isLoading,setIsLoading] = useState(false)
    const [error,setError] = useState('')

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        setIsLoading(true)
        setError('')

        try {
            await auth.register(username, password)
            // Navigate to the redirect URL using router navigation
            navigate({ to: redirect })
        } catch (err) {
            setError('Invalid username or password')
        } finally {
            setIsLoading(false)
        }
    }

    return (
        <div className="min-h-screen flex items-center justify-center">
            <Card className="w-full max-w-sm">
                <CardHeader>
                    <CardTitle>Register</CardTitle>
                </CardHeader>
                <CardContent>
                    {/* Input Felder */}
                    <Input className="mb-2"
                           value={username}
                           onChange={e => setUsername(e.target.value)}
                           placeholder="Username"
                    />
                    <Input className="mb-4"
                           value={password}
                           onChange={e => setPassword(e.target.value)}
                           placeholder="Passwort"
                    />

                    {/* Login Button */}
                    <Button
                        type="submit"
                        onClick={handleSubmit}
                        disabled={isLoading}
                    >
                        Registrieren
                    </Button>


                </CardContent>
            </Card>
        </div>
    )
}
