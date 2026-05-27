import React, { useState } from 'react'
import { Button } from '#components/ui/button'
import { Input } from '#components/ui/input'
import { Card, CardContent, CardHeader, CardTitle } from '#components/ui/card'
import {createFileRoute, Link, redirect} from '@tanstack/react-router'

export const Route = createFileRoute('/login')({
    validateSearch: (search) => ({
        redirect: (search.redirect as string) || '/',
    }),
    beforeLoad: ({ context, search }) => {
        // Redirect if already authenticated
        if (context.auth.isAuthenticated) {
            throw redirect({ to: search.redirect })
        }
    },
    component: LoginComponent,
})

function LoginComponent() {
    const { auth } = Route.useRouteContext()
    const { redirect } = Route.useSearch()
    const navigate = Route.useNavigate()
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [isLoading, setIsLoading] = useState(false)
    const [error, setError] = useState('')

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        setIsLoading(true)
        setError('')

        try {
            await auth.login(username, password)
            // Navigate to the redirect URL using router navigation
            navigate({ to: redirect })
        } catch (err) {
            setError('Invalid username or password')
        } finally {
            setIsLoading(false)
        }
    }

    return (
        <div className="min-h-screen flex items-center justify-center flex-col">
            <Card className="w-full max-w-sm mb-2">
                <CardHeader>
                    <CardTitle>Login</CardTitle>
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
                        Login
                    </Button>


                </CardContent>
            </Card>
            <p className="text-sm text-center text-muted-foreground mt-4">
                Noch kein Account? {' '}
                <Link to="/register" className="underline">
                    Hier registrieren.
                </Link>
            </p>
        </div>

    )
}


