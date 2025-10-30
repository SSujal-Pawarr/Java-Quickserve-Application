// config.js - API Configuration

const API_CONFIG = {
    BASE_URL: 'http://localhost:8080/api',
    ENDPOINTS: {
        // Auth
        LOGIN_USER: '/auth/login/user',
        LOGIN_ADMIN: '/auth/login/admin',
        LOGIN_SP: '/auth/login/sp',
        SIGNUP_USER: '/auth/signup/user',
        SIGNUP_ADMIN: '/auth/signup/admin',
        SIGNUP_SP: '/auth/signup/sp',
        
        // Admin
        ADMIN_STATS: '/admin/stats',
        ADMIN_USERS: '/admin/users',
        ADMIN_PROVIDERS: '/admin/providers',
        ADMIN_BOOKINGS: '/admin/bookings',
        
        // Users
        USERS: '/users',
        
        // Providers
        PROVIDERS: '/providers',
        
        // Bookings
        BOOKINGS: '/bookings'
    }
};

// Helper function to build full URL
function buildURL(endpoint) {
    return API_CONFIG.BASE_URL + endpoint;
}