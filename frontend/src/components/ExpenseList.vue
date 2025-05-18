<template>
  <div>
    <h1>Expenses</h1>
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

      <button @click="toggleArchived">
        {{ showArchived ? 'Show Active' : 'Show Archived' }}
      </button>
    </div>

    <!-- Expense List -->
    <ul>
      </li> -->
      <li class="expense-row" v-for="expense in expenses" :key="expense.id">
        <div class="expense-details">
          <div class="expense-name">{{ expense.description }} | <small>{{ expense.amount }} CAD</small></div>
          <div class="expense-meta">{{ expense.date }} </div>
          <span class="category-tag" :class="`category-${expense.category.toLowerCase()}`">{{ expense.category }}</span>
        </div>
        <button v-if="!showArchived" class="archive-btn" @click="archiveExpense(expense.id)">Archive</button>
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
      totalPages: 0,
      showArchived: false
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
      this.showArchived = false;
      this.fetchExpenses();
    },
    toggleArchived() {
      this.showArchived = !this.showArchived;
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
          sort: this.sort,
          archived: this.showArchived
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
    archiveExpense(id) {
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

.expense-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 18px;
  background-color: #f9f9f9;
  border-bottom: 1px solid #e0e0e0;
  border-radius: 10px;
  margin-bottom: 10px;
  font-size: 15px;
}

.expense-details {
  display: flex;
  flex-direction: column;
}

.expense-name {
  font-weight: 600;
  font-size: 16px;
  color: #2c3e50;
}

.expense-meta {
  color: #666;
  font-size: 14px;
  margin-top: 4px;
}

h1 {
  margin-left: 45%;
}

.archive-btn {
  padding: 6px 14px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 9999px;
  font-size: 13px;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.2s ease-in-out;
}

.archive-btn:hover {
  background-color: #217dbb;
}

.category-tag {
  padding: 4px 8px;
  border-radius: 4px;
  color: white;
  font-weight: 600;
  font-size: 0.85rem;
  display: inline-block;
  width: fit-content;
}

/* Category color classes */
.category-education {
  background-color: #4caf50; /* green */
}

.category-entertainment {
  background-color: #ff9800; /* orange */
}

.category-food {
  background-color: #f44336; /* red */
}

.category-health {
  background-color: #009688; /* teal */
}

.category-household {
  background-color: #795548; /* brown */
}

.category-personal {
  background-color: #9c27b0; /* purple */
}

.category-shopping {
  background-color: #3f51b5; /* indigo */
}

.category-transport {
  background-color: #607d8b; /* blue-grey */
}

.category-travel {
  background-color: #03a9f4; /* light blue */
}

.category-utilities {
  background-color: #ff5722; /* deep orange */
}

.category-work {
  background-color: #673ab7; /* deep purple */
}

</style>
