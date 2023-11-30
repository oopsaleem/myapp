/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Dept from './dept.vue';
import DeptService from './dept.service';
import AlertService from '@/shared/alert/alert.service';

type DeptComponentType = InstanceType<typeof Dept>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Dept Management Component', () => {
    let deptServiceStub: SinonStubbedInstance<DeptService>;
    let mountOptions: MountingOptions<DeptComponentType>['global'];

    beforeEach(() => {
      deptServiceStub = sinon.createStubInstance<DeptService>(DeptService);
      deptServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          deptService: () => deptServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        deptServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });

        // WHEN
        const wrapper = shallowMount(Dept, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(deptServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.depts[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
      });
    });
    describe('Handles', () => {
      let comp: DeptComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Dept, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        deptServiceStub.retrieve.reset();
        deptServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        deptServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 'ABC' });

        comp.removeDept();
        await comp.$nextTick(); // clear components

        // THEN
        expect(deptServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(deptServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
