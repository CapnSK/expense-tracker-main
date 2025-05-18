<template>
  <div id="app" class="container">
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'App'
};
</script>

<style>
.container {
  max-width: fit-content;
  margin: 30px auto;
  padding: 20px;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.4);
  background-color: #fff;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}
input,
select {
  padding: 10px;
  margin: 6px 4px;
  border-radius: 8px;
  border: 1px solid #ccc;
  font-size: 14px;
  width: auto;
}

input:focus,
select:focus {
  border-color: #3c9cdb;
  outline: none;
  box-shadow: 0 0 5px rgba(60, 156, 219, 0.5);
}
button {
  padding: 8px 16px;
  margin: 6px 4px;
  border: none;
  border-radius: 20px;
  background-color: #3c9cdb;
  color: white;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.1s ease;
}

button:hover {
  background-color: #2b83c2;
  transform: scale(1.03);
}

button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
.expense-card {
  list-style: none;
  background: #f8f9fa;
  margin: 12px 0;
  padding: 12px 16px;
  border-radius: 12px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.05);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.expense-card span {
  font-weight: 500;
}
.pagination-info {
  margin-top: 20px;
  font-size: 14px;
  color: #666;
}
</style>