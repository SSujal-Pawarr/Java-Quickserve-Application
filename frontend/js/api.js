// api.js - API Helper Functions

// Generic API call function
async function apiCall(url, options = {}) {
    try {
        const response = await fetch(url, {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });
        
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('API Error:', error);
        return { status: 'error', message: 'Network error. Please check if backend is running.' };
    }
}

// GET request
async function apiGet(endpoint) {
    return await apiCall(buildURL(endpoint), {
        method: 'GET'
    });
}

// POST request
async function apiPost(endpoint, data) {
    return await apiCall(buildURL(endpoint), {
        method: 'POST',
        body: JSON.stringify(data)
    });
}

// PUT request
async function apiPut(endpoint, data) {
    return await apiCall(buildURL(endpoint), {
        method: 'PUT',
        body: JSON.stringify(data)
    });
}

// DELETE request
async function apiDelete(endpoint) {
    return await apiCall(buildURL(endpoint), {
        method: 'DELETE'
    });
}