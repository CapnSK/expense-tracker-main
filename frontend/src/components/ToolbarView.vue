<template>
    <div class="toolbar">
        <input v-model="localKeyword" placeholder="Search description" />
        <select v-model="localCategory">
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
        <select v-model="localSort">
            <option value="date,desc">Date Desc</option>
            <option value="date,asc">Date Asc</option>
            <option value="amount,desc">Amount Desc</option>
            <option value="amount,asc">Amount Asc</option>
        </select>
        <select v-model="localSize">
            <option value="5">5 / page</option>
            <option value="10">10 / page</option>
            <option value="20">20 / page</option>
        </select>

        <button @click="$emit('apply', getState())">Search</button>
        <button @click="$emit('clear')">Clear</button>
        &nbsp;
        <button @click="$emit('toggle-archived')">
            {{ showArchived ? 'Show Active' : 'Show Archived' }}
        </button>
    </div>
</template>

<script>
export default {
    props: {
        keyword: String,
        category: String,
        sort: String,
        size: Number,
        showArchived: Boolean
    },
    emits: ['apply', 'clear', 'toggle-archived'],
    data() {
        return {
            localKeyword: this.keyword,
            localCategory: this.category,
            localSort: this.sort,
            localSize: this.size
        };
    },
    watch: {
        keyword(val) { this.localKeyword = val; },
        category(val) { this.localCategory = val; },
        sort(val) { this.localSort = val; },
        size(val) { this.localSize = val; }
    },
    methods: {
        getState() {
            return {
                keyword: this.localKeyword,
                category: this.localCategory,
                sort: this.localSort,
                size: Number(this.localSize)
            };
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
</style>