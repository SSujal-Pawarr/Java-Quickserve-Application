// service_provider.js - Service provider functionality (Backend Integration)

document.addEventListener('DOMContentLoaded', () => {
  checkProviderAuth();
  
  // Load dashboard stats if on dashboard page
  if (document.getElementById('totalBookings')) {
    loadProviderStats();
  }
  
  setupLogout();
});

// Check if service provider is logged in
function checkProviderAuth() {
  const user = getLoggedInUser();
  if (!user || user.role !== 'sp') {
    window.location.href = 'sp_login.html';
    return false;
  }
  
  // Display provider name
  const nameElement = document.getElementById('providerName');
  if (nameElement) {
    nameElement.textContent = user.name;
  }
  
  return true;
}

// Load provider statistics
async function loadProviderStats() {
  const user = getLoggedInUser();
  const provider = await apiGet(`${API_CONFIG.ENDPOINTS.PROVIDERS}/${user.email}`);
  
  if (!provider) return;
  
  const bookings = await apiGet(`${API_CONFIG.ENDPOINTS.BOOKINGS}/service/${provider.serviceType}`);
  
  const totalBookings = bookings.length;
  const pendingBookings = bookings.filter(b => b.status === 'Pending').length;
  const completedBookings = bookings.filter(b => b.status === 'Completed').length;
  const earnings = completedBookings * 500; // Assume ₹500 per job
  
  document.getElementById('totalBookings').textContent = totalBookings;
  document.getElementById('pendingBookings').textContent = pendingBookings;
  document.getElementById('completedBookings').textContent = completedBookings;
  document.getElementById('earnings').textContent = `₹${earnings}`;
}

// Load provider bookings (for sp_bookings.html)
let currentFilter = 'all';

async function loadProviderBookings() {
  const user = getLoggedInUser();
  const provider = await apiGet(`${API_CONFIG.ENDPOINTS.PROVIDERS}/${user.email}`);
  
  if (!provider) return;
  
  let bookings = await apiGet(`${API_CONFIG.ENDPOINTS.BOOKINGS}/service/${provider.serviceType}`);
  
  // Apply filter
  if (currentFilter !== 'all') {
    bookings = bookings.filter(b => b.status === currentFilter);
  }
  
  const container = document.getElementById('bookingsList');
  
  if (!bookings || bookings.length === 0) {
    container.innerHTML = '<p>No bookings found.</p>';
    return;
  }

  container.innerHTML = bookings.map(booking => `
    <div class="booking-card">
      <h3>${booking.service}</h3>
      <p><strong>Customer:</strong> ${booking.customerName}</p>
      <p><strong>Phone:</strong> ${booking.phone}</p>
      <p><strong>Address:</strong> ${booking.address}</p>
      <p><strong>Date:</strong> ${booking.date}</p>
      <p><strong>Time:</strong> ${booking.timeSlot}</p>
      ${booking.notes ? `<p><strong>Notes:</strong> ${booking.notes}</p>` : ''}
      <p><strong>Status:</strong> <span class="status-${booking.status.toLowerCase()}">${booking.status}</span></p>
      <p><small>Booked: ${booking.bookedAt}</small></p>
      
      ${booking.status === 'Pending' ? `
        <div class="button-group-inline">
          <button class="btn-primary" onclick="updateBookingStatus(${booking.id}, 'Accepted')">Accept</button>
          <button class="btn-danger" onclick="updateBookingStatus(${booking.id}, 'Rejected')">Reject</button>
        </div>
      ` : ''}
      
      ${booking.status === 'Accepted' ? `
        <button class="btn-success" onclick="updateBookingStatus(${booking.id}, 'Completed')">Mark as Completed</button>
      ` : ''}
    </div>
  `).join('');
}

// Filter bookings
function filterBookings(status) {
  currentFilter = status;
  
  // Update active button
  document.querySelectorAll('.filter-btn').forEach(btn => {
    btn.classList.remove('active');
  });
  event.target.classList.add('active');
  
  loadProviderBookings();
}

// Update booking status
async function updateBookingStatus(bookingId, newStatus) {
  if (confirm(`Are you sure you want to mark this booking as ${newStatus}?`)) {
    const response = await apiPut(`${API_CONFIG.ENDPOINTS.BOOKINGS}/${bookingId}/status`, {
      status: newStatus
    });
    
    if (response.status === 'success') {
      alert(`✅ Booking ${newStatus.toLowerCase()} successfully!`);
      loadProviderBookings();
    } else {
      alert('❌ ' + response.message);
    }
  }
}

// Load provider profile (for sp_profile.html)
async function loadProfile() {
  const user = getLoggedInUser();
  const provider = await apiGet(`${API_CONFIG.ENDPOINTS.PROVIDERS}/${user.email}`);
  
  if (provider) {
    document.getElementById('profileName').value = provider.name || '';
    document.getElementById('profileEmail').value = provider.email || '';
    document.getElementById('profilePhone').value = provider.phone || '';
    document.getElementById('profileServiceType').value = provider.serviceType || 'Electrician';
    document.getElementById('profileExperience').value = provider.experience || '';
    document.getElementById('profileArea').value = provider.area || '';
    document.getElementById('profileBio').value = provider.bio || '';
  }
}

// Update provider profile
async function updateProviderProfile() {
  const user = getLoggedInUser();
  const updatedData = {
    name: document.getElementById('profileName').value.trim(),
    phone: document.getElementById('profilePhone').value.trim(),
    serviceType: document.getElementById('profileServiceType').value,
    experience: parseInt(document.getElementById('profileExperience').value),
    area: document.getElementById('profileArea').value.trim(),
    bio: document.getElementById('profileBio').value.trim()
  };

  const response = await apiPut(`${API_CONFIG.ENDPOINTS.PROVIDERS}/${user.email}`, updatedData);
  
  if (response.status === 'success') {
    // Update session with new name
    setLoggedInUser(user.role, user.email, updatedData.name);
    alert('✅ Profile updated successfully!');
    window.location.reload();
  } else {
    alert('❌ ' + response.message);
  }
}

// Load availability (for sp_availability.html)
async function loadAvailability() {
  const user = getLoggedInUser();
  const provider = await apiGet(`${API_CONFIG.ENDPOINTS.PROVIDERS}/${user.email}`);
  
  if (provider) {
    document.getElementById('availabilityStatus').value = provider.availabilityStatus || 'Available';
    document.getElementById('workStart').value = provider.workStart || '09:00';
    document.getElementById('workEnd').value = provider.workEnd || '18:00';
    document.getElementById('serviceRadius').value = provider.serviceRadius || 10;
    
    if (provider.workDays) {
      const workDays = provider.workDays.split(',');
      document.querySelectorAll('input[name="workDays"]').forEach(checkbox => {
        checkbox.checked = workDays.includes(checkbox.value);
      });
    }
  }
}

// Save availability
async function saveAvailability() {
  const user = getLoggedInUser();
  const workDays = Array.from(document.querySelectorAll('input[name="workDays"]:checked'))
    .map(cb => cb.value)
    .join(',');

  const availabilityData = {
    status: document.getElementById('availabilityStatus').value,
    workDays: workDays,
    workStart: document.getElementById('workStart').value,
    workEnd: document.getElementById('workEnd').value,
    serviceRadius: parseInt(document.getElementById('serviceRadius').value)
  };

  const response = await apiPut(`${API_CONFIG.ENDPOINTS.PROVIDERS}/${user.email}/availability`, availabilityData);
  
  if (response.status === 'success') {
    alert('✅ Availability updated successfully!');
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