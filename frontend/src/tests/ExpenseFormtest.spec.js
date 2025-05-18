import { mount } from '@vue/test-utils';
import ExpenseForm from '@/components/ExpenseForm.vue'; // adjust path
import axios from 'axios';

jest.mock('axios');

// Mock axios
jest.mock('axios');

describe('ExpenseForm.vue', () => {
  let wrapper;

  beforeEach(() => {
    wrapper = mount(ExpenseForm);
    jest.spyOn(console, 'error').mockImplementation(() => {});
    localStorage.setItem('auth', btoa('user:password'));
  });

  afterEach(() => {
    console.error.mockRestore();
    jest.clearAllMocks();
  });

  it('renders form inputs correctly', () => {
    expect(wrapper.find('input[placeholder="Description"]').exists()).toBe(true);
    expect(wrapper.find('input[type="number"]').exists()).toBe(true);
    expect(wrapper.find('select').exists()).toBe(true);
    expect(wrapper.find('input[type="date"]').exists()).toBe(true);
  });

  it('emits "expense-added" event on successful submission', async () => {
    axios.post.mockResolvedValueOnce({});

    await wrapper.find('input[placeholder="Description"]').setValue('Test expense');
    await wrapper.find('input[type="number"]').setValue(100);
    await wrapper.find('select').setValue('FOOD');
    await wrapper.find('input[type="date"]').setValue('2024-05-01');
    await wrapper.find('form').trigger('submit.prevent');

    // Wait for promises to resolve
    await wrapper.vm.$nextTick();

    expect(axios.post).toHaveBeenCalledTimes(1);
    expect(wrapper.emitted('expense-added')).toBeTruthy();
  });

  it('displays error message on 400 response', async () => {
    axios.post.mockRejectedValueOnce({
      response: {
        status: 400,
        data: 'Amount must be greater than 0'
      }
    });

    await wrapper.find('input[placeholder="Description"]').setValue('Invalid expense');
    await wrapper.find('input[type="number"]').setValue(0); // invalid
    await wrapper.find('select').setValue('FOOD');
    await wrapper.find('input[type="date"]').setValue('2024-05-01');
    await wrapper.find('form').trigger('submit.prevent');
    await wrapper.vm.$nextTick();

    expect(wrapper.text()).toContain('Amount must be greater than 0');
  });

  it('handles unexpected error', async () => {
    axios.post.mockRejectedValueOnce(new Error('Server crash'));

    await wrapper.find('input[placeholder="Description"]').setValue('Any');
    await wrapper.find('input[type="number"]').setValue(50);
    await wrapper.find('select').setValue('FOOD');
    await wrapper.find('input[type="date"]').setValue('2024-05-01');
    await wrapper.find('form').trigger('submit.prevent');
    await wrapper.vm.$nextTick();

    expect(wrapper.text()).toContain('An unexpected error occurred.');
  });
});
