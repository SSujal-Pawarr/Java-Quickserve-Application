// admin.js - Admin dashboard functionality (Backend Integration)

document.addEventListener('DOMContentLoaded', () => {
  checkAdminAuth();
  
  // Load dashboard stats if on dashboard page
  if (document.getElementById('totalUsers')) {
    loadDashboardStats();
  }
  
  setupLogout();
});

// Check if admin is logged in
function checkAdminAuth() {
  const user = getLoggedInUser();
  if (!user || user.role !== 'admin') {
    window.location.href = 'admin_login.html';
    return false;
  }
  
  // Display admin name
  const nameElement = document.getElementById('adminName');
  if (nameElement) {
    nameElement.textContent = user.name;
  }
  
  return true;
}

// Load dashboard statistics from backend
async function loadDashboardStats() {
  const stats = await apiGet(API_CONFIG.ENDPOINTS.ADMIN_STATS);
  
  if (stats) {
    document.getElementById('totalUsers').textContent = stats.totalUsers || 0;
    document.getElementById('totalProviders').textContent = stats.totalProviders || 0;
    document.getElementById('totalBookings').textContent = stats.totalBookings || 0;
    document.getElementById('pendingBookings').textContent = stats.pendingBookings || 0;
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

// Load users list (for admin_users.html)
async function loadUsersList() {
  const users = await apiGet(API_CONFIG.ENDPOINTS.ADMIN_USERS);
  const container = document.getElementById('usersList');
  
  if (!users || users.length === 0) {
    container.innerHTML = '<p>No users registered yet.</p>';
    return;
  }

  container.innerHTML = `
    <table>
      <thead>
        <tr>
          <th>Name</th>
          <th>Email</th>
          <th>Phone</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        ${users.map(user => `
          <tr>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>${user.phone || 'N/A'}</td>
            <td><button class="btn-danger" onclick="deleteUser(${user.id})">Delete</button></td>
          </tr>
        `).join('')}
      </tbody>
    </table>
  `;
}

// Delete user
async function deleteUser(userId) {
  if (confirm('Are you sure you want to delete this user?')) {
    const response = await apiDelete(`${API_CONFIG.ENDPOINTS.ADMIN_USERS}/${userId}`);
    
    if (response.status === 'success') {
      alert('✅ User deleted successfully!');
      loadUsersList();
    } else {
      alert('❌ ' + response.message);
    }
  }
}

// Load providers list (for admin_providers.html)
async function loadProvidersList() {
  const providers = await apiGet(API_CONFIG.ENDPOINTS.ADMIN_PROVIDERS);
  const container = document.getElementById('providersList');
  
  if (!providers || providers.length === 0) {
    container.innerHTML = '<p>No service providers registered yet.</p>';
    return;
  }

  container.innerHTML = `
    <table>
      <thead>
        <tr>
          <th>Business Name</th>
          <th>Email</th>
          <th>Service Type</th>
          <th>Experience</th>
          <th>Area</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        ${providers.map(provider => `
          <tr>
            <td>${provider.name}</td>
            <td>${provider.email}</td>
            <td>${provider.serviceType || 'N/A'}</td>
            <td>${provider.experience || 'N/A'} years</td>
            <td>${provider.area || 'N/A'}</td>
            <td><button class="btn-danger" onclick="deleteProvider(${provider.id})">Delete</button></td>
          </tr>
        `).join('')}
      </tbody>
    </table>
  `;
}

// Delete provider
async function deleteProvider(providerId) {
  if (confirm('Are you sure you want to delete this provider?')) {
    const response = await apiDelete(`${API_CONFIG.ENDPOINTS.ADMIN_PROVIDERS}/${providerId}`);
    
    if (response.status === 'success') {
      alert('✅ Provider deleted successfully!');
      loadProvidersList();
    } else {
      alert('❌ ' + response.message);
    }
  }
}

// Load all bookings (for admin_bookings.html)
async function loadAllBookings() {
  const bookings = await apiGet(API_CONFIG.ENDPOINTS.ADMIN_BOOKINGS);
  const container = document.getElementById('bookingsList');
  
  if (!bookings || bookings.length === 0) {
    container.innerHTML = '<p>No bookings yet.</p>';
    return;
  }

  container.innerHTML = bookings.map(booking => `
    <div class="booking-card">
      <h3>${booking.service}</h3>
      <p><strong>Customer:</strong> ${booking.customerName}</p>
      <p><strong>Email:</strong> ${booking.customerEmail}</p>
      <p><strong>Phone:</strong> ${booking.phone}</p>
      <p><strong>Address:</strong> ${booking.address}</p>
      <p><strong>Date:</strong> ${booking.date}</p>
      <p><strong>Time:</strong> ${booking.timeSlot}</p>
      <p><strong>Status:</strong> <span class="status-${booking.status.toLowerCase()}">${booking.status}</span></p>
      ${booking.notes ? `<p><strong>Notes:</strong> ${booking.notes}</p>` : ''}
      <p><small>Booked: ${booking.bookedAt}</small></p>
    </div>
  `).join('');
}