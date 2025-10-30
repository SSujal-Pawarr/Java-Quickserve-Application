// storage.js - Session Management (Backend handles data storage)

// Session Management (stored in sessionStorage for security)
function setLoggedInUser(role, email, name) {
    const userData = { role, email, name };
    try {
        sessionStorage.setItem('loggedInUser', JSON.stringify(userData));
    } catch (e) {
        console.error('Storage error:', e);
    }
}

function getLoggedInUser() {
    try {
        const data = sessionStorage.getItem('loggedInUser');
        return data ? JSON.parse(data) : null;
    } catch (e) {
        return null;
    }
}

function logoutUser() {
    sessionStorage.removeItem('loggedInUser');
    window.location.href = 'index.html';
}

// Check if user is logged in
function isLoggedIn() {
    return getLoggedInUser() !== null;
}

// Check specific role
function checkRole(expectedRole) {
    const user = getLoggedInUser();
    return user && user.role === expectedRole;
}