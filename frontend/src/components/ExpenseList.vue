<template>
  <div>
    <h2>Expenses</h2>
    <expense-form @expense-added="fetchExpenses" />

    <!-- Toolbar (add dropdown for page size) -->
    <div class="toolbar">
      <input v-model="keyword" placeholder="Search description" />
      <select v-model="category">
        <option value="">All Categories</option>
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
      <select v-model="sort">
        <option value="date,desc">Date Desc</option>
        <option value="date,asc">Date Asc</option>
        <option value="amount,desc">Amount Desc</option>
        <option value="amount,asc">Amount Asc</option>
      </select>
      <select v-model="size" @change="page = 0; fetchExpenses()">
        <option value="5">5 / page</option>
        <option value="10">10 / page</option>
        <option value="20">20 / page</option>
      </select>
      <button @click="applyFilters">Search</button>
      <button @click="clearFilters">Clear</button>
    </div>

    <!-- Expense List -->
    <ul>
      <li v-for="expense in expenses" :key="expense.id">
        {{ expense.description }} - {{ expense.amount }} - {{ expense.date }} - {{ expense.category }}
        <button @click="deleteExpense(expense.id)">Delete</button>
      </li>
    </ul>

    <!-- Pagination -->
    <div class="pagination">
      <div>
        Showing {{ fromItem }} to {{ toItem }} of {{ totalElements }} |
        Page {{ page + 1 }} of {{ totalPages }}
      </div>
      <button :disabled="page === 0" @click="page--; fetchExpenses()">Previous</button>
      <button :disabled="!hasMore" @click="page++; fetchExpenses()">Next</button>
    </div>

    <button @click="logout">Logout</button>
  </div>
</template>

<script>
import axios from 'axios';
import ExpenseForm from './ExpenseForm.vue';

export default {
  components: { ExpenseForm },
  data() {
    return {
      expenses: [],
      keyword: '',
      category: '',
      sort: 'date,desc',
      page: 0,
      size: 5,
      hasMore: false,
      totalElements: 0,
      totalPages: 0
    };
  },
  computed: {
    fromItem() {
      return this.totalElements === 0 ? 0 : this.page * this.size + 1;
    },
    toItem() {
      return Math.min((this.page + 1) * this.size, this.totalElements);
    }
  },
  mounted() {
    this.fetchExpenses();
  },
  methods: {
    applyFilters() {
      this.page = 0;
      this.fetchExpenses();
    },
    clearFilters() {
      this.keyword = '';
      this.category = '';
      this.sort = 'date,desc';
      this.page = 0;
      this.fetchExpenses();
    },
    fetchExpenses() {
      const auth = localStorage.getItem('auth');
      axios.get('http://localhost:8080/api/expenses', {
        params: {
          keyword: this.keyword,
          category: this.category,
          page: this.page,
          size: this.size,
          sort: this.sort
        },
        headers: { 'Authorization': `Basic ${auth}` }
      })
        .then(res => {
          this.expenses = res.data.content;
          this.hasMore = !res.data.last;
          this.totalElements = res.data.totalElements;
          this.totalPages = res.data.totalPages;
        })
        .catch(err => console.error(err));
    },
    deleteExpense(id) {
      const auth = localStorage.getItem('auth');
      axios.delete(`http://localhost:8080/api/expenses/${id}`, {
        headers: { 'Authorization': `Basic ${auth}` }
      }).then(() => this.fetchExpenses())
        .catch(err => console.error(err));
    },
    logout() {
      localStorage.removeItem('auth');
      this.$router.push('/login');
    }
  }
};
</script>

<style scoped>
.toolbar {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
  align-items: center;
}

.pagination {
  margin-top: 1rem;
}
</style>
