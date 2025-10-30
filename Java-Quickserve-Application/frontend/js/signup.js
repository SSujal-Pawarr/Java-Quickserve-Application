// signup.js - Handles signup for all roles (Backend Integration)

document.addEventListener('DOMContentLoaded', () => {
  
  // ADMIN SIGNUP
  const adminForm = document.getElementById('adminSignupForm');
  if (adminForm) {
    adminForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      
      const signupData = {
        name: document.getElementById('adminName').value.trim(),
        email: document.getElementById('adminEmail').value.trim(),
        password: document.getElementById('adminPassword').value.trim(),
        adminCode: document.getElementById('adminCode').value.trim()
      };

      if (!signupData.name || !signupData.email || !signupData.password || !signupData.adminCode) {
        alert('⚠️ Please fill all fields!');
        return;
      }

      const response = await apiPost(API_CONFIG.ENDPOINTS.SIGNUP_ADMIN, signupData);
      
      if (response.status === 'success') {
        alert('✅ Admin account created! Please login.');
        window.location.href = 'admin_login.html';
      } else {
        alert('❌ ' + response.message);
      }
    });
  }

  // USER SIGNUP
  const userForm = document.getElementById('userSignupForm');
  if (userForm) {
    userForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      
      const signupData = {
        name: document.getElementById('userName').value.trim(),
        email: document.getElementById('userEmail').value.trim(),
        phone: document.getElementById('userPhone').value.trim(),
        password: document.getElementById('userPassword').value.trim()
      };

      if (!signupData.name || !signupData.email || !signupData.phone || !signupData.password) {
        alert('⚠️ Please fill all fields!');
        return;
      }

      const response = await apiPost(API_CONFIG.ENDPOINTS.SIGNUP_USER, signupData);
      
      if (response.status === 'success') {
        alert('✅ User account created! Please login.');
        window.location.href = 'user_login.html';
      } else {
        alert('❌ ' + response.message);
      }
    });
  }

  // SERVICE PROVIDER SIGNUP
  const spForm = document.getElementById('spSignupForm');
  if (spForm) {
    spForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      
      const signupData = {
        name: document.getElementById('spName').value.trim(),
        email: document.getElementById('spEmail').value.trim(),
        phone: document.getElementById('spPhone').value.trim(),
        serviceType: document.getElementById('spServiceType').value,
        experience: parseInt(document.getElementById('spExperience').value),
        area: document.getElementById('spArea').value.trim(),
        password: document.getElementById('spPassword').value.trim()
      };

      if (!signupData.name || !signupData.email || !signupData.phone || 
          !signupData.serviceType || !signupData.experience || 
          !signupData.area || !signupData.password) {
        alert('⚠️ Please fill all fields!');
        return;
      }

      const response = await apiPost(API_CONFIG.ENDPOINTS.SIGNUP_SP, signupData);
      
      if (response.status === 'success') {
        alert('✅ Service Provider account created! Please login.');
        window.location.href = 'sp_login.html';
      } else {
        alert('❌ ' + response.message);
      }
    });
  }
});