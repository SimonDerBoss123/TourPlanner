
interface TourRequest {
    name: string
    description: string
    fromLocation: string
    toLocation: string
    transportType: string
}

export const tourService = {
    getAll: async () => {
        const token = localStorage.getItem('auth-token')
        const response = await fetch('http://localhost:8080/api/tours', {
            headers: { Authorization: `Bearer ${token}` }
        })
        return response.json()
    },

    getById: async (id: number) => {
        const token = localStorage.getItem('auth-token');
        const response = await fetch(`http://localhost:8080/api/tours/${id}`,{
            headers: {Authorization: `Bearer ${token}`}
        })
        return response.json();
    },

    create: async (tour: TourRequest) => {
        const token = localStorage.getItem('auth-token')
        const response = await fetch('http://localhost:8080/api/tours', {
            method: 'POST',
            headers: {
                Authorization: `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(tour)
        })
        return response.ok
    },

    update: async(tour: TourRequest, id: number) => {
        const token = localStorage.getItem('auth-token');
        const response = await fetch(`http://localhost:8080/api/tours/${id}`, {
            method: 'PUT',
            headers: {
                Authorization: `Bearer ${token}`,
                'Content-Type' : 'application/json'
            },
            body: JSON.stringify(tour)
        })
        return response.ok
    },

    delete: async(id: number) => {
        const token = localStorage.getItem('auth-token');
        const response = await fetch(`http://localhost:8080/api/tours/${id}`, {
            method: 'DELETE',
            headers: {
                Authorization: `Bearer ${token}`,
            }
        })
        return response.ok;
    },






}