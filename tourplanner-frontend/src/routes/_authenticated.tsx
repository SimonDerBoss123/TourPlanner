import { createFileRoute, redirect, Outlet } from '@tanstack/react-router'

export const Route = createFileRoute('/_authenticated')({
    beforeLoad: ({ context, location }) => {
        if (!context.auth.isAuthenticated) {
            throw redirect({
                to: '/login',
                search: {
                    // Save current location for redirect after login
                    redirect: location.href,
                },
            })
        }
    },
    component: () => <Outlet />,
})

//der guard für alle protected routes. checkt ob man eh eingeloggt ist wenn man zmb dashboard aufrufen will. sonst redirect