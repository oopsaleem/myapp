import { defineComponent, provide } from 'vue';

import EmpService from './emp/emp.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('empService', () => new EmpService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
