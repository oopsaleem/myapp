import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

const Emp = () => import('@/entities/emp/emp.vue');
const EmpUpdate = () => import('@/entities/emp/emp-update.vue');
const EmpDetails = () => import('@/entities/emp/emp-details.vue');

const Dept = () => import('@/entities/dept/dept.vue');
const DeptUpdate = () => import('@/entities/dept/dept-update.vue');
const DeptDetails = () => import('@/entities/dept/dept-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'emp',
      name: 'Emp',
      component: Emp,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'emp/new',
      name: 'EmpCreate',
      component: EmpUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'emp/:empId/edit',
      name: 'EmpEdit',
      component: EmpUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'emp/:empId/view',
      name: 'EmpView',
      component: EmpDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'dept',
      name: 'Dept',
      component: Dept,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'dept/new',
      name: 'DeptCreate',
      component: DeptUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'dept/:deptId/edit',
      name: 'DeptEdit',
      component: DeptUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'dept/:deptId/view',
      name: 'DeptView',
      component: DeptDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
