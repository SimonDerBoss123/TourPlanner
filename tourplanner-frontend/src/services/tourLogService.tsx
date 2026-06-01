
interface TourLogRequest{
    comment: string;
    rating: string;
    difficulty: string;
    totalDistance: string;
    totalTime: string
    dateTime: string;
}

export const tourLogService = {

    getById: async (id:number) => {
        const token = localStorage.getItem('auth-token');
        const response = await fetch(`http://localhost:8080/api/tours/${id}/tourlogs`,{
            headers: { Authorization: `Bearer ${token}` }
        })
        return response.json();
    },

    create: async (tour: TourLogRequest, tourId: number ) => {
        const token = localStorage.getItem('auth-token')
        const response = await fetch(`http://localhost:8080/api/tours/${tourId}/tourlogs`,{
            method: 'POST',
            headers: {
                Authorization: `Bearer ${token}`,
                'Content-Type':'application/json'
            },
            body: JSON.stringify(tour)
        })
        return response.ok;
    },

    delete: async (logId: number, tourId:number) => {
        const token = localStorage.getItem('auth-token');
        const response = await fetch(`http://localhost:8080/api/tours/${tourId}/tourlogs/${logId}`, {
            method: 'DELETE',
            headers: {Authorization:`Bearer ${token}`}
    })
    return response.ok;
    },

    update: async (logId: number, tourId: number, log: TourLogRequest) => {
        const token = localStorage.getItem('auth-token');
        const response = await fetch(`http://localhost:8080/api/tours/${tourId}/tourlogs/${logId}`, {
            method: 'PUT',
            headers: {
                Authorization: `Bearer ${token}`,
                'Content-Type' : 'application/json'
            },
            body: JSON.stringify(log)
        })
        return response.ok;
    }

}