<template>
  <div>
    <h2>Add Expense</h2>
    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>
    <form @submit.prevent="addExpense">
      <input v-model="description" placeholder="Description" required />
      <input v-model="amount" type="number" placeholder="Amount" required />
      <select v-model="category" required>
        <option disabled value="">Select category</option>
        <option>EDUCATION</option>
        <option>ENTERTAINMENT</option>
        <option>FOOD</option>
        <option>HEALTH</option>
        <option>HOUSEHOLD</option>
        <option>PERSONAL</option>
        <option>SHOPPING</option>
        <option>TRANSPORT</option>
        <option>TRAVEL</option>
        <option>UTILITIES</option>
        <option>WORK</option>
      </select>
      <input v-model="date" type="date" required />
      <button type="submit">Add</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      description: '',
      amount: 0,
      date: '',
      category: '',
      errorMessage: ''
    };
  },
  methods: {
    addExpense() {
      const auth = localStorage.getItem('auth');
      axios.post('http://localhost:8080/api/expenses/create', {
        description: this.description,
        amount: this.amount,
        date: this.date,
        category: this.category || null
      }, {
        headers: { 'Authorization': `Basic ${auth}` }
      })
        .then(() => {
          this.$emit('expense-added');
          this.description = '';
          this.amount = 0;
          this.date = '';
          this.category = '';
          this.errorMessage = '';
        })
        .catch(error => {
          if (error.response && error.response.status === 400) {
            this.errorMessage = error.response.data;
          } else {
            console.error(error);
            this.errorMessage = 'An unexpected error occurred.';
          }
        });
    }
  }
};
</script>

<style scoped>
.error-message {
  color: red;
  margin-bottom: 1rem;
}
</style>