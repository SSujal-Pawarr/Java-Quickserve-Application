// login.js - Handles login for all roles (Backend Integration)

document.addEventListener('DOMContentLoaded', () => {
  
  // ADMIN LOGIN
  const adminForm = document.getElementById('adminLoginForm');
  if (adminForm) {
    adminForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      const email = document.getElementById('adminEmail').value.trim();
      const password = document.getElementById('adminPassword').value.trim();
      
      const response = await apiPost(API_CONFIG.ENDPOINTS.LOGIN_ADMIN, { email, password });
      
      if (response.status === 'success') {
        setLoggedInUser(response.role, response.email, response.name);
        alert(`✅ Welcome back, ${response.name}!`);
        window.location.href = 'admin_dashboard.html';
      } else {
        alert('❌ ' + response.message);
      }
    });
  }

  // USER LOGIN
  const userForm = document.getElementById('userLoginForm');
  if (userForm) {
    userForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      const email = document.getElementById('userEmail').value.trim();
      const password = document.getElementById('userPassword').value.trim();
      
      const response = await apiPost(API_CONFIG.ENDPOINTS.LOGIN_USER, { email, password });
      
      if (response.status === 'success') {
        setLoggedInUser(response.role, response.email, response.name);
        alert(`✅ Welcome back, ${response.name}!`);
        window.location.href = 'user_dashboard.html';
      } else {
        alert('❌ ' + response.message);
      }
    });
  }

  // SERVICE PROVIDER LOGIN
  const spForm = document.getElementById('spLoginForm');
  if (spForm) {
    spForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      const email = document.getElementById('spEmail').value.trim();
      const password = document.getElementById('spPassword').value.trim();
      
      const response = await apiPost(API_CONFIG.ENDPOINTS.LOGIN_SP, { email, password });
      
      if (response.status === 'success') {
        setLoggedInUser(response.role, response.email, response.name);
        alert(`✅ Welcome back, ${response.name}!`);
        window.location.href = 'sp_dashboard.html';
      } else {
        alert('❌ ' + response.message);
      }
    });
  }
});