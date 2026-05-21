import { createFileRoute } from '@tanstack/react-router'
import * as React from 'react'

export const Route = createFileRoute('/')({
    component: HomeComponent,
})

function HomeComponent() {
    return (
        <div className="p-2 bg-red-500 text-white text-4xl">
            <h3>Welcome Home!</h3>
        </div>
    )
}