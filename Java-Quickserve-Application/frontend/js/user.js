// user.js - User dashboard functionality (Backend Integration)

document.addEventListener('DOMContentLoaded', () => {
  checkUserAuth();
  
  // Load recent bookings if on dashboard
  if (document.getElementById('recentBookings')) {
    loadRecentBookings();
  }
  
  setupLogout();
});

// Check if user is logged in
function checkUserAuth() {
  const user = getLoggedInUser();
  if (!user || user.role !== 'user') {
    window.location.href = 'user_login.html';
    return false;
  }
  
  // Display user name
  const nameElement = document.getElementById('userName');
  if (nameElement) {
    nameElement.textContent = user.name;
  }
  
  return true;
}

// Load recent bookings on dashboard
async function loadRecentBookings() {
  const user = getLoggedInUser();
  const bookings = await apiGet(`${API_CONFIG.ENDPOINTS.BOOKINGS}/user/${user.email}`);
  const container = document.getElementById('recentBookings');
  
  if (!bookings || bookings.length === 0) {
    container.innerHTML = '<p>No recent bookings</p>';
    return;
  }
  
  // Show only last 3 bookings
  const recentBookings = bookings.slice(-3).reverse();
  
  container.innerHTML = recentBookings.map(booking => `
    <div class="booking-card-mini">
      <h4>${booking.service}</h4>
      <p>${booking.date} - ${booking.timeSlot}</p>
      <span class="status-badge status-${booking.status.toLowerCase()}">${booking.status}</span>
    </div>
  `).join('');
}

// Load all user bookings (for user_bookings.html)
async function loadMyBookings() {
  const user = getLoggedInUser();
  const bookings = await apiGet(`${API_CONFIG.ENDPOINTS.BOOKINGS}/user/${user.email}`);
  const container = document.getElementById('bookingsList');
  
  if (!bookings || bookings.length === 0) {
    container.innerHTML = '<p>No bookings yet. <a href="user_browse_services.html">Book a service now!</a></p>';
    return;
  }

  container.innerHTML = bookings.map(booking => `
    <div class="booking-card">
      <h3>${booking.service}</h3>
      <p><strong>Date:</strong> ${booking.date}</p>
      <p><strong>Time:</strong> ${booking.timeSlot}</p>
      <p><strong>Address:</strong> ${booking.address}</p>
      <p><strong>Phone:</strong> ${booking.phone}</p>
      ${booking.notes ? `<p><strong>Notes:</strong> ${booking.notes}</p>` : ''}
      <p><strong>Status:</strong> <span class="status-${booking.status.toLowerCase()}">${booking.status}</span></p>
      <p><small>Booked on: ${booking.bookedAt}</small></p>
      ${booking.status === 'Pending' ? `<button class="btn-danger" onclick="cancelBooking(${booking.id})">Cancel Booking</button>` : ''}
    </div>
  `).join('');
}

// Cancel booking
async function cancelBooking(bookingId) {
  if (confirm('Are you sure you want to cancel this booking?')) {
    const response = await apiDelete(`${API_CONFIG.ENDPOINTS.BOOKINGS}/${bookingId}`);
    
    if (response.status === 'success') {
      alert('✅ Booking cancelled successfully!');
      loadMyBookings();
    } else {
      alert('❌ ' + response.message);
    }
  }
}

// Load user profile (for user_profile.html)
async function loadProfile() {
  const user = getLoggedInUser();
  const userDetails = await apiGet(`${API_CONFIG.ENDPOINTS.USERS}/${user.email}`);
  
  if (userDetails) {
    document.getElementById('profileName').value = userDetails.name || '';
    document.getElementById('profileEmail').value = userDetails.email || '';
    document.getElementById('profilePhone').value = userDetails.phone || '';
    document.getElementById('profileAddress').value = userDetails.address || '';
  }
}

// Update user profile
async function updateUserProfile() {
  const user = getLoggedInUser();
  const updatedData = {
    name: document.getElementById('profileName').value.trim(),
    phone: document.getElementById('profilePhone').value.trim(),
    address: document.getElementById('profileAddress').value.trim()
  };

  const response = await apiPut(`${API_CONFIG.ENDPOINTS.USERS}/${user.email}`, updatedData);
  
  if (response.status === 'success') {
    // Update session with new name
    setLoggedInUser(user.role, user.email, updatedData.name);
    alert('✅ Profile updated successfully!');
    window.location.reload();
  } else {
    alert('❌ ' + response.message);
  }
}

// Setup logout functionality
function setupLogout() {
  const logoutBtn = document.getElementById('logoutBtn');
  if (logoutBtn) {
    logoutBtn.addEventListener('click', (e) => {
      e.preventDefault();
      if (confirm('Are you sure you want to logout?')) {
        logoutUser();
      }
    });
  }
}

// Create booking (called from user_book_service.html)
async function createBooking(bookingData) {
  const response = await apiPost(API_CONFIG.ENDPOINTS.BOOKINGS, bookingData);
  
  if (response.status === 'success') {
    alert(`✅ Booking Confirmed for ${bookingData.service}!`);
    window.location.href = 'user_bookings.html';
  } else {
    alert('❌ ' + response.message);
  }
}