/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import Emp from './emp.vue';
import EmpService from './emp.service';
import AlertService from '@/shared/alert/alert.service';

type EmpComponentType = InstanceType<typeof Emp>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('Emp Management Component', () => {
    let empServiceStub: SinonStubbedInstance<EmpService>;
    let mountOptions: MountingOptions<EmpComponentType>['global'];

    beforeEach(() => {
      empServiceStub = sinon.createStubInstance<EmpService>(EmpService);
      empServiceStub.retrieve.resolves({ headers: {} });

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
          empService: () => empServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        empServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 'ABC' }] });

        // WHEN
        const wrapper = shallowMount(Emp, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(empServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.emps[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
      });
    });
    describe('Handles', () => {
      let comp: EmpComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(Emp, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        empServiceStub.retrieve.reset();
        empServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        empServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 'ABC' });

        comp.removeEmp();
        await comp.$nextTick(); // clear components

        // THEN
        expect(empServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(empServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
